package com.example.ecombazaarx.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
}
