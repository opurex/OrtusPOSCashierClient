package com.opurex.ortus.client.utils;

import android.content.Context;

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
}
