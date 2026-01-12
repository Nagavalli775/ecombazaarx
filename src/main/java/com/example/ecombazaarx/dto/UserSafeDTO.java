package com.example.ecombazaarx.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSafeDTO {
    private Long id;
    private String username;
    private String email;
    private Long phone;
    private String role;
    private Double ecoScore;
}
