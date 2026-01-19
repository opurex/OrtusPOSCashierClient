# 🎯 ACCESSING REPORTS IN ORTUSPOS SYSTEM

## 🔍 WHERE TO FIND EXISTING REPORTS

### 🖥️ BACKOFFICE REPORTS ACCESS

**URL**: `http://your-server-address/OrtusPOSBackOffice/` or `http://localhost:8080/OrtusPOSBackOffice/`

### 📋 MAIN MENU NAVIGATION

Once logged in, you'll see the main menu with these sections:

1. **Catalog** - Product and category management
2. **Sales** - Sales reports and ticket management  
3. **Accounting** - Financial reports
4. **Settings** - System configuration
5. **User Menu** - Preferences and logout

### 📊 AVAILABLE REPORTS ALREADY IN SYSTEM

#### 1. SALES REPORTS
**Location**: Sales menu → 
- **By Product** - `http://localhost:8080/OrtusPOSBackOffice/?p=salesbyproduct`
- **By Category** - `http://localhost:8080/OrtusPOSBackOffice/?p=salesbycategory`
- **Details** - `http://localhost:8080/OrtusPOSBackOffice/?p=salesdetails`

#### 2. STOCK REPORTS
**Location**: Catalog menu → Products → Scroll down to see stock information

Also direct access:
- **Stock Reports** - `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`

#### 3. TICKET MANAGEMENT
**Location**: Sales menu → Tickets or Final Tickets

## 🔧 HOW TO TEST REPORTS WITH WEIGHTED PRODUCTS

### STEP 1: MAKE A SALE WITH ORANGES (WEIGHTED PRODUCT)
1. Open the Android POS app
2. Select "Oranges (Fresh)" product (assuming it's marked as scaled)
3. Place oranges on Aclas Bluetooth scale
4. Weight should display automatically (e.g., 1.750 kg)
5. Price calculates automatically (e.g., 437.50 KSH)
6. Complete the sale

### STEP 2: VIEW THE SALE IN REPORTS
1. Go to BackOffice: `http://localhost:8080/OrtusPOSBackOffice/`
2. Navigate to **Sales** → **By Product**
3. You should see "Oranges (Fresh)" in the report
4. The quantity will show the total weight sold
5. Revenue will show total income from oranges

### STEP 3: CHECK STOCK REPORTS
1. Go to **Catalog** → **Products**
2. Find "Oranges (Fresh)" in the product list
3. Check the stock level - it should have decreased by 1.750 kg

## 📈 UNDERSTANDING WEIGHTED PRODUCT DATA

### HOW WEIGHT DATA IS STORED
When you sell 1.750 kg of oranges:
- **Database**: `ticketlines.quantity = 1.750` (instead of count = 1)
- **Product**: `products.scaled = true` (flag for weighted products)
- **Price**: Automatically calculated as `1.750 kg × 250.00 KSH/kg = 437.50 KSH`

### REPORT INTERPRETATION
In sales reports:
- **Quantity Column**: Shows total weight sold (e.g., "25.750" means 25.750 kg)
- **Revenue Column**: Shows total money earned
- **Average**: Shows average weight per transaction

## 📤 EXPORTING REPORTS

### PDF/EXCEL/CSV EXPORT OPTIONS
1. In any report view, look for export buttons (usually at top or bottom)
2. Options typically include:
   - **PDF** - Professional printable format
   - **Excel** - Spreadsheet with formulas
   - **CSV** - Comma-separated values for analysis

## 🚀 TESTING REAL-TIME REPORTING

### VALIDATION PROCESS

#### Test 1: Verify Weight Data Recording
```sql
-- Run this query on your database to verify weight data is stored
SELECT 
    t.date,
    p.label as product_name,
    tl.quantity as weight_kg,
    tl.finaltaxedprice as price
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
ORDER BY t.date DESC
LIMIT 10;
```

Expected result: You should see rows with weight in the `quantity` column (e.g., 1.750)

#### Test 2: Check Product Configuration
```sql
-- Verify product is marked as scaled
SELECT id, label, scaled, pricesell 
FROM products 
WHERE label LIKE '%Orange%' OR label LIKE '%Apple%';
```

Expected result: `scaled` column should be `true` for weight-based products

#### Test 3: Verify API Access
```bash
# Test basic API connectivity
curl http://localhost:8080/api/product/getAll | jq '.'
```

## 📋 TROUBLESHOOTING COMMON ISSUES

### ISSUE 1: Reports Don't Show Weight Data
**Checklist**:
- [ ] Product is marked as `scaled = true` in database
- [ ] Weight data is stored in `ticketlines.quantity` field
- [ ] At least one weighted product sale has been completed
- [ ] BackOffice data has been synced (click "Sync" on home screen)

### ISSUE 2: Weight Data Appears Incorrect
**Checklist**:
- [ ] Aclas scale is properly connected and calibrated
- [ ] Weight shows correctly on scale display
- [ ] Product price is set correctly (per kg, not total)
- [ ] No decimal point confusion (1.750 kg, not 1750 kg)

### ISSUE 3: Export Functions Not Working
**Checklist**:
- [ ] Browser allows popups/downloads
- [ ] Sufficient disk space available
- [ ] Proper file permissions on server
- [ ] Required PHP extensions installed (GD, mbstring, etc.)

## 🎯 QUICK ACCESS LINKS

### DIRECT REPORT URLs:
1. **Sales by Product**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbyproduct`
2. **Sales by Category**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbycategory`
3. **Sales Details**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesdetails`
4. **Stock Reports**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
5. **Product Catalog**: `http://localhost:8080/OrtusPOSBackOffice/?p=products`

### ADMIN LOGIN:
- **Username**: admin (or configured admin user)
- **Password**: Your configured admin password

## 📞 SUPPORT RESOURCES

### DOCUMENTATION:
- `API_DOCUMENTATION_AND_TESTING.md` - Complete API reference
- `ADMIN_REPORTING_INTERFACE.md` - Reporting capabilities
- `TESTING_AND_VALIDATION_GUIDE.md` - Testing procedures

### DATABASE TABLES TO CHECK:
1. `products` - Check `scaled` flag is true for weight-based products
2. `ticketlines` - Check `quantity` contains weight data for scaled products
3. `tickets` - Transaction records linking to ticketlines

---

**🎉 YOU ALREADY HAVE A FULLY FUNCTIONAL REPORTING SYSTEM!**

The reports are already built into your OrtusPOS system. You just need to:
1. Make sales with weighted products (oranges, apples, etc.)
2. Access reports through the BackOffice interface
3. View real-time data in the existing report screens

No additional installation is needed - everything is ready to use!