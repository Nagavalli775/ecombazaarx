package com.example.ecombazaarx.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private String orderDate;
    private double totalPrice;
    private double totalCarbonKg;
    private List<OrderItemDTO> items;
}
