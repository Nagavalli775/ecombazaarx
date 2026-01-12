package com.example.ecombazaarx.dto;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<com.example.ecombazaarx.dto.CartItemDTO> items;
    private double totalPrice;
    private double totalCarbon;
}
