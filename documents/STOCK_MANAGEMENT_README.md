# OrtusPOS Stock Management Feature

## Overview

This feature adds comprehensive stock management capabilities to the OrtusPOS Android application. It allows users to track inventory levels, manage stock transactions (receipts and issues), and synchronize stock data with the web backend.

## Features

1. **Stock Level Tracking**: Track current stock levels for each product
2. **Security Levels**: Set minimum stock levels to trigger alerts
3. **Maximum Levels**: Set maximum stock levels for inventory control
4. **Stock Transactions**: Record all stock movements (receipts, issues, adjustments)
5. **Synchronization**: Sync stock data between Android app and web backend
6. **Low Stock Alerts**: Identify products with stock below security levels

## Database Schema

### Tables

1. **stock_levels**
   - Tracks current stock levels for each product
   - Columns: id, product_id, quantity, security_level, max_level, last_updated, location

2. **stock_transactions**
   - Records all stock movements
   - Columns: id, product_id, transaction_type, quantity, previous_quantity, new_quantity, reason, reference, created_at, created_by

### Relationships

- Both tables are linked to the `products` table via foreign key constraints
- Transaction records are automatically purged when a product is deleted

## API Endpoints

The web backend provides RESTful API endpoints for stock management:

1. **GET /stock/{productId}** - Get stock level for a product
2. **POST /stock/{productId}** - Update stock level for a product
3. **POST /stock/{productId}/add** - Add stock to a product (receipt)
4. **POST /stock/{productId}/remove** - Remove stock from a product (issue)
5. **GET /stock/low** - Get all products with low stock
6. **GET /stock/{productId}/transactions** - Get stock transactions for a product

## Android Components

### Classes

1. **StockManager** - Main class for stock operations with synchronization
2. **StockSyncService** - Handles API calls to the web backend
3. **StockManagementActivity** - UI for managing stock levels

### Models

1. **Stock** - Represents stock information for a product

## Installation

1. Run the database migration script to create the required tables:
   ```
   php migration_stock_management.php
   ```

2. Deploy the updated Android app with the new stock management features

3. Ensure the web backend API endpoints are accessible from the Android app

## Usage

1. Navigate to the stock management screen for any product
2. View current stock levels, security levels, and maximum levels
3. Add stock when receiving new inventory
4. Remove stock when selling or using inventory
5. Adjust stock levels for corrections
6. Set security and maximum levels for inventory control

## Synchronization

Stock data is automatically synchronized between the Android app and web backend:

- When viewing stock levels, the app first tries to fetch data from the server
- When updating stock, changes are first sent to the server
- If the server is unavailable, changes are stored locally and synced when connectivity is restored

## Error Handling

The system gracefully handles network failures and server errors:

- Local operations continue even when offline
- Failed synchronization attempts are retried automatically
- Users are notified of any errors that require attention