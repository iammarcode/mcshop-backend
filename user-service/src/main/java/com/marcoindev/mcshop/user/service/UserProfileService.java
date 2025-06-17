package com.marcoindev.mcshop.user.service;

import com.marcoindev.mcshop.user.payload.request.CreateProfileRequest;
import com.marcoindev.mcshop.user.payload.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getProfileById(String userId);
    UserProfileResponse createProfile(CreateProfileRequest request);
}
