package com.example.ecombazaarx.repository;

import com.example.ecombazaarx.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}
