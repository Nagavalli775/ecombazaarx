package com.example.ecombazaarx.repository;

import com.example.ecombazaarx.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findTop3ByCategoryAndCarbonImpactKgLessThanAndIdNotOrderByCarbonImpactKgAsc(
        String category,
        Double carbonImpactKg,
        Long excludeProductId
);
}
