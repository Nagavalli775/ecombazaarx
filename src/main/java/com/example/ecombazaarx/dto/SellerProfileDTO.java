package com.example.ecombazaarx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfileDTO {

    private String username;
    private String shopName;
    private String shopAddress;
    private String businessType;
    private String gstNumber;
}
