# How Weight Information is Stored in Ticket Lines

## Overview
This document explains how weight information is stored and retrieved for scaled products in the OrtusPOS system.

## TicketLine Structure for Scaled Products

### Weight Storage
For scaled products, the weight is stored directly in the `quantity` field of the `TicketLine` object:
- When a customer buys 1.250 kg of apples, the `quantity` field contains `1.250`
- For product returns, the quantity would be negative (e.g., `-0.750`)

### Identifying Scaled Products
The `Product` object has an `isScaled()` method that returns `true` for weight-based products:
```java
Product product = ticketLine.getProduct();
if (product.isScaled()) {
    // This is a weight-based product
    double weight = ticketLine.getQuantity();
}
```

### Accessing Weight Information
To retrieve weight information from a ticket line:

1. Check if the product is scaled:
   ```java
   if (ticketLine.getProduct().isScaled()) {
       // Handle scaled product
   }
   ```

2. Get the weight value:
   ```java
   double weight = Math.abs(ticketLine.getQuantity());
   ```

3. Format for display:
   ```java
   DecimalFormat weightFormat = new DecimalFormat("#0.000");
   String formattedWeight = weightFormat.format(weight) + " kg";
   ```

## Example Implementation

### Reading Weight from Ticket Lines
```java
public class WeightInfoExtractor {
    public static String getWeightInfo(TicketLine line) {
        if (line.getProduct().isScaled()) {
            double weight = Math.abs(line.getQuantity());
            DecimalFormat format = new DecimalFormat("#0.000");
            return format.format(weight) + " kg";
        }
        return null; // Not a scaled product
    }
    
    public static String getPricePerKg(TicketLine line) {
        if (line.getProduct().isScaled()) {
            DecimalFormat format = new DecimalFormat("#0.00");
            return format.format(line.getProductIncTax()) + "/kg";
        }
        return null; // Not a scaled product
    }
}
```

### Using in Receipt Printing
```java
// In receipt printing code
for (TicketLine line : ticket.getLines()) {
    if (line.getProduct().isScaled()) {
        // Print weight information
        String weightInfo = WeightInfoExtractor.getWeightInfo(line);
        String pricePerKg = WeightInfoExtractor.getPricePerKg(line);
        
        printer.printLine("  " + weightInfo);
        printer.printLine("  @" + pricePerKg);
    }
}
```

## Database Storage

### TicketLine JSON Representation
When tickets are saved, scaled product weights are stored in the JSON representation:
```json
{
  "product": {
    "id": "PROD001",
    "label": "Apples (Fresh)",
    "scaled": true,
    "priceSell": 350.00
  },
  "quantity": 1.250,
  "price": 350.00,
  "total": 437.50
}
```

### Key Points
- The `quantity` field directly contains the weight for scaled products
- The `scaled` flag on the product indicates weight-based pricing
- Price calculations use the stored weight value
- Negative quantities represent returns

## Best Practices

### Handling Weight Data
1. Always use `Math.abs()` when displaying weights to customers
2. Store weights with sufficient precision (3 decimal places)
3. Validate weight values before processing
4. Handle negative quantities for returns appropriately

### Precision and Formatting
1. Use `DecimalFormat("#0.000")` for weight display
2. Use `DecimalFormat("#0.00")` for price display
3. Round appropriately for calculations
4. Maintain consistency in units (kilograms)

### Error Handling
1. Check for null values before accessing
2. Validate that scaled products have non-zero quantities
3. Handle formatting exceptions gracefully
4. Log errors for troubleshooting

This structure ensures that weight information is consistently stored and can be easily retrieved for receipt printing, reporting, and other business operations.