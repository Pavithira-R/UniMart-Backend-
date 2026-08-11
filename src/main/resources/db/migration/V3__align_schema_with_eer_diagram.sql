-- Idempotent helper to add columns if they do not exist
DROP PROCEDURE IF EXISTS AddColumnIfNotExist;
DELIMITER //
CREATE PROCEDURE AddColumnIfNotExist(
    IN tableName VARCHAR(64),
    IN columnName VARCHAR(64),
    IN columnDef VARCHAR(255)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = tableName 
        AND COLUMN_NAME = columnName
    ) THEN
        SET @sqlStmt = CONCAT('ALTER TABLE ', tableName, ' ADD COLUMN ', columnName, ' ', columnDef);
        PREPARE stmt FROM @sqlStmt;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END //
DELIMITER ;

-- Call helper to add missing columns safely
CALL AddColumnIfNotExist('orders', 'total_amount', 'DECIMAL(12,2) NOT NULL DEFAULT 0.00');
CALL AddColumnIfNotExist('orders', 'payment_method', 'VARCHAR(50)');
CALL AddColumnIfNotExist('reviews', 'reviewer_id', 'BIGINT');
CALL AddColumnIfNotExist('reviews', 'reviewee_id', 'BIGINT');

DROP PROCEDURE IF EXISTS AddColumnIfNotExist;

-- Idempotent helper to add foreign key constraints if they do not exist
DROP PROCEDURE IF EXISTS AddConstraintIfNotExist;
DELIMITER //
CREATE PROCEDURE AddConstraintIfNotExist(
    IN tableName VARCHAR(64),
    IN constraintName VARCHAR(64),
    IN constraintDef VARCHAR(255)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.TABLE_CONSTRAINTS 
        WHERE CONSTRAINT_SCHEMA = DATABASE() 
        AND TABLE_NAME = tableName 
        AND CONSTRAINT_NAME = constraintName
    ) THEN
        SET @sqlStmt = CONCAT('ALTER TABLE ', tableName, ' ADD CONSTRAINT ', constraintName, ' ', constraintDef);
        PREPARE stmt FROM @sqlStmt;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END //
DELIMITER ;

CALL AddConstraintIfNotExist('reviews', 'fk_review_reviewer', 'FOREIGN KEY (reviewer_id) REFERENCES users(id)');
CALL AddConstraintIfNotExist('reviews', 'fk_review_reviewee', 'FOREIGN KEY (reviewee_id) REFERENCES users(id)');

DROP PROCEDURE IF EXISTS AddConstraintIfNotExist;

-- Modify comment column to TEXT (idempotent)
ALTER TABLE reviews MODIFY COLUMN comment TEXT;

-- 2. Create the missing tables if they do not exist
CREATE TABLE IF NOT EXISTS listing_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    listing_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_image_listing FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    listing_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conversation_listing FOREIGN KEY (listing_id) REFERENCES listings(id),
    CONSTRAINT fk_conversation_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
    CONSTRAINT fk_conversation_seller FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    provider_reference VARCHAR(255),
    idempotency_key VARCHAR(255),
    paid_at TIMESTAMP NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
