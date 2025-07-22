package com.marcoindev.mcshop.product.service;

import com.marcoindev.mcshop.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Page<ProductEntity> getAllProducts(Pageable pageable);
    Optional<ProductEntity> getProductById(String id);
}
