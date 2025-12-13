package com.ecobazaarx.backend.mapper;

import com.ecobazaarx.backend.dto.OrderItemDTO;
import com.ecobazaarx.backend.dto.OrderResponseDTO;
import com.ecobazaarx.backend.entity.Order;
import com.ecobazaarx.backend.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderItemDTO toOrderItemDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getProduct().getPrice())
                .carbonImpactKg(item.getProduct().getCarbonImpactKg())
                .build();
    }

    public OrderResponseDTO toOrderDTO(Order order, List<OrderItem> items) {

        List<OrderItemDTO> dtos = items.stream()
                .map(this::toOrderItemDTO)
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate().toString())
                .totalPrice(order.getTotalPrice())
                .totalCarbon(order.getTotalCarbon())
                .items(dtos)
                .build();
    }
}
