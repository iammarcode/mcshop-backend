package com.marcoindev.mcshop.order.service.impl;

import com.marcoindev.mcshop.order.entity.OrderEntity;
import com.marcoindev.mcshop.order.entity.OrderItemEntity;
import com.marcoindev.mcshop.order.entity.OrderTransactionEntity;
import com.marcoindev.mcshop.order.feign.ProductFeignClient;
import com.marcoindev.mcshop.order.feign.ProductFeignClient.ProductDTO;
import com.marcoindev.mcshop.order.feign.ProductInventoryClient;
import com.marcoindev.mcshop.order.payload.request.PlaceOrderRequest;
import com.marcoindev.mcshop.order.payload.response.PlaceOrderResponse;
import com.marcoindev.mcshop.order.repository.OrderItemMapper;
import com.marcoindev.mcshop.order.repository.OrderMapper;
import com.marcoindev.mcshop.order.repository.OrderTransactionMapper;
import com.marcoindev.mcshop.order.service.OrderService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderTransactionMapper orderTransactionMapper;
    private final ProductInventoryClient inventoryClient;
    private final RedissonClient redissonClient = Redisson.create();
    private final RabbitTemplate rabbitTemplate;
    private final ProductFeignClient productFeignClient;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private static final String PRODUCT_LOCK_PREFIX = "lock:product:";

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        List<String> reservedProductIds = new ArrayList<>();
        Map<String, Integer> reservedQuantities = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        Map<String, ProductDTO> productDTOs = new HashMap<>();
        String lockKey = PRODUCT_LOCK_PREFIX + request.getProducts().stream().map(PlaceOrderRequest.ProductOrder::getProductId).sorted().reduce("", String::concat);
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                return PlaceOrderResponse.builder().success(false).message("System busy, try again.").build();
            }
            // 1. Reserve inventory and fetch price for all products
            List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
            for (PlaceOrderRequest.ProductOrder po : request.getProducts()) {
                boolean reserved = inventoryClient.reserveInventory(po.getProductId(), po.getQuantity());
                if (!reserved) {
                    for (String pid : reservedProductIds) {
                        inventoryClient.releaseInventory(pid, reservedQuantities.get(pid));
                    }
                    return PlaceOrderResponse.builder().success(false).message("Insufficient inventory for product: " + po.getProductId()).build();
                }
                reservedProductIds.add(po.getProductId());
                reservedQuantities.put(po.getProductId(), po.getQuantity());
                var product = productFeignClient.getProductById(po.getProductId());
                if (product == null || product.getPrice() == null) {
                    for (String pid : reservedProductIds) {
                        inventoryClient.releaseInventory(pid, reservedQuantities.get(pid));
                    }
                    return PlaceOrderResponse.builder().success(false).message("Product not found or price missing: " + po.getProductId()).build();
                }
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(po.getQuantity())));
                productDTOs.put(po.getProductId(), product);
                // Build Stripe Checkout line item
                lineItems.add(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(po.getQuantity()))
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(request.getCurrency())
                                .setUnitAmount(product.getPrice().movePointRight(2).longValue())
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Product: " + po.getProductId()) // TODO: fetch real name if needed
                                        .build()
                                )
                                .build()
                        )
                        .build()
                );
            }
            // 2. Create order (status: PENDING_PAYMENT)
            String orderId = UUID.randomUUID().toString();
            OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .status("PENDING_PAYMENT")
                .total(total)
                .userId(request.getUserId())
                .userAddressId(request.getUserAddressId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            orderMapper.insert(order);
            // 3. Create Stripe Checkout Session
            Stripe.apiKey = stripeApiKey;
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://yourdomain.com/payment-success?orderId=" + orderId)
                .setCancelUrl("https://yourdomain.com/payment-cancel?orderId=" + orderId)
                .addAllLineItem(lineItems)
                .putMetadata("orderId", orderId)
                .build();
            Session session = Session.create(params);
            // 4. Create order transaction (status: PENDING)
            OrderTransactionEntity transaction = OrderTransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(total)
                .provider("stripe")
                .accountNo(null)
                .status("PENDING")
                .orderId(orderId)
                .idempotencyKey(session.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            orderTransactionMapper.insert(transaction);
            // 5. Create order items for each product
            for (PlaceOrderRequest.ProductOrder po : request.getProducts()) {
                OrderItemEntity orderItem = OrderItemEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .quantity(po.getQuantity())
                    .orderId(orderId)
                    .productId(po.getProductId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                orderItemMapper.insert(orderItem);
            }
            // 6. Return Stripe Checkout session URL to client
            return PlaceOrderResponse.builder()
                .success(true)
                .orderId(orderId)
                .message("Redirect to Stripe Checkout")
                .paymentUrl(session.getUrl())
                .build();
        } catch (Exception e) {
            for (String pid : reservedProductIds) {
                inventoryClient.releaseInventory(pid, reservedQuantities.get(pid));
            }
            return PlaceOrderResponse.builder().success(false).message("Purchase failed: " + e.getMessage()).build();
        } finally {
            if (locked) lock.unlock();
        }
    }
}