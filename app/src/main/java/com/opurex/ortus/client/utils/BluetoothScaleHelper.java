package com.opurex.ortus.client.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

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
                }

                @Override
                public void onDisConnected() {
                    isConnected = false;
                    Log.d(TAG, "Aclas scaler disconnected");
                    if (connectionStateListener != null) {
                        connectionStateListener.onDisconnected();
                    }
                }

                @Override
                public void onConnected() {
                    isConnected = true;
                    lastError = null;
                    Log.d(TAG, "Aclas scaler connected");
                    if (connectionStateListener != null) {
                        connectionStateListener.onConnected();
                    }
                }

                @Override
                public void onRcvData(AclasScaler.WeightInfoNew info) {
                    // Handle weight data
                    if (scaleDataListener != null && info != null) {
                        // Convert weight to kg if needed
                        double weightInKg = info.netWeight;
                        String unit = info.unit != null ? info.unit : "kg";
                        scaleDataListener.onWeightReceived(weightInKg, unit);
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
        if (aclasScaler != null) {
            aclasScaler.startScanBluetooth(true);
        }
    }

    public void stopScan() {
        if (aclasScaler != null) {
            aclasScaler.startScanBluetooth(false);
        }
    }

    @Override
    public void onSearchBluetooth(String deviceInfo) {
        if (deviceInfo != null && deviceInfo.contains(",") && scanListener != null) {
            String[] parts = deviceInfo.split(",");
            scanListener.onDeviceFound(parts[0], parts[1], parts[2]);
        }
    }

    @Override
    public void onSearchFinish() {
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
        if (aclasScaler == null) {
            Log.e(TAG, "Aclas scaler not initialized");
            lastError = "Scale not initialized";
            return false;
        }

        try {
            // For already paired devices, pass empty string
            // For new devices, pass the MAC address
            int result = aclasScaler.AclasConnect(macAddress != null ? macAddress : "");
            if (result == 0) {
                isConnected = true;
                lastError = null;
                Log.d(TAG, "Successfully connected to scale");
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

    public void cleanup() {
        disconnect();
        if (aclasScaler != null) {
            // Clean up resources if needed
            aclasScaler = null;
        }
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