package com.opurex.ortus.client.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Integration test for Bluetooth SPP scale functionality
 * This test verifies the complete Bluetooth SPP workflow including:
 * 1. Bluetooth availability check
 * 2. ScaleManager initialization
 * 3. Device scanning
 * 4. Connection management
 * 5. Scale operations
 */
@RunWith(AndroidJUnit4.class)
public class BluetoothSPPIntegrationTest {

    private static final String TAG = "BluetoothSPPTest";
    private Context context;
    private ScaleManager scaleManager;
    private BluetoothAdapter bluetoothAdapter;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        scaleManager = new ScaleManager(context);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Test
    public void testBluetoothAvailability() {
        Log.d(TAG, "Testing Bluetooth availability");
        
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth not supported on this device");
            fail("Bluetooth not supported");
        }
        
        Log.d(TAG, "Bluetooth adapter found: " + bluetoothAdapter.getName());
        assertNotNull("Bluetooth adapter should not be null", bluetoothAdapter);
    }

    @Test
    public void testScaleManagerInitialization() {
        Log.d(TAG, "Testing ScaleManager initialization");
        
        assertNotNull("ScaleManager should be initialized", scaleManager);
        assertFalse("Scale should not be connected initially", scaleManager.isConnected());
        assertNull("No initial error should be present", scaleManager.getLastError());
        
        Log.d(TAG, "ScaleManager initialized successfully");
    }

    @Test
    public void testBluetoothScanWorkflow() {
        Log.d(TAG, "Testing Bluetooth scan workflow");
        
        if (bluetoothAdapter == null) {
            Log.w(TAG, "Skipping scan test - Bluetooth not supported");
            return;
        }
        
        if (!bluetoothAdapter.isEnabled()) {
            Log.w(TAG, "Skipping scan test - Bluetooth is disabled");
            return;
        }
        
        // Set up scan listener
        final boolean[] scanFinished = {false};
        final int[] devicesFound = {0};
        
        scaleManager.setScanListener(new ScaleManager.ScanListener() {
            @Override
            public void onDeviceFound(String name, String mac, String signal) {
                Log.d(TAG, "Found device: " + name + " (" + mac + ") Signal: " + signal + "db");
                devicesFound[0]++;
            }
            
            @Override
            public void onScanFinished() {
                Log.d(TAG, "Scan finished. Total devices found: " + devicesFound[0]);
                scanFinished[0] = true;
            }
        });
        
        // Start scan
        Log.d(TAG, "Starting Bluetooth scan...");
        scaleManager.startScan();
        
        // Wait for scan to complete (with timeout)
        long startTime = System.currentTimeMillis();
        final long TIMEOUT_MS = 15000; // 15 seconds
        
        while (!scanFinished[0] && (System.currentTimeMillis() - startTime) < TIMEOUT_MS) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                fail("Scan interrupted: " + e.getMessage());
            }
        }
        
        // Stop scan
        scaleManager.stopScan();
        
        Log.d(TAG, "Scan completed. Found " + devicesFound[0] + " devices");
        
        // Verify scan completed
        assertTrue("Scan should have completed", scanFinished[0]);
        
        // If no devices found, that's okay - it depends on the test environment
        Log.d(TAG, "Bluetooth scan workflow test completed successfully");
    }

    @Test
    public void testScaleConnectionManagement() {
        Log.d(TAG, "Testing scale connection management");
        
        // Test connection state listener
        final boolean[] connectedCalled = {false};
        final boolean[] disconnectedCalled = {false};
        final String[] errorMessage = {null};
        
        scaleManager.setConnectionStateListener(new ScaleManager.ConnectionStateListener() {
            @Override
            public void onScaleConnected() {
                Log.d(TAG, "Scale connected callback received");
                connectedCalled[0] = true;
            }
            
            @Override
            public void onScaleDisconnected() {
                Log.d(TAG, "Scale disconnected callback received");
                disconnectedCalled[0] = true;
            }
            
            @Override
            public void onScaleError(String errorMessage) {
                Log.e(TAG, "Scale error: " + errorMessage);
                errorMessage[0] = errorMessage;
            }
        });
        
        // Test invalid connection
        Log.d(TAG, "Testing invalid MAC address connection...");
        boolean result = scaleManager.connectToScale("");
        assertFalse("Connection with empty MAC should fail", result);
        
        // Test valid connection attempt (will fail in test environment but should not crash)
        Log.d(TAG, "Testing valid MAC address connection attempt...");
        result = scaleManager.connectToScale("AA:BB:CC:DD:EE:FF");
        assertTrue("Connection attempt should start successfully", result);
        
        // Wait a bit for connection attempt to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail("Connection test interrupted");
        }
        
        // Test disconnection
        Log.d(TAG, "Testing disconnection...");
        scaleManager.disconnect();
        
        // Verify we're disconnected
        assertFalse("Scale should be disconnected", scaleManager.isConnected());
        
        Log.d(TAG, "Scale connection management test completed");
    }

    @Test
    public void testScaleOperations() {
        Log.d(TAG, "Testing scale operations");
        
        // Test weight listener
        final double[] lastWeight = {0.0};
        final String[] lastUnit = {null};
        
        scaleManager.setScaleWeightListener(new ScaleManager.ScaleWeightListener() {
            @Override
            public void onWeightReceived(double weight, String unit) {
                Log.d(TAG, "Weight received: " + weight + " " + unit);
                lastWeight[0] = weight;
                lastUnit[0] = unit;
            }
        });
        
        // Test scale operations (these will fail in test environment but should not crash)
        Log.d(TAG, "Testing zero scale operation...");
        scaleManager.zeroScale();
        
        Log.d(TAG, "Testing tare scale operation...");
        scaleManager.tareScale();
        
        // Test price calculation
        Product testProduct = new Product("Test Product", 5.99, 10.0, 1);
        testProduct.setScaled(true);
        
        double weight = 2.5;
        double price = scaleManager.calculatePrice(testProduct, weight);
        String priceString = scaleManager.calculatePriceString(testProduct, weight);
        
        Log.d(TAG, "Price calculation test: " + weight + "kg * " + testProduct.getPrice() + "/kg = " + price);
        
        // Verify price calculation
        double expectedPrice = testProduct.getPrice() * weight;
        assertEquals("Price calculation should be correct", expectedPrice, price, 0.001);
        assertEquals("Price string should be formatted correctly", "14.98", priceString);
        
        Log.d(TAG, "Scale operations test completed");
    }

    @Test
    public void testErrorHandling() {
        Log.d(TAG, "Testing error handling");
        
        // Test error scenarios
        String initialError = scaleManager.getLastError();
        assertNull("No initial error should be present", initialError);
        
        // Test operations when not connected
        scaleManager.zeroScale();
        scaleManager.tareScale();
        
        // Check if any errors were reported
        String finalError = scaleManager.getLastError();
        if (finalError != null) {
            Log.d(TAG, "Error reported: " + finalError);
        } else {
            Log.d(TAG, "No errors reported during error handling test");
        }
        
        Log.d(TAG, "Error handling test completed");
    }

    @Test
    public void testCompleteBluetoothSPPWorkflow() {
        Log.d(TAG, "Testing complete Bluetooth SPP workflow");
        
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.w(TAG, "Skipping complete workflow test - Bluetooth not available");
            return;
        }
        
        // Step 1: Initialize
        Log.d(TAG, "Step 1: Initializing ScaleManager...");
        assertNotNull("ScaleManager should be initialized", scaleManager);
        
        // Step 2: Set up listeners
        Log.d(TAG, "Step 2: Setting up listeners...");
        final boolean[] scanFinished = {false};
        final boolean[] connected = {false};
        final boolean[] weightReceived = {false};
        
        scaleManager.setScanListener(new ScaleManager.ScanListener() {
            @Override
            public void onDeviceFound(String name, String mac, String signal) {
                Log.d(TAG, "Device found during workflow: " + name + " (" + mac + ")");
            }
            
            @Override
            public void onScanFinished() {
                Log.d(TAG, "Scan finished in workflow");
                scanFinished[0] = true;
            }
        });
        
        scaleManager.setConnectionStateListener(new ScaleManager.ConnectionStateListener() {
            @Override
            public void onScaleConnected() {
                Log.d(TAG, "Scale connected in workflow");
                connected[0] = true;
            }
            
            @Override
            public void onScaleDisconnected() {
                Log.d(TAG, "Scale disconnected in workflow");
            }
            
            @Override
            public void onScaleError(String errorMessage) {
                Log.e(TAG, "Error in workflow: " + errorMessage);
            }
        });
        
        scaleManager.setScaleWeightListener(new ScaleManager.ScaleWeightListener() {
            @Override
            public void onWeightReceived(double weight, String unit) {
                Log.d(TAG, "Weight received in workflow: " + weight + " " + unit);
                weightReceived[0] = true;
            }
        });
        
        // Step 3: Scan for devices
        Log.d(TAG, "Step 3: Scanning for devices...");
        scaleManager.startScan();
        
        // Wait for scan
        long startTime = System.currentTimeMillis();
        while (!scanFinished[0] && (System.currentTimeMillis() - startTime) < 10000) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                fail("Workflow interrupted");
            }
        }
        
        scaleManager.stopScan();
        
        // Step 4: Test connection (will fail in test environment but should not crash)
        Log.d(TAG, "Step 4: Testing connection...");
        boolean connectionResult = scaleManager.connectToScale("AA:BB:CC:DD:EE:FF");
        assertTrue("Connection attempt should start", connectionResult);
        
        // Wait for connection attempt
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail("Connection wait interrupted");
        }
        
        // Step 5: Test operations
        Log.d(TAG, "Step 5: Testing scale operations...");
        scaleManager.zeroScale();
        scaleManager.tareScale();
        
        // Step 6: Disconnect
        Log.d(TAG, "Step 6: Disconnecting...");
        scaleManager.disconnect();
        assertFalse("Should be disconnected", scaleManager.isConnected());
        
        Log.d(TAG, "Complete Bluetooth SPP workflow test completed successfully");
    }
}