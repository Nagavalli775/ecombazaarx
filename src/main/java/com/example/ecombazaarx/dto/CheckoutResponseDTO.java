package com.example.ecombazaarx.dto;

import java.util.List;

import lombok.*;

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
