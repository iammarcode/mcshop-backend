package com.marcoindev.mcshop.auth.service;

import com.marcoindev.mcshop.auth.payload.request.RefreshTokenRequest;
import com.marcoindev.mcshop.auth.payload.request.UserLoginRequest;
import com.marcoindev.mcshop.auth.payload.request.UserRegisterRequest;
import com.marcoindev.mcshop.auth.payload.response.RefreshTokenResponse;
import com.marcoindev.mcshop.auth.payload.response.UserLoginResponse;
import com.marcoindev.mcshop.auth.payload.response.UserRegisterResponse;

public interface AuthService {

    void requestOtp(String email);

    UserRegisterResponse register(UserRegisterRequest user);

    UserLoginResponse login(UserLoginRequest loginReq);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
