package com.opurex.ortus.client.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.opurex.ortus.client.adapters.DeviceListAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.BluetoothScaleHelper;
import com.opurex.ortus.client.utils.BluetoothPrinterHelper;
import com.opurex.ortus.client.utils.ScaleManager;

import java.util.ArrayList;
import java.util.HashMap;
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
    private DeviceListAdapter deviceListAdapter;
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
            // Check if virtual scale is enabled in settings
            boolean virtualScaleEnabled = isVirtualScaleEnabled();
            virtualScaleButton.setVisibility(virtualScaleEnabled ? View.VISIBLE : View.GONE);
            virtualScaleButton.setEnabled(virtualScaleEnabled);

            if (virtualScaleEnabled) {
                virtualScaleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectToVirtualScale();
                    }
                });
            } else {
                // Disable the button and show appropriate message
                virtualScaleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BluetoothScaleSelectionActivity.this,
                            "Virtual scale is disabled in settings",
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Log.w("BluetoothScaleSelection", "Virtual scale button not found in layout");
        }

        availableDevices = new ArrayList<>();
        // Use the custom device layout with name, MAC, and signal strength
        List<HashMap<String, String>> deviceDataList = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(this, deviceDataList,
                R.layout.device_layout,
                new String[]{"NAME", "MAC", "SIG"},
                new int[]{R.id.tv_pair_name, R.id.tv_pair_mac, R.id.tv_pair_sig});
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
        // Cast the adapter to DeviceListAdapter to work with HashMap data
        if (deviceListAdapter instanceof DeviceListAdapter) {
            DeviceListAdapter deviceAdapter = (DeviceListAdapter) deviceListAdapter;
            deviceAdapter.clearItems(); // Clear existing items

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

                HashMap<String, String> deviceMap = new HashMap<>();
                deviceMap.put("NAME", device.getName() != null ? device.getName() : "Unknown Device");
                deviceMap.put("MAC", device.getAddress());
                deviceMap.put("SIG", "N/A"); // Signal strength not available for paired devices
                deviceAdapter.addItem(deviceMap);
            }
        } else {
            // Fallback for compatibility
            List<String> deviceNames = new ArrayList<>();
            for (BluetoothDevice device : availableDevices) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                deviceNames.add(BluetoothPrinterHelper.formatDeviceInfo(this, device));
            }

            deviceListAdapter.clearItems();
            // Add items to the DeviceListAdapter
            for (String deviceName : deviceNames) {
                HashMap<String, String> deviceMap = new HashMap<>();
                deviceMap.put("NAME", deviceName);
                deviceMap.put("MAC", "");
                deviceMap.put("SIG", "");
                deviceListAdapter.addItem(deviceMap);
            }
        }
    }
    
    private void selectDevice(int position) {
        // Handle selection from the device list
        if (position >= 0 && deviceListAdapter instanceof DeviceListAdapter) {
            DeviceListAdapter deviceAdapter = (DeviceListAdapter) deviceListAdapter;
            if (position < deviceAdapter.getCount()) {
                HashMap<String, String> selectedItem = (HashMap<String, String>) deviceAdapter.getItem(position);
                String deviceAddress = selectedItem.get("MAC");
                String deviceName = selectedItem.get("NAME");

                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SCALE_ADDRESS, deviceAddress);
                resultIntent.putExtra(EXTRA_SCALE_NAME, deviceName);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    private String extractMacAddress(String deviceInfo) {
        // Extract MAC address from device info string
        // Format might be "Device Name (AA:BB:CC:DD:EE:FF) - Signal: -70db"
        if (deviceInfo.contains("(") && deviceInfo.contains(")")) {
            int start = deviceInfo.indexOf('(') + 1;
            int end = deviceInfo.indexOf(')');
            if (start < end) {
                return deviceInfo.substring(start, end);
            }
        }
        return deviceInfo; // fallback
    }

    private String extractDeviceName(String deviceInfo) {
        // Extract device name from device info string
        // Format might be "Device Name (AA:BB:CC:DD:EE:FF) - Signal: -70db"
        if (deviceInfo.contains("(")) {
            int end = deviceInfo.indexOf('(');
            if (end > 0) {
                return deviceInfo.substring(0, end).trim();
            }
        }
        return deviceInfo; // fallback
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
        // Check if location services are enabled (required for many Android devices including Redmi)
        if (!isLocationEnabled()) {
            showLocationServicesRequiredDialog();
            return;
        }

        // Check if we have the required permissions for Android 15 and Redmi devices
        String[] requiredPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12+, we need these permissions
            requiredPermissions = new String[]{
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            // For older versions, we need traditional permissions
            requiredPermissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            };
        }

        // Check if any permission is missing
        boolean hasAllPermissions = true;
        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasAllPermissions = false;
                break;
            }
        }

        if (!hasAllPermissions) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1002);
            return;
        }

        // Clear the device list before starting a new scan
        deviceListAdapter.clearItems();

        // Update UI to show scanning state
        progressBar.setVisibility(View.VISIBLE);
        scanButton.setEnabled(false);
        statusText.setText("Scanning for Bluetooth scales...");

        // Register the scan listener to receive device discovery events
        bluetoothScaleHelper.setScanListener(new BluetoothScaleHelper.ScanListener() {
            @Override
            public void onDeviceFound(String name, String mac, String signal) {
                runOnUiThread(() -> {
                    // Check if device is already in the list to avoid duplicates
                    boolean deviceExists = false;

                    if (deviceListAdapter instanceof DeviceListAdapter) {
                        DeviceListAdapter deviceAdapter = (DeviceListAdapter) deviceListAdapter;
                        deviceExists = deviceAdapter.containsDeviceWithMac(mac);

                        if (!deviceExists) {
                            HashMap<String, String> deviceMap = new HashMap<>();
                            deviceMap.put("NAME", name != null ? name : "Unknown Device");
                            deviceMap.put("MAC", mac);
                            deviceMap.put("SIG", signal != null ? signal : "N/A");

                            deviceAdapter.addItem(deviceMap);
                            statusText.setText("Found device: " + name);
                        }
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
                // Check if we need to request ignoring battery optimization on Android 6.0+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestIgnoreBatteryOptimization();
                } else {
                    startScanning();
                }
            } else {
                Toast.makeText(this, "Bluetooth permissions are required for scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Check if location services are enabled
     * This is required for Bluetooth scanning on many Android devices including Redmi
     */
    private boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android 9+, check location service status
            android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                try {
                    return locationManager.isLocationEnabled();
                } catch (Exception e) {
                    // Handle potential SecurityException
                    return true; // Assume enabled if we can't check
                }
            }
        } else {
            // For older versions, check the secure settings
            try {
                int locationMode = android.provider.Settings.Secure.getInt(getContentResolver(),
                        android.provider.Settings.Secure.LOCATION_MODE);
                return locationMode != android.provider.Settings.Secure.LOCATION_MODE_OFF;
            } catch (android.provider.Settings.SettingNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show dialog to enable location services
     */
    private void showLocationServicesRequiredDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Location Services Required");
        builder.setMessage("Location services must be enabled for Bluetooth scanning to work properly on your Redmi device.");
        builder.setPositiveButton("Enable", (dialog, which) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Request to be ignored from battery optimization on Android 6.0+
     * This is critical for Redmi devices and Android 15 to maintain stable Bluetooth connections
     */
    private void requestIgnoreBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.os.PowerManager pm = (android.os.PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                    startActivity(intent);

                    // Wait a bit for the user to respond to the dialog, then start scanning
                    new android.os.Handler().postDelayed(() -> startScanning(), 2000);
                } catch (Exception e) {
                    // If the above fails, just start scanning anyway
                    startScanning();
                }
            } else {
                // Already ignoring battery optimizations, start scanning
                startScanning();
            }
        } else {
            // Below Android 6.0, no battery optimization to worry about
            startScanning();
        }
    }
    
    /**
     * Check if virtual scale is enabled in settings
     */
    private boolean isVirtualScaleEnabled() {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("enable_virtual_scale", false); // Default to false as requested
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
