package com.example.ecombazaarx.service;

import com.example.ecombazaarx.dto.SellerProfileDTO;
import com.example.ecombazaarx.entity.SellerProfile;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.repository.SellerProfileRepository;
import com.example.ecombazaarx.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;

    public SellerProfileService(SellerProfileRepository sellerProfileRepository,
                                UserRepository userRepository) {
        this.sellerProfileRepository = sellerProfileRepository;
        this.userRepository = userRepository;
    }

    public SellerProfileDTO getProfileByLoggedInEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SellerProfile profile = sellerProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Seller profile not found"));

        return new SellerProfileDTO(
                profile.getUsername(),
                profile.getShopName(),
                profile.getShopAddress(),
                profile.getBusinessType(),
                profile.getGstNumber()
        );
    }
}
