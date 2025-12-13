package com.ecobazaarx.backend.dto;

import lombok.Data;

@Data
public class CheckoutResponseDTO {
    private Long orderId;
    private Double totalPrice;
    private Double totalCarbonKg;
    private String ecoRating;
}
