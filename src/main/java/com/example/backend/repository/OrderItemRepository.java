package com.example.backend.repository;

import com.example.backend.model.OrderItem;
import com.example.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find all order items belonging to a specific order
    List<OrderItem> findByOrder(Order order);
}
