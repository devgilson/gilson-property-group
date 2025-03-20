package com.example.breken30daysback.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id") // Explicitly map to the `reservation_id` column
    private String reservationId; // Foreign key to Reservation

    private String status; // e.g., "accepted", "cancelled"
    private String statusCategory; // e.g., "request", "booking"
    private String statusSubCategory; // e.g., "pending verification"
    private LocalDateTime changedAt; // Timestamp of the status change

    @ManyToOne
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false) // Reference the same column
    private Reservation reservation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(String statusCategory) {
        this.statusCategory = statusCategory;
    }

    public String getStatusSubCategory() {
        return statusSubCategory;
    }

    public void setStatusSubCategory(String statusSubCategory) {
        this.statusSubCategory = statusSubCategory;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}