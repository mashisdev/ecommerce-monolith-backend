package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductController {

    ResponseEntity<ProductDto> createProduct(ProductRequest request);
    ResponseEntity<ProductDto> getProductById(Long productId);
    ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable);
    ResponseEntity<Page<ProductDto>> getProductsByCategory(Long categoryId, Pageable pageable);
    ResponseEntity<ProductDto> updateProduct(Long productId, ProductRequest request);
    ResponseEntity<Void> deleteProduct(Long productId);
    void disableProduct(Long productId);
    void enableProduct(Long productId);
    ResponseEntity<ProductDto> searchProduct(String searchValue);
}
