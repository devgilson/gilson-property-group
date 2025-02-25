package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.GuestFinancials;
import com.example.breken30daysback.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestFinancialsRepository extends JpaRepository<GuestFinancials, Long> {
    Optional<GuestFinancials> findByReservation(Reservation reservation);
    boolean existsByReservationId(String reservationId);
}
