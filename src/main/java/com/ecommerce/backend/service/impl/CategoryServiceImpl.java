package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.mapper.CategoryMapper;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(String name) {
        return null;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return null;
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, String name) {
        return null;
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }
}
