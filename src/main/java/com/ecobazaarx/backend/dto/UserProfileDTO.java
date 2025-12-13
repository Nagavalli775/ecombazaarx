package com.ecobazaarx.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private Long phone;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;
    private Double ecoScore;
}
