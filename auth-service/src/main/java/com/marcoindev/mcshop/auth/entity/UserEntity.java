package com.marcoindev.mcshop.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user")
public class UserEntity {

    @TableId(value = "id")
    private String id;

    private String email;

    @Builder.Default
    private boolean emailVerified = false;

    private String password;

    private String username;

    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum UserStatus {
        ACTIVE, SUSPENDED, DELETED
    }
}