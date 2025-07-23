USE mcshop;

-- Users
INSERT INTO user (id, email, email_verified, password, username, status, created_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'alice@example.com', TRUE, 'hashedpassword1', 'alice', 'ACTIVE', NOW()),
  ('22222222-2222-2222-2222-222222222222', 'bob@example.com', FALSE, 'hashedpassword2', 'bob', 'ACTIVE', NOW()),
  ('33333333-3333-3333-3333-333333333333', 'charlie@example.com', TRUE, 'hashedpassword3', 'charlie', 'SUSPENDED', NOW());

-- User Profiles
INSERT INTO user_profile (user_id, first_name, last_name, phone, created_at, updated_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Alice', 'Wonderland', '+1234567890', NOW(), NOW()),
  ('22222222-2222-2222-2222-222222222222', 'Bob', 'Builder', '+0987654321', NOW(), NOW()),
  ('33333333-3333-3333-3333-333333333333', 'Charlie', 'Brown', '+1122334455', NOW(), NOW());

-- Product Categories
INSERT INTO product_category (id, name, description, image_url, created_at, updated_at)
VALUES
  ('c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1', 'Weapons', 'All kinds of weapons', 'https://example.com/images/weapons.png', NOW(), NOW()),
  ('c2c2c2c2-c2c2-c2c2-c2c2-c2c2c2c2c2c2', 'Consumables', 'Items you can consume', 'https://example.com/images/consumables.png', NOW(), NOW()),
  ('c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3', 'Books', 'Magical and enchanted books', 'https://example.com/images/books.png', NOW(), NOW());

-- Products
INSERT INTO product (id, name, price, description, image_url, category_id, created_at, updated_at)
VALUES
  ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Diamond Sword', 99.99, 'A powerful sword for your adventures.', 'https://example.com/images/diamond_sword.png', 'c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1', NOW(), NOW()),
  ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'Golden Apple', 19.99, 'A rare and magical apple.', 'https://example.com/images/golden_apple.png', 'c2c2c2c2-c2c2-c2c2-c2c2-c2c2c2c2c2c2', NOW(), NOW()),
  ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'Enchanted Book', 29.99, 'Contains mysterious enchantments.', 'https://example.com/images/enchanted_book.png', 'c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3', NOW(), NOW());

-- Product Inventory
INSERT INTO product_inventory (id, quantity, product_id, created_at, updated_at)
VALUES
  ('inv-aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 10, 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', NOW(), NOW()),
  ('inv-aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 50, 'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', NOW(), NOW()),
  ('inv-aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 5, 'aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', NOW(), NOW());

-- Product Discounts
INSERT INTO product_discount (id, name, description, type, value, active, expiry, created_at, updated_at)
VALUES
  ('d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', 'Summer Sale', '10% off all weapons', 'PERCENT', 10.00, TRUE, '2024-12-31 23:59:59', NOW(), NOW()),
  ('d2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2', 'Book Bonanza', '5 off all books', 'AMOUNT', 5.00, TRUE, '2024-11-30 23:59:59', NOW(), NOW());

-- Product Discount Links
INSERT INTO product_discount_link (id, product_id, discount_id, created_at, updated_at)
VALUES
  ('link-aaaaaaa1-d1d1', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'd1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', NOW(), NOW()),
  ('link-aaaaaaa3-d2d2', 'aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'd2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2', NOW(), NOW()); 