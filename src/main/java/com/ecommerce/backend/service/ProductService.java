package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(Long categoryId, String image, String name, String description, BigDecimal price);
    ProductDto getProductById(Long productId);
    Page<ProductDto> getAllProducts(Pageable pageable);
    Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable);
    void disableById(Long id);
    void enableById(Long id);
    ProductDto searchProduct(String searchValue);
    ProductDto updateProduct(Long productId, Long categoryId, String image, String name, String description, BigDecimal price);
    void deleteProduct(Long productId);
}
