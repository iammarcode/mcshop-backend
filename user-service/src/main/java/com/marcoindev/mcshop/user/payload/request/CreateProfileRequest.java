package com.marcoindev.mcshop.user.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProfileRequest {
    @NotBlank
    private String userId;

    private String firstName;

    private String lastName;

    private String phone;
}


