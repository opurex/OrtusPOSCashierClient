package com.opurex.ortus.client.activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.opurex.ortus.client.utils.BluetoothPrinterHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Activity for selecting a Bluetooth printer
 */
public class BluetoothPrinterSelectionActivity extends AppCompatActivity {
    
    public static final String EXTRA_PRINTER_ADDRESS = "printer_address";
    public static final String EXTRA_PRINTER_NAME = "printer_name";
    
    private static final int REQUEST_SCAN_DEVICES = 1001;
    
    private BluetoothPrinterHelper bluetoothHelper;
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
        setContentView(R.layout.activity_bluetooth_printer_selection);
        
        // Set up the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Bluetooth Printer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initViews();
        initBluetoothHelper();
        loadPairedDevices();
    }
    
    private void initViews() {
        deviceListView = findViewById(R.id.device_list);
        progressBar = findViewById(R.id.progress_bar);
        statusText = findViewById(R.id.status_text);
        scanButton = findViewById(R.id.scan_button);
        cancelButton = findViewById(R.id.cancel_button);
        
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
                // Launch the new DeviceListActivity for scanning
                Intent intent = new Intent(BluetoothPrinterSelectionActivity.this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_SCAN_DEVICES);
            }
        });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    
    private void initBluetoothHelper() {
        bluetoothHelper = new BluetoothPrinterHelper(this);
        
        // Note: The new BluetoothPrinterHelper doesn't have isBluetoothSupported() or isBluetoothEnabled() methods
        // We'll check these manually
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
    }
    
    private void loadPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothHelper.getPairedDevices(this);
        updateDeviceList(new ArrayList<>(pairedDevices));
        
        if (pairedDevices.isEmpty()) {
            statusText.setText("No paired devices found. Please pair your printer in Bluetooth settings.");
        } else {
            statusText.setText("Select a printer from the list below:");
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
            resultIntent.putExtra(EXTRA_PRINTER_ADDRESS, selectedDevice.getAddress());
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
            resultIntent.putExtra(EXTRA_PRINTER_NAME, selectedDevice.getName());
            
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
                resultIntent.putExtra(EXTRA_PRINTER_ADDRESS, deviceAddress);
                resultIntent.putExtra(EXTRA_PRINTER_NAME, deviceName != null ? deviceName : "");
                
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothHelper != null) {
            bluetoothHelper.cleanup();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        return true;
    }
}