package com.opurex.ortus.client.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.Set;

public class BluetoothPrinterHelper {

    private static final String TAG = "BluetoothPrinterHelper";

    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;
    private DiscoveryListener discoveryListener;
    private boolean receiverRegistered = false;

    public interface DiscoveryListener {
        void onDeviceFound(BluetoothDevice device);
        void onDiscoveryFinished();
    }

    public BluetoothPrinterHelper(Context context) {
        this.context = context.getApplicationContext();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startDiscovery(DiscoveryListener listener) {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth not supported");
            return;
        }

        this.discoveryListener = listener;

        // Check BLUETOOTH_SCAN permission for discovery
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "BLUETOOTH_SCAN permission not granted, cannot start discovery");
                return;
            }
        }

        try {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException when cancelling discovery", e);
        }

        if (!receiverRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            context.registerReceiver(discoveryReceiver, filter);
            receiverRegistered = true;
        }

        try {
            bluetoothAdapter.startDiscovery();
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException when starting discovery", e);
        }
    }

    public void cleanup() {
        if (receiverRegistered) {
            try {
                context.unregisterReceiver(discoveryReceiver);
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Receiver already unregistered");
            }
            receiverRegistered = false;
        }
        
        if (bluetoothAdapter != null) {
            try {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException when cancelling discovery", e);
            }
        }
    }

    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && discoveryListener != null) {
                    discoveryListener.onDeviceFound(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (discoveryListener != null) {
                    discoveryListener.onDiscoveryFinished();
                }
            }
        }
    };

    /** Format device as Name + MAC address */
    public static String formatDeviceInfo(Context ctx, BluetoothDevice device) {
        if (device == null) return "Unknown device";

        String name = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) {
                name = device.getName();
            }
        } else {
            name = device.getName();
        }

        if (name == null || name.isEmpty()) {
            name = "Unknown";
        }

        return name + "\n" + device.getAddress();
    }
    
    /** Get a set of currently paired devices */
    public Set<BluetoothDevice> getPairedDevices(Context ctx) {
        if (bluetoothAdapter == null) {
            return Set.of(); // Return empty set
        }
        
        // Check BLUETOOTH_CONNECT permission for getting paired devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "BLUETOOTH_CONNECT permission not granted, cannot get paired devices");
                return Set.of(); // Return empty set
            }
        }
        
        try {
            return bluetoothAdapter.getBondedDevices();
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException when getting paired devices", e);
            return Set.of(); // Return empty set
        }
    }
}
