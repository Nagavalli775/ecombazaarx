package com.ecobazaarx.backend.controller;

import com.ecobazaarx.backend.dto.UserSignupRequestDTO.java;
import com.ecobazaarx.backend.dto.SellerSignupRequestDTO.java;
import com.ecobazaarx.backend.dto.LoginRequestDTO.java;
import com.ecobazaarx.backend.entity.User;
import com.ecobazaarx.backend.enums.UserRole;
import com.ecobazaarx.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ---------------- USER SIGNUP ----------------
    @PostMapping("/signup/user")
    public ResponseEntity<?> userSignup(
            @Valid @RequestBody UserSignupRequestDTO request
    ) {
        authService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    // ---------------- SELLER SIGNUP ----------------
    @PostMapping("/signup/seller")
    public ResponseEntity<?> sellerSignup(
            @Valid @RequestBody SellerSignupRequestDTO request
    ) {
        authService.registerSeller(request);
        return ResponseEntity.ok("Seller registered successfully");
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}
