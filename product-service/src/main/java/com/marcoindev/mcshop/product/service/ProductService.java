package com.marcoindev.mcshop.product.service;

import com.marcoindev.mcshop.product.entity.ProductEntity;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> getAllProducts();
    Optional<ProductEntity> getProductById(String id);
}
