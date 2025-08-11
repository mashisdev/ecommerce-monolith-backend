package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.address.CreateAddressRequest;
import com.ecommerce.backend.dto.request.address.UpdateAddressRequest;

public interface AddressService {
    AddressDto createAddress(CreateAddressRequest request);
    AddressDto updateAddress(Long id, UpdateAddressRequest request);
}
