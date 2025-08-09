package com.ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String imageUrl;
    private BigDecimal unitPrice;
    private Long categoryId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
