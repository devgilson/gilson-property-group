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
    private double petFee;

    // Taxes
    private double generalSalesTaxBreckenridge;
    private double generalSalesTaxColorado;
    private double generalSalesTaxSummit;
    private double localSalesTaxSummitCountyHousingAuthority;
    private double accommodationsTaxBreckenridge;
    private double resolutionPayout;
    private double extraGuestFee;
    private double propertyDamageProtection;
    private double petDamages;
    private double damageProtection;
    private double resortFee;

    // Payment 1
    private Double payment1Amount;
    private String payment1Formatted;
    private String payment1Label;

    // Payment 2
    private Double payment2Amount;
    private String payment2Formatted;
    private String payment2Label;

    public double getPetFee() {
        return petFee;
    }

    public void setPetFee(double petFee) {
        this.petFee = petFee;
    }

    public double getResolutionPayout() {
        return resolutionPayout;
    }

    public void setResolutionPayout(double resolutionPayout) {
        this.resolutionPayout = resolutionPayout;
    }

    public double getExtraGuestFee() {
        return extraGuestFee;
    }

    public void setExtraGuestFee(double extraGuestFee) {
        this.extraGuestFee = extraGuestFee;
    }

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

    public double getResortFee() {
        return resortFee;
    }

    public void setResortFee(double resortFee) {
        this.resortFee = resortFee;
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

    public double getPropertyDamageProtection() {
        return propertyDamageProtection;
    }

    public void setPropertyDamageProtection(double propertyDamageProtection) {
        this.propertyDamageProtection = propertyDamageProtection;
    }

    public double getPetDamages() {
        return petDamages;
    }

    public void setPetDamages(double petDamages) {
        this.petDamages = petDamages;
    }

    public double getDamageProtection() {
        return damageProtection;
    }

    public void setDamageProtection(double damageProtection) {
        this.damageProtection = damageProtection;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getPayment1Amount() {
        return payment1Amount;
    }

    public void setPayment1Amount(Double payment1Amount) {
        this.payment1Amount = payment1Amount;
    }

    public String getPayment1Formatted() {
        return payment1Formatted;
    }

    public void setPayment1Formatted(String payment1Formatted) {
        this.payment1Formatted = payment1Formatted;
    }

    public String getPayment1Label() {
        return payment1Label;
    }

    public void setPayment1Label(String payment1Label) {
        this.payment1Label = payment1Label;
    }

    public Double getPayment2Amount() {
        return payment2Amount;
    }

    public void setPayment2Amount(Double payment2Amount) {
        this.payment2Amount = payment2Amount;
    }

    public String getPayment2Formatted() {
        return payment2Formatted;
    }

    public void setPayment2Formatted(String payment2Formatted) {
        this.payment2Formatted = payment2Formatted;
    }

    public String getPayment2Label() {
        return payment2Label;
    }

    public void setPayment2Label(String payment2Label) {
        this.payment2Label = payment2Label;
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
