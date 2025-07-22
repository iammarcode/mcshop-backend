package com.marcoindev.mcshop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marcoindev.mcshop.product.entity.ProductEntity;
import com.marcoindev.mcshop.product.repository.ProductMapper;
import com.marcoindev.mcshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    @Override
    public IPage<ProductEntity> getAllProducts(long page, long size) {
        Page<ProductEntity> mybatisPage = new Page<>(page, size);
        return productMapper.selectPage(mybatisPage, new QueryWrapper<>());
    }

    @Override
    public Optional<ProductEntity> getProductById(String id) {
        return Optional.ofNullable(productMapper.selectById(id));
    }
}