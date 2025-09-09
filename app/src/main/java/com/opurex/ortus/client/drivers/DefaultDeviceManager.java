package com.opurex.ortus.client.drivers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import com.opurex.ortus.client.OrtusPOS;
import com.opurex.ortus.client.drivers.printer.Printer;
import com.opurex.ortus.client.drivers.printer.EmptyPrinter;
import com.opurex.ortus.client.drivers.printer.EpsonPrinter;
import com.opurex.ortus.client.drivers.printer.LKPXXPrinter;
import com.opurex.ortus.client.drivers.printer.WoosimPrinter;
import com.opurex.ortus.client.drivers.printer.OCPPM083Printer;
import com.opurex.ortus.client.drivers.printer.documents.PrintableDocument;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEventListener;
import com.opurex.ortus.client.utils.OpurexConfiguration;

/**
 * Manager for a single printer.
 * Created by svirch_n on 23/12/15.
 */
public class DefaultDeviceManager implements POSDeviceManager, DeviceManagerEventListener
{
    private static final String TAG = "DefaultDeviceManager";
    protected String name;
    protected Printer printer;
    protected DeviceManagerEventListener listener;

    DefaultDeviceManager(DeviceManagerEventListener listener, int deviceIndex) {
        OpurexConfiguration conf = OrtusPOS.getConf();
        String prDriver = conf.getPrinterDriver(deviceIndex);
        String prAddress = conf.getPrinterAddress(deviceIndex);
        
        // Validate printer address for Bluetooth printers
        boolean isValidAddress = isValidBluetoothAddress(prAddress);
        
        switch (prDriver) {
        case "LK-PXX":
            this.name = "LK-PXX " + prAddress;
            if (isValidAddress) {
                this.printer = new LKPXXPrinter(prAddress, this);
            } else {
                Log.e(TAG, "Invalid Bluetooth address for LK-PXX printer: " + prAddress);
                this.printer = new EmptyPrinter(this);
            }
            break;
        case "Woosim":
            this.name = "Woosim " + prAddress;
            if (isValidAddress) {
                this.printer = new WoosimPrinter(prAddress, this);
            } else {
                Log.e(TAG, "Invalid Bluetooth address for Woosim printer: " + prAddress);
                this.printer = new EmptyPrinter(this);
            }
            break;
        case "EPSON ePOS IP":
            this.name = "EPSON " + conf.getPrinterModel(deviceIndex) + " " + prAddress;
            // EPSON printers use IP addresses, not Bluetooth addresses
            this.printer = new EpsonPrinter(EpsonPrinter.CTX_ETH, prAddress, conf.getPrinterModel(deviceIndex), this);
            break;
        case "OCPP M083":
            this.name = "OCPP M083 " + prAddress;
            if (isValidAddress) {
                this.printer = new OCPPM083Printer(prAddress, this);
            } else {
                Log.e(TAG, "Invalid Bluetooth address for OCPP M083 printer: " + prAddress);
                this.printer = new EmptyPrinter(this);
            }
            break;
        case "None":
        default:
            this.name = "Empty";
            this.printer = new EmptyPrinter(this);
        }
        this.listener = listener;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void print(PrintableDocument doc) {
        if (this.printer.isConnected()) {
            Context context = OrtusPOS.getAppContext();
            doc.print(this.printer, context);
            this.listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrintDone, doc));
        }
    }

    @Override
    public void connect() {
        if (!this.printer.isConnected()) {
            this.printer.connect();
        } else {
            this.listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterConnected));
        }
    }

    @Override
    public void disconnect() {
        if (this.printer.isConnected()) {
            this.printer.disconnect();
        } else {
            this.listener.onDeviceManagerEvent(this, new DeviceManagerEvent(DeviceManagerEvent.PrinterDisconnected));
        }
    }

    public void wasDisconnected() {
        this.printer.forceDisconnect();
    }

    @Override
    public void openCashDrawer() { }
    @Override
    public boolean hasCashDrawer() { return false; }

    @Override
    public void onDeviceManagerEvent(POSDeviceManager manager, DeviceManagerEvent event) {
        // Forward the event to our listener
        if (this.listener != null) {
            this.listener.onDeviceManagerEvent(this, event);
        }
    }

    @Override
    public boolean isManaging(Object o) {
        return false;
    }
    
    /**
     * Validates a Bluetooth MAC address format
     * @param address The Bluetooth address to validate
     * @return true if the address is valid, false otherwise
     */
    private boolean isValidBluetoothAddress(String address) {
        // Allow empty addresses for non-Bluetooth printers
        if (address == null || address.isEmpty()) {
            return false;
        }
        
        // Check format: XX:XX:XX:XX:XX:XX (17 characters)
        if (address.length() != 17) {
            return false;
        }
        
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
}
