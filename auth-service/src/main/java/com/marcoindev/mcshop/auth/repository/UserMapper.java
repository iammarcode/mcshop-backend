package com.marcoindev.mcshop.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcoindev.mcshop.auth.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
} 