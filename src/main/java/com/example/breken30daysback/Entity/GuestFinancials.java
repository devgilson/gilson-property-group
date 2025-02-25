package com.example.breken30daysback.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "guest_financials")
public class GuestFinancials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private String currency;
    private double accommodation;
    private double averageNightlyRate;
    private double totalPrice;

    // Fees
    private double cleaningFee;
    private double managementFee;
    private double communityFee;
    private double guestServiceFee;

    // Taxes
    private double accommodationsTax;
    private double localSalesTax;
    private double generalSalesTax;
    private double summitSalesTax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(double accommodation) {
        this.accommodation = accommodation;
    }

    public double getAverageNightlyRate() {
        return averageNightlyRate;
    }

    public void setAverageNightlyRate(double averageNightlyRate) {
        this.averageNightlyRate = averageNightlyRate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getCleaningFee() {
        return cleaningFee;
    }

    public void setCleaningFee(double cleaningFee) {
        this.cleaningFee = cleaningFee;
    }

    public double getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(double managementFee) {
        this.managementFee = managementFee;
    }

    public double getCommunityFee() {
        return communityFee;
    }

    public void setCommunityFee(double communityFee) {
        this.communityFee = communityFee;
    }

    public double getGuestServiceFee() {
        return guestServiceFee;
    }

    public void setGuestServiceFee(double guestServiceFee) {
        this.guestServiceFee = guestServiceFee;
    }

    public double getAccommodationsTax() {
        return accommodationsTax;
    }

    public void setAccommodationsTax(double accommodationsTax) {
        this.accommodationsTax = accommodationsTax;
    }

    public double getLocalSalesTax() {
        return localSalesTax;
    }

    public void setLocalSalesTax(double localSalesTax) {
        this.localSalesTax = localSalesTax;
    }

    public double getGeneralSalesTax() {
        return generalSalesTax;
    }

    public void setGeneralSalesTax(double generalSalesTax) {
        this.generalSalesTax = generalSalesTax;
    }

    public double getSummitSalesTax() {
        return summitSalesTax;
    }

    public void setSummitSalesTax(double summitSalesTax) {
        this.summitSalesTax = summitSalesTax;
    }

    public void setTaxAmount(String taxLabel, double amount) {
        switch (taxLabel) {
            case "Accommodations Tax (Summit)":
                this.accommodationsTax = amount;
                break;
            case "Local Sales and Use Tax (Summit County Housing Authority)":
                this.localSalesTax = amount;
                break;
            case "General Sales and Use Tax (Colorado)":
                this.generalSalesTax = amount;
                break;
            case "General Sales and Use Tax (Summit)":
                this.summitSalesTax = amount;
                break;
        }
    }

    public void setFeeAmount(String feeLabel, double amount) {
        switch (feeLabel) {
            case "Cleaning Fee":
                this.cleaningFee = amount;
                break;
            case "Management Fee":
                this.managementFee = amount;
                break;
            case "Community Fee":
                this.communityFee = amount;
                break;
            case "Guest Service Fee":
                this.guestServiceFee = amount;
                break;
        }
    }
}
