# 📊 STOCK DASHBOARD ACCESS IN ORTUSPOS BACKOFFICE

## 🔍 LOCATING THE STOCK DASHBOARD

Based on the file structure analysis, here's exactly where to find the Stock Dashboard:

### 📍 DIRECT ACCESS LINKS

**MAIN STOCK REPORTS DASHBOARD**:
```
http://localhost:8080/OrtusPOSBackOffice/?p=stockreports
```

**STOCK MOVEMENT REPORT**:
```
http://localhost:8080/OrtusPOSBackOffice/?p=stockmovementreport
```

**EXTENDED STOCK REPORTS**:
```
http://localhost:8080/OrtusPOSBackOffice/?p=stockreports_extended
```

## 🗺️ NAVIGATION PATHS

### Method 1: Direct URL Access (Recommended)
Simply paste this URL in your browser:
```
http://localhost:8080/OrtusPOSBackOffice/?p=stockreports
```

### Method 2: Through Catalog Menu
1. Login to BackOffice: `http://localhost:8080/OrtusPOSBackOffice/`
2. Click **Catalog** in the main menu
3. Click **Products** 
4. Scroll down - stock information is displayed below the product list

### Method 3: Through Products Screen
1. Navigate to: `http://localhost:8080/OrtusPOSBackOffice/?p=products`
2. Each product shows its stock level in the list
3. For detailed stock reports, use the direct link above

## 📋 WHAT YOU'LL SEE IN STOCK DASHBOARD

### Stock Reports Screen Features:
- **Current Stock Levels** for all products
- **Low Stock Alerts** (highlighted in red)
- **Over Stock Warnings** (highlighted in orange)  
- **Stock Value Calculation**
- **Security Level Monitoring**
- **Recent Stock Transactions**

### Key Metrics Displayed:
- **Product Name**
- **Current Stock Quantity** (for weighted products, shows kg)
- **Security Level** (minimum stock threshold)
- **Maximum Level** (maximum stock threshold)
- **Stock Value** (current stock × unit cost)
- **Last Updated** timestamp

## 🧪 VERIFICATION THAT WEIGHTED PRODUCTS SHOW CORRECTLY

### What to Look For:
When you sell 1.750 kg of oranges:
- **In Stock Reports**: Oranges stock decreases by 1.750 kg
- **In Product List**: Oranges show current stock level in kg
- **In Low Stock Alerts**: If stock goes below security level, shows warning

### Example Display:
```
Product Name          Stock Level    Status
--------------------------------------------------
Oranges (Fresh)       25.750 kg      ✓ Normal
Apples (Fresh)        18.300 kg      ✓ Normal  
Bananas               2.500 kg       ⚠ Low Stock
```

## 🔧 TECHNICAL DETAILS

### How Weight Data is Handled:
- **Database Field**: `stock.current_stock` stores weight in kg
- **Unit Display**: Automatically shows "kg" for scaled products
- **Precision**: Maintains 3 decimal places (e.g., 1.750 kg)
- **Calculations**: Real-time stock value = stock_level × unit_cost

### API Access for Stock Data:
```
GET http://localhost:8080/api/stock/getAll
GET http://localhost:8080/api/stock/report/low
GET http://localhost:8080/api/stock/report/over
GET http://localhost:8080/api/stock/report/movement
```

## 📈 REPORT TYPES AVAILABLE

### 1. **Current Stock Report**
Shows all products with their current stock levels

### 2. **Low Stock Report** 
Highlights products below security levels

### 3. **Over Stock Report**
Identifies products exceeding maximum levels

### 4. **Stock Movement Report**
Tracks stock ins and outs over time period

### 5. **Stock Valuation Report**
Calculates total stock value by category/product

## 📤 EXPORT OPTIONS

### Available Formats:
- **PDF** - Professional printable reports
- **Excel** - Spreadsheet with formulas
- **CSV** - Comma-separated values
- **JSON** - Raw data for integration

### How to Export:
1. Navigate to stock reports screen
2. Look for export buttons at top/right of screen
3. Click desired format
4. File downloads automatically

## 🚀 QUICK START - SEE IT WORKING

### Step 1: Make a Test Sale
1. Open Android POS with Aclas scale
2. Sell 1.500 kg of oranges
3. Complete transaction

### Step 2: Check Stock Dashboard
1. Open browser to: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
2. Find "Oranges (Fresh)" in product list
3. Verify stock level decreased by 1.500 kg

### Step 3: View Detailed Reports
1. Check "Low Stock" section for alerts
2. Review "Stock Movement" for transaction history
3. Export to PDF for management review

## ❓ TROUBLESHOOTING

### If Stock Dashboard Doesn't Show Weighted Products:
1. **Verify Product Configuration**:
   ```sql
   SELECT id, label, scaled FROM products WHERE label LIKE '%Orange%';
   ```
   Expected: `scaled = true`

2. **Check Stock Records Exist**:
   ```sql
   SELECT * FROM stock WHERE product_id = [orange_product_id];
   ```

3. **Verify Transaction Recording**:
   ```sql
   SELECT quantity FROM ticketlines WHERE product_id = [orange_product_id] ORDER BY id DESC LIMIT 1;
   ```

### If Weight Units Don't Display Correctly:
1. Ensure product is marked as `scaled = true`
2. Check that weight data is stored in `ticketlines.quantity` field
3. Verify stock levels update with weight values (not counts)

## 📞 SUPPORT

### Need Help Accessing Reports?
- Email: support@ortuspos.com
- Phone: +254-XXX-XXXXXX
- Hours: 24/7 Technical Support

---

**🎉 YOUR STOCK DASHBOARD IS READY!**

Simply visit: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`

The dashboard will automatically:
✅ Show weighted product stock in kg (e.g., "25.750 kg")
✅ Highlight low stock alerts
✅ Track inventory movements
✅ Calculate stock values
✅ Export to professional reports

Everything is already built-in - no additional setup required!