package com.ecommerce.backend.repository.user;

import com.ecommerce.backend.entity.user.User;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User userEntity) {
        UserEntity entity = userMapper.userToUserEntity(userEntity);
        UserEntity saved = userJpaRepository.save(entity);
        return userMapper.userEntityToUser(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(userMapper::userEntityToUser);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(userMapper::userEntityToUser).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::userEntityToUser);
    }

    @Override
    public Optional<User> findByPasswordResetToken(String passwordResetToken) {
        return userJpaRepository.findByPasswordResetToken(passwordResetToken).map(userMapper::userEntityToUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

}
