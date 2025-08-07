package com.ecommerce.backend.dto.request.user;

public record RegisterRequest (
        String firstname,
        String lastname,
        String email,
        String password
) {}
