USE chatbot;

SET @userId='dc615dbd-2a5e-47bd-b293-e31c01f91eda';
SET @refreshToken='eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsInN1YiI6ImRjNjE1ZGJkLTJhNWUtNDdiZC1iMjkzLWUzMWMwMWY5MWVkYSIsImlhdCI6MTc0NTQwMTg3NywiZXhwIjoxNzQ2MDA2Njc3LCJpc3MiOiJjaGF0Ym90Lm1hcmNvaW5kZXYuY29tIn0.uK2SJSsAcnlddYtrZOkIuKxnYSuXors62KMal4593bw';

-- Insert AI providers
INSERT INTO ai_provider (name, api_base_url, active, created_at, updated_at)
VALUES ('deepseek', 'https://api.deepseek.com', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('chatgpt', 'https://genai.hkbu.edu.hk/general/rest', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert user with dynamic UUID and timestamp
INSERT INTO `user`
VALUES (@userId,
        'workmarcohk@gmail.com',
        0,
        '1234',
        'superman',
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL);

-- Insert user profile (assuming userId matches the one just inserted)
INSERT INTO `user_profile`
VALUES (@userId,
        NULL,
        NULL,
        NULL,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL);


INSERT INTO `refresh_token`
VALUES (UUID(), @userId,
        @refreshToken,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);


-- Insert initial subscription plans (non-modifiable)
INSERT INTO subscription_plan (id, name, description, price, billing_cycle, features, active)
VALUES
    (UUID(), 'Basic', 'Basic plan with limited features', 9.99, 'monthly', '["Feature 1", "Feature 2"]', TRUE),
    (UUID(), 'Premium', 'Premium plan with all features', 19.99, 'monthly', '["All Features"]', TRUE),
    (UUID(), 'Annual Pro', 'Annual professional plan', 199.99, 'yearly', '["Pro Features"]', TRUE);