package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(ProductRequest request);
    ProductDto getProductById(Long productId);
    Page<ProductDto> getAllProducts(Pageable pageable);
    Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable);
    void disableById(Long id);
    void enableById(Long id);
    ProductDto searchProduct(String searchValue);
    ProductDto updateProduct(Long productId, ProductRequest request);
    void deleteProduct(Long productId);
}
