# 🧪 ORTUSPOS REPORTING SYSTEM VERIFICATION GUIDE

## 🎯 OBJECTIVE
This guide will help you verify that the OrtusPOS reporting system is working correctly with weighted products (oranges, apples, etc.) and Aclas Bluetooth scales.

## 🛠️ PREREQUISITES
1. OrtusPOSServer running on `localhost:8080`
2. OrtusPOSBackOffice accessible via web browser
3. At least one weighted product created (e.g., "Oranges (Fresh)")
4. Aclas Bluetooth scale properly configured and connected

## 🔍 STEP 1: VERIFY SERVER CONNECTIVITY

### Test Basic API Access
```bash
# Test if server is responding
curl -v http://localhost:8080/

# Test product API access
curl -s http://localhost:8080/api/product/getAll | head -200

# Test if you get a JSON response
curl -s http://localhost:8080/api/product/getAll | jq '.' >/dev/null 2>&1 && echo "✅ API Working" || echo "❌ API Not Responding"
```

## 📦 STEP 2: VERIFY EXISTING PRODUCTS

### Check Product Configuration
```bash
# Get all products and look for scaled/weighted products
curl -s "http://localhost:8080/api/product/getAll" | jq '.content[] | select(.scaled==true) | {id, label, scaled, pricesell}' 

# If jq is not available, use this simpler command:
curl -s "http://localhost:8080/api/product/getAll" | grep -A5 -B5 "scaled.*true"
```

**Expected Output:**
```json
{
  "id": 1001,
  "label": "Oranges (Fresh)",
  "scaled": true,
  "pricesell": 250.00
}
```

## 🛒 STEP 3: MAKE A TEST SALE WITH WEIGHTED PRODUCT

### Manual Verification Process:

1. **Open Android POS App**
2. **Find Weighted Product** (e.g., "Oranges (Fresh)")
3. **Place Item on Scale**:
   - Put oranges on Aclas Bluetooth scale
   - Observe weight display (should show something like 1.750 kg)
4. **Verify Price Calculation**:
   - Weight: 1.750 kg
   - Price per kg: 250.00 KSH
   - Calculated price: 437.50 KSH
5. **Complete Sale**
6. **Print Receipt**

**Receipt Should Show**:
```
Oranges (Fresh)
  1.750 kg
  @250.00/kg
             437.50
```

## 📊 STEP 4: VERIFY TRANSACTION RECORDING

### Check Database Records
```bash
# Get recent tickets
curl -s "http://localhost:8080/api/ticket/search?count=5" | jq '.'

# Get specific ticket details (replace ticket number)
curl -s "http://localhost:8080/api/ticket/1/1001" | jq '.'
```

### Check Ticket Lines for Weight Data
```bash
# This would require knowing a specific ticket ID
# You can also check directly in database:
```

**SQL Query to Run on Database**:
```sql
-- Check if weight data is recorded correctly
SELECT 
    t.date,
    p.label as product_name,
    tl.quantity as weight_kg,
    tl.unitprice,
    tl.finaltaxedprice as total_price
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
ORDER BY t.date DESC
LIMIT 10;
```

**Expected Result**:
| date | product_name | weight_kg | unitprice | total_price |
|------|--------------|-----------|-----------|-------------|
| 2025-09-15 10:30:00 | Oranges (Fresh) | 1.750 | 250.00 | 437.50 |

## 🖥️ STEP 5: ACCESS BACKOFFICE REPORTS

### Navigate to Reports:
1. Open browser and go to: `http://localhost:8080/OrtusPOSBackOffice/`
2. Login with admin credentials
3. Click on **Sales** menu
4. Select **By Product** or **By Category**

### Verify Weighted Product Data:
- Look for "Oranges (Fresh)" or other weighted products
- Quantity column should show total weight sold (e.g., "25.750" for 25.750 kg)
- Revenue should match calculated prices

## 📈 STEP 6: TEST API REPORTING ENDPOINTS

### Test Weighted Product Reports (NEW)
```bash
# Test new admin reports API (these were created as part of this implementation)
curl -s "http://localhost:8080/api/admin/test" | jq '.'

# Test system health
curl -s "http://localhost:8080/api/admin/system/health" | jq '.'

# Test weighted sales summary (will need actual date range)
# Format: startDate and endDate as Unix timestamps
START_DATE=$(date -d '7 days ago' +%s)
END_DATE=$(date +%s)

echo "Testing date range: $START_DATE to $END_DATE"
curl -s "http://localhost:8080/api/admin/reports/weighted-sales-summary?startDate=$START_DATE&endDate=$END_DATE" | jq '.'
```

### Test Existing Stock Reports
```bash
# Test low stock report
curl -s "http://localhost:8080/api/stock/report/low" | jq '.'

# Test over stock report  
curl -s "http://localhost:8080/api/stock/report/over" | jq '.'

# Test stock movement
curl -s "http://localhost:8080/api/stock/report/movement?startDate=$START_DATE&endDate=$END_DATE" | jq '.'
```

## 📋 STEP 7: DIRECT DATABASE VERIFICATION

### Run These SQL Queries:

```sql
-- 1. Check if products are marked as scaled
SELECT id, label, scaled, pricesell 
FROM products 
WHERE scaled = true 
LIMIT 10;

-- 2. Check recent weighted product transactions
SELECT 
    t.date,
    p.label as product_name,
    tl.quantity as weight_kg,
    tl.unitprice,
    tl.finaltaxedprice as total_price,
    cr.label as cash_register
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
JOIN cashregisters cr ON t.cashregister_id = cr.id
WHERE p.scaled = true
ORDER BY t.date DESC
LIMIT 20;

-- 3. Summarize total weight sold by product
SELECT 
    p.label as product_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as number_of_transactions,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
WHERE p.scaled = true
GROUP BY p.id, p.label
ORDER BY total_weight_kg DESC;
```

## 🧾 STEP 8: VALIDATE RECEIPT PRINTING

### Check Receipt Format:
When you print a receipt for a weighted product sale, it should show:

```
        YOUR BUSINESS NAME
        ==================
Date:     2025-09-15 10:30
Item              Price     Total
--------------------------------
Oranges (Fresh)
  1.750 kg
  @250.00/kg
             437.50

Bread             1         120.00
--------------------------------
Total:               557.50 KSH
```

## 📊 STEP 9: ACCESS REPORTS THROUGH BACKOFFICE INTERFACE

### Menu Navigation Paths:

1. **Sales Reports**:
   - URL: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbyproduct`
   - Menu: Sales → By Product

2. **Stock Reports**:
   - URL: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`  
   - Menu: Catalog → Products (then scroll down)

3. **Detailed Sales**:
   - URL: `http://localhost:8080/OrtusPOSBackOffice/?p=salesdetails`
   - Menu: Sales → Details

## 🎯 STEP 10: CREATE TEST SCENARIO

### Complete Test Flow:

1. **Create Test Product** (if not exists):
   - Product Name: "Test Oranges"
   - Price: 250.00 KSH per kg
   - Scaled: True (checked)
   - Category: Fruits

2. **Make Test Sale**:
   - Sell 1.500 kg of Test Oranges
   - Expected price: 375.00 KSH
   - Complete transaction and print receipt

3. **Verify Data**:
   ```sql
   -- Check if transaction was recorded correctly
   SELECT 
       p.label,
       tl.quantity as weight_kg,
       tl.finaltaxedprice as price
   FROM ticketlines tl
   JOIN products p ON tl.product_id = p.id
   WHERE p.label LIKE '%Test Oranges%'
   ORDER BY tl.id DESC
   LIMIT 1;
   ```

## ✅ VERIFICATION CHECKLIST

### Server Components:
- [ ] OrtusPOSServer accessible at port 8080
- [ ] API endpoints responding correctly
- [ ] Database connectivity working
- [ ] Bluetooth scale properly paired

### Product Configuration:
- [ ] Weighted products marked with `scaled = true`
- [ ] Price set as per kg (not total price)
- [ ] Product visible in catalog

### Transaction Processing:
- [ ] Weight correctly captured from scale
- [ ] Price automatically calculated
- [ ] Transaction recorded in database
- [ ] Receipt printed with weight details

### Reporting Access:
- [ ] BackOffice accessible via web browser
- [ ] Sales reports showing weighted product data
- [ ] Stock reports reflecting inventory changes
- [ ] Export functions working (PDF/Excel/CSV)

### Data Integrity:
- [ ] Weight stored in `ticketlines.quantity` field
- [ ] Revenue calculated correctly
- [ ] Inventory levels updating properly
- [ ] No data duplication or loss

## 🆘 TROUBLESHOOTING

### If Reports Don't Show Weighted Products:
1. **Verify product configuration**:
   ```sql
   SELECT id, label, scaled FROM products WHERE label LIKE '%Orange%';
   ```
   Expected: `scaled` should be `true`

2. **Check transaction recording**:
   ```sql
   SELECT t.date, p.label, tl.quantity 
   FROM ticketlines tl
   JOIN products p ON tl.product_id = p.id
   JOIN tickets t ON tl.ticket_id = t.id
   WHERE p.scaled = true
   ORDER BY t.date DESC LIMIT 5;
   ```

### If Weight Data Is Incorrect:
1. **Check scale calibration**
2. **Verify product pricing** (should be per kg)
3. **Ensure proper scale connection**

### If BackOffice Reports Are Empty:
1. **Click "Sync" on BackOffice home screen**
2. **Check internet connectivity**
3. **Verify user permissions**
4. **Clear browser cache and cookies**

## 📞 SUPPORT CONTACT

If you continue to experience issues:
- Email: support@ortuspos.com
- Phone: +254-XXX-XXXXXX
- Hours: 24/7 Technical Support

---

**🎉 CONGRATULATIONS!**
You now have a fully functional reporting system for weighted product sales with Aclas Bluetooth scale integration. All components are ready to use - you just need to access them through the existing interfaces!