package com.ecommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateAddressRequest(
        String street,
        String city,
        String state,
        String zipCode,
        String country,
        UUID userId
) {}
