package com.opurex.ortus.client.utils;

import android.content.Context;

import com.opurex.ortus.client.models.Product;

public class ScaleManager {

    private BluetoothScaleHelper bluetoothScaleHelper;
    private ScaleWeightListener scaleWeightListener;
    private ConnectionStateListener connectionStateListener;
    private ScanListener scanListener;

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
        this.bluetoothScaleHelper = new BluetoothScaleHelper(context);
        this.bluetoothScaleHelper.setScaleDataListener(new BluetoothScaleHelper.ScaleDataListener() {
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
                if (connectionStateListener != null) {
                    connectionStateListener.onScaleError(errorMessage);
                }
            }
        });

        this.bluetoothScaleHelper.setConnectionStateListener(new BluetoothScaleHelper.ConnectionStateListener() {
            @Override
            public void onConnected() {
                if (connectionStateListener != null) {
                    connectionStateListener.onScaleConnected();
                }
            }

            @Override
            public void onDisconnected() {
                if (connectionStateListener != null) {
                    connectionStateListener.onScaleDisconnected();
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (connectionStateListener != null) {
                    connectionStateListener.onScaleError(errorMessage);
                }
            }
        });

        this.bluetoothScaleHelper.setScanListener(new BluetoothScaleHelper.ScanListener() {
            @Override
            public void onDeviceFound(String name, String mac, String signal) {
                if (scanListener != null) {
                    scanListener.onDeviceFound(name, mac, signal);
                }
            }

            @Override
            public void onScanFinished() {
                if (scanListener != null) {
                    scanListener.onScanFinished();
                }
            }
        });
    }

    public void startScan() {
        bluetoothScaleHelper.startScan();
    }

    public void stopScan() {
        bluetoothScaleHelper.stopScan();
    }

    public boolean connectToScale(String macAddress) {
        return bluetoothScaleHelper.connectToScale(macAddress);
    }

    public void disconnect() {
        bluetoothScaleHelper.disconnect();
    }

    public boolean isConnected() {
        return bluetoothScaleHelper.isConnected();
    }

    public void zeroScale() {
        bluetoothScaleHelper.zeroScale();
    }

    public void tareScale() {
        bluetoothScaleHelper.tareScale();
    }

    public void setScaleWeightListener(ScaleWeightListener listener) {
        this.scaleWeightListener = listener;
    }

    public void setConnectionStateListener(ConnectionStateListener listener) {
        this.connectionStateListener = listener;
    }

    public void setScanListener(ScanListener listener) {
        this.scanListener = listener;
        // Propagate the listener down to the helper
        if (bluetoothScaleHelper != null) {
            if (listener != null) {
                // Wrap the ScaleManager.ScanListener in a BluetoothScaleHelper.ScanListener
                bluetoothScaleHelper.setScanListener(new BluetoothScaleHelper.ScanListener() {
                    @Override
                    public void onDeviceFound(String name, String mac, String signal) {
                        if (scanListener != null) {
                            scanListener.onDeviceFound(name, mac, signal);
                        }
                    }

                    @Override
                    public void onScanFinished() {
                        if (scanListener != null) {
                            scanListener.onScanFinished();
                        }
                    }
                });
            } else {
                bluetoothScaleHelper.setScanListener(null);
            }
        }
    }

    public void cleanup() {
        bluetoothScaleHelper.cleanup();
    }

    /**
     * Initialize the scale and check if Bluetooth is supported
     */
    public boolean initializeScale() {
        return bluetoothScaleHelper.isBluetoothSupported();
    }

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
     * Get the last error from the Bluetooth scale helper
     */
    public String getLastError() {
        return bluetoothScaleHelper.getLastError();
    }

    // Virtual scale testing support
    private VirtualScaleTestUtility virtualScaleTestUtility;
    private boolean useVirtualScale = false;

    /**
     * Initialize virtual scale testing utility
     */
    public void initializeVirtualScaleTesting() {
        virtualScaleTestUtility = new VirtualScaleTestUtility(context);
        virtualScaleTestUtility.initializeVirtualScale(bluetoothScaleHelper);
        useVirtualScale = true;
    }

    /**
     * Start virtual scale scanning
     */
    public void startVirtualScaleScan() {
        if (virtualScaleTestUtility != null) {
            virtualScaleTestUtility.startVirtualScan();
        }
    }

    /**
     * Connect to virtual scale
     */
    public boolean connectToVirtualScale() {
        if (virtualScaleTestUtility != null) {
            return virtualScaleTestUtility.connectToVirtualScale();
        }
        return false;
    }

    /**
     * Add weight to virtual scale for testing
     */
    public void addWeightToVirtualScale(double weight) {
        if (virtualScaleTestUtility != null) {
            virtualScaleTestUtility.addWeightToVirtualScale(weight);
        }
    }

    /**
     * Zero the virtual scale
     */
    public void zeroVirtualScale() {
        if (virtualScaleTestUtility != null) {
            virtualScaleTestUtility.zeroVirtualScale();
        }
    }

    /**
     * Tare the virtual scale
     */
    public void tareVirtualScale() {
        if (virtualScaleTestUtility != null) {
            virtualScaleTestUtility.tareVirtualScale();
        }
    }

    /**
     * Check if using virtual scale
     */
    public boolean isUsingVirtualScale() {
        return useVirtualScale;
    }
}
