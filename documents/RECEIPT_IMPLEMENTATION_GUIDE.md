# Implementation Guide: Adding Weight Information to Receipts

## Overview
This guide explains how to modify the existing OrtusPOS system to include weight information for scaled products on printed receipts.

## Understanding the Current Implementation

### TicketLine Weight Storage
- Weight for scaled products is stored in the `quantity` field of `TicketLine`
- The `Product.isScaled()` method identifies weight-based products
- No additional fields are needed to store weight information

### ReceiptDocument Current Behavior
The existing `ReceiptDocument` formats all products the same way:
```java
printer.printLine(PrinterHelper.padAfter(line.getProduct().getLabel(), 32));
lineTxt = priceFormat.format(line.getProductIncTax());
lineTxt = PrinterHelper.padBefore(lineTxt, 17);
lineTxt += PrinterHelper.padBefore("x" + line.getQuantity(), 5);
lineTxt += PrinterHelper.padBefore(priceFormat.format(line.getTotalDiscPIncTax()), 10);
printer.printLine(lineTxt);
```

For a 1.250 kg apple priced at 350.00/kg, this produces:
```
Apples (Fresh)
            350.00 x1.250      437.50
```

## Modification Approach

### Option 1: Modify Existing ReceiptDocument

1. **Add weight format declaration** at the beginning of the print method:
   ```java
   DecimalFormat priceFormat = new DecimalFormat("#0.00");
   DecimalFormat weightFormat = new DecimalFormat("#0.000");
   ```

2. **Replace the ticket line printing section** with enhanced logic:
   ```java
   for (TicketLine line : this.r.getTicket().getLines()) {
       Product product = line.getProduct();
       double quantity = line.getQuantity();
       
       // Print product label
       printer.printLine(PrinterHelper.padAfter(product.getLabel(), 32));
       
       // For scaled products, show weight and price per kg
       if (product.isScaled()) {
           // Print weight information
           String weightText = weightFormat.format(Math.abs(quantity)) + " kg";
           printer.printLine(PrinterHelper.padAfter("  " + weightText, 32));
           
           // Print price per kg
           String pricePerKg = priceFormat.format(line.getProductIncTax()) + "/" + ctx.getString(R.string.kg_unit);
           printer.printLine(PrinterHelper.padAfter("  @" + pricePerKg, 32));
           
           // Print total price for this line
           lineTxt = PrinterHelper.padBefore(priceFormat.format(line.getTotalDiscPIncTax()), 32);
           printer.printLine(lineTxt);
       } else {
           // For non-scaled products, use original format
           lineTxt = priceFormat.format(line.getProductIncTax());
           lineTxt = PrinterHelper.padBefore(lineTxt, 17);
           lineTxt += PrinterHelper.padBefore("x" + quantity, 5);
           lineTxt += PrinterHelper.padBefore(priceFormat.format(line.getTotalDiscPIncTax()), 10);
           printer.printLine(lineTxt);
       }
       
       if (line.getDiscountRate() != 0) {
           printer.printLine(PrinterHelper.padBefore(ctx.getString(R.string.include_discount) + Double.toString(line.getDiscountRate() * 100) + "%", 32));
       }
   }
   ```

3. **Add the kg_unit string** to `strings.xml`:
   ```xml
   <string name="kg_unit">kg</string>
   ```

### Option 2: Use EnhancedReceiptDocument

1. **Create a factory method** to choose the appropriate receipt document:
   ```java
   public class ReceiptDocumentFactory {
       public static PrintableDocument createReceiptDocument(Receipt receipt) {
           // Check if any products in the receipt are scaled
           for (TicketLine line : receipt.getTicket().getLines()) {
               if (line.getProduct().isScaled()) {
                   return new EnhancedReceiptDocument(receipt);
               }
           }
           // If no scaled products, use the standard receipt
           return new ReceiptDocument(receipt);
       }
   }
   ```

2. **Modify POSConnectedTrackedActivity** to use the factory:
   ```java
   // Replace this line:
   // return this.deviceService.print(new ReceiptDocument(r));
   
   // With this:
   return this.deviceService.print(ReceiptDocumentFactory.createReceiptDocument(r));
   ```

## Expected Output Comparison

### Before Modification (Standard Format)
```
Apples (Fresh)
            350.00 x1.250      437.50
Bananas
            200.00 x0.750      150.00
Bread             1         120.00
--------------------------------
```

### After Modification (Enhanced Format)
```
Apples (Fresh)
  1.250 kg
  @350.00/kg
             437.50
Bananas
  0.750 kg
  @200.00/kg
             150.00
Bread             1         120.00
--------------------------------
```

## Implementation Steps

### Step 1: Backup Current Files
1. Backup `ReceiptDocument.java`
2. Backup `strings.xml`

### Step 2: Add String Resource
1. Open `app/src/main/res/values/strings.xml`
2. Add `<string name="kg_unit">kg</string>` before the closing `</resources>` tag

### Step 3: Modify ReceiptDocument (Option 1)
1. Open `app/src/main/java/com/opurex/ortus/client/drivers/printer/documents/ReceiptDocument.java`
2. Add `DecimalFormat weightFormat = new DecimalFormat("#0.000");` after the priceFormat declaration
3. Replace the ticket line printing loop with the enhanced version provided above

### Step 4: Alternative - Use EnhancedReceiptDocument (Option 2)
1. Ensure `EnhancedReceiptDocument.java` is in the correct package
2. Create `ReceiptDocumentFactory.java` with the factory method
3. Modify `POSConnectedTrackedActivity.java` to use the factory

### Step 5: Testing
1. Create a test scaled product (e.g., "Test Apples" at 350.00/kg, Scaled: Yes)
2. Process a sale with the scaled product
3. Place an item on the scale or enter weight manually
4. Complete the sale and print the receipt
5. Verify weight information appears correctly

## Testing Checklist

### Basic Functionality
- [ ] Scaled products print with weight information
- [ ] Non-scaled products print in original format
- [ ] Weight displays with 3 decimal places
- [ ] Price per kg displays correctly
- [ ] Total price calculates correctly
- [ ] Negative quantities (returns) handle properly

### Edge Cases
- [ ] Zero weight items
- [ ] Very small weights (0.001 kg)
- [ ] Large weights (10+ kg)
- [ ] Products with discounts
- [ ] Mixed tickets (scaled and non-scaled items)

### Formatting
- [ ] Proper alignment on receipt
- [ ] Consistent spacing
- [ ] Clear separation between items
- [ ] Currency symbols display correctly
- [ ] Decimal places consistent

## Troubleshooting

### Common Issues
1. **Missing kg_unit string**: Add to strings.xml
2. **Formatting issues**: Adjust padding values
3. **Weight not showing**: Verify product is marked as scaled
4. **Incorrect calculations**: Check quantity handling

### Debugging Tips
1. Add log statements to verify weight values
2. Test with hardcoded values
3. Check printer column limitations
4. Verify TicketLine quantity values

## Rollback Procedure

If issues occur after implementation:

1. Restore the backed-up `ReceiptDocument.java`
2. Remove the kg_unit string from `strings.xml` if added
3. Rebuild and redeploy the application
4. Test with a simple transaction

This implementation provides clear weight information for customers while maintaining backward compatibility with existing non-scaled products.