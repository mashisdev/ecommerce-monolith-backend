package com.ecommerce.backend.entity.user;

import com.ecommerce.backend.entity.Address;
import com.ecommerce.backend.entity.order.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private Set<Order> orders;
    private Address address;
    private boolean enabled;
    private Integer verificationCode;
    private LocalDateTime verificationCodeExpiration;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiration;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
