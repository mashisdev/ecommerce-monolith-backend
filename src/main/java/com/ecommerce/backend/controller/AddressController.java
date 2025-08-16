package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.AddressRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AddressController {
    ResponseEntity<AddressDto> getMyAddress(UserEntity user);
    ResponseEntity<AddressDto> getAddressByUserId(UUID userId);
    ResponseEntity<AddressDto> createAddress(UserEntity user, AddressRequest request);
    ResponseEntity<AddressDto> updateAddress(UserEntity user, AddressRequest request);
}
