# How the OrtusPOS Application Prints and Saves to PDF

The OrtusPOS application implements a dual-output system that both prints receipts to physical printers and saves copies as PDF files. Here's how the solution works:

## 1. Dual Output Architecture

### Physical Printing Component
- **Printer Interface**: Abstracts different printer types (Bluetooth thermal, ESC/POS, etc.)
- **POSDeviceManager**: Manages communication with hardware devices
- **DeviceService**: Background service handling connection states and print queuing
- **PrintableDocument**: Interface for different document types (receipts, orders, etc.)

### PDF Generation Component  
- **ReceiptBuffer**: Captures formatted receipt lines during printing process
- **ReceiptPdfWriter**: Creates PDF documents using Android's PdfDocument API
- **MediaStore Integration**: Saves PDFs to Documents/OrtusPOS/Receipts directory

## 2. How the Solution Works

### During Receipt Printing:
1. **Simultaneous Processing**: When a receipt is printed, it's processed for both outputs
2. **Buffer Capture**: The `ReceiptBuffer` captures each line as it's formatted for printing
3. **Physical Print**: Sends formatted lines to the connected printer via ESC/POS commands
4. **PDF Creation**: Simultaneously creates a PDF with the same formatted content
5. **File Storage**: Saves PDF to device's Documents/OrtusPOS/Receipts directory

### Technical Implementation:
- **ReceiptDocument.print()** method uses `printAndSave()` wrapper that:
  - Sends line to physical printer via `printer.printLine(line)`
  - Adds same line to `ReceiptBuffer` via `buffer.add(line)`
- After printing completes, `ReceiptPdfWriter.write()` is called with the collected buffer
- PDF is created with dimensions optimized for receipt format (226x1200)

## 3. Key Features

### Physical Printing:
- Bluetooth thermal receipt printer support
- Automatic connection management
- Print queuing when printer is unavailable
- ESC/POS command protocol
- Multiple printer type support (OCPPM083, Woosim, generic Bluetooth)

### PDF Generation:
- Automatic PDF creation during print process
- Monospaced font matching printer output
- Proper receipt formatting preserved
- Organized file storage in dedicated directory
- Timestamped filenames with receipt ID

## 4. Enhanced Support for Weight-Based Products

The system includes special handling for products measured by weight:
- **Weight Display**: Shows weight (e.g., "1.750 kg") on separate line
- **Price Per Unit**: Shows price per kg (e.g., "@250.00/kg") 
- **Total Calculation**: Automatically calculates total based on weight × price-per-unit
- **Consistent Formatting**: Both printed and PDF receipts show weight details identically

## 5. Benefits of This Solution

- **Backup Copies**: PDFs serve as digital backups of all printed receipts
- **Digital Records**: Enables digital record keeping and email delivery
- **Format Consistency**: Printed and PDF versions match exactly
- **Reliability**: If printer fails, PDF records remain intact
- **Compliance**: Helps meet regulatory requirements for digital records
- **Customer Service**: PDFs can be emailed to customers if requested

## 6. File Organization

- **Location**: `/Documents/OrtusPOS/Receipts/`
- **Naming**: `receipt_[receiptId]_[timestamp].pdf`
- **Format**: Standard PDF with monospaced font for receipt-like appearance

This solution ensures that every receipt is both physically printed for the customer and digitally preserved as a PDF for record-keeping, audit trails, and potential reissuance.