package com.ecommerce.backend.repository.user;

import com.ecommerce.backend.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User userEntity);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    List<User> findAll();

    Boolean existsByEmail(String email);

    void deleteById(UUID id);
}
