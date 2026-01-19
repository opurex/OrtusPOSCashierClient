# Bluetooth Scale Integration Usage Guide

## Overview
This guide explains how to use the Bluetooth scale integration in the OrtusPOS system, including setting up weight-based products, connecting scales, making sales, and printing weights on receipts.

## Setting Up Weight-Based Products

### Creating New Weight-Based Products
1. Navigate to the product management section
2. Create a new product or edit an existing one
3. Set the "Scaled" property to "Yes" or "True"
4. Enter the price per kilogram (kg) in the price field
5. Save the product

### Example Products
- Fresh Apples: Price 350 per kg, Scaled = Yes
- Ground Beef: Price 700 per kg, Scaled = Yes
- Loose Rice: Price 150 per kg, Scaled = Yes

### Verifying Product Setup
1. In the product list, look for the scale icon next to weight-based products
2. Ensure the price is set correctly as price per kg
3. Confirm the "Scaled" flag is enabled

## Connecting the Bluetooth Scale

### Initial Setup
1. Turn on your Aclas Bluetooth scale
2. On your Android device, go to Settings > Bluetooth
3. Pair the scale with your device (follow manufacturer's pairing instructions)
4. Note the scale's MAC address

### Configuring Scale in OrtusPOS
1. Open OrtusPOS application
2. Go to Settings (gear icon)
3. Scroll to "Scale Configuration" section
4. Tap "Select Bluetooth Scale"
5. Choose your scale from the list of paired devices
6. The scale's MAC address will be saved automatically

### Connection Status
- Green checkmark (✓): Scale is connected and ready
- Red X (✗): Scale is disconnected or connection failed
- Status is displayed in the weighing dialog

## Making Sales with Weight-Based Products

### Selling a Weight-Based Product
1. Select a weight-based product from the product catalog
2. The Product Scale Dialog will appear automatically
3. If connected, place the item on the scale
4. Weight will display in real-time (e.g., "Weight: 1.250 kg")
5. Price will calculate automatically (e.g., "Price: 437.50")
6. Press "Ok" to add the item to the ticket

### Manual Weight Entry
1. If scale is not connected, enter weight manually in the input field
2. Weight must be entered in kilograms (kg)
3. Price will update as you type
4. Press "Ok" to add the item to the ticket

### Scale Operations
- **Zero Scale**: Press to reset scale to 0.000 kg
  - Use before weighing to ensure accuracy
- **Tare Scale**: Press to subtract container weight
  - Place empty bag on scale, press Tare, then add items

## Printing Receipts with Weight Information

### Weight Information on Receipts
When a sale includes weight-based products, the receipt will automatically include:
- Product name
- Weight in kg (e.g., "1.250 kg")
- Price per kg (e.g., "350.00/kg")
- Total price for that item

### Example Receipt Format
```
ORTUS POS RECEIPT
=================
Apples (Fresh)     1.250 kg
                   350.00/kg
                   437.50
-------------------------
Bananas            0.750 kg
                   200.00/kg
                   150.00
=========================
TOTAL:             587.50
```

### Enabling Weight Printing
1. The weight information is automatically included on receipts
2. No additional configuration is required
3. Both Bluetooth scale weights and manually entered weights will print

## Troubleshooting Common Issues

### Scale Not Connecting
1. Ensure Bluetooth is enabled on the Android device
2. Verify the scale is paired in device Bluetooth settings
3. Check that the scale is powered on
4. Restart the OrtusPOS application
5. Re-select the scale in Settings > Scale Configuration

### Incorrect Weight Readings
1. Place scale on a stable, level surface
2. Zero the scale before weighing items
3. Ensure no drafts or vibrations affect the scale
4. Check that the scale is calibrated according to manufacturer instructions

### Price Calculation Errors
1. Verify product price is set correctly as price per kg
2. Confirm weight is being read correctly from scale
3. Check that the product is marked as "Scaled"

### Receipt Printing Issues
1. Ensure printer is properly connected and configured
2. Check that the receipt template includes weight fields
3. Verify that weight data is being captured correctly

## Best Practices

### Scale Maintenance
- Regularly clean the scale according to manufacturer guidelines
- Check battery level and replace when needed
- Calibrate the scale periodically for accuracy
- Store in a dry, temperature-controlled environment

### Sales Process
1. Zero the scale before each weighing
2. Place items gently on the scale center
3. Wait for weight to stabilize before finalizing
4. Use Tare function for items in containers
5. Verify price calculation before completing sale

### Product Setup
1. Always set the "Scaled" flag for weight-based products
2. Use consistent pricing units (per kg)
3. Regularly review and update prices as needed
4. Test new products with small weights before full implementation

## Technical Details

### Data Flow
1. Product marked as "Scaled" triggers automatic weighing dialog
2. Bluetooth scale sends weight data in real-time
3. Weight is multiplied by product price per kg
4. Calculated price is added to the ticket
5. Weight and pricing details are stored for receipt printing

### Supported Formats
- Weight: Kilograms (kg) with 3 decimal places
- Price: Local currency with 2 decimal places
- Scale Display: Real-time updating in dialog

### Error Handling
- Connection errors display user-friendly messages
- Invalid weight entries show error indicators
- Scale malfunctions are logged for troubleshooting
- Fallback to manual entry when scale is unavailable

## Testing Your Setup

### Quick Test Procedure
1. Connect your Bluetooth scale
2. Select a weight-based product
3. Place an item on the scale
4. Verify weight displays correctly
5. Confirm price calculates properly
6. Complete a test sale
7. Check that receipt includes weight information

### Verification Points
- [ ] Scale connects automatically when app starts
- [ ] Weight displays in real-time
- [ ] Price updates automatically
- [ ] Zero/Tare functions work
- [ ] Receipt includes weight details
- [ ] Manual entry works when scale disconnected

## Support and Maintenance

### Regular Maintenance Tasks
1. Weekly: Clean scale surface
2. Monthly: Check battery level
3. Quarterly: Calibrate scale
4. Annually: Review product pricing

### When to Contact Support
- Scale won't connect after troubleshooting
- Weight readings are consistently inaccurate
- Receipts don't show weight information
- Price calculations are incorrect

### Updating the System
- Keep the OrtusPOS application updated
- Check for scale firmware updates from manufacturer
- Review this documentation for process changes