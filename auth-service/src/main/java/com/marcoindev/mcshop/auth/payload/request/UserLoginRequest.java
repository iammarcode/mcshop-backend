package com.marcoindev.mcshop.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
