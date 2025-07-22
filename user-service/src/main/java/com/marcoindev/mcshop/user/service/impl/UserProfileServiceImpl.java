package com.marcoindev.mcshop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcoindev.mcshop.user.entity.UserProfileEntity;
import com.marcoindev.mcshop.user.exception.profile.ProfileNotFoundException;
import com.marcoindev.mcshop.user.payload.request.CreateProfileRequest;
import com.marcoindev.mcshop.user.payload.response.UserProfileResponse;
import com.marcoindev.mcshop.user.repository.UserProfileMapper;
import com.marcoindev.mcshop.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileMapper profileMapper;

    @Override
    public UserProfileResponse getProfileById(String userId) {
        UserProfileEntity profileFound = profileMapper.selectOne(new QueryWrapper<UserProfileEntity>().eq("user_id", userId));
        if (profileFound == null) {
            throw new ProfileNotFoundException("User profile not found with userId: " + userId);
        }

        return UserProfileResponse.builder()
                .phone(profileFound.getPhone())
                .userId(userId)
                .lastName(profileFound.getLastName())
                .firstName(profileFound.getFirstName())
                .updatedAt(profileFound.getUpdatedAt())
                .createdAt(profileFound.getCreatedAt())
                .build();
    }

    @Override
    public UserProfileResponse createProfile(CreateProfileRequest request) {
        UserProfileEntity profile = UserProfileEntity.builder()
                .userId(request.getUserId())
                .phone(request.getPhone())
                .lastName(request.getLastName())
                .build();
        UserProfileEntity savedProfile = null;
        profileMapper.insert(profile);
        savedProfile = profile;

        return UserProfileResponse.builder()
                .phone(savedProfile.getPhone())
                .lastName(savedProfile.getLastName())
                .firstName(savedProfile.getFirstName())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt()).build();
    }
}
