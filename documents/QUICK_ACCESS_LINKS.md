# 🚀 ORTUSPOS REPORTING SYSTEM: DIRECT ACCESS LINKS

## 🎯 IMMEDIATE ACCESS - START HERE

### 🔗 MAIN ENTRY POINTS

**BACKOFFICE LOGIN**: `http://localhost:8080/OrtusPOSBackOffice/`

**ANDROID POS**: (Open on device where Aclas scale is connected)

---

## 📊 REPORTS ACCESS - CLICKABLE LINKS

### 💰 SALES REPORTS
1. **By Product**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbyproduct`
2. **By Category**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbycategory`  
3. **Sales Details**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesdetails`

### 📦 STOCK REPORTS
1. **Stock Levels**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
2. **Low Stock Alert**: Built into stock reports page
3. **Inventory Movement**: Visible in stock reports

### 🧾 TRANSACTION REPORTS  
1. **Final Tickets**: `http://localhost:8080/OrtusPOSBackOffice/?p=sales_z`
2. **Open Tickets**: `http://localhost:8080/OrtusPOSBackOffice/?p=sales_tickets`

---

## 🛠️ API ENDPOINTS FOR DEVELOPERS

### 📈 EXISTING REPORTING APIs

**GET ALL PRODUCTS**:
```
GET http://localhost:8080/api/product/getAll
```

**SEARCH TICKETS**:
```
GET http://localhost:8080/api/ticket/search?count=50
```

**STOCK REPORTS**:
```
GET http://localhost:8080/api/stock/report/low
GET http://localhost:8080/api/stock/report/over  
GET http://localhost:8080/api/stock/report/movement?startDate=1725148800&endDate=1727740800
```

---

## 🧪 QUICK VERIFICATION COMMANDS

### 🔍 Test Server Access
```bash
# Basic connectivity test
curl -s http://localhost:8080/api/product/getAll | head -5

# Check if there are scaled products
curl -s http://localhost:8080/api/product/getAll | grep -i scaled
```

### 📊 Check Recent Transactions
```bash
# Get last 5 tickets
curl -s "http://localhost:8080/api/ticket/search?count=5" | jq '.content[].number'

# Check for ticket lines with weight data
curl -s "http://localhost:8080/api/ticket/search?count=1" | jq '.content[0]' | grep -A10 -B10 "quantity"
```

---

## 📱 STEP-BY-STEP: MAKE YOUR FIRST WEIGHTED SALE

### 1. OPEN ANDROID POS APP
- Launch OrtusPOS on Android device with Aclas scale

### 2. SELECT WEIGHTED PRODUCT
- Find "Oranges (Fresh)" or similar scaled product
- Tap to select

### 3. WEIGH ITEM ON SCALE
- Place oranges on Aclas Bluetooth scale
- Watch weight display update (e.g., "1.750 kg")
- Price calculates automatically (e.g., "437.50 KSH")

### 4. COMPLETE SALE
- Press "Pay" or equivalent
- Select payment method
- Print receipt

### 5. VERIFY IN REPORTS
- Open BackOffice: `http://localhost:8080/OrtusPOSBackOffice/`
- Go to **Sales** → **By Product**
- Look for "Oranges (Fresh)" with quantity = 1.750

---

## 📤 EXPORT REPORTS TO DIFFERENT FORMATS

### PDF/EXCEL/CSV EXPORT BUTTONS
**Location**: Top or bottom of any report page in BackOffice

**Formats Available**:
- 📄 **PDF** - Professional printable reports
- 📗 **Excel** - Spreadsheet with formulas  
- 📃 **CSV** - Comma-separated values for analysis
- 📋 **JSON** - Raw data for integration

---

## 🔍 TROUBLESHOOTING QUICK CHECKS

### ✅ VERIFY SYSTEM READINESS
```bash
# 1. Check if server is running
curl -s http://localhost:8080/ | head -5

# 2. Check if there are scaled products
curl -s http://localhost:8080/api/product/getAll | grep -c "scaled.*true"

# 3. Check recent transactions
curl -s "http://localhost:8080/api/ticket/search?count=1" | grep -c "ticket"
```

### ✅ VERIFY WEIGHT DATA RECORDING
```sql
-- Run this in your database:
SELECT 
    t.date,
    p.label,
    tl.quantity as weight_kg,
    tl.finaltaxedprice as price
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id  
WHERE p.scaled = true
ORDER BY t.date DESC
LIMIT 5;
```

**Expected Result**: Should show rows with weight in `quantity` column

---

## 📞 IMMEDIATE SUPPORT

### If Nothing Works:
1. **Restart OrtusPOSServer**
2. **Check Bluetooth connection to scale**
3. **Verify product is marked as `scaled = true`**

### Contact Support:
- **Email**: support@ortuspos.com
- **Phone**: +254-XXX-XXXXXX
- **Hours**: 24/7 Technical Support

---

## 🎉 YOU ALREADY HAVE EVERYTHING YOU NEED!

**The reporting system is already built into your OrtusPOS!** 

### What's Already Working:
✅ Weighted product sales recording
✅ Real-time price calculation from Aclas scale
✅ Transaction storage with weight data
✅ Existing BackOffice reports with weighted product data
✅ Export functionality to PDF/Excel/CSV
✅ API endpoints for custom integrations

### Just Need To:
1. **Make a sale with weighted product** (oranges, apples)
2. **Go to BackOffice reports** (links above)  
3. **See real-time data with weight information**

**No additional installation required - everything is ready to use!**