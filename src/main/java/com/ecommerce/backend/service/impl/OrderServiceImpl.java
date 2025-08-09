package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.OrderDto;
import com.ecommerce.backend.dto.request.OrderItemRequest;
import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.order.Order;
import com.ecommerce.backend.entity.order.OrderStatus;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.exception.orders.InvalidOrderStatusException;
import com.ecommerce.backend.exception.orders.OrderNotFoundException;
import com.ecommerce.backend.exception.product.ProductNotFoundException;
import com.ecommerce.backend.exception.user.UserNotFoundException;
import com.ecommerce.backend.mapper.OrderMapper;
import com.ecommerce.backend.mapper.UserMapper;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.user.UserRepository;
import com.ecommerce.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        Order order = new Order();
        order.setUser(userMapper.userToUserEntity(user));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTrackingNumber(UUID.randomUUID().toString());

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ProductNotFoundException("Product with id " + itemRequest.productId() + " not found."));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getUnitPrice());
            order.add(orderItem);

            totalPrice = totalPrice.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            totalQuantity += orderItem.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with tracking number: {}", savedOrder.getOrderTrackingNumber());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        log.info("Fetching order with id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDto> getOrdersByUser(UUID userId, Pageable pageable) {
        log.info("Fetching orders for user with id: {}", userId);
        Page<Order> orderPage = orderRepository.findByUser_Id(userId, pageable);
        return orderPage.map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        log.info("Updating status for order with id: {} to {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        try {
            OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
            order.setStatus(status);
            orderRepository.save(order);
            log.info("Order {} status updated successfully.", orderId);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("Invalid status provided: " + newStatus);
        }
    }
}
