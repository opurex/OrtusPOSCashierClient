# How Auxiliary and Network Printers Work in OrtusPOS

## 1. Auxiliary Printers Architecture

### Multi-Device Support
The OrtusPOS system supports up to 3 printers simultaneously:
- **Device Index 0**: Main printer (typically for customer receipts)
- **Device Index 1**: Auxiliary printer 1 (typically for kitchen orders)
- **Device Index 2**: Auxiliary printer 2 (typically for additional locations)

### DeviceService Configuration
- The `DeviceService.getDeviceCount()` method returns 3, indicating support for 1 main + 2 auxiliary printers
- Each device has its own connection status, print queue, and timer
- All devices are managed through the same `DeviceService` with individual indices

### Configuration Interface
- **Main Printer**: Configured via primary printer settings
- **Auxiliary Printers**: Configured separately via "Aux. printer 1" and "Aux. printer 2" in settings
- Each can have different printer types and addresses

## 2. How Auxiliary Printers Work

### Print Method Overloading
- `print(PrintableDocument doc)` - Prints to main printer (device index 0)
- `print(int deviceIndex, PrintableDocument doc)` - Prints to specific device

### UI Integration
- Menu items: `ab_menu_printaux1` and `ab_menu_printaux2` for auxiliary printing
- In Transaction activity, these menu items trigger printing to device index 1 and 2 respectively
- Used for kitchen orders, duplicate receipts, or separate departments

### Use Cases
- **Kitchen Orders**: Print food orders to kitchen printer (aux1)
- **Bar Orders**: Print drinks to bar printer (aux2) 
- **Duplicate Receipts**: Print copies to different locations
- **Department Separation**: Different departments get relevant receipts

## 3. Network Printer Support

### Supported Network Printers
The system supports network-enabled printers through the Epson ePOS SDK:

#### EPSON ePOS Wi-Fi Printers
- **Protocol**: TCP/IP connection
- **Address Format**: IP address (e.g., "192.168.1.100")
- **Connection Type**: `EpsonPrinter.CTX_ETH` (TCP:) or `CTX_BLUETOOTH` (BT:)
- **Models Supported**:
  - TM-T20
  - TM-T70
  - TM-T88V
  - TM-P60
  - TM-U220

### Network Printer Configuration
- **Address Field**: IP address for network printers (not Bluetooth MAC address)
- **Model Selection**: Specific Epson model selection required
- **Connection**: TCP/IP socket connection through Epson ePOS SDK

### Other Network Capabilities
- The app includes network security configuration (`network_security_config.xml`)
- Network state permissions (`ACCESS_NETWORK_STATE`)
- Network connectivity monitoring for server synchronization

## 4. Implementation Details

### DefaultDeviceManager Logic
```java
case "EPSON ePOS IP":
    this.name = "EPSON " + conf.getPrinterModel(deviceIndex) + " " + prAddress;
    // EPSON printers use IP addresses, not Bluetooth addresses
    this.printer = new EpsonPrinter(EpsonPrinter.CTX_ETH, prAddress, conf.getPrinterModel(deviceIndex), this);
    break;
```

### EpsonPrinter Network Connection
- Uses Epson's ePOS SDK for network communication
- Supports Ethernet (TCP) and Bluetooth contexts
- Handles connection, status checking, and data transmission
- Implements `ReceiveListener` for bidirectional communication

## 5. How to Use Network Printers

### Configuration Steps:
1. Go to Settings → Auxiliary Printer 1 or 2
2. Select "EPSON ePOS Wi-Fi" as the driver
3. Enter the printer's IP address (e.g., "192.168.1.100")
4. Select the specific Epson model
5. Save configuration

### Programming Interface:
- Call `deviceService.print(deviceIndex, document)` where deviceIndex is 1 or 2 for aux printers
- The system automatically handles network connection and data transmission
- Print jobs are queued if network is temporarily unavailable

## 6. Benefits of Multi-Printer Support

### Operational Efficiency
- **Parallel Processing**: Multiple receipts/orders printed simultaneously
- **Departmental Separation**: Different printers for different functions
- **Redundancy**: Backup printing if main printer fails

### Network Advantages
- **Wireless Flexibility**: No cables required for network printers
- **Range**: Can place printers anywhere on the network
- **Management**: Centralized network printer management
- **Scalability**: Easy to add more network printers

The system provides a flexible printing infrastructure supporting both Bluetooth and network printers with proper queuing, error handling, and multi-device coordination.