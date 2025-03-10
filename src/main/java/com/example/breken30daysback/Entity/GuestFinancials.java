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
    private double generalSalesTaxBreckenridge;
    private double generalSalesTaxColorado;
    private double generalSalesTaxSummit;
    private double localSalesTaxSummitCountyHousingAuthority;
    private double accommodationsTaxBreckenridge;

    private double totalTax;

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

    public double getGeneralSalesTaxBreckenridge() {
        return generalSalesTaxBreckenridge;
    }

    public void setGeneralSalesTaxBreckenridge(double generalSalesTaxBreckenridge) {
        this.generalSalesTaxBreckenridge = generalSalesTaxBreckenridge;
    }

    public double getGeneralSalesTaxColorado() {
        return generalSalesTaxColorado;
    }

    public void setGeneralSalesTaxColorado(double generalSalesTaxColorado) {
        this.generalSalesTaxColorado = generalSalesTaxColorado;
    }

    public double getGeneralSalesTaxSummit() {
        return generalSalesTaxSummit;
    }

    public void setGeneralSalesTaxSummit(double generalSalesTaxSummit) {
        this.generalSalesTaxSummit = generalSalesTaxSummit;
    }

    public double getLocalSalesTaxSummitCountyHousingAuthority() {
        return localSalesTaxSummitCountyHousingAuthority;
    }

    public void setLocalSalesTaxSummitCountyHousingAuthority(double localSalesTaxSummitCountyHousingAuthority) {
        this.localSalesTaxSummitCountyHousingAuthority = localSalesTaxSummitCountyHousingAuthority;
    }

    public double getAccommodationsTaxBreckenridge() {
        return accommodationsTaxBreckenridge;
    }

    public void setAccommodationsTaxBreckenridge(double accommodationsTaxBreckenridge) {
        this.accommodationsTaxBreckenridge = accommodationsTaxBreckenridge;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public void setTaxAmount(String label, double amount) {
        switch (label) {
            case "General Sales and Use Tax (Breckenridge)":
                this.generalSalesTaxBreckenridge = amount;
                break;
            case "General Sales and Use Tax (Colorado)":
                this.generalSalesTaxColorado = amount;
                break;
            case "General Sales and Use Tax (Summit)":
                this.generalSalesTaxSummit = amount;
                break;
            case "Local Sales and Use Tax (Summit County Housing Authority)":
                this.localSalesTaxSummitCountyHousingAuthority = amount;
                break;
            case "Accommodations Tax (Breckenridge)":
                this.accommodationsTaxBreckenridge = amount;
                break;
            default:
                break;
        }
        this.totalTax += amount; // Add to total tax sum
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
