package com.marcoindev.mcshop.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user_preference")
public class UserPreferenceEntity {

    @TableId(value = "user_id")
    private String userId;  // Matches users.id

    private String defaultProviderId;

    private String enabledProviders;

    private String aiPreferences;

    private String defaultModel;

    private Theme theme = Theme.SYSTEM;

    private String notifPref;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum Theme {
        LIGHT, DARK, SYSTEM
    }
}