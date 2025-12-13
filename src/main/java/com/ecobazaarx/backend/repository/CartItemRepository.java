package com.ecobazaarx.backend.repository;

import com.ecobazaarx.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartId(Long cartId);
}