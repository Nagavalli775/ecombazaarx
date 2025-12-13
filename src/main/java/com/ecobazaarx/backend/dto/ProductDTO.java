package com.ecobazaarx.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Double carbonImpactKg;
    private Boolean ecoVerified;
    private String category;
    private Long sellerId;
    private String sellerName;
}
