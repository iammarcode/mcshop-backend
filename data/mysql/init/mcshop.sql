CREATE DATABASE IF NOT EXISTS mcshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mcshop;

-- 1. Authentication Service
CREATE TABLE IF NOT EXISTS `user` (
  `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'UUID for security',
  `email` VARCHAR(255) NOT NULL COMMENT 'User email address (unique)',
  `email_verified` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Whether email has been verified',
  `password` VARCHAR(255) NOT NULL COMMENT 'Hashed password',
  `username` VARCHAR(100) NOT NULL COMMENT 'Unique display name',
  `status` ENUM('ACTIVE', 'SUSPENDED', 'DELETED') NOT NULL DEFAULT 'ACTIVE' COMMENT 'Account status',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time',
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update time',
  `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp',
  -- Indexes
  UNIQUE INDEX `idx_email` (`email`),
  UNIQUE INDEX `idx_username` (`username`),
  INDEX `idx_status` (`status`)
) ENGINE = InnoDB COMMENT = 'User authentication information';

CREATE TABLE IF NOT EXISTS refresh_token (
  `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'UUID for security',
  `user_id` CHAR(36) NOT NULL COMMENT 'Reference to users.id',
  `token` VARCHAR(255) NOT NULL COMMENT 'Refresh token value',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Token creation time',
  `expire_at` DATETIME NOT NULL COMMENT 'Token expiration time',
  `deleted_at` DATETIME NULL COMMENT '',
  -- Indexes
  UNIQUE INDEX `idx_token` (`token`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_expire_at` (`expire_at`)
) ENGINE = InnoDB COMMENT = 'Refresh tokens for maintaining user session';

-- 2. User Service
CREATE TABLE IF NOT EXISTS user_profile (
  `user_id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Reference to users.id',
  `first_name` VARCHAR(100) NULL COMMENT 'User first name',
  `last_name` VARCHAR(100) NULL COMMENT 'User last name',
  `phone` VARCHAR(20) NULL COMMENT 'Phone number with country code',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
  `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
) ENGINE = InnoDB COMMENT = 'Extended user profile information';

CREATE TABLE IF NOT EXISTS user_payment (
  `user_id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Reference to users.id',
  `type` VARCHAR(64) NOT NULL COMMENT 'Payment type',
  `provider` VARCHAR(64) NOT NULL COMMENT 'Payment provider',
  `account_no` VARCHAR(64) NOT NULL COMMENT 'Payment account number',
  `expiry` DATETIME NULL COMMENT 'Expiry of the payment',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
  `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
) ENGINE=InnoDB COMMENT='Payment information';

CREATE TABLE IF NOT EXISTS user_address (
    `user_id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Reference to users.id',
    `address_line1` TEXT NOT NULL COMMENT 'Address line1',
    `address_line2` TEXT NULL COMMENT 'Address line2',
    `postal_code` VARCHAR(100) NOT NULL COMMENT 'Postal Code',
    `city` VARCHAR(100) NOT NULL COMMENT 'City',
    `country` VARCHAR(100) NOT NULL COMMENT 'Country',
    `phone` VARCHAR(64) NOT NULL COMMENT 'Phone',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Address information';

-- 3. Product Service
CREATE TABLE IF NOT EXISTS product (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'Product name',
    `price` DECIMAL(10,2) NOT NULL  COMMENT 'Product price',
    `description` TEXT NULL COMMENT 'Product desc',
    `image_url` VARCHAR(255) NULL COMMENT 'Product image url',
    `category_id` CHAR(36) NULL COMMENT 'Product category id',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Product';

CREATE TABLE IF NOT EXISTS product_category (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'product category name',
    `description` TEXT NULL COMMENT 'product desc',
    `image_url` VARCHAR(255) NULL COMMENT 'product category image url',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Product Category';

CREATE TABLE IF NOT EXISTS product_inventory (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `quantity` INT NOT NULL COMMENT 'Quantity of product',
    `product_id` CHAR(36) NOT NULL COMMENT 'Product ID',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Profile creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Product inventory';

CREATE TABLE IF NOT EXISTS product_discount (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `name` VARCHAR(255) NOT NULL COMMENT 'name',
    `description` TEXT NULL COMMENT 'desc',
    `type` VARCHAR(100) NOT NULL COMMENT 'type',
    `value` DECIMAL(10,2) NOT NULL COMMENT 'value',
    `active` BOOLEAN NOT NULL DEFAULT true COMMENT 'active',
    `expiry` DATETIME NULL COMMENT 'expiry',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Product discount';

CREATE TABLE IF NOT EXISTS product_discount_link (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `product_id` CHAR(36) NOT NULL COMMENT 'product id',
    `discount_id` CHAR(36) NOT NULL COMMENT 'discount id',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='Product discount link';

-- 4. Shopping cart Service
CREATE TABLE IF NOT EXISTS shopping_cart (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `user_id` CHAR(36) NOT NULL COMMENT 'user id',

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='user shopping cart';

CREATE TABLE IF NOT EXISTS shopping_cart_item (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `quantity` INT NOT NULL,
    `product_id` CHAR(36) NOT NULL,
    `shopping_cart_id` CHAR(36) NOT NULL,

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='shopping cart item';

-- 5. Order Service
CREATE TABLE IF NOT EXISTS `order` (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'ID',
    `status` VARCHAR(100) NOT NULL DEFAULT 'ORDERED',
    `total` DECIMAL(10,2) NOT NULL,
    `user_id` CHAR(36) NOT NULL,
    `user_address_id` CHAR(36) NOT NULL,

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='order';

CREATE TABLE IF NOT EXISTS `mcshop`.`order_item` (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'order item ID',
    `quantity` INT NOT NULL,
    `order_id` CHAR(36) NOT NULL,
    `product_id` CHAR(36) NOT NULL,

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    ) ENGINE=InnoDB COMMENT='order item';

CREATE TABLE IF NOT EXISTS `mcshop`.`order_transaction` (
    `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'order transaction ID',
    `amount` DECIMAL(10,2) NOT NULL,
    `provider` VARCHAR(64) NOT NULL,
    `account_no` VARCHAR(64) NOT NULL,
    `status` VARCHAR(64) NOT NULL DEFAULT 'PENDING',
    `order_id` CHAR(36) NOT NULL,

    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last profile update time',
    `deleted_at` DATETIME NULL COMMENT 'Soft delete timestamp'
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='order transaction';

