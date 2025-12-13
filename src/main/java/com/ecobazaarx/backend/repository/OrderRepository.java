package com.ecobazaarx.backend.repository;

import com.ecobazaarx.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}