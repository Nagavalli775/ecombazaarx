package com.example.ecombazaarx.service;

import com.example.ecombazaarx.entity.Product;
import com.example.ecombazaarx.repository.ProductRepository;
import com.example.ecombazaarx.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class AdminReportService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public AdminReportService(UserRepository userRepository,
                              ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public byte[] generateEcoReport() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 11);

            document.add(new Paragraph("EcoBasaarX – Admin Eco Report", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Platform Summary", headerFont));

            long totalUsers = userRepository.findAll()
                    .stream().filter(u -> u.getRole().name().equals("USER")).count();

            long totalSellers = userRepository.findAll()
                    .stream().filter(u -> u.getRole().name().equals("SELLER")).count();

            long totalProducts = productRepository.count();

            document.add(new Paragraph("Total Users: " + totalUsers, textFont));
            document.add(new Paragraph("Total Sellers: " + totalSellers, textFont));
            document.add(new Paragraph("Total Products: " + totalProducts, textFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Top Eco-Friendly Products", headerFont));

            List<Product> ecoProducts =
                    productRepository.findTop5ByOrderByCarbonImpactKgAsc();

            for (Product p : ecoProducts) {
                document.add(new Paragraph(
                        "- " + p.getName() +
                        " | Carbon Impact: " + p.getCarbonImpactKg() + " kg",
                        textFont
                ));
            }

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return out.toByteArray();
    }
}
