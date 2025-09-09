package com.opurex.ortus.client.drivers.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;

import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Bluetooth printer implementation for generic ESC/POS compatible printers
 */
public class BluetoothPrinter implements Printer {
    private static final String TAG = "BluetoothPrinter";
    
    // Well known SPP UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private BluetoothSocket sock;
    private OutputStream printerStream;
    private boolean connected;
    private String address;
    private DeviceManagerEventListener listener;
    private StringBuilder printBuffer;

    public BluetoothPrinter(String address, DeviceManagerEventListener listener) {
        super();
        this.address = address;
        this.listener = listener;
        this.printBuffer = new StringBuilder();
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    private void notifyListener(int event) { 
        this.notifyListener(event, null); 
    }
    
    private void notifyListener(int event, Object data) {
        if (this.listener != null) {
            this.listener.onDeviceManagerEvent(null, new DeviceManagerEvent(event, data));
        }
    }

    @Override
    public void connect() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (btAdapter == null) {
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new Exception("Bluetooth not supported on this device"));
            return;
        }
        
        if (!btAdapter.isEnabled()) {
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new Exception("Bluetooth is disabled"));
            return;
        }
        
        try {
            BluetoothDevice device = btAdapter.getRemoteDevice(this.address.toUpperCase());
            // Get a BluetoothSocket
            this.sock = device.createRfcommSocketToServiceRecord(SPP_UUID);
            
            // Connect to the BluetoothSocket
            this.sock.connect();
            
            // Get the output stream
            this.printerStream = this.sock.getOutputStream();
            
            this.connected = true;
            this.notifyListener(DeviceManagerEvent.PrinterConnected);
        } catch (IllegalArgumentException e) {
            this.connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new Exception("Invalid Bluetooth address: " + e.getMessage()));
        } catch (IOException e) {
            this.connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new Exception("Failed to connect to Bluetooth printer: " + e.getMessage()));
        }
    }

    @Override
    public void disconnect() {
        try {
            if (this.sock != null) {
                this.sock.close();
            }
            
            if (this.printerStream != null) {
                this.printerStream.close();
            }
            
            this.connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterDisconnected);
        } catch (IOException e) {
            this.notifyListener(DeviceManagerEvent.PrinterDisconnectFailure, 
                new Exception("Failed to disconnect from Bluetooth printer: " + e.getMessage()));
        }
    }

    @Override
    public void forceDisconnect() {
        try {
            if (this.sock != null) {
                this.sock.close();
            }
        } catch (IOException e) {
            Log.w(TAG, "Error closing socket: " + e.getMessage());
        }
        
        try {
            if (this.printerStream != null) {
                this.printerStream.close();
            }
        } catch (IOException e) {
            Log.w(TAG, "Error closing stream: " + e.getMessage());
        }
        
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void initPrint() {
        // Clear the buffer
        this.printBuffer.setLength(0);
    }

    @Override
    public void printLine() {
        this.printBuffer.append("\n");
    }

    @Override
    public void printLine(String data) {
        this.printBuffer.append(data).append("\n");
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        // For simplicity, we'll convert bitmap to text representation
        // In a real implementation, you would convert the bitmap to printer commands
        this.printBuffer.append("[Bitmap Image]\n");
    }

    @Override
    public void cut() {
        // Standard ESC/POS cut command
        this.printBuffer.append("\u001D\u0056\u0000"); // Full cut
    }

    @Override
    public void flush() {
        if (!this.connected) {
            this.notifyListener(DeviceManagerEvent.PrinterWriteFailure, 
                new Exception("Printer not connected"));
            return;
        }
        
        try {
            // Send the buffer content to the printer
            byte[] data = this.printBuffer.toString().getBytes("UTF-8");
            this.printerStream.write(data);
            this.printerStream.flush();
            
            // Clear the buffer
            this.printBuffer.setLength(0);
            
            this.notifyListener(DeviceManagerEvent.PrinterWriteSuccess);
        } catch (IOException e) {
            this.notifyListener(DeviceManagerEvent.PrinterWriteFailure, 
                new Exception("Failed to send data to printer: " + e.getMessage()));
        }
    }
}