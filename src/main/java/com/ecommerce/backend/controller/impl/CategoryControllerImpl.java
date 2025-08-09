package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.CategoryController;
import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.dto.request.CategoryRequest;
import com.ecommerce.backend.service.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequest request) {
        log.info("Received request to create a new category with name: {}", request.name());
        CategoryDto newCategory = categoryService.createCategory(request.name());
        log.info("Successfully created category with ID: {}", newCategory.getId());
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        log.info("Received request to get category with ID: {}", categoryId);
        CategoryDto category = categoryService.getCategoryById(categoryId);
        log.info("Successfully retrieved category with ID: {}", categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategories(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get all categories. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<CategoryDto> categories = categoryService.getAllCategories(pageable);
        log.info("Successfully retrieved categories. Total elements: {}", categories.getTotalElements());
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest request) {
        log.info("Received request to update category with ID: {}. New name: {}", categoryId, request.name());
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, request.name());
        log.info("Successfully updated category with ID: {}", updatedCategory.getId());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        log.info("Received request to delete category with ID: {}", categoryId);
        categoryService.deleteCategory(categoryId);
        log.info("Successfully deleted category with ID: {}", categoryId);
        return ResponseEntity.noContent().build();
    }
}
