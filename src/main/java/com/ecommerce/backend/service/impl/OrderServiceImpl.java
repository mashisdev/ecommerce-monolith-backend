package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderItemRequest;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.order.Order;
import com.ecommerce.backend.entity.order.OrderStatus;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.exception.order.AddressRequiredException;
import com.ecommerce.backend.exception.product.InsufficientStockException;
import com.ecommerce.backend.exception.order.InvalidOrderStatusException;
import com.ecommerce.backend.exception.product.ProductDisabledException;
import com.ecommerce.backend.exception.resource.ResourceNotFoundException;
import com.ecommerce.backend.exception.user.UserNotFoundException;
import com.ecommerce.backend.mapper.OrderMapper;
import com.ecommerce.backend.mapper.UserMapper;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.user.UserRepository;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.specifications.OrderSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.userId() + " not found."));

        if (user.getAddress() == null) {
            throw new AddressRequiredException("User with id " + request.userId() + " does not have an address. An address is required to create an order.");
        }

        Order order = new Order();
        order.setUser(userMapper.userToUserEntity(user));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTrackingNumber(UUID.randomUUID().toString());

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id " + itemRequest.productId() + " not found."));

            if (!product.isActive()) {
                throw new ProductDisabledException("Product with id " + itemRequest.productId() + " is currently disabled and cannot be ordered.");
            }

            if (product.getStock() < itemRequest.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - itemRequest.quantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getUnitPrice());

            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalPrice = totalPrice.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            totalQuantity += orderItem.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with tracking number: {}", savedOrder.getOrderTrackingNumber());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID orderId) {
        log.info("Fetching order with id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> searchOrders(UUID userId, OrderStatus status, String trackingNumber, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {

        Specification<Order> spec = Specification.unrestricted();

        if (userId != null) {
            spec = spec.and(OrderSpecifications.byUserId(userId));
        }
        if (status != null) {
            spec = spec.and(OrderSpecifications.byStatus(status));
        }
        if (trackingNumber != null) {
            spec = spec.and(OrderSpecifications.byOrderTrackingNumber(trackingNumber));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(OrderSpecifications.byCreationDateBetween(startDate, endDate));
        }

        Page<Order> orders = orderRepository.findAll(spec, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(UUID orderId, String newStatus) {
        log.info("Updating status for order with id: {} to {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + orderId + " not found."));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Cannot update the status of a cancelled order.");
        }

        try {
            OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());

            if (status == OrderStatus.CANCELLED) {
                log.info("Cancelling order {}. Returning stock to inventory.", orderId);
                for (OrderItem item : order.getOrderItems()) {
                    productRepository.findById(item.getProductId()).ifPresent(product -> {
                        product.setStock(product.getStock() + item.getQuantity());
                    });
                }
            }

            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);
            log.info("Order {} status updated successfully.", orderId);
            return orderMapper.toDto(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("Invalid status provided: " + newStatus);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOrderOwner(UUID orderId, UUID userId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getId().equals(userId))
                .orElse(false);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found.");
        }

        orderRepository.deleteById(orderId);
        log.info("Order with ID {} has been permanently deleted by an ADMIN.", orderId);
    }
}
