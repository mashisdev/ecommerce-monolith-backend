package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.BrandDto;
import com.ecommerce.backend.entity.Brand;
import com.ecommerce.backend.exception.resource.ResourceAlreadyExistsException;
import com.ecommerce.backend.exception.resource.ResourceNotFoundException;
import com.ecommerce.backend.mapper.BrandMapper;
import com.ecommerce.backend.repository.BrandRepository;
import com.ecommerce.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDto createBrand(String name) {
        log.info("Creating new brand with name: {}", name);
        brandRepository.findByName(name).ifPresent(b -> {
            log.warn("Attempted to create a brand with an existing name: {}", name);
            throw new ResourceAlreadyExistsException("Brand with name '" + name + "' already exists.");
        });

        Brand brand = new Brand();
        brand.setName(name);

        Brand savedBrand = brandRepository.save(brand);
        log.info("Brand created successfully with ID: {}", savedBrand.getId());
        return brandMapper.brandToBrandDto(savedBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDto getBrandById(Long brandId) {
        log.info("Fetching brand with ID: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + brandId));
        return brandMapper.brandToBrandDto(brand);
    }

    @Override
    @Cacheable(value = "BRAND_INFO", unless = "#result==null")
    @Transactional(readOnly = true)
    public Page<BrandDto> getAllBrands(Pageable pageable) {
        log.info("Fetching all brands, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return brandRepository.findAll(pageable)
                .map(brandMapper::brandToBrandDto);
    }

    @Override
    @Transactional
    public BrandDto updateBrand(Long brandId, String name) {
        log.info("Updating brand with ID: {} to new name: {}", brandId, name);
        Brand existingBrand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + brandId));

        brandRepository.findByName(name).ifPresent(b -> {
            if (!b.getId().equals(existingBrand.getId())) {
                log.warn("Attempted to update brand {} to an existing name: {}", brandId, name);
                throw new ResourceAlreadyExistsException("Another brand with name '" + name + "' already exists.");
            }
        });

        existingBrand.setName(name);
        Brand updatedBrand = brandRepository.save(existingBrand);
        log.info("Brand with ID: {} updated successfully.", updatedBrand.getId());
        return brandMapper.brandToBrandDto(updatedBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandDto> searchBrandsByName(String name, Pageable pageable) {
        log.info("Searching brands by name: '{}' with pagination: {}", name, pageable);
        return brandRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(brandMapper::brandToBrandDto);
    }

    @Override
    @Transactional
    public void deleteBrand(Long brandId) {
        log.info("Attempting to delete brand with ID: {}", brandId);
        if (!brandRepository.existsById(brandId)) {
            log.warn("Deletion failed: Brand with ID: {} not found.", brandId);
            throw new ResourceNotFoundException("Brand not found with ID: " + brandId);
        }
        brandRepository.deleteById(brandId);
        log.info("Brand with ID: {} deleted successfully.", brandId);
    }
}
