package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
    List<ReservationHistory> findByReservationId(String reservationId);
}