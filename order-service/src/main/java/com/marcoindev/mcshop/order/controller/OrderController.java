package com.marcoindev.mcshop.order.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.order.payload.request.PurchaseRequest;
import com.marcoindev.mcshop.order.payload.response.PurchaseResponse;
import com.marcoindev.mcshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<PurchaseResponse>> purchase(@RequestBody PurchaseRequest request) {
        PurchaseResponse response = orderService.purchaseProduct(request);
        return ResponseEntity.ok(ApiResponse.<PurchaseResponse>builder().data(response).build());
    }
}