package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.dto.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoryController {

    ResponseEntity<CategoryDto> createCategory(CategoryRequest request);
    ResponseEntity<Page<CategoryDto>> getAllCategories(Pageable pageable);
    ResponseEntity<CategoryDto> getCategoryById(Long categoryId);
    ResponseEntity<Page<CategoryDto>> searchCategories(String name, Pageable pageable);
    ResponseEntity<CategoryDto> updateCategory(Long categoryId, CategoryRequest request);
    ResponseEntity<Void> deleteCategory(Long categoryId);
}
