package com.example.breken30daysback.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "host_financials")
public class HostFinancials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private String currency;
    private double accommodation;
    private double revenue;
    private double hostServiceFee;
    private double lengthOfStayDiscount;
    private double totalGuestFees;
    private double totalHostFees;
    private double totalDiscounts;
    private double totalAdjustments;
    private double totalTaxes;
    private double paidToVrbo;
    private Double managementFee;
    private Double cleaningFee;
    private Double extraGuestFee;
    private Double communityFee;
    private Double resortFee;
    private Double damageProtection;

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

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getHostServiceFee() {
        return hostServiceFee;
    }

    public void setHostServiceFee(double hostServiceFee) {
        this.hostServiceFee = hostServiceFee;
    }

    public double getLengthOfStayDiscount() {
        return lengthOfStayDiscount;
    }

    public void setLengthOfStayDiscount(double lengthOfStayDiscount) {
        this.lengthOfStayDiscount = lengthOfStayDiscount;
    }

    public double getTotalGuestFees() {
        return totalGuestFees;
    }

    public void setTotalGuestFees(double totalGuestFees) {
        this.totalGuestFees = totalGuestFees;
    }

    public double getTotalHostFees() {
        return totalHostFees;
    }

    public void setTotalHostFees(double totalHostFees) {
        this.totalHostFees = totalHostFees;
    }

    public double getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public double getTotalAdjustments() {
        return totalAdjustments;
    }

    public void setTotalAdjustments(double totalAdjustments) {
        this.totalAdjustments = totalAdjustments;
    }

    public double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public double getPaidToVrbo() {
        return paidToVrbo;
    }

    public void setPaidToVrbo(double paidToVrbo) {
        this.paidToVrbo = paidToVrbo;
    }

    public Double getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(Double managementFee) {
        this.managementFee = managementFee;
    }

    public Double getCleaningFee() {
        return cleaningFee;
    }

    public void setCleaningFee(Double cleaningFee) {
        this.cleaningFee = cleaningFee;
    }

    public Double getExtraGuestFee() {
        return extraGuestFee;
    }

    public void setExtraGuestFee(Double extraGuestFee) {
        this.extraGuestFee = extraGuestFee;
    }

    public Double getCommunityFee() {
        return communityFee;
    }

    public void setCommunityFee(Double communityFee) {
        this.communityFee = communityFee;
    }

    public Double getResortFee() {
        return resortFee;
    }

    public void setResortFee(Double resortFee) {
        this.resortFee = resortFee;
    }

    public Double getDamageProtection() {
        return damageProtection;
    }

    public void setDamageProtection(Double damageProtection) {
        this.damageProtection = damageProtection;
    }
}
