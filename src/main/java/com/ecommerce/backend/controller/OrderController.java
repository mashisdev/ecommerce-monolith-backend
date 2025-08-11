package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderController {

    ResponseEntity<OrderDto> createOrder(OrderRequest request, UserEntity user);
    ResponseEntity<OrderDto> getOrderById(UUID orderId, UserEntity user);
    ResponseEntity<Page<OrderDto>> searchOrders(
            UUID userId,
            String status,
            String trackingNumber,
            LocalDateTime startDate, LocalDateTime endDate,
            Pageable pageable,
            UserEntity user);
    ResponseEntity<OrderDto> updateOrderStatus(UUID orderId, String newStatus, UserEntity user);
    ResponseEntity<Void> deleteOrder(UUID orderId, UserEntity user);
}
