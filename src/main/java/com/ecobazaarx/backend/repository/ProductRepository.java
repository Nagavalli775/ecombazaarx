package com.ecobazaarx.backend.repository;

import com.ecobazaarx.backend.entity.Product;
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
