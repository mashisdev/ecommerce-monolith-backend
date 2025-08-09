package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.exception.category.CategoryNotFoundException;
import com.ecommerce.backend.exception.product.ProductNotFoundException;
import com.ecommerce.backend.mapper.ProductMapper;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        log.info("Creating new product with name: {}", request.name());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + request.categoryId() + " not found."));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        log.info("Fetching product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."));
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        log.info("Fetching all products, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        log.info("Fetching products by category id: {}", categoryId);
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public void disableById(Long id) {
        log.info("Disabling product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void enableById(Long id) {
        log.info("Enabling product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
        product.setActive(true);
        productRepository.save(product);
    }

    @Override
    public ProductDto searchProduct(String searchValue) {
        log.info("Searching for product with value: {}", searchValue);
        Product product = productRepository.findByNameContainingIgnoreCase(searchValue)
                .orElseThrow(() -> new ProductNotFoundException("Product with search value '" + searchValue + "' not found."));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long productId, ProductRequest request) {
        log.info("Updating product with id: {}", productId);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + request.categoryId() + " not found."));

        existingProduct.setName(request.name());
        existingProduct.setDescription(request.description());
        existingProduct.setImageUrl(request.imageUrl());
        existingProduct.setUnitPrice(request.unitPrice());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Deleting product with id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product with id " + productId + " not found.");
        }
        productRepository.deleteById(productId);
    }
}
