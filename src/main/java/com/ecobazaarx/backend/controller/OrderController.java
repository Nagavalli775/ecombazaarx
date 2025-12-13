package com.ecobazaarx.backend.controller;

import com.ecobazaarx.backend.entity.Order;
import com.ecobazaarx.backend.service.CartService;
import com.ecobazaarx.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    // Checkout
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Order> checkout(@PathVariable Long userId) {
        Order order = orderService.checkout(userId);
        return ResponseEntity.ok(order);
    }

    // Get user's orders
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrders(userId);
        return ResponseEntity.ok(orders);
    }

    // Eco rating for user's cart before checkout
    @GetMapping("/{userId}/eco-rating")
    public ResponseEntity<String> getEcoRating(@PathVariable Long userId) {
        double carbon = cartService.calculateCarbon(userId);
        String rating = orderService.generateEcoRating(carbon);
        return ResponseEntity.ok(rating);
    }
}
