-- Stock management schema for OrtusPOS

-- Table for tracking stock levels for products
CREATE TABLE stock_levels (
    id INT AUTO_INCREMENT NOT NULL,
    product_id INT NOT NULL,
    quantity DOUBLE PRECISION NOT NULL DEFAULT 0,
    security_level DOUBLE PRECISION DEFAULT NULL,
    max_level DOUBLE PRECISION DEFAULT NULL,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE INDEX UNIQ_STOCK_LEVELS_PRODUCT_ID (product_id),
    INDEX IDX_STOCK_LEVELS_QUANTITY (quantity)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB;

-- Foreign key constraint to link stock levels to products
ALTER TABLE stock_levels ADD CONSTRAINT FK_STOCK_LEVELS_PRODUCT_ID 
    FOREIGN KEY (product_id) REFERENCES products (id) 
    ON DELETE CASCADE;

-- Table for tracking stock transactions (receipts, issues, adjustments)
CREATE TABLE stock_transactions (
    id INT AUTO_INCREMENT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    transaction_type ENUM('receipt', 'issue', 'adjustment', 'transfer') NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    previous_quantity DOUBLE PRECISION NOT NULL,
    new_quantity DOUBLE PRECISION NOT NULL,
    reason VARCHAR(255) DEFAULT NULL,
    reference VARCHAR(255) DEFAULT NULL,
    location_from VARCHAR(255) DEFAULT NULL,
    location_to VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT DEFAULT NULL,
    PRIMARY KEY(id),
    INDEX IDX_STOCK_TRANSACTIONS_PRODUCT_ID (product_id),
    INDEX IDX_STOCK_TRANSACTIONS_TYPE (transaction_type),
    INDEX IDX_STOCK_TRANSACTIONS_CREATED_AT (created_at)
) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB;

-- Foreign key constraint to link stock transactions to products
ALTER TABLE stock_transactions ADD CONSTRAINT FK_STOCK_TRANSACTIONS_PRODUCT_ID 
    FOREIGN KEY (product_id) REFERENCES products (id) 
    ON DELETE CASCADE;

-- Foreign key constraint to link stock transactions to users
ALTER TABLE stock_transactions ADD CONSTRAINT FK_STOCK_TRANSACTIONS_USER_ID 
    FOREIGN KEY (created_by) REFERENCES users (id) 
    ON DELETE SET NULL;