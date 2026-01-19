# Technical Implementation Guide: Weight-Based Product Reporting

## Overview
This guide provides step-by-step instructions for implementing comprehensive reporting and analytics for weight-based products in the OrtusPOS system.

## 1. Database Schema Enhancements

### 1.1 Scale Device Tracking
Create tables to track scale devices and their usage:

```sql
-- Scale devices table
CREATE TABLE scale_devices (
    id SERIAL PRIMARY KEY,
    mac_address VARCHAR(17) UNIQUE NOT NULL,
    name VARCHAR(255),
    cashregister_id INTEGER REFERENCES cashregisters(id),
    registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_used TIMESTAMP
);

-- Scale usage log table
CREATE TABLE scale_usage_log (
    id SERIAL PRIMARY KEY,
    scale_id INTEGER REFERENCES scale_devices(id),
    ticket_id INTEGER REFERENCES tickets(id),
    weight_kg DECIMAL(10,3),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add index for performance
CREATE INDEX idx_scale_usage_ticket ON scale_usage_log(ticket_id);
CREATE INDEX idx_scale_usage_scale ON scale_usage_log(scale_id);
CREATE INDEX idx_scale_usage_timestamp ON scale_usage_log(timestamp);
```

## 2. API Endpoint Implementation

### 2.1 Weight Sales Summary Endpoint
Create a new PHP file for weight-based sales reporting:

**File: `/OrtusPOSServer/src/http/routes/weightreport.php`**

```php
<?php

use \Ortus\Server\Model\Ticket;
use \Ortus\Server\Model\Product;
use \Ortus\Server\System\DAO\DAOCondition;
use \Ortus\Server\System\API\APICaller;
use \Ortus\Server\System\API\APIResult;

/**
 * Get weight-based product sales summary
 */
$app->GET('/api/reports/weight-sales-summary', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    
    // Get parameters
    $dateStart = isset($queryParams['dateStart']) ? intval($queryParams['dateStart']) : null;
    $dateStop = isset($queryParams['dateStop']) ? intval($queryParams['dateStop']) : null;
    $cashRegisterId = isset($queryParams['cashRegister']) ? intval($queryParams['dateStop']) : null;
    
    if ($dateStart === null || $dateStop === null) {
        return $response->withJson([
            'status' => 'error',
            'message' => 'dateStart and dateStop parameters required'
        ], 400);
    }
    
    try {
        // Query for weight-based product sales
        $sql = "
            SELECT 
                p.id,
                p.label,
                SUM(tl.quantity) as total_weight_kg,
                COUNT(DISTINCT tl.ticket_id) as transaction_count,
                SUM(tl.finaltaxedprice) as total_revenue,
                AVG(tl.quantity) as avg_weight_per_transaction
            FROM ticketlines tl
            JOIN products p ON tl.product_id = p.id
            JOIN tickets t ON tl.ticket_id = t.id
            WHERE p.scaled = true 
            AND t.date >= TO_TIMESTAMP(:dateStart)
            AND t.date <= TO_TIMESTAMP(:dateStop)
        ";
        
        $params = [
            'dateStart' => $dateStart,
            'dateStop' => $dateStop
        ];
        
        if ($cashRegisterId) {
            $sql .= " AND t.cashregister_id = :cashRegisterId";
            $params['cashRegisterId'] = $cashRegisterId;
        }
        
        $sql .= " GROUP BY p.id, p.label ORDER BY total_weight_kg DESC";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $response->withJson([
            'status' => 'success',
            'data' => $results,
            'summary' => [
                'total_weight_kg' => array_sum(array_column($results, 'total_weight_kg')),
                'total_revenue' => array_sum(array_column($results, 'total_revenue')),
                'total_transactions' => array_sum(array_column($results, 'transaction_count'))
            ]
        ]);
        
    } catch (Exception $e) {
        return $response->withJson([
            'status' => 'error',
            'message' => $e->getMessage()
        ], 500);
    }
});

/**
 * Get detailed weight sales by product
 */
$app->GET('/api/reports/weight-sales-by-product', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    
    $productId = isset($queryParams['productId']) ? intval($queryParams['productId']) : null;
    $dateStart = isset($queryParams['dateStart']) ? intval($queryParams['dateStart']) : null;
    $dateStop = isset($queryParams['dateStop']) ? intval($queryParams['dateStop']) : null;
    
    if ($dateStart === null || $dateStop === null) {
        return $response->withJson([
            'status' => 'error',
            'message' => 'dateStart and dateStop parameters required'
        ], 400);
    }
    
    try {
        $sql = "
            SELECT 
                t.date,
                tl.quantity as weight_kg,
                tl.finaltaxedprice as revenue,
                t.number as ticket_number,
                cr.label as cash_register
            FROM ticketlines tl
            JOIN products p ON tl.product_id = p.id
            JOIN tickets t ON tl.ticket_id = t.id
            JOIN cashregisters cr ON t.cashregister_id = cr.id
            WHERE p.scaled = true 
            AND t.date >= TO_TIMESTAMP(:dateStart)
            AND t.date <= TO_TIMESTAMP(:dateStop)
        ";
        
        $params = [
            'dateStart' => $dateStart,
            'dateStop' => $dateStop
        ];
        
        if ($productId) {
            $sql .= " AND p.id = :productId";
            $params['productId'] = $productId;
        }
        
        $sql .= " ORDER BY t.date DESC LIMIT 1000"; // Limit for performance
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $response->withJson([
            'status' => 'success',
            'data' => $results
        ]);
        
    } catch (Exception $e) {
        return $response->withJson([
            'status' => 'error',
            'message' => $e->getMessage()
        ], 500);
    }
});

/**
 * Get scale usage statistics
 */
$app->GET('/api/reports/scale-usage', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    
    $dateStart = isset($queryParams['dateStart']) ? intval($queryParams['dateStart']) : null;
    $dateStop = isset($queryParams['dateStop']) ? intval($queryParams['dateStop']) : null;
    
    if ($dateStart === null || $dateStop === null) {
        return $response->withJson([
            'status' => 'error',
            'message' => 'dateStart and dateStop parameters required'
        ], 400);
    }
    
    try {
        // Try to get scale usage from the new tables
        $sql = "
            SELECT 
                sd.name,
                sd.mac_address,
                COUNT(sul.id) as usage_count,
                SUM(sul.weight_kg) as total_weight_kg,
                AVG(sul.weight_kg) as avg_weight_kg
            FROM scale_usage_log sul
            JOIN scale_devices sd ON sul.scale_id = sd.id
            WHERE sul.timestamp >= TO_TIMESTAMP(:dateStart)
            AND sul.timestamp <= TO_TIMESTAMP(:dateStop)
            GROUP BY sd.id, sd.name, sd.mac_address
            ORDER BY usage_count DESC
        ";
        
        $params = [
            'dateStart' => $dateStart,
            'dateStop' => $dateStop
        ];
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $response->withJson([
            'status' => 'success',
            'data' => $results
        ]);
        
    } catch (Exception $e) {
        // Fallback to estimate from ticket data if scale tables don't exist yet
        $fallbackSql = "
            SELECT 
                'Unknown Scale' as name,
                COUNT(tl.id) as usage_count,
                SUM(tl.quantity) as total_weight_kg,
                AVG(tl.quantity) as avg_weight_kg
            FROM ticketlines tl
            JOIN products p ON tl.product_id = p.id
            JOIN tickets t ON tl.ticket_id = t.id
            WHERE p.scaled = true 
            AND t.date >= TO_TIMESTAMP(:dateStart)
            AND t.date <= TO_TIMESTAMP(:dateStop)
        ";
        
        $stmt = $this->db->prepare($fallbackSql);
        $stmt->execute([
            'dateStart' => $dateStart,
            'dateStop' => $dateStop
        ]);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $response->withJson([
            'status' => 'success',
            'data' => $results,
            'fallback' => true
        ]);
    }
});
```

### 2.2 Register the Routes
Add the new routes to the main application configuration:

**File: `/OrtusPOSServer/src/http/routes.php`**
```php
// Add this line to include the weight report routes
require __DIR__ . '/routes/weightreport.php';
```

## 3. BackOffice Implementation

### 3.1 Weight Reports Screen
Create a new backoffice screen for weight-based reporting:

**File: `/OrtusPOSBackOffice/src/screens/weightreports.js`**

```javascript
var _weightreports_data = {};

function weightreports_show() {
    let start = new Date(new Date().getTime() - 604800000); // Now minus 7 days
    let stop = new Date(new Date().getTime() + 86400000); // Now + 1 day
    
    vue.screen.data = {
        "start": start,
        "stop": stop,
        "table": new Table().reference("weightreports-list")
    }
    vue.screen.component = "vue-weightreports";
}

function weightreports_filter() {
    let start = vue.screen.data.start;
    let stop = vue.screen.data.stop;
    
    _weightreports_data = {
        "start": start.getTime() / 1000,
        "stop": stop.getTime() / 1000
    };
    
    // Load weight sales summary
    srvcall_get("api/reports/weight-sales-summary?dateStart=" + _weightreports_data.start + "&dateStop=" + _weightreports_data.stop, _weightreports_summaryCallback);
    gui_showLoading();
}

function _weightreports_summaryCallback(request, status, response) {
    if (srvcall_callbackCatch(request, status, response, weightreports_filter)) {
        return;
    }
    
    try {
        let data = JSON.parse(response);
        if (data.status === "success") {
            vue.screen.data.summary = data.summary;
            vue.screen.data.products = data.data;
            
            // Create table for display
            let table = vue.screen.data.table;
            table.reset();
            table.addColumn("Product", "label");
            table.addColumn("Total Weight (kg)", "total_weight_kg", "number", { decimals: 3 });
            table.addColumn("Transactions", "transaction_count", "number");
            table.addColumn("Revenue", "total_revenue", "currency");
            table.addColumn("Avg Weight/Trans", "avg_weight_per_transaction", "number", { decimals: 3 });
            
            table.setRows(data.data);
            table.sortByColumn("total_weight_kg", true);
        }
    } catch (e) {
        gui_alert("Error processing data: " + e.message);
    }
    
    gui_hideLoading();
}

// Register the screen
screens["weightreports"] = {
    "show": weightreports_show,
    "filter": weightreports_filter
};
```

### 3.2 Vue Component for Weight Reports
Create the Vue component for the weight reports interface:

**File: `/OrtusPOSBackOffice/src/views/weightreports.js`**

```vue
<template id="vue-weightreports">
    <div>
        <h2>Weight-Based Product Sales Report</h2>
        
        <div class="filter-section">
            <div class="form-group">
                <label for="report-start">Start Date:</label>
                <input type="date" id="report-start" v-model="data.start">
            </div>
            <div class="form-group">
                <label for="report-stop">End Date:</label>
                <input type="date" id="report-stop" v-model="data.stop">
            </div>
            <button @click="filter" class="btn-primary">Generate Report</button>
        </div>
        
        <div v-if="data.summary" class="summary-section">
            <h3>Summary</h3>
            <div class="summary-grid">
                <div class="summary-item">
                    <div class="summary-value">{{ data.summary.total_weight_kg | number(3) }} kg</div>
                    <div class="summary-label">Total Weight Sold</div>
                </div>
                <div class="summary-item">
                    <div class="summary-value">{{ data.summary.total_revenue | currency }}</div>
                    <div class="summary-label">Total Revenue</div>
                </div>
                <div class="summary-item">
                    <div class="summary-value">{{ data.summary.total_transactions }}</div>
                    <div class="summary-label">Total Transactions</div>
                </div>
            </div>
        </div>
        
        <div v-if="data.products && data.products.length > 0">
            <h3>Product Breakdown</h3>
            <vue-table v-bind:table="data.table"></vue-table>
        </div>
        
        <div v-else-if="data.products">
            <p>No weight-based products sold during this period.</p>
        </div>
    </div>
</template>

<script>
Vue.component('vue-weightreports', {
    template: '#vue-weightreports',
    props: ['data'],
    methods: {
        filter: function() {
            screens["weightreports"].filter();
        }
    },
    filters: {
        number: function(value, decimals) {
            if (typeof value !== 'number') return value;
            return value.toFixed(decimals || 2);
        },
        currency: function(value) {
            if (typeof value !== 'number') return value;
            return 'KSh ' + value.toFixed(2);
        }
    }
});
</script>

<style>
.summary-section {
    background: #f5f5f5;
    padding: 20px;
    border-radius: 5px;
    margin: 20px 0;
}

.summary-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
}

.summary-item {
    text-align: center;
    padding: 15px;
    background: white;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.summary-value {
    font-size: 1.5em;
    font-weight: bold;
    color: #333;
}

.summary-label {
    color: #666;
    margin-top: 5px;
}

.filter-section {
    display: flex;
    gap: 15px;
    align-items: end;
    margin-bottom: 20px;
}

.form-group {
    display: flex;
    flex-direction: column;
}

.form-group label {
    margin-bottom: 5px;
    font-weight: bold;
}
</style>
```

### 3.3 Add Navigation Menu Item
Add the weight reports to the navigation menu:

**File: `/OrtusPOSBackOffice/src/screens/home.js`** (or wherever the menu is defined)
```javascript
// Add to the reports section
{
    "label": "Weight Reports",
    "action": "weightreports",
    "icon": "scale"
}
```

## 4. Mobile App Integration

### 4.1 Scale Usage Logging
Modify the Android app to log scale usage to the server:

**In Transaction.java, after successful ticket creation:**

```java
// Add this method to log scale usage
private void logScaleUsage(Ticket ticket) {
    // Only log if we have a connected scale and scaled products
    if (scaleManager != null && scaleManager.isConnected()) {
        String scaleAddress = Configure.getScaleAddress(this);
        if (scaleAddress != null && !scaleAddress.isEmpty()) {
            // Check if ticket has scaled products
            boolean hasScaledProducts = false;
            double totalWeight = 0.0;
            
            for (TicketLine line : ticket.getLines()) {
                if (line.getProduct().isScaled()) {
                    hasScaledProducts = true;
                    totalWeight += Math.abs(line.getQuantity());
                }
            }
            
            if (hasScaledProducts) {
                // Log to server (this would be implemented as an API call)
                logScaleUsageToServer(scaleAddress, ticket.getId(), totalWeight);
            }
        }
    }
}

private void logScaleUsageToServer(String scaleMac, String ticketId, double weightKg) {
    // Implementation would send data to:
    // POST /api/scale/log-usage
    // {
    //   "scaleMac": "AA:BB:CC:DD:EE:FF",
    //   "ticketId": "12345",
    //   "weightKg": 1.250
    // }
}
```

## 5. Testing and Validation

### 5.1 Test Data Generation
Create sample data for testing:

```sql
-- Insert sample scale devices
INSERT INTO scale_devices (mac_address, name, cashregister_id) VALUES
('AA:BB:CC:DD:EE:01', 'Produce Scale - Counter 1', 1),
('AA:BB:CC:DD:EE:02', 'Meat Scale - Counter 2', 2);

-- Insert sample scale usage logs
INSERT INTO scale_usage_log (scale_id, ticket_id, weight_kg) VALUES
(1, 1001, 1.250),
(1, 1002, 0.750),
(2, 1003, 2.500);
```

### 5.2 Validation Queries
Use these queries to validate the implementation:

```sql
-- Verify weight data is correctly stored
SELECT 
    t.id as ticket_id,
    t.number as ticket_number,
    p.label as product,
    tl.quantity as weight_kg,
    tl.finaltaxedprice as price
FROM tickets t
JOIN ticketlines tl ON t.id = tl.ticket_id
JOIN products p ON tl.product_id = p.id
WHERE p.scaled = true
ORDER BY t.date DESC
LIMIT 10;

-- Verify reports are working
SELECT 
    p.label,
    SUM(tl.quantity) as total_weight,
    COUNT(*) as transactions
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
WHERE p.scaled = true
GROUP BY p.id, p.label
ORDER BY total_weight DESC;
```

## 6. Performance Considerations

### 6.1 Database Indexes
Ensure proper indexing for performance:

```sql
-- Add indexes for common queries
CREATE INDEX IF NOT EXISTS idx_products_scaled ON products(scaled);
CREATE INDEX IF NOT EXISTS idx_ticketlines_product ON ticketlines(product_id);
CREATE INDEX IF NOT EXISTS idx_ticketlines_ticket ON ticketlines(ticket_id);
CREATE INDEX IF NOT EXISTS idx_tickets_date ON tickets(date);
```

### 6.2 Query Optimization
For large datasets, consider using materialized views:

```sql
-- Daily summary view for performance
CREATE MATERIALIZED VIEW weight_sales_daily AS
SELECT 
    DATE(t.date) as sale_date,
    p.id as product_id,
    p.label as product_label,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(*) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
GROUP BY DATE(t.date), p.id, p.label;

-- Refresh the materialized view periodically
-- REFRESH MATERIALIZED VIEW weight_sales_daily;
```

## 7. Security Considerations

### 7.1 API Security
Ensure API endpoints are properly secured:

```php
// Add authentication check to weight report endpoints
$app->GET('/api/reports/weight-sales-summary', function ($request, $response, $args) {
    // Check user authentication and permissions
    if (!isUserAuthenticated() || !hasPermission('view_reports')) {
        return $response->withStatus(403)->withJson([
            'status' => 'error',
            'message' => 'Access denied'
        ]);
    }
    
    // ... rest of the implementation
});
```

This implementation provides a comprehensive solution for tracking, reporting, and analyzing weight-based product sales in the OrtusPOS system.