package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.AddressController;
import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.AddressRequest;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<AddressDto> getMyAddress(@AuthenticationPrincipal UserEntity user) {
        log.info("Received request to retrieve address for authenticated user with id: {}", user.getId());
        AddressDto address = addressService.findByUserId(user.getId());
        log.info("Successfully retrieved address with id {} for user with id: {}", address.getId(), user.getId());
        return ResponseEntity.ok(address);
    }

    @Override
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AddressDto> getAddressByUserId(@PathVariable UUID userId) {
        log.info("Admin user requesting address for user with id: {}", userId);
        AddressDto address = addressService.findByUserId(userId);
        log.info("Successfully retrieved address with id {} for user with id: {} by admin request.", address.getId(), userId);
        return ResponseEntity.ok(address);
    }

    @PostMapping()
    public ResponseEntity<AddressDto> createAddress(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody AddressRequest request) {
        log.info("Received request to create address for user with id: {}", user.getId());
        AddressDto newAddress = addressService.createAddress(user.getId(), request);
        log.info("Address created with id: {} for user with id: {}", newAddress.getId(), user.getId());
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<AddressDto> updateAddress(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody AddressRequest request) {
        log.info("Received request to update address for user with id: {}", user.getId());
        AddressDto updatedAddress = addressService.updateAddress(user.getId(), request);
        log.info("Address with id {} updated successfully for user with id: {}.", updatedAddress.getId(), user.getId());
        return ResponseEntity.ok(updatedAddress);
    }
}
