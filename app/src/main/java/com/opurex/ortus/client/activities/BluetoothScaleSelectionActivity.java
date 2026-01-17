package com.opurex.ortus.client.activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.BluetoothScaleHelper;
import com.opurex.ortus.client.utils.BluetoothPrinterHelper;
import com.opurex.ortus.client.utils.ScaleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Activity for selecting a Bluetooth scale
 */
public class BluetoothScaleSelectionActivity extends AppCompatActivity {
    
    public static final String EXTRA_SCALE_ADDRESS = "scale_address";
    public static final String EXTRA_SCALE_NAME = "scale_name";
    
    private static final int REQUEST_SCAN_DEVICES = 1001;
    
    private BluetoothScaleHelper bluetoothScaleHelper;
    private ArrayAdapter<String> deviceListAdapter;
    private List<BluetoothDevice> availableDevices;
    
    private ListView deviceListView;
    private ProgressBar progressBar;
    private TextView statusText;
    private Button scanButton;
    private Button cancelButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scale_selection);
        
        // Set up the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Bluetooth Scale");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initViews();
        initBluetoothScaleHelper();
        loadPairedDevices();
    }
    
    private void initViews() {
        deviceListView = findViewById(R.id.device_list);
        progressBar = findViewById(R.id.progress_bar);
        statusText = findViewById(R.id.status_text);
        scanButton = findViewById(R.id.scan_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Add a button for virtual scale
        Button virtualScaleButton = findViewById(R.id.virtual_scale_button);
        if (virtualScaleButton != null) {
            virtualScaleButton.setVisibility(View.VISIBLE);
            virtualScaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectToVirtualScale();
                }
            });
        } else {
            Log.w("BluetoothScaleSelection", "Virtual scale button not found in layout");
        }

        availableDevices = new ArrayList<>();
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        deviceListView.setAdapter(deviceListAdapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectDevice(position);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopScanning();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    
    private void initBluetoothScaleHelper() {
        // Check if Bluetooth is supported and enabled
        android.bluetooth.BluetoothAdapter bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable Bluetooth and try again", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bluetoothScaleHelper = new BluetoothScaleHelper(this);
    }

    private void loadPairedDevices() {
        if (bluetoothScaleHelper == null) {
            // Skip loading paired devices if Bluetooth is not available
            statusText.setText("Bluetooth not available. Please enable Bluetooth to see paired devices.");
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothScaleHelper.getPairedDevices();
        updateDeviceList(new ArrayList<>(pairedDevices));

        if (pairedDevices.isEmpty()) {
            statusText.setText("No paired devices found. Please pair your scale in Bluetooth settings.");
        } else {
            statusText.setText("Select a scale from the list below:");
        }
    }
    
    private void updateDeviceList(List<BluetoothDevice> devices) {
        availableDevices.clear();
        availableDevices.addAll(devices);
        updateDeviceListDisplay();
    }
    
    private void updateDeviceListDisplay() {
        List<String> deviceNames = new ArrayList<>();
        for (BluetoothDevice device : availableDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            deviceNames.add(BluetoothPrinterHelper.formatDeviceInfo(this, device));
        }
        
        deviceListAdapter.clear();
        deviceListAdapter.addAll(deviceNames);
        deviceListAdapter.notifyDataSetChanged();
    }
    
    private void selectDevice(int position) {
        if (position >= 0 && position < availableDevices.size()) {
            BluetoothDevice selectedDevice = availableDevices.get(position);
            
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCALE_ADDRESS, selectedDevice.getAddress());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            resultIntent.putExtra(EXTRA_SCALE_NAME, selectedDevice.getName());
            
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_DEVICES && resultCode == RESULT_OK && data != null) {
            String deviceAddress = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            String deviceName = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_NAME);

            if (deviceAddress != null && !deviceAddress.isEmpty()) {
                // Create a mock BluetoothDevice for our list
                // In a real implementation, we would need to create a proper BluetoothDevice
                // For now, we'll just update the UI directly
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SCALE_ADDRESS, deviceAddress);
                resultIntent.putExtra(EXTRA_SCALE_NAME, deviceName != null ? deviceName : "");

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    private void startScanning() {
        // Check if we have the required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                    1002);
                return;
            }
        }

        // Note: The Aclas SDK is designed to find specific Aclas scale devices
        // It may not detect generic Bluetooth devices like speakers
        // The scanning is specifically looking for Aclas scale devices

        // Register the scan listener to receive device discovery events
        bluetoothScaleHelper.setScanListener(new BluetoothScaleHelper.ScanListener() {
            @Override
            public void onDeviceFound(String name, String mac, String signal) {
                runOnUiThread(() -> {
                    // Create a temporary BluetoothDevice object to add to our list
                    // Since we can't create BluetoothDevice directly, we'll store the info differently
                    String deviceInfo = name + " (" + mac + ") - Signal: " + signal;

                    // Check if device is already in the list to avoid duplicates
                    boolean deviceExists = false;
                    for (int i = 0; i < deviceListAdapter.getCount(); i++) {
                        if (deviceListAdapter.getItem(i).equals(deviceInfo)) {
                            deviceExists = true;
                            break;
                        }
                    }

                    if (!deviceExists) {
                        deviceListAdapter.add(deviceInfo);
                        deviceListAdapter.notifyDataSetChanged();

                        // Store the actual device info for later use
                        // We'll use a simple approach: parse the MAC from the string
                        // In a production app, you'd want a better mapping
                        statusText.setText("Found device: " + name);
                    }
                });
            }

            @Override
            public void onScanFinished() {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    scanButton.setEnabled(true);
                    statusText.setText("Scan finished. Select a device from the list.");
                });
            }
        });

        // Clear the device list and start scanning
        deviceListAdapter.clear();
        deviceListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        scanButton.setEnabled(false);
        statusText.setText("Scanning for Bluetooth scales...");

        // Start the actual scan
        bluetoothScaleHelper.startScan();
    }

    private void stopScanning() {
        bluetoothScaleHelper.stopScan();
        progressBar.setVisibility(View.GONE);
        scanButton.setEnabled(true);
    }

    private void connectToVirtualScale() {
        // Initialize virtual scale testing
        ScaleManager tempScaleManager = new ScaleManager(this);
        tempScaleManager.initializeVirtualScaleTesting();

        // Connect to virtual scale
        boolean connected = tempScaleManager.connectToVirtualScale();

        if (connected) {
            // Simulate finding a virtual scale device
            String virtualDeviceName = "Virtual Aclas Scale";
            String virtualDeviceAddress = "VIRTUAL:AC:LAS:SC:AL:E0";

            // Return the virtual device as if it was selected
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCALE_ADDRESS, virtualDeviceAddress);
            resultIntent.putExtra(EXTRA_SCALE_NAME, virtualDeviceName);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Failed to connect to virtual scale", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startScanning();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required for scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothScaleHelper != null) {
            bluetoothScaleHelper.cleanup();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        return true;
    }
}
