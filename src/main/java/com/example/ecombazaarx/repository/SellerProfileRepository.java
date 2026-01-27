package com.example.ecombazaarx.repository;

import com.example.ecombazaarx.entity.SellerProfile;
import com.example.ecombazaarx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {

    Optional<SellerProfile> findByUser(User user);
}
