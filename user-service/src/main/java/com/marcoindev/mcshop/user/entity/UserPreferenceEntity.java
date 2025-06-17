package com.marcoindev.mcshop.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_preference")
public class UserPreferenceEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userId;  // Matches users.id

    @Column(name = "default_provider_id", columnDefinition = "CHAR(36)", nullable = false)
    private String defaultProviderId;

    @Column(name = "enabled_providers", columnDefinition = "JSON", nullable = false)
    private String enabledProviders;

    @Column(name = "ai_preferences", columnDefinition = "JSON")
    private String aiPreferences;

    @Column(name = "default_model", length = 100)
    private String defaultModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false)
    @Builder.Default
    private Theme theme = Theme.SYSTEM;

    @Column(name = "notif_pref", columnDefinition = "JSON", nullable = false)
    private String notifPref;

    @UpdateTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    public enum Theme {
        LIGHT, DARK, SYSTEM
    }
}