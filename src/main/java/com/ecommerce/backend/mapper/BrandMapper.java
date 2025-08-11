package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.BrandDto;
import com.ecommerce.backend.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {

    // Brand <-> BrandDto
    BrandDto brandToBrandDto(Brand brand);
}
