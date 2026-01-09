package com.opurex.ortus.client.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.opurex.ortus.client.Configure;
import com.opurex.ortus.client.models.Product;

public class ScaleManager {
    private static final String TAG = "ScaleManager";
    
    private final Context context;
    private BluetoothScaleHelper bluetoothScaleHelper;
    private ScaleWeightListener scaleWeightListener;
    private boolean isConnected = false;
    private String lastError = null;
    
    public interface ScaleWeightListener {
        void onWeightReceived(double weight, String unit);
        void onScaleConnected();
        void onScaleDisconnected();
        void onScaleError(String errorMessage);
    }
    
    public ScaleManager(Context context) {
        this.context = context.getApplicationContext();
    }
    
    public boolean initializeScale() {
        String scaleAddress = Configure.getScaleAddress(context);
        if (scaleAddress == null || scaleAddress.isEmpty()) {
            Log.d(TAG, "No scale address configured");
            lastError = "No scale address configured";
            return false;
        }
        
        if (bluetoothScaleHelper == null) {
            bluetoothScaleHelper = new BluetoothScaleHelper(context);
        }
        
        if (!bluetoothScaleHelper.isBluetoothSupported()) {
            Log.e(TAG, "Bluetooth not supported on this device");
            lastError = "Bluetooth not supported on this device";
            return false;
        }
        
        if (!bluetoothScaleHelper.isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            lastError = "Bluetooth is not enabled. Please enable Bluetooth in device settings.";
            return false;
        }
        
        // Set up listeners
        bluetoothScaleHelper.setScaleDataListener(new BluetoothScaleHelper.ScaleDataListener() {
            @Override
            public void onWeightReceived(double weight, String unit) {
                if (scaleWeightListener != null) {
                    scaleWeightListener.onWeightReceived(weight, unit);
                }
            }
            
            @Override
            public void onPriceDataReceived(double price, double amount) {
                // Not used in this implementation
            }
            
            @Override
            public void onError(String errorMessage) {
                lastError = errorMessage;
                if (scaleWeightListener != null) {
                    scaleWeightListener.onScaleError(errorMessage);
                }
            }
        });
        
        bluetoothScaleHelper.setConnectionStateListener(new BluetoothScaleHelper.ConnectionStateListener() {
            @Override
            public void onConnected() {
                isConnected = true;
                lastError = null;
                if (scaleWeightListener != null) {
                    scaleWeightListener.onScaleConnected();
                }
            }
            
            @Override
            public void onDisconnected() {
                isConnected = false;
                if (scaleWeightListener != null) {
                    scaleWeightListener.onScaleDisconnected();
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                lastError = errorMessage;
                if (scaleWeightListener != null) {
                    scaleWeightListener.onScaleError(errorMessage);
                }
            }
        });
        
        // Connect to the scale
        boolean connected = bluetoothScaleHelper.connectToScale(scaleAddress);
        if (connected) {
            Log.d(TAG, "Successfully connected to scale");
        } else {
            Log.e(TAG, "Failed to connect to scale: " + bluetoothScaleHelper.getLastError());
            lastError = bluetoothScaleHelper.getLastError();
        }
        
        return connected;
    }
    
    public void disconnectScale() {
        if (bluetoothScaleHelper != null) {
            bluetoothScaleHelper.disconnect();
            isConnected = false;
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public String getLastError() {
        if (bluetoothScaleHelper != null) {
            String helperError = bluetoothScaleHelper.getLastError();
            if (helperError != null) {
                return helperError;
            }
        }
        return lastError;
    }
    
    public void setScaleWeightListener(ScaleWeightListener listener) {
        this.scaleWeightListener = listener;
    }
    
    public void cleanup() {
        if (bluetoothScaleHelper != null) {
            bluetoothScaleHelper.cleanup();
            bluetoothScaleHelper = null;
        }
        isConnected = false;
        lastError = null;
    }
    
    public void zeroScale() {
        if (bluetoothScaleHelper != null && isConnected) {
            bluetoothScaleHelper.zeroScale();
        } else {
            String error = "Cannot zero scale: not connected";
            lastError = error;
            if (scaleWeightListener != null) {
                scaleWeightListener.onScaleError(error);
            }
        }
    }
    
    public void tareScale() {
        if (bluetoothScaleHelper != null && isConnected) {
            bluetoothScaleHelper.tareScale();
        } else {
            String error = "Cannot tare scale: not connected";
            lastError = error;
            if (scaleWeightListener != null) {
                scaleWeightListener.onScaleError(error);
            }
        }
    }
    
    /**
     * Calculate the price for a scaled product based on weight
     * @param product The scaled product
     * @param weight The weight in kg
     * @return The calculated price
     */
    public double calculatePrice(Product product, double weight) {
        if (product == null || !product.isScaled()) {
            return 0.0;
        }
        
        // Price is per kg, so multiply by weight
        return product.getPrice() * weight;
    }
    
    /**
     * Calculate the price for a scaled product based on weight and format it as a string
     * @param product The scaled product
     * @param weight The weight in kg
     * @return The formatted price string
     */
    public String calculatePriceString(Product product, double weight) {
        double price = calculatePrice(product, weight);
        return String.format("%.2f", price);
    }
}