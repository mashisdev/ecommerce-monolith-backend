package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.AddressDto;
import com.ecommerce.backend.dto.request.address.CreateAddressRequest;
import com.ecommerce.backend.dto.request.address.UpdateAddressRequest;
import com.ecommerce.backend.entity.Address;
import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.exception.resources.ResourceNotFoundException;
import com.ecommerce.backend.exception.user.UserNotFoundException;
import com.ecommerce.backend.mapper.AddressMapper;
import com.ecommerce.backend.mapper.UserMapper;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.user.UserRepository;
import com.ecommerce.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressDto createAddress(CreateAddressRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.userId() + " not found."));

        Address newAddress = addressMapper.toEntity(request);
        newAddress.setUser(userMapper.userToUserEntity(user));

        Address savedAddress = addressRepository.save(newAddress);
        return addressMapper.addressToAddressDto(savedAddress);
    }

    @Transactional
    public AddressDto updateAddress(Long id, UpdateAddressRequest request) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + " not found."));

        existingAddress.setStreet(request.street());
        existingAddress.setCity(request.city());
        existingAddress.setState(request.state());
        existingAddress.setZipCode(request.zipCode());
        existingAddress.setCountry(request.country());

        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.addressToAddressDto(updatedAddress);
    }
}