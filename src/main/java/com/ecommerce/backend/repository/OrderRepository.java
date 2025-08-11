package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    Page<Order> findByUserEmail(String email, Pageable pageable);
    Page<Order> findByUser_Id(UUID userId, Pageable pageable);

}
