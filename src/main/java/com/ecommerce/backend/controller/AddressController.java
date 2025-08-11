package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.address.CreateAddressRequest;
import com.ecommerce.backend.dto.request.address.UpdateAddressRequest;
import org.springframework.http.ResponseEntity;

public interface AddressController {
    ResponseEntity<AddressDto> createAddress(CreateAddressRequest request);
    ResponseEntity<AddressDto> updateAddress(Long id, UpdateAddressRequest request);
}
