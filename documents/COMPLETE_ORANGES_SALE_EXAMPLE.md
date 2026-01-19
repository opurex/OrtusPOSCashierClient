# Complete Example: Using Paired Aclas Scale to Weigh Oranges

## Overview
This document provides a step-by-step example of how to check if an Aclas scale is paired, use it to weigh oranges, process the sale, record data, and print a receipt with weight information.

## 1. Checking Scale Pairing Status

### 1.1 Verify Bluetooth Pairing
```java
// In BluetoothScaleHelper.java - Method to check pairing status
public boolean isScalePaired(String scaleMacAddress) {
    if (bluetoothAdapter == null) {
        Log.e(TAG, "Bluetooth not supported");
        return false;
    }
    
    // Check BLUETOOTH_CONNECT permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "BLUETOOTH_CONNECT permission not granted");
            return false;
        }
    }
    
    try {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(scaleMacAddress)) {
                Log.d(TAG, "Scale is paired: " + device.getName() + " (" + device.getAddress() + ")");
                return true;
            }
        }
        Log.d(TAG, "Scale with MAC " + scaleMacAddress + " is not paired");
        return false;
    } catch (SecurityException e) {
        Log.e(TAG, "SecurityException when checking paired devices", e);
        return false;
    }
}
```

### 1.2 Check Connection Status
```java
// In ScaleManager.java - Method to check if scale is ready
public boolean isScaleReady() {
    // 1. Check if scale address is configured
    String scaleAddress = Configure.getScaleAddress(context);
    if (scaleAddress == null || scaleAddress.isEmpty()) {
        Log.d("ScaleManager", "No scale address configured");
        return false;
    }
    
    // 2. Check if Bluetooth is supported and enabled
    if (bluetoothScaleHelper == null || 
        !bluetoothScaleHelper.isBluetoothSupported() || 
        !bluetoothScaleHelper.isBluetoothEnabled()) {
        Log.d("ScaleManager", "Bluetooth not available or not enabled");
        return false;
    }
    
    // 3. Check if scale is paired
    if (!bluetoothScaleHelper.isScalePaired(scaleAddress)) {
        Log.d("ScaleManager", "Scale is not paired with this device");
        return false;
    }
    
    // 4. Try to connect (optional - can be done on-demand)
    Log.d("ScaleManager", "Scale is ready for use");
    return true;
}
```

## 2. Complete Orange Sale Example

### 2.1 Product Setup
First, ensure "Oranges (Fresh)" is set up as a scaled product:

**Database Record:**
```sql
INSERT INTO products (
    id, category_id, tax_id, reference, barcode, label, 
    pricebuy, pricesell, visible, scaled, scaletype, scalevalue,
    disp_order, hasimage, discountenabled, discountrate, prepay, composition
) VALUES (
    1001, 5, 1, 'ORANGE001', '2000000010015', 'Oranges (Fresh)',
    NULL, 250.00, true, true, 0, 1.0,
    1, false, false, 0.0, false, false
);
```

### 2.2 User Workflow
1. **Staff Member**: Opens OrtusPOS application
2. **System**: Automatically checks scale status
3. **UI**: Shows "✓ Scale Connected" indicator
4. **Customer**: Selects "Oranges (Fresh)" product
5. **System**: Opens Product Scale Dialog automatically

### 2.3 Scale Dialog Implementation
```java
// ProductScaleDialog.java - Complete implementation
public class ProductScaleDialog extends DialogFragment {
    private EditText input;
    private TextView weightDisplay;
    private TextView priceDisplay;
    private TextView statusDisplay;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext);
        
        // Create custom view
        View view = getLayoutInflater().inflate(R.layout.dialog_product_scale, null);
        input = view.findViewById(R.id.scale_input);
        weightDisplay = view.findViewById(R.id.scale_weight_display);
        priceDisplay = view.findViewById(R.id.scale_price_display);
        statusDisplay = view.findViewById(R.id.scale_status_display);
        
        // Set up input field with real-time price calculation
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updatePriceDisplay();
            }
            // ... other methods empty
        });
        
        // Initialize scale connection status
        updateScaleStatus();
        
        // Set up buttons
        view.findViewById(R.id.scale_zero_button).setOnClickListener(v -> zeroScale());
        view.findViewById(R.id.scale_tare_button).setOnClickListener(v -> tareScale());
        
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(mProd.getLabel());
        alertDialogBuilder.setIcon(R.drawable.scale);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
            if (mListener != null) {
                String weightStr = input.getText().toString();
                if (!TextUtils.isEmpty(weightStr)) {
                    try {
                        double weight = Double.parseDouble(weightStr);
                        mListener.onPsdPositiveClick(mProd, weight, mIsProductReturn);
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, "Invalid weight entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.scaled_products_cancel, (dialog, id) -> dialog.cancel());
        
        return alertDialogBuilder.create();
    }
    
    private void updateScaleStatus() {
        // Check if scale manager indicates connection
        if (isScaleConnected) {
            statusDisplay.setText("✓ Scale connected");
            statusDisplay.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            weightDisplay.setText("Place item on scale");
        } else {
            statusDisplay.setText("✗ Scale not connected");
            statusDisplay.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            weightDisplay.setText("Enter weight manually (kg)");
        }
    }
    
    // Method called by Transaction activity when weight data arrives
    public void onWeightReceived(double weight, String unit) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Format and display weight
                String weightText = String.format("%.3f %s", weight, unit);
                weightDisplay.setText("Weight: " + weightText);
                
                // Auto-fill the input field
                input.setText(String.format("%.3f", weight));
                
                // Update price display
                updatePriceDisplay();
            });
        }
    }
    
    private void updatePriceDisplay() {
        String weightStr = input.getText().toString();
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                double weight = Double.parseDouble(weightStr);
                double price = mProd.getPrice() * weight;
                priceDisplay.setText(String.format("Price: %.2f Ksh", price));
            } catch (NumberFormatException e) {
                priceDisplay.setText("Invalid weight");
            }
        } else {
            priceDisplay.setText("Enter weight to calculate price");
        }
    }
}
```

## 3. Real-World Example: Weighing 1.750 kg of Oranges

### 3.1 Step-by-Step Process

#### Step 1: System Initialization
```
[10:30:15] System startup
[10:30:16] Checking Bluetooth status... OK
[10:30:17] Checking scale pairing... FOUND (Aclas-FSC-2000, AA:BB:CC:DD:EE:FF)
[10:30:18] Connecting to scale... SUCCESS
[10:30:19] Scale ready for use
```

#### Step 2: Customer Interaction
1. **Customer**: "I'd like some fresh oranges"
2. **Staff**: Taps "Oranges (Fresh)" on POS screen
3. **System**: Opens Product Scale Dialog
4. **Display**: 
   ```
   Oranges (Fresh)
   ✓ Scale connected
   Place item on scale
   [Weight Input Field]
   Price: 0.00 Ksh
   [Zero Scale] [Tare Scale] [Cancel] [Ok]
   ```

#### Step 3: Weighing Process
1. **Staff**: Places oranges on scale
2. **Scale**: Displays "1.750" and stabilizes
3. **Bluetooth**: Transmits data to Android app
4. **App**: Receives weight data
5. **UI Update**:
   ```
   Oranges (Fresh)
   ✓ Scale connected
   Weight: 1.750 kg
   [1.750] ← Auto-filled
   Price: 437.50 Ksh ← Auto-calculated (1.750 × 250.00)
   [Zero Scale] [Tare Scale] [Cancel] [Ok]
   ```

#### Step 4: Sale Completion
1. **Staff**: Presses "Ok" button
2. **System**: Validates data
3. **Database**: Creates ticket line record
4. **Ticket**: Shows "Oranges (Fresh) - 1.750 kg - 437.50 Ksh"
5. **Total**: Updates to include oranges

### 3.2 Database Record Creation
```sql
-- Ticket Line Record Created
INSERT INTO ticketlines (
    ticket_id, product_id, productlabel, quantity, 
    unitprice, taxedunitprice, price, taxedprice,
    finalprice, finaltaxedprice, taxrate, discountrate,
    disporder, tax_id
) VALUES (
    12345,           -- Current ticket
    1001,            -- Oranges product ID
    'Oranges (Fresh)', -- Product name
    1.750,           -- Weight in kg
    250.00,          -- Price per kg
    287.50,          -- Price per kg with 15% tax
    437.50,          -- Total before tax (1.750 × 250.00)
    503.13,          -- Total with tax (437.50 × 1.15)
    437.50,          -- Final price before tax
    503.13,          -- Final price with tax
    0.15,            -- 15% VAT rate
    0.00,            -- 0% discount
    1,               -- Display order
    1                -- Tax ID
);
```

## 4. Receipt Printing with Weight Details

### 4.1 Receipt Generation
```java
// EnhancedReceiptDocument.java
public boolean print(Printer printer, Context ctx) {
    // ... header code ...
    
    // Print each ticket line
    for (TicketLine line : this.r.getTicket().getLines()) {
        Product product = line.getProduct();
        double quantity = line.getQuantity();
        
        // Print product name
        printer.printLine(PrinterHelper.padAfter(product.getLabel(), 32));
        
        // Special handling for scaled products
        if (product.isScaled()) {
            // Print weight: "  1.750 kg"
            String weightText = String.format("%.3f kg", Math.abs(quantity));
            printer.printLine(PrinterHelper.padAfter("  " + weightText, 32));
            
            // Print price per kg: "  @250.00/kg"
            String pricePerKg = String.format("%.2f/kg", line.getProductIncTax());
            printer.printLine(PrinterHelper.padAfter("  @" + pricePerKg, 32));
            
            // Print total amount: "             503.13"
            String totalText = String.format("%.2f", line.getTotalDiscPIncTax());
            printer.printLine(PrinterHelper.padBefore(totalText, 32));
        } else {
            // Regular product formatting
            // ... standard formatting code ...
        }
    }
    
    // ... footer code ...
    return true;
}
```

### 4.2 Printed Receipt
```
        WAMBUGU MOUNTAIN VIEW GROCERIES
        ==============================
Date:     2025-09-13 10:32
Customer: Walk-in Customer
Number:   Counter 1 - 001025

Item                           Total
----------------------------------
Oranges (Fresh)
  1.750 kg
  @250.00/kg
                          503.13

Bread                          120.00
Milk                           200.00
----------------------------------
Subtotal:                 737.50 Ksh
Total:                    848.13 Ksh
Tax (15%):                110.63 Ksh

Cash                       1,000.00 Ksh
Change                        151.87 Ksh

    Thank you for shopping!
    www.mountainviewgroceries.co.ke
```

## 5. Data Recording and Reporting

### 5.1 Sales Data Storage
The system stores comprehensive data for reporting:

```sql
-- Sales summary query for reporting
SELECT 
    DATE(t.date) as sale_date,
    p.label as product_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue,
    AVG(tl.quantity) as avg_weight_per_transaction
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date >= '2025-09-13 00:00:00'
AND t.date <= '2025-09-13 23:59:59'
GROUP BY DATE(t.date), p.id, p.label;
```

**Sample Result for Today:**
| sale_date | product_name | total_weight_kg | transaction_count | total_revenue |
|-----------|--------------|-----------------|-------------------|---------------|
| 2025-09-13 | Oranges (Fresh) | 25.750 | 18 | 6,437.50 |
| 2025-09-13 | Apples (Fresh) | 18.300 | 15 | 6,405.00 |

### 5.2 Scale Usage Tracking (Enhanced System)
```sql
-- Scale usage log (if implemented)
INSERT INTO scale_usage_log (
    scale_id, ticket_id, weight_kg, timestamp
) VALUES (
    1,           -- Scale ID for Aclas-FSC-2000
    12345,       -- Ticket ID
    1.750,       -- Weight of oranges
    NOW()        -- Current timestamp
);
```

## 6. Troubleshooting Common Scenarios

### 6.1 Scale Shows "Not Connected"
**Diagnosis Steps:**
1. Check if scale is powered on
2. Verify Bluetooth is enabled on Android device
3. Confirm scale is paired in device settings
4. Restart OrtusPOS application
5. Re-select scale in Settings

**Log Output:**
```
[10:30:15] Checking scale pairing... NOT FOUND
[10:30:15] Scale with MAC AA:BB:CC:DD:EE:FF is not paired
[10:30:15] Scale not ready for use
```

**Resolution:**
1. Go to Android Settings > Bluetooth
2. Find "Aclas-FSC-2000" in paired devices
3. If not found, put scale in pairing mode and pair again
4. Return to OrtusPOS and reconfigure scale

### 6.2 Incorrect Weight Reading
**Example Issue:**
- Customer places 1.750 kg oranges
- Scale shows 1.250 kg instead

**Troubleshooting:**
1. Zero the scale before weighing
2. Ensure scale is on level surface
3. Check for environmental interference
4. Clean scale platform
5. Verify scale calibration

**System Response:**
```
[10:31:22] Scale reading: 1.250 kg
[10:31:25] Staff notices discrepancy
[10:31:26] Press "Zero Scale" button
[10:31:27] Scale zeros to 0.000 kg
[10:31:30] Re-weigh oranges
[10:31:32] Correct reading: 1.750 kg
```

This complete example demonstrates how the system handles the entire process from checking scale pairing status to making a sale with oranges, recording the data, and printing a professional receipt with weight information.