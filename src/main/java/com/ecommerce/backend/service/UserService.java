package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserDto findById(UUID id);
    UserDto findByEmail(String email);
    Page<UserDto> findAll(Pageable pageable);
    UserDto update(UserDto userDto);
    void delete(UUID id);

}
