package com.ecobazaarx.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double carbonImpactKg;
}
