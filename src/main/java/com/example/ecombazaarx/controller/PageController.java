package com.example.ecombazaarx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/landing")
    public String landingPage() {
        return "landing"; // returns landing.html
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard() {
        return "sellerdashboard"; // returns Sellerdashboard.html
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admindashboard"; // returns Admindashboard.html
    }
}
