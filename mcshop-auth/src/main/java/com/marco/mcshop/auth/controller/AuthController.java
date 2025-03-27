package com.marco.mcshop.auth.controller;

import com.marco.mcshop.auth.payload.request.OtpRequest;
import com.marco.mcshop.auth.payload.request.CustomerLoginRequest;
import com.marco.mcshop.auth.payload.request.CustomerRegisterRequest;
import com.marco.mcshop.auth.payload.response.CustomerLoginResponse;
import com.marco.mcshop.auth.payload.response.CustomerRegisterResponse;
import com.marco.mcshop.auth.service.AuthService;
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
    public ResponseEntity<String> otp(@RequestBody OtpRequest otpRequest) throws Exception {
        authService.requestOtp(otpRequest.getEmail());

        return ResponseEntity.ok("Request OTP Successfully");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CustomerLoginResponse> login(@RequestBody @Valid CustomerLoginRequest customerLoginReq) {
        CustomerLoginResponse loginResponse = authService.login(customerLoginReq);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<CustomerRegisterResponse> login(@RequestBody @Valid CustomerRegisterRequest customerRegisterReq) {
        CustomerRegisterResponse response = authService.register(customerRegisterReq);

        return ResponseEntity.ok(response);
    }
}
