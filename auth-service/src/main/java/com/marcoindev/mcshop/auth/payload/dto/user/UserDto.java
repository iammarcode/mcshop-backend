package com.marcoindev.mcshop.auth.payload.dto.user;

import com.marcoindev.mcshop.auth.entity.UserEntity;
import com.marcoindev.mcshop.auth.entity.UserEntity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;

    @Email(message = "Email should be valid")
    private String email;

    private boolean emailVerified;

    @Size(max = 100, message = "username must be less than 100 characters")
    private String username;

    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .emailVerified(entity.isEmailVerified())
                .username(entity.getUsername())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}