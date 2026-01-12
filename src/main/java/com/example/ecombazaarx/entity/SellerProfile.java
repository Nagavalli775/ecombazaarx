package com.example.ecombazaarx.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seller_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfile {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String username;
    private String shopName;
    private String shopAddress;
    private String businessType;
    private String gstNumber;
    private String bankAccountNumber;
    private String ifscCode;
    private Boolean termsAccepted;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
