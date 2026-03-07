package com.example.ecombazaarx.service;

import com.example.ecombazaarx.dto.ProductDTO;
import com.example.ecombazaarx.entity.Product;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.repository.ProductRepository;
import com.example.ecombazaarx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /* ================= CREATE ================= */
    public ProductDTO createProduct(ProductDTO dto, User seller) {

        // Fetch seller from DB (important)
        User dbSeller = userRepository
                .findByUsername(seller.getUsername())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .carbonImpactKg(dto.getCarbonImpactKg())
                .ecoVerified(dto.getEcoVerified())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .seller(dbSeller)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        return mapToDTO(productRepository.save(product));
    }

    /* ================= READ ALL ================= */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* ================= READ BY ID ================= */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    /* ================= UPDATE ================= */
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCarbonImpactKg(dto.getCarbonImpactKg());
        product.setEcoVerified(dto.getEcoVerified());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return mapToDTO(productRepository.save(product));
    }

    /* ================= DELETE ================= */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    /* ================= ECO FRIENDLY ALTERNATIVES ================= */
    public List<ProductDTO> getEcoFriendlyAlternatives(
            String category,
            Double carbonImpactKg,
            Long excludeProductId
    ) {
        return productRepository
                .findTop3ByCategoryAndCarbonImpactKgLessThanAndIdNotOrderByCarbonImpactKgAsc(
                        category,
                        carbonImpactKg,
                        excludeProductId
                )
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /* ================= DTO MAPPER ================= */
    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .carbonImpactKg(product.getCarbonImpactKg())
                .ecoVerified(product.getEcoVerified())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .sellerId(product.getSeller() != null ? product.getSeller().getId() : null)
                .sellerName(product.getSeller() != null ? product.getSeller().getUsername() : null)
                .build();
    }
}
