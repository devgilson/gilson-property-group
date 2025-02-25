package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}