package com.marcoindev.mcshop.user.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.user.payload.request.CreateProfileRequest;
import com.marcoindev.mcshop.user.payload.response.UserProfileResponse;
import com.marcoindev.mcshop.user.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user/profile")
public class UserProfileController {
    @Autowired
    private UserProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfileById(@RequestHeader("X-User-Id") String userId) {
        UserProfileResponse response = profileService.getProfileById(userId);
        return ResponseEntity.ok(ApiResponse.<UserProfileResponse>builder().data(response).build());
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createProfile(@RequestBody @Valid CreateProfileRequest request) {
        UserProfileResponse response = profileService.createProfile(request);
        return ResponseEntity.ok(response);
    }
}
