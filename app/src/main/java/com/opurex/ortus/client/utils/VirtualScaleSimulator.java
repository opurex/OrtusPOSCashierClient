package com.opurex.ortus.client.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.util.Random;

/**
 * Virtual scale simulator for testing purposes
 * Simulates the behavior of an Aclas Bluetooth scale
 */
public class VirtualScaleSimulator {
    private static final String TAG = "VirtualScaleSimulator";
    
    private final Context context;

    
    private boolean isConnected = false;
    private boolean isScanning = false;
    private Handler handler;
    private Random random;
    
    // Simulation parameters
    private double currentNetWeight = 0.0;  // Actual weight on the scale
    private double currentTareWeight = 0.0; // Tare weight (container weight)
    private String currentUnit = "kg";
    private boolean isStable = true;
    
    public VirtualScaleSimulator(Context context) {
        this.context = context.getApplicationContext();

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
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isScanning && scanListener != null) {
//                    // Simulate finding a virtual Aclas scale device
//                    String deviceName = "VirtualAclasScale";
//                    String deviceMac = "AA:BB:CC:DD:EE:FF"; // Virtual MAC address
//                    String signalStrength = "-60"; // Simulated signal strength
//
//                    Log.d(TAG, "Virtual scale device found: " + deviceName + " (" + deviceMac + ")");
//                    scanListener.onDeviceFound(deviceName, deviceMac, signalStrength);
//
//                    // Finish scanning after finding the device
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (scanListener != null) {
//                                Log.d(TAG, "Virtual scan finished");
//                                scanListener.onScanFinished();
//                                isScanning = false;
//                            }
//                        }
//                    }, 1000);
//                }
//            }
//        }, 1000); // Delay to simulate scanning time
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
//    public boolean connectToVirtualScale() {
//        Log.d(TAG, "Connecting to virtual scale...");
//
//        // Simulate connection delay
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isConnected = true;
//                Log.d(TAG, "Connected to virtual scale");
//
//                if (connectionStateListener != null) {
//                    connectionStateListener.onConnected();
//                }
//
//                // Start sending simulated weight data
//                startSendingWeightData();
//            }
//        }, 1500); // Simulate connection time
//
//        return true;
//    }
//
//    /**
//     * Disconnect from the virtual scale
//     */
//    public void disconnect() {
//        isConnected = false;
//        Log.d(TAG, "Disconnected from virtual scale");
//
//        if (connectionStateListener != null) {
//            connectionStateListener.onDisconnected();
//        }
//    }
//
//    /**
//     * Start sending simulated weight data
//     */
//    private void startSendingWeightData() {
//        if (!isConnected) return;
//
//        // Send initial weight
//        sendWeightData(currentNetWeight, currentUnit);
//
//        // Schedule periodic weight updates
//        handler.postDelayed(weightUpdateRunnable, 1000); // Update every second
//    }

//    private Runnable weightUpdateRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (!isConnected) return;
//
//            // Simulate slight variations in weight (realistic behavior)
//            if (isStable) {
//                // Small random fluctuations around current weight
//                double fluctuation = (random.nextDouble() - 0.5) * 0.01; // ±0.005 variation
//                currentNetWeight = Math.max(0, currentNetWeight + fluctuation);
//            } else {
//                // More significant variations when unstable
//                double fluctuation = (random.nextDouble() - 0.5) * 0.1; // ±0.05 variation
//                currentNetWeight = Math.max(0, currentNetWeight + fluctuation);
//            }
//
//            // Send the updated weight
//            sendWeightData(currentNetWeight, currentUnit);
//
//            // Continue sending updates
//            handler.postDelayed(this, 1000);
//        }
//    };
//
//    /**
//     * Send weight data to the listener
//     */
//    private void sendWeightData(double weight, String unit) {
//        if (scaleDataListener != null) {
//            Log.d(TAG, "Sending weight data: Net=" + weight + " Tare=" + currentTareWeight + " " + unit);
//            scaleDataListener.onWeightReceived(weight, unit);
//        }
//    }
//
//    /**
//     * Send detailed weight data including tare weight (simulated WeightInfoNew)
//     */
//    public void sendDetailedWeightData() {
//        if (scaleDataListener != null) {
//            // Simulate the WeightInfoNew object behavior
//            Log.d(TAG, "Sending detailed weight data: Net=" + currentNetWeight + " Tare=" + currentTareWeight + " " + currentUnit);
//            scaleDataListener.onWeightReceived(currentNetWeight, currentUnit);
//        }
//    }
    
    /**
     * Simulate adding weight to the scale
     */
//    public void addWeight(double weightToAdd) {
//        currentNetWeight += weightToAdd;
//        Log.d(TAG, "Added weight: " + weightToAdd + ", Net Total: " + currentNetWeight);
//        sendWeightData(currentNetWeight, currentUnit);
//    }
//
//    /**
//     * Simulate removing weight from the scale
//     */
//    public void removeWeight(double weightToRemove) {
//        currentNetWeight = Math.max(0, currentNetWeight - weightToRemove);
//        Log.d(TAG, "Removed weight: " + weightToRemove + ", Net Total: " + currentNetWeight);
//        sendWeightData(currentNetWeight, currentUnit);
//    }
//
//    /**
//     * Simulate zeroing the scale
//     */
//    public void zeroScale() {
//        currentNetWeight = 0.0;
//        Log.d(TAG, "Scale zeroed");
//        sendWeightData(currentNetWeight, currentUnit);
//    }
    
    /**
     * Simulate taring the scale
     */
//    public void tareScale() {
//        // In a real scale, tare sets the current weight as the "tare weight" (container weight)
//        // Future weight readings will subtract this tare weight
//        currentTareWeight = currentNetWeight;  // Set current net weight as tare weight
//        currentNetWeight = 0.0;  // Net weight becomes 0 after taring
//        Log.d(TAG, "Scale tared. Tare weight set to: " + currentTareWeight);
//        sendWeightData(currentNetWeight, currentUnit);
//    }
//
//    /**
//     * Simulate zeroing the scale
//     */
//    public void zeroScale() {
//        // Zeroing resets the current net weight to 0, but keeps tare weight if any
//        currentNetWeight = 0.0;
//        Log.d(TAG, "Scale zeroed. Current net weight: " + currentNetWeight + ", Tare weight: " + currentTareWeight);
//        sendWeightData(currentNetWeight, currentUnit);
//    }

    /**
     * Add weight to the tare (container) weight
     */
    public void addTareWeight(double tareToAdd) {
        currentTareWeight += tareToAdd;
        Log.d(TAG, "Added tare weight: " + tareToAdd + ", Tare Total: " + currentTareWeight);
    }

    /**
     * Reset tare weight to zero
     */
    public void resetTare() {
        currentTareWeight = 0.0;
        Log.d(TAG, "Tare weight reset to zero");
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
     * Get current net weight
     */
    public double getCurrentNetWeight() {
        return currentNetWeight;
    }

    /**
     * Get current tare weight
     */
    public double getCurrentTareWeight() {
        return currentTareWeight;
    }
    
    /**
     * Get current unit
     */
    public String getCurrentUnit() {
        return currentUnit;
    }
}