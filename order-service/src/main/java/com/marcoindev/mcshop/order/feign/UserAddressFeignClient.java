package com.marcoindev.mcshop.order.feign;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "${user.service.name}",
        path = "${user.service.path}"
)
public interface UserAddressFeignClient {
    
    @GetMapping("/address/{addressId}")
    UserAddressResponse getAddressById(
            @PathVariable("addressId") String addressId,
            @RequestParam("userId") String userId);

    @GetMapping("/address/default")
    UserAddressResponse getDefaultAddress(@RequestParam("userId") String userId);

    @Data
    class UserAddressResponse {
        public UserAddressDTO data;
    }
    
    @Data
    class UserAddressDTO {
        public String id;
        public String userId;
        public String addressLine1;
        public String addressLine2;
        public String postalCode;
        public String city;
        public String country;
        public String phone;
        public Boolean isDefault;
    }
} 