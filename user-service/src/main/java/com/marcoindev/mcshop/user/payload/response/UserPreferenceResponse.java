package com.marcoindev.mcshop.user.payload.response;

import com.marcoindev.mcshop.user.payload.dto.UserPreferenceDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceResponse {


    private UserPreferenceDto preference;
}


