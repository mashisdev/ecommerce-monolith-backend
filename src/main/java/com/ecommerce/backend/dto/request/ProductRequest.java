package com.ecommerce.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @Size(max = 255)
        String imageUrl,
        @NotBlank @Size(max = 100)
        String name,
        @Size(max = 500)
        String description,
        @NotNull @DecimalMin("0.01")
        BigDecimal unitPrice,
        @NotNull(message = "Category ID cannot be null")
        Long categoryId
) {}
