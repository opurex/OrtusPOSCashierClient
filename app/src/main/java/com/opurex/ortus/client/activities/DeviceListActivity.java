package com.opurex.ortus.client.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.BluetoothPrinterHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    private static final String TAG = "DeviceListActivity";
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1001;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    private BluetoothPrinterHelper bluetoothHelper;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        setResult(RESULT_CANCELED);

        // Initialize the button to perform device discovery
        MaterialButton scanButton = findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermissionsAndStartDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        bluetoothHelper = new BluetoothPrinterHelper(this);

        // Get a set of currently paired devices
        loadPairedDevices();
        
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothHelper != null) {
            bluetoothHelper.cleanup();
        }
        try {
            this.unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver not registered
            Log.w(TAG, "Receiver not registered: " + e.getMessage());
        }
    }

    private void checkPermissionsAndStartDiscovery() {
        List<String> neededPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }

        if (!neededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    neededPermissions.toArray(new String[0]),
                    REQUEST_BLUETOOTH_PERMISSIONS
            );
        } else {
            doDiscovery();
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        // Indicate scanning in the title
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (bluetoothHelper != null) {
            bluetoothHelper.cleanup();
        }

        // Request discover from BluetoothAdapter
        bluetoothHelper.startDiscovery(new BluetoothPrinterHelper.DiscoveryListener() {
            @Override
            public void onDeviceFound(BluetoothDevice device) {
                runOnUiThread(() -> {
                    mNewDevicesArrayAdapter.add(BluetoothPrinterHelper.formatDeviceInfo(DeviceListActivity.this, device));
                });
            }

            @Override
            public void onDiscoveryFinished() {
                runOnUiThread(() -> {
                    setTitle(R.string.select_device);
                    if (mNewDevicesArrayAdapter.getCount() == 0) {
                        String noDevices = getResources().getText(R.string.none_found).toString();
                        mNewDevicesArrayAdapter.add(noDevices);
                    }
                });
            }
        });
    }

    private void loadPairedDevices() {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothHelper.getPairedDevices(this);

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(BluetoothPrinterHelper.formatDeviceInfo(this, device));
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            if (bluetoothHelper != null) {
                bluetoothHelper.cleanup();
            }

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.length() - 17).trim();

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            intent.putExtra(EXTRA_DEVICE_NAME, name);
            Log.d(TAG, "Device address: " + address + ", name: " + name);

            // Set result and finish this Activity
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(BluetoothPrinterHelper.formatDeviceInfo(DeviceListActivity.this, device));
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                doDiscovery();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_LONG).show();
            }
        }
    }
}
