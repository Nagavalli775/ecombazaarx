package com.ecobazaarx.backend.dto;

import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponseDTO {
    private Long orderId;
    private double totalPrice;
    private double totalCarbonKg;
    private String orderStatus;
    private String ecoRating;
    private List<OrderItemDTO> items;
}
