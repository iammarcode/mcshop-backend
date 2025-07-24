package com.marcoindev.mcshop.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("product_inventory")
public class ProductInventoryEntity {
    @TableId
    private String id;
    private Integer quantity;
    private Integer reservedQuantity;
    private String productId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private java.time.LocalDateTime deletedAt;
} 