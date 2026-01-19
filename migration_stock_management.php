<?php

/*
 * Migration script to add stock management tables to the existing database
 * This script should be run once to update the database schema
 */

// Database configuration
$host = 'localhost';
$dbname = 'ortuspos';
$username = 'your_username';
$password = 'your_password';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // Check if stock_levels table exists
    $stmt = $pdo->query("SHOW TABLES LIKE 'stock_levels'");
    $tableExists = $stmt->rowCount() > 0;
    
    if (!$tableExists) {
        echo "Creating stock_levels table...\n";
        
        // Create stock_levels table
        $sql = "
            CREATE TABLE IF NOT EXISTS stock_levels (
                id INT AUTO_INCREMENT NOT NULL,
                product_id VARCHAR(255) NOT NULL,
                quantity DOUBLE PRECISION NOT NULL DEFAULT 0,
                security_level DOUBLE PRECISION DEFAULT NULL,
                max_level DOUBLE PRECISION DEFAULT NULL,
                last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                location VARCHAR(255) DEFAULT NULL,
                PRIMARY KEY(id),
                UNIQUE INDEX UNIQ_STOCK_LEVELS_PRODUCT_ID (product_id)
            ) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB
        ";
        
        $pdo->exec($sql);
        
        // Add foreign key constraint
        $sql = "
            ALTER TABLE stock_levels ADD CONSTRAINT FK_STOCK_LEVELS_PRODUCT_ID 
                FOREIGN KEY (product_id) REFERENCES products (id) 
                ON DELETE CASCADE
        ";
        
        $pdo->exec($sql);
        
        echo "stock_levels table created successfully.\n";
    } else {
        echo "stock_levels table already exists.\n";
    }
    
    // Check if stock_transactions table exists
    $stmt = $pdo->query("SHOW TABLES LIKE 'stock_transactions'");
    $tableExists = $stmt->rowCount() > 0;
    
    if (!$tableExists) {
        echo "Creating stock_transactions table...\n";
        
        // Create stock_transactions table
        $sql = "
            CREATE TABLE IF NOT EXISTS stock_transactions (
                id INT AUTO_INCREMENT NOT NULL,
                product_id VARCHAR(255) NOT NULL,
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
            ) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB
        ";
        
        $pdo->exec($sql);
        
        // Add foreign key constraints
        $sql = "
            ALTER TABLE stock_transactions ADD CONSTRAINT FK_STOCK_TRANSACTIONS_PRODUCT_ID 
                FOREIGN KEY (product_id) REFERENCES products (id) 
                ON DELETE CASCADE
        ";
        
        $pdo->exec($sql);
        
        $sql = "
            ALTER TABLE stock_transactions ADD CONSTRAINT FK_STOCK_TRANSACTIONS_USER_ID 
                FOREIGN KEY (created_by) REFERENCES users (id) 
                ON DELETE SET NULL
        ";
        
        $pdo->exec($sql);
        
        echo "stock_transactions table created successfully.\n";
    } else {
        echo "stock_transactions table already exists. Applying schema updates...\n";
        
        // Check and add missing columns
        try {
            // Add product_name column
            $pdo->exec("ALTER TABLE stock_transactions ADD COLUMN product_name VARCHAR(255) NOT NULL AFTER product_id");
            echo "Added product_name column.\n";
        } catch (PDOException $e) {
            if (strpos($e->getMessage(), 'Duplicate column name') === false) {
                echo "Error adding product_name column: " . $e->getMessage() . "\n";
            } else {
                echo "product_name column already exists.\n";
            }
        }
        
        try {
            // Modify transaction_type to include 'transfer'
            $pdo->exec("ALTER TABLE stock_transactions MODIFY transaction_type ENUM('receipt', 'issue', 'adjustment', 'transfer') NOT NULL");
            echo "Updated transaction_type column to include 'transfer'.\n";
        } catch (PDOException $e) {
            echo "Error updating transaction_type column: " . $e->getMessage() . "\n";
        }
        
        try {
            // Add location_from column
            $pdo->exec("ALTER TABLE stock_transactions ADD COLUMN location_from VARCHAR(255) DEFAULT NULL AFTER reference");
            echo "Added location_from column.\n";
        } catch (PDOException $e) {
            if (strpos($e->getMessage(), 'Duplicate column name') === false) {
                echo "Error adding location_from column: " . $e->getMessage() . "\n";
            } else {
                echo "location_from column already exists.\n";
            }
        }
        
        try {
            // Add location_to column
            $pdo->exec("ALTER TABLE stock_transactions ADD COLUMN location_to VARCHAR(255) DEFAULT NULL AFTER location_from");
            echo "Added location_to column.\n";
        } catch (PDOException $e) {
            if (strpos($e->getMessage(), 'Duplicate column name') === false) {
                echo "Error adding location_to column: " . $e->getMessage() . "\n";
            } else {
                echo "location_to column already exists.\n";
            }
        }
        
        // Populate product_name column with existing product labels
        try {
            $stmt = $pdo->prepare("
                UPDATE stock_transactions st
                JOIN products p ON st.product_id = p.id
                SET st.product_name = p.label
                WHERE st.product_name = '' OR st.product_name IS NULL
            ");
            $stmt->execute();
            echo "Populated product_name column with product labels.\n";
        } catch (PDOException $e) {
            echo "Error populating product_name column: " . $e->getMessage() . "\n";
        }
    }
    
    echo "Migration completed successfully!\n";
    
} catch (PDOException $e) {
    echo "Error: " . $e->getMessage() . "\n";
    exit(1);
}