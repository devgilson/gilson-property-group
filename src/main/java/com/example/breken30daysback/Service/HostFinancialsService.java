package com.example.breken30daysback.Service;


import com.example.breken30daysback.Entity.HostFinancials;
import com.example.breken30daysback.Entity.Reservation;
import com.example.breken30daysback.Models.ReservationDTO;
import com.example.breken30daysback.Repository.HostFinancialsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class HostFinancialsService {
    private final HostFinancialsRepository hostFinancialsRepository;
    private final ObjectMapper objectMapper;

    public HostFinancialsService(HostFinancialsRepository hostFinancialsRepository, ObjectMapper objectMapper) {
        this.hostFinancialsRepository = hostFinancialsRepository;
        this.objectMapper = objectMapper;
    }

    public void processHostFinancials(ReservationDTO dto, Reservation reservation) {
        if (dto.getFinancials() == null) return;
        if (hostFinancialsRepository.existsByReservationId(reservation.getId())) return;

        JsonNode financialsNode = objectMapper.valueToTree(dto.getFinancials());
        JsonNode hostFinancialsNode = financialsNode.path("host");

        HostFinancials hostFinancials = new HostFinancials();
        hostFinancials.setReservation(reservation);
        hostFinancials.setCurrency(financialsNode.path("currency").asText(""));
        hostFinancials.setRevenue(parseDouble(hostFinancialsNode, "revenue"));
        hostFinancials.setAccommodation(parseDouble(hostFinancialsNode, "accommodation"));
        hostFinancials.setTotalGuestFees(sumArray(hostFinancialsNode.path("guest_fees")));
        hostFinancials.setTotalHostFees(sumArray(hostFinancialsNode.path("host_fees")));
        hostFinancials.setTotalDiscounts(sumArray(hostFinancialsNode.path("discounts")));
        hostFinancials.setTotalAdjustments(sumArray(hostFinancialsNode.path("adjustments")));
        hostFinancials.setTotalTaxes(sumArray(hostFinancialsNode.path("taxes")));
        hostFinancials.setHostServiceFee(extractSpecificFee(hostFinancialsNode.path("host_fees"), "Host Service Fee"));

        hostFinancialsRepository.save(hostFinancials);
    }

    private double parseDouble(JsonNode node, String key) {
        if (node.has(key) && node.get(key).has("amount")) {
            return node.get(key).get("amount").asDouble(0.0);
        }
        return 0.0;
    }

    private double sumArray(JsonNode arrayNode) {
        double total = 0.0;
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                total += item.path("amount").asDouble(0.0);
            }
        }
        return total;
    }

    private double extractSpecificFee(JsonNode arrayNode, String label) {
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                if (item.path("label").asText("").equalsIgnoreCase(label)) {
                    return item.path("amount").asDouble(0.0);
                }
            }
        }
        return 0.0;
    }
}
