package com.example.ecombazaarx.dto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcoAlternativeDTO {
    private Long originalProductId;
    private Long alternativeProductId;
    private String alternativeName;
    private Double price;
    private Double carbonImpactKg;
}
