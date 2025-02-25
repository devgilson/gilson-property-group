package com.example.breken30daysback.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "listing_details")
public class ListingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property property;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "standard_check_in_time")
    private String standardCheckInTime;

    @Column(name = "standard_check_out_time")
    private String standardCheckOutTime;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String propertyAddress;

    @Column(columnDefinition = "TEXT")
    private String roomDetails;

    @Column(name = "support_page_link")
    private String supportPageLink;

    @Column(columnDefinition = "TEXT")
    private String locationData;

    @Column(columnDefinition = "TEXT")
    private String accessDetails;

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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getStandardCheckInTime() {
        return standardCheckInTime;
    }

    public void setStandardCheckInTime(String standardCheckInTime) {
        this.standardCheckInTime = standardCheckInTime;
    }

    public String getStandardCheckOutTime() {
        return standardCheckOutTime;
    }

    public void setStandardCheckOutTime(String standardCheckOutTime) {
        this.standardCheckOutTime = standardCheckOutTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(String roomDetails) {
        this.roomDetails = roomDetails;
    }

    public String getSupportPageLink() {
        return supportPageLink;
    }

    public void setSupportPageLink(String supportPageLink) {
        this.supportPageLink = supportPageLink;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public String getAccessDetails() {
        return accessDetails;
    }

    public void setAccessDetails(String accessDetails) {
        this.accessDetails = accessDetails;
    }
}
