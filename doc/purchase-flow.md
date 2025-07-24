# Purchase & Payment Flow (Best Practice)

This document describes the robust, industry-standard purchase and payment flow for MCShop, based on microservice architecture and the amended schema.

## Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant OrderService
    participant ProductService
    participant Stripe
    participant RabbitMQ

    Client->>OrderService: POST /api/v1/order/purchase (cart, address, payment info)
    OrderService->>ProductService: Check & Reserve Inventory (lock, reserve)
    ProductService-->>OrderService: Inventory reserved
    OrderService->>OrderService: Create Order (status: PENDING_PAYMENT)
    OrderService->>Stripe: Create PaymentIntent (orderId as idempotency key)
    Stripe-->>OrderService: PaymentIntent (client_secret)
    OrderService-->>Client: Return orderId, paymentIntent client_secret

    %% Client completes payment on frontend with client_secret

    Stripe-->>OrderService: Webhook: payment_intent.succeeded (orderId)
    OrderService->>OrderService: Update Order (status: PAID)
    OrderService->>ProductService: Finalize Inventory (confirm decrement)
    OrderService->>RabbitMQ: Publish OrderPaid event

    Stripe-->>OrderService: Webhook: payment_intent.failed (orderId)
    OrderService->>OrderService: Update Order (status: PAYMENT_FAILED)
    OrderService->>ProductService: Release Inventory (rollback reservation)
```

## Key Points
- **Order is created before payment** for traceability and idempotency.
- **Inventory is reserved before payment** to prevent overselling.
- **Payment is confirmed asynchronously** via Stripe webhook.
- **Order status is updated** based on payment result.
- **All critical state changes are event-driven** (RabbitMQ).
- **Idempotency keys** are used for payment and order creation.
- **Schema supports reservation, status, and idempotency.** 