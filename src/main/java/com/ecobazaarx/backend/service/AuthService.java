package com.ecobazaarx.backend.service;

import com.ecobazaarx.backend.dto.UserSignupRequestDTO;
import com.ecobazaarx.backend.dto.SellerSignupRequestDTO;
import com.ecobazaarx.backend.dto.LoginRequestDTO;
import com.ecobazaarx.backend.entity.User;
import com.ecobazaarx.backend.enums.UserRole;
import com.ecobazaarx.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---------- USER REGISTRATION ----------
    public void registerUser(UserSignupRequestDTO dto) {

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.USER)
                .ecoScore(0.0)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();

        userRepository.save(user);
    }

    // ---------- SELLER REGISTRATION ----------
    public void registerSeller(SellerSignupRequestDTO dto) {

        User seller = User.builder()
                .username(dto.getBusinessName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.SELLER)
                .ecoScore(0.0)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();

        userRepository.save(seller);
        // seller_profile table entry can be added here later
    }

    // ---------- LOGIN ----------
    public String login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Token logic later (JWT)
        return "Login successful for role: " + user.getRole();
    }
}
