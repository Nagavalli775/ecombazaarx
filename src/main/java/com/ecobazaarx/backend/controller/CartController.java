package com.ecobazaarx.backend.controller;

import com.ecobazaarx.backend.dto.ApiResponse;
import com.ecobazaarx.backend.dto.CartResponseDTO;
import com.ecobazaarx.backend.entity.Cart;
import com.ecobazaarx.backend.entity.CartItem;
import com.ecobazaarx.backend.mapper.CartMapper;
import com.ecobazaarx.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Cart cart = cartService.addToCart(userId, productId, quantity);
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartMapper.toCartResponseDTO(cart, items));
    }

    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<CartResponseDTO> updateItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        Cart cart = cartService.updateQuantity(userId, productId, quantity);
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartMapper.toCartResponseDTO(cart, items));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        cartService.removeFromCart(userId, productId);
        Cart cart = cartService.getOrCreateCart(userId);
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartMapper.toCartResponseDTO(cart, items));
    }

    // ✅ FIXED METHOD
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(@PathVariable Long userId) {

        Cart cart = cartService.getOrCreateCart(userId);
        List<CartItem> items = cartService.getCartItems(userId);

        CartResponseDTO response =
                cartMapper.toCartResponseDTO(cart, items);

        return ResponseEntity.ok(
                ApiResponse.ok(response, "Cart loaded successfully")
        );
    }
}
