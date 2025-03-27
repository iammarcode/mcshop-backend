CREATE DATABASE IF NOT EXISTS mcshop_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mcshop_auth;

CREATE TABLE IF NOT EXISTS customer (
  `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  `email` VARCHAR(255) NOT NULL,
  `email_verified` BOOLEAN NOT NULL DEFAULT FALSE,
  `password` VARCHAR(255) NOT NULL COMMENT 'Should store Argon2id or BCrypt hash',

  -- Personal information
  `first_name` VARCHAR(100) NULL,
  `last_name` VARCHAR(100) NULL,
  `display_name` VARCHAR(100) NULL,
  
  -- Account status
  `status` ENUM('ACTIVE', 'SUSPENDED', 'DELETED') NOT NULL DEFAULT 'ACTIVE',

  -- Timestamps
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` DATETIME NULL,
  
  -- Constraints
  CONSTRAINT `uq_email` UNIQUE (`email`)
) ENGINE=InnoDB;