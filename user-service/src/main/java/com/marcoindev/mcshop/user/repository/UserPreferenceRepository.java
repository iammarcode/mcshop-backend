package com.marcoindev.mcshop.user.repository;

import com.marcoindev.mcshop.user.entity.UserPreferenceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends CrudRepository<UserPreferenceEntity, String> {
}
