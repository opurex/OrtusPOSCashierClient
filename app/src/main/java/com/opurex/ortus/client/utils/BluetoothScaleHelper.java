package com.opurex.ortus.client.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

import com.example.scaler.AclasScaler;

import java.util.Set;

public class BluetoothScaleHelper implements AclasScaler.AclasBluetoothListener {
    private static final String TAG = "BluetoothScaleHelper";

    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;
    private AclasScaler aclasScaler;
    private ScaleDataListener scaleDataListener;
    private ConnectionStateListener connectionStateListener;
    private ScanListener scanListener;
    private boolean isConnected = false;
    private String lastError = null;

    public interface ScaleDataListener {
        void onWeightReceived(double weight, String unit);
        void onPriceDataReceived(double price, double amount);
        void onError(String errorMessage);
    }

    public interface ConnectionStateListener {
        void onConnected();
        void onDisconnected();
        void onError(String errorMessage);
    }

    public interface ScanListener {
        void onDeviceFound(String name, String mac, String signal);
        void onScanFinished();
    }

    public BluetoothScaleHelper(Context context) {
        this.context = context.getApplicationContext();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize the Aclas scaler
        initAclasScaler();

        // Ensure the ACLAS scaler has this as the Bluetooth listener
        if (aclasScaler != null) {
            aclasScaler.setBluetoothListener(this);
        }
    }

    private void initAclasScaler() {
        try {
            aclasScaler = new AclasScaler(AclasScaler.Type_FSC, context, new AclasScaler.AclasScalerListener() {
                @Override
                public void onError(int errornum, String str) {
                    Log.e(TAG, "Aclas scaler error: " + errornum + ", " + str);
                    lastError = "Scale error: " + str + " (code: " + errornum + ")";
                    if (scaleDataListener != null) {
                        scaleDataListener.onError(lastError);
                    }
                    if (connectionStateListener != null) {
                        connectionStateListener.onError(lastError);
                    }
                }

                @Override
                public void onDisConnected() {
                    isConnected = false;
                    Log.d(TAG, "Aclas scaler disconnected callback received");
                    if (connectionStateListener != null) {
                        connectionStateListener.onDisconnected();
                    }
                }

                @Override
                public void onConnected() {
                    isConnected = true;
                    lastError = null;
                    Log.d(TAG, "Aclas scaler connected callback received");
                    if (connectionStateListener != null) {
                        connectionStateListener.onConnected();
                    }
                }

                @Override
                public void onRcvData(AclasScaler.WeightInfoNew info) {
                    // Handle weight data
                    if (scaleDataListener != null && info != null) {
                        // Log received weight data for debugging
                        Log.d(TAG, "Received weight data - Net Weight: " + info.netWeight +
                              ", Unit: " + info.unit);

                        // Convert weight to kg if needed
                        double weightInKg = info.netWeight;
                        String unit = info.unit != null ? info.unit : "kg";
                        scaleDataListener.onWeightReceived(weightInKg, unit);
                    } else {
                        Log.w(TAG, "Received weight data but listener is null or info is null");
                    }
                }

                @Override
                public void onUpdateProcess(int iIndex, int iTotal) {
                    // Not used in this implementation
                }
            });

            // Set up price/amount listener
            aclasScaler.setAclasPSXListener(new AclasScaler.AclasScalerPSXListener() {
                @Override
                public void onRcvData(com.example.data.St_PSData st_psData) {
                    if (scaleDataListener != null && st_psData != null) {
                        scaleDataListener.onPriceDataReceived(st_psData.dPrice, st_psData.dAmount);
                    }
                }
            });

            // Set up Bluetooth scanning listener
            aclasScaler.setBluetoothListener(this);

        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Aclas scaler", e);
            lastError = "Failed to initialize scale: " + e.getMessage();
        }
    }

    // --- Scan Methods ---
    public void startScan() {
        // Start foreground service for Bluetooth operations
        startForegroundService();

        if (aclasScaler != null) {
            aclasScaler.startScanBluetooth(true);
        }
    }

    public void stopScan() {
        if (aclasScaler != null) {
            aclasScaler.startScanBluetooth(false);
        }
        // Stop foreground service when scan stops
        stopForegroundService();
    }

    @Override
    public void onSearchBluetooth(String deviceInfo) {
        Log.d(TAG, "Found Bluetooth device: " + deviceInfo);
        if (deviceInfo != null && deviceInfo.contains(",") && scanListener != null) {
            String[] parts = deviceInfo.split(",");
            // According to ACLAS SDK, format is "name,mac,signal" - but sometimes signal might not be present
            if (parts.length >= 2) {
                String name = parts[0];
                String mac = parts[1];
                String signal = parts.length >= 3 ? parts[2] : "Unknown";

                Log.d(TAG, "Parsed device - Name: " + name + ", MAC: " + mac + ", Signal: " + signal);
                scanListener.onDeviceFound(name, mac, signal);
            } else {
                Log.w(TAG, "Invalid device info format: " + deviceInfo + " (expected at least 'name,mac')");
            }
        }
    }

    @Override
    public void onSearchFinish() {
        Log.d(TAG, "Bluetooth scan finished");
        if (scanListener != null) {
            scanListener.onScanFinished();
        }
    }
    // --------------------

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    public Set<android.bluetooth.BluetoothDevice> getPairedDevices() {
        if (bluetoothAdapter == null) {
            return java.util.Collections.emptySet();
        }

        // Check BLUETOOTH_CONNECT permission for getting paired devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "BLUETOOTH_CONNECT permission not granted, cannot get paired devices");
                lastError = "Bluetooth permission not granted";
                return java.util.Collections.emptySet();
            }
        }

        try {
            return bluetoothAdapter.getBondedDevices();
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException when getting paired devices", e);
            lastError = "Security error when accessing paired devices: " + e.getMessage();
            return java.util.Collections.emptySet();
        }
    }

    public boolean connectToScale(String macAddress) {
        Log.d(TAG, "Attempting to connect to scale with MAC: " + macAddress);

        if (aclasScaler == null) {
            Log.e(TAG, "Aclas scaler not initialized");
            lastError = "Scale not initialized";
            return false;
        }

        try {
            // According to ACLAS demo app implementation:
            // For already paired devices, pass empty string
            // For new connections to unpaired devices, pass the MAC address
            String connectionParam = "";

            // Check if device is already paired
            Set<android.bluetooth.BluetoothDevice> pairedDevices = getPairedDevices();
            Log.d(TAG, "Checking paired devices, found: " + pairedDevices.size());

            boolean isAlreadyPaired = false;
            if (macAddress != null && !macAddress.isEmpty()) {
                for (android.bluetooth.BluetoothDevice device : pairedDevices) {
                    Log.d(TAG, "Paired device: " + device.getName() + " - " + device.getAddress());
                    if (device.getAddress().equalsIgnoreCase(macAddress)) {
                        isAlreadyPaired = true;
                        Log.d(TAG, "Device is already paired, will use empty string for connection");
                        break;
                    }
                }
            }

            // For already paired devices, pass empty string for reconnection
            // For new connections to unpaired devices, pass the MAC address
            connectionParam = isAlreadyPaired || (macAddress == null || macAddress.isEmpty()) ? "" : macAddress;

            Log.d(TAG, "Calling AclasConnect with param: '" + connectionParam + "'");
            int result = aclasScaler.AclasConnect(connectionParam);
            Log.d(TAG, "AclasConnect returned: " + result);

            if (result == 0) {
                isConnected = true;
                lastError = null;
                Log.d(TAG, "Successfully connected to scale with result: " + result);
                return true;
            } else {
                Log.e(TAG, "Failed to connect to scale, error code: " + result);
                lastError = "Failed to connect to scale: error code " + result;
                if (connectionStateListener != null) {
                    connectionStateListener.onError(lastError);
                }
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while connecting to scale", e);
            lastError = "Exception while connecting to scale: " + e.getMessage();
            if (connectionStateListener != null) {
                connectionStateListener.onError(lastError);
            }
            return false;
        }
    }

    public void disconnect() {
        if (aclasScaler != null) {
            try {
                aclasScaler.AclasDisconnect();
                isConnected = false;
                lastError = null;
                Log.d(TAG, "Disconnected from scale");
            } catch (Exception e) {
                Log.e(TAG, "Exception while disconnecting from scale", e);
                lastError = "Exception while disconnecting from scale: " + e.getMessage();
            }
        }
    }

    /**
     * Request weight data from the scale after connection
     */
    public void requestWeightData() {
        Log.d(TAG, "Requesting weight data from scale");
        // Some scales may require a command to start sending weight data
        // This is a placeholder - the actual implementation depends on the specific scale model
        // The ACLAS PSX scale should send weight data continuously once connected
        // but we'll add this method for completeness
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getLastError() {
        return lastError;
    }

    public void setScaleDataListener(ScaleDataListener listener) {
        this.scaleDataListener = listener;
    }

    public void setConnectionStateListener(ConnectionStateListener listener) {
        this.connectionStateListener = listener;
    }

    public void setScanListener(ScanListener listener) {
        this.scanListener = listener;
        // Also propagate the listener to the ACLAS scaler if it exists
        if (aclasScaler != null) {
            aclasScaler.setBluetoothListener(this);
        }
    }

    public void cleanup() {
        disconnect();
        stopForegroundService();
        if (aclasScaler != null) {
            // Clean up resources if needed
            aclasScaler = null;
        }
    }

    /**
     * Start the foreground service for Bluetooth operations
     */
    public void startForegroundService() {
        try {
            Intent serviceIntent = new Intent(context, com.opurex.ortus.client.services.BluetoothForegroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error starting foreground service", e);
        }
    }

    /**
     * Stop the foreground service
     */
    public void stopForegroundService() {
        try {
            Intent serviceIntent = new Intent(context, com.opurex.ortus.client.services.BluetoothForegroundService.class);
            context.stopService(serviceIntent);
        } catch (Exception e) {
            Log.e(TAG, "Error stopping foreground service", e);
        }
    }

    // Getters for the listeners to support virtual scale testing
    public ScaleDataListener getScaleDataListener() {
        return scaleDataListener;
    }

    public ConnectionStateListener getConnectionStateListener() {
        return connectionStateListener;
    }

    public ScanListener getScanListener() {
        return scanListener;
    }

    // Methods for scale operations
    public void zeroScale() {
        if (aclasScaler != null && isConnected) {
            try {
                aclasScaler.AclasZero();
            } catch (Exception e) {
                Log.e(TAG, "Exception while zeroing scale", e);
                lastError = "Exception while zeroing scale: " + e.getMessage();
                if (scaleDataListener != null) {
                    scaleDataListener.onError(lastError);
                }
            }
        } else {
            lastError = "Cannot zero scale: not connected";
            if (scaleDataListener != null) {
                scaleDataListener.onError(lastError);
            }
        }
    }

    public void tareScale() {
        if (aclasScaler != null && isConnected) {
            try {
                aclasScaler.AclasTare();
            } catch (Exception e) {
                Log.e(TAG, "Exception while taring scale", e);
                lastError = "Exception while taring scale: " + e.getMessage();
                if (scaleDataListener != null) {
                    scaleDataListener.onError(lastError);
                }
            }
        } else {
            lastError = "Cannot tare scale: not connected";
            if (scaleDataListener != null) {
                scaleDataListener.onError(lastError);
            }
        }
    }
}