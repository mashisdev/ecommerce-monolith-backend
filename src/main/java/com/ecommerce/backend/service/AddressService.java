package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.AddressRequest;

import java.util.UUID;

public interface AddressService {
    AddressDto findByUserId(UUID userId);
    AddressDto createAddress(UUID userId, AddressRequest request);
    AddressDto updateAddress(UUID userId, AddressRequest request);
}
