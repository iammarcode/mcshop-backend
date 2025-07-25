USE mcshop;

-- Users
INSERT INTO user (id, email, email_verified, password, username, status, created_at)
VALUES
  ('userid1', 'alice@example.com', TRUE, 'hashedpassword1', 'alice', 'ACTIVE', NOW()),
  ('userid2', 'bob@example.com', FALSE, 'hashedpassword2', 'bob', 'ACTIVE', NOW()),
  ('userid3', 'charlie@example.com', TRUE, 'hashedpassword3', 'charlie', 'SUSPENDED', NOW());

-- User Profiles
INSERT INTO user_profile (user_id, first_name, last_name, phone, created_at, updated_at)
VALUES
  ('userid1', 'Alice', 'Wonderland', '+1234567890', NOW(), NOW()),
  ('userid2', 'Bob', 'Builder', '+0987654321', NOW(), NOW()),
  ('userid3', 'Charlie', 'Brown', '+1122334455', NOW(), NOW());

-- User Addresses
INSERT INTO user_address (id, user_id, address_line1, address_line2, postal_code, city, country, phone, is_default, created_at, updated_at)
VALUES
  ('addressid1', 'userid1', '123 Wonderland Street', 'Apt 4B', '12345', 'Fantasy City', 'United States', '+1234567890', TRUE, NOW(), NOW()),
  ('addressid2', 'userid1', '456 Tea Party Lane', 'Garden House', '12346', 'Fantasy City', 'United States', '+1234567890', FALSE, NOW(), NOW()),
  ('addressid3', 'userid2', '789 Construction Avenue', 'Building 7', '67890', 'Builder Town', 'United States', '+0987654321', TRUE, NOW(), NOW()),
  ('addressid4', 'userid2', '321 Tool Street', 'Workshop 3', '67891', 'Builder Town', 'United States', '+0987654321', FALSE, NOW(), NOW()),
  ('addressid5', 'userid3', '654 Peanuts Lane', 'House 10', '11223', 'Comic Strip City', 'United States', '+1122334455', TRUE, NOW(), NOW());

-- Product Categories
INSERT INTO product_category (id, name, description, image_url, created_at, updated_at)
VALUES
  ('categoryid1', 'Weapons', 'All kinds of weapons', 'https://example.com/images/weapons.png', NOW(), NOW()),
  ('categoryid2', 'Consumables', 'Items you can consume', 'https://example.com/images/consumables.png', NOW(), NOW()),
  ('categoryid3', 'Books', 'Magical and enchanted books', 'https://example.com/images/books.png', NOW(), NOW());

-- Products
INSERT INTO product (id, name, price, description, image_url, category_id, created_at, updated_at)
VALUES
  ('productid1', 'Diamond Sword', 99.99, 'A powerful sword for your adventures.', 'https://example.com/images/diamond_sword.png', 'categoryid1', NOW(), NOW()),
  ('productid2', 'Golden Apple', 19.99, 'A rare and magical apple.', 'https://example.com/images/golden_apple.png', 'categoryid2', NOW(), NOW()),
  ('productid3', 'Enchanted Book', 29.99, 'Contains mysterious enchantments.', 'https://example.com/images/enchanted_book.png', 'categoryid3', NOW(), NOW());

-- Product Inventory
INSERT INTO product_inventory (id, quantity, product_id, created_at, updated_at)
VALUES
  ('inventoryid1', 10, 'productid1', NOW(), NOW()),
  ('inventoryid2', 50, 'productid2', NOW(), NOW()),
  ('inventoryid3', 5, 'productid3', NOW(), NOW());

-- Product Discounts
INSERT INTO product_discount (id, name, description, type, value, active, expiry, created_at, updated_at)
VALUES
  ('discountid1', 'Summer Sale', '10% off all weapons', 'PERCENT', 10.00, TRUE, '2024-12-31 23:59:59', NOW(), NOW()),
  ('discountid2', 'Book Bonanza', '5 off all books', 'AMOUNT', 5.00, TRUE, '2024-11-30 23:59:59', NOW(), NOW());

-- Product Discount Links
INSERT INTO product_discount_link (id, product_id, discount_id, created_at, updated_at)
VALUES
  ('linkid1', 'productid1', 'discountid1', NOW(), NOW()),
  ('linkid2', 'productid3', 'discountid2', NOW(), NOW()); 