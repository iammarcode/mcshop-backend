package com.marcoindev.mcshop.auth.feign;

import com.marcoindev.mcshop.auth.payload.dto.user.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${user.service.name}",
        path = "${user.service.url}"
)
public interface UserClient {

    @GetMapping("/profile/{userId}")
    UserProfileDto getUserProfile(@PathVariable("userId") String userId);

    @PostMapping("/profile")
    UserProfileDto createUserProfile(@RequestBody UserProfileDto profile);
}