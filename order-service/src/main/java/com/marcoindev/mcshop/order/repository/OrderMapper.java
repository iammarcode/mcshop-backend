package com.marcoindev.mcshop.order.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcoindev.mcshop.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
} 