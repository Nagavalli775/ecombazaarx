package com.example.ecombazaarx.controller;

import com.example.ecombazaarx.dto.LoginRequest;
import com.example.ecombazaarx.dto.UserSignupRequest;
import com.example.ecombazaarx.dto.SellerSignupRequest;
import com.example.ecombazaarx.dto.UserProfileDto;
import com.example.ecombazaarx.service.UserService;
import com.example.ecombazaarx.service.SellerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @PostMapping("/signup/user")
    public ResponseEntity<String> signupUser(@RequestBody UserSignupRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/signup/seller")
    public ResponseEntity<String> signupSeller(@RequestBody SellerSignupRequest request) {
        return ResponseEntity.ok(sellerService.registerSeller(request));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/user/profile")
    public UserProfileDto getProfile(Authentication auth) {
        return userService.getUserProfile((auth.getName()));
    }
}
