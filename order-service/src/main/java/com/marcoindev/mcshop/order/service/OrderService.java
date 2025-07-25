package com.marcoindev.mcshop.order.service;

import com.marcoindev.mcshop.order.payload.request.PlaceOrderRequest;
import com.marcoindev.mcshop.order.payload.response.PlaceOrderResponse;

public interface OrderService {
    PlaceOrderResponse placeOrder(PlaceOrderRequest request, String userId);
}
