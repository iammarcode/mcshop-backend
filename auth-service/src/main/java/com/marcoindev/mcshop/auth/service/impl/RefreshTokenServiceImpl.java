package com.marcoindev.mcshop.auth.service.impl;

import com.marcoindev.mcshop.auth.entity.RefreshTokenEntity;
import com.marcoindev.mcshop.auth.entity.UserEntity;
import com.marcoindev.mcshop.auth.repository.RefreshTokenRepository;
import com.marcoindev.mcshop.auth.service.RefreshTokenService;
import com.marcoindev.mcshop.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public Boolean verifyExpiration(RefreshTokenEntity token) {
        return token.getExpireAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void saveRefreshToken(UserEntity user, String newToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .token(newToken)
                .expireAt(jwtUtil.getRefreshExpirationTime())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public void deleteByToken(RefreshTokenEntity refreshToken) {
        refreshToken.setDeletedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }
}
