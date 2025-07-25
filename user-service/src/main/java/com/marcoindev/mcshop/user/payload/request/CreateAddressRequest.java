package com.marcoindev.mcshop.user.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "Postal code is required")
    private String postalCode;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotNull(message = "Is default flag is required")
    private Boolean isDefault;
} 