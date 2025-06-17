# API Endpoints

### 1.Auth Service
```
GET /auth/otp
POST /auth/register
POST /auth/login
POST /auth/refresh
```

### 2.User Service
```
GET /users/me - Get current user profile
PUT /users/me - Update profile
DELETE /users/me - Delete account
```

### 3.Chat Service
AI provider: deepseek, chatgpt
```
POST /chat/session - Start new chat session
GET /chat/sessions - Get all chat sessions
POST /chat/session/{id}/messages - Send message
GET /chat/session/{id}/messages - Get messages
DELETE /chat/session/{id} - End session
```

### 4.Subscription Service
```
GET /subscriptions/plans - List available plans
GET /subscriptions/me - Get current subscription
POST /subscriptions/me - Create subscription
DELETE /subscriptions/me - Cancel subscription
```

### 5.Payment Service
Payment Method: Stripe
```
POST /payments - Create payment
GET /payments/{id} - Get payment status
GET /payments/history - Payment history
```