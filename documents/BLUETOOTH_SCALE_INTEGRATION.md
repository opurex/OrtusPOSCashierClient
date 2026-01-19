# Bluetooth Scale Integration Documentation

## Overview
This document explains how to set up and use Bluetooth scale integration in the OrtusPOS Android application. The integration allows automatic weight detection from compatible Bluetooth scales, streamlining the checkout process for weight-based products.

## Supported Scales
The current implementation supports Aclas Bluetooth scales using the AclasScaleSdk.

## Setup Instructions

### 1. Hardware Setup
1. Ensure your Aclas Bluetooth scale is powered on
2. Pair the scale with your Android device through the device's Bluetooth settings
3. Note the MAC address of the scale (can be found in Bluetooth settings)

### 2. Software Configuration
1. Open the OrtusPOS application
2. Navigate to Settings > Scale Configuration
3. Tap "Select Bluetooth Scale"
4. Choose your scale from the list of paired devices
5. The scale's MAC address will be automatically saved

### 3. Testing the Connection
1. After configuration, the application will automatically attempt to connect to the scale
2. Connection status is displayed in the scale dialog when weighing products
3. Green checkmark indicates successful connection
4. Red X indicates disconnected or failed connection

## Usage Instructions

### Weighing Products
1. Select a weight-based product in the POS interface
2. The Product Scale Dialog will appear
3. If connected, the scale will automatically detect weight
4. Weight is displayed in real-time in the dialog
5. Price is automatically calculated based on the product's price per kg
6. Enter weight manually if the scale is not connected
7. Press "Ok" to add the product to the ticket

### Scale Operations
- **Zero Scale**: Resets the scale to zero
- **Tare Scale**: Subtracts the weight of a container (e.g., bag) from the total weight

## Troubleshooting

### Common Issues
1. **Scale not connecting**
   - Ensure Bluetooth is enabled on the device
   - Verify the scale is paired with the device
   - Check that the scale is powered on
   - Restart the application

2. **Incorrect weight readings**
   - Ensure the scale is on a stable, level surface
   - Zero the scale before weighing
   - Check that the scale is calibrated properly

3. **Price calculation errors**
   - Verify the product's price per kg is set correctly in the product database
   - Check that the weight is being read correctly from the scale

### Error Messages
- "Bluetooth not supported": The device does not have Bluetooth capability
- "Bluetooth not enabled": Enable Bluetooth in device settings
- "No scale address configured": Configure the scale in Settings > Scale Configuration
- "Failed to connect to scale": Check that the scale is powered on and paired

## Technical Implementation

### Architecture
The integration uses a layered approach:
1. **BluetoothScaleHelper**: Low-level Bluetooth communication with the scale
2. **ScaleManager**: High-level scale management and integration with the POS
3. **ProductScaleDialog**: User interface for weighing products

### Key Classes
- `BluetoothScaleHelper`: Handles Bluetooth connection and data reception
- `ScaleManager`: Manages scale operations and integrates with the POS system
- `ProductScaleDialog`: Provides the user interface for weighing products

### Data Flow
1. Scale connects via Bluetooth
2. Weight data is received and parsed
3. Weight is automatically entered in the scale dialog
4. Price is calculated based on product pricing
5. Product is added to the ticket with the calculated price

## Maintenance
- Regularly check the scale's battery level
- Clean the scale according to manufacturer's instructions
- Calibrate the scale as needed
- Update the application to ensure compatibility with scale firmware