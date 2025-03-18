package com.example.breken30daysback.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuestDetailsDTO {
    @JsonProperty("total")
    private int totalGuests;

    @JsonProperty("adult_count")
    private int adultCount;

    @JsonProperty("child_count")
    private int childCount;

    @JsonProperty("infant_count")
    private int infantCount;

    @JsonProperty("pet_count")
    private int petCount;


    public int getTotalGuests() {
        return totalGuests;
    }

    public void setTotalGuests(int totalGuests) {
        this.totalGuests = totalGuests;
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
}
