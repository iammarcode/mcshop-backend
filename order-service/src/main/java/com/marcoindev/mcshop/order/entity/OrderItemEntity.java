package com.marcoindev.mcshop.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("order_item")
public class OrderItemEntity {
    @TableId
    private String id;
    private Integer quantity;
    private String orderId;
    private String productId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private java.time.LocalDateTime deletedAt;
} 