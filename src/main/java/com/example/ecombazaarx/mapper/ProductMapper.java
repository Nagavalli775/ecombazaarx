package com.example.ecombazaarx.mapper;

import com.example.ecombazaarx.dto.ProductDTO;
import com.example.ecombazaarx.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stockQuantity(p.getStockQuantity())
                .carbonImpactKg(p.getCarbonImpactKg())
                .ecoVerified(p.getEcoVerified())
                .category(p.getCategory())
                .sellerId(p.getSeller().getId())
                .sellerName(p.getSeller().getUsername())
                .build();
    }
}
