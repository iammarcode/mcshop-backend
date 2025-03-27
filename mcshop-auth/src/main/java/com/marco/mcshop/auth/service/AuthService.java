package com.marco.mcshop.auth.service;

import com.marco.mcshop.auth.payload.request.CustomerLoginRequest;
import com.marco.mcshop.auth.payload.request.CustomerRegisterRequest;
import com.marco.mcshop.auth.payload.response.CustomerLoginResponse;
import com.marco.mcshop.auth.payload.response.CustomerRegisterResponse;
import com.marco.mcshop.auth.payload.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void requestOtp(String email);

    CustomerRegisterResponse register(CustomerRegisterRequest customer);

    CustomerLoginResponse login(CustomerLoginRequest loginReq);

    RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
