package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(String name);
    CategoryDto getCategoryById(Long categoryId);
    Page<CategoryDto> getAllCategories(Pageable pageable);
    CategoryDto updateCategory(Long categoryId, String name);
    void deleteCategory(Long categoryId);
}
