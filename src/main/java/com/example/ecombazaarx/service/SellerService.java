package com.example.ecombazaarx.service;

import com.example.ecombazaarx.dto.SellerSignupRequest;
import com.example.ecombazaarx.entity.Role;
import com.example.ecombazaarx.entity.SellerProfile;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.repository.SellerProfileRepository;
import com.example.ecombazaarx.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class SellerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerProfileRepository sellerProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerSeller(SellerSignupRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.SELLER);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        userRepository.save(user);

        SellerProfile sellerProfile = new SellerProfile();
        sellerProfile.setUser(user);
        sellerProfile.setShopName(request.getShopName());
        sellerProfile.setShopAddress(request.getShopAddress());
        sellerProfile.setBusinessType(request.getBusinessType());
        sellerProfile.setGstNumber(request.getGstNumber());
        sellerProfile.setBankAccountNumber(request.getBankAccountNumber());
        sellerProfile.setIfscCode(request.getIfscCode());
        sellerProfile.setTermsAccepted(request.getTermsAccepted());
        sellerProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        sellerProfile.setCreatedAt(LocalDateTime.now());
        sellerProfile.setUpdatedAt(LocalDateTime.now());

        sellerProfileRepository.save(sellerProfile);

        return "Seller registered successfully";
    }
}


