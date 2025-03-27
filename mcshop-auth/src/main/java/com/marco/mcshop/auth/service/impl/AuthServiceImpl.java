package com.marco.mcshop.auth.service.impl;

import com.marco.mcshop.auth.entity.CustomerEntity;
import com.marco.mcshop.auth.exception.auth.RefreshTokenInvalidException;
import com.marco.mcshop.auth.payload.dto.customer.CustomerDto;
import com.marco.mcshop.auth.payload.mapper.CustomerMapper;
import com.marco.mcshop.auth.payload.request.CustomerLoginRequest;
import com.marco.mcshop.auth.payload.request.CustomerRegisterRequest;
import com.marco.mcshop.auth.payload.response.CustomerLoginResponse;
import com.marco.mcshop.auth.payload.response.CustomerRegisterResponse;
import com.marco.mcshop.auth.payload.response.RefreshTokenResponse;
import com.marco.mcshop.auth.repository.CustomerRepository;
import com.marco.mcshop.auth.service.AuthService;
import com.marco.mcshop.auth.service.CustomerService;
import com.marco.mcshop.auth.service.EmailService;
import com.marco.mcshop.auth.service.OtpService;
import com.marco.mcshop.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private EmailService emailService;

    @Override
    public void requestOtp(String email) {
        // check email
        boolean existsByEmail = customerRepository.existsByEmail(email);
        if (existsByEmail) {
            throw new RuntimeException();
        }

        String otp = otpService.generateOTP(email);
        emailService.sendSimpleMessage(email, "One-time password", otp);
    }

    @Override
    @Transactional
    public CustomerRegisterResponse register(CustomerRegisterRequest registerReq) {
        // check otp
        String otpCached = otpService.getOtpByKey(registerReq.getEmail());
        if (!registerReq.getOtp().equals(otpCached)) {
            throw new RuntimeException();
        }
        otpService.clearOtpByKey(registerReq.getEmail());

        // create customer
        CustomerEntity customerEntity = customerMapper.toEntity(registerReq);
        CustomerEntity customerSaved = customerRepository.save(customerEntity);

        // gen token
        UserDetails userDetails = User.builder()
                .username(customerSaved.getEmail())
                .password(customerSaved.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .authorities(List.of())
                .credentialsExpired(false)
                .disabled(false)
                .build();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return CustomerRegisterResponse.builder()
                .refreshToken(refreshToken)
                .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                .accessToken(accessToken)
                .accessExpireAt(jwtUtil.getRefreshExpirationTime())
                .customer(CustomerDto.fromEntity(customerSaved))
                .build();
    }

    @Override
    public CustomerLoginResponse login(CustomerLoginRequest customerLoginReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customerLoginReq.getEmail(),
                        customerLoginReq.getPassword()
                )
        );

        UserDetails customerFound = customerService.findUserDetailsByEmail(customerLoginReq.getEmail());

        String accessToken = jwtUtil.generateAccessToken(customerFound);
        String refreshToken = jwtUtil.generateRefreshToken(customerFound);

        return CustomerLoginResponse.builder()
                .accessToken(accessToken)
                .accessExpireAt(jwtUtil.getAccessExpirationTime())
                .refreshToken(refreshToken)
                .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RefreshTokenInvalidException(authHeader);
        }

        String refreshToken = authHeader.substring(7);
        String email = jwtUtil.getUsername(refreshToken);
        if (email != null) {
            UserDetails userDetails = customerService.findUserDetailsByEmail(email);

            if (jwtUtil.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

                return RefreshTokenResponse.builder()
                        .refreshToken(newRefreshToken)
                        .refreshExpireAt(jwtUtil.getRefreshExpirationTime())
                        .accessToken(newAccessToken)
                        .accessExpireAt(jwtUtil.getAccessExpirationTime())
                        .build();
            }
        }
        throw new RefreshTokenInvalidException(refreshToken);
    }
}
