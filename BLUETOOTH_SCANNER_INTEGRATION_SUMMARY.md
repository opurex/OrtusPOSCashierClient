# Bluetooth Scanner Integration Summary

## Overview
This document explains how the Bluetooth scanning functionality works in the Aclas scale integration system.

## Original SDK Implementation
Based on the PSXAndroid_BT_SDK_En_V1.006, the scanning functionality works as follows:

1. **AclasScaler Initialization**: The `AclasScaler` class provides Bluetooth scanning capabilities
2. **Bluetooth Listener**: The `AclasScaler.AclasBluetoothListener` interface handles scanning events
3. **Scanning Methods**:
   - `startScanBluetooth(true)` - Starts the Bluetooth scanning process
   - `onSearchBluetooth(String s)` - Called when a device is found (format: "name,mac,signal")
   - `onSearchFinish()` - Called when scanning completes

## Integration Flow
The complete flow from UI to Bluetooth scanning:

1. **UI Layer** (e.g., BluetoothScaleSelectionActivity) calls ScaleManager methods
2. **ScaleManager** registers its scan listener via `setScanListener()` 
3. **BluetoothScaleHelper** receives AclasScaler callbacks and forwards them to the registered listener
4. **UI receives events** through the listener's `onDeviceFound()` and `onScanFinished()` methods

## Key Components
- `ScaleManager.ScanListener` - Interface for receiving scan events in the UI
- `BluetoothScaleHelper.ScanListener` - Internal interface for forwarding events
- `AclasScaler.AclasBluetoothListener` - Original SDK interface for Bluetooth events
- `setScanListener()` method - Connects the UI layer to the scanning system

## Device Discovery Process
1. User initiates scan (calls `startScan()`)
2. AclasScaler discovers nearby Bluetooth devices
3. For each device found, `onSearchBluetooth(String s)` is called with "name,mac,signal" format
4. BluetoothScaleHelper parses the string and calls `scanListener.onDeviceFound(name, mac, signal)`
5. When scanning completes, `onSearchFinish()` is called and forwarded as `scanListener.onScanFinished()`

This integration allows the OrtusPOS system to discover and pair with Aclas Bluetooth scales through the standard Android Bluetooth APIs.