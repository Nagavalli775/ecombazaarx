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
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    // ✅ Checkout (Cart → Order)
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@PathVariable Long userId) {

        Order order = orderService.checkout(userId);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        return ResponseEntity.ok(
                orderMapper.toOrderResponseDTO(order, items)
        );
    }

    // ✅ User order history
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(
            @PathVariable Long userId
    ) {

        List<Order> orders = orderService.getOrders(userId);

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> {
                    List<OrderItem> items =
                            orderItemRepository.findByOrderId(order.getId());
                    return orderMapper.toOrderResponseDTO(order, items);
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // ✅ Eco rating before checkout
    @GetMapping("/{userId}/eco-rating")
    public ResponseEntity<String> getEcoRating(@PathVariable Long userId) {

        double carbon = cartService.calculateCarbon(userId);
        return ResponseEntity.ok(orderService.generateEcoRating(carbon));
    }
}
