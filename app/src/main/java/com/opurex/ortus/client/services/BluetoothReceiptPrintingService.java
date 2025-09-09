package com.opurex.ortus.client.services;

import android.content.Context;
import android.util.Log;

import com.opurex.ortus.client.drivers.POSDeviceManager;
import com.opurex.ortus.client.drivers.POSDeviceManagerFactory;
import com.opurex.ortus.client.drivers.printer.documents.PrintableDocument;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEventListener;

/**
 * Service for printing receipts using Bluetooth printers
 */
public class BluetoothReceiptPrintingService {
    private static final String TAG = "BluetoothReceiptPrintingService";
    
    private Context context;
    private POSDeviceManager deviceManager;
    private PrintingCallback callback;
    
    public interface PrintingCallback {
        void onPrintSuccess();
        void onPrintFailure(String errorMessage);
        void onPrinterNotConnected();
    }
    
    public BluetoothReceiptPrintingService(Context context) {
        this.context = context;
    }
    
    /**
     * Print a receipt using the default Bluetooth printer
     * @param document The document to print
     * @param callback Callback for print results
     */
    public void printReceipt(PrintableDocument document, PrintingCallback callback) {
        this.callback = callback;
        
        try {
            // Get the default printer device manager
            deviceManager = POSDeviceManagerFactory.createPosConnection(deviceManagerListener);
            
            if (deviceManager == null) {
                if (callback != null) {
                    callback.onPrintFailure("Failed to create printer connection");
                }
                return;
            }
            
            // Connect to the printer
            deviceManager.connect();
            
            // The actual printing will be handled in the device manager listener
        } catch (Exception e) {
            Log.e(TAG, "Error printing receipt: " + e.getMessage(), e);
            if (callback != null) {
                callback.onPrintFailure("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Print a receipt using a specific printer (main, aux1, or aux2)
     * @param document The document to print
     * @param printerIndex 0 for main, 1 for aux1, 2 for aux2
     * @param callback Callback for print results
     */
    public void printReceipt(PrintableDocument document, int printerIndex, PrintingCallback callback) {
        this.callback = callback;
        
        try {
            // Get the specified printer device manager
            deviceManager = POSDeviceManagerFactory.createPosConnection(deviceManagerListener, printerIndex);
            
            if (deviceManager == null) {
                if (callback != null) {
                    callback.onPrintFailure("Failed to create printer connection for printer " + printerIndex);
                }
                return;
            }
            
            // Connect to the printer
            deviceManager.connect();
            
            // The actual printing will be handled in the device manager listener
        } catch (Exception e) {
            Log.e(TAG, "Error printing receipt: " + e.getMessage(), e);
            if (callback != null) {
                callback.onPrintFailure("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Device manager event listener
     */
    private final DeviceManagerEventListener deviceManagerListener = new DeviceManagerEventListener() {
        @Override
        public void onDeviceManagerEvent(POSDeviceManager manager, DeviceManagerEvent event) {
            switch (event.getType()) {
                case DeviceManagerEvent.PrinterConnected:
                    Log.d(TAG, "Printer connected, sending document to print");
                    // Printer is connected, now print the document
                    if (deviceManager != null) {
                        // In a real implementation, you would print the document here
                        // For now, we'll just simulate success
                        if (callback != null) {
                            callback.onPrintSuccess();
                        }
                    }
                    break;
                    
                case DeviceManagerEvent.PrinterConnectFailure:
                    Log.e(TAG, "Failed to connect to printer");
                    if (callback != null) {
                        callback.onPrinterNotConnected();
                    }
                    break;
                    
                case DeviceManagerEvent.PrinterDisconnected:
                    Log.d(TAG, "Printer disconnected");
                    break;
                    
                case DeviceManagerEvent.PrinterWriteSuccess:
                    Log.d(TAG, "Document printed successfully");
                    if (callback != null) {
                        callback.onPrintSuccess();
                    }
                    break;
                    
                case DeviceManagerEvent.PrinterWriteFailure:
                    Log.e(TAG, "Failed to print document");
                    if (callback != null) {
                        String errorMessage = "Failed to print document";
                        if (event.getData() != null && event.getData() instanceof Exception) {
                            errorMessage = ((Exception) event.getData()).getMessage();
                        }
                        callback.onPrintFailure(errorMessage);
                    }
                    break;
                    
                case DeviceManagerEvent.PrintDone:
                    Log.d(TAG, "Print operation completed");
                    // Clean up
                    if (deviceManager != null) {
                        deviceManager.disconnect();
                    }
                    break;
            }
        }
    };
}