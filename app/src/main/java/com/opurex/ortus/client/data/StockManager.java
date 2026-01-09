/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.opurex.ortus.client.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.models.Stock;
import com.opurex.ortus.client.services.StockSyncService;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for handling stock operations in the Android app.
 */
public class StockManager {
    
    private static final String TAG = "StockManager";
    private static final String TABLE_STOCK_LEVELS = "stock_levels";
    private static final String TABLE_STOCK_TRANSACTIONS = "stock_transactions";
    
    // Stock levels table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_SECURITY_LEVEL = "security_level";
    private static final String COLUMN_MAX_LEVEL = "max_level";
    private static final String COLUMN_LAST_UPDATED = "last_updated";
    private static final String COLUMN_LOCATION = "location";
    
    // Stock transactions table columns
    private static final String COLUMN_TRANSACTION_TYPE = "transaction_type";
    private static final String COLUMN_PREVIOUS_QUANTITY = "previous_quantity";
    private static final String COLUMN_NEW_QUANTITY = "new_quantity";
    private static final String COLUMN_REASON = "reason";
    private static final String COLUMN_REFERENCE = "reference";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_CREATED_BY = "created_by";
    
    private DatabaseHelper dbHelper;
    private StockSyncService syncService;
    private Context context;
    
    public StockManager(Context context, DatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.syncService = new StockSyncService(context);
    }
    
    /**
     * Get stock level for a product
     * @param productId The product ID
     * @return Stock object with current stock information
     */
    public Stock getStockForProduct(String productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        
        try {
            String query = "SELECT * FROM " + TABLE_STOCK_LEVELS + 
                          " WHERE " + COLUMN_PRODUCT_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{productId});
            
            if (cursor.moveToFirst()) {
                Double quantity = cursor.isNull(cursor.getColumnIndex(COLUMN_QUANTITY)) ? 
                                 null : cursor.getDouble(cursor.getColumnIndex(COLUMN_QUANTITY));
                Double security = cursor.isNull(cursor.getColumnIndex(COLUMN_SECURITY_LEVEL)) ? 
                                 null : cursor.getDouble(cursor.getColumnIndex(COLUMN_SECURITY_LEVEL));
                Double max = cursor.isNull(cursor.getColumnIndex(COLUMN_MAX_LEVEL)) ? 
                            null : cursor.getDouble(cursor.getColumnIndex(COLUMN_MAX_LEVEL));
                
                return new Stock(productId, quantity, security, max);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        // Return default stock object if no record found
        return new Stock(productId, null, null, null);
    }
    
    /**
     * Get stock level for a product, synchronizing with server if needed
     * @param productId The product ID
     * @param syncWithServer Whether to synchronize with server
     * @param callback Callback to handle the result
     */
    public void getStockForProduct(String productId, boolean syncWithServer, StockCallback callback) {
        if (syncWithServer) {
            // Try to get from server first
            syncService.getStockFromServer(productId, new StockSyncService.StockCallback() {
                @Override
                public void onSuccess(Stock stock) {
                    // Update local database with server data
                    updateStockLocally(stock);
                    callback.onSuccess(stock);
                }
                
                @Override
                public void onError(String error) {
                    Log.w(TAG, "Failed to get stock from server, using local data: " + error);
                    // Fall back to local data
                    Stock localStock = getStockForProduct(productId);
                    callback.onSuccess(localStock);
                }
            });
        } else {
            Stock localStock = getStockForProduct(productId);
            callback.onSuccess(localStock);
        }
    }
    
    /**
     * Update stock level for a product
     * @param productId The product ID
     * @param newQuantity The new quantity
     * @param reason The reason for the update
     * @param reference Optional reference (e.g., invoice number)
     * @param userId The user performing the update
     * @return true if successful, false otherwise
     */
    public boolean updateStock(String productId, double newQuantity, String reason, String reference, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Get current stock level
        Stock currentStock = getStockForProduct(productId);
        Double previousQuantity = currentStock.getQuantity();
        
        db.beginTransaction();
        try {
            // Update stock level
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_ID, productId);
            values.put(COLUMN_QUANTITY, newQuantity);
            values.put(COLUMN_LAST_UPDATED, System.currentTimeMillis() / 1000);
            
            long result;
            if (previousQuantity == null) {
                // Insert new record
                result = db.insert(TABLE_STOCK_LEVELS, null, values);
            } else {
                // Update existing record
                result = db.update(TABLE_STOCK_LEVELS, values, 
                                 COLUMN_PRODUCT_ID + " = ?", 
                                 new String[]{productId});
            }
            
            if (result == -1) {
                db.endTransaction();
                return false;
            }
            
            // Record transaction
            ContentValues transactionValues = new ContentValues();
            transactionValues.put(COLUMN_PRODUCT_ID, productId);
            transactionValues.put(COLUMN_TRANSACTION_TYPE, "adjustment");
            transactionValues.put(COLUMN_QUANTITY, newQuantity - (previousQuantity == null ? 0 : previousQuantity));
            transactionValues.put(COLUMN_PREVIOUS_QUANTITY, previousQuantity == null ? 0 : previousQuantity);
            transactionValues.put(COLUMN_NEW_QUANTITY, newQuantity);
            transactionValues.put(COLUMN_REASON, reason);
            transactionValues.put(COLUMN_REFERENCE, reference);
            transactionValues.put(COLUMN_CREATED_BY, userId);
            
            long transactionResult = db.insert(TABLE_STOCK_TRANSACTIONS, null, transactionValues);
            if (transactionResult == -1) {
                db.endTransaction();
                return false;
            }
            
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }
    
    /**
     * Update stock level in local database only
     * @param stock The stock object to update
     * @return true if successful, false otherwise
     */
    private boolean updateStockLocally(Stock stock) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, stock.getProductId());
        values.put(COLUMN_QUANTITY, stock.getQuantity());
        values.put(COLUMN_SECURITY_LEVEL, stock.getSecurity());
        values.put(COLUMN_MAX_LEVEL, stock.getMaxLevel());
        values.put(COLUMN_LAST_UPDATED, System.currentTimeMillis() / 1000);
        
        long result = db.insertWithOnConflict(TABLE_STOCK_LEVELS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        
        return result != -1;
    }
    
    /**
     * Update stock level for a product, synchronizing with server
     * @param productId The product ID
     * @param newQuantity The new quantity
     * @param reason The reason for the update
     * @param reference Optional reference (e.g., invoice number)
     * @param userId The user performing the update
     * @param callback Callback to handle the result
     */
    public void updateStock(String productId, double newQuantity, String reason, String reference, int userId, StockCallback callback) {
        Stock stock = new Stock(productId, newQuantity, null, null);
        // Try to update on server first
        syncService.updateStockOnServer(productId, stock, new StockSyncService.StockCallback() {
            @Override
            public void onSuccess(Stock stock) {
                // Update local database
                updateStockLocally(stock);
                callback.onSuccess(stock);
            }
            
            @Override
            public void onError(String error) {
                Log.w(TAG, "Failed to update stock on server, updating locally: " + error);
                // Fall back to local update
                boolean success = updateStock(productId, newQuantity, reason, reference, userId);
                if (success) {
                    Stock updatedStock = getStockForProduct(productId);
                    callback.onSuccess(updatedStock);
                } else {
                    callback.onError("Failed to update stock locally");
                }
            }
        });
    }
    
    /**
     * Add stock to a product (receipt)
     * @param productId The product ID
     * @param quantity The quantity to add
     * @param reason The reason for the receipt
     * @param reference Optional reference (e.g., purchase order number)
     * @param userId The user performing the receipt
     * @return true if successful, false otherwise
     */
    public boolean addStock(String productId, double quantity, String reason, String reference, int userId) {
        Stock currentStock = getStockForProduct(productId);
        double newQuantity = (currentStock.getQuantity() == null ? 0 : currentStock.getQuantity()) + quantity;
        return updateStock(productId, newQuantity, reason, reference, userId);
    }
    
    /**
     * Add stock to a product (receipt), synchronizing with server
     * @param productId The product ID
     * @param quantity The quantity to add
     * @param reason The reason for the receipt
     * @param reference Optional reference (e.g., purchase order number)
     * @param userId The user performing the receipt
     * @param callback Callback to handle the result
     */
    public void addStock(String productId, double quantity, String reason, String reference, int userId, StockCallback callback) {
        // Try to add on server first
        syncService.addStockOnServer(productId, quantity, reason, reference, new StockSyncService.StockCallback() {
            @Override
            public void onSuccess(Stock stock) {
                // Update local database
                updateStockLocally(stock);
                callback.onSuccess(stock);
            }
            
            @Override
            public void onError(String error) {
                Log.w(TAG, "Failed to add stock on server, adding locally: " + error);
                // Fall back to local update
                boolean success = addStock(productId, quantity, reason, reference, userId);
                if (success) {
                    Stock updatedStock = getStockForProduct(productId);
                    callback.onSuccess(updatedStock);
                } else {
                    callback.onError("Failed to add stock locally");
                }
            }
        });
    }
    
    /**
     * Remove stock from a product (issue)
     * @param productId The product ID
     * @param quantity The quantity to remove
     * @param reason The reason for the issue
     * @param reference Optional reference (e.g., invoice number)
     * @param userId The user performing the issue
     * @return true if successful, false otherwise
     */
    public boolean removeStock(String productId, double quantity, String reason, String reference, int userId) {
        Stock currentStock = getStockForProduct(productId);
        double currentQuantity = currentStock.getQuantity() == null ? 0 : currentStock.getQuantity();
        
        // Check if we have enough stock
        if (currentQuantity < quantity) {
            return false; // Not enough stock
        }
        
        double newQuantity = currentQuantity - quantity;
        return updateStock(productId, newQuantity, reason, reference, userId);
    }

    /**
     * Remove stock from a product (issue), synchronizing with server
     * @param productId The product ID
     * @param quantity The quantity to remove
     * @param reason The reason for the issue
     * @param reference Optional reference (e.g., invoice number)
     * @param userId The user performing the issue
     * @param callback Callback to handle the result
     */
    public void removeStock(String productId, double quantity, String reason, String reference, int userId, StockCallback callback) {
        // Try to remove on server first
        syncService.removeStockOnServer(productId, quantity, reason, reference, new StockSyncService.StockCallback() {
            @Override
            public void onSuccess(Stock stock) {
                // Update local database
                updateStockLocally(stock);
                callback.onSuccess(stock);
            }
            
            @Override
            public void onError(String error) {
                Log.w(TAG, "Failed to remove stock on server, removing locally: " + error);
                // Fall back to local update
                boolean success = removeStock(productId, quantity, reason, reference, userId);
                if (success) {
                    Stock updatedStock = getStockForProduct(productId);
                    callback.onSuccess(updatedStock);
                } else {
                    callback.onError("Failed to remove stock locally");
                }
            }
        });
    }
    
    /**
     * Get all products with low stock (below security level)
     * @return List of product IDs with low stock
     */
    public List<String> getLowStockProducts() {
        List<String> lowStockProducts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        
        try {
            String query = "SELECT sl." + COLUMN_PRODUCT_ID + 
                          " FROM " + TABLE_STOCK_LEVELS + " sl" +
                          " WHERE sl." + COLUMN_SECURITY_LEVEL + " IS NOT NULL" +
                          " AND sl." + COLUMN_QUANTITY + " < sl." + COLUMN_SECURITY_LEVEL;
            cursor = db.rawQuery(query, null);
            
            while (cursor.moveToNext()) {
                String productId = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
                lowStockProducts.add(productId);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return lowStockProducts;
    }
    
    /**
     * Get all products with low stock, synchronizing with server
     * @param callback Callback to handle the result
     */
    public void getLowStockProducts(LowStockCallback callback) {
        // Try to get from server first
        syncService.getLowStockProductsFromServer(new StockSyncService.LowStockCallback() {
            @Override
            public void onSuccess(String[] productIds) {
                callback.onSuccess(productIds);
            }
            
            @Override
            public void onError(String error) {
                Log.w(TAG, "Failed to get low stock products from server, using local data: " + error);
                // Fall back to local data
                List<String> localLowStockProducts = getLowStockProducts();
                String[] productIds = localLowStockProducts.toArray(new String[0]);
                callback.onSuccess(productIds);
            }
        });
    }
    
    /**
     * Get all stock transactions for a product
     * @param productId The product ID
     * @return List of transaction records
     */
    public List<StockTransaction> getStockTransactions(String productId) {
        List<StockTransaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        
        try {
            String query = "SELECT * FROM " + TABLE_STOCK_TRANSACTIONS + 
                          " WHERE " + COLUMN_PRODUCT_ID + " = ?" +
                          " ORDER BY " + COLUMN_CREATED_AT + " DESC";
            cursor = db.rawQuery(query, new String[]{productId});
            
            while (cursor.moveToNext()) {
                StockTransaction transaction = new StockTransaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                transaction.setProductId(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                transaction.setTransactionType(cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTION_TYPE)));
                transaction.setQuantity(cursor.getDouble(cursor.getColumnIndex(COLUMN_QUANTITY)));
                transaction.setPreviousQuantity(cursor.getDouble(cursor.getColumnIndex(COLUMN_PREVIOUS_QUANTITY)));
                transaction.setNewQuantity(cursor.getDouble(cursor.getColumnIndex(COLUMN_NEW_QUANTITY)));
                transaction.setReason(cursor.getString(cursor.getColumnIndex(COLUMN_REASON)));
                transaction.setReference(cursor.getString(cursor.getColumnIndex(COLUMN_REFERENCE)));
                transaction.setCreatedAt(cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT)));
                transaction.setCreatedBy(cursor.isNull(cursor.getColumnIndex(COLUMN_CREATED_BY)) ? 
                                        -1 : cursor.getInt(cursor.getColumnIndex(COLUMN_CREATED_BY)));
                
                transactions.add(transaction);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return transactions;
    }
    
    /**
     * Set security level for a product
     * @param productId The product ID
     * @param securityLevel The security level (minimum stock)
     * @return true if successful, false otherwise
     */
    public boolean setSecurityLevel(String productId, double securityLevel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_SECURITY_LEVEL, securityLevel);
        
        int result = db.update(TABLE_STOCK_LEVELS, values, 
                              COLUMN_PRODUCT_ID + " = ?", 
                              new String[]{productId});
        
        return result > 0;
    }
    
    /**
     * Set maximum level for a product
     * @param productId The product ID
     * @param maxLevel The maximum stock level
     * @return true if successful, false otherwise
     */
    public boolean setMaxLevel(String productId, double maxLevel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAX_LEVEL, maxLevel);
        
        int result = db.update(TABLE_STOCK_LEVELS, values, 
                              COLUMN_PRODUCT_ID + " = ?", 
                              new String[]{productId});
        
        return result > 0;
    }
    
    /**
     * Callback interface for stock operations
     */
    public interface StockCallback {
        void onSuccess(Stock stock);
        void onError(String error);
    }
    
    /**
     * Callback interface for low stock operations
     */
    public interface LowStockCallback {
        void onSuccess(String[] productIds);
        void onError(String error);
    }
    
    /**
     * Inner class to represent a stock transaction
     */
    public static class StockTransaction {
        private int id;
        private String productId;
        private String transactionType;
        private double quantity;
        private double previousQuantity;
        private double newQuantity;
        private String reason;
        private String reference;
        private long createdAt;
        private int createdBy;
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getTransactionType() { return transactionType; }
        public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
        
        public double getQuantity() { return quantity; }
        public void setQuantity(double quantity) { this.quantity = quantity; }
        
        public double getPreviousQuantity() { return previousQuantity; }
        public void setPreviousQuantity(double previousQuantity) { this.previousQuantity = previousQuantity; }
        
        public double getNewQuantity() { return newQuantity; }
        public void setNewQuantity(double newQuantity) { this.newQuantity = newQuantity; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        
        public int getCreatedBy() { return createdBy; }
        public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    }
}