package com.marcoindev.mcshop.product.service;

import com.marcoindev.mcshop.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ProductService {
    IPage<ProductEntity> getAllProducts(long page, long size);
    Optional<ProductEntity> getProductById(String id);
}
