package com.example.ecombazaarx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String phone;
    private String role;
}
