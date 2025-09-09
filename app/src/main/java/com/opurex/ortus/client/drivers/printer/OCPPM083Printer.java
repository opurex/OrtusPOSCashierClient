/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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

public class OCPPM083Printer implements Printer {

    private static final String TAG = "OCPPM083Printer";
    // Standard SerialPortService ID for Bluetooth SPP
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket socket;
    private OutputStream printerStream;
    private boolean connected;
    private String address;
    private DeviceManagerEventListener listener;

    public OCPPM083Printer(String address, DeviceManagerEventListener listener) {
        super();
        this.address = address;
        this.listener = listener;
    }

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
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, new Exception("Bluetooth not supported"));
            return;
        }

        if (!btAdapter.isEnabled()) {
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, new Exception("Bluetooth not enabled"));
            return;
        }

        // Validate Bluetooth address
        if (this.address == null || this.address.isEmpty()) {
            Log.e(TAG, "Invalid Bluetooth address: address is null or empty");
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new IllegalArgumentException("Bluetooth address is null or empty"));
            return;
        }

        // Validate Bluetooth address format (should be 17 characters in format XX:XX:XX:XX:XX:XX)
        if (!isValidBluetoothAddress(this.address)) {
            Log.e(TAG, "Invalid Bluetooth address format: " + this.address);
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, 
                new IllegalArgumentException("Invalid Bluetooth address format: " + this.address));
            return;
        }

        try {
            BluetoothDevice device = btAdapter.getRemoteDevice(this.address.toUpperCase());
            // Create a BluetoothSocket to connect to the device
            this.socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            
            // Connect to the device
            this.socket.connect();
            
            // Get the output stream for sending data
            this.printerStream = this.socket.getOutputStream();
            
            this.connected = true;
            this.notifyListener(DeviceManagerEvent.PrinterConnected);
        } catch (IOException e) {
            Log.e(TAG, "Connection failed", e);
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid Bluetooth address", e);
            connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterConnectFailure, e);
        }
    }

    /**
     * Validates a Bluetooth MAC address format
     * @param address The Bluetooth address to validate
     * @return true if the address is valid, false otherwise
     */
    private boolean isValidBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        
        // Check format: XX:XX:XX:XX:XX:XX
        String[] parts = address.split(":");
        if (parts.length != 6) {
            return false;
        }
        
        for (String part : parts) {
            if (part.length() != 2) {
                return false;
            }
            // Check if each part is a valid hexadecimal number
            try {
                Integer.parseInt(part, 16);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void disconnect() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.printerStream != null) {
                this.printerStream.close();
            }
            this.connected = false;
            this.notifyListener(DeviceManagerEvent.PrinterDisconnected);
        } catch (IOException e) {
            Log.e(TAG, "Error disconnecting", e);
            this.notifyListener(DeviceManagerEvent.PrinterDisconnectFailure, e);
        }
    }

    @Override
    public void forceDisconnect() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error force disconnecting socket", e);
        }
        try {
            if (this.printerStream != null) {
                this.printerStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error force disconnecting stream", e);
        }
        this.connected = false;
        this.notifyListener(DeviceManagerEvent.PrinterDisconnected);
    }

    @Override
    public void initPrint() {
        try {
            if (this.printerStream != null) {
                // Initialize printer
                this.printerStream.write(0x1B);
                this.printerStream.write(0x40);
                
                // Set line spacing to 30 (default)
                this.printerStream.write(0x1B);
                this.printerStream.write(0x33);
                this.printerStream.write(30);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error initializing printer", e);
        }
    }

    @Override
    public void printLine(String data) {
        if (data == null) return;
        
        // Convert special characters to ASCII equivalents
        String ascii = data.replace("é", "e");
        ascii = ascii.replace("è", "e");
        ascii = ascii.replace("ê", "e");
        ascii = ascii.replace("ë", "e");
        ascii = ascii.replace("à", "a");
        ascii = ascii.replace("ï", "i");
        ascii = ascii.replace("ô", "o");
        ascii = ascii.replace("ç", "c");
        ascii = ascii.replace("ù", "u");
        ascii = ascii.replace("É", "E");
        ascii = ascii.replace("È", "E");
        ascii = ascii.replace("Ê", "E");
        ascii = ascii.replace("Ë", "E");
        ascii = ascii.replace("À", "A");
        ascii = ascii.replace("Ï", "I");
        ascii = ascii.replace("Ô", "O");
        ascii = ascii.replace("Ç", "C");
        ascii = ascii.replace("Ù", "U");
        
        try {
            if (this.printerStream != null) {
                this.printerStream.write(ascii.getBytes("UTF-8"));
                // Add line feed
                this.printerStream.write(0x0A);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error printing line", e);
        }
    }

    @Override
    public void printLine() {
        try {
            if (this.printerStream != null) {
                // Send line feed command
                this.printerStream.write(0x0A);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error printing empty line", e);
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        if (bitmap == null) return;
        
        try {
            if (this.printerStream != null) {
                // Convert bitmap to printer format
                printBitmapData(bitmap);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error printing bitmap", e);
        }
    }

    @Override
    public void cut() {
        try {
            if (this.printerStream != null) {
                // Send cut command (ESC + "m")
                this.printerStream.write(0x1B);
                this.printerStream.write(0x6D);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error cutting paper", e);
        }
    }

    @Override
    public void flush() {
        try {
            if (this.printerStream != null) {
                // Ensure data is sent to printer
                this.printerStream.flush();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error flushing stream", e);
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * Converts a bitmap to printer-compatible format and sends it to the printer
     * @param bitmap The bitmap to print
     * @throws IOException
     */
    private void printBitmapData(Bitmap bitmap) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        // Resize bitmap to fit printer width (384 pixels is common for 58mm printers)
        int targetWidth = 384;
        if (width > targetWidth) {
            float ratio = (float) targetWidth / width;
            int targetHeight = (int) (height * ratio);
            bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            width = targetWidth;
            height = targetHeight;
        }
        
        // Convert to black and white
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        
        // Convert to printer format (1 bit per pixel)
        byte[] data = new byte[(width + 7) / 8 * height];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x += 8) {
                byte value = 0;
                for (int k = 0; k < 8; k++) {
                    int pixelIndex = y * width + x + k;
                    if (pixelIndex < pixels.length) {
                        int pixel = pixels[pixelIndex];
                        int alpha = (pixel >> 24) & 0xFF;
                        int red = (pixel >> 16) & 0xFF;
                        int green = (pixel >> 8) & 0xFF;
                        int blue = pixel & 0xFF;
                        
                        // Simple threshold for black/white conversion
                        if (alpha > 128 && (red + green + blue) / 3 < 128) {
                            value |= (1 << (7 - k));
                        }
                    }
                }
                data[(y * ((width + 7) / 8)) + (x / 8)] = value;
            }
        }
        
        // Send graphics command GS v 0
        this.printerStream.write(0x1D);
        this.printerStream.write(0x76);
        this.printerStream.write(0x30);
        this.printerStream.write(0x00);
        
        // Width (LSB, MSB) - bytes per line
        int bytesPerLine = (width + 7) / 8;
        this.printerStream.write(bytesPerLine % 256);
        this.printerStream.write(bytesPerLine / 256);
        
        // Height (LSB, MSB)
        this.printerStream.write(height % 256);
        this.printerStream.write(height / 256);
        
        // Image data
        this.printerStream.write(data);
        
        // Add a blank line after the image
        this.printerStream.write(0x0A);
    }
}