package com.marcoindev.mcshop.user.payload.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferenceDto {
    private String userId;  // Matches users.id

    private String defaultProviderId;

    private String enabledProviders;

    private String aiPreferences;

    private String defaultModel;

    private Theme theme = Theme.SYSTEM;

    private String notificationPrefs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum Theme {
        LIGHT, DARK, SYSTEM
    }
}