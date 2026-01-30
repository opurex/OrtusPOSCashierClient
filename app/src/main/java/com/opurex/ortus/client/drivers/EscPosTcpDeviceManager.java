package com.opurex.ortus.client.drivers;

import android.content.Context;
import android.util.Log;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.opurex.ortus.client.OrtusPOS;
import com.opurex.ortus.client.drivers.printer.EscPosPrinterWrapper;
import com.opurex.ortus.client.drivers.printer.documents.PrintableDocument;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEventListener;
import com.opurex.ortus.client.utils.OpurexConfiguration;

public class EscPosTcpDeviceManager implements POSDeviceManager {
    private static final String TAG = "EscPosTcpDeviceManager";
    private DeviceManagerEventListener listener;
    private EscPosPrinterWrapper printer;
    private int deviceIndex;
    private String address;
    private int port;

    public EscPosTcpDeviceManager(DeviceManagerEventListener listener, int deviceIndex) {
        this.listener = listener;
        this.deviceIndex = deviceIndex;

        OpurexConfiguration conf = OrtusPOS.getConf();
        this.address = conf.getPrinterAddress(deviceIndex);

        // Parse address to extract port if in format "ip:port", otherwise default to 9100
        if (this.address.contains(":")) {
            String[] parts = this.address.split(":");
            this.address = parts[0];
            try {
                this.port = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                this.port = 9100; // Default port for ESC/POS printers
            }
        } else {
            this.port = 9100; // Default port for ESC/POS printers
        }
    }

    @Override
    public String getName() {
        return "ESCPOS TCP " + address + ":" + port;
    }

    @Override
    public void connect() {
        try {
            TcpConnection connection = new TcpConnection(address, port);
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
            Log.e(TAG, "Failed to connect to ESCPOS TCP printer", e);
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
        // For TCP printers, we can't directly compare with network objects
        // Return false as this is not typically used for TCP printers
        return false;
    }
}