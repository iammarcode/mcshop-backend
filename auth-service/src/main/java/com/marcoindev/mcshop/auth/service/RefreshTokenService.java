package com.marcoindev.mcshop.auth.service;


import com.marcoindev.mcshop.auth.entity.RefreshTokenEntity;
import com.marcoindev.mcshop.auth.entity.UserEntity;

import java.util.Optional;

public interface RefreshTokenService {

    public Optional<RefreshTokenEntity> findByToken(String token);

    public Boolean verifyExpiration(RefreshTokenEntity token);

    public void saveRefreshToken(UserEntity user, String newToken);

    void deleteByToken(RefreshTokenEntity refreshToken);
}