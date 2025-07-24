package com.marcoindev.mcshop.order.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseResponse {
    private boolean success;
    private String orderId;
    private String message;
    private String paymentIntentClientSecret;
    private String paymentUrl; // Stripe Checkout session URL
} 