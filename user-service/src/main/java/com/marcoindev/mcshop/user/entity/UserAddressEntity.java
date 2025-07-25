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
@TableName("user_address")
public class UserAddressEntity {

    @TableId(value = "id")
    private String id;

    private String userId;

    private String addressLine1;

    private String addressLine2;

    private String postalCode;

    private String city;

    private String country;

    private String phone;

    private Boolean isDefault;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
} 