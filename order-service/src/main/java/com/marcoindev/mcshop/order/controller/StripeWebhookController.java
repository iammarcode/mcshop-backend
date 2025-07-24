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
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final OrderMapper orderMapper;
    private final ProductFeignClient productFeignClient;
    private final OrderItemMapper orderItemMapper;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public String handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            JsonObject eventJson = JsonParser.parseString(rawJson).getAsJsonObject();

            if ("payment_intent.succeeded".equals(event.getType())) {
                String orderId = eventJson.getAsJsonObject("metadata").get("orderId").getAsString();
                OrderEntity order = orderMapper.selectById(orderId);
                if (order != null && !"PAID".equals(order.getStatus())) {
                    //1.update order
                    order.setStatus("PAID");
                    order.setPaymentIntentId(eventJson.get("id").getAsString());
                    orderMapper.updateById(order);

                    //2.update transaction acc, status
                    UpdateWrapper<OrderTransactionEntity> transactionWrapper = new UpdateWrapper<>();
                    transactionWrapper.eq("order_id", orderId)
                            .isNull("deleted_at")
                            .set("status", "PAID");

                    //3.finalize inventory
                    QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("order_id", orderId)
                            .isNull("deleted_at")
                            .select("product_id", "quantity");
                    List<OrderItemEntity> orderItems = orderItemMapper.selectList(queryWrapper);
                    for (OrderItemEntity orderItem : orderItems) {
                        productFeignClient.finalizeInventory(orderItem.getProductId(), orderItem.getQuantity());
                    }

                    //TODO: 4.send notification
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