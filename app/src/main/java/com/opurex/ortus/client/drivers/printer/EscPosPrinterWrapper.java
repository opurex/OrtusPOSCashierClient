package com.opurex.ortus.client.drivers.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParser;

public class EscPosPrinterWrapper implements Printer {
    private DeviceConnection deviceConnection;
    private EscPosPrinter escPosPrinter;
    private Context context;
    private boolean isConnected = false;

    public EscPosPrinterWrapper(Context context, DeviceConnection connection, int printerDpi, float printerWidthMM, int printerNbrCharactersPerLine) {
        this.context = context;
        this.deviceConnection = connection;
        try {
            this.escPosPrinter = new EscPosPrinter(deviceConnection, printerDpi, printerWidthMM, printerNbrCharactersPerLine);
            this.isConnected = true;
        } catch (EscPosConnectionException e) {
            e.printStackTrace();
            this.isConnected = false;
        }
    }

    @Override
    public String getAddress() {
        if (deviceConnection != null) {
            return deviceConnection instanceof BluetoothConnection ?
                    ((BluetoothConnection) deviceConnection).getDevice().getAddress() :
                    "Network Printer";
        }
        return "";
    }

    @Override
    public void connect() {
        try {
            deviceConnection.connect();
            isConnected = true;
        } catch (EscPosConnectionException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    @Override
    public void disconnect() {
        if (deviceConnection != null) {
            deviceConnection.disconnect();
            isConnected = false;
        }
    }

    @Override
    public void forceDisconnect() {
        if (deviceConnection != null) {
            deviceConnection.disconnect();
            isConnected = false;
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected && deviceConnection != null && deviceConnection.isConnected();
    }

    @Override
    public void initPrint() {
        // ESCPOS handles initialization internally
    }

    @Override
    public void printLine() {
        try {
            escPosPrinter.printFormattedText("\n");
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printLine(String data) {
        try {
            escPosPrinter.printFormattedText(data + "\n");
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        try {
            escPosPrinter.printFormattedText("[C]<img>" + bitmap + "</img>\n");
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cut() {
        try {
            escPosPrinter.printFormattedText("\n\n[f]\n");
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException | EscPosBarcodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush() {
        // ESCPOS printer handles disconnection internally
        // Just ensure all data is sent
    }
}