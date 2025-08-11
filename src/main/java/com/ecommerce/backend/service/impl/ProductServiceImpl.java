package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.entity.Brand;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.exception.resources.ResourceNotFoundException;
import com.ecommerce.backend.mapper.ProductMapper;
import com.ecommerce.backend.repository.BrandRepository;
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
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        log.info("Creating a new product with name: {}", request.name());

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.categoryId() + " not found."));

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id " + request.brandId() + " not found."));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setImageUrl(request.imageUrl());
        product.setStock(request.stock());
        product.setUnitPrice(request.unitPrice());
        product.setActive(true);
        product.setCategory(category);
        product.setBrand(brand);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        log.info("Fetching all products, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        log.info("Fetching product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found."));
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> searchProductsByName(String name, Pageable pageable) {
        log.info("Searching products by name: '{}' with pagination: {}", name, pageable);
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return products.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public void disableById(Long id) {
        log.info("Disabling product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void enableById(Long id) {
        log.info("Enabling product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
        product.setActive(true);
        productRepository.save(product);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductRequest request) {
        log.info("Updating product with ID: {}", productId);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found."));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.categoryId() + " not found."));

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id " + request.brandId() + " not found."));

        existingProduct.setName(request.name());
        existingProduct.setDescription(request.description());
        existingProduct.setImageUrl(request.imageUrl());
        existingProduct.setStock(request.stock());
        existingProduct.setUnitPrice(request.unitPrice());
        existingProduct.setActive(true);
        existingProduct.setCategory(category);
        existingProduct.setBrand(brand);

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product with ID: {} updated successfully.", updatedProduct.getId());
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Attempting to delete product with ID: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
        log.info("Product with ID: {} deleted successfully.", productId);
    }
}
