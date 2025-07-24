package com.marcoindev.mcshop.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("order")
public class OrderEntity {
    @TableId
    private String id;
    private String status;
    private BigDecimal total;
    private String userId;
    private String userAddressId;
    private String paymentIntentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}