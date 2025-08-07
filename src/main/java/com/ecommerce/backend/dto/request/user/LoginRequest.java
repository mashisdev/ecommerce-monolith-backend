package com.ecommerce.backend.dto.request.user;

public record LoginRequest (
        String email,
        String password
) {}
