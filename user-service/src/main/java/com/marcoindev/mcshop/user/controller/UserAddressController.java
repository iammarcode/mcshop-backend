package com.marcoindev.mcshop.user.controller;

import com.marcoindev.mcshop.common.payload.ApiResponse;
import com.marcoindev.mcshop.user.payload.request.CreateAddressRequest;
import com.marcoindev.mcshop.user.payload.response.UserAddressResponse;
import com.marcoindev.mcshop.user.service.UserAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/address")
public class UserAddressController {
    
    @Autowired
    private UserAddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAddressResponse>>> getAddresses(@RequestHeader("X-User-Id") String userId) {
        List<UserAddressResponse> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressResponse>> getAddressById(
            @PathVariable String addressId,
            @RequestParam String userId) {
        UserAddressResponse address = addressService.getAddressById(addressId, userId);
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<UserAddressResponse>> getDefaultAddress(
            @RequestParam String userId) {
        UserAddressResponse address = addressService.getDefaultAddress(userId);
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserAddressResponse>> createAddress(
            @RequestBody @Valid CreateAddressRequest request,
            @RequestHeader("X-User-Id") String userId) {
        // Ensure the user can only create addresses for themselves
        request.setUserId(userId);
        UserAddressResponse address = addressService.createAddress(request);
        return ResponseEntity.ok(ApiResponse.created(address));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressResponse>> updateAddress(
            @PathVariable String addressId,
            @RequestBody @Valid CreateAddressRequest request,
            @RequestHeader("X-User-Id") String userId) {
        // Ensure the user can only update addresses for themselves
        request.setUserId(userId);
        UserAddressResponse address = addressService.updateAddress(addressId, request, userId);
        return ResponseEntity.ok(ApiResponse.updated(address));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable String addressId,
            @RequestHeader("X-User-Id") String userId) {
        addressService.deleteAddress(addressId, userId);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<UserAddressResponse>> setDefaultAddress(
            @PathVariable String addressId,
            @RequestHeader("X-User-Id") String userId) {
        UserAddressResponse address = addressService.setDefaultAddress(addressId, userId);
        return ResponseEntity.ok(ApiResponse.updated(address));
    }
} 