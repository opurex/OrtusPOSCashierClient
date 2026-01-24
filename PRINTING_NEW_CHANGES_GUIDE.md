# How to Print New Changes and Settings in OrtusPOS with ESCPOS

## 1. Using Enhanced ESCPOS Printing Methods

### For Receipts with Enhanced Formatting:
```java
// In any activity that extends POSConnectedTrackedActivity
Receipt receipt = // your receipt object

// Use the new enhanced ESCPOS method for better formatting
if (!this.printReceiptWithEscPos(receipt)) {
    // Handle print failure
    Toast.makeText(this, "Print failed", Toast.LENGTH_SHORT).show();
}
```

### For Simple Receipts with ESCPOS:
```java
// Use simple ESCPOS formatting
if (!this.printSimpleReceiptWithEscPos(receipt)) {
    // Handle print failure
    askReprint(); // or your custom retry method
}
```

## 2. Configuring New Printer Types

### Add ESCPOS Printer Support in Configuration:
The system now supports these new printer types:
- **ESCPOS_BLUETOOTH** - For Bluetooth ESC/POS printers
- **ESCPOS_TCP** - For network/wireless ESC/POS printers

### Update Printer Configuration:
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

## 3. Using ESCPOS Formatting Tags

### Enhanced Receipt Formatting:
The ESCPOS documents support these formatting tags:

#### Text Formatting:
- `[L]` - Left align text
- `[C]` - Center align text  
- `[R]` - Right align text
- `<b>text</b>` - Bold text
- `<u>text</u>` - Underlined text
- `<font size='big'>text</font>` - Larger font

#### Example Usage:
```java
// In your receipt document
printer.printLine("[C]<u><font size='big'>CUSTOMER RECEIPT</font></u>");
printer.printLine("[L]Item Name[R]Price");
printer.printLine("[L]Product A[R]$10.00");
printer.printLine("[C]<barcode type='code128'>123456789012</barcode>");
printer.printLine("[C]<qrcode>ReceiptData</qrcode>");
```

## 4. New Print Methods Available

### In POSConnectedTrackedActivity:
- `printReceiptWithEscPos(Receipt r)` - Enhanced formatting with barcodes/QR codes
- `printSimpleReceiptWithEscPos(Receipt r)` - Basic ESCPOS formatting

### Example Implementation:
```java
public class YourActivity extends POSConnectedTrackedActivity {
    
    private void printEnhancedReceipt(Receipt receipt) {
        // Try ESCPOS enhanced printing first
        if (!printReceiptWithEscPos(receipt)) {
            // Fallback to original printing
            if (!printReceipt(receipt)) {
                // Handle print failure
                showPrintErrorDialog();
            }
        }
    }
}
```

## 5. Printer Discovery and Connection

### For Bluetooth Printers:
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

### Test Enhanced Receipt Printing:
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

### Add New Printer Options in Settings:
In your configuration UI, add options for:
- "ESCPOS Bluetooth Printer"
- "ESCPOS Network Printer"
- Option to select paper width (58mm or 80mm)
- Option to enable/disable barcode printing

## 8. Error Handling for New Features

### Handle ESCPOS-Specific Errors:
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

### Gradual Rollout:
1. Existing users continue using current printers without changes
2. New installations can select ESCPOS printers for enhanced features
3. Provide option to switch to ESCPOS for better receipt quality
4. Maintain backward compatibility with all existing printer types

## 10. Best Practices

### For Optimal Results:
- Use `printReceiptWithEscPos()` for new installations with ESCPOS printers
- Keep `printReceipt()` as fallback for legacy printer types
- Test with actual ESC/POS thermal printers for best results
- Update user documentation to highlight new features
- Provide printer compatibility list for ESCPOS support

The ESCPOS integration provides significantly enhanced printing capabilities while maintaining full backward compatibility with existing systems.