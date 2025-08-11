package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.OrderController;
import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.order.OrderStatus;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.exception.orders.InvalidOrderStatusException;
import com.ecommerce.backend.exception.resources.UnauthorizedActionException;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderRequest request,
                                                @AuthenticationPrincipal UserEntity user) {
        if (!request.userId().equals(user.getId())) {
            throw new UnauthorizedActionException("Cannot create an order for another user.");
        }
        log.info("Creating a new order for user ID: {}", user.getId());
        OrderDto newOrder = orderService.createOrder(request);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID orderId,
                                                 @AuthenticationPrincipal UserEntity user) {
        log.info("User {} is fetching order with ID: {}", user.getId(), orderId);
        if (!"ADMIN".equals(user.getRole()) && !orderService.isOrderOwner(orderId, user.getId())) {
            throw new UnauthorizedActionException("You are not authorized to view this order.");
        }
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<OrderDto>> searchOrders(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @PageableDefault(size = 10, sort = "createdDate") Pageable pageable,
            @AuthenticationPrincipal UserEntity user) {

        if (!"ADMIN".equals(user.getRole())) {
            if (userId != null && !userId.equals(user.getId())) {
                throw new UnauthorizedActionException("You can only search for your own orders.");
            }
            userId = user.getId();
        }

        OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOrderStatusException("Invalid status provided: " + status);
            }
        }
        Page<OrderDto> orders = orderService.searchOrders(userId, orderStatus, trackingNumber, startDate, endDate, pageable);
        return ResponseEntity.ok(orders);
    }

    @Override
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable UUID orderId,
                                                      @RequestParam String newStatus,
                                                      @AuthenticationPrincipal UserEntity user) {
        if ("ADMIN".equals(user.getRole())) {
            log.info("ADMIN is updating order {} status to: {}", orderId, newStatus);
            OrderDto updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(updatedOrder);
        }

        if (!orderService.isOrderOwner(orderId, user.getId())) {
            throw new UnauthorizedActionException("You are not authorized to change the status of this order.");
        }
        OrderDto existingOrder = orderService.getOrderById(orderId);
        if (existingOrder.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("Only orders with PENDING status can be cancelled.");
        }
        if (!"CANCELLED".equalsIgnoreCase(newStatus)) {
            throw new InvalidOrderStatusException("You can only change the status to 'CANCELLED'.");
        }

        log.info("User {} is cancelling order with ID: {}", user.getId(), orderId);
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @Override
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId,
                                            @AuthenticationPrincipal UserEntity user) {
        if ("ADMIN".equals(user.getRole())) {
            log.info("ADMIN is deleting order with ID: {}", orderId);
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        }

        if (!orderService.isOrderOwner(orderId, user.getId())) {
            throw new UnauthorizedActionException("You are not authorized to delete this order.");
        }
        OrderDto order = orderService.getOrderById(orderId);
        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Only orders with status CANCELLED or DELIVERED can be deleted.");
        }

        log.info("User {} is deleting order with ID: {}", user.getId(), orderId);
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}