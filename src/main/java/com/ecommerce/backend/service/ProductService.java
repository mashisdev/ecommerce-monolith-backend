package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.product.CreateProductRequest;
import com.ecommerce.backend.dto.request.product.UpdateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest request);
    ProductDto getProductById(Long productId);
    Page<ProductDto> searchProducts(String name,
                                    Boolean active,
                                    Long categoryId, Long brandId,
                                    Pageable pageable);
    ProductDto updateProduct(Long productId, UpdateProductRequest request);
    void deleteProduct(Long productId);
}
