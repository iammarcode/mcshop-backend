package com.marcoindev.mcshop.order.controller;

import com.marcoindev.mcshop.order.entity.OrderEntity;
import com.marcoindev.mcshop.order.feign.ProductInventoryClient;
import com.marcoindev.mcshop.order.repository.OrderMapper;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final OrderMapper orderMapper;
    private final ProductInventoryClient inventoryClient;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public String handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                String orderId = intent.getMetadata().get("orderId");
                OrderEntity order = orderMapper.selectById(orderId);
                if (order != null && !"PAID".equals(order.getStatus())) {
                    order.setStatus("PAID");
                    order.setPaymentIntentId(intent.getId());
                    orderMapper.updateById(order);
                    // TODO: Finalize inventory if not already done (double-check logic)
                }
            } else if ("payment_intent.failed".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                String orderId = intent.getMetadata().get("orderId");
                OrderEntity order = orderMapper.selectById(orderId);
                if (order != null && !"PAYMENT_FAILED".equals(order.getStatus())) {
                    order.setStatus("PAYMENT_FAILED");
                    order.setPaymentIntentId(intent.getId());
                    orderMapper.updateById(order);
                    // TODO: Release inventory if not already done (double-check logic)
                }
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
        // TODO: Extend webhook to handle more Stripe event types as needed
    }
} 