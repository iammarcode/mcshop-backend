package com.marcoindev.mcshop.order.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcoindev.mcshop.order.entity.OrderTransactionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderTransactionMapper extends BaseMapper<OrderTransactionEntity> {} 