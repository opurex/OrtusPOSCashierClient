# Technical Data Flow: Aclas Scale Integration for Weight-Based Sales

## Overview
This document explains the complete technical flow of weight data from an Aclas Bluetooth scale through the OrtusPOS system, covering pairing, data reception, sale processing, data recording, and receipt printing.

## 1. Bluetooth Pairing and Connection Flow

### 1.1 Initial Pairing Process
```java
// In BluetoothScaleSelectionActivity.java
public class BluetoothScaleSelectionActivity extends AppCompatActivity {
    private void loadPairedDevices() {
        // Retrieve paired Bluetooth devices
        Set<BluetoothDevice> pairedDevices = bluetoothHelper.getPairedDevices(this);
        
        // Filter for Aclas scales (typically named "Aclas*" or similar)
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().startsWith("Aclas")) {
                // Add to device list for selection
                addDeviceToList(device);
            }
        }
    }
    
    private void selectDevice(int position) {
        BluetoothDevice selectedDevice = availableDevices.get(position);
        
        // Store scale MAC address in app preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
            .putString("scale_address", selectedDevice.getAddress())
            .putString("scale_name", selectedDevice.getName())
            .apply();
            
        // Return result to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCALE_ADDRESS, selectedDevice.getAddress());
        resultIntent.putExtra(EXTRA_SCALE_NAME, selectedDevice.getName());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
```

### 1.2 Scale Connection in Transaction Activity
```java
// In Transaction.java
public class Transaction extends POSConnectedTrackedActivity {
    private ScaleManager scaleManager;
    
    private void initializeScaleManager() {
        scaleManager = new ScaleManager(this);
        scaleManager.setScaleWeightListener(new ScaleManager.ScaleWeightListener() {
            @Override
            public void onWeightReceived(double weight, String unit) {
                // Handle weight data received from scale
                Log.d("Scale", "Received weight: " + weight + " " + unit);
                
                // Update any active scale dialogs
                updateActiveScaleDialog(weight, unit);
            }
            
            @Override
            public void onScaleConnected() {
                Log.d("Scale", "Scale connected");
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "Scale connected", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onScaleDisconnected() {
                Log.d("Scale", "Scale disconnected");
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "Scale disconnected", Toast.LENGTH_SHORT).show();
                });
            }
        });
        
        // Attempt to connect to configured scale
        boolean connected = scaleManager.initializeScale();
        Log.d("Scale", "Scale initialization result: " + connected);
    }
}
```

## 2. Aclas SDK Integration

### 2.1 Scale Communication Layer
```java
// In BluetoothScaleHelper.java
public class BluetoothScaleHelper {
    private AclasScaler aclasScaler;
    
    private void initAclasScaler() {
        try {
            // Initialize Aclas SDK with FSC scale type
            aclasScaler = new AclasScaler(AclasScaler.Type_FSC, context, 
                new AclasScaler.AclasScalerListener() {
                    @Override
                    public void onRcvData(AclasScaler.WeightInfoNew info) {
                        // Handle weight data from scale
                        if (scaleDataListener != null && info != null) {
                            // Convert to kg if needed and notify listener
                            double weightInKg = info.netWeight;
                            String unit = info.unit != null ? info.unit : "kg";
                            scaleDataListener.onWeightReceived(weightInKg, unit);
                        }
                    }
                    
                    @Override
                    public void onConnected() {
                        isConnected = true;
                        if (connectionStateListener != null) {
                            connectionStateListener.onConnected();
                        }
                    }
                    
                    @Override
                    public void onDisConnected() {
                        isConnected = false;
                        if (connectionStateListener != null) {
                            connectionStateListener.onDisconnected();
                        }
                    }
                });
        } catch (Exception e) {
            Log.e("BluetoothScaleHelper", "Failed to initialize Aclas scaler", e);
        }
    }
    
    public boolean connectToScale(String macAddress) {
        try {
            // For paired devices, pass empty string
            // For new devices, pass MAC address
            int result = aclasScaler.AclasConnect(macAddress != null ? macAddress : "");
            return result == 0; // 0 = success
        } catch (Exception e) {
            Log.e("BluetoothScaleHelper", "Exception while connecting to scale", e);
            return false;
        }
    }
}
```

## 3. Weight Data Reception and Processing

### 3.1 Real-Time Weight Updates
```java
// In ProductScaleDialog.java
public class ProductScaleDialog extends DialogFragment {
    private EditText input;
    private TextView weightDisplay;
    private TextView priceDisplay;
    
    // Method called when weight is received from Bluetooth scale
    public void onWeightReceived(double weight, String unit) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Format and display weight
                String weightText = String.format("%.3f %s", weight, unit);
                weightDisplay.setText("Weight: " + weightText);
                
                // Auto-fill the input field with the weight from the scale
                input.setText(String.format("%.3f", weight));
                
                // Update the price display
                updatePriceDisplay();
            });
        }
    }
    
    private void updatePriceDisplay() {
        String weightStr = input.getText().toString();
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                double weight = Double.parseDouble(weightStr);
                // Calculate price: weight × product price per kg
                double price = mProd.getPrice() * weight;
                priceDisplay.setText(String.format("Price: %.2f", price));
            } catch (NumberFormatException e) {
                priceDisplay.setText("Invalid weight");
            }
        } else {
            priceDisplay.setText("Enter weight to calculate price");
        }
    }
}
```

## 4. Sale Processing with Weight Data

### 4.1 Adding Scaled Product to Ticket
```java
// In Transaction.java
public class Transaction extends POSConnectedTrackedActivity {
    void askForAScaledProduct(Product p, boolean isReturnProduct) {
        // Show scale dialog for weight-based products
        ProductScaleDialog dial = ProductScaleDialog.newInstance(p, isReturnProduct);
        dial.setDialogListener(this);
        dial.show(getSupportFragmentManager(), ProductScaleDialog.TAG);
    }
    
    @Override
    public void onPsdPositiveClick(Product p, double weight, boolean isProductReturned) {
        // Handle positive response from scale dialog
        if (weight > 0) {
            if (isProductReturned) {
                addAScaledProductReturnToTicket(p, weight);
            } else {
                addAScaledProductToTicket(p, weight);
            }
        }
    }
    
    private void addAScaledProductToTicket(Product p, double weight) {
        TicketFragment ticket = getTicketFragment();
        // Add scaled product with weight as quantity
        ticket.addScaledProduct(p, weight);
        ticket.scrollDown();
        ticket.updateView();
    }
}
```

### 4.2 Ticket Line Creation
```java
// In TicketFragment.java
public class TicketFragment extends Fragment {
    public void addScaledProduct(Product p, double scale) {
        mTicketData.addScaledProduct(p, scale);
        updateView();
    }
}

// In Ticket.java (data model)
public class Ticket {
    /**
     * Adds scaled product to the ticket
     * @param p     the product to add
     * @param scale the products weight in kg
     */
    public void addScaledProduct(Product p, double scale) {
        // Create ticket line with weight as quantity
        this.addLineProductScaled(p, scale);
    }
    
    public void addLineProductScaled(Product p, double scale) {
        // Add ticket line where quantity = weight in kg
        addTicketLine(new TicketLine(p, scale, getTariffArea()));
    }
}
```

## 5. Data Storage in Database

### 5.1 Ticket Line Storage
The weight data is stored in the `ticketlines` table where:
- `quantity` field contains the actual weight in kg
- `product_id` links to the weight-based product
- `finaltaxedprice` contains the calculated total price

```sql
-- Example ticket line record for 1.750 kg of oranges at 250.00/kg
INSERT INTO ticketlines (
    ticket_id,
    product_id,
    productlabel,
    quantity,
    unitprice,
    taxedunitprice,
    price,
    taxedprice,
    finalprice,
    finaltaxedprice,
    taxrate,
    discountrate,
    disporder,
    tax_id
) VALUES (
    12345,           -- ticket_id
    678,             -- product_id (Oranges)
    'Oranges (Fresh)', -- productlabel
    1.750,           -- quantity (weight in kg)
    250.00,          -- unitprice (price per kg)
    287.50,          -- taxedunitprice (price per kg with tax)
    437.50,          -- price (total before tax: 1.750 × 250.00)
    503.13,          -- taxedprice (total with tax)
    437.50,          -- finalprice (after discounts)
    503.13,          -- finaltaxedprice (final amount)
    0.15,            -- taxrate (15% VAT)
    0.00,            -- discountrate (0% discount)
    1,               -- disporder
    1                -- tax_id
);
```

### 5.2 Product Configuration Storage
Weight-based products are marked in the `products` table:

```sql
-- Example product record for Oranges
UPDATE products SET 
    scaled = true,        -- Indicates weight-based product
    scaletype = 0,        -- 0 = weight scale
    scalevalue = 1.0      -- Scale capacity (not used for Aclas)
WHERE id = 678;           -- Product ID for Oranges
```

## 6. Receipt Printing with Weight Information

### 6.1 Enhanced Receipt Document
```java
// In EnhancedReceiptDocument.java
public class EnhancedReceiptDocument implements PrintableDocument {
    public boolean print(Printer printer, Context ctx) {
        // ... header printing code ...
        
        // Print ticket lines
        for (TicketLine line : this.r.getTicket().getLines()) {
            Product product = line.getProduct();
            double quantity = line.getQuantity();
            
            // Print product label
            printer.printLine(PrinterHelper.padAfter(product.getLabel(), 32));
            
            // For scaled products, show weight and price per kg
            if (product.isScaled()) {
                // Print weight information: "  1.750 kg"
                String weightText = weightFormat.format(Math.abs(quantity)) + " kg";
                printer.printLine(PrinterHelper.padAfter("  " + weightText, 32));
                
                // Print price per kg: "  @250.00/kg"
                String pricePerKg = priceFormat.format(line.getProductIncTax()) + "/" + 
                                  ctx.getString(R.string.kg_unit);
                printer.printLine(PrinterHelper.padAfter("  @" + pricePerKg, 32));
                
                // Print total price for this line
                String lineTxt = PrinterHelper.padBefore(
                    priceFormat.format(line.getTotalDiscPIncTax()), 32);
                printer.printLine(lineTxt);
            } else {
                // For non-scaled products, use original format
                String lineTxt = priceFormat.format(line.getProductIncTax());
                lineTxt = PrinterHelper.padBefore(lineTxt, 17);
                lineTxt += PrinterHelper.padBefore("x" + quantity, 5);
                lineTxt += PrinterHelper.padBefore(
                    priceFormat.format(line.getTotalDiscPIncTax()), 10);
                printer.printLine(lineTxt);
            }
        }
        
        // ... footer printing code ...
        return true;
    }
}
```

### 6.2 Example Receipt Output
```
        ORTUS POS RECEIPT
        =================
Date:     2025-09-13 14:30
Customer: Walk-in Customer
Number:   Cash Register - 001025

Item              Price     Total
--------------------------------
Oranges (Fresh)
  1.750 kg
  @250.00/kg
             503.13

Bread             1         120.00
--------------------------------
Subtotal:            437.50 Ksh
Total:               503.13 Ksh
Tax:                  65.63 Ksh

Cash                  600.00 Ksh
Change                 96.87 Ksh

    Thank you for shopping!
    Powered by OrtusPOS
```

## 7. Complete Data Flow Example

### 7.1 Scenario: Customer Buys Oranges
1. **Customer Action**: Places oranges on Aclas scale
2. **Scale Reading**: 1.750 kg displayed on scale
3. **Bluetooth Transmission**: Scale sends data via Bluetooth
4. **Aclas SDK Processing**: SDK parses weight data
5. **Android App Reception**: BluetoothScaleHelper receives data
6. **UI Update**: ProductScaleDialog displays 1.750 kg
7. **Price Calculation**: 1.750 × 250.00 = 437.50 Ksh
8. **User Confirmation**: Customer presses "Ok"
9. **Ticket Update**: TicketLine created with quantity=1.750
10. **Database Storage**: Record inserted into ticketlines table
11. **Receipt Printing**: Enhanced formatting shows weight details
12. **Reporting**: Data available for weight-based analytics

### 7.2 Data Structure Flow
```
Aclas Scale Hardware
    ↓ (Bluetooth)
Aclas SDK (Java)
    ↓ (Callback)
BluetoothScaleHelper.java
    ↓ (Listener Pattern)
ScaleManager.java
    ↓ (UI Update)
ProductScaleDialog.java
    ↓ (User Action)
Transaction.java
    ↓ (Ticket Creation)
Ticket.java → TicketLine.java
    ↓ (Database Sync)
PostgreSQL ticketlines table
    ↓ (API Query)
Server Reports
    ↓ (API Response)
Backoffice Dashboard
    ↓ (User Interface)
Weight Reports Screen
```

## 8. Error Handling and Fallbacks

### 8.1 Connection Failures
```java
// In ScaleManager.java
public boolean initializeScale() {
    String scaleAddress = Configure.getScaleAddress(context);
    if (scaleAddress == null || scaleAddress.isEmpty()) {
        lastError = "No scale address configured";
        return false;
    }
    
    if (!bluetoothScaleHelper.isBluetoothSupported()) {
        lastError = "Bluetooth not supported on this device";
        return false;
    }
    
    if (!bluetoothScaleHelper.isBluetoothEnabled()) {
        lastError = "Bluetooth is not enabled. Please enable Bluetooth in device settings.";
        return false;
    }
    
    // Try to connect, fallback to manual entry if fails
    boolean connected = bluetoothScaleHelper.connectToScale(scaleAddress);
    if (!connected) {
        lastError = "Failed to connect to scale. Please check connection and try again.";
        // System automatically falls back to manual weight entry
    }
    
    return connected;
}
```

### 8.2 Manual Entry Fallback
When scale is not available, the system allows manual weight entry:
1. Scale status shows "✗ Scale not connected"
2. User can type weight value (e.g., "1.750")
3. Price calculates automatically as user types
4. All other functionality remains the same

This comprehensive integration ensures seamless operation whether using an Aclas Bluetooth scale or manual entry, with all weight data properly recorded and reported.