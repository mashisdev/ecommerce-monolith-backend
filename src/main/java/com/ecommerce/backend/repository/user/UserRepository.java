package com.ecommerce.backend.repository.user;

import com.ecommerce.backend.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User userEntity);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    List<User> findAll();

    Boolean existsByEmail(String email);

    void deleteById(Long id);
}
