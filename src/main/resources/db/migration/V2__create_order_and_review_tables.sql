CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    listing_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL, -- e.g. 'COMPLETED', 'PENDING', 'CANCELLED'
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_listing FOREIGN KEY (listing_id) REFERENCES listings(id),
    CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES users(id)
);

CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE, -- One review per order
    rating INT NOT NULL,
    comment VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5)
);

-- Seed initial categories for listing creation
INSERT INTO categories (name, active) VALUES
('Electronics', TRUE),
('Books', TRUE),
('Clothing', TRUE),
('Home & Living', TRUE),
('Sports & Outdoors', TRUE);
