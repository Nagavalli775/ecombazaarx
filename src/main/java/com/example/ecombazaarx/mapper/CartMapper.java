// package com.ecobazaarx.backend.mapper;
package com.example.ecombazaarx.mapper;

import com.example.ecombazaarx.dto.CartItemDTO;
import com.example.ecombazaarx.dto.CartResponseDTO;
import com.example.ecombazaarx.entity.Cart;
import com.example.ecombazaarx.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    /**
     * Convert CartItem -> CartItemDTO
     */
    public CartItemDTO toItemDTO(CartItem item) {

        double totalItemPrice =
                item.getQuantity() * item.getProduct().getPrice();

        double totalItemCarbon =
                item.getQuantity() * item.getProduct().getCarbonImpactKg();

        return CartItemDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .carbonImpactKg(item.getProduct().getCarbonImpactKg())
                .totalItemPrice(totalItemPrice)
                .totalItemCarbon(totalItemCarbon)
                .build();
    }

    /**
     * Convert Cart + CartItems -> CartResponseDTO
     */
    public CartResponseDTO toCartResponseDTO(Cart cart, List<CartItem> items) {

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
                .items(itemDTOs)
                .totalPrice(totalPrice)
                .totalCarbon(totalCarbon)
                .build();
    }
}
