package com.example.breken30daysback.Controllers;

import com.example.breken30daysback.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/capacity")
public class CapacityDetailsController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/sync")
    public ResponseEntity<String> syncCapacityDetails() {
        try {
            reservationService.fetchAndSaveReservations();
            return ResponseEntity.ok("Capacity details synchronized successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to sync capacity details: " + e.getMessage());
        }
    }
}
