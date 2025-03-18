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
        JsonNode guestFinancialsNode = financialsNode.path("guest");

        HostFinancials hostFinancials = new HostFinancials();
        hostFinancials.setReservation(reservation);
        hostFinancials.setCurrency(financialsNode.path("currency").asText(""));
        hostFinancials.setRevenue(parseFormattedValue(hostFinancialsNode, "revenue"));
        hostFinancials.setManagementFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Management Fee"));
        hostFinancials.setCleaningFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Cleaning Fee"));
        hostFinancials.setExtraGuestFee(extractSpecificFee(guestFinancialsNode.path("fees"), "EXTRA_GUEST_FEE"));
        hostFinancials.setCommunityFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Community Fee"));
        hostFinancials.setResortFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Resort Fee"));
        hostFinancials.setDamageProtection(extractSpecificFee(guestFinancialsNode.path("fees"), "Damage Protection"));
        hostFinancials.setAccommodation(parseFormattedValue(hostFinancialsNode, "accommodation"));
        hostFinancials.setTotalGuestFees(sumArray(hostFinancialsNode.path("guest_fees")));
        hostFinancials.setTotalHostFees(sumArray(hostFinancialsNode.path("host_fees")));
        hostFinancials.setTotalDiscounts(sumArray(hostFinancialsNode.path("discounts")));
        hostFinancials.setTotalAdjustments(sumArray(hostFinancialsNode.path("adjustments")));
        hostFinancials.setTotalTaxes(sumArray(hostFinancialsNode.path("taxes")));
        hostFinancials.setHostServiceFee(extractSpecificFee(hostFinancialsNode.path("host_fees"), "Host Service Fee"));
        hostFinancials.setPaidToVrbo(extractSpecificFee(hostFinancialsNode.path("host_fees"), "Paid to Vrbo"));

        hostFinancialsRepository.save(hostFinancials);
    }

    private double parseFormattedValue(JsonNode node, String key) {
        if (node.has(key)) {
            JsonNode valueNode = node.get(key);
            String formattedValue;

            if (valueNode.isObject() && valueNode.has("formatted")) {
                formattedValue = valueNode.path("formatted").asText("");
            } else if (valueNode.isValueNode()) {
                formattedValue = valueNode.asText("");
            } else {
                return 0.0;
            }

            formattedValue = formattedValue.replace("$", "").replace(",", "");
            try {
                return Double.parseDouble(formattedValue);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private double sumArray(JsonNode arrayNode) {
        double total = 0.0;
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                total += parseFormattedValue(item, "formatted");
            }
        }
        return total;
    }

    private double extractSpecificFee(JsonNode arrayNode, String label) {
        if (arrayNode.isArray()) {
            String normalizedLabel = normalizeLabel(label);
            for (JsonNode item : arrayNode) {
                String itemLabel = item.path("label").asText("");
                if (normalizeLabel(itemLabel).equals(normalizedLabel)) {
                    return parseFormattedValue(item, "formatted");
                }
            }
        }
        return 0.0;
    }

    private String normalizeLabel(String label) {
        return label.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}