package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.exception.resources.ResourceAlreadyExistsException;
import com.ecommerce.backend.exception.resources.ResourceNotFoundException;
import com.ecommerce.backend.mapper.CategoryMapper;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            throw new ResourceAlreadyExistsException("Category with name '" + name + "' already exists.");
        }

        Category newCategory = new Category();
        newCategory.setName(name);

        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID '" + categoryId + "' not found."));
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(categoryMapper::categoryToCategoryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> searchCategoriesByName(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(categoryMapper::categoryToCategoryDto);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID '" + categoryId + "' not found."));

        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryId)) {
            throw new ResourceAlreadyExistsException("Category with name '" + name + "' already exists.");
        }

        category.setName(name);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category with ID '" + categoryId + "' not found.");
        }
        categoryRepository.deleteById(categoryId);
    }
}
