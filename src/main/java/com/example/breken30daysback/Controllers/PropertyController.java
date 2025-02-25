package com.example.breken30daysback.Controllers;

import com.example.breken30daysback.Models.ListingDetails;
import com.example.breken30daysback.Repository.ListingDetailsRepository;
import com.example.breken30daysback.Service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ListingDetailsRepository listingDetailsRepository;

    @PostMapping("/sync")
    public ResponseEntity<String> syncProperties() {
        try {
            propertyService.syncProperties();
            return ResponseEntity.ok("Properties synchronized successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to sync properties: " + e.getMessage());
        }
    }


    @GetMapping("/listings")
    public ResponseEntity<List<ListingDetails>> getAllListings() {
        return ResponseEntity.ok(listingDetailsRepository.findAll());
    }

    /**
     * Fetch a specific listing detail by property ID.
     */
    @GetMapping("listings/{propertyId}")
    public ResponseEntity<ListingDetails> getListingByPropertyId(@PathVariable Long propertyId) {
        Optional<ListingDetails> listingDetails = listingDetailsRepository.findById(propertyId);
        return listingDetails.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
