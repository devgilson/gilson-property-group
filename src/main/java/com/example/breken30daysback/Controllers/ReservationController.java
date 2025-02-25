package com.example.breken30daysback.Controllers;

import com.example.breken30daysback.Service.ReservationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/fetch")
    public String fetchReservations() {
        reservationService.fetchAndSaveReservations();
        //reservationService.fetchAndPrintFullResponse();
        return "Reservations fetched successfully!";
    }
}

