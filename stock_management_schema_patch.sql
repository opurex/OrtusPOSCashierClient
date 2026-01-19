-- Patch script to update existing stock management schema

-- Add missing columns to stock_transactions table
ALTER TABLE stock_transactions ADD COLUMN product_name VARCHAR(255) NOT NULL AFTER product_id;
ALTER TABLE stock_transactions MODIFY transaction_type ENUM('receipt', 'issue', 'adjustment', 'transfer') NOT NULL;
ALTER TABLE stock_transactions ADD COLUMN location_from VARCHAR(255) DEFAULT NULL AFTER reference;
ALTER TABLE stock_transactions ADD COLUMN location_to VARCHAR(255) DEFAULT NULL AFTER location_from;

-- Populate product_name column with existing product labels
UPDATE stock_transactions st
JOIN products p ON st.product_id = p.id
SET st.product_name = p.label
WHERE st.product_name = '';

-- Add indexes for better performance
ALTER TABLE stock_transactions ADD INDEX IDX_STOCK_TRANSACTIONS_PRODUCT_NAME (product_name);