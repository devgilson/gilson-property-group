package com.example.breken30daysback.Service;


import com.example.breken30daysback.Models.Property;
import com.example.breken30daysback.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

@Service
public class ZohoCRMService {

    @Value("${zoho.client.id}")
    private String clientId;

    @Value("${zoho.client.secret}")
    private String clientSecret;

    @Value("${zoho.refresh.token}")
    private String refreshToken;

    private final PropertyRepository propertyRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ZohoAuthService zohoAuthService;

    private final String tokenUrl = "https://accounts.zoho.com/oauth/v2/token";
    private final String apiBaseUrl = "https://www.zohoapis.com/crm/v7/";

    public ZohoCRMService(PropertyRepository propertyRepository, ZohoAuthService zohoAuthService) {
        this.propertyRepository = propertyRepository;
        this.zohoAuthService = zohoAuthService;
    }

    /**
     * Fetch a new access token using the refresh token.
     */
//    public String getAccessToken() {
//        String url = tokenUrl + "?grant_type=refresh_token" +
//                "&client_id=" + clientId +
//                "&client_secret=" + clientSecret +
//                "&refresh_token=" + refreshToken;
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
//        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//            return response.getBody().get("access_token").toString();
//        }
//        throw new RuntimeException("Failed to get access token from Zoho.");
//    }

    /**
     * Check if a property already exists in Zoho CRM.
     */
    public boolean propertyExistsInZoho(String propertyId) {
        String accessToken = zohoAuthService.getAccessToken();
        String url = apiBaseUrl + "Properties/search?criteria=(Property_ID:equals:" + propertyId + ")";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            return data != null && !data.isEmpty();
        }
        return false;
    }

    /**
     * Sync properties from MySQL to Zoho CRM.
     */
    public List<String> syncPropertiesToZoho() {
        List<Property> properties = propertyRepository.findAll();
        List<String> syncedPropertyIds = new ArrayList<>();

        for (Property property : properties) {
            if (propertyExistsInZoho(property.getPropertyId())) {
                System.out.println("Skipping property: " + property.getPropertyId() + " (Already exists in Zoho)");
                continue;
            }

            // Prepare property data for Zoho CRM
            Map<String, Object> propertyData = new HashMap<>();
            propertyData.put("Property_ID", property.getPropertyId());
            propertyData.put("Name", property.getName());
            propertyData.put("City", property.getCity());
            propertyData.put("State", property.getState());
            propertyData.put("Country", property.getCountry());
            propertyData.put("Latitude", property.getLatitude() != null ? Math.round(property.getLatitude() * 100.0) / 100.0 : null);
            propertyData.put("Longitude", property.getLongitude() != null ? Math.round(property.getLongitude() * 100.0) / 100.0 : null);
            propertyData.put("Events_Allowed", property.getEventsAllowed());
            propertyData.put("Pets_Allowed", property.getPetsAllowed());
            propertyData.put("Smoking_Allowed", property.getSmokingAllowed());
            propertyData.put("Street", property.getStreet());
            propertyData.put("Timezone", property.getTimezone());


            boolean success = false;
            int retryCount = 0;

            while (!success && retryCount < 5) {
                try {
                    createProperty(propertyData);
                    syncedPropertyIds.add(property.getPropertyId());
                    success = true;

                    // ✅ Introduce a delay to prevent rate limiting
                    Thread.sleep(1000); // 1-second delay

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS || e.getMessage().contains("too many requests")) {
                        System.out.println("Rate limit hit! Retrying in 10 seconds...");
                        retryCount++;
                        try {
                            Thread.sleep(10000); // Wait 10 seconds before retrying
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        System.err.println("Failed to sync property: " + e.getMessage());
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return syncedPropertyIds;
    }
    /**
     * Create a Property record in Zoho CRM.
     */
    public String createProperty(Map<String, Object> propertyData) {
        String accessToken = zohoAuthService.getAccessToken();
        String url = apiBaseUrl + "Properties"; // Zoho CRM module

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("data", new Object[]{propertyData});

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }
}