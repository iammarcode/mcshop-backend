package com.marcoindev.mcshop.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcoindev.mcshop.order.entity.OrderEntity;
import com.marcoindev.mcshop.order.entity.OrderItemEntity;
import com.marcoindev.mcshop.order.entity.OrderTransactionEntity;
import com.marcoindev.mcshop.order.feign.ProductFeignClient;
import com.marcoindev.mcshop.order.repository.OrderItemMapper;
import com.marcoindev.mcshop.order.repository.OrderMapper;
import com.marcoindev.mcshop.order.repository.OrderTransactionMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    public String handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            // Handle PaymentIntent events
            if ("payment_intent.succeeded".equals(event.getType()) || "payment_intent.failed".equals(event.getType())) {
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                PaymentIntent intent = null;
                if (deserializer.getObject().isPresent()) {
                    intent = (PaymentIntent) deserializer.getObject().get();
                } else if (deserializer.getRawJson() != null) {
                    JsonObject eventJson = JsonParser.parseString(deserializer.getRawJson()).getAsJsonObject();
                    String id = eventJson.get("id").getAsString();
                    JsonObject metadata = eventJson.has("metadata") ? eventJson.getAsJsonObject("metadata") : null;
                    String orderId = metadata != null && metadata.has("orderId") ? metadata.get("orderId").getAsString() : null;
                    if (orderId != null) {
                        updateOrderStatus(event.getType(), orderId, id);
                    }
                    return "success";
                } else {
                    return "error";
                }
                String orderId = intent.getMetadata().get("orderId");
                updateOrderStatus(event.getType(), orderId, intent.getId());
                return "success";
            }

            // Handle Checkout Session events (for Stripe Checkout)
            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                if (deserializer.getObject().isPresent()) {
                    com.stripe.model.checkout.Session session = (com.stripe.model.checkout.Session) deserializer.getObject().get();
                    String orderId = session.getMetadata().get("orderId");
                    updateOrderStatus("payment_intent.succeeded", orderId, session.getPaymentIntent());
                    return "success";
                }
            }

            return "ignored";
        } catch (Exception e) {
            return "error";
        }
    }

    private void updateOrderStatus(String eventType, String orderId, String paymentIntentId) {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) return;
        
        if ("payment_intent.succeeded".equals(eventType)) {
            if (!"PAID".equals(order.getStatus())) {
                // Update order status
                order.setStatus("PAID");
                order.setPaymentIntentId(paymentIntentId);
                orderMapper.updateById(order);
                
                // Update transaction status
                UpdateWrapper<OrderTransactionEntity> transactionWrapper = new UpdateWrapper<>();
                transactionWrapper.eq("order_id", orderId)
                        .isNull("deleted_at")
                        .set("status", "PAID")
                        .set("account_no", paymentIntentId);
                orderTransactionMapper.update(null, transactionWrapper);
                
                // Finalize inventory
                QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_id", orderId).isNull("deleted_at").select("product_id", "quantity");
                List<OrderItemEntity> orderItems = orderItemMapper.selectList(queryWrapper);
                for (OrderItemEntity orderItem : orderItems) {
                    productFeignClient.finalizeInventory(orderItem.getProductId(), orderItem.getQuantity());
                }
                // TODO: send notification
            }
        } else if ("payment_intent.failed".equals(eventType)) {
            if (!"PAYMENT_FAILED".equals(order.getStatus())) {
                // Update order status
                order.setStatus("PAYMENT_FAILED");
                order.setPaymentIntentId(paymentIntentId);
                orderMapper.updateById(order);
                
                // Update transaction status
                UpdateWrapper<OrderTransactionEntity> transactionWrapper = new UpdateWrapper<>();
                transactionWrapper.eq("order_id", orderId)
                        .isNull("deleted_at")
                        .set("status", "FAILED")
                        .set("account_no", paymentIntentId);
                orderTransactionMapper.update(null, transactionWrapper);
                
                // TODO: release inventory
            }
        }
    }
} 