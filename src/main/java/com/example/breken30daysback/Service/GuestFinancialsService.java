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
        guestFinancials.setTotalPrice(parseFormattedValue(guestFinancialsNode, "total_price"));
        guestFinancials.setAccommodation(parseFormattedValue(guestFinancialsNode, "accommodation"));
        guestFinancials.setAverageNightlyRate(parseFormattedValue(guestFinancialsNode, "average_nightly_rate"));
        guestFinancials.setExtraGuestFee(extractSpecificFee(guestFinancialsNode.path("fees"), "EXTRA_GUEST_FEE"));
        guestFinancials.setGuestServiceFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Guest Service Fee"));
        guestFinancials.setPetFee(extractSpecificFee(guestFinancialsNode.path("fees"), "Pet Fee"));
        guestFinancials.setResolutionPayout(extractSpecificAdjustment(guestFinancialsNode.path("adjustments"), "Resolution payout"));

        processPayments(guestFinancialsNode.path("payments"), guestFinancials);
        processArray(guestFinancialsNode.path("fees"), guestFinancials, true);
        processArray(guestFinancialsNode.path("taxes"), guestFinancials, false);
        processDynamicFees(guestFinancialsNode.path("fees"), guestFinancials);

        guestFinancialsRepository.save(guestFinancials);
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

    private void processPayments(JsonNode paymentsNode, GuestFinancials guestFinancials) {
        if (paymentsNode.isArray()) {
            int paymentIndex = 1;
            for (JsonNode payment : paymentsNode) {
                if (paymentIndex > 2) break;

                double amount = parseFormattedValue(payment, "formatted");
                String formatted = payment.path("formatted").asText("");
                String label = payment.path("label").asText("");

                switch (paymentIndex) {
                    case 1:
                        guestFinancials.setPayment1Amount(amount);
                        guestFinancials.setPayment1Formatted(formatted.replace("$", ""));
                        guestFinancials.setPayment1Label(label);
                        break;
                    case 2:
                        guestFinancials.setPayment2Amount(amount);
                        guestFinancials.setPayment2Formatted(formatted.replace("$", ""));
                        guestFinancials.setPayment2Label(label);
                        break;
                }
                paymentIndex++;
            }
        }
    }

    private void processDynamicFees(JsonNode feesNode, GuestFinancials guestFinancials) {
        if (feesNode.isArray()) {
            for (JsonNode fee : feesNode) {
                String label = fee.path("label").asText("").toLowerCase();
                double formattedValue = parseFormattedValue(fee, "formatted");

                if (label.contains("property") && label.contains("damage") && label.contains("protection")) {
                    guestFinancials.setPropertyDamageProtection(formattedValue);
                } else if (label.contains("pet") && label.contains("fee")) {
                    guestFinancials.setPetFee(formattedValue);
                } else if (label.contains("resort")) {
                    guestFinancials.setResortFee(formattedValue);
                }
            }
        }
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

    private double extractSpecificAdjustment(JsonNode arrayNode, String label) {
        if (arrayNode.isArray()) {
            String normalizedLabel = normalizeLabel(label);
            for (JsonNode item : arrayNode) {
                if (normalizeLabel(item.path("label").asText("")).equals(normalizedLabel)) {
                    return parseFormattedValue(item, "formatted");
                }
            }
        }
        return 0.0;
    }

    private void processArray(JsonNode arrayNode, GuestFinancials guestFinancials, boolean isFee) {
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                double formattedValue = parseFormattedValue(item, "formatted");
                String label = item.has("label") ? item.get("label").asText() : "";

                if (isFee) {
                    guestFinancials.setFeeAmount(label, formattedValue);
                } else {
                    guestFinancials.setTaxAmount(label, formattedValue);
                }
            }
        }
    }

    private String normalizeLabel(String label) {
        return label.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}