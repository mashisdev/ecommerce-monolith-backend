package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderControllerImpl {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received request to create a new order for user with ID: {}", request.userId());
        OrderDto newOrder = orderService.createOrder(request);
        log.info("Successfully created order with ID: {}", newOrder.getId());
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get all orders. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<OrderDto> orders = orderService.getAllOrders(pageable);
        log.info("Successfully retrieved all orders. Total elements: {}", orders.getTotalElements());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN') or @orderService.getOrderById(#orderId).userId == #user.id")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal UserEntity user) {
        log.info("Received request to get order with ID: {}", orderId);
        OrderDto order = orderService.getOrderById(orderId);
        log.info("Successfully retrieved order with ID: {}", orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or #userId.equals(#user.id)")
    public ResponseEntity<Page<OrderDto>> getOrdersByUser(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserEntity user) {
        log.info("Received request to get orders for user with ID: {}", userId);
        Page<OrderDto> orders = orderService.getOrdersByUser(userId, pageable);
        log.info("Successfully retrieved orders for user ID: {}. Total elements: {}", userId, orders.getTotalElements());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        log.info("Received request to update status of order with ID: {} to status: {}", orderId, newStatus);
        orderService.updateOrderStatus(orderId, newStatus);
        log.info("Order status updated successfully.");
    }
}
