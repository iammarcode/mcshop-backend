package com.marcoindev.mcshop.auth.controller;

import com.marcoindev.mcshop.auth.payload.request.OtpRequest;
import com.marcoindev.mcshop.auth.payload.request.RefreshTokenRequest;
import com.marcoindev.mcshop.auth.payload.request.UserLoginRequest;
import com.marcoindev.mcshop.auth.payload.request.UserRegisterRequest;
import com.marcoindev.mcshop.auth.payload.response.RefreshTokenResponse;
import com.marcoindev.mcshop.auth.payload.response.UserLoginResponse;
import com.marcoindev.mcshop.auth.payload.response.UserRegisterResponse;
import com.marcoindev.mcshop.auth.service.AuthService;
import com.marcoindev.mcshop.common.payload.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/otp")
    public ResponseEntity<ApiResponse<String>> otp(@RequestBody OtpRequest otpRequest) throws Exception {
        authService.requestOtp(otpRequest.getEmail());

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("Request OTP Successfully")
                .build());
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody @Valid UserLoginRequest userLoginReq) {
        UserLoginResponse loginResponse = authService.login(userLoginReq);

        return ResponseEntity.ok(ApiResponse.<UserLoginResponse>builder()
                .data(loginResponse)
                .build());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody @Valid UserRegisterRequest userRegisterReq) {
        UserRegisterResponse response = authService.register(userRegisterReq);

        return ResponseEntity.ok(ApiResponse.<UserRegisterResponse>builder()
                .data(response)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
                .data(refreshTokenResponse)
                .build());
    }
}
