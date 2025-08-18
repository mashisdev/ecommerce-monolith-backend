package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProductId(Long productId);
    boolean existsByProductIdAndOrder_Status(Long productId, OrderStatus status);
}
