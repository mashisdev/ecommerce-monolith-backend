package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(OrderRequest request);
    OrderDto getOrderById(Long orderId);
    Page<OrderDto> getOrdersByUser(UUID userId, Pageable pageable);
    Page<OrderDto> getAllOrders(Pageable pageable);
    OrderDto updateOrderStatus(Long orderId, String newStatus);
    boolean isOrderOwner(Long orderId, UUID userId);
    void deleteOrder(Long orderId);
    void cancelOrder(Long orderId, UUID userId);
}
