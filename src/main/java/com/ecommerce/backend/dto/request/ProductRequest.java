package com.ecommerce.backend.dto.request;

import com.ecommerce.backend.dto.CategoryDto;

import java.math.BigDecimal;

public record ProductRequest (
         Long id,
         String name,
         String description,
         boolean active,
         String imageUrl,
         BigDecimal unitPrice,
         CategoryDto category
) {}
