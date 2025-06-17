package com.marcoindev.mcshop.order.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(ApiResponse.<String>builder().data(userId).build());
    }
}