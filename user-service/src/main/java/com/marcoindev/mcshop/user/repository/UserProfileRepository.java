package com.marcoindev.mcshop.user.repository;

import com.marcoindev.mcshop.user.entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfileEntity, String> {
    Optional<UserProfileEntity> findByUserId(String userId);
}
