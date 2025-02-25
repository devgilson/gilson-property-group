package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.Reservation;
import com.example.breken30daysback.Models.CapacityDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CapacityDetailsRepository extends JpaRepository<CapacityDetails, Long> {
    Optional<CapacityDetails> findByReservation(Reservation reservation);
    boolean existsByReservationId(String reservationId);
}
