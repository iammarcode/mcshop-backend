package com.marcoindev.mcshop.product.service.impl;

import com.marcoindev.mcshop.product.entity.ProductEntity;
import com.marcoindev.mcshop.product.repository.ProductRepository;
import com.marcoindev.mcshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductEntity> getProductById(String id) {
        return productRepository.findById(id);
    }
}