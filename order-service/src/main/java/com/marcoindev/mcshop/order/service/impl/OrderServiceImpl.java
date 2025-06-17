package com.marcoindev.mcshop.order.service.impl;

import com.marcoindev.mcshop.order.config.RabbitMQConfig;
import com.marcoindev.mcshop.order.payload.request.OrderCreateRequest;
import com.marcoindev.mcshop.order.payload.response.OrderCreateResponse;
import com.marcoindev.mcshop.order.repository.OrderRepository;
import com.marcoindev.mcshop.order.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public OrderCreateResponse createPayment(OrderCreateRequest request) {
        // Send payment request to queue
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, request);
        return new OrderCreateResponse("Payment processing started");
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void processPayment(OrderCreateRequest request) {
        try {
            Stripe.apiKey = stripeApiKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(request.getAmount().longValue())
                    .setCurrency(request.getCurrency().toLowerCase())
                    .putMetadata("userId", request.getUserId())
                    .setPaymentMethod(request.getStripeToken())
                    .setConfirm(true)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // TODO: Save payment record
//            OrderEntity entity = OrderEntity.builder()
//                    .userId(request.getUserId())
//                    .amount(request.getAmount())
//                    .currency(request.getCurrency())
//                    .status(paymentIntent.getStatus())
//                    .gatewayId(paymentIntent.getId())
//                    .build();
//            orderRepository.save(entity);

        } catch (StripeException e) {
            // Handle error and retry logic
        }
    }
}