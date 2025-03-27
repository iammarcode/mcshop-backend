package com.marco.mcshop.auth.payload.mapper;

import com.marco.mcshop.auth.entity.CustomerEntity;
import com.marco.mcshop.auth.payload.request.CustomerRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", expression = "from request")
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "displayName", expression = "from request")
    @Mapping(target = "password", expression = "java(encodePassword(passwordEncoder, request.getPassword()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract CustomerEntity toEntity(CustomerRegisterRequest request);

    protected String encodePassword(PasswordEncoder passwordEncoder, String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}