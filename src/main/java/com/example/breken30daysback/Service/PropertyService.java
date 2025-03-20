package com.example.breken30daysback.Service;

import com.example.breken30daysback.Models.ListingDetails;
import com.example.breken30daysback.Models.Property;
import com.example.breken30daysback.Models.Amenities;
import com.example.breken30daysback.Repository.ListingDetailsRepository;
import com.example.breken30daysback.Repository.PropertyRepository;
import com.example.breken30daysback.Repository.AmenitiesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Service
public class PropertyService {

    private static final String HOSPITABLE_API_URL = "https://public.api.hospitable.com/v2/properties";
    private static final String AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5YTYyNGRmMC0xMmYxLTQ0OGUtYjg4NC00MzY3ODBhNWQzY2QiLCJqdGkiOiIwY2ZmODZiMjRlYTZjN2E0NzhiYTZmYmRhNDg1YjY0ZTE5ZWZlYjk0NjI5YjdiZjI1MzljZTJhZWIzMzUxZWQ1ODQ2NzIyMTg5MzUzNzhjYSIsImlhdCI6MTczMzc2MDY0OS44OTk0NTEsIm5iZiI6MTczMzc2MDY0OS44OTk0NTUsImV4cCI6MTc2NTI5NjY0OS44OTMzMjcsInN1YiI6IjEwMTE1NiIsInNjb3BlcyI6WyJwYXQ6cmVhZCIsInBhdDp3cml0ZSJdfQ.eYTPsKJ3gGZFj6CgU6UvKUAE06kwehLh6h9y9J5E4d2w9XNGXIOz8GJGfxG1KzPIyePzbUpRXnztjEL4rciGMvzInzowkCNjR4PO44BLvrDQUC6KDIIwPFC8Tt6oz8SroiJie94j7z2vGcq1kvUn1VXtgv8qAd02m308Qs7XyLedAKuyQN7Ni9Itb6iqtXrQ2B4YpNheR6l2mmmzoiqLX_LKHYsEGIPZv5EIGJYRKckABZ8Zk0tJLwOUtS-CyXDeYFQbv38dzd4-dI3KY5blRBR9JoUZUWfrAQ3blf0yhMASXYrXyUrGHaomfjpwyk479abV6BoFNDvNzHpbX_ZOTLOTtus-13b-TilLK0WXxt6QtmmqjvTj5IIFRSSuSJ7li_A8r3BSybpucjsvLAHi3Y-ZjjTZrGCT5INW7IJzCF_A-8thF0LweEh48_h0bD074kf13tRdxJDqP_mU0fIkCk8TWYpX9jeqcMCVQd7DSVLW5RC3teu4rqPAuxbqjjaLS_KODeAjx_qDQcQXujEs7ErHdh2E9ReQn6AWgfTGhuLIpghkAnzCtQAAYu0Zb8sM3bgHsoBQvwbiZYy1TfHugJDBpDh1SbaGVc016lGQAE-_v3uw2NUjzSbpVKeVWOV2yYYsryJ21vuyda_LlO1VdfZJV_s_A07i-9qlQpaz9X8";
    private static final int PAGE_SIZE = 100;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ListingDetailsRepository listingDetailsRepository;

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @Autowired
    private ZohoAuthService zohoAuthService;

    @Value("${zoho.crm.api.url.properties}")
    private String crmApiUrl;


    @Scheduled(cron = "0 0 1 * * ?")  // Runs every day at 1 AM
    public void schedulePropertySync() {
        System.out.println("Scheduled Property Sync started at 1 AM...");
        syncProperties();
    }

    public void syncProperties() {
        try {
            System.out.println("Starting property sync...");
            fetchAndSaveProperties();
            System.out.println("Properties synced successfully.");
        } catch (Exception e) {
            System.err.println("Failed to sync properties: " + e.getMessage());
        }
    }

    private void fetchAndSaveProperties() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        int page = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", AUTH_TOKEN);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        while (true) {
            String url = HOSPITABLE_API_URL + "?page=" + page + "&limit=" + PAGE_SIZE;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode data = root.get("data");

                if (data == null || !data.isArray() || data.isEmpty()) {
                    System.out.println("No more data to process. Exiting loop.");
                    break;
                }

                for (JsonNode propertyNode : data) {
                    try {
                        String propertyId = propertyNode.has("id") ? propertyNode.get("id").asText() : null;
                        if (propertyId == null) continue;

                        // ✅ Check if property already exists, if yes, skip
                        Optional<Property> existingProperty = propertyRepository.findByPropertyId(propertyId);
                        if (existingProperty.isPresent()) {
                            System.out.println("Skipping existing property: " + propertyId);
                            continue;
                        }

                        // ✅ Save new property
                        Property property = extractProperty(propertyNode);
                        propertyRepository.save(property);
                        syncAmenities(propertyNode, property);

                        // ✅ Save corresponding ListingDetails



                        ListingDetails listingDetails = extractListingDetails(propertyNode, property);
                        listingDetailsRepository.save(listingDetails);

                        syncPropertyToCRM(property, listingDetails);

                    } catch (Exception ex) {
                        System.err.println("Failed to process property: " + propertyNode.toString());
                        ex.printStackTrace();
                    }
                }

                JsonNode linksNode = root.get("links");
                if (linksNode == null || linksNode.get("next") == null || linksNode.get("next").isNull()) {
                    System.out.println("No more pages to process. Exiting loop.");
                    break;
                }

                page++;
            } else {
                throw new Exception("Failed to fetch properties: HTTP " + response.getStatusCode());
            }
        }
    }

    //Rework on zoho crm commented for now to continue with Analytics

//    private void syncPropertyToCRM(Property property, ListingDetails listingDetails) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization",  zohoAuthService.getAccessToken());
//        headers.set("Content-Type", "application/json");
//
//        String requestBody = "{\"data\":[{"
//                + "\"PropertyID\": \"" + property.getPropertyId() + "\", "
//                + "\"Name\": \"" + property.getName() + "\", "
//                + "\"City\": \"" + property.getCity() + "\", "
//                + "\"State\": \"" + property.getState() + "\", "
//                + "\"Country\": \"" + property.getCountry() + "\", "
//                + "\"Latitude\": " + property.getLatitude() + ", "
//                + "\"Longitude\": " + property.getLongitude() + "}]}";
//
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> response = restTemplate.exchange(crmApiUrl, HttpMethod.POST, entity, String.class);
//
//        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
//            System.out.println("Successfully synced property to CRM: " + property.getPropertyId());
//        } else {
//            System.err.println("Failed to sync property to CRM: HTTP " + response.getStatusCode());
//        }
//    }

    private Property extractProperty(JsonNode propertyNode) {
        Property property = new Property();
        property.setPropertyId(propertyNode.has("id") ? propertyNode.get("id").asText() : null);
        property.setName(propertyNode.has("name") ? propertyNode.get("name").asText() : null);
        property.setTimezone(propertyNode.has("timezone") ? propertyNode.get("timezone").asText() : null);

        JsonNode addressNode = propertyNode.get("address");
        if (addressNode != null) {
            property.setCity(addressNode.has("city") ? addressNode.get("city").asText() : null);
            property.setCountry(addressNode.has("country") ? addressNode.get("country").asText() : null);
            property.setCountryName(addressNode.has("country_name") ? addressNode.get("country_name").asText() : null);
            property.setState(addressNode.has("state") ? addressNode.get("state").asText() : null);
            property.setStreet(addressNode.has("street") ? addressNode.get("street").asText() : null);

            JsonNode coordinatesNode = addressNode.get("coordinates");
            if (coordinatesNode != null) {
                property.setLatitude(coordinatesNode.has("latitude") ? coordinatesNode.get("latitude").asDouble() : null);
                property.setLongitude(coordinatesNode.has("longitude") ? coordinatesNode.get("longitude").asDouble() : null);
            }
        }

        // Extract house rules
        JsonNode houseRulesNode = propertyNode.get("house_rules");
        if (houseRulesNode != null) {
            property.setEventsAllowed(houseRulesNode.has("events_allowed") ? houseRulesNode.get("events_allowed").asBoolean() : null);
            property.setPetsAllowed(houseRulesNode.has("pets_allowed") ? houseRulesNode.get("pets_allowed").asBoolean() : null);
            property.setSmokingAllowed(houseRulesNode.has("smoking_allowed") ? houseRulesNode.get("smoking_allowed").asBoolean() : null);
        }

        Optional<Property> existingProperty = propertyRepository.findByPropertyId(property.getPropertyId());
        if (existingProperty.isPresent()) {
            property.setCleaningExpenses(existingProperty.get().getCleaningExpenses());
        }

        return property;
    }

    private ListingDetails extractListingDetails(JsonNode propertyNode, Property property) {
        ListingDetails listingDetails = new ListingDetails();
        listingDetails.setProperty(property);
        listingDetails.setPropertyType(propertyNode.has("property_type") ? propertyNode.get("property_type").asText() : null);
        listingDetails.setStandardCheckInTime(propertyNode.has("checkin") ? propertyNode.get("checkin").asText() : null);
        listingDetails.setStandardCheckOutTime(propertyNode.has("checkout") ? propertyNode.get("checkout").asText() : null);
        listingDetails.setSummary(propertyNode.has("summary") ? propertyNode.get("summary").asText() : null);
        listingDetails.setDescription(propertyNode.has("description") ? propertyNode.get("description").asText() : null);
        listingDetails.setSupportPageLink(null); // Update if a support page link exists

        JsonNode addressNode = propertyNode.get("address");
        if (addressNode != null) {
            listingDetails.setPropertyAddress(addressNode.toString());
        }

        JsonNode capacityNode = propertyNode.get("capacity");
        JsonNode roomDetailsNode = propertyNode.get("room_details");
        if (capacityNode != null || roomDetailsNode != null) {
            listingDetails.setRoomDetails("{\"capacity\":" + capacityNode + ", \"room_details\":" + roomDetailsNode + "}");
        }

        return listingDetails;
    }

    private void syncAmenities(JsonNode propertyNode, Property property) {
        JsonNode amenitiesNode = propertyNode.get("amenities");

        if (amenitiesNode != null && amenitiesNode.isArray()) {
            for (JsonNode amenityNode : amenitiesNode) {
                String item = amenityNode.asText();

                Optional<Amenities> existingAmenity = amenitiesRepository.findByPropertyAndItem(property, item);
                if (existingAmenity.isPresent()) {
                    // If amenity exists, increment count
                    Amenities amenity = existingAmenity.get();
                    amenity.setCount(amenity.getCount() + 1);
                    amenitiesRepository.save(amenity);
                } else {
                    // If new amenity, create entry with count 1
                    Amenities amenity = new Amenities();
                    amenity.setProperty(property);
                    amenity.setItem(item);
                    amenity.setCount(1);
                    amenitiesRepository.save(amenity);
                }
            }
        }
    }
}
