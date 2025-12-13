package com.ecobazaarx.backend.repository;

import com.ecobazaarx.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}
