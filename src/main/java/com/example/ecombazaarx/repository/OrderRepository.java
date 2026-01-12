package com.example.ecombazaarx.repository;

import com.example.ecombazaarx.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}