package com.ecommerce.backend.entity.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private boolean enabled;
    private LocalDateTime verificationCodeExpiresAt;
    private Integer verificationCode;
    private String resetToken;
    private Instant resetTokenExpiration;
}
