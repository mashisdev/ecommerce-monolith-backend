package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(String name);
    CategoryDto getCategoryById(Long categoryId);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long categoryId, String name);
    void deleteCategory(Long categoryId);
}
