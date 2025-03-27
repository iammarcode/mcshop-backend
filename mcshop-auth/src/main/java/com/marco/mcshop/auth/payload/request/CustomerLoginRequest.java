package com.marco.mcshop.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
