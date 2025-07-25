package com.marcoindev.mcshop.order.service.impl;

import com.marcoindev.mcshop.order.entity.OrderEntity;
import com.marcoindev.mcshop.order.entity.OrderItemEntity;
import com.marcoindev.mcshop.order.entity.OrderTransactionEntity;
import com.marcoindev.mcshop.order.exception.APIRuntimeException;
import com.marcoindev.mcshop.order.feign.ProductFeignClient;
import com.marcoindev.mcshop.order.feign.UserAddressFeignClient;
import com.marcoindev.mcshop.order.payload.request.PlaceOrderRequest;
import com.marcoindev.mcshop.order.payload.request.PlaceOrderRequest.ProductOrder;
import com.marcoindev.mcshop.order.payload.response.PlaceOrderResponse;
import com.marcoindev.mcshop.order.repository.OrderItemMapper;
import com.marcoindev.mcshop.order.repository.OrderMapper;
import com.marcoindev.mcshop.order.repository.OrderTransactionMapper;
import com.marcoindev.mcshop.order.service.OrderService;
import com.marcoindev.mcshop.common.payload.ErrorCode;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderTransactionMapper orderTransactionMapper;
    private final RedissonClient redissonClient = Redisson.create();
    private final ProductFeignClient productFeignClient;
    private final UserAddressFeignClient userAddressFeignClient;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private static final String PRODUCT_LOCK_PREFIX = "lock:product:";

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request, String userId) {
        List<String> reservedProductIds = new ArrayList<>();
        Map<String, Integer> reservedQuantities = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        
        // Create lock key from product IDs
        String lockKey = PRODUCT_LOCK_PREFIX + request.getProducts().stream()
                .map(PlaceOrderRequest.ProductOrder::getProductId)
                .sorted()
                .reduce("", String::concat);
        
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new APIRuntimeException(ErrorCode.PURCHASE_SYSTEM_BUSY);
            }
            
            // 0. Get and validate user address (use default if not provided)
            String userAddressId = request.getUserAddressId();
            try {
                UserAddressFeignClient.UserAddressResponse addressResponse;
                
                if (userAddressId == null || userAddressId.trim().isEmpty()) {
                    // Use default address
                    addressResponse = userAddressFeignClient.getDefaultAddress(userId);
                    userAddressId = addressResponse.data.getId();
                } else {
                    // Use provided address
                    addressResponse = userAddressFeignClient.getAddressById(userAddressId, userId);
                }
                
                if (addressResponse == null || addressResponse.data == null) {
                    throw new APIRuntimeException(ErrorCode.PURCHASE_PRODUCT_NOT_FOUND, 
                        "Address not found or does not belong to user: " + userAddressId);
                }
                // Additional validation: ensure address belongs to the requesting user
                if (!addressResponse.data.getUserId().equals(userId)) {
                    throw new APIRuntimeException(ErrorCode.PURCHASE_PRODUCT_NOT_FOUND, 
                        "Address does not belong to user: " + userAddressId);
                }
            } catch (Exception e) {
                throw new APIRuntimeException(ErrorCode.PURCHASE_PRODUCT_NOT_FOUND, 
                    "Failed to validate address: " + e.getMessage());
            }
            
            // 1. Determine order currency first
            String orderCurrency = "usd"; // Default
            if (request.getCurrency() != null && !request.getCurrency().trim().isEmpty()) {
                orderCurrency = request.getCurrency().toLowerCase();
            }
            
            // 2. Reserve inventory and fetch price for all products
            List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
            for (ProductOrder po : request.getProducts()) {
                boolean reserved = productFeignClient.reserveInventory(po.getProductId(), po.getQuantity());
                if (!reserved) {
                    for (String pid : reservedProductIds) {
                        productFeignClient.releaseInventory(pid, reservedQuantities.get(pid));
                    }
                    throw new APIRuntimeException(ErrorCode.PURCHASE_INSUFFICIENT_INVENTORY, 
                        "Insufficient inventory for product: " + po.getProductId());
                }
                reservedProductIds.add(po.getProductId());
                reservedQuantities.put(po.getProductId(), po.getQuantity());
                var product = productFeignClient.getProductById(po.getProductId()).data;
                if (product == null || product.getPrice() == null) {
                    for (String pid : reservedProductIds) {
                        productFeignClient.releaseInventory(pid, reservedQuantities.get(pid));
                    }
                    throw new APIRuntimeException(ErrorCode.PURCHASE_PRODUCT_NOT_FOUND, 
                        "Product not found or price missing: " + po.getProductId());
                }
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(po.getQuantity())));
                // Build Stripe Checkout line item
                SessionCreateParams.LineItem.PriceData.Builder priceDataBuilder = 
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setUnitAmount(product.getPrice().movePointRight(2).longValue())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Product: " + product.getName())
                                .build()
                        );
                
                // Use order currency for Stripe Checkout
                priceDataBuilder.setCurrency(orderCurrency);
                
                lineItems.add(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(po.getQuantity()))
                        .setPriceData(priceDataBuilder.build())
                        .build()
                );
            }
            // 2. Create order (status: PENDING_PAYMENT)
            String orderId = UUID.randomUUID().toString();
            
            OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .status("PENDING_PAYMENT")
                .total(total)
                .currency(orderCurrency)
                .userId(userId)
                .userAddressId(userAddressId)
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
            // 4. Create order transaction (status: PENDING) - currency will be set during webhook
            OrderTransactionEntity transaction = OrderTransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(total)
                .provider("stripe")
                .status("PENDING")
                .orderId(orderId)
                .idempotencyKey(session.getId())
                .build();
            orderTransactionMapper.insert(transaction);
            // 5. Create order items for each product
            for (ProductOrder po : request.getProducts()) {
                OrderItemEntity orderItem = OrderItemEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .quantity(po.getQuantity())
                    .orderId(orderId)
                    .productId(po.getProductId())
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
                productFeignClient.releaseInventory(pid, reservedQuantities.get(pid));
            }
            throw new APIRuntimeException(ErrorCode.PURCHASE_PAYMENT_FAILED, 
                "Purchase failed: " + e.getMessage(), e);
        } finally {
            if (locked) lock.unlock();
        }
    }
}