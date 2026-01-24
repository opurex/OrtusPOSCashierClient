# How Printing Works in OrtusPOS

The OrtusPOS application has a sophisticated printing system designed to handle receipts, tickets, and other documents. Here's how it works:

## 1. Architecture Overview
- **DeviceService**: A background service that manages all connected devices (printers, scanners, etc.)
- **POSDeviceManager**: Interface that manages communication with specific hardware devices
- **Printer Interface**: Abstracts different printer implementations
- **PrintableDocument**: Represents documents that can be printed (receipts, tickets, etc.)

## 2. Core Components

### Device Service Layer
- Manages connections to multiple devices (primary printer + auxiliary devices)
- Handles connection states (disconnected, connecting, printing, idle)
- Implements queuing mechanism for print jobs when printer is unavailable
- Automatically reconnects when Bluetooth devices connect/disconnect

### Printer Implementations
The system supports multiple printer types:
- **OCPPM083Printer**: For OCPPM083 compatible thermal printers
- **BluetoothPrinter**: Generic ESC/POS compatible printers
- **WoosimPrinter**: For Woosim brand printers
- **PowaPrinter**: For PowaPOS integrated hardware

### Document Formatting
- **ReceiptDocument**: Standard receipt formatting
- **EnhancedReceiptDocument**: Specialized for weight-based products with Bluetooth scales
- **OrderDocument**: For kitchen order slips
- **ZTicketDocument**: For end-of-day reports

## 3. Printing Process Flow

1. **Initiation**: Activities call `printReceipt()` on the base `POSConnectedTrackedActivity`
2. **Queue Management**: `DeviceService.print()` queues documents if printer is not ready
3. **Connection Handling**: Automatically connects/reconnects to printer if needed
4. **Document Rendering**: The `PrintableDocument.print()` method formats content for the printer
5. **Hardware Communication**: Printer implementation sends ESC/POS commands via Bluetooth
6. **Status Reporting**: Events are propagated back through `DeviceManagerEventListener`

## 4. Enhanced Features for Weight-Based Products

The system includes special handling for products measured by weight (like fruits/vegetables from Bluetooth scales):
- **Weight Display**: Shows weight (e.g., "1.750 kg") on separate line
- **Price Per Unit**: Shows price per kg (e.g., "@250.00/kg") 
- **Total Calculation**: Automatically calculates total based on weight × price-per-unit
- **Special Formatting**: Enhanced receipt layout for scaled products

## 5. Key Features

- **Bluetooth Connectivity**: Automatic connection management for Bluetooth thermal printers
- **Print Queue**: Queues documents when printer is offline/disconnecting
- **Error Handling**: Retry mechanisms and error notifications
- **Multi-device Support**: Can print to multiple printers (main + auxiliary)
- **Cash Drawer Integration**: Can open cash drawers connected to printers
- **ESC/POS Commands**: Direct communication with thermal receipt printers

## 6. User Interface Integration

- **Receipt Selection**: `ReceiptSelect.java` allows viewing and reprinting past receipts
- **Receipt Detail View**: `ReceiptDetailActivity.java` shows receipt details with print button
- **Progress Indicators**: Shows printing progress and handles retry scenarios
- **Material Design**: Modern UI with Material Design 3 components

The system is designed to be robust, handling printer connectivity issues gracefully while maintaining print job integrity through its queuing mechanism.