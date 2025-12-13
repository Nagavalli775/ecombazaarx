package com.ecobazaarx.backend.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private String orderDate;
    private double totalPrice;
    private double totalCarbon;
    private List<OrderItemDTO> items;
}
