package com.example.ecombazaarx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class pageController {
    @GetMapping("/pageSignin")
    public String roleLogin() {
        return "signin";
    }

    @GetMapping("/registerUser")
    public String userSignup() {
        return "register";
    }
   
    @GetMapping("/registerSeller")
    public String sellerSignup() {
        return "SellerSignup";
    }

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard() {
        return "SellerDashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "AdminDashboard";
    }
    
}
