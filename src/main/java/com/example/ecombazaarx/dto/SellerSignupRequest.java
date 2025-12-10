package com.example.ecombazaarx.dto;

import lombok.Data;

@Data
public class SellerSignupRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String shopName;
    private String shopAddress;
    private String businessType;
    private String gstNumber;
    private String bankAccountNumber;
    private String ifscCode;
    private Boolean termsAccepted;
}
