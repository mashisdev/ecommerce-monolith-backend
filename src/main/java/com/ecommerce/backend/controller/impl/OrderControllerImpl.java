package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.OrderController;
import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.service.OrderService;
import jakarta.validation.Valid;
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

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN') and (@orderService.isOrderOwner(#orderId, #user.id) or hasAuthority('ADMIN'))")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal UserEntity user) {
        log.info("Received request to get order with ID: {}", orderId);
        OrderDto order = orderService.getOrderById(orderId);
        log.info("Successfully retrieved order with ID: {}", orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN') and (#userId.equals(#user.id) or hasAuthority('ADMIN'))")
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
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        log.info("Received request to update status of order with ID: {} to status: {}", orderId, newStatus);

        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, newStatus);

        log.info("Order status updated successfully for order with ID: {}", orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN') and (@orderService.isOrderOwner(#orderId, #user.id) or hasAuthority('ADMIN'))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserEntity user) {
        log.info("Received request to cancel order with ID: {} from user: {}", orderId, user.getEmail());
        orderService.cancelOrder(orderId, user.getId());
        log.info("Order with ID {} cancelled successfully.", orderId);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        log.info("Received request to permanently delete order with ID: {}", orderId);
        orderService.deleteOrder(orderId);
        log.info("Order with ID {} permanently deleted successfully.", orderId);
    }
}
