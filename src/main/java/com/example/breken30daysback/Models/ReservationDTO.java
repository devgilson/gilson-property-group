package com.example.breken30daysback.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReservationDTO {
    private String id;
    private String code;
    private String platform;

    @JsonProperty("platform_id")
    private String platformId;

    @JsonProperty("booking_date")
    private String bookingDate;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("check_in")
    private String checkIn;

    @JsonProperty("check_out")
    private String checkOut;

    private int nights;

    @JsonProperty("reservation_status")
    private Map<String, Object> reservationStatus;

    @JsonProperty("guests")
    private GuestDetailsDTO guests;

    private int adultCount;
    private int childCount;
    private int infantCount;
    private int petCount;


    @JsonProperty("financials")
    private Map<String, Object> financials;

    @JsonProperty("properties")
    private List<Map<String, Object>> properties;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public Map<String, Object> getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(Map<String, Object> reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public GuestDetailsDTO getGuests() {
        return guests;
    }


    public void setGuests(GuestDetailsDTO guests) {
        this.guests = guests;
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

    public Map<String, Object> getFinancials() {
        return financials;
    }

    public void setFinancials(Map<String, Object> financials) {
        this.financials = financials;
    }

    public List<Map<String, Object>> getProperties() {
        return properties;
    }

    public void setProperties(List<Map<String, Object>> properties) {
        this.properties = properties;
    }



}
