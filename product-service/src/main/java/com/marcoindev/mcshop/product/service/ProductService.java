package com.marcoindev.mcshop.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marcoindev.mcshop.product.entity.ProductEntity;

import java.util.Optional;

public interface ProductService {
    IPage<ProductEntity> getAllProducts(long page, long size);
    Optional<ProductEntity> getProductById(String id);
}
