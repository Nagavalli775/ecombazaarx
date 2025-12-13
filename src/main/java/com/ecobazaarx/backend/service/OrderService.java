package com.ecobazaarx.backend.service;

import com.ecobazaarx.backend.entity.*;
import com.ecobazaarx.backend.repository.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    // Checkout Process
    public Order checkout(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty())
            throw new RuntimeException("Your cart is empty");

        // Calculate totals
        double totalPrice = cartItems.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        double totalCarbon = cartItems.stream()
                .mapToDouble(i -> i.getProduct().getCarbonImpactKg() * i.getQuantity())
                .sum();

        // Create order
        Order order = Order.builder()
                .user(User.builder().id(userId).build())
                .orderStatus(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PAID)
                .paymentMethod(PaymentMethod.UPI)
                .totalPrice(totalPrice)
                .totalCarbonKg(totalCarbon)
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .build();

        orderRepository.save(order);

        // Save each item in order_items
        for (CartItem ci : cartItems) {

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .unitPrice(ci.getProduct().getPrice())
                    .quantity(ci.getQuantity())
                    .carbonCostKg(ci.getProduct().getCarbonImpactKg() * ci.getQuantity())
                    .build();

            orderItemRepository.save(oi);
        }

        // Clear cart after checkout
        cartService.clearCart(userId);

        return order;
    }

    // Eco rating logic
    public String generateEcoRating(double carbon) {

        if (carbon < 2.0) return "♻️ Eco Hero";
        if (carbon < 5.0) return "🌿 Eco Friendly";
        if (carbon < 10.0) return "🙂 Moderate Impact";

        return "⚠️ High Carbon User";
    }

    // Get all orders for user
    public List<Order> getOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
