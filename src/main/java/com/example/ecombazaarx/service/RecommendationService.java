package com.example.ecombazaarx.service;

import com.example.ecombazaarx.dto.EcoAlternativeDTO;
import com.example.ecombazaarx.entity.CartItem;
import com.example.ecombazaarx.entity.Product;
import com.example.ecombazaarx.repository.CartItemRepository;
import com.example.ecombazaarx.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<EcoAlternativeDTO> recommendAlternatives(Long cartId) {

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        List<EcoAlternativeDTO> results = new ArrayList<>();

        for (CartItem item : items) {

            Product p = item.getProduct();

            List<Product> alternatives =
                    productRepository
                            .findTop3ByCategoryAndCarbonImpactKgLessThanAndIdNotOrderByCarbonImpactKgAsc(
                                    p.getCategory(),
                                    p.getCarbonImpactKg(),
                                    p.getId()
                            );

            if (alternatives.isEmpty()) {
                results.add(
                        EcoAlternativeDTO.builder()
                                .originalProductId(p.getId())
                                .alternativeProductId(null)
                                .alternativeName("Best low-impact choice already selected ✔")
                                .price(p.getPrice())
                                .carbonImpactKg(p.getCarbonImpactKg())
                                .build()
                );
            } else {
                for (Product alt : alternatives) {
                    results.add(
                            EcoAlternativeDTO.builder()
                                    .originalProductId(p.getId())
                                    .alternativeProductId(alt.getId())
                                    .alternativeName(alt.getName())
                                    .price(alt.getPrice())
                                    .carbonImpactKg(alt.getCarbonImpactKg())
                                    .build()
                    );
                }
            }
        }
        return results;
    }
}
