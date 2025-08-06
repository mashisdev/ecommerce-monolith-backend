package com.ecommerce.backend.dto;

import com.ecommerce.backend.entity.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
        private UUID id;
        private String firstname;
        private String lastname;
        private String email;
        @Enumerated(EnumType.STRING)
        private Role role;
}
