package com.opurex.ortus.client.utils;

import android.content.Context;
import android.util.Log;

import com.opurex.ortus.client.models.Product;

/**
 * Test class to simulate Bluetooth scale integration
 */
public class BluetoothScaleTester {
    private static final String TAG = "BluetoothScaleTester";
    
    private ScaleManager scaleManager;
    private Context context;
    
    public BluetoothScaleTester(Context context) {
        this.context = context;
        this.scaleManager = new ScaleManager(context);
    }
    
    /**
     * Test the scale integration
     */
    public void testScaleIntegration() {
        Log.d(TAG, "Starting Bluetooth scale integration test");
        
        // Test 1: Check if Bluetooth is supported
        boolean isBluetoothSupported = scaleManager.initializeScale();
        Log.d(TAG, "Bluetooth supported: " + isBluetoothSupported);
        
        if (!isBluetoothSupported) {
            Log.e(TAG, "Bluetooth not supported on this device");
            return;
        }
        
        // Test 2: Simulate product weighing
        Product testProduct = new Product("Test Product", 10.50, 12.00, 1); // Price: 10.50 per kg
        testProduct.setScaled(true);
        
        // Simulate weight reading
        double testWeight = 2.350; // 2.350 kg
        Log.d(TAG, "Simulated weight reading: " + testWeight + " kg");
        
        // Test 3: Calculate price
        double calculatedPrice = scaleManager.calculatePrice(testProduct, testWeight);
        Log.d(TAG, "Calculated price: " + calculatedPrice);
        
        // Expected price: 10.50 * 2.350 = 24.675
        double expectedPrice = 24.675;
        boolean priceCalculationCorrect = Math.abs(calculatedPrice - expectedPrice) < 0.001;
        Log.d(TAG, "Price calculation correct: " + priceCalculationCorrect);
        
        // Test 4: Format price string
        String formattedPrice = scaleManager.calculatePriceString(testProduct, testWeight);
        Log.d(TAG, "Formatted price: " + formattedPrice);
        
        // Expected formatted price: "24.68"
        boolean priceFormattingCorrect = "24.68".equals(formattedPrice);
        Log.d(TAG, "Price formatting correct: " + priceFormattingCorrect);
        
        Log.d(TAG, "Bluetooth scale integration test completed");
    }
    
    /**
     * Test error handling
     */
    public void testErrorHandling() {
        Log.d(TAG, "Testing error handling");
        
        // Test getting last error when no error occurred
        String lastError = scaleManager.getLastError();
        Log.d(TAG, "Last error (should be null): " + lastError);
        
        // Test connection status
        boolean isConnected = scaleManager.isConnected();
        Log.d(TAG, "Is connected: " + isConnected);
        
        Log.d(TAG, "Error handling test completed");
    }
}