package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.ProductController;
import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
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
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Received request to create a new product.");
        ProductDto newProduct = productService.createProduct(request);
        log.info("Successfully created product with ID: {}", newProduct.getId());
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        log.info("Received request to get product with ID: {}", productId);
        ProductDto product = productService.getProductById(productId);
        log.info("Successfully retrieved product with ID: {}", productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get all products. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ProductDto> products = productService.getAllProducts(pageable);
        log.info("Successfully retrieved products. Total elements: {}", products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get products by category ID: {}. Page: {}, Size: {}", categoryId, pageable.getPageNumber(), pageable.getPageSize());
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, pageable);
        log.info("Successfully retrieved products for category ID: {}. Total elements: {}", categoryId, products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {
        log.info("Received request to update product with ID: {}", productId);
        ProductDto updatedProduct = productService.updateProduct(productId, request);
        log.info("Successfully updated product with ID: {}", productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.info("Received request to delete product with ID: {}", productId);
        productService.deleteProduct(productId);
        log.info("Successfully deleted product with ID: {}", productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableProduct(@PathVariable Long productId) {
        productService.disableById(productId);
    }

    @PutMapping("/{productId}/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableProduct(@PathVariable Long productId) {
        productService.enableById(productId);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductDto> searchProduct(@RequestParam String searchValue) {
        log.info("Received request to search for product with value: {}", searchValue);
        ProductDto product = productService.searchProduct(searchValue);
        return ResponseEntity.ok(product);
    }
}
