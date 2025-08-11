package com.ecommerce.backend.dto;

import com.ecommerce.backend.entity.order.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private String orderTrackingNumber;
    private int totalQuantity;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UUID userId;
    private List<OrderItemDto> orderItems;
}
