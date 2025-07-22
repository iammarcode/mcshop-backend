package com.marcoindev.mcshop.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcoindev.mcshop.auth.entity.RefreshTokenEntity;
import com.marcoindev.mcshop.auth.entity.UserEntity;
import com.marcoindev.mcshop.auth.repository.RefreshTokenMapper;
import com.marcoindev.mcshop.auth.service.RefreshTokenService;
import com.marcoindev.mcshop.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;


    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return Optional.ofNullable(refreshTokenMapper.selectOne(new QueryWrapper<RefreshTokenEntity>().eq("token", token)));
    }

    @Override
    public Boolean verifyExpiration(RefreshTokenEntity token) {
        return token.getExpireAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void saveRefreshToken(UserEntity user, String newToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenEntity.setToken(newToken);
        refreshTokenEntity.setExpireAt(jwtUtil.getRefreshExpirationTime());
        refreshTokenMapper.insert(refreshTokenEntity);
    }

    @Override
    public void deleteByToken(RefreshTokenEntity refreshToken) {
        refreshToken.setDeletedAt(LocalDateTime.now());
        refreshTokenMapper.updateById(refreshToken);
    }
}
