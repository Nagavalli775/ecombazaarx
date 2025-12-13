package com.ecobazaarx.backend.repository;

import com.ecobazaarx.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
