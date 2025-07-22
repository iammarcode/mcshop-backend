USE mcshop;

-- Insert fake users
INSERT INTO user (id, email, email_verified, password, username, status, created_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'alice@example.com', TRUE, 'hashedpassword1', 'alice', 'ACTIVE', NOW()),
  ('22222222-2222-2222-2222-222222222222', 'bob@example.com', FALSE, 'hashedpassword2', 'bob', 'ACTIVE', NOW());

-- Insert fake user profiles
INSERT INTO user_profile (user_id, first_name, last_name, phone, created_at, updated_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Alice', 'Wonderland', '+1234567890', NOW(), NOW()),
  ('22222222-2222-2222-2222-222222222222', 'Bob', 'Builder', '+0987654321', NOW(), NOW());

-- Insert fake products
INSERT INTO product (id, name, price, description, image_url, category_id, created_at, updated_at)
VALUES
  ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Diamond Sword', 99.99, 'A powerful sword for your adventures.', 'https://example.com/images/diamond_sword.png', NULL, NOW(), NOW()),
  ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'Golden Apple', 19.99, 'A rare and magical apple.', 'https://example.com/images/golden_apple.png', NULL, NOW(), NOW()),
  ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'Enchanted Book', 29.99, 'Contains mysterious enchantments.', 'https://example.com/images/enchanted_book.png', NULL, NOW(), NOW()); 