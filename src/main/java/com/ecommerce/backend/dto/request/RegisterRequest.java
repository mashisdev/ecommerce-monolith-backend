package com.ecommerce.backend.dto.request;

public record RegisterRequest (
        String firstname,
        String lastname,
        String email,
        String password
) {}
