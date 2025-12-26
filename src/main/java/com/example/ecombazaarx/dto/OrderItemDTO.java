package com.example.ecombazaarx.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private Long productId;
    private long sellerId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double carbonCostKg;
}
