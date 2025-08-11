package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.AddressController;
import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.address.CreateAddressRequest;
import com.ecommerce.backend.dto.request.address.UpdateAddressRequest;
import com.ecommerce.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        log.info("Received request to create address for user with id: {}", request.userId());
        AddressDto newAddress = addressService.createAddress(request);
        log.info("Address created with id: {}", newAddress.getId());
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @Valid @RequestBody UpdateAddressRequest request) {
        log.info("Received request to update address with id: {}", id);
        AddressDto updatedAddress = addressService.updateAddress(id, request);
        log.info("Address with id {} updated successfully.", updatedAddress.getId());
        return ResponseEntity.ok(updatedAddress);
    }
}
