package com.marcoindev.mcshop.order.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    @NotEmpty(message = "Products list cannot be empty")
    @Valid
    private List<ProductOrder> products; // List of productId and quantity
    
    private String userAddressId; // Optional - will use default address if not provided

    private String currency; // Optional - user chooses currency on Stripe Checkout

    @Data
    public static class ProductOrder {
        @NotBlank(message = "Product ID is required")
        private String productId;
        
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        private Integer quantity;
    }
} 