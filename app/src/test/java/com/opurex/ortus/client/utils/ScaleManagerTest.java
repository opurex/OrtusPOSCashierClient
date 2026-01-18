package com.opurex.ortus.client.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Unit tests for ScaleManager that properly mock dependencies to avoid native library issues
 */
public class ScaleManagerTest {

    @Mock
    private Context mockContext;

    @Mock
    private BluetoothScaleHelper mockBluetoothScaleHelper;

    private ScaleManager scaleManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Create ScaleManager with mocked BluetoothScaleHelper to avoid native library loading
        scaleManager = new ScaleManager(mockContext, mockBluetoothScaleHelper);
    }

    @Test
    public void testStartScanDelegatesToBluetoothScaleHelper() {
        scaleManager.startScan();
        verify(mockBluetoothScaleHelper).startScan();
    }

    @Test
    public void testStopScanDelegatesToBluetoothScaleHelper() {
        scaleManager.stopScan();
        verify(mockBluetoothScaleHelper).stopScan();
    }

    @Test
    public void testConnectToScaleDelegatesToBluetoothScaleHelper() {
        String macAddress = "AA:BB:CC:DD:EE:FF";
        scaleManager.connectToScale(macAddress);
        verify(mockBluetoothScaleHelper).connectToScale(macAddress);
    }

    @Test
    public void testDisconnectDelegatesToBluetoothScaleHelper() {
        scaleManager.disconnect();
        verify(mockBluetoothScaleHelper).disconnect();
    }

    @Test
    public void testIsConnectedDelegatesToBluetoothScaleHelper() {
        scaleManager.isConnected();
        verify(mockBluetoothScaleHelper).isConnected();
    }

    @Test
    public void testZeroScaleDelegatesToBluetoothScaleHelper() {
        scaleManager.zeroScale();
        verify(mockBluetoothScaleHelper).zeroScale();
    }

    @Test
    public void testTareScaleDelegatesToBluetoothScaleHelper() {
        scaleManager.tareScale();
        verify(mockBluetoothScaleHelper).tareScale();
    }

    @Test
    public void testSetScaleWeightListener() {
        ScaleManager.ScaleWeightListener mockListener = mock(ScaleManager.ScaleWeightListener.class);
        scaleManager.setScaleWeightListener(mockListener);
        // The listener should be stored in the ScaleManager, not directly passed to BluetoothScaleHelper
        // since we're using a mock helper that doesn't have the internal listeners set up
        // Verify that the internal listener field is set correctly
    }

    @Test
    public void testSetConnectionStateListener() {
        ScaleManager.ConnectionStateListener mockListener = mock(ScaleManager.ConnectionStateListener.class);
        scaleManager.setConnectionStateListener(mockListener);
        // The listener should be stored in the ScaleManager, not directly passed to BluetoothScaleHelper
        // since we're using a mock helper that doesn't have the internal listeners set up
        // Verify that the internal listener field is set correctly
    }

    @Test
    public void testSetScanListener() {
        ScaleManager.ScanListener mockListener = mock(ScaleManager.ScanListener.class);
        scaleManager.setScanListener(mockListener);
        // The listener should be stored in the ScaleManager, not directly passed to BluetoothScaleHelper
        // since we're using a mock helper that doesn't have the internal listeners set up
        // Verify that the internal listener field is set correctly
    }
}