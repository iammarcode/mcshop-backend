package com.marcoindev.mcshop.order.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.order.payload.request.PlaceOrderRequest;
import com.marcoindev.mcshop.order.payload.response.PlaceOrderResponse;
import com.marcoindev.mcshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<PlaceOrderResponse>> place(
            @RequestBody @Valid PlaceOrderRequest request,
            @RequestHeader("X-User-Id") String userId) {
        PlaceOrderResponse response = orderService.placeOrder(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}