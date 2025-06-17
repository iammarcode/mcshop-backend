package com.marcoindev.mcshop.order.service;

import com.marcoindev.mcshop.order.payload.request.OrderCreateRequest;
import com.marcoindev.mcshop.order.payload.response.OrderCreateResponse;

public interface OrderService {
    OrderCreateResponse createPayment(OrderCreateRequest request);

}
