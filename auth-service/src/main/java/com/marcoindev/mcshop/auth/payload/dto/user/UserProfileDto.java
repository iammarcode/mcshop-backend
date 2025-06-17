package com.marcoindev.mcshop.auth.payload.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileDto {
    private String userId;

    private String firstname;
    private String lastname;
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
