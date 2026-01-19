# Modifying Receipt Printing for Weight-Based Products

## Overview
This document explains how to modify the receipt printing system to include weight information for scaled products. Two approaches are provided: modifying the existing ReceiptDocument or using the new EnhancedReceiptDocument.

## Approach 1: Modifying Existing ReceiptDocument

To modify the existing ReceiptDocument to include weight information:

1. Locate the ReceiptDocument.java file:
   `app/src/main/java/com/opurex/ortus/client/drivers/printer/documents/ReceiptDocument.java`

2. In the print method, find the section that handles ticket line printing (around line 84):
   ```java
   for (TicketLine line : this.r.getTicket().getLines()) {
       printer.printLine(PrinterHelper.padAfter(line.getProduct().getLabel(), 32));
       lineTxt = priceFormat.format(line.getProductIncTax());
       lineTxt = PrinterHelper.padBefore(lineTxt, 17);
       lineTxt += PrinterHelper.padBefore("x" + line.getQuantity(), 5);
       lineTxt += PrinterHelper.padBefore(priceFormat.format(line.getTotalDiscPIncTax()), 10);
       printer.printLine(lineTxt);
       if (line.getDiscountRate() != 0) {
           printer.printLine(PrinterHelper.padBefore(ctx.getString(R.string.include_discount) + Double.toString(line.getDiscountRate() * 100) + "%", 32));
       }
   }
   ```

3. Replace with the enhanced version that handles scaled products:
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

4. Add the weight format declaration at the beginning of the print method:
   ```java
   DecimalFormat priceFormat = new DecimalFormat("#0.00");
   DecimalFormat weightFormat = new DecimalFormat("#0.000");
   ```

5. Add the kg_unit string to strings.xml if not already present:
   ```xml
   <string name="kg_unit">kg</string>
   ```

## Approach 2: Using EnhancedReceiptDocument

Alternatively, you can use the EnhancedReceiptDocument which already includes weight information:

1. Create a factory method to choose between ReceiptDocument and EnhancedReceiptDocument:
   ```java
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
   ```

2. Modify the POSConnectedTrackedActivity to use the factory method:
   ```java
   // Replace this line:
   // return this.deviceService.print(new ReceiptDocument(r));
   
   // With this:
   return this.deviceService.print(ReceiptDocumentFactory.createReceiptDocument(r));
   ```

## Example Receipt Output

### Before (Standard Receipt)
```
Apples (Fresh)
            350.00 x1.250      437.50
```

### After (Enhanced Receipt with Weight)
```
Apples (Fresh)
  1.250 kg
  @350.00/kg
             437.50
```

## Testing the Changes

1. Create a test product marked as "Scaled" with a price per kg
2. Process a sale with the scaled product
3. Place an item on the Bluetooth scale
4. Verify weight is captured and displayed
5. Complete the sale and print the receipt
6. Check that the receipt includes weight information

## Best Practices

1. Always test receipt formatting with various weight values
2. Ensure decimal formatting is consistent
3. Verify that non-scaled products still print correctly
4. Test with both positive and negative quantities (returns)
5. Check that discount information still displays properly

## Troubleshooting

### Common Issues
1. **Weight not displaying**: Ensure product is marked as "Scaled"
2. **Incorrect formatting**: Check decimal format patterns
3. **Missing kg_unit string**: Add to strings.xml
4. **Printer alignment issues**: Adjust padding values

### Debugging Tips
1. Add log statements to verify weight values
2. Test with hardcoded weight values
3. Check printer column width limitations
4. Verify TicketLine quantity values for scaled products