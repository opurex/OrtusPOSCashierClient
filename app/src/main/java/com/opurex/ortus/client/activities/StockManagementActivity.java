///*
//    Opurex Android com.opurex.ortus.client
//    Copyright (C) Opurex contributors, see the COPYRIGHT file
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
//*/
//package com.opurex.ortus.client.activities;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.opurex.ortus.client.R;
//import com.opurex.ortus.client.data.DatabaseHelper;
//import com.opurex.ortus.client.data.DataSavable.UserData;
//
///**
// * Activity for managing stock levels for a product.
// */
//public class StockManagementActivity extends Activity {
//
//    private Product product;
//    private StockManager stockManager;
//    private Stock currentStock;
//
//    private TextView productLabelTextView;
//    private TextView currentStockTextView;
//    private TextView securityLevelTextView;
//    private TextView maxLevelTextView;
//    private EditText quantityEditText;
//    private EditText reasonEditText;
//    private EditText referenceEditText;
//    private Button addButton;
//    private Button removeButton;
//    private Button updateButton;
//    private Button setSecurityLevelButton;
//    private Button setMaxLevelButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_stock_management);
//
//        // Get the product from the intent
//        String productId = getIntent().getStringExtra("PRODUCT_ID");
//        if (productId != null) {
//            product = Data.Catalog.catalog(this).getProduct(productId);
//        }
//
//        if (product == null) {
//            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Initialize stock manager with synchronization
//        stockManager = new StockManager(this, new DatabaseHelper(this));
//
//        // Get stock information (synchronize with server)
//        stockManager.getStockForProduct(product.getId(), true, new StockManager.StockCallback() {
//            @Override
//            public void onSuccess(Stock stock) {
//                currentStock = stock;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateUI();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                Toast.makeText(StockManagementActivity.this, "Error loading stock: " + error, Toast.LENGTH_SHORT).show();
//                // Still show UI with default stock
//                currentStock = new Stock(product.getId(), null, null, null);
//                updateUI();
//            }
//        });
//
//        // Initialize views
//        initializeViews();
//
//        // Set up button click listeners
//        setupClickListeners();
//    }
//
//    private void initializeViews() {
//        productLabelTextView = findViewById(R.id.product_label);
//        currentStockTextView = findViewById(R.id.current_stock);
//        securityLevelTextView = findViewById(R.id.security_level);
//        maxLevelTextView = findViewById(R.id.max_level);
//        quantityEditText = findViewById(R.id.quantity);
//        reasonEditText = findViewById(R.id.reason);
//        referenceEditText = findViewById(R.id.reference);
//        addButton = findViewById(R.id.add_stock);
//        removeButton = findViewById(R.id.remove_stock);
//        updateButton = findViewById(R.id.update_stock);
//        setSecurityLevelButton = findViewById(R.id.set_security_level);
//        setMaxLevelButton = findViewById(R.id.set_max_level);
//    }
//
//    private void updateUI() {
//        if (productLabelTextView != null) {
//            productLabelTextView.setText(product.getLabel());
//        }
//
//        if (currentStockTextView != null) {
//            if (currentStock.isManaged()) {
//                currentStockTextView.setText(String.valueOf(currentStock.getQuantity()));
//            } else {
//                currentStockTextView.setText("Not tracked");
//            }
//        }
//
//        if (securityLevelTextView != null) {
//            if (currentStock.hasSecurityLevel()) {
//                securityLevelTextView.setText(String.valueOf(currentStock.getSecurity()));
//            } else {
//                securityLevelTextView.setText("Not set");
//            }
//        }
//
//        if (maxLevelTextView != null) {
//            if (currentStock.hasMaxLevel()) {
//                maxLevelTextView.setText(String.valueOf(currentStock.getMaxLevel()));
//            } else {
//                maxLevelTextView.setText("Not set");
//            }
//        }
//    }
//
//    private void setupClickListeners() {
//        if (addButton != null) {
//            addButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addStock();
//                }
//            });
//        }
//
//        if (removeButton != null) {
//            removeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    removeStock();
//                }
//            });
//        }
//
//        if (updateButton != null) {
//            updateButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updateStock();
//                }
//            });
//        }
//
//        if (setSecurityLevelButton != null) {
//            setSecurityLevelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setSecurityLevel();
//                }
//            });
//        }
//
//        if (setMaxLevelButton != null) {
//            setMaxLevelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setMaxLevel();
//                }
//            });
//        }
//    }
//
//    private void addStock() {
//        String quantityStr = quantityEditText.getText().toString();
//        String reason = reasonEditText.getText().toString();
//        String reference = referenceEditText.getText().toString();
//
//        if (quantityStr.isEmpty()) {
//            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            double quantity = Double.parseDouble(quantityStr);
//            if (quantity <= 0) {
//                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Show progress
//            Toast.makeText(this, "Adding stock...", Toast.LENGTH_SHORT).show();
//
//            stockManager.addStock(
//                product.getId(),
//                quantity,
//                reason.isEmpty() ? "Stock receipt" : reason,
//                reference,
//                Integer.parseInt(Data.User.users(this).get(0).getId()),
//                new StockManager.StockCallback() {
//                    @Override
//                    public void onSuccess(Stock stock) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(StockManagementActivity.this, "Stock added successfully", Toast.LENGTH_SHORT).show();
//                                currentStock = stock;
//                                updateUI();
//
//                                // Clear input fields
//                                quantityEditText.setText("");
//                                reasonEditText.setText("");
//                                referenceEditText.setText("");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(StockManagementActivity.this, "Failed to add stock: " + error, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            );
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void removeStock() {
//        String quantityStr = quantityEditText.getText().toString();
//
//        if (quantityStr.isEmpty()) {
//            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            double quantity = Double.parseDouble(quantityStr);
//            if (quantity <= 0) {
//                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String reason = reasonEditText.getText().toString();
//            String reference = referenceEditText.getText().toString();
//
//            // Confirm removal
//            new AlertDialog.Builder(this)
//                .setTitle("Confirm Stock Removal")
//                .setMessage("Are you sure you want to remove " + quantity + " units from stock?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Show progress
//                        Toast.makeText(StockManagementActivity.this, "Removing stock...", Toast.LENGTH_SHORT).show();
//
//                        stockManager.removeStock(
//                            product.getId(),
//                            quantity,
//                            reason.isEmpty() ? "Stock issue" : reason,
//                            reference,
//                            Integer.parseInt(Data.User.users(StockManagementActivity.this).get(0).getId()),
//                            new StockManager.StockCallback() {
//                                @Override
//                                public void onSuccess(Stock stock) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(StockManagementActivity.this, "Stock removed successfully", Toast.LENGTH_SHORT).show();
//                                            currentStock = stock;
//                                            updateUI();
//
//                                            // Clear input fields
//                                            quantityEditText.setText("");
//                                            reasonEditText.setText("");
//                                            referenceEditText.setText("");
//                                        }
//                                    });
//                                }
//
//                                @Override
//                                public void onError(String error) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(StockManagementActivity.this, "Failed to remove stock: " + error, Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        );
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void updateStock() {
//        String quantityStr = quantityEditText.getText().toString();
//
//        if (quantityStr.isEmpty()) {
//            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            double quantity = Double.parseDouble(quantityStr);
//            if (quantity < 0) {
//                Toast.makeText(this, "Quantity cannot be negative", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String reason = reasonEditText.getText().toString();
//            String reference = referenceEditText.getText().toString();
//
//            // Show progress
//            Toast.makeText(this, "Updating stock...", Toast.LENGTH_SHORT).show();
//
//            stockManager.updateStock(
//                product.getId(),
//                quantity,
//                reason.isEmpty() ? "Stock adjustment" : reason,
//                reference,
//                Integer.parseInt(Data.User.users(this).get(0).getId()),
//                new StockManager.StockCallback() {
//                    @Override
//                    public void onSuccess(Stock stock) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(StockManagementActivity.this, "Stock updated successfully", Toast.LENGTH_SHORT).show();
//                                currentStock = stock;
//                                updateUI();
//
//                                // Clear input fields
//                                quantityEditText.setText("");
//                                reasonEditText.setText("");
//                                referenceEditText.setText("");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(StockManagementActivity.this, "Failed to update stock: " + error, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            );
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void setSecurityLevel() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Set Security Level");
//
//        final EditText input = new EditText(this);
//        if (currentStock.hasSecurityLevel()) {
//            input.setText(String.valueOf(currentStock.getSecurity()));
//        }
//        builder.setView(input);
//
//        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String valueStr = input.getText().toString();
//                if (!valueStr.isEmpty()) {
//                    try {
//                        double value = Double.parseDouble(valueStr);
//                        if (value >= 0) {
//                            boolean success = stockManager.setSecurityLevel(product.getId(), value);
//                            if (success) {
//                                Toast.makeText(StockManagementActivity.this, "Security level set successfully", Toast.LENGTH_SHORT).show();
//                                currentStock.setSecurity(value);
//                                updateUI();
//                            } else {
//                                Toast.makeText(StockManagementActivity.this, "Failed to set security level", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(StockManagementActivity.this, "Security level cannot be negative", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (NumberFormatException e) {
//                        Toast.makeText(StockManagementActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//
//        builder.show();
//    }
//
//    private void setMaxLevel() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Set Maximum Level");
//
//        final EditText input = new EditText(this);
//        if (currentStock.hasMaxLevel()) {
//            input.setText(String.valueOf(currentStock.getMaxLevel()));
//        }
//        builder.setView(input);
//
//        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String valueStr = input.getText().toString();
//                if (!valueStr.isEmpty()) {
//                    try {
//                        double value = Double.parseDouble(valueStr);
//                        if (value >= 0) {
//                            boolean success = stockManager.setMaxLevel(product.getId(), value);
//                            if (success) {
//                                Toast.makeText(StockManagementActivity.this, "Maximum level set successfully", Toast.LENGTH_SHORT).show();
//                                currentStock.setMaxLevel(value);
//                                updateUI();
//                            } else {
//                                Toast.makeText(StockManagementActivity.this, "Failed to set maximum level", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(StockManagementActivity.this, "Maximum level cannot be negative", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (NumberFormatException e) {
//                        Toast.makeText(StockManagementActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//
//        builder.show();
//    }
//}