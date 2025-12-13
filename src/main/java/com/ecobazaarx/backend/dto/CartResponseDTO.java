package com.ecobazaarx.backend.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<com.ecobazaarx.backend.dto.cart.CartItemDTO> items;
    private double totalPrice;
    private double totalCarbon;
}
