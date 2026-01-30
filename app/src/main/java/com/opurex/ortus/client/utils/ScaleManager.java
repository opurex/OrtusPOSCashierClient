package com.opurex.ortus.client.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.example.scaler.AclasScaler;
import com.opurex.ortus.client.models.Product;

public class ScaleManager {

    private static final String TAG = "ScaleManager";
    private Context context;
    private AclasScaler aclasScaler;
    private BluetoothAdapter bluetoothAdapter;
    private ScaleWeightListener scaleWeightListener;
    private ConnectionStateListener connectionStateListener;
    private ScanListener scanListener;
    private boolean isConnected = false;
    private String lastError = null;

    public interface ScaleWeightListener {
        void onWeightReceived(double weight, String unit);
    }

    public interface ConnectionStateListener {
        void onScaleConnected();
        void onScaleDisconnected();
        void onScaleError(String errorMessage);
    }

    public interface ScanListener {
        void onDeviceFound(String name, String mac, String signal);
        void onScanFinished();
    }

    public ScaleManager(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initializeAclasScaler();
    }

    /**
     * Initialize the AclasScaler for Bluetooth SPP communication
     */
    private void initializeAclasScaler() {
        try {
            if (aclasScaler == null) {
                aclasScaler = new AclasScaler(AclasScaler.Type_FSC, context, new AclasScaler.AclasScalerListener() {
                    @Override
                    public void onError(int errornum, String str) {
                        Log.e(TAG, "AclasScaler error: " + errornum + " - " + str);
                        lastError = "Error " + errornum + ": " + str;
                        if (connectionStateListener != null) {
                            connectionStateListener.onScaleError(lastError);
                        }
                    }

                    @Override
                    public void onDisConnected() {
                        Log.d(TAG, "Scale disconnected");
                        isConnected = false;
                        if (connectionStateListener != null) {
                            connectionStateListener.onScaleDisconnected();
                        }
                    }

                    @Override
                    public void onConnected() {
                        Log.d(TAG, "Scale connected");
                        isConnected = true;
                        if (connectionStateListener != null) {
                            connectionStateListener.onScaleConnected();
                        }
                    }

                    @Override
                    public void onRcvData(AclasScaler.WeightInfoNew info) {
                        Log.d(TAG, "Received weight data: " + info.toString());
                        if (scaleWeightListener != null) {
                            scaleWeightListener.onWeightReceived(info.netWeight, info.unit);
                        }
                    }

                    @Override
                    public void onUpdateProcess(int iIndex, int iTotal) {
                        // Progress update callback - can be ignored for basic functionality
                    }
                });
                
                // Set up Bluetooth listener for device discovery
                aclasScaler.setBluetoothListener(new AclasScaler.AclasBluetoothListener() {
                    @Override
                    public void onSearchBluetooth(String deviceInfo) {
                        Log.d(TAG, "Found Bluetooth device: " + deviceInfo);
                        if (scanListener != null && deviceInfo.contains(",")) {
                            String[] parts = deviceInfo.split(",");
                            if (parts.length >= 2) {
                                String name = parts[0];
                                String mac = parts[1];
                                String signal = parts.length >= 3 ? parts[2] : "0";
                                scanListener.onDeviceFound(name, mac, signal);
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
                });
                
                Log.d(TAG, "AclasScaler initialized successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize AclasScaler: " + e.getMessage(), e);
            lastError = "Initialization failed: " + e.getMessage();
        }
    }

    public void startScan() {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth not supported on this device");
            lastError = "Bluetooth not supported";
            if (scanListener != null) {
                scanListener.onScanFinished();
            }
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth is disabled");
            lastError = "Bluetooth disabled";
            if (scanListener != null) {
                scanListener.onScanFinished();
            }
            return;
        }

        // Start both native Android discovery and AclasScaler SPP discovery
        startNativeBluetoothDiscovery();
        startAclasScalerDiscovery();
    }

    /**
     * Start native Android Bluetooth discovery for all devices
     */
    private void startNativeBluetoothDiscovery() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            // Clear any previous discovery
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            
            // Register broadcast receiver for device discovery
            // Note: This would normally be done in an Activity context
            Log.d(TAG, "Starting native Android Bluetooth discovery");
            
            // Start discovery
            boolean discoveryStarted = bluetoothAdapter.startDiscovery();
            Log.d(TAG, "Native Bluetooth discovery started: " + discoveryStarted);
        }
    }

    /**
     * Start AclasScaler SPP-specific discovery
     */
    private void startAclasScalerDiscovery() {
        if (aclasScaler != null) {
            Log.d(TAG, "Starting AclasScaler SPP discovery");
            aclasScaler.startScanBluetooth(true);
        } else {
            Log.e(TAG, "AclasScaler not initialized, cannot start SPP discovery");
        }
    }


    public void setScaleWeightListener(ScaleWeightListener listener) {
        this.scaleWeightListener = listener;
    }

    public void setConnectionStateListener(ConnectionStateListener listener) {
        this.connectionStateListener = listener;
    }

    public void setScanListener(ScanListener listener) {
        this.scanListener = listener;
    }

//    public void cleanup() {
//        disconnect();
//        if (aclasScaler != null) {
//            // Clean up resources if needed
//            aclasScaler = null;
//        }
//    }

    /**
     * Initialize the scale and check if Bluetooth is supported
     */
//    public boolean initializeScale() {
//        return bluetoothAdapter != null;
//    }

    /**
     * Calculate the price based on product price and weight
     */
    public double calculatePrice(Product product, double weight) {
        if (product != null) {
            return product.getPrice() * weight;
        }
        return 0.0;
    }

    /**
     * Calculate and format the price as a string
     */
    public String calculatePriceString(Product product, double weight) {
        double price = calculatePrice(product, weight);
        // Format to 2 decimal places
        return String.format("%.2f", price);
    }

    /**
     * Connect to a specific scale device
     * @param macAddress MAC address of the scale
     * @return true if connection attempt started successfully
     */
    public boolean connectToScale(String macAddress) {
        if (aclasScaler == null) {
            Log.e(TAG, "AclasScaler not initialized");
            lastError = "Scale not initialized";
            return false;
        }

        if (macAddress == null || macAddress.isEmpty()) {
            Log.e(TAG, "Invalid MAC address");
            lastError = "Invalid MAC address";
            return false;
        }

        Log.d(TAG, "Attempting to connect to scale: " + macAddress);
        
        // Connect in a separate thread to avoid blocking the UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = aclasScaler.AclasConnect(macAddress);
                if (result != 0) {
                    Log.e(TAG, "Failed to connect to scale. Error code: " + result);
                    lastError = "Connection failed: " + result;
                    if (connectionStateListener != null) {
                        connectionStateListener.onScaleError(lastError);
                    }
                } else {
                    Log.d(TAG, "Successfully connected to scale");
                }
            }
        }).start();

        return true;
    }

    /**
     * Disconnect from the current scale
     */
    public void disconnect() {
        if (aclasScaler != null) {
            Log.d(TAG, "Disconnecting from scale");
            aclasScaler.AclasDisconnect();
            isConnected = false;
        }
    }

    /**
     * Check if scale is connected
     * @return true if connected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Zero the scale
     */
    public void zeroScale() {
        if (aclasScaler != null && isConnected) {
            Log.d(TAG, "Zeroing scale");
            aclasScaler.AclasZero();
        } else {
            Log.e(TAG, "Cannot zero scale - not connected or not initialized");
            lastError = "Not connected";
        }
    }

    /**
     * Tare the scale
     */
    public void tareScale() {
        if (aclasScaler != null && isConnected) {
            Log.d(TAG, "Taring scale");
            aclasScaler.AclasTare();
        } else {
            Log.e(TAG, "Cannot tare scale - not connected or not initialized");
            lastError = "Not connected";
        }
    }

    /**
     * Stop the current scan
     */
    public void stopScan() {
        if (aclasScaler != null) {
            Log.d(TAG, "Stopping Bluetooth scan");
            aclasScaler.startScanBluetooth(false);
        }
        
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            Log.d(TAG, "Stopping native Bluetooth discovery");
            bluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * Get the last error from the scale
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Get paired Bluetooth devices
     */
//    private java.util.Set<android.bluetooth.BluetoothDevice> getPairedDevices() {
//        if (bluetoothAdapter == null) {
//            return java.util.Collections.emptySet();
//        }
//        return bluetoothAdapter.getBondedDevices();
//    }

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

}
