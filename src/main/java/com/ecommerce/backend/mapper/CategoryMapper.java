package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.CategoryDto;
import com.ecommerce.backend.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    // Category <-> CategoryDto
    Category categoryDtoToCategory(CategoryDto categoryDto);
    CategoryDto categoryToCategoryDto(Category category);
}
