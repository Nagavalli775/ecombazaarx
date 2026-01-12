package com.example.ecombazaarx.controller;

import com.example.ecombazaarx.entity.Role;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.ecombazaarx.entity.Product;
import com.example.ecombazaarx.repository.ProductRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.ecombazaarx.service.AdminReportService;




import java.util.List;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AdminReportService adminReportService;



    // ✅ Get all USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.USER)
                .toList();
    }

    // ✅ Get all SELLERS
    @GetMapping("/sellers")
    public List<User> getAllSellers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.SELLER)
                .toList();
    }
    @GetMapping("/stats")
    public Map<String, Long> getAdminStats() {

        long totalUsers = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == com.example.ecombazaarx.entity.Role.USER)
                .count();

        long totalSellers = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == com.example.ecombazaarx.entity.Role.SELLER)
                .count();

        return Map.of(
                "totalUsers", totalUsers,
                "totalSellers", totalSellers
        );
    }

    @GetMapping("/carbon/monthly")
    public Map<String, Double> getMonthlyCarbonTrend() {

        // TEMP demo data (safe, no DB dependency)
        Map<String, Double> trend = new HashMap<>();

        trend.put("Jan", 120.5);
        trend.put("Feb", 98.2);
        trend.put("Mar", 140.0);
        trend.put("Apr", 110.8);
        trend.put("May", 160.3);
        trend.put("Jun", 130.9);

        return trend;
    }

    @GetMapping("/top-eco-products")
    public List<Product> getTopEcoProducts() {
        return productRepository.findTop5ByOrderByCarbonImpactKgAsc();
    }

    @GetMapping("/eco-report")
    public ResponseEntity<byte[]> downloadEcoReport() {

        byte[] pdf = adminReportService.generateEcoReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=EcoBasaarX_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}
