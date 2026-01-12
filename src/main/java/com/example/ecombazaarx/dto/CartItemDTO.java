package com.example.ecombazaarx.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {

    private Long productId;
    private String productName;
    private double price;
    private int quantity;

    private double carbonImpactKg;

    private double totalItemPrice;
    private double totalItemCarbon;
}
