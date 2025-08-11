package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.BrandController;
import com.ecommerce.backend.dto.BrandDto;
import com.ecommerce.backend.dto.request.BrandRequest;
import com.ecommerce.backend.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandControllerImpl implements BrandController {

    private final BrandService brandService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BrandDto> createBrand(@Valid @RequestBody BrandRequest request) {
        log.info("Received request to create a brand with name: {}", request.name());
        BrandDto newBrand = brandService.createBrand(request.name());
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<BrandDto>> getAllBrands(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        log.info("Fetching all brands with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<BrandDto> brands = brandService.getAllBrands(pageable);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long brandId) {
        log.info("Fetching brand with ID: {}", brandId);
        BrandDto brand = brandService.getBrandById(brandId);
        return ResponseEntity.ok(brand);
    }

    @GetMapping
    public ResponseEntity<Page<BrandDto>> searchBrands(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<BrandDto> brands = brandService.searchBrandsByName(name, pageable);
        return ResponseEntity.ok(brands);
    }

    @PutMapping("/{brandId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BrandDto> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody BrandRequest request) {
        log.info("Received request to update brand with ID: {} to name: {}", brandId, request.name());
        BrandDto updatedBrand = brandService.updateBrand(brandId, request.name());
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
        log.info("Received request to delete brand with ID: {}", brandId);
        brandService.deleteBrand(brandId);
        return ResponseEntity.noContent().build();
    }
}
