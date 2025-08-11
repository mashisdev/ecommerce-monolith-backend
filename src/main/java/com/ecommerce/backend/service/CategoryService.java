package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDto createCategory(String name);
    CategoryDto getCategoryById(Long categoryId);
    Page<CategoryDto> getAllCategories(Pageable pageable);
    CategoryDto updateCategory(Long categoryId, String name);
    Page<CategoryDto> searchCategoriesByName(String name, Pageable pageable);
    void deleteCategory(Long categoryId);
}
