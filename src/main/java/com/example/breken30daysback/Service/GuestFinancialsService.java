package com.example.breken30daysback.Service;

import com.example.breken30daysback.Entity.GuestFinancials;
import com.example.breken30daysback.Entity.Reservation;
import com.example.breken30daysback.Models.ReservationDTO;
import com.example.breken30daysback.Repository.GuestFinancialsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class GuestFinancialsService {
    private final GuestFinancialsRepository guestFinancialsRepository;
    private final ObjectMapper objectMapper;

    public GuestFinancialsService(GuestFinancialsRepository guestFinancialsRepository, ObjectMapper objectMapper) {
        this.guestFinancialsRepository = guestFinancialsRepository;
        this.objectMapper = objectMapper;
    }

    public void processGuestFinancials(ReservationDTO dto, Reservation reservation) {
        if (dto.getFinancials() == null) return;
        if (guestFinancialsRepository.existsByReservationId(reservation.getId())) return;

        JsonNode financialsNode = objectMapper.valueToTree(dto.getFinancials());
        JsonNode guestFinancialsNode = financialsNode.path("guest");

        GuestFinancials guestFinancials = new GuestFinancials();
        guestFinancials.setReservation(reservation);
        guestFinancials.setCurrency(financialsNode.path("currency").asText(""));
        guestFinancials.setTotalPrice(parseDouble(guestFinancialsNode, "total_price"));
        guestFinancials.setAccommodation(parseDouble(guestFinancialsNode, "accommodation"));
        guestFinancials.setAverageNightlyRate(parseDouble(guestFinancialsNode, "average_nightly_rate"));

        // Process fees
        processArray(guestFinancialsNode.path("fees"), guestFinancials, true);

        // Process taxes
        processArray(guestFinancialsNode.path("taxes"), guestFinancials, false);

        guestFinancialsRepository.save(guestFinancials);
    }

    private double parseDouble(JsonNode node, String key) {
        if (node.has(key) && node.get(key).has("amount")) {
            return node.get(key).get("amount").asDouble(0.0);
        }
        return 0.0;
    }

    private void processArray(JsonNode arrayNode, GuestFinancials guestFinancials, boolean isFee) {
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                double amount = item.has("amount") ? item.get("amount").asDouble(0.0) : 0.0;
                String label = item.has("label") ? item.get("label").asText() : "";

                if (isFee) {
                    guestFinancials.setFeeAmount(label, amount);
                } else {
                    guestFinancials.setTaxAmount(label, amount);
                }
            }
        }
    }
}
