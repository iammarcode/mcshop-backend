package com.marcoindev.mcshop.product.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcoindev.mcshop.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
} 