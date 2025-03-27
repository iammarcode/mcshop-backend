package com.marco.mcshop.auth.controller;

import com.marco.mcshop.auth.payload.request.CustomerLoginRequest;
import com.marco.mcshop.auth.payload.response.CustomerLoginResponse;
import com.marco.mcshop.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<CustomerLoginResponse> login(@RequestBody CustomerLoginRequest customerLoginReq) {
        CustomerLoginResponse loginResponse = authService.login(customerLoginReq);

        return ResponseEntity.ok(loginResponse);
    }
}
