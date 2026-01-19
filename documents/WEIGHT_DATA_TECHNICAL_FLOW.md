# Technical Data Flow: Weight Information in OrtusPOS System

## Overview
This document explains the complete technical flow of weight data from the Aclas Bluetooth scale through the Android app, to the server, and finally to reports and receipts.

## 1. Data Flow Architecture

### 1.1 System Components
1. **Aclas Bluetooth Scale**: Hardware device that measures weight
2. **Android POS App**: Mobile application that communicates with scale
3. **OrtusPOS Server**: Central database and API server
4. **Backoffice System**: Web-based reporting and management interface
5. **Receipt Printer**: Hardware that prints customer receipts

### 1.2 Data Flow Path
```
Aclas Scale → Bluetooth → Android App → Ticket Line → Server DB → Reports/Receipts
```

## 2. Weight Data Capture Process

### 2.1 Bluetooth Communication
The Aclas scale communicates with the Android device using the Bluetooth protocol:

1. **Connection Establishment**:
   ```java
   // In BluetoothScaleHelper.java
   public boolean connectToScale(String macAddress) {
       // Connects to scale using MAC address
       // Returns true if successful
   }
   ```

2. **Data Reception**:
   ```java
   // Aclas SDK callback for weight data
   public void onRcvData(AclasScaler.WeightInfoNew info) {
       double weightInKg = info.netWeight;  // e.g., 1.250
       String unit = info.unit;             // e.g., "kg"
       // Send to UI and pricing engine
   }
   ```

### 2.2 Android App Processing
The Android app processes the weight data and integrates it with the POS system:

1. **Product Scale Dialog**:
   ```java
   // In ProductScaleDialog.java
   public void onWeightReceived(double weight, String unit) {
       // Update UI with weight information
       weightDisplay.setText("Weight: " + weight + " " + unit);
       
       // Auto-fill input field
       input.setText(String.format("%.3f", weight));
       
       // Update price calculation
       updatePriceDisplay();
   }
   ```

2. **Price Calculation**:
   ```java
   // In ScaleManager.java
   public double calculatePrice(Product product, double weight) {
       if (product.isScaled()) {
           return product.getPrice() * weight;  // e.g., 250.00 * 1.250 = 312.50
       }
       return 0.0;
   }
   ```

3. **Ticket Line Creation**:
   ```java
   // In Transaction.java
   private void addAScaledProductToTicket(Product p, double weight) {
       TicketFragment ticket = getTicketFragment();
       ticket.addScaledProduct(p, weight);  // weight stored in quantity field
       ticket.updateView();
   }
   ```

## 3. Database Storage

### 3.1 Products Table
Weight-based products are flagged in the database:
```sql
-- Products table structure
CREATE TABLE products (
    id INTEGER PRIMARY KEY,
    label VARCHAR(255),
    pricesell DECIMAL(10,2),
    scaled BOOLEAN,           -- TRUE for weight-based products
    scaletype SMALLINT,       -- 0 for weight, 1 for volume
    scalevalue DECIMAL(10,3)  -- Capacity/unit value
);

-- Example product entry
INSERT INTO products (id, label, pricesell, scaled, scaletype, scalevalue)
VALUES (101, 'Oranges (Fresh)', 250.00, TRUE, 0, 1.0);
```

### 3.2 Ticket Lines Table
Weight information is stored in the quantity field:
```sql
-- Ticket lines table structure
CREATE TABLE ticketlines (
    ticket_id INTEGER,
    product_id INTEGER,
    quantity DECIMAL(10,3),      -- Weight in kg for scaled products
    unitprice DECIMAL(10,2),     -- Price per kg
    finaltaxedprice DECIMAL(10,2) -- Total calculated price
);

-- Example ticket line for weighed oranges
INSERT INTO ticketlines (ticket_id, product_id, quantity, unitprice, finaltaxedprice)
VALUES (5001, 101, 1.250, 250.00, 312.50);
```

### 3.3 Data Integrity
The system maintains data integrity through:
1. **Validation**: Ensuring weight values are positive and reasonable
2. **Precision**: Storing weights with 3 decimal places (grams precision)
3. **Consistency**: Using the same units (kg) throughout the system

## 4. Server-Side Processing

### 4.1 API Endpoints
The server provides endpoints for weight data access:

1. **Ticket Creation**:
   ```php
   // POST /api/ticket
   {
     "lines": [
       {
         "productId": 101,
         "quantity": 1.250,        // Weight in kg
         "unitPrice": 250.00,      // Price per kg
         "finalTaxedPrice": 312.50 // Calculated total
       }
     ]
   }
   ```

2. **Weight Analytics**:
   ```php
   // GET /api/reports/weight-sales
   SELECT 
     p.label,
     SUM(tl.quantity) as total_weight_kg,
     SUM(tl.finaltaxedprice) as total_revenue
   FROM ticketlines tl
   JOIN products p ON tl.product_id = p.id
   WHERE p.scaled = true
   GROUP BY p.id, p.label;
   ```

### 4.2 Data Synchronization
The Android app synchronizes data with the server:
```java
// In data synchronization process
public void syncTicketToServer(Ticket ticket) {
    for (TicketLine line : ticket.getLines()) {
        if (line.getProduct().isScaled()) {
            // Quantity field contains weight data
            double weight = line.getQuantity();  // e.g., 1.250 kg
            // Send to server API
        }
    }
}
```

## 5. Receipt Generation

### 5.1 Enhanced Receipt Formatting
The receipt system formats weight-based products differently:

```java
// In EnhancedReceiptDocument.java
public boolean print(Printer printer, Context ctx) {
    for (TicketLine line : this.r.getTicket().getLines()) {
        Product product = line.getProduct();
        
        if (product.isScaled()) {
            // Print product label
            printer.printLine(product.getLabel());
            
            // Print weight information
            String weightText = String.format("%.3f kg", Math.abs(line.getQuantity()));
            printer.printLine("  " + weightText);
            
            // Print price per kg
            String pricePerKg = String.format("%.2f/kg", line.getProductIncTax());
            printer.printLine("  @" + pricePerKg);
            
            // Print total price
            String totalPrice = String.format("%.2f", line.getTotalDiscPIncTax());
            printer.printLine(String.format("%32s", totalPrice));
        } else {
            // Standard product formatting
            // ... existing code
        }
    }
}
```

### 5.2 Example Receipt Output
```
Oranges (Fresh)
  1.250 kg
  @250.00/kg
             312.50
```

## 6. Reporting and Analytics

### 6.1 Weight-Based Sales Reports
The system generates detailed reports on weight-based product sales:

```sql
-- Comprehensive weight sales report
SELECT 
    p.label as product_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue,
    AVG(tl.quantity) as avg_weight_per_transaction
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY p.id, p.label
ORDER BY total_weight_kg DESC;
```

### 6.2 Scale Performance Analytics
Track scale usage and performance:
```sql
-- Scale usage statistics
SELECT 
    sd.name as scale_name,
    COUNT(sul.id) as usage_count,
    SUM(sul.weight_kg) as total_weight_processed,
    AVG(sul.weight_kg) as avg_weight_per_transaction
FROM scale_usage_log sul
JOIN scale_devices sd ON sul.scale_id = sd.id
WHERE sul.timestamp BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY sd.id, sd.name;
```

## 7. Error Handling and Data Validation

### 7.1 Weight Data Validation
The system validates weight data at multiple points:

1. **Android App Validation**:
   ```java
   private boolean validateWeight(double weight) {
       return weight > 0 && weight <= 1000; // Reasonable limits
   }
   ```

2. **Server-Side Validation**:
   ```php
   function validateTicketLine($ticketLine) {
       if ($ticketLine['product']['scaled']) {
           if ($ticketLine['quantity'] <= 0) {
               throw new ValidationException("Weight must be positive");
           }
           if ($ticketLine['quantity'] > 1000) {
               throw new ValidationException("Weight exceeds maximum limit");
           }
       }
   }
   ```

### 7.2 Error Recovery
The system handles errors gracefully:
1. **Scale Disconnection**: Fall back to manual weight entry
2. **Data Corruption**: Validate and repair inconsistent data
3. **Network Issues**: Queue data for later synchronization

## 8. Performance Optimization

### 8.1 Database Indexes
Optimized database structure for weight queries:
```sql
-- Indexes for performance
CREATE INDEX idx_products_scaled ON products(scaled);
CREATE INDEX idx_ticketlines_product ON ticketlines(product_id);
CREATE INDEX idx_tickets_date ON tickets(date);
```

### 8.2 Caching Strategies
Cache frequently accessed data:
1. **Product Information**: Cache scaled product details
2. **Price Calculations**: Cache common weight/price combinations
3. **Report Data**: Cache daily summary statistics

## 9. Security Considerations

### 9.1 Data Protection
1. **Encryption**: Weight data encrypted in transit and at rest
2. **Access Control**: Role-based access to weight reports
3. **Audit Trail**: Log all weight-related transactions

### 9.2 Scale Authentication
```java
// Verify scale authenticity
private boolean verifyScale(String macAddress) {
    // Check against registered scales
    // Validate firmware version
    // Confirm device authenticity
}
```

## 10. Example Complete Data Flow

### Scenario: Customer Buys 1.750 kg of Oranges at 250.00/kg

1. **Scale Measurement**:
   ```
   Aclas Scale: 1.750 kg detected
   ```

2. **Bluetooth Transmission**:
   ```
   Bluetooth: Weight data packet sent
   ```

3. **Android App Processing**:
   ```
   Weight: 1.750 kg
   Price Calculation: 1.750 × 250.00 = 437.50 Ksh
   ```

4. **Database Storage**:
   ```sql
   INSERT INTO ticketlines 
   (ticket_id, product_id, quantity, unitprice, finaltaxedprice)
   VALUES (5002, 101, 1.750, 250.00, 437.50);
   ```

5. **Server Synchronization**:
   ```
   API Call: POST /api/ticket
   Data: {"productId": 101, "quantity": 1.750, "price": 437.50}
   ```

6. **Receipt Printing**:
   ```
   Oranges (Fresh)
     1.750 kg
     @250.00/kg
                437.50
   ```

7. **Reporting**:
   ```sql
   SELECT SUM(quantity) FROM ticketlines 
   WHERE product_id = 101 AND DATE(date) = '2025-09-13';
   -- Result: 15.250 kg (cumulative for the day)
   ```

This comprehensive data flow ensures accurate, reliable, and traceable handling of weight-based product sales throughout the entire OrtusPOS system.