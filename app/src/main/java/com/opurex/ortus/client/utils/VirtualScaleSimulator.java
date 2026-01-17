package com.opurex.ortus.client.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.opurex.ortus.client.utils.BluetoothScaleHelper;

import java.util.Random;

/**
 * Virtual scale simulator for testing purposes
 * Simulates the behavior of an Aclas Bluetooth scale
 */
public class VirtualScaleSimulator {
    private static final String TAG = "VirtualScaleSimulator";
    
    private final Context context;
    private final BluetoothScaleHelper.ScaleDataListener scaleDataListener;
    private final BluetoothScaleHelper.ConnectionStateListener connectionStateListener;
    private final BluetoothScaleHelper.ScanListener scanListener;
    
    private boolean isConnected = false;
    private boolean isScanning = false;
    private Handler handler;
    private Random random;
    
    // Simulation parameters
    private double currentWeight = 0.0;
    private String currentUnit = "kg";
    private boolean isStable = true;
    
    public VirtualScaleSimulator(Context context, 
                                BluetoothScaleHelper.ScaleDataListener scaleDataListener,
                                BluetoothScaleHelper.ConnectionStateListener connectionStateListener,
                                BluetoothScaleHelper.ScanListener scanListener) {
        this.context = context.getApplicationContext();
        this.scaleDataListener = scaleDataListener;
        this.connectionStateListener = connectionStateListener;
        this.scanListener = scanListener;
        this.handler = new Handler(Looper.getMainLooper());
        this.random = new Random();
    }
    
    /**
     * Start scanning for virtual scale devices
     */
    public void startScan() {
        if (isScanning) {
            Log.w(TAG, "Already scanning");
            return;
        }
        
        isScanning = true;
        Log.d(TAG, "Starting virtual scale scan...");
        
        // Simulate finding a virtual Aclas scale device after a short delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScanning && scanListener != null) {
                    // Simulate finding a virtual Aclas scale device
                    String deviceName = "VirtualAclasScale";
                    String deviceMac = "AA:BB:CC:DD:EE:FF"; // Virtual MAC address
                    String signalStrength = "-60"; // Simulated signal strength
                    
                    Log.d(TAG, "Virtual scale device found: " + deviceName + " (" + deviceMac + ")");
                    scanListener.onDeviceFound(deviceName, deviceMac, signalStrength);
                    
                    // Finish scanning after finding the device
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (scanListener != null) {
                                Log.d(TAG, "Virtual scan finished");
                                scanListener.onScanFinished();
                                isScanning = false;
                            }
                        }
                    }, 1000);
                }
            }
        }, 1000); // Delay to simulate scanning time
    }
    
    /**
     * Stop scanning for virtual scale devices
     */
    public void stopScan() {
        isScanning = false;
        Log.d(TAG, "Stopped virtual scale scan");
    }
    
    /**
     * Connect to the virtual scale
     */
    public boolean connectToVirtualScale() {
        Log.d(TAG, "Connecting to virtual scale...");
        
        // Simulate connection delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnected = true;
                Log.d(TAG, "Connected to virtual scale");
                
                if (connectionStateListener != null) {
                    connectionStateListener.onConnected();
                }
                
                // Start sending simulated weight data
                startSendingWeightData();
            }
        }, 1500); // Simulate connection time
        
        return true;
    }
    
    /**
     * Disconnect from the virtual scale
     */
    public void disconnect() {
        isConnected = false;
        Log.d(TAG, "Disconnected from virtual scale");
        
        if (connectionStateListener != null) {
            connectionStateListener.onDisconnected();
        }
    }
    
    /**
     * Start sending simulated weight data
     */
    private void startSendingWeightData() {
        if (!isConnected) return;
        
        // Send initial weight
        sendWeightData(currentWeight, currentUnit);
        
        // Schedule periodic weight updates
        handler.postDelayed(weightUpdateRunnable, 1000); // Update every second
    }
    
    private Runnable weightUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isConnected) return;
            
            // Simulate slight variations in weight (realistic behavior)
            if (isStable) {
                // Small random fluctuations around current weight
                double fluctuation = (random.nextDouble() - 0.5) * 0.01; // ±0.005 variation
                currentWeight = Math.max(0, currentWeight + fluctuation);
            } else {
                // More significant variations when unstable
                double fluctuation = (random.nextDouble() - 0.5) * 0.1; // ±0.05 variation
                currentWeight = Math.max(0, currentWeight + fluctuation);
            }
            
            // Send the updated weight
            sendWeightData(currentWeight, currentUnit);
            
            // Continue sending updates
            handler.postDelayed(this, 1000);
        }
    };
    
    /**
     * Send weight data to the listener
     */
    private void sendWeightData(double weight, String unit) {
        if (scaleDataListener != null) {
            Log.d(TAG, "Sending weight data: " + weight + " " + unit);
            scaleDataListener.onWeightReceived(weight, unit);
        }
    }
    
    /**
     * Simulate adding weight to the scale
     */
    public void addWeight(double weightToAdd) {
        currentWeight += weightToAdd;
        Log.d(TAG, "Added weight: " + weightToAdd + ", Total: " + currentWeight);
        sendWeightData(currentWeight, currentUnit);
    }
    
    /**
     * Simulate removing weight from the scale
     */
    public void removeWeight(double weightToRemove) {
        currentWeight = Math.max(0, currentWeight - weightToRemove);
        Log.d(TAG, "Removed weight: " + weightToRemove + ", Total: " + currentWeight);
        sendWeightData(currentWeight, currentUnit);
    }
    
    /**
     * Simulate zeroing the scale
     */
    public void zeroScale() {
        currentWeight = 0.0;
        Log.d(TAG, "Scale zeroed");
        sendWeightData(currentWeight, currentUnit);
    }
    
    /**
     * Simulate taring the scale
     */
    public void tareScale() {
        // In a real scale, tare removes the current weight as the "tare weight"
        // For simulation, we'll just zero it
        Log.d(TAG, "Scale tared");
        zeroScale();
    }
    
    /**
     * Set the scale stability (to simulate stable/unstable readings)
     */
    public void setIsStable(boolean stable) {
        this.isStable = stable;
        Log.d(TAG, "Scale stability set to: " + stable);
    }
    
    /**
     * Get current connection status
     */
    public boolean isConnected() {
        return isConnected;
    }
    
    /**
     * Get current weight
     */
    public double getCurrentWeight() {
        return currentWeight;
    }
    
    /**
     * Get current unit
     */
    public String getCurrentUnit() {
        return currentUnit;
    }
}