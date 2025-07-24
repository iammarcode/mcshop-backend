package com.marcoindev.mcshop.order.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    private String userId;
    private List<ProductOrder> products; // List of productId and quantity
    private String userAddressId;
    private String paymentMethodId;
    private String currency;

    @Data
    public static class ProductOrder {
        private String productId;
        private Integer quantity;
    }
} 