package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.BrandDto;
import com.ecommerce.backend.dto.request.BrandRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface BrandController {

    ResponseEntity<BrandDto> createBrand(BrandRequest request);
    ResponseEntity<Page<BrandDto>> getAllBrands(Pageable pageable);
    ResponseEntity<BrandDto> getBrandById(@PathVariable Long brandId);
    ResponseEntity<Page<BrandDto>> searchBrands(String name, Pageable pageable);
    ResponseEntity<BrandDto> updateBrand(Long brandId, BrandRequest request);
    ResponseEntity<Void> deleteBrand(@PathVariable Long brandId);
}
