-- Schema
CREATE DATABASE IF NOT EXISTS mcshop_product;

USE mcshop_product;

-- Tables
CREATE TABLE IF NOT EXISTS product (
  `id` CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  `name` varchar(100) NOT NULL,
  `price` decimal NOT NULL,
  `description` text NULL DEFAULT NULL,

  -- Timestamps
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime  NULL DEFAULT NULL,
  `deleted_at` datetime  NULL DEFAULT NULL
) ENGINE=InnoDB;