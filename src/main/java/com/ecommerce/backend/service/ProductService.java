package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(Long categoryId, String image, String name, String description, BigDecimal price);
    ProductDto getProductById(Long productId);
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByCategory(Long categoryId);
    void disableById(Long id);
    void enableById(Long id);
    ProductDto searchProduct(String searchValue);
    ProductDto updateProduct(Long productId, Long categoryId, String image, String name, String description, BigDecimal price);
    void deleteProduct(Long productId);
}
