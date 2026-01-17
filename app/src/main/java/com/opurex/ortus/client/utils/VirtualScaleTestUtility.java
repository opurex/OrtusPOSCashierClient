package com.opurex.ortus.client.utils;

import android.content.Context;
import android.util.Log;

/**
 * Test utility for virtual scale simulation
 * Allows testing of the scale integration without a physical device
 */
public class VirtualScaleTestUtility {
    private static final String TAG = "VirtualScaleTestUtility";
    
    private VirtualScaleSimulator virtualScale;
    private Context context;
    
    public VirtualScaleTestUtility(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Initialize the virtual scale simulator
     */
    public void initializeVirtualScale(BluetoothScaleHelper bluetoothScaleHelper) {
        Log.d(TAG, "Initializing virtual scale simulator");
        
        // Create the virtual scale simulator with the same listeners as the real one
        virtualScale = new VirtualScaleSimulator(
            context,
            bluetoothScaleHelper.getScaleDataListener(),
            bluetoothScaleHelper.getConnectionStateListener(),
            bluetoothScaleHelper.getScanListener()
        );
        
        Log.d(TAG, "Virtual scale simulator initialized");
    }
    
    /**
     * Start scanning using the virtual scale
     */
    public void startVirtualScan() {
        if (virtualScale != null) {
            Log.d(TAG, "Starting virtual scan");
            virtualScale.startScan();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Connect to the virtual scale
     */
    public boolean connectToVirtualScale() {
        if (virtualScale != null) {
            Log.d(TAG, "Connecting to virtual scale");
            return virtualScale.connectToVirtualScale();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
            return false;
        }
    }
    
    /**
     * Disconnect from the virtual scale
     */
    public void disconnectVirtualScale() {
        if (virtualScale != null) {
            Log.d(TAG, "Disconnecting from virtual scale");
            virtualScale.disconnect();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Add weight to the virtual scale (for testing)
     */
    public void addWeightToVirtualScale(double weight) {
        if (virtualScale != null) {
            Log.d(TAG, "Adding " + weight + "kg to virtual scale");
            virtualScale.addWeight(weight);
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Remove weight from the virtual scale (for testing)
     */
    public void removeWeightFromVirtualScale(double weight) {
        if (virtualScale != null) {
            Log.d(TAG, "Removing " + weight + "kg from virtual scale");
            virtualScale.removeWeight(weight);
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Zero the virtual scale
     */
    public void zeroVirtualScale() {
        if (virtualScale != null) {
            Log.d(TAG, "Zeroing virtual scale");
            virtualScale.zeroScale();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Tare the virtual scale
     */
    public void tareVirtualScale() {
        if (virtualScale != null) {
            Log.d(TAG, "Taring virtual scale");
            virtualScale.tareScale();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
    
    /**
     * Check if virtual scale is connected
     */
    public boolean isVirtualScaleConnected() {
        if (virtualScale != null) {
            return virtualScale.isConnected();
        }
        return false;
    }
    
    /**
     * Get current net weight from virtual scale
     */
    public double getVirtualScaleNetWeight() {
        if (virtualScale != null) {
            return virtualScale.getCurrentNetWeight();
        }
        return 0.0;
    }

    /**
     * Get current tare weight from virtual scale
     */
    public double getVirtualScaleTareWeight() {
        if (virtualScale != null) {
            return virtualScale.getCurrentTareWeight();
        }
        return 0.0;
    }
    
    /**
     * Check if virtual scale is available
     */
    public boolean isVirtualScaleAvailable() {
        return virtualScale != null;
    }

    /**
     * Add tare weight to virtual scale
     */
    public void addTareWeightToVirtualScale(double tareWeight) {
        if (virtualScale != null) {
            Log.d(TAG, "Adding " + tareWeight + "kg to virtual scale tare weight");
            virtualScale.addTareWeight(tareWeight);
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }

    /**
     * Reset tare weight on virtual scale
     */
    public void resetTareWeightOnVirtualScale() {
        if (virtualScale != null) {
            Log.d(TAG, "Resetting virtual scale tare weight");
            virtualScale.resetTare();
        } else {
            Log.e(TAG, "Virtual scale not initialized");
        }
    }
}