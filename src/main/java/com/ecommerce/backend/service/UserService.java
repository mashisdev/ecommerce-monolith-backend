package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto findById(UUID id);
    UserDto findByEmail(String email);
    List<UserDto> findAll();
    UserDto update(UserDto userDto);
    void delete(UUID id);

}
