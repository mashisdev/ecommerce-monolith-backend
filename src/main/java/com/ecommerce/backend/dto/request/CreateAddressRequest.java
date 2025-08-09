package com.ecommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateAddressRequest(
        @NotBlank(message = "Street cannot be blank")
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,

        @NotBlank(message = "City cannot be blank")
        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,

        @NotBlank(message = "State cannot be blank")
        @Size(max = 50, message = "State must not exceed 50 characters")
        String state,

        @NotBlank(message = "Zip code cannot be blank")
        @Size(max = 10, message = "Zip code must not exceed 10 characters")
        String zipCode,

        @NotBlank(message = "Country cannot be blank")
        @Size(max = 50, message = "Country must not exceed 50 characters")
        String country,

        @NotNull(message = "User ID cannot be null")
        UUID userId
) {}
