package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface OrderController {

    ResponseEntity<OrderDto> createOrder(OrderRequest request);
    ResponseEntity<Page<OrderDto>> getAllOrders(Pageable pageable);
    ResponseEntity<OrderDto> getOrderById(Long orderId, UserEntity user);
    ResponseEntity<Page<OrderDto>> getOrdersByUser(
            UUID userId,
            Pageable pageable,
            UserEntity user);
    ResponseEntity<OrderDto> updateOrderStatus(Long orderId,
                                               String newStatus);
    void cancelOrder(Long orderId, UserEntity user);
    void deleteOrder(Long orderId);
}
