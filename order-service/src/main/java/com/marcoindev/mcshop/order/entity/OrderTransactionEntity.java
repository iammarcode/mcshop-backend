package com.marcoindev.mcshop.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orders_transaction")
public class OrderTransactionEntity {
    @TableId
    private String id;
    private BigDecimal amount;
    private String currency;
    private String provider;
    private String status;
    private String orderId;
    private String idempotencyKey;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private java.time.LocalDateTime deletedAt;
} 