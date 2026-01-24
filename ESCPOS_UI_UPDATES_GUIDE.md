# Updating OrtusPOS to Use ESCPOS Features

## 1. Configuration Updates

### Update Printer Driver Options
Add new printer driver options in the configuration UI to allow users to select ESCPOS printers:

In the configuration screen, add these new options to the printer driver selection:
- "ESCPOS Bluetooth" - For Bluetooth ESC/POS printers
- "ESCPOS TCP" - For network/wireless ESC/POS printers

## 2. Update ReceiptSelect Activity

Modify the ReceiptSelect.java to use ESCPOS documents when appropriate:

```java
// In ReceiptSelect.java, update the print method
private void print(final Receipt receipt) {
    showProgressDialog();
    
    // Check if ESCPOS printer is configured and use enhanced document
    if (isEscPosPrinterConfigured()) {
        // Use ESCPOS enhanced document for better formatting
        if (!this.printReceiptWithEscPos(receipt)) {
            this.askReprint();
        }
    } else {
        // Fallback to original printing
        if (!this.printReceipt(receipt)) {
            this.askReprint();
        }
    }
}

private boolean isEscPosPrinterConfigured() {
    // Check if current printer configuration uses ESCPOS
    String printerDriver = OrtusPOS.getConfiguration().getPrinterDriver(0);
    return "ESCPOS_BLUETOOTH".equals(printerDriver) || "ESCPOS_TCP".equals(printerDriver);
}
```

## 3. Update ReceiptDetailActivity

Similarly, update the ReceiptDetailActivity to use ESCPOS when appropriate:

```java
// In ReceiptDetailActivity.java, update the printReceipt method
private void printReceipt() {
    if (receipt == null) return;
    
    if (isEscPosPrinterConfigured()) {
        if (!super.printReceiptWithEscPos(receipt)) {
            askReprint();
        }
    } else {
        if (!super.printReceipt(receipt)) {
            askReprint();
        }
    }
}
```

## 4. Enhanced Features Available

### Text Formatting
- Bold text: `[L]<b>Text</b>`
- Underlined text: `[L]<u>Text</u>`
- Large fonts: `[L]<font size='big'>Text</font>`
- Centered text: `[C]Text`
- Right-aligned text: `[R]Text`
- Left-aligned text: `[L]Text`

### Barcodes
- Add barcodes to receipts: `[C]<barcode type='code128'>123456789012</barcode>`
- Supported types: EAN8, EAN13, UPC_A, UPC_E, CODE39, CODE128

### QR Codes
- Add QR codes for receipt verification: `[C]<qrcode>ReceiptData</qrcode>`

### Images
- Print logos and images: `[C]<img>bitmap_object</img>`

## 5. Configuration Changes

### Add New Printer Types
In the configuration UI, add options for:
- ESCPOS Bluetooth printers
- ESCPOS TCP/IP printers

### Update Printer Address Fields
- For Bluetooth: Standard MAC address format (AA:BB:CC:DD:EE:FF)
- For TCP: IP address and port (192.168.1.100:9100)

## 6. Testing the Implementation

### Basic Functionality
1. Configure an ESCPOS Bluetooth printer
2. Process a sale
3. Print a receipt - verify enhanced formatting
4. Check that barcodes/QR codes appear correctly

### Edge Cases
1. Test with different paper widths (58mm, 80mm)
2. Test with various ESC/POS compliant printers
3. Verify fallback behavior when ESCPOS isn't available

## 7. Benefits of ESCPOS Integration

- **Enhanced Formatting**: Better text formatting with bold, underline, and sizing options
- **Barcode Support**: Built-in barcode generation for receipts
- **QR Code Support**: QR codes for receipt verification
- **Improved Layout**: Better column alignment and spacing
- **Reliability**: More robust Bluetooth and network connections
- **Compatibility**: Works with most ESC/POS compliant thermal printers

## 8. Migration Path

For existing installations:
1. Deploy the updated APK with ESCPOS support
2. Users can continue using existing printer configurations
3. New installations can select ESCPOS printer types for enhanced features
4. Gradual rollout of enhanced receipt formatting

This implementation maintains full backward compatibility while adding powerful new printing capabilities through the ESCPOS library.