package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.ProductController;
import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.product.CreateProductRequest;
import com.ecommerce.backend.dto.request.product.UpdateProductRequest;
import com.ecommerce.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Received request to create a new product: {}", request.name());
        ProductDto newProduct = productService.createProduct(request);
        log.info("Product created with ID: {}", newProduct.getId());
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        log.info("Fetching product with ID: {}", productId);
        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        log.info("Searching products with criteria: name='{}', active={}, categoryId={}, brandId={}", name, active, categoryId, brandId);
        Page<ProductDto> products = productService.searchProducts(name, active, categoryId, brandId, pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("Received request to update product with ID: {}", productId);
        ProductDto updatedProduct = productService.updateProduct(productId, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.info("Received request to delete product with ID: {}", productId);
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
