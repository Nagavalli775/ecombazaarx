package com.example.ecombazaarx.controller;

import com.example.ecombazaarx.dto.SellerProfileDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecombazaarx.service.SellerProfileService;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = "*")
public class SellerController {

    private final SellerProfileService sellerProfileService;

    public SellerController(SellerProfileService sellerProfileService) {
        this.sellerProfileService = sellerProfileService;
    }


@GetMapping("/profile")
public SellerProfileDTO getSellerProfile(Authentication authentication) {

    System.out.println("LOGGED-IN USERNAME = " + authentication.getName());
    System.out.println("ROLES = " + authentication.getAuthorities());

    return sellerProfileService.getProfileByLoggedInEmail(authentication.getName());
}


}
