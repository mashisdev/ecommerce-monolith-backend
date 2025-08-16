package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.AddressRequest;
import com.ecommerce.backend.entity.Address;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.exception.resource.ResourceAlreadyExistsException;
import com.ecommerce.backend.exception.resource.ResourceNotFoundException;
import com.ecommerce.backend.exception.user.UserNotFoundException;
import com.ecommerce.backend.mapper.AddressMapper;
import com.ecommerce.backend.mapper.UserMapper;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.user.UserRepository;
import com.ecommerce.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public AddressDto findByUserId(UUID userId) {
        Address address = userRepository.findById(userId)
                .map(User::getAddress)
                .orElse(null);
        if (address == null) {
            throw new ResourceNotFoundException("Address not found for user with ID: " + userId);
        }
        return addressMapper.addressToAddressDto(address);
    }

    @Override
    @Transactional
    public AddressDto createAddress(UUID userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));

        if (user.getAddress() != null) {
            throw new ResourceAlreadyExistsException("User with id " + userId + " already has an address.");
        }

        Address newAddress = new Address();
        newAddress.setUser(userMapper.userToUserEntity(user));
        addressMapper.updateAddressFromRequest(request, newAddress);

        Address savedAddress = addressRepository.save(newAddress);
        log.info("Address created for user with ID: {}", userId);
        return addressMapper.addressToAddressDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(UUID userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));

        Address existingAddress = user.getAddress();
        if (existingAddress == null) {
            throw new ResourceNotFoundException("Address not found for user with ID: " + userId);
        }

        addressMapper.updateAddressFromRequest(request, existingAddress);

        Address savedAddress = addressRepository.save(existingAddress);
        log.info("Address updated for user with ID: {}", userId);
        return addressMapper.addressToAddressDto(savedAddress);
    }
}