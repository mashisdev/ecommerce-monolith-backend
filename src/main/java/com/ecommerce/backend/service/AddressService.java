package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.CreateAddressRequest;
import com.ecommerce.backend.dto.request.UpdateAddressRequest;

public interface AddressService {
    AddressDto createAddress(CreateAddressRequest request);
    AddressDto updateAddress(Long id, UpdateAddressRequest request);
}
