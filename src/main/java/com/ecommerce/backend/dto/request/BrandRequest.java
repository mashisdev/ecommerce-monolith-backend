package com.ecommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BrandRequest(
        @NotBlank(message = "Brand name is required.")
        @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9\\s&.'-]+$", message = "Brand name can only contain letters, numbers, spaces, and special characters (&.'-)")
        String name
) {}
