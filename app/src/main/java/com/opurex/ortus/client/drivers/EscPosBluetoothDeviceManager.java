package com.opurex.ortus.client.drivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.opurex.ortus.client.OrtusPOS;
import com.opurex.ortus.client.drivers.printer.EscPosPrinterWrapper;
import com.opurex.ortus.client.drivers.printer.documents.PrintableDocument;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEventListener;
import com.opurex.ortus.client.utils.OpurexConfiguration;

public class EscPosBluetoothDeviceManager implements POSDeviceManager {
    private static final String TAG = "EscPosBluetoothDeviceManager";
    private DeviceManagerEventListener listener;
    private EscPosPrinterWrapper printer;
    private int deviceIndex;
    private String address;

    public EscPosBluetoothDeviceManager(DeviceManagerEventListener listener, int deviceIndex) {
        this.listener = listener;
        this.deviceIndex = deviceIndex;
        
        OpurexConfiguration conf = OrtusPOS.getConf();
        this.address = conf.getPrinterAddress(deviceIndex);
    }

    @Override
    public String getName() {
        return "ESCPOS Bluetooth " + address;
    }

    @Override
    public void connect() {
        try {
            // Find the Bluetooth device by address
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            BluetoothConnection connection = new BluetoothConnection(device);

            this.printer = new EscPosPrinterWrapper(
                OrtusPOS.getAppContext(),
                connection,
                200, // DPI
                48f, // Width in mm
                32   // Characters per line
            );

            if (printer.isConnected()) {
                listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterConnected));
            } else {
                listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterConnectFailure));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to connect to ESCPOS Bluetooth printer", e);
            listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterConnectFailure));
        }
    }

    @Override
    public void disconnect() {
        if (printer != null) {
            printer.disconnect();
            listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterDisconnected));
        }
    }

    @Override
    public void wasDisconnected() {
        listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterDisconnected));
    }

    @Override
    public void print(PrintableDocument doc) {
        if (printer != null && printer.isConnected()) {
            Context context = OrtusPOS.getAppContext();
            boolean success = doc.print(printer, context);
            
            if (success) {
                listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrintDone, doc));
            } else {
                listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrintError));
            }
        } else {
            listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrintError));
        }
    }

    @Override
    public void openCashDrawer() {
        // Not implemented for ESCPOS
    }

    @Override
    public boolean hasCashDrawer() {
        return false;
    }

    @Override
    public boolean isManaging(Object o) {
        if (o instanceof BluetoothDevice) {
            BluetoothDevice device = (BluetoothDevice) o;
            return device.getAddress().equals(address);
        }
        return false;
    }
}