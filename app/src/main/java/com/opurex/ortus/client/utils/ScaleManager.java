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

    }


    public void startScan() {
        if (aclasScaler != null) {
            aclasScaler.startScanBluetooth(true);
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
