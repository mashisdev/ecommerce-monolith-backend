package com.ecommerce.backend.specifications;

import com.ecommerce.backend.entity.order.Order;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.entity.order.OrderStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderSpecifications {

    public static Specification<Order> byUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            Join<Order, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<Order> byStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Order> byOrderTrackingNumber(String trackingNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("orderTrackingNumber"), "%" + trackingNumber + "%");
    }

    public static Specification<Order> byCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), startDate, endDate);
    }
}