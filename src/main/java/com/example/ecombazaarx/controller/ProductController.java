package com.example.ecombazaarx.controller;

import com.example.ecombazaarx.dto.ProductDTO;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    /* ========== CREATE (SELLER) ========== */
    @PostMapping("/seller/products")
    public ProductDTO createProduct(
            @RequestBody ProductDTO productDTO,
            Principal principal
    ) {
        // TEMP: Replace with DB fetch later
        User seller = new User();
        seller.setUsername(principal.getName());

        return productService.createProduct(productDTO, seller);
    }

    /* ========== READ ALL (PUBLIC) ========== */
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    /* ========== READ BY ID ========== */
    @GetMapping("/products/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /* ========== UPDATE (SELLER) ========== */
    @PutMapping("/seller/products/{id}")
    public ProductDTO updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        return productService.updateProduct(id, productDTO);
    }

    /* ========== DELETE (SELLER) ========== */
    @DeleteMapping("/seller/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    /* ========== ECO ALTERNATIVES ========== */
    @GetMapping("/products/{id}/eco-alternatives")
    public List<ProductDTO> ecoAlternatives(
            @PathVariable Long id,
            @RequestParam String category,
            @RequestParam Double carbonImpactKg
    ) {
        return productService.getEcoFriendlyAlternatives(
                category, carbonImpactKg, id
        );
    }
}
