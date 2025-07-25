package com.marcoindev.mcshop.user.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressResponse {
    private String id;
    private String userId;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;
    private String country;
    private String phone;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 