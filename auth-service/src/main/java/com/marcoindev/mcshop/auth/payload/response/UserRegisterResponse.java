package com.marcoindev.mcshop.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marcoindev.mcshop.auth.payload.dto.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterResponse {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessExpireAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshExpireAt;

    private UserDto user;
}


