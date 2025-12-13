package com.ecobazaarx.backend.mapper;

import com.ecobazaarx.backend.dto.CartItemDTO;
import com.ecobazaarx.backend.dto.CartResponseDTO;
import com.ecobazaarx.backend.entity.Cart;
import com.ecobazaarx.backend.entity.CartItem;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartItemDTO toItemDTO(CartItem item) {

        double totalPrice = item.getQuantity() * item.getProduct().getPrice();
        double totalCarbon = item.getQuantity() * item.getProduct().getCarbonImpactKg();

        return CartItemDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .carbonImpactKg(item.getProduct().getCarbonImpactKg())
                .totalItemPrice(totalPrice)
                .totalItemCarbon(totalCarbon)
                .build();
    }

    public @Nullable CartResponseDTO toCartResponseDTO(Cart cart, List<CartItem> items) {

        if (cart == null) return null;

        List<CartItemDTO> itemDTOs = items.stream()
                .map(this::toItemDTO)
                .toList();

        double totalPrice = itemDTOs.stream()
                .mapToDouble(CartItemDTO::getTotalItemPrice)
                .sum();

        double totalCarbon = itemDTOs.stream()
                .mapToDouble(CartItemDTO::getTotalItemCarbon)
                .sum();

        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                //.items(itemDTOs)
                .totalPrice(totalPrice)
                .totalCarbon(totalCarbon)
                .build();
    }
}
