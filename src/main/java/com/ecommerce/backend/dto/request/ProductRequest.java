package com.ecommerce.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @URL(message = "Invalid image URL format")
        @Size(max = 255, message = "Image URL must not exceed 255 characters")
        String imageUrl,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than or equal to 0.01")
        BigDecimal unitPrice,

        @NotNull(message = "Category ID cannot be null")
        Long categoryId,

        @NotNull(message = "Brand ID cannot be null")
        Long brandId
) {}
