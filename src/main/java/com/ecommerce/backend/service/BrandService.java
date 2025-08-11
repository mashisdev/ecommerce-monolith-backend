package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.BrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    BrandDto createBrand(String name);
    BrandDto getBrandById(Long brandId);
    Page<BrandDto> getAllBrands(Pageable pageable);
    BrandDto updateBrand(Long brandId, String name);
    Page<BrandDto> searchBrandsByName(String name, Pageable pageable);
    void deleteBrand(Long brandId);
}
