package com.ecommerce.backend.repository.user;

import com.ecommerce.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User userEntity);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    Page<User> findAll(Pageable pageable);

    Boolean existsByEmail(String email);

    void deleteById(UUID id);
}
