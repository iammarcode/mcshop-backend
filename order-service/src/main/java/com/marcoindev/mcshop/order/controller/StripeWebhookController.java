package com.marcoindev.mcshop.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marcoindev.mcshop.order.entity.OrderEntity;
import com.marcoindev.mcshop.order.entity.OrderItemEntity;
import com.marcoindev.mcshop.order.entity.OrderTransactionEntity;
import com.marcoindev.mcshop.order.feign.ProductFeignClient;
import com.marcoindev.mcshop.order.repository.OrderItemMapper;
import com.marcoindev.mcshop.order.repository.OrderMapper;
import com.marcoindev.mcshop.order.repository.OrderTransactionMapper;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order/stripe/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {
    private final OrderMapper orderMapper;
    private final ProductFeignClient productFeignClient;
    private final OrderItemMapper orderItemMapper;
    private final OrderTransactionMapper orderTransactionMapper;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public void handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        try {
            log.info("Received Stripe webhook event with signature: {}", sigHeader);
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            log.info("Processing event type: {}, API version: {}", event.getType(), event.getApiVersion());

            // Handle Checkout Session events
            if ("checkout.session.completed".equals(event.getType())) {
                processCheckoutSessionCompleted(event);
            } else if ("checkout.session.async_payment_failed".equals(event.getType())) {
                processAsyncPaymentFailed(event);
            } else if ("checkout.session.expired".equals(event.getType())) {
                processSessionExpired(event);
            } else {
                log.info("Ignoring unhandled event type: {}", event.getType());
            }
        } catch (Exception e) {
            log.error("Failed to process Stripe webhook: {}", e.getMessage(), e);
        }
    }

    private void processCheckoutSessionCompleted(Event event) {
        log.info("Processing checkout.session.completed event");
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        String orderId = null;
        String paymentIntentId = null;

        if (deserializer.getObject().isPresent()) {
            Session session = (Session) deserializer.getObject().get();
            orderId = session.getMetadata().get("orderId");
            paymentIntentId = session.getPaymentIntent();
            log.info("Checkout session completed - orderId: {}, paymentIntentId: {}", orderId, paymentIntentId);
            log.debug("Session metadata: {}", session.getMetadata());
        } else if (deserializer.getRawJson() != null) {
            log.warn("Using fallback JSON parsing for checkout.session.completed");
            JsonObject eventJson = JsonParser.parseString(deserializer.getRawJson()).getAsJsonObject();
            JsonObject metadata = eventJson.has("metadata") ? eventJson.getAsJsonObject("metadata") : null;
            orderId = metadata != null && metadata.has("orderId") ? metadata.get("orderId").getAsString() : null;
            paymentIntentId = eventJson.has("payment_intent") ? eventJson.get("payment_intent").getAsString() : null;
            log.info("Fallback parsing - orderId: {}, paymentIntentId: {}", orderId, paymentIntentId);
        } else {
            log.error("Failed to deserialize checkout.session.completed event - no object or raw JSON available");
            return;
        }

        if (orderId != null) {
            log.info("Updating order status for orderId: {}", orderId);
            updateOrderStatus(orderId, paymentIntentId, "PAID", true);
        } else {
            log.error("orderId is null in checkout.session.completed event metadata");
        }
    }

    private void processAsyncPaymentFailed(Event event) {
        log.info("Processing checkout.session.async_payment_failed event");
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        String orderId = null;
        String paymentIntentId = null;

        if (deserializer.getObject().isPresent()) {
            Session session = (Session) deserializer.getObject().get();
            orderId = session.getMetadata().get("orderId");
            paymentIntentId = session.getPaymentIntent();
            log.info("Async payment failed - orderId: {}, paymentIntentId: {}", orderId, paymentIntentId);
            log.debug("Session metadata: {}", session.getMetadata());
        } else if (deserializer.getRawJson() != null) {
            log.warn("Using fallback JSON parsing for checkout.session.async_payment_failed");
            JsonObject eventJson = JsonParser.parseString(deserializer.getRawJson()).getAsJsonObject();
            JsonObject metadata = eventJson.has("metadata") ? eventJson.getAsJsonObject("metadata") : null;
            orderId = metadata != null && metadata.has("orderId") ? metadata.get("orderId").getAsString() : null;
            paymentIntentId = eventJson.has("payment_intent") ? eventJson.get("payment_intent").getAsString() : null;
            log.info("Fallback parsing - orderId: {}, paymentIntentId: {}", orderId, paymentIntentId);
        } else {
            log.error("Failed to deserialize checkout.session.async_payment_failed event - no object or raw JSON available");
            return;
        }

        if (orderId != null) {
            log.info("Updating order status to PAYMENT_FAILED for orderId: {}", orderId);
            updateOrderStatus(orderId, paymentIntentId, "PAYMENT_FAILED", false);
        } else {
            log.error("orderId is null in checkout.session.async_payment_failed event metadata");
        }
    }

    private void processSessionExpired(Event event) {
        log.info("Processing checkout.session.expired event");
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        String orderId = null;

        if (deserializer.getObject().isPresent()) {
            Session session = (Session) deserializer.getObject().get();
            orderId = session.getMetadata().get("orderId");
            log.info("Checkout session expired - orderId: {}", orderId);
            log.debug("Session metadata: {}", session.getMetadata());
        } else if (deserializer.getRawJson() != null) {
            log.warn("Using fallback JSON parsing for checkout.session.expired");
            JsonObject eventJson = JsonParser.parseString(deserializer.getRawJson()).getAsJsonObject();
            JsonObject metadata = eventJson.has("metadata") ? eventJson.getAsJsonObject("metadata") : null;
            orderId = metadata != null && metadata.has("orderId") ? metadata.get("orderId").getAsString() : null;
            log.info("Fallback parsing - orderId: {}", orderId);
        } else {
            log.error("Failed to deserialize checkout.session.expired event - no object or raw JSON available");
            return;
        }

        if (orderId != null) {
            log.info("Updating order status to EXPIRED for orderId: {}", orderId);
            updateOrderStatus(orderId, null, "EXPIRED", false);
        } else {
            log.error("orderId is null in checkout.session.expired event metadata");
        }
    }

    private void updateOrderStatus(String orderId, String paymentIntentId, String status, boolean finalizeInventory) {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) {
            log.error("Order not found for orderId: {}", orderId);
            return;
        }

        if (!status.equals(order.getStatus())) {
            log.info("Updating order status to {} for orderId: {}", status, orderId);
            // Update order status
            order.setStatus(status);
            if (paymentIntentId != null) {
                order.setPaymentIntentId(paymentIntentId);
            }
            orderMapper.updateById(order);

            // Update transaction status
            log.debug("Updating transaction status to {} for orderId: {}", status, orderId);
            UpdateWrapper<OrderTransactionEntity> transactionWrapper = new UpdateWrapper<>();
            transactionWrapper.eq("order_id", orderId)
                    .isNull("deleted_at")
                    .set("status", status.equals("EXPIRED") ? "CANCELED" : status)
                    .set(paymentIntentId != null ? "account_no" : null, paymentIntentId);
            orderTransactionMapper.update(null, transactionWrapper);

            // Handle inventory
            QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId).isNull("deleted_at").select("product_id", "quantity");
            List<OrderItemEntity> orderItems = orderItemMapper.selectList(queryWrapper);

            if (finalizeInventory) {
                log.debug("Finalizing inventory for orderId: {}", orderId);
                for (OrderItemEntity orderItem : orderItems) {
                    log.debug("Finalizing inventory for productId: {}, quantity: {}", orderItem.getProductId(), orderItem.getQuantity());
                    productFeignClient.finalizeInventory(orderItem.getProductId(), orderItem.getQuantity());
                }
            } else {
                log.debug("Releasing inventory for orderId: {}", orderId);
                for (OrderItemEntity orderItem : orderItems) {
                    log.debug("Releasing inventory for productId: {}, quantity: {}", orderItem.getProductId(), orderItem.getQuantity());
                    productFeignClient.releaseInventory(orderItem.getProductId(), orderItem.getQuantity());
                }
            }
            // TODO: send notification
            log.info("Successfully processed orderId: {} with status: {}", orderId, status);
        } else {
            log.info("OrderId: {} already in status: {}, skipping update", orderId, status);
        }
    }
}