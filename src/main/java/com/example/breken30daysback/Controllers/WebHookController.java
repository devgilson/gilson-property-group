package com.example.breken30daysback.Controllers;

import com.example.breken30daysback.Models.GuestDetailsDTO;
import com.example.breken30daysback.Models.ReservationDTO;
import com.example.breken30daysback.Service.ReservationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebHookController {
    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;


    public WebHookController(ReservationService reservationService, ObjectMapper objectMapper) {
        this.reservationService = reservationService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/hospitable")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("🚀 Webhook Received: " + payload);

            // Convert payload into JSON format
            JsonNode jsonNode = objectMapper.valueToTree(payload);

            // Extract reservation data
            JsonNode reservationNode = jsonNode.path("data");
            if (reservationNode.isMissingNode()) {
                return ResponseEntity.badRequest().body("Invalid webhook payload!");
            }

            // Extract guests data
            JsonNode guestsNode = reservationNode.path("guests");
            if (!guestsNode.isMissingNode()) {
                // Map guests data to GuestDetailsDTO
                GuestDetailsDTO guestDetails = objectMapper.treeToValue(guestsNode, GuestDetailsDTO.class);
                // Create ReservationDTO and set guests
                ReservationDTO reservationDTO = objectMapper.treeToValue(reservationNode, ReservationDTO.class);
                reservationDTO.setGuests(guestDetails);
                // Process reservation
                reservationService.processReservation(reservationDTO);
            } else {
                System.out.println("⚠️ No guests data found in webhook payload.");
                return ResponseEntity.badRequest().body("No guests data found!");
            }

            return ResponseEntity.ok("✅ Webhook Processed Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Webhook Processing Failed!");
        }
    }
}
