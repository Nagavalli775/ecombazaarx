package com.example.ecombazaarx.service;

import com.example.ecombazaarx.entity.*;
import com.example.ecombazaarx.repository.*;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    // private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    // Checkout Process
    public Order checkout(Long userId) {

    Cart cart = cartService.getOrCreateCart(userId);
    List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

    if (cartItems.isEmpty()) {
        throw new RuntimeException("Your cart is empty");
    }

    Order order = Order.builder()
            .user(cart.getUser())
            .orderStatus(OrderStatus.CONFIRMED)
            .paymentStatus(PaymentStatus.PAID)
            .paymentMethod(PaymentMethod.UPI) // dummy for now
            .orderDate(new Timestamp(System.currentTimeMillis()))
            .totalPrice(0.0)
            .totalCarbonKg(0.0)
            .build();

    order = orderRepository.save(order); // get order ID

    double totalPrice = 0;
    double totalCarbon = 0;

    for (CartItem ci : cartItems) {

        double itemPrice = ci.getProduct().getPrice() * ci.getQuantity();
        double itemCarbon = ci.getProduct().getCarbonImpactKg() * ci.getQuantity();

        OrderItem oi = OrderItem.builder()
                .order(order)
                .product(ci.getProduct())
                .unitPrice(ci.getProduct().getPrice())
                .quantity(ci.getQuantity())
                .carbonCostKg(itemCarbon)
                .build();

        orderItemRepository.save(oi);

        totalPrice += itemPrice;
        totalCarbon += itemCarbon;
    }

    order.setTotalPrice(totalPrice);
    order.setTotalCarbonKg(totalCarbon);
    orderRepository.save(order);

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
