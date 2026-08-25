-- V4__seed_orders_and_users.sql
-- Truncate existing test data to ensure clean, reproducible state for testing
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE reviews;
TRUNCATE TABLE payments;
TRUNCATE TABLE messages;
TRUNCATE TABLE conversations;
TRUNCATE TABLE notifications;
TRUNCATE TABLE orders;
TRUNCATE TABLE listings;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Seed Users
-- The bcrypt hash matches 'password123'
INSERT INTO users (id, university_email, password_hash, full_name, role, email_verified) VALUES
(1, 'student@kln.ac.lk', '$2a$10$K1UcV7LQ927wsSuocYdC0e1T4Xst7eBoq7a6X0wAEty8/tsOgPgTa', 'John Doe', 'ROLE_USER', FALSE),
(2, 'seller@kln.ac.lk', '$2a$10$K1UcV7LQ927wsSuocYdC0e1T4Xst7eBoq7a6X0wAEty8/tsOgPgTa', 'Jane Seller', 'ROLE_SELLER', FALSE),
(3, 'another_buyer@kln.ac.lk', '$2a$10$K1UcV7LQ927wsSuocYdC0e1T4Xst7eBoq7a6X0wAEty8/tsOgPgTa', 'Another Buyer', 'ROLE_USER', FALSE);

-- Seed Listing
INSERT INTO listings (id, seller_id, category_id, title, description, price, status, version) VALUES
(1, 2, 2, 'Data Structures Textbook', 'Second edition, clean condition.', 2500.00, 'AVAILABLE', 0);

-- Seed Orders
-- Order 1: Completed order for student@kln.ac.lk (ID 1) -> Expect success (201)
-- Order 2: Pending order for student@kln.ac.lk (ID 1) -> Expect conflict (409)
-- Order 3: Completed order for another_buyer@kln.ac.lk (ID 3) -> Expect forbidden (403) for student@kln.ac.lk
INSERT INTO orders (id, listing_id, buyer_id, status, total_amount, payment_method) VALUES
(1, 1, 1, 'COMPLETED', 2500.00, 'CASH_ON_DELIVERY'),
(2, 1, 1, 'PENDING', 2500.00, 'CASH_ON_DELIVERY'),
(3, 1, 3, 'COMPLETED', 2500.00, 'CASH_ON_DELIVERY');
