package com.marcoindev.mcshop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.marcoindev.mcshop.user.entity.UserAddressEntity;
import com.marcoindev.mcshop.user.exception.address.AddressNotFoundException;
import com.marcoindev.mcshop.user.payload.request.CreateAddressRequest;
import com.marcoindev.mcshop.user.payload.response.UserAddressResponse;
import com.marcoindev.mcshop.user.repository.UserAddressMapper;
import com.marcoindev.mcshop.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    
    @Autowired
    private UserAddressMapper addressMapper;

    @Override
    public List<UserAddressResponse> getAddressesByUserId(String userId) {
        List<UserAddressEntity> addresses = addressMapper.selectList(
            new QueryWrapper<UserAddressEntity>()
                .eq("user_id", userId)
                .isNull("deleted_at")
                .orderByDesc("is_default")
                .orderByDesc("created_at")
        );
        
        return addresses.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserAddressResponse getAddressById(String addressId, String userId) {
        UserAddressEntity address = addressMapper.selectOne(
            new QueryWrapper<UserAddressEntity>()
                .eq("id", addressId)
                .eq("user_id", userId)
                .isNull("deleted_at")
        );
        
        if (address == null) {
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
        
        return convertToResponse(address);
    }

    @Override
    public UserAddressResponse getDefaultAddress(String userId) {
        UserAddressEntity address = addressMapper.selectOne(
            new QueryWrapper<UserAddressEntity>()
                .eq("user_id", userId)
                .eq("is_default", true)
                .isNull("deleted_at")
        );
        
        if (address == null) {
            throw new AddressNotFoundException("No default address found for user: " + userId);
        }
        
        return convertToResponse(address);
    }

    @Override
    @Transactional
    public UserAddressResponse createAddress(CreateAddressRequest request) {
        // If this is the first address or marked as default, unset other default addresses
        if (request.getIsDefault()) {
            addressMapper.update(null, 
                new UpdateWrapper<UserAddressEntity>()
                    .eq("user_id", request.getUserId())
                    .isNull("deleted_at")
                    .set("is_default", false)
            );
        }
        
        UserAddressEntity address = UserAddressEntity.builder()
            .userId(request.getUserId())
            .addressLine1(request.getAddressLine1())
            .addressLine2(request.getAddressLine2())
            .postalCode(request.getPostalCode())
            .city(request.getCity())
            .country(request.getCountry())
            .phone(request.getPhone())
            .isDefault(request.getIsDefault())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        addressMapper.insert(address);
        return convertToResponse(address);
    }

    @Override
    @Transactional
    public UserAddressResponse updateAddress(String addressId, CreateAddressRequest request, String userId) {
        UserAddressEntity existingAddress = addressMapper.selectOne(
            new QueryWrapper<UserAddressEntity>()
                .eq("id", addressId)
                .eq("user_id", userId)
                .isNull("deleted_at")
        );
        
        if (existingAddress == null) {
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
        
        // If setting as default, unset other default addresses
        if (request.getIsDefault()) {
            addressMapper.update(null, 
                new UpdateWrapper<UserAddressEntity>()
                    .eq("user_id", userId)
                    .ne("id", addressId)
                    .isNull("deleted_at")
                    .set("is_default", false)
            );
        }
        
        existingAddress.setAddressLine1(request.getAddressLine1());
        existingAddress.setAddressLine2(request.getAddressLine2());
        existingAddress.setPostalCode(request.getPostalCode());
        existingAddress.setCity(request.getCity());
        existingAddress.setCountry(request.getCountry());
        existingAddress.setPhone(request.getPhone());
        existingAddress.setIsDefault(request.getIsDefault());
        existingAddress.setUpdatedAt(LocalDateTime.now());
        
        addressMapper.updateById(existingAddress);
        return convertToResponse(existingAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(String addressId, String userId) {
        UserAddressEntity address = addressMapper.selectOne(
            new QueryWrapper<UserAddressEntity>()
                .eq("id", addressId)
                .eq("user_id", userId)
                .isNull("deleted_at")
        );
        
        if (address == null) {
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
        
        // Soft delete
        address.setDeletedAt(LocalDateTime.now());
        addressMapper.updateById(address);
    }

    @Override
    @Transactional
    public UserAddressResponse setDefaultAddress(String addressId, String userId) {
        UserAddressEntity address = addressMapper.selectOne(
            new QueryWrapper<UserAddressEntity>()
                .eq("id", addressId)
                .eq("user_id", userId)
                .isNull("deleted_at")
        );
        
        if (address == null) {
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
        
        // Unset all other default addresses for this user
        addressMapper.update(null, 
            new UpdateWrapper<UserAddressEntity>()
                .eq("user_id", userId)
                .isNull("deleted_at")
                .set("is_default", false)
        );
        
        // Set this address as default
        address.setIsDefault(true);
        address.setUpdatedAt(LocalDateTime.now());
        addressMapper.updateById(address);
        
        return convertToResponse(address);
    }
    
    private UserAddressResponse convertToResponse(UserAddressEntity entity) {
        return UserAddressResponse.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .addressLine1(entity.getAddressLine1())
            .addressLine2(entity.getAddressLine2())
            .postalCode(entity.getPostalCode())
            .city(entity.getCity())
            .country(entity.getCountry())
            .phone(entity.getPhone())
            .isDefault(entity.getIsDefault())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
} 