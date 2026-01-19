# Complete Guide: Using Bluetooth Scales with OrtusPOS

## Table of Contents
1. Overview
2. Initial Setup
3. Product Configuration
4. Scale Connection
5. Making Sales
6. Receipt Printing
7. Troubleshooting
8. Best Practices

## 1. Overview

This guide explains how to use Bluetooth scales with the OrtusPOS system to streamline sales of weight-based products like fresh produce, meat, and bulk items. The integration provides automatic weight detection, real-time price calculation, and detailed receipt printing.

### Benefits
- Eliminates manual weight entry errors
- Speeds up checkout for weight-based items
- Provides accurate pricing automatically
- Prints weight details on receipts
- Improves customer experience

## 2. Initial Setup

### Hardware Requirements
- Aclas Bluetooth scale (or compatible model)
- Android device with Bluetooth capability
- OrtusPOS application installed

### Bluetooth Scale Setup
1. Turn on your Bluetooth scale
2. On your Android device, go to Settings > Bluetooth
3. Put the scale in pairing mode (refer to scale manual)
4. Select the scale from the available devices list
5. Complete the pairing process
6. Note the scale's MAC address for reference

### OrtusPOS Configuration
1. Open the OrtusPOS application
2. Tap the gear icon to access Settings
3. Scroll down to "Scale Configuration"
4. Tap "Select Bluetooth Scale"
5. Choose your scale from the list of paired devices
6. The system will save the scale's MAC address automatically

## 3. Product Configuration

### Creating Weight-Based Products
1. Navigate to the product management section
2. Create a new product or edit an existing one
3. Set the following properties:
   - **Label**: Product name (e.g., "Apples (Fresh)")
   - **Price**: Price per kilogram (e.g., 350.00)
   - **Scaled**: Set to "Yes" or "True"
   - **Barcode**: Optional, for scanning
   - **Category**: As appropriate
4. Save the product

### Example Products
| Product Name | Price per kg | Scaled |
|--------------|--------------|--------|
| Apples (Fresh) | 350.00 | Yes |
| Ground Beef | 700.00 | Yes |
| Loose Rice | 150.00 | Yes |
| Bananas | 200.00 | Yes |

### Verifying Product Setup
1. In the product catalog, look for the scale icon next to products
2. Ensure prices are set as per kg values
3. Confirm the "Scaled" flag is enabled
4. Test with small transactions before full implementation

## 4. Scale Connection

### Connection Status Indicators
- **Green Checkmark (✓)**: Scale is connected and ready
- **Red X (✗)**: Scale is disconnected or connection failed
- Status displays automatically in the weighing dialog

### Automatic Connection
1. The system attempts to connect to the scale when the app starts
2. Connection status shows in the weighing dialog
3. If disconnected, the system falls back to manual entry

### Manual Reconnection
1. If the scale disconnects, go to Settings
2. Tap "Select Bluetooth Scale" again
3. Choose your scale from the list
4. Connection should re-establish automatically

## 5. Making Sales

### Selling Weight-Based Products
1. Select a weight-based product from the catalog
2. The Product Scale Dialog appears automatically
3. If connected:
   - Place the item on the scale
   - Weight displays in real-time (e.g., "Weight: 1.250 kg")
   - Price calculates automatically (e.g., "Price: 437.50")
4. If not connected:
   - Enter weight manually in kilograms
   - Price updates as you type
5. Press "Ok" to add the item to the ticket

### Scale Operations
- **Zero Scale**: Resets scale to 0.000 kg
  - Use before weighing for accuracy
  - Tap the "Zero Scale" button in the dialog
- **Tare Scale**: Subtracts container weight
  - Place empty container on scale
  - Press "Tare Scale" button
  - Add items to container
  - Scale shows net weight only

### Example Transaction
1. Select "Apples (Fresh)" (350.00/kg, Scaled: Yes)
2. Product Scale Dialog opens
3. Place apples on scale
4. Scale shows "Weight: 1.250 kg"
5. Dialog shows "Price: 437.50"
6. Press "Ok"
7. Item appears on ticket with calculated price

### Manual Weight Entry
1. If scale is disconnected, enter weight manually
2. Use decimal format (e.g., 1.250 for 1.25 kg)
3. Price updates automatically as you type
4. Press "Ok" to add to ticket

## 6. Receipt Printing

### Weight Information on Receipts
When a sale includes weight-based products, receipts automatically include:
- Product name
- Weight in kg with 3 decimal places
- Price per kg
- Total price for that item

### Example Receipt Format
```
        ORTUS POS RECEIPT
        =================
Date:     2025-09-13 10:30
Customer: Walk-in Customer
Number:   Cash Register - 001025

Item              Price     Total
--------------------------------
Apples (Fresh)
  1.250 kg
  @350.00/kg
             437.50

Bananas
  0.750 kg
  @200.00/kg
             150.00

Bread             1         120.00
--------------------------------
Discount: 0%
Subtotal:            707.50 Ksh
Total:               707.50 Ksh
Tax:                  50.00 Ksh

Cash                  800.00 Ksh
Change                 92.50 Ksh

Prepaid Amount:       0.00 Ksh
Balance:              0.00 Ksh

    Thank you for shopping!
    Powered by OrtusPOS
```

### Non-Weight Product Format (For Comparison)
```
Bread             1         120.00
Milk              2         200.00
```

### Enabling Weight Printing
1. Weight information prints automatically for scaled products
2. No additional configuration required
3. Works with both Bluetooth scale weights and manual entries

## 7. Troubleshooting

### Common Issues and Solutions

#### Scale Won't Connect
1. **Check Bluetooth**: Ensure device Bluetooth is enabled
2. **Verify Pairing**: Confirm scale is paired in device settings
3. **Power Status**: Check that scale is turned on
4. **Restart App**: Close and reopen OrtusPOS
5. **Re-select Scale**: Go to Settings > Scale Configuration and re-select

#### Incorrect Weight Readings
1. **Stable Surface**: Place scale on level, vibration-free surface
2. **Zero Scale**: Press "Zero Scale" before weighing
3. **Environmental Factors**: Avoid drafts, temperature changes
4. **Calibration**: Follow manufacturer's calibration instructions

#### Price Calculation Errors
1. **Product Setup**: Verify product is marked as "Scaled"
2. **Price Format**: Confirm price is set as per kg (not total)
3. **Weight Accuracy**: Check that weight is reading correctly
4. **System Update**: Ensure app is running latest version

#### Receipt Printing Issues
1. **Printer Connection**: Verify printer is properly connected
2. **Template Check**: Ensure receipt template supports weight fields
3. **Data Capture**: Confirm weight data is being recorded
4. **Test Print**: Try printing a test receipt

#### Status Indicators Not Working
1. **Permissions**: Check Bluetooth permissions for the app
2. **System Resources**: Restart device if app is unresponsive
3. **Scale Firmware**: Update scale to latest firmware
4. **Contact Support**: If issues persist, contact technical support

## 8. Best Practices

### Scale Maintenance
- Clean scale regularly according to manufacturer guidelines
- Check battery levels weekly
- Calibrate monthly or as needed
- Store in dry, temperature-controlled environment

### Sales Process
1. Zero scale before each weighing
2. Place items gently on center of scale platform
3. Wait for weight to stabilize before finalizing
4. Use Tare function for items in containers
5. Verify price calculation before completing sale

### Product Management
- Regularly review and update prices
- Ensure all weight-based products are marked "Scaled"
- Use consistent naming conventions
- Test new products with small transactions

### Customer Service
- Explain weight-based pricing to customers
- Demonstrate real-time weight display
- Offer manual entry option if scale issues occur
- Provide clear receipts with weight details

### Data Management
- Regular backup of product database
- Monitor sales reports for pricing accuracy
- Track inventory based on weight sold
- Review discount applications

## Technical Specifications

### Supported Formats
- **Weight**: Kilograms (kg) with 3 decimal places
- **Price**: Local currency with 2 decimal places
- **Scale Display**: Real-time updating in dialog

### Data Flow
1. Product marked "Scaled" triggers weighing dialog
2. Bluetooth scale sends real-time weight data
3. Weight multiplies by product price per kg
4. Calculated price adds to ticket
5. Weight and pricing details store for receipt

### Error Handling
- Connection failures show user-friendly messages
- Invalid weights display error indicators
- Scale malfunctions log for troubleshooting
- Manual entry available when scale disconnected

## Support Information

### Regular Maintenance Tasks
- **Weekly**: Clean scale surface
- **Monthly**: Check battery levels
- **Quarterly**: Calibrate scale
- **Annually**: Review product pricing

### When to Contact Support
- Scale won't connect after troubleshooting
- Persistent incorrect weight readings
- Receipts missing weight information
- Price calculation discrepancies

### System Updates
- Keep OrtusPOS application current
- Check for scale firmware updates
- Review documentation for changes
- Test integrations after updates

This guide provides comprehensive information for setting up and using Bluetooth scales with the OrtusPOS system. Following these instructions will help ensure accurate, efficient processing of weight-based products and excellent customer service.