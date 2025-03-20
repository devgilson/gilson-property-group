package com.example.breken30daysback.Service;

import com.example.breken30daysback.Entity.Reservation;
import com.example.breken30daysback.Entity.ReservationHistory;
import com.example.breken30daysback.Repository.ReservationHistoryRepository;
import com.example.breken30daysback.Models.GuestDetailsDTO;
import com.example.breken30daysback.Models.Property;
import com.example.breken30daysback.Models.ReservationDTO;
import com.example.breken30daysback.Repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

    @Service
    public class ReservationService {
        private final RestTemplate restTemplate;
        private final ObjectMapper objectMapper;
        private final ReservationRepository reservationRepository;
        private final GuestFinancialsService guestFinancialsService;
        private final HostFinancialsService hostFinancialsService;
        private final CapacityDetailsService capacityDetailsService;

        private final PropertyRepository propertyRepository;

        private final ReservationHistoryRepository reservationHistoryRepository;

        private static final String API_URL = "https://public.api.hospitable.com/v2/reservations?page=";

        private static final String API_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5YTYyNGRmMC0xMmYxLTQ0OGUtYjg4NC00MzY3ODBhNWQzY2QiLCJqdGkiOiIwY2ZmODZiMjRlYTZjN2E0NzhiYTZmYmRhNDg1YjY0ZTE5ZWZlYjk0NjI5YjdiZjI1MzljZTJhZWIzMzUxZWQ1ODQ2NzIyMTg5MzUzNzhjYSIsImlhdCI6MTczMzc2MDY0OS44OTk0NTEsIm5iZiI6MTczMzc2MDY0OS44OTk0NTUsImV4cCI6MTc2NTI5NjY0OS44OTMzMjcsInN1YiI6IjEwMTE1NiIsInNjb3BlcyI6WyJwYXQ6cmVhZCIsInBhdDp3cml0ZSJdfQ.eYTPsKJ3gGZFj6CgU6UvKUAE06kwehLh6h9y9J5E4d2w9XNGXIOz8GJGfxG1KzPIyePzbUpRXnztjEL4rciGMvzInzowkCNjR4PO44BLvrDQUC6KDIIwPFC8Tt6oz8SroiJie94j7z2vGcq1kvUn1VXtgv8qAd02m308Qs7XyLedAKuyQN7Ni9Itb6iqtXrQ2B4YpNheR6l2mmmzoiqLX_LKHYsEGIPZv5EIGJYRKckABZ8Zk0tJLwOUtS-CyXDeYFQbv38dzd4-dI3KY5blRBR9JoUZUWfrAQ3blf0yhMASXYrXyUrGHaomfjpwyk479abV6BoFNDvNzHpbX_ZOTLOTtus-13b-TilLK0WXxt6QtmmqjvTj5IIFRSSuSJ7li_A8r3BSybpucjsvLAHi3Y-ZjjTZrGCT5INW7IJzCF_A-8thF0LweEh48_h0bD074kf13tRdxJDqP_mU0fIkCk8TWYpX9jeqcMCVQd7DSVLW5RC3teu4rqPAuxbqjjaLS_KODeAjx_qDQcQXujEs7ErHdh2E9ReQn6AWgfTGhuLIpghkAnzCtQAAYu0Zb8sM3bgHsoBQvwbiZYy1TfHugJDBpDh1SbaGVc016lGQAE-_v3uw2NUjzSbpVKeVWOV2yYYsryJ21vuyda_LlO1VdfZJV_s_A07i-9qlQpaz9X8";

        private static final List<String> PROPERTY_IDS = Arrays.asList(
                "f044df90-ec45-4f83-a143-336ad5563053",
                "5ed2c822-257c-4e08-bb12-aeabce726d0a",
                "566e6845-492e-4d6c-84de-f7e7e0e9bf11",
                "931f82b9-86ae-4889-8e17-d941cee27de5",
                "2d7f69c8-76cb-4e98-8426-7378b5fe2fdd",
                "06c75ff4-f2b4-4b5c-8049-8873b5696cc2",
                "70fc6d1b-d40e-4759-b2c0-ad95c24e1276",
                "5e0cd3eb-9d7f-4980-be1e-11d0fb05b432",
                "25b64966-9f7c-4584-94fe-d702f0c748a4",
                "dd974412-3c60-47b2-ae3d-96d7b62847c0",
                "26156e2d-9d4e-4387-9561-8e867115ccc7",
                "80a4b684-6bd3-42ac-aa46-1096563b176e",
                "1eabd560-52d8-4286-a34d-cd218d8be03f",
                "92a851e5-f15b-4def-9ee7-137d3cfcbec4",
                "d20af906-6bdf-4c0a-a091-5298097c9985",
                "2b21a1cd-4f9b-40ad-8c67-1a9f97c98ba7",
                "fccdf896-9c68-40e9-ac0d-61e980ad00ae",
                "2c06debf-9832-42e7-91ff-22822a2438fa",
                "ac702554-6887-44f1-9756-e7d37631237c",
                "328af2ea-4343-4de4-80a0-8330ce5a0c3d",
                "bbb5353e-1562-4241-bb31-38be2a9eb4ad",
                "aa535ffa-ec6d-43d6-bc2b-db9c3885862c",
                "d357e435-10bc-40f1-8acb-14893f14a00c",
                "b010629e-7fbf-4635-9996-01be1c6831cd",
                "e4571cd9-09ba-4cc4-af36-c73dc9003af7",
                "81de4e76-58fc-466f-bdb7-284c19c98c08",
                "fd5bf89f-fa9a-439c-9b8d-c67620ff2ad7",
                "adb8dc6f-8403-4982-9ce7-72c2e9ec49a7",
                "6c897dee-ee4b-4e34-a78c-d86d09c46a86",
                "d054fd25-c088-4279-b141-6a62a7e44b13"
        );
        private static final HttpHeaders HEADERS = new HttpHeaders();
        private static final int API_CALL_LIMIT = 50;
        private static final int RETRY_DELAY = 30000;

        static {
            HEADERS.set("Authorization", API_TOKEN);
            HEADERS.setContentType(MediaType.APPLICATION_JSON);
        }

        // ✅ Scheduled job to run at 3:00 AM every day
        @Scheduled(cron = "0 0 3 * * ?")
        public void scheduleReservationSync() {
            System.out.println("🔄 Running scheduled reservation sync at 3:00 AM...");
            fetchAndSaveReservations();
        }

        public ReservationService(RestTemplate restTemplate, ObjectMapper objectMapper,
                                  ReservationRepository reservationRepository,
                                  GuestFinancialsService guestFinancialsService,
                                  HostFinancialsService hostFinancialsService,
                                  CapacityDetailsService capacityDetailsService,
                                  ReservationHistoryRepository reservationHistoryRepository,
                                  PropertyRepository propertyRepository) {
            this.restTemplate = restTemplate;
            this.objectMapper = objectMapper;
            this.reservationRepository = reservationRepository;
            this.guestFinancialsService = guestFinancialsService;
            this.hostFinancialsService = hostFinancialsService;
            this.capacityDetailsService = capacityDetailsService;
            this.reservationHistoryRepository = reservationHistoryRepository;
            this.propertyRepository = propertyRepository;
        }

        public void fetchAndSaveReservations() {
            int page = 1;
            int lastPage = Integer.MAX_VALUE;
            int requestCount = 0;

            while (page <= lastPage) {
                try {
                    // ✅ **Rate Limiting: Stop after 50 requests & introduce delay**
                    if (requestCount >= API_CALL_LIMIT) {
                        System.out.println("⚠️ API rate limit reached. Pausing for 30 seconds...");
                        Thread.sleep(RETRY_DELAY);
                        requestCount = 0;
                    }

                    // ✅ **Create request body**
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("properties", PROPERTY_IDS);
                    requestBody.put("start_date", "2023-01-01");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    requestBody.put("end_date", LocalDateTime.now().format(formatter));

                    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, HEADERS);

                    System.out.println("\n📌 Fetching Page: " + page);
                    ResponseEntity<String> response = restTemplate.exchange(
                            API_URL + page + "&include=properties,financials",
                            HttpMethod.GET,
                            requestEntity,
                            String.class
                    );

                    // ✅ **Process API Response**
                    JsonNode rootNode = objectMapper.readTree(response.getBody());
                    JsonNode reservationsNode = rootNode.path("data");

                    if (!reservationsNode.isArray() || reservationsNode.isEmpty()) {
                        System.out.println("✅ No more data. Stopping...");
                        break;
                    }

                    if (rootNode.has("meta") && rootNode.path("meta").has("last_page")) {
                        lastPage = rootNode.path("meta").get("last_page").asInt();
                    }

                    for (JsonNode resNode : reservationsNode) {
                        ReservationDTO reservationDTO = objectMapper.treeToValue(resNode, ReservationDTO.class);
                        processReservation(reservationDTO);
                    }

                    page++; // Move to the next page
                    requestCount++; // Increase API call counter

                } catch (HttpServerErrorException e) {
                    System.out.println("🚨 Server error (500). Retrying after 10 seconds...");
                    sleep(10000);
                } catch (ResourceAccessException e) {
                    System.out.println("🚨 Connection closed (GOAWAY). Retrying after 15 seconds...");
                    sleep(15000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        @Transactional
        public void processReservation(ReservationDTO dto) {
            String reservationId = dto.getId();

            // Check if the reservation already exists
            Optional<Reservation> existingReservationOpt = reservationRepository.findById(reservationId);

            if (existingReservationOpt.isPresent()) {
                // ✅ Reservation exists, update it
                Reservation existingReservation = existingReservationOpt.get();
                updateReservation(existingReservation, dto);
                System.out.println("🔄 Updated existing reservation: " + reservationId);
            } else {
                // ✅ Reservation does not exist, create a new one
                Reservation newReservation = saveReservation(dto);
                if (newReservation == null) return;

                guestFinancialsService.processGuestFinancials(dto, newReservation);
                hostFinancialsService.processHostFinancials(dto, newReservation);
                capacityDetailsService.processCapacityDetails(dto, newReservation);
                System.out.println("✅ Created new reservation: " + reservationId);
            }
        }

        @Transactional
        public void updateReservation(Reservation reservation, ReservationDTO dto) {
            // Update reservation fields
            reservation.setCode(dto.getCode());
            reservation.setPlatform(dto.getPlatform());
            reservation.setPlatformId(dto.getPlatformId());
            reservation.setBookingDate(dto.getBookingDate());
            reservation.setArrivalDate(dto.getArrivalDate());
            reservation.setDepartureDate(dto.getDepartureDate());
            reservation.setCheckIn(dto.getCheckIn());
            reservation.setCheckOut(dto.getCheckOut());
            reservation.setNights(dto.getNights());

            // Update status fields
            Map<String, Object> reservationStatus = dto.getReservationStatus();
            if (reservationStatus != null) {
                Map<String, Object> currentStatus = (Map<String, Object>) reservationStatus.get("current");
                if (currentStatus != null) {
                    reservation.setStatus((String) currentStatus.get("category"));
                    reservation.setStatusCategory((String) currentStatus.get("category"));
                    reservation.setStatusSubCategory((String) currentStatus.get("sub_category"));
                }
            }

            // Save the updated reservation
            reservationRepository.save(reservation);

            // Add a new entry to the reservation history
            ReservationHistory historyEntry = new ReservationHistory();
            historyEntry.setReservationId(reservation.getId());
            historyEntry.setStatus(reservation.getStatus());
            historyEntry.setStatusCategory(reservation.getStatusCategory());
            historyEntry.setStatusSubCategory(reservation.getStatusSubCategory());
            historyEntry.setChangedAt(LocalDateTime.now());
            reservationHistoryRepository.save(historyEntry);

            // Update financials and capacity details
            guestFinancialsService.processGuestFinancials(dto, reservation);
            hostFinancialsService.processHostFinancials(dto, reservation);
            capacityDetailsService.processCapacityDetails(dto, reservation);
        }

        @Transactional
        public Reservation saveReservation(ReservationDTO dto) {
            String reservationId = dto.getId();

            // ✅ Ensure at least one property exists
            if (dto.getProperties() == null || dto.getProperties().isEmpty()) {
                //System.out.println("🚨 Reservation " + reservationId + " has no properties. Skipping...");
                return null;
            }

            // ✅ Extract property ID from DTO
            String propertyId = dto.getProperties().get(0).get("id").toString().replace("\"", "");

            // ✅ Fetch property from DB
            Optional<Property> optionalProperty = propertyRepository.findByPropertyId(propertyId);
            if (optionalProperty.isEmpty()) {
               // System.out.println("🚨 Property " + propertyId + " not found for reservation " + reservationId);
                return null; // Don't save if property doesn't exist
            }

            ObjectMapper objectMapper = new ObjectMapper();

            // ✅ Create and Save Reservation
            Reservation reservation = new Reservation();
            reservation.setId(dto.getId());
            reservation.setCode(dto.getCode());
            reservation.setPlatform(dto.getPlatform());
            reservation.setPlatformId(dto.getPlatformId());
            reservation.setBookingDate(dto.getBookingDate());
            reservation.setArrivalDate(dto.getArrivalDate());
            reservation.setDepartureDate(dto.getDepartureDate());
            reservation.setCheckIn(dto.getCheckIn());
            reservation.setCheckOut(dto.getCheckOut());
            reservation.setNights(dto.getNights());
            GuestDetailsDTO guestDetails = dto.getGuests();
            if (guestDetails != null) {  // ✅ Prevent NullPointerException
                reservation.setAdultCount(guestDetails.getAdultCount());
                reservation.setChildCount(guestDetails.getChildCount());
                reservation.setInfantCount(guestDetails.getInfantCount());
                reservation.setPetCount(guestDetails.getPetCount());
            } else {
                reservation.setAdultCount(0);
                reservation.setChildCount(0);
                reservation.setInfantCount(0);
                reservation.setPetCount(0);
            }

            //reservation.setGuests(dto.getGuests().toString());
            reservation.setProperty(optionalProperty.get()); // ✅ Set the property

            return reservationRepository.save(reservation);
        }


        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
