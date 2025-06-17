package com.marcoindev.mcshop.auth.repository;

import com.marcoindev.mcshop.auth.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByToken(String token);
}
