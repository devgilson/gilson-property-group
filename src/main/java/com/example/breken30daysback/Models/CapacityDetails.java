package com.example.breken30daysback.Models;

import com.example.breken30daysback.Entity.Reservation;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "capacity_details")
public class CapacityDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "maximum_guest_count")
    private int maximumGuestCount;

    @Column(name = "adult_count")
    private int adultCount;

    @Column(name = "child_count")
    private int childCount;

    @Column(name = "infant_count")
    private int infantCount;

    @Column(name = "pet_count")
    private int petCount;

    @Column(name = "number_of_bedrooms")
    private int numberOfBedrooms;

    @Column(name = "number_of_bathrooms")
    private int numberOfBathrooms;

    @Column(name = "square_footage")
    private Integer squareFootage;

    @Column(name = "garage_spaces")
    private Integer garageSpaces;

    @Column(name = "parking_spaces")
    private Integer parkingSpaces;

    @Column(name = "total_bed_count")
    private int totalBedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public int getMaximumGuestCount() {
        return maximumGuestCount;
    }

    public void setMaximumGuestCount(int maximumGuestCount) {
        this.maximumGuestCount = maximumGuestCount;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getInfantCount() {
        return infantCount;
    }

    public void setInfantCount(int infantCount) {
        this.infantCount = infantCount;
    }

    public int getPetCount() {
        return petCount;
    }

    public void setPetCount(int petCount) {
        this.petCount = petCount;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public Integer getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(Integer squareFootage) {
        this.squareFootage = squareFootage;
    }

    public Integer getGarageSpaces() {
        return garageSpaces;
    }

    public void setGarageSpaces(Integer garageSpaces) {
        this.garageSpaces = garageSpaces;
    }

    public Integer getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(Integer parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public int getTotalBedCount() {
        return totalBedCount;
    }

    public void setTotalBedCount(int totalBedCount) {
        this.totalBedCount = totalBedCount;
    }
}
