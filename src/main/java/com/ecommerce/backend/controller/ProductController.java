package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.product.CreateProductRequest;
import com.ecommerce.backend.dto.request.product.UpdateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductController {

    ResponseEntity<ProductDto> createProduct(CreateProductRequest request);
    ResponseEntity<ProductDto> getProductById(Long productId);
    ResponseEntity<Page<ProductDto>> searchProducts(String name,
                                                    Boolean active,
                                                    Long categoryId, Long brandId,
                                                    Pageable pageable);
    ResponseEntity<ProductDto> updateProduct(Long productId, UpdateProductRequest request);
    ResponseEntity<Void> deleteProduct(Long productId);
}
