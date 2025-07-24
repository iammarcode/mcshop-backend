package com.marcoindev.mcshop.order.service;

import com.marcoindev.mcshop.order.payload.request.PurchaseRequest;
import com.marcoindev.mcshop.order.payload.response.PurchaseResponse;

public interface OrderService {
    PurchaseResponse purchaseProduct(PurchaseRequest request);
}
