package com.marcoindev.mcshop.product.service.impl;

import com.marcoindev.mcshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.marcoindev.mcshop.product.entity.ProductEntity;
import com.marcoindev.mcshop.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<ProductEntity> getProductById(String id) {
        return productRepository.findById(id);
    }
}