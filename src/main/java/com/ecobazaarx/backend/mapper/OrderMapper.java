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
                .sellerId(item.getProduct().getSeller().getId())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .carbonCostKg(item.getCarbonCostKg())
                .build();
    }

    public OrderResponseDTO toOrderResponseDTO(Order order, List<OrderItem> items) {

        List<OrderItemDTO> itemDTOs = items.stream()
                .map(this::toOrderItemDTO)
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus().name())
                .totalPrice(order.getTotalPrice())
                .totalCarbonKg(order.getTotalCarbonKg())
                .items(itemDTOs)
                .build();
    }
}
