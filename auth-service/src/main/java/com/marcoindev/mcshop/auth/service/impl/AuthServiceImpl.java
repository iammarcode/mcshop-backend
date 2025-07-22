package com.marcoindev.mcshop.auth.service.impl;

import com.marcoindev.mcshop.auth.entity.RefreshTokenEntity;
import com.marcoindev.mcshop.auth.entity.UserEntity;
import com.marcoindev.mcshop.auth.exception.auth.OtpValidationFailedException;
import com.marcoindev.mcshop.auth.exception.auth.RefreshTokenInvalidException;
import com.marcoindev.mcshop.auth.exception.user.UserAlreadyExistException;
import com.marcoindev.mcshop.auth.exception.user.UserNotFoundException;
import com.marcoindev.mcshop.auth.feign.UserClient;
import com.marcoindev.mcshop.auth.payload.dto.user.UserDto;
import com.marcoindev.mcshop.auth.payload.dto.user.UserProfileDto;
import com.marcoindev.mcshop.auth.payload.request.RefreshTokenRequest;
import com.marcoindev.mcshop.auth.payload.request.UserLoginRequest;
import com.marcoindev.mcshop.auth.payload.request.UserRegisterRequest;
import com.marcoindev.mcshop.auth.payload.response.RefreshTokenResponse;
import com.marcoindev.mcshop.auth.payload.response.UserLoginResponse;
import com.marcoindev.mcshop.auth.payload.response.UserRegisterResponse;
import com.marcoindev.mcshop.auth.repository.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcoindev.mcshop.auth.service.AuthService;
import com.marcoindev.mcshop.auth.service.RefreshTokenService;
import com.marcoindev.mcshop.common.email.service.EmailService;
import com.marcoindev.mcshop.common.otp.service.OtpService;
import com.marcoindev.mcshop.common.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final UserClient userClient;

    public AuthServiceImpl(UserMapper userMapper, OtpService otpService, JwtUtil jwtUtil, EmailService emailService, RefreshTokenService refreshTokenService, UserClient userClient) {
        this.userMapper = userMapper;
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.userClient = userClient;
    }

    @Override
    public void requestOtp(String email) {
        // check email
        boolean existsByEmail = userMapper.selectCount(new QueryWrapper<UserEntity>().eq("email", email)) > 0;
        if (existsByEmail) {
            throw new UserAlreadyExistException("User already exist with email: " + email);
        }

        String otp = otpService.generateOTP(email);
        emailService.sendSimpleMessage(email, "One-time password", otp);
    }

    @Override
    @Transactional
    public UserRegisterResponse register(UserRegisterRequest request) {
        // check otp
        String otpCached = otpService.getOtpByKey(request.getEmail());
        if (request.getOtp() == null || !request.getOtp().equals(otpCached)) {
            throw new OtpValidationFailedException(request.getOtp());
        }

        otpService.clearOtpByKey(request.getEmail());

        // create user
        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        UserEntity userSaved = null;
        userMapper.insert(userEntity);
        userSaved = userEntity;

        // create user profile
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .userId(userSaved.getId())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone()).build();
        userClient.createUserProfile(userProfileDto);

        // gen token
        String accessToken = jwtUtil.genAccessToken(userSaved.getId());
        String refreshToken = jwtUtil.genRefreshToken(userSaved.getId());

        // save refreshToken
        refreshTokenService.saveRefreshToken(userSaved, refreshToken);

        return UserRegisterResponse.builder()
                .refreshToken(refreshToken)
                .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                .accessToken(accessToken)
                .accessExpireAt(jwtUtil.getRefreshExpirationTime())
                .user(UserDto.fromEntity(userSaved))
                .build();
    }

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginReq) {
        UserEntity userFound = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", userLoginReq.getEmail()));
        if (userFound == null) {
            throw new UserNotFoundException("User not found with email: " + userLoginReq.getEmail());
        }

        String accessToken = jwtUtil.genAccessToken(userFound.getId());
        String refreshToken = jwtUtil.genRefreshToken(userFound.getId());

        // save refreshToken
        refreshTokenService.saveRefreshToken(userFound, refreshToken);

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .accessExpireAt(jwtUtil.getAccessExpirationTime())
                .refreshToken(refreshToken)
                .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String oldRefreshToken = request.getRefreshToken();

        // verify token nature
        if (!jwtUtil.isTokenValid(oldRefreshToken)) {
            throw new RefreshTokenInvalidException("Refresh Token is invalid");
        }

        // verify token in db
        RefreshTokenEntity oldRefreshTokenFound = refreshTokenService.findByToken(oldRefreshToken).orElseThrow(
                () -> new RefreshTokenInvalidException("Refresh Token not found in DB: " + oldRefreshToken)
        );

        // verify user
        String userId = jwtUtil.getSubject(oldRefreshToken);
        UserEntity userFound = userMapper.selectById(userId);
        if (userFound == null) {
            throw new UserNotFoundException("User not found with email: " + userId);
        }

        // disable old refresh token
        refreshTokenService.deleteByToken(oldRefreshTokenFound);

        // rotate refresh token
        String newRefreshToken = jwtUtil.genRefreshToken(userId);
        refreshTokenService.saveRefreshToken(userFound, newRefreshToken);

        // access token
        String newAccessToken = jwtUtil.genRefreshToken(userFound.getId());

        return RefreshTokenResponse.builder()
                .refreshToken(newRefreshToken)
                .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                .accessToken(newAccessToken)
                .accessExpireAt(jwtUtil.getAccessExpirationTime())
                .build();
    }
}
