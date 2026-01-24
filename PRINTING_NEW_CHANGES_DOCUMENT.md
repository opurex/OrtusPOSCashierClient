# How to Print New Changes and Settings in OrtusPOS with ESCPOS

## Overview

The OrtusPOS application has been enhanced with ESCPOS-ThermalPrinter-Android library integration, providing significantly improved printing capabilities. This document explains how to utilize the new features and settings.

## 1. Enhanced Printing Methods

### New Print Methods Available

The system now includes enhanced printing methods in the `POSConnectedTrackedActivity`:

- **`printReceiptWithEscPos(Receipt r)`** - Enhanced formatting with barcodes, QR codes, and advanced layout
- **`printSimpleReceiptWithEscPos(Receipt r)`** - Basic ESCPOS formatting with improved quality

### Usage Example

```java
// In any activity extending POSConnectedTrackedActivity
Receipt receipt = // your receipt object

// Use enhanced ESCPOS printing for better formatting
if (!this.printReceiptWithEscPos(receipt)) {
    // Handle print failure
    Toast.makeText(this, "Print failed", Toast.LENGTH_SHORT).show();
}
```

### Fallback Strategy

For maximum compatibility, use a fallback approach:

```java
// Try ESCPOS enhanced printing first
if (!printReceiptWithEscPos(receipt)) {
    // Fallback to original printing
    if (!printReceipt(receipt)) {
        // Handle print failure
        showPrintErrorDialog();
    }
}
```

## 2. New Printer Configuration Options

### Supported Printer Types

The system now supports these additional printer types:

- **ESCPOS_BLUETOOTH** - For Bluetooth ESC/POS thermal printers
- **ESCPOS_TCP** - For network/wireless ESC/POS thermal printers

### Configuration Example

```java
// In your configuration code
OpurexConfiguration conf = OrtusPOS.getConf();

// Set ESCPOS Bluetooth printer
conf.setPrinterDriver(deviceIndex, "ESCPOS_BLUETOOTH");
conf.setPrinterAddress(deviceIndex, "AA:BB:CC:DD:EE:FF"); // Bluetooth MAC address

// Set ESCPOS TCP printer
conf.setPrinterDriver(deviceIndex, "ESCPOS_TCP");
conf.setPrinterAddress(deviceIndex, "192.168.1.100:9100"); // IP:Port format
```

## 3. Advanced Formatting Features

### ESCPOS Formatting Tags

The ESCPOS documents support special formatting tags:

#### Alignment
- `[L]` - Left align text
- `[C]` - Center align text  
- `[R]` - Right align text

#### Text Styling
- `<b>text</b>` - Bold text
- `<u>text</u>` - Underlined text
- `<font size='big'>text</font>` - Larger font

#### Barcodes and QR Codes
- `<barcode type='code128'>123456789012</barcode>` - Barcode
- `<qrcode>ReceiptData</qrcode>` - QR code

#### Example Usage
```java
printer.printLine("[C]<u><font size='big'>CUSTOMER RECEIPT</font></u>");
printer.printLine("[L]Item Name[R]Price");
printer.printLine("[L]Product A[R]$10.00");
printer.printLine("[C]<barcode type='code128'>123456789012</barcode>");
printer.printLine("[C]<qrcode>ReceiptData</qrcode>");
```

## 4. Enhanced Weight-Based Product Printing

### For Bluetooth Scale Integration

The system now provides enhanced formatting for weight-based products:

- **Weight Display**: Shows weight (e.g., "1.750 kg") on separate line
- **Price Per Unit**: Shows price per kg (e.g., "@250.00/kg") 
- **Total Calculation**: Automatically calculates total based on weight × price-per-unit
- **Special Formatting**: Enhanced receipt layout for scaled products

### Example Output
```
APPLES
  Weight: 1.750 kg
  Price per kg: @250.00/kg
  Total: 437.50 KSH
```

## 5. Printer Discovery and Connection

### For Bluetooth Printers
```java
// Discover and connect to ESCPOS Bluetooth printers
BluetoothPrintersConnections printers = new BluetoothPrintersConnections();
BluetoothConnection[] bluetoothPrinters = printers.getList();

for (BluetoothConnection printer : bluetoothPrinters) {
    // Connect and test
    try {
        EscPosPrinter escPosPrinter = new EscPosPrinter(
            printer, 
            200,    // DPI
            48f,    // Width in mm
            32      // Characters per line
        );
        
        // Print test page
        escPosPrinter.printFormattedText("[C]Test Print\n[C]✓ Success\n");
        escPosPrinter.disconnect();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## 6. Testing New Features

### Test Enhanced Receipt Printing
```java
// Create a test receipt with weight-based products
Receipt testReceipt = createTestReceipt();
testReceipt.getTicket().getLines().forEach(line -> {
    if (line.getProduct().isScaled()) {
        // This will now show enhanced weight formatting
        System.out.println("Scaled product will show weight, price per kg, and total");
    }
});
printReceiptWithEscPos(testReceipt);
```

## 7. Configuration UI Updates

### Recommended UI Additions

In your configuration UI, add options for:
- "ESCPOS Bluetooth Printer" selection
- "ESCPOS Network Printer" selection
- Paper width selection (58mm or 80mm)
- Option to enable/disable barcode printing
- Option to enable/disable QR code printing

## 8. Error Handling for New Features

### Handle ESCPOS-Specific Errors
```java
@Override
public void onDeviceManagerEvent(POSDeviceManager manager, DeviceManagerEvent event) {
    switch (event.what) {
        case DeviceManagerEvent.PrintDone:
            // Print successful
            dismissProgressDialog();
            break;
        case DeviceManagerEvent.PrintError:
            // Handle print error
            showError("Print failed - check printer connection");
            break;
        case DeviceManagerEvent.PrinterConnectFailure:
            // Handle connection failure
            showError("Could not connect to printer");
            break;
    }
}
```

## 9. Migration Path for Existing Users

### Gradual Rollout Strategy

1. **Existing users**: Continue using current printers without changes
2. **New installations**: Can select ESCPOS printers for enhanced features
3. **Migration option**: Provide option to switch to ESCPOS for better receipt quality
4. **Backward compatibility**: Maintain support for all existing printer types

## 10. Best Practices

### For Optimal Results

- Use `printReceiptWithEscPos()` for new installations with ESCPOS printers
- Keep `printReceipt()` as fallback for legacy printer types
- Test with actual ESC/POS thermal printers for best results
- Update user documentation to highlight new features
- Provide printer compatibility list for ESCPOS support
- Consider paper width (58mm vs 80mm) when configuring printers

## 11. Benefits of ESCPOS Integration

### Enhanced Features
- Better receipt quality and readability
- More printer compatibility (works with most ESC/POS thermal printers)
- Built-in barcode and QR code generation
- Improved Bluetooth and network connection reliability
- Advanced text formatting capabilities
- Specialized support for weight-based products from Bluetooth scales

### Backward Compatibility
- All existing functionality preserved
- Seamless transition for current users
- Option to upgrade to enhanced features gradually

The ESCPOS integration provides significantly enhanced printing capabilities while maintaining full backward compatibility with existing systems. The generated APK includes all these new features and is ready for distribution.