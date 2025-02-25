package com.example.breken30daysback.Service;

import com.example.breken30daysback.Entity.Reservation;
import com.example.breken30daysback.Models.CapacityDetails;
import com.example.breken30daysback.Models.Property;
import com.example.breken30daysback.Models.ReservationDTO;
import com.example.breken30daysback.Repository.CapacityDetailsRepository;
import com.example.breken30daysback.Repository.PropertyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CapacityDetailsService {
    private final CapacityDetailsRepository capacityDetailsRepository;
    private final PropertyRepository propertyRepository;
    private final ObjectMapper objectMapper;

    public CapacityDetailsService(CapacityDetailsRepository capacityDetailsRepository,
                                  PropertyRepository propertyRepository,
                                  ObjectMapper objectMapper) {
        this.capacityDetailsRepository = capacityDetailsRepository;
        this.propertyRepository = propertyRepository;
        this.objectMapper = objectMapper;
    }

    public void processCapacityDetails(ReservationDTO dto, Reservation reservation) {
        if (reservation == null) return;
        if (capacityDetailsRepository.existsByReservationId(reservation.getId())) return;

        JsonNode guestsNode = objectMapper.valueToTree(dto.getGuests());
        JsonNode propertyNode = objectMapper.valueToTree(dto.getProperties().get(0));

        String propertyId = propertyNode.get("id").asText();
        Optional<Property> optionalProperty = propertyRepository.findByPropertyId(propertyId);
        if (optionalProperty.isEmpty()) return;

        CapacityDetails capacity = new CapacityDetails();
        capacity.setReservation(reservation);
        capacity.setProperty(optionalProperty.get());

        capacity.setAdultCount(guestsNode.has("adult_count") ? guestsNode.get("adult_count").asInt() : 0);
        capacity.setChildCount(guestsNode.has("child_count") ? guestsNode.get("child_count").asInt() : 0);
        capacity.setInfantCount(guestsNode.has("infant_count") ? guestsNode.get("infant_count").asInt() : 0);
        capacity.setPetCount(guestsNode.has("pet_count") ? guestsNode.get("pet_count").asInt() : 0);

        capacity.setMaximumGuestCount(
                capacity.getAdultCount() +
                        capacity.getChildCount() +
                        capacity.getInfantCount() +
                        capacity.getPetCount()
        );

        // ✅ Extract room details
        JsonNode roomDetailsNode = propertyNode.path("room_details");
        int totalBedCount = 0;
        int numberOfBedrooms = roomDetailsNode.size();

        for (JsonNode room : roomDetailsNode) {
            JsonNode bedsNode = room.path("beds");
            for (JsonNode bed : bedsNode) {
                totalBedCount += bed.path("quantity").asInt(0);
            }
        }

        capacity.setTotalBedCount(totalBedCount);
        capacity.setNumberOfBedrooms(numberOfBedrooms);

        capacityDetailsRepository.save(capacity);
    }
}

