package com.marco.mcshop.auth.payload.dto.customer;

import com.marco.mcshop.auth.entity.CustomerEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String id;

    @Email(message = "Email should be valid")
    private String email;

    private boolean emailVerified;

    @Size(max = 100, message = "First name must be less than 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastName;

    @Size(max = 100, message = "Display name must be less than 100 characters")
    private String displayName;

    private CustomerEntity.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerDto fromEntity(CustomerEntity entity) {
        return CustomerDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .emailVerified(entity.isEmailVerified())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .displayName(entity.getDisplayName())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}