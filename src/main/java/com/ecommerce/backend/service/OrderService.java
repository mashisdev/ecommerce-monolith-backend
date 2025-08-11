package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(OrderRequest request);
    OrderDto getOrderById(UUID orderId);
    Page<OrderDto> searchOrders(UUID userId,
                                OrderStatus status,
                                String trackingNumber,
                                LocalDateTime startDate, LocalDateTime endDate,
                                Pageable pageable);
    OrderDto updateOrderStatus(UUID orderId, String newStatus);
    boolean isOrderOwner(UUID orderId, UUID userId);
    void deleteOrder(UUID orderId);
}
