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
@TableName("user_profile")
public class UserProfileEntity {

    @TableId(value = "user_id")
    private String userId;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}