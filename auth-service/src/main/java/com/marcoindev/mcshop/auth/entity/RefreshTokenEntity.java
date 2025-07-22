package com.marcoindev.mcshop.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("refresh_token")
public class RefreshTokenEntity {
    @TableId(value = "id")
    private String id;
    private String userId;
    private String token;
    private java.time.LocalDateTime expireAt;
    private java.time.LocalDateTime deletedAt;
}
