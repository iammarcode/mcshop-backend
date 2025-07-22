package com.marcoindev.mcshop.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("product")
public class ProductEntity {
    @TableId(value = "id")
    private String id;

    private String name;

    private java.math.BigDecimal price;

    private String description;

    private String imageUrl;

    private String categoryId;

    private java.time.LocalDateTime createdAt;

    private java.time.LocalDateTime updatedAt;

    private java.time.LocalDateTime deletedAt;
}