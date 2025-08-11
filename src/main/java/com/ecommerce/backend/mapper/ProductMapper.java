package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.ProductDto;
import com.ecommerce.backend.dto.request.ProductRequest;
import com.ecommerce.backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "brandName", source = "brand.name")
    ProductDto toDto(Product product);

    Product toEntity(ProductRequest request);
}
