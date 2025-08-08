package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.exception.category.CategoryAlreadyExistsException;
import com.ecommerce.backend.exception.category.CategoryNotFoundException;
import com.ecommerce.backend.mapper.CategoryMapper;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            throw new CategoryAlreadyExistsException("Category with name '" + name + "' already exists.");
        }

        Category newCategory = new Category();
        newCategory.setName(name);

        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID '" + categoryId + "' not found."));
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(categoryMapper::categoryToCategoryDto);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID '" + categoryId + "' not found."));

        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryId)) {
            throw new CategoryAlreadyExistsException("Category with name '" + name + "' already exists.");
        }

        category.setName(name);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category with ID '" + categoryId + "' not found.");
        }
        categoryRepository.deleteById(categoryId);
    }
}
