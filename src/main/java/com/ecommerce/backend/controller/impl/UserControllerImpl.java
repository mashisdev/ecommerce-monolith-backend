package com.ecommerce.backend.controller.impl;

import com.ecommerce.backend.controller.UserController;
import com.ecommerce.backend.dto.UserDto;
import com.ecommerce.backend.dto.request.user.UpdateUserRequest;
import com.ecommerce.backend.entity.user.Role;
import com.ecommerce.backend.exception.user.NotAllowedToChangeCredentialsException;
import com.ecommerce.backend.mapper.UserMapper;
import com.ecommerce.backend.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    @GetMapping("/me")
    @RateLimiter(name = "userRateLimiter")
    public ResponseEntity<UserDto> findMeByEmail() {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Received request to get authenticated user's details for email: {}", authenticatedUserEmail);

        UserDto userDto = userService.findByEmail(authenticatedUserEmail);
        log.info("Successfully retrieved details for user: {}", authenticatedUserEmail);
        return ResponseEntity.ok(userDto);
    }

    @Override
    @GetMapping("/{id}")
    @RateLimiter(name = "userRateLimiter")
    public ResponseEntity<UserDto> findById(@PathVariable UUID id) {
        log.info("Received request to find user by ID: {}", id);

        UserDto userDto = userService.findById(id);
        log.info("Successfully retrieved user with ID: {}", id);
        return ResponseEntity.ok(userDto);
    }

    @Override
    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @RateLimiter(name = "userRateLimiter")
    public ResponseEntity<Page<UserDto>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to find all users");

        Page<UserDto> users = userService.findAll(pageable);
        log.info("Successfully retrieved all users. Total: {}", users.getSize());
        return ResponseEntity.ok(users);
    }

    @Override
    @PutMapping("/{id}")
    @RateLimiter(name = "userRateLimiter")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Received update request for user ID: {} from authenticated user: {}", id, authenticatedUserEmail);
        UserDto currentUser = userService.findByEmail(authenticatedUserEmail);
        UserDto targetUserInRequest = userService.findById(updateUserRequest.id());

        if (!currentUser.getId().equals(targetUserInRequest.getId()) || !currentUser.getId().equals(id)) {
            log.warn("Authorization failed: User {} attempted to update user ID {} (target ID in request: {}). Not allowed.",
                    authenticatedUserEmail, id, updateUserRequest.id());
            throw new NotAllowedToChangeCredentialsException("Not allowed to change another user's credentials");
        }
        log.debug("Authorization successful: User {} is allowed to update user ID {}.", authenticatedUserEmail, id);

        UserDto userToUpdate = userMapper.updateUserRequestToUserDto(updateUserRequest);
        userToUpdate.setRole(Role.USER);

        UserDto updatedUser = userService.update(userToUpdate);
        log.info("Successfully updated user with ID: {}", updatedUser.getId());
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    @DeleteMapping("/{id}")
    @RateLimiter(name = "userRateLimiter")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Received delete request for user ID: {} from authenticated user: {}", id, authenticatedUserEmail);

        UserDto currentUser = userService.findByEmail(authenticatedUserEmail);
        UserDto targetUserToDelete = userService.findById(id);

        if (!currentUser.getId().equals(targetUserToDelete.getId())) {
            log.warn("Authorization failed: User {} attempted to delete user ID {}. Not allowed.",
                    authenticatedUserEmail, id);
            throw new NotAllowedToChangeCredentialsException("Not allowed to change another user's credentials");
        }
        log.debug("Authorization successful: User {} is allowed to delete user ID {}.", authenticatedUserEmail, id);

        userService.delete(id);
        log.info("Successfully deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
