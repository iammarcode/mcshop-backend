package com.marcoindev.mcshop.order.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @NotBlank(message = "User ID is required")
    @Size(min = 36, max = 36, message = "User ID must be a valid UUID")
    private String userId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a valid ISO 4217 code")
    private String currency;

    @NotBlank(message = "Stripe token is required")
    private String stripeToken;
}
