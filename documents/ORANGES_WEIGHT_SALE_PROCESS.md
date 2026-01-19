# How to Use Aclas Bluetooth Scale for Weighing Oranges and Making Sales

## Overview
This guide explains the complete process of using an Aclas Bluetooth scale to weigh oranges (or any weight-based product), make sales, record the transaction, and print receipts with weight information.

## 1. Setup and Pairing Process

### 1.1 Initial Hardware Setup
1. **Power on the Aclas Scale**:
   - Press and hold the power button until the display lights up
   - Wait for the scale to complete its startup sequence

2. **Put Scale in Pairing Mode**:
   - Look for the Bluetooth symbol on the scale display
   - If not visible, press the "Mode" or "Function" button until Bluetooth pairing mode is activated
   - The scale will show "PAIRING" or similar indication

### 1.2 Pair Scale with Android Device
1. **On Your Android POS Device**:
   - Open "Settings"
   - Tap "Bluetooth"
   - Ensure Bluetooth is turned ON
   - Tap "Scan" or wait for device discovery

2. **Complete Pairing**:
   - Look for the Aclas scale in the available devices list (e.g., "Aclas-FSC-2000")
   - Tap on the scale name to initiate pairing
   - If prompted for a PIN, enter "0000" or "1234" (common defaults)
   - Wait for "Paired" confirmation message
   - Note the MAC address for future reference (e.g., "AA:BB:CC:DD:EE:FF")

## 2. Configure Scale in OrtusPOS

### 2.1 Access Settings
1. Open the OrtusPOS application
2. Tap the gear icon (⚙️) in the top-right corner to access Settings
3. Scroll down to find "Scale Configuration" section

### 2.2 Select Bluetooth Scale
1. Tap "Select Bluetooth Scale"
2. From the list of paired devices, select your Aclas scale
3. The system will automatically save the scale's MAC address
4. You should see a confirmation message like "Scale configured successfully"

### 2.3 Verify Connection
1. The system will attempt to connect to the scale automatically
2. Look for connection status indicators:
   - Green checkmark (✓): Connected and ready
   - Red X (✗): Disconnected or connection failed

## 3. Setting Up Weight-Based Products

### 3.1 Create Weight-Based Product (Oranges)
1. Navigate to Product Management in OrtusPOS
2. Create a new product or edit existing one:
   - **Product Name**: "Oranges (Fresh)"
   - **Price**: 250.00 (per kg)
   - **Scaled**: Set to "Yes" or check the "Sold by Weight" box
   - **Barcode**: Optional (if you want to scan)
   - **Category**: "Fruits" or appropriate category
   - **Tax**: As applicable

### 3.2 Verify Product Setup
1. Check that the product shows a scale icon in the product list
2. Confirm price is set as per kg (not total price)
3. Ensure "Scaled" flag is enabled

## 4. Weighing Oranges and Making Sales

### 4.1 Start a New Sale
1. Open the main POS screen
2. Select a customer (or use "Walk-in Customer")
3. Begin adding products to the ticket

### 4.2 Weigh Oranges
1. **Select the Oranges Product**:
   - Tap on "Oranges (Fresh)" from your product catalog
   - The Product Scale Dialog will appear automatically

2. **Place Oranges on Scale**:
   - Put the oranges on the Aclas scale platform
   - Ensure they're centered and stable
   - Wait for weight to stabilize (scale will show steady reading)

3. **Observe Automatic Updates**:
   - Weight display: "Weight: 1.250 kg" (example)
   - Price calculation: "Price: 312.50" (1.250 kg × 250.00/kg)
   - Status indicator turns green when connected

### 4.3 Scale Operations
- **Zero Scale**: Tap "Zero Scale" button to reset to 0.000 kg
  - Use before weighing for accuracy
- **Tare Scale**: Tap "Tare Scale" to subtract container weight
  - Place empty bag on scale, press Tare, then add oranges
  - Scale shows net weight only

### 4.4 Complete the Transaction
1. **Verify Information**:
   - Confirm weight is correct (e.g., 1.250 kg)
   - Check calculated price (e.g., 312.50 Ksh)
   - Review any discounts or modifications

2. **Add to Ticket**:
   - Press "Ok" to add the weighed item to the ticket
   - The item will appear on the ticket with calculated price

3. **Continue Shopping** (if needed):
   - Add more items using the same process
   - Mix of weight-based and regular products works seamlessly

4. **Process Payment**:
   - Tap "Checkout" or payment button
   - Select payment method (Cash, Card, M-Pesa, etc.)
   - Complete the transaction

## 5. Data Recording and Storage

### 5.1 Automatic Data Storage
When you complete the sale, the system automatically records:

1. **Ticket Information**:
   - Ticket number and timestamp
   - Cash register used
   - Staff member (if logged in)

2. **Product Details**:
   - Product ID and name ("Oranges (Fresh)")
   - Weight in kg (1.250)
   - Calculated price (312.50 Ksh)
   - Tax information

3. **Scale Usage** (in enhanced system):
   - Scale MAC address used
   - Timestamp of weighing
   - Transaction correlation

### 5.2 Database Storage Format
The data is stored in the system as:
```sql
-- Ticket Line Record
ticketlines: {
  ticket_id: 12345,
  product_id: 678,           -- Oranges product ID
  quantity: 1.250,           -- Weight in kg
  unitprice: 250.00,         -- Price per kg
  finaltaxedprice: 312.50,   -- Total price
  productlabel: "Oranges (Fresh)"
}
```

## 6. Receipt Printing with Weight Information

### 6.1 Automatic Weight Printing
When you print the receipt, weight-based products show:

```
        ORTUS POS RECEIPT
        =================
Date:     2025-09-13 14:30
Customer: Walk-in Customer
Number:   Cash Register - 001025

Item              Price     Total
--------------------------------
Oranges (Fresh)
  1.250 kg
  @250.00/kg
             312.50

Bread             1         120.00
--------------------------------
Subtotal:            432.50 Ksh
Total:               432.50 Ksh
Tax:                  32.50 Ksh

Cash                  500.00 Ksh
Change                 67.50 Ksh

    Thank you for shopping!
    Powered by OrtusPOS
```

### 6.2 Printing Process
1. After completing payment, tap "Print Receipt"
2. System automatically sends to configured printer
3. Weight information prints automatically for scaled products
4. Regular products print in standard format

## 7. Daily Operations and Best Practices

### 7.1 Daily Startup Routine
1. Power on Aclas scale
2. Verify Bluetooth connection in OrtusPOS
3. Test with sample item before first customer
4. Check receipt printer paper and ink

### 7.2 Weighing Best Practices
1. **Before Each Weighing**:
   - Press "Zero Scale" to ensure accuracy
   - Clean scale platform if needed

2. **During Weighing**:
   - Place items gently on center of platform
   - Wait for weight to stabilize (no bouncing numbers)
   - Keep scale away from drafts or vibrations

3. **For Container Weighing**:
   - Place empty container on scale
   - Press "Tare Scale"
   - Add items to container
   - Scale shows net weight only

### 7.3 Troubleshooting Common Issues

#### Scale Not Connecting
1. Check that scale is powered on
2. Verify scale is paired in device Bluetooth settings
3. Restart OrtusPOS application
4. Re-select scale in Settings > Scale Configuration

#### Incorrect Weight Readings
1. Ensure scale is on stable, level surface
2. Zero the scale before weighing
3. Check for environmental interference (fans, AC, etc.)
4. Clean scale platform

#### Price Calculation Errors
1. Verify product is marked as "Scaled"
2. Confirm price is set per kg (not total)
3. Check that weight is reading correctly

## 8. Reporting and Analytics

### 8.1 Accessing Weight Reports
1. In Backoffice system, navigate to "Reports"
2. Select "Weight-Based Product Reports"
3. Choose date range (e.g., Today, This Week, Custom)
4. Generate report to see:

**Sample Report Data**:
| Product | Total Weight (kg) | Transactions | Revenue (Ksh) | Avg Weight/Trans |
|---------|------------------|--------------|---------------|------------------|
| Oranges (Fresh) | 25.750 kg | 42 | 6,437.50 | 0.613 kg |
| Apples (Fresh) | 18.300 kg | 31 | 6,405.00 | 0.590 kg |
| Bananas | 12.450 kg | 28 | 2,490.00 | 0.445 kg |

### 8.2 Key Metrics Tracked
- **Total weight sold** by product per day/week/month
- **Average transaction size** in weight and value
- **Peak selling hours** for weight-based products
- **Revenue contribution** from weight-based items
- **Scale utilization** statistics

## 9. Example Complete Transaction

### Scenario: Customer Buys Oranges
1. **Setup**: Scale is paired and connected
2. **Product**: "Oranges (Fresh)" at 250.00 Ksh/kg
3. **Weighing**: Customer places oranges on scale
4. **Reading**: Scale shows 1.750 kg
5. **Calculation**: 1.750 kg × 250.00 = 437.50 Ksh
6. **Add to Ticket**: Press "Ok" to add item
7. **Payment**: Customer pays 500.00 Ksh cash
8. **Change**: 62.50 Ksh returned
9. **Receipt**: Prints automatically with weight details
10. **Recording**: Data stored in database for reporting

### Receipt Output:
```
Oranges (Fresh)
  1.750 kg
  @250.00/kg
             437.50
```

### Database Entry:
```sql
INSERT INTO ticketlines (
  ticket_id, product_id, quantity, unitprice, finaltaxedprice
) VALUES (
  12346, 678, 1.750, 250.00, 437.50
);
```

## 10. Maintenance and Support

### 10.1 Regular Maintenance
- **Daily**: Clean scale platform, check battery
- **Weekly**: Zero calibration check
- **Monthly**: Deep cleaning, battery replacement if needed
- **Annually**: Professional calibration service

### 10.2 When to Contact Support
- Scale won't connect after troubleshooting
- Consistent incorrect weight readings
- Receipts missing weight information
- System errors during weighing process

This comprehensive system ensures accurate weighing, proper pricing, detailed recording, and professional receipt printing for all weight-based product sales.