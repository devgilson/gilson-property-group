package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.HostFinancials;
import com.example.breken30daysback.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostFinancialsRepository extends JpaRepository<HostFinancials, Long> {
    Optional<HostFinancials> findByReservation(Reservation reservation);
    boolean existsByReservationId(String reservationId);
}
