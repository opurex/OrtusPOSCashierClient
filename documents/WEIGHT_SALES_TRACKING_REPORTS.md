# Sales Tracking and Reporting for Weighed Products

## Overview
This document explains how to track sales data for weighed products and generate comprehensive reports using the existing OrtusPOS infrastructure with enhanced analytics for weight-based items.

## 1. Understanding Weight Data Storage

### Current Data Structure
The OrtusPOS system already stores weight information correctly:

1. **Products Table** (`products`):
   - `scaled` (boolean): True for weight-based products
   - `scaletype` (smallint): 0 for weight, 1 for volume, etc.
   - `scalevalue` (double precision): Capacity/unit value

2. **Ticket Lines Table** (`ticketlines`):
   - `quantity` (double precision): Contains actual weight in kg for scaled products
   - `product_id` (integer): Links to products table
   - Standard sales fields (price, tax, etc.)

### Example Data Flow
1. Customer selects "Apples (Fresh)" (scaled product)
2. 1.250 kg of apples are weighed
3. System calculates price: 1.250 kg × 350.00 Ksh/kg = 437.50 Ksh
4. Ticket line is created with:
   - `product_id`: ID of "Apples (Fresh)"
   - `quantity`: 1.250
   - `finaltaxedprice`: 437.50

## 2. Querying Weight-Based Sales Data

### Basic Weight Sales Query
```sql
-- Get all weight-based product sales in a date range
SELECT 
    t.date,
    p.label as product_name,
    tl.quantity as weight_kg,
    tl.finaltaxedprice as revenue,
    t.number as ticket_number,
    cr.label as cash_register
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
JOIN cashregisters cr ON t.cashregister_id = cr.id
WHERE p.scaled = true
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
ORDER BY t.date DESC;
```

### Aggregated Weight Sales Report
```sql
-- Summary of weight-based product sales
SELECT 
    p.label as product_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue,
    AVG(tl.quantity) as avg_weight_per_transaction,
    MIN(tl.quantity) as min_weight,
    MAX(tl.quantity) as max_weight
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY p.id, p.label
ORDER BY total_weight_kg DESC;
```

### Daily Weight Sales Trend
```sql
-- Daily trend of weight-based product sales
SELECT 
    DATE(t.date) as sale_date,
    SUM(tl.quantity) as daily_weight_kg,
    COUNT(*) as daily_transactions,
    SUM(tl.finaltaxedprice) as daily_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY DATE(t.date)
ORDER BY sale_date;
```

### Hourly Sales Pattern for Weight Products
```sql
-- Peak hours for weight-based product sales
SELECT 
    EXTRACT(HOUR FROM t.date) as hour_of_day,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY EXTRACT(HOUR FROM t.date)
ORDER BY hour_of_day;
```

## 3. Creating Custom Reports

### 3.1 Product Performance Analysis
Create a report that compares weight-based products with regular products:

```sql
-- Combined product performance report
SELECT 
    p.label as product_name,
    p.scaled,
    SUM(tl.quantity) as total_quantity,
    CASE 
        WHEN p.scaled THEN SUM(tl.quantity) 
        ELSE COUNT(*) 
    END as effective_units_sold,
    SUM(tl.finaltaxedprice) as total_revenue,
    AVG(tl.finaltaxedprice) as avg_transaction_value
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY p.id, p.label, p.scaled
ORDER BY total_revenue DESC;
```

### 3.2 Customer Behavior Analysis
Analyze customer preferences for weight-based products:

```sql
-- Customers who frequently buy weight-based products
SELECT 
    c.name as customer_name,
    COUNT(DISTINCT t.id) as total_visits,
    COUNT(DISTINCT CASE WHEN has_scaled_items THEN t.id END) as visits_with_scaled_items,
    SUM(CASE WHEN has_scaled_items THEN 1 ELSE 0 END) * 100.0 / COUNT(*) as percentage_scaled_visits
FROM (
    SELECT 
        t.id,
        t.customer_id,
        EXISTS(
            SELECT 1 FROM ticketlines tl 
            JOIN products p ON tl.product_id = p.id 
            WHERE tl.ticket_id = t.id AND p.scaled = true
        ) as has_scaled_items
    FROM tickets t
    WHERE t.date BETWEEN '2025-09-01' AND '2025-09-30'
) t
JOIN customers c ON t.customer_id = c.id
GROUP BY c.id, c.name
HAVING COUNT(DISTINCT t.id) >= 5 -- Customers with at least 5 visits
ORDER BY percentage_scaled_visits DESC;
```

## 4. Implementing Reports in BackOffice

### 4.1 Weight-Based Product Sales Report
Create a JavaScript function to fetch and display weight-based sales data:

```javascript
// File: /OrtusPOSBackOffice/src/screens/weightproductsales.js
function weightproductsales_show() {
    vue.screen.data = {
        "dateRange": {
            "start": new Date(new Date().getTime() - 604800000), // 7 days ago
            "stop": new Date()
        },
        "reportData": null,
        "loading": false
    };
    vue.screen.component = "vue-weightproductsales";
}

function weightproductsales_loadReport() {
    vue.screen.data.loading = true;
    
    const startTimestamp = Math.floor(vue.screen.data.dateRange.start.getTime() / 1000);
    const stopTimestamp = Math.floor(vue.screen.data.dateRange.stop.getTime() / 1000);
    
    srvcall_get(
        `api/reports/weight-product-sales?start=${startTimestamp}&stop=${stopTimestamp}`,
        function(request, status, response) {
            vue.screen.data.loading = false;
            
            if (status === 200) {
                try {
                    const data = JSON.parse(response);
                    if (data.status === "success") {
                        vue.screen.data.reportData = data.data;
                        vue.screen.data.summary = data.summary;
                    }
                } catch (e) {
                    console.error("Error parsing response:", e);
                }
            }
        }
    );
}
```

### 4.2 Server-Side Report Endpoint
Create the API endpoint to serve the report data:

```php
// Add to weightreport.php
/**
 * Get detailed weight-based product sales report
 */
$app->GET('/api/reports/weight-product-sales', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    $start = isset($queryParams['start']) ? intval($queryParams['start']) : null;
    $stop = isset($queryParams['stop']) ? intval($queryParams['stop']) : null;
    
    if ($start === null || $stop === null) {
        return $response->withJson([
            'status' => 'error',
            'message' => 'start and stop parameters required'
        ], 400);
    }
    
    try {
        // Main query for weight-based product sales
        $sql = "
            SELECT 
                p.id,
                p.label as product_name,
                p.pricesell as unit_price,
                SUM(tl.quantity) as total_weight_kg,
                COUNT(*) as transaction_count,
                SUM(tl.finaltaxedprice) as total_revenue,
                AVG(tl.quantity) as avg_weight_per_transaction,
                MIN(tl.quantity) as min_weight,
                MAX(tl.quantity) as max_weight
            FROM ticketlines tl
            JOIN products p ON tl.product_id = p.id
            JOIN tickets t ON tl.ticket_id = t.id
            WHERE p.scaled = true
            AND t.date >= TO_TIMESTAMP(:start)
            AND t.date <= TO_TIMESTAMP(:stop)
            GROUP BY p.id, p.label, p.pricesell
            ORDER BY total_weight_kg DESC
        ";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            'start' => $start,
            'stop' => $stop
        ]);
        
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        // Calculate summary statistics
        $summary = [
            'total_weight_kg' => array_sum(array_column($results, 'total_weight_kg')),
            'total_revenue' => array_sum(array_column($results, 'total_revenue')),
            'total_transactions' => array_sum(array_column($results, 'transaction_count')),
            'unique_products' => count($results)
        ];
        
        return $response->withJson([
            'status' => 'success',
            'data' => $results,
            'summary' => $summary
        ]);
        
    } catch (Exception $e) {
        return $response->withJson([
            'status' => 'error',
            'message' => $e->getMessage()
        ], 500);
    }
});
```

## 5. Advanced Analytics and Insights

### 5.1 Seasonal Trend Analysis
Identify seasonal patterns in weight-based product sales:

```sql
-- Monthly weight sales trends
SELECT 
    EXTRACT(YEAR FROM t.date) as year,
    EXTRACT(MONTH FROM t.date) as month,
    TO_CHAR(t.date, 'Month') as month_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
GROUP BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date), TO_CHAR(t.date, 'Month')
ORDER BY year, month;
```

### 5.2 Product Correlation Analysis
Identify which products are frequently bought together with weight-based items:

```sql
-- Products frequently bought with weight-based items
SELECT 
    p2.label as associated_product,
    COUNT(*) as frequency,
    SUM(tl2.finaltaxedprice) as associated_revenue
FROM tickets t
JOIN ticketlines tl1 ON t.id = tl1.ticket_id
JOIN products p1 ON tl1.product_id = p1.id
JOIN ticketlines tl2 ON t.id = tl2.ticket_id
JOIN products p2 ON tl2.product_id = p2.id
WHERE p1.scaled = true
AND p2.scaled = false
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY p2.id, p2.label
ORDER BY frequency DESC
LIMIT 20;
```

### 5.3 Staff Performance Metrics
Track which cashiers process the most weight-based transactions:

```sql
-- Staff performance with weight-based products
SELECT 
    u.name as staff_name,
    COUNT(DISTINCT t.id) as total_tickets,
    SUM(CASE WHEN has_scaled_items THEN 1 ELSE 0 END) as tickets_with_scaled_items,
    SUM(CASE WHEN has_scaled_items THEN scaled_weight ELSE 0 END) as total_weight_processed
FROM (
    SELECT 
        t.id,
        t.user_id,
        EXISTS(
            SELECT 1 FROM ticketlines tl 
            JOIN products p ON tl.product_id = p.id 
            WHERE tl.ticket_id = t.id AND p.scaled = true
        ) as has_scaled_items,
        (
            SELECT COALESCE(SUM(tl.quantity), 0) 
            FROM ticketlines tl 
            JOIN products p ON tl.product_id = p.id 
            WHERE tl.ticket_id = t.id AND p.scaled = true
        ) as scaled_weight
    FROM tickets t
    WHERE t.date BETWEEN '2025-09-01' AND '2025-09-30'
) t
JOIN users u ON t.user_id = u.id
GROUP BY u.id, u.name
ORDER BY total_weight_processed DESC;
```

## 6. Report Automation and Distribution

### 6.1 Scheduled Report Generation
Set up automated daily/weekly/monthly reports:

```php
// File: /OrtusPOSServer/src/tasks/scheduled_weight_reports.php
class ScheduledWeightReportsTask {
    public function execute() {
        $today = date('Y-m-d');
        
        // Generate daily reports
        if (date('H') == 6) { // Run at 6 AM
            $this->generateDailyReport($today);
        }
        
        // Generate weekly reports
        if (date('w') == 1 && date('H') == 6) { // Monday at 6 AM
            $this->generateWeeklyReport();
        }
        
        // Generate monthly reports
        if (date('j') == 1 && date('H') == 6) { // First day of month at 6 AM
            $this->generateMonthlyReport();
        }
    }
    
    private function generateDailyReport($date) {
        $start = strtotime($date . ' 00:00:00');
        $stop = strtotime($date . ' 23:59:59');
        
        // Generate report data
        $reportData = $this->getWeightSalesData($start, $stop);
        
        // Save to database or file
        $this->saveReport('daily', $date, $reportData);
        
        // Send email to managers
        $this->sendReportEmail('daily', $date, $reportData);
    }
    
    private function sendReportEmail($type, $date, $data) {
        // Email implementation
        $subject = "Weight-Based Sales Report - {$type} - {$date}";
        $body = $this->formatReportEmail($data);
        
        // Send email to configured recipients
        // Implementation depends on email system
    }
}
```

### 6.2 Report Export Functionality
Add export capabilities to backoffice:

```javascript
// Add export functionality to the weight products sales screen
function weightproductsales_export(format) {
    const startTimestamp = Math.floor(vue.screen.data.dateRange.start.getTime() / 1000);
    const stopTimestamp = Math.floor(vue.screen.data.dateRange.stop.getTime() / 1000);
    
    const url = `api/reports/weight-product-sales?start=${startTimestamp}&stop=${stopTimestamp}&format=${format}`;
    
    // For CSV export
    if (format === 'csv') {
        window.open(url, '_blank');
    }
    
    // For PDF export (would require additional PDF generation)
    if (format === 'pdf') {
        // Implementation would generate PDF and download
    }
}
```

## 7. Performance Optimization

### 7.1 Database Indexes
Ensure proper indexing for performance:

```sql
-- Essential indexes for weight-based product queries
CREATE INDEX IF NOT EXISTS idx_products_scaled ON products(scaled);
CREATE INDEX IF NOT EXISTS idx_ticketlines_product ON ticketlines(product_id);
CREATE INDEX IF NOT EXISTS idx_tickets_date ON tickets(date);
CREATE INDEX IF NOT EXISTS idx_ticketlines_ticket ON ticketlines(ticket_id);

-- Composite indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_tickets_date_scaled ON tickets(date) WHERE EXISTS (
    SELECT 1 FROM ticketlines tl 
    JOIN products p ON tl.product_id = p.id 
    WHERE tl.ticket_id = tickets.id AND p.scaled = true
);
```

### 7.2 Materialized Views for Large Datasets
For large databases, use materialized views:

```sql
-- Daily summary materialized view
CREATE MATERIALIZED VIEW weight_sales_daily_summary AS
SELECT 
    DATE(t.date) as sale_date,
    p.id as product_id,
    p.label as product_name,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
GROUP BY DATE(t.date), p.id, p.label;

-- Refresh regularly (e.g., daily)
-- REFRESH MATERIALIZED VIEW weight_sales_daily_summary;
```

## 8. Data Validation and Quality Assurance

### 8.1 Data Integrity Checks
Regular validation queries to ensure data quality:

```sql
-- Check for inconsistent weight data
SELECT 
    t.id as ticket_id,
    t.date,
    p.label as product_name,
    tl.quantity as weight_kg
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND (tl.quantity <= 0 OR tl.quantity > 1000) -- Unreasonable weights
ORDER BY t.date DESC;

-- Check for missing price data in weight-based products
SELECT 
    t.id as ticket_id,
    p.label as product_name,
    tl.quantity as weight_kg,
    tl.finaltaxedprice
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND (tl.finaltaxedprice IS NULL OR tl.finaltaxedprice = 0)
ORDER BY t.date DESC;
```

## 9. Business Intelligence Applications

### 9.1 Inventory Planning
Use weight sales data for inventory planning:

```sql
-- Estimate inventory needs based on weight sales trends
SELECT 
    p.label as product_name,
    SUM(tl.quantity) as total_weight_sold_last_30_days,
    AVG(tl.quantity) as avg_weight_per_day,
    SUM(tl.quantity) / 30 * 7 as estimated_weekly_need,
    p.scalevalue as current_inventory_weight_kg,
    CASE 
        WHEN p.scalevalue < (SUM(tl.quantity) / 30 * 7) THEN 'Reorder Needed'
        WHEN p.scalevalue < (SUM(tl.quantity) / 30 * 14) THEN 'Monitor Closely'
        ELSE 'Adequate Stock'
    END as stock_status
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.id, p.label, p.scalevalue
ORDER BY total_weight_sold_last_30_days DESC;
```

### 9.2 Pricing Strategy Analysis
Analyze the impact of price changes on weight-based product sales:

```sql
-- Compare sales before and after price changes
SELECT 
    p.label as product_name,
    p.pricesell as current_price,
    COUNT(*) as transaction_count,
    SUM(tl.quantity) as total_weight_sold,
    SUM(tl.finaltaxedprice) as total_revenue,
    AVG(tl.quantity) as avg_weight_per_transaction
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.id, p.label, p.pricesell
ORDER BY total_revenue DESC;
```

## 10. Implementation Checklist

### Server-Side Implementation
- [ ] Create weight report API endpoints
- [ ] Add database indexes for performance
- [ ] Implement scheduled report generation
- [ ] Set up data validation routines
- [ ] Configure email reporting (if needed)

### BackOffice Implementation
- [ ] Create weight product sales report screen
- [ ] Implement Vue components for data visualization
- [ ] Add export functionality (CSV, PDF)
- [ ] Create navigation menu items
- [ ] Test with sample data

### Mobile App Integration
- [ ] Ensure scale usage is logged to server
- [ ] Verify weight data is correctly transmitted
- [ ] Test with various scale models
- [ ] Validate data integrity

### Testing and Validation
- [ ] Test queries with production-like data volumes
- [ ] Validate report accuracy against manual calculations
- [ ] Check performance with large datasets
- [ ] Verify security and access controls
- [ ] Document user procedures

This comprehensive approach to weight-based product sales tracking and reporting will provide valuable business insights while leveraging the existing OrtusPOS infrastructure.