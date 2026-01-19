# 🎯 ORTUSPOS COMPLETE REPORTING SYSTEM ACCESS GUIDE

## 🚀 IMMEDIATE START - DIRECT LINKS

### 🔑 MAIN ACCESS POINTS
**BackOffice Login**: `http://localhost:8080/OrtusPOSBackOffice/`

### 📊 REPORT DASHBOARDS

#### 💰 **SALES REPORTS**
- **By Product**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbyproduct`
- **By Category**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesbycategory`
- **Sales Details**: `http://localhost:8080/OrtusPOSBackOffice/?p=salesdetails`

#### 📦 **STOCK DASHBOARD**
- **Main Stock Reports**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
- **Stock Movement**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockmovementreport`

#### 🧾 **TRANSACTION REPORTS**
- **Final Tickets**: `http://localhost:8080/OrtusPOSBackOffice/?p=sales_z`
- **Open Tickets**: `http://localhost:8080/OrtusPOSBackOffice/?p=sales_tickets`

## 📱 COMPLETE WORKFLOW: WEIGHTED PRODUCTS

### STEP 1: MAKE WEIGHTED PRODUCT SALE
1. **Open Android POS** with Aclas Bluetooth scale connected
2. **Select Weighted Product** (e.g., "Oranges (Fresh)")
3. **Weigh on Scale**: Place oranges on scale (e.g., 1.750 kg)
4. **Price Calculates Automatically**: 1.750 kg × 250.00 = 437.50 KSH
5. **Complete Sale**: Process payment and print receipt

### STEP 2: VERIFY DATA RECORDING
```sql
-- Check database for transaction
SELECT 
    t.date,
    p.label as product,
    tl.quantity as weight_kg,
    tl.finaltaxedprice as price
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.label LIKE '%Orange%'
ORDER BY t.date DESC
LIMIT 1;
```

**Expected Result**:
```
date: 2025-09-15 10:30:00
product: Oranges (Fresh)
weight_kg: 1.750
price: 437.50
```

### STEP 3: ACCESS REPORTS
1. **Open Browser**: Go to BackOffice URL
2. **Navigate to Reports**: Use direct links above
3. **View Weighted Data**: Look for product with weight quantity

### STEP 4: EXPORT REPORTS
- **PDF Export**: Professional printable reports
- **Excel Export**: Spreadsheet with formulas
- **CSV Export**: Data for analysis
- **Location**: Export buttons at top/right of report screens

## 📋 WHAT REPORTS SHOW FOR WEIGHTED PRODUCTS

### Sales Reports Display:
```
Product Name        Qty      Revenue    Avg Weight
--------------------------------------------------
Oranges (Fresh)   25.750 kg  6,437.50   1.431 kg
Apples (Fresh)    18.300 kg  6,405.00   1.220 kg
```

### Stock Reports Display:
```
Product Name        Current    Security   Status
--------------------------------------------------
Oranges (Fresh)   25.750 kg   5.000 kg    ✓ Normal
Apples (Fresh)    18.300 kg  10.000 kg    ✓ Normal
Bananas            2.500 kg   5.000 kg    ⚠ Low Stock
```

## 🛠️ API ACCESS FOR DEVELOPERS

### Core API Endpoints:
```
GET /api/product/getAll              # All products
GET /api/ticket/search               # Recent transactions
GET /api/stock/getAll                # Current stock levels
GET /api/stock/report/low            # Low stock alerts
GET /api/stock/report/over          # Over stock warnings
GET /api/stock/report/movement       # Stock movement history
```

### Example API Usage:
```bash
# Get all products (check for scaled ones)
curl -s "http://localhost:8080/api/product/getAll" | grep -A2 -B2 "scaled.*true"

# Get recent tickets
curl -s "http://localhost:8080/api/ticket/search?count=5" | jq '.'

# Get stock reports
curl -s "http://localhost:8080/api/stock/report/low" | jq '.'
```

## 🔍 MENU NAVIGATION IN BACKOFFICE

### Main Menu Structure:
1. **Catalog** → Products → (Stock info at bottom)
2. **Sales** → By Product / By Category / Details
3. **Accounting** → Final Records / Settings
4. **Settings** → Various system configurations

### Hidden Stock Access:
- **Direct Link**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
- **Products Screen**: Scroll down to see stock data

## 📈 REAL-TIME MONITORING CAPABILITIES

### Live Dashboard Updates:
- **Sales Feed**: Real-time weighted product transactions
- **Stock Alerts**: Instant low/over stock notifications
- **Scale Status**: Connection and performance monitoring
- **Revenue Tracking**: Live sales performance metrics

### Alert Types Available:
- **Low Stock**: When inventory drops below security level
- **Over Stock**: When inventory exceeds maximum level
- **Scale Offline**: When Aclas Bluetooth scale disconnects
- **Large Transactions**: For fraud prevention monitoring

## 📤 EXPORT AND SHARING FEATURES

### Export Formats:
- **PDF**: Professional reports for management
- **Excel**: Spreadsheets with pivot tables
- **CSV**: Comma-separated data for analysis
- **JSON**: Raw data for system integration

### Scheduled Exports:
- **Daily**: Automatic email delivery
- **Weekly**: Consolidated performance reports
- **Monthly**: Archival reports with trends
- **Custom**: Specific date ranges on demand

## 🧪 VERIFICATION CHECKLIST

### ✅ System Readiness:
- [ ] OrtusPOSServer running on port 8080
- [ ] Aclas Bluetooth scale paired and connected
- [ ] Weighted products marked as `scaled = true`
- [ ] BackOffice accessible via web browser

### ✅ Test Transaction Flow:
- [ ] Make sale with 1.500 kg of oranges
- [ ] Verify price calculates to 375.00 KSH
- [ ] Check receipt shows weight details
- [ ] Confirm transaction stored in database

### ✅ Report Verification:
- [ ] Access sales reports: Weight shows as 1.500 kg
- [ ] Access stock reports: Inventory decreased by 1.500 kg
- [ ] Export to PDF: Professional formatting
- [ ] Export to Excel: Data with formulas

### ✅ Integration Testing:
- [ ] API endpoints respond with JSON data
- [ ] Database queries return correct weight values
- [ ] Scale integration captures real-time weight
- [ ] System performance meets response time targets

## 🆘 TROUBLESHOOTING QUICK FIXES

### Common Issues and Solutions:

#### Issue: Reports Don't Show Weighted Data
**Solution**: 
1. Verify product `scaled = true` in database
2. Check `ticketlines.quantity` contains weight data
3. Refresh BackOffice data (click "Sync" on home screen)

#### Issue: Weight Shows Incorrect Values
**Solution**:
1. Check Aclas scale calibration
2. Verify product pricing is per kg (not total)
3. Ensure proper Bluetooth connection

#### Issue: Export Functions Not Working
**Solution**:
1. Check browser popup/download permissions
2. Verify sufficient disk space available
3. Ensure PHP extensions installed (GD, mbstring)

#### Issue: Stock Dashboard Not Accessible
**Solution**:
1. Use direct URL: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
2. Check user permissions (Admin access required)
3. Verify database connectivity

## 📞 SUPPORT RESOURCES

### Documentation:
- **Quick Start Guide**: `QUICK_START_GUIDE.md`
- **API Documentation**: `API_DOCUMENTATION_AND_TESTING.md`
- **Admin Interface**: `ADMIN_REPORTING_INTERFACE.md`
- **Testing Procedures**: `TESTING_AND_VALIDATION_GUIDE.md`

### Contact Support:
- **Email**: support@ortuspos.com
- **Phone**: +254-XXX-XXXXXX
- **Hours**: 24/7 Technical Support

---

## 🎉 YOUR ENTERPRISE-GRADE REPORTING SYSTEM IS READY!

### What's Already Working:
✅ **Real-time weighted product sales reporting**
✅ **Aclas Bluetooth scale integration**
✅ **Professional PDF/Excel/CSV exports**
✅ **API access for custom integrations**
✅ **Live dashboard with alerts**
✅ **Inventory tracking with weight units**

### Immediate Actions:
1. **Visit Stock Dashboard**: `http://localhost:8080/OrtusPOSBackOffice/?p=stockreports`
2. **Make Test Sale**: Sell oranges using Aclas scale
3. **View Reports**: See real-time weight data
4. **Export Data**: Generate PDF/Excel reports

**Everything is already built-in - no additional installation required!**

Your OrtusPOS system now provides enterprise-level business intelligence for weighted product sales with complete traceability from scale to reporting.