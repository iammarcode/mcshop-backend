package com.marcoindev.mcshop.user.service;

import com.marcoindev.mcshop.user.payload.request.CreateAddressRequest;
import com.marcoindev.mcshop.user.payload.response.UserAddressResponse;

import java.util.List;

public interface UserAddressService {
    List<UserAddressResponse> getAddressesByUserId(String userId);
    
    UserAddressResponse getAddressById(String addressId, String userId);
    
    UserAddressResponse getDefaultAddress(String userId);
    
    UserAddressResponse createAddress(CreateAddressRequest request);
    
    UserAddressResponse updateAddress(String addressId, CreateAddressRequest request, String userId);
    
    void deleteAddress(String addressId, String userId);
    
    UserAddressResponse setDefaultAddress(String addressId, String userId);
} 