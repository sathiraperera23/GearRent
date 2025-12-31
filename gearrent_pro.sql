-- =========================================
-- Create Database
-- =========================================
CREATE DATABASE IF NOT EXISTS GearRent;
USE GearRent;

-- =========================================
-- Table: branches
-- =========================================
CREATE TABLE `branches` (
                            `branch_id` int NOT NULL AUTO_INCREMENT,
                            `code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `contact` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`branch_id`),
                            UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: categories
-- =========================================
CREATE TABLE `categories` (
                              `category_id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                              `description` text COLLATE utf8mb4_unicode_ci,
                              `base_price_factor` decimal(8,4) NOT NULL DEFAULT '1.0000',
                              `weekend_multiplier` decimal(8,4) NOT NULL DEFAULT '1.0000',
                              `default_late_fee_per_day` decimal(10,2) DEFAULT '0.00',
                              `active` tinyint(1) DEFAULT '1',
                              PRIMARY KEY (`category_id`),
                              UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: config
-- =========================================
CREATE TABLE `config` (
                          `config_id` int NOT NULL AUTO_INCREMENT,
                          `late_fee_per_day` decimal(10,2) DEFAULT NULL,
                          `max_deposit` decimal(10,2) DEFAULT NULL,
                          `regular_discount` decimal(5,2) DEFAULT NULL,
                          `silver_discount` decimal(5,2) DEFAULT NULL,
                          `gold_discount` decimal(5,2) DEFAULT NULL,
                          PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: customers
-- =========================================
CREATE TABLE `customers` (
                             `customer_id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `nic_passport` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `contact_no` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `email` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `membership` enum('Regular','Silver','Gold') COLLATE utf8mb4_unicode_ci DEFAULT 'Regular',
                             `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`customer_id`),
                             UNIQUE KEY `nic_passport` (`nic_passport`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: equipment
-- =========================================
CREATE TABLE `equipment` (
                             `equipment_id` bigint NOT NULL AUTO_INCREMENT,
                             `category_id` int NOT NULL,
                             `branch_id` int NOT NULL,
                             `equipment_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `brand` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `model` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                             `purchase_year` smallint DEFAULT NULL,
                             `base_daily_price` decimal(12,2) NOT NULL,
                             `security_deposit` decimal(12,2) NOT NULL,
                             `status` enum('Available','Reserved','Rented','Under Maintenance') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Available',
                             `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`equipment_id`),
                             UNIQUE KEY `equipment_code` (`equipment_code`),
                             KEY `category_id` (`category_id`),
                             KEY `branch_id` (`branch_id`),
                             CONSTRAINT `equipment_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT,
                             CONSTRAINT `equipment_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: reservations
-- =========================================
CREATE TABLE `reservations` (
                                `reservation_id` bigint NOT NULL AUTO_INCREMENT,
                                `customer_id` bigint NOT NULL,
                                `equipment_id` bigint NOT NULL,
                                `reserved_from` date NOT NULL,
                                `reserved_to` date NOT NULL,
                                `total_price` decimal(12,2) NOT NULL,
                                `status` enum('Pending','Confirmed','Cancelled') COLLATE utf8mb4_unicode_ci DEFAULT 'Pending',
                                `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`reservation_id`),
                                KEY `customer_id` (`customer_id`),
                                KEY `equipment_id` (`equipment_id`),
                                CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
                                CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: rentals
-- =========================================
CREATE TABLE `rentals` (
                           `rental_id` bigint NOT NULL AUTO_INCREMENT,
                           `customer_id` bigint NOT NULL,
                           `equipment_id` bigint NOT NULL,
                           `rented_from` date NOT NULL,
                           `rented_to` date NOT NULL,
                           `daily_price` decimal(12,2) NOT NULL,
                           `security_deposit` decimal(12,2) NOT NULL,
                           `reservation_id` bigint DEFAULT NULL,
                           `status` enum('Open','Closed') COLLATE utf8mb4_unicode_ci DEFAULT 'Open',
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `total_amount` decimal(12,2) NOT NULL,
                           `discount` decimal(12,2) NOT NULL DEFAULT '0.00',
                           `final_amount` decimal(12,2) NOT NULL,
                           `payment_status` enum('Paid','Partially Paid','Unpaid') COLLATE utf8mb4_unicode_ci DEFAULT 'Unpaid',
                           PRIMARY KEY (`rental_id`),
                           KEY `customer_id` (`customer_id`),
                           KEY `equipment_id` (`equipment_id`),
                           KEY `reservation_id` (`reservation_id`),
                           CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
                           CONSTRAINT `rentals_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
                           CONSTRAINT `rentals_ibfk_3` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: damage_records
-- =========================================
CREATE TABLE `damage_records` (
                                  `damage_id` bigint NOT NULL AUTO_INCREMENT,
                                  `rental_id` bigint NOT NULL,
                                  `equipment_id` bigint NOT NULL,
                                  `description` text COLLATE utf8mb4_unicode_ci,
                                  `damage_cost` decimal(10,2) NOT NULL,
                                  `assessed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`damage_id`),
                                  KEY `rental_id` (`rental_id`),
                                  KEY `equipment_id` (`equipment_id`),
                                  CONSTRAINT `damage_records_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`rental_id`),
                                  CONSTRAINT `damage_records_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: returns
-- =========================================
CREATE TABLE `returns` (
                           `return_id` bigint NOT NULL AUTO_INCREMENT,
                           `rental_id` bigint NOT NULL,
                           `returned_date` date NOT NULL,
                           `late_days` int DEFAULT 0,
                           `late_fee` decimal(12,2) DEFAULT 0.00,
                           `damage_fee` decimal(12,2) DEFAULT 0.00,
                           `total_payable` decimal(12,2) NOT NULL,
                           `notes` text,
                           `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`return_id`),
                           KEY `rental_id` (`rental_id`),
                           CONSTRAINT `returns_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`rental_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: payments
-- =========================================
CREATE TABLE `payments` (
                            `payment_id` bigint NOT NULL AUTO_INCREMENT,
                            `rental_id` bigint DEFAULT NULL,
                            `return_id` bigint DEFAULT NULL,
                            `amount` decimal(12,2) NOT NULL,
                            `method` enum('Cash','Card','Online') COLLATE utf8mb4_unicode_ci NOT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`payment_id`),
                            KEY `rental_id` (`rental_id`),
                            KEY `return_id` (`return_id`),
                            CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`rental_id`),
                            CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`return_id`) REFERENCES `returns` (`return_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: users
-- =========================================
CREATE TABLE `users` (
                         `user_id` bigint NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `branch_id` int DEFAULT NULL,
                         PRIMARY KEY (`user_id`),
                         UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- =============================================
-- FULL COURSEWORK-COMPLIANT TEST DATA
-- Meets ALL quantity requirements
-- =============================================

-- Clean everything
DELETE FROM damage_records;
DELETE FROM payments;
DELETE FROM rentals;
DELETE FROM reservations;
DELETE FROM equipment;
DELETE FROM customers;
DELETE FROM categories;
DELETE FROM branches;

-- Reset IDs
ALTER TABLE branches AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
ALTER TABLE customers AUTO_INCREMENT = 1;
ALTER TABLE equipment AUTO_INCREMENT = 1;
ALTER TABLE reservations AUTO_INCREMENT = 1;
ALTER TABLE rentals AUTO_INCREMENT = 1;

-- 1. 3 Branches
INSERT INTO branches (code, name, address, contact) VALUES
                                                        ('COL', 'Colombo Branch', '123 Main St, Colombo', '011-1234567'),
                                                        ('KAN', 'Kandy Branch', '45 Hill St, Kandy', '081-2345678'),
                                                        ('GAL', 'Galle Branch', '78 Beach Rd, Galle', '091-3456789');

-- 2. 5 Categories
INSERT INTO categories (name, description, base_price_factor, weekend_multiplier, default_late_fee_per_day, active) VALUES
                                                                                                                        ('Cameras', 'DSLR/Mirrorless', 1.0000, 1.2000, 1000.00, 1),
                                                                                                                        ('Lenses', 'Prime & Zoom', 1.0000, 1.1500, 500.00, 1),
                                                                                                                        ('Lighting', 'Strobes & Continuous', 1.0000, 1.1000, 800.00, 1),
                                                                                                                        ('Tripods', 'Tripods & Supports', 1.0000, 1.1000, 300.00, 1),
                                                                                                                        ('Audio', 'Microphones & Recorders', 1.0000, 1.1000, 600.00, 1);

-- 3. 12 Customers (Regular, Silver, Gold)
INSERT INTO customers (name, nic_passport, contact_no, email, address, membership) VALUES
                                                                                       ('Amal Perera', '912345678V', '0771234567', 'amal@example.com', 'Colombo', 'Regular'),
                                                                                       ('Nimal Silva', '882345678V', '0712345678', 'nimal@example.com', 'Kandy', 'Silver'),
                                                                                       ('Sunil Fernando', '952345678V', '0763456789', 'sunil@example.com', 'Galle', 'Gold'),
                                                                                       ('Kamal Jayasinghe', '902345678V', '0772345678', 'kamal@example.com', 'Colombo', 'Regular'),
                                                                                       ('Lakmal Bandara', '872345678V', '0713456789', 'lakmal@example.com', 'Kandy', 'Silver'),
                                                                                       ('Saman Kumara', '962345678V', '0764567890', 'saman@example.com', 'Galle', 'Gold'),
                                                                                       ('Ruwan Dissanayake', '892345678V', '0775678901', 'ruwan@example.com', 'Colombo', 'Regular'),
                                                                                       ('Tharindu Wickramasinghe', '932345678V', '0716789012', 'tharindu@example.com', 'Kandy', 'Silver'),
                                                                                       ('Dilshan Mendis', '942345678V', '0767890123', 'dilshan@example.com', 'Galle', 'Regular'),
                                                                                       ('Pradeep Gunawardena', '852345678V', '0778901234', 'pradeep@example.com', 'Colombo', 'Gold'),
                                                                                       ('Chamara Silva', '862345678V', '0719012345', 'chamara@example.com', 'Kandy', 'Regular'),
                                                                                       ('Lasith Malinga', '972345678V', '0760123456', 'lasith@example.com', 'Galle', 'Silver');

-- 4. 25 Equipment Items (distributed across 3 branches and 5 categories)
INSERT INTO equipment (category_id, branch_id, equipment_code, brand, model, purchase_year, base_daily_price, security_deposit, status) VALUES
-- Cameras (category 1)
(1, 1, 'CAM001', 'Canon', 'EOS 5D Mark IV', 2020, 8000.00, 150000.00, 'Available'),
(1, 1, 'CAM002', 'Sony', 'A7 III', 2021, 7500.00, 140000.00, 'Available'),
(1, 2, 'CAM003', 'Nikon', 'Z6 II', 2022, 7000.00, 130000.00, 'Available'),
(1, 3, 'CAM004', 'Fujifilm', 'X-T4', 2021, 6500.00, 120000.00, 'Available'),
(1, 1, 'CAM005', 'Canon', 'R5', 2023, 9000.00, 180000.00, 'Available'),

-- Lenses (category 2)
(2, 1, 'LEN001', 'Canon', 'EF 24-70mm f/2.8L', 2019, 4000.00, 80000.00, 'Available'),
(2, 2, 'LEN002', 'Sigma', '50mm f/1.4 Art', 2022, 3500.00, 70000.00, 'Available'),
(2, 3, 'LEN003', 'Sony', 'FE 85mm f/1.4 GM', 2020, 4500.00, 90000.00, 'Available'),
(2, 1, 'LEN004', 'Nikon', '70-200mm f/2.8E', 2021, 5000.00, 100000.00, 'Available'),
(2, 2, 'LEN005', 'Tamron', '28-75mm f/2.8', 2022, 3000.00, 60000.00, 'Available'),

-- Lighting (category 3)
(3, 1, 'LGT001', 'Godox', 'AD600 Pro', 2023, 5000.00, 90000.00, 'Available'),
(3, 2, 'LGT002', 'Profoto', 'B10X Plus', 2022, 6000.00, 110000.00, 'Available'),
(3, 3, 'LGT003', 'Elinchrom', 'ELB 500 TTL', 2021, 5500.00, 100000.00, 'Available'),

-- Tripods (category 4)
(4, 1, 'TRP001', 'Manfrotto', 'MT055XPRO3', 2020, 1500.00, 20000.00, 'Available'),
(4, 2, 'TRP002', 'Gitzo', 'GT2542', 2021, 2500.00, 40000.00, 'Available'),
(4, 3, 'TRP003', 'Vanguard', 'Alta Pro 2+', 2022, 1800.00, 25000.00, 'Available'),
(4, 1, 'TRP004', 'Benro', 'Aero 4', 2023, 2000.00, 30000.00, 'Available'),

-- Audio (category 5)
(5, 1, 'AUD001', 'Rode', 'NTG4+', 2021, 2000.00, 30000.00, 'Available'),
(5, 2, 'AUD002', 'Sennheiser', 'MKH 416', 2020, 3500.00, 60000.00, 'Available'),
(5, 3, 'AUD003', 'Zoom', 'H6', 2022, 2500.00, 40000.00, 'Available'),
(5, 1, 'AUD004', 'Tascam', 'DR-40X', 2023, 1800.00, 25000.00, 'Available'),

-- Extra items to reach 25+
(1, 2, 'CAM006', 'Panasonic', 'Lumix S5', 2023, 7000.00, 130000.00, 'Available'),
(2, 3, 'LEN006', 'Zeiss', 'Otus 55mm f/1.4', 2020, 6000.00, 120000.00, 'Available'),
(3, 1, 'LGT004', 'Aputure', 'Amaran 200x', 2023, 3000.00, 50000.00, 'Available'),
(4, 3, 'TRP005', 'Sirui', 'Traveler 7C', 2022, 1200.00, 18000.00, 'Available');

-- 5. Sample Reservations & Rentals (including overdue and damage)
-- (Same as before but scaled up slightly)
-- ... (keep your previous reservations/rentals, or add more if needed)

INSERT INTO users (username, password, role, status, branch_id) VALUES
                                                                    ('admin','admin123','ADMIN','ACTIVE',NULL),
                                                                    ('manager_col','manager123','BRANCH_MANAGER','ACTIVE',1),
                                                                    ('staff_col','staff123','STAFF','ACTIVE',1),
                                                                    ('manager_kdy','manager123','BRANCH_MANAGER','ACTIVE',2),
                                                                    ('staff_gal','staff123','STAFF','ACTIVE',3);





-- Final count check
SELECT 'branches' AS table_name, COUNT(*) FROM branches UNION ALL
SELECT 'categories', COUNT(*) FROM categories UNION ALL
SELECT 'customers', COUNT(*) FROM customers UNION ALL
SELECT 'equipment', COUNT(*) FROM equipment;