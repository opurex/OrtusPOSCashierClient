# Complete Integration Guide: Aclas Scale with OrtusPOS Server and Backoffice

## Overview
This guide explains how to fully integrate Aclas Bluetooth scales with the OrtusPOS server and backoffice systems, enabling comprehensive tracking, reporting, and analytics for weight-based product sales.

## 1. Current Integration Status

### What's Already Working
1. **Mobile App Integration**:
   - Bluetooth connection to Aclas scales
   - Real-time weight capture
   - Automatic price calculation
   - Receipt printing with weight information

2. **Database Structure**:
   - Products table with `scaled` flag
   - Ticket lines store weight in `quantity` field
   - Basic sales data capture

### What Needs Enhancement
1. **Server-Side Analytics**:
   - Dedicated reporting for weight-based products
   - Scale usage tracking and performance metrics
   - Inventory management for weight-based items

2. **Backoffice Integration**:
   - Comprehensive weight-based sales reports
   - Scale management dashboard
   - Business intelligence dashboards

## 2. Aclas Scale Integration with Server

### 2.1 Scale Registration System
Create a system to register and track Aclas scales:

#### Database Schema
```sql
-- Scale devices table
CREATE TABLE scale_devices (
    id SERIAL PRIMARY KEY,
    mac_address VARCHAR(17) UNIQUE NOT NULL,
    name VARCHAR(255),
    cashregister_id INTEGER REFERENCES cashregisters(id),
    registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_used TIMESTAMP,
    model VARCHAR(100),
    firmware_version VARCHAR(50)
);

-- Scale usage log table
CREATE TABLE scale_usage_log (
    id SERIAL PRIMARY KEY,
    scale_id INTEGER REFERENCES scale_devices(id),
    ticket_id INTEGER REFERENCES tickets(id),
    weight_kg DECIMAL(10,3),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    product_id INTEGER REFERENCES products(id)
);
```

#### Mobile App Integration
Modify the Android app to register scales with the server:

```java
// In ScaleManager.java, add registration method
public void registerScaleWithServer() {
    String scaleAddress = Configure.getScaleAddress(context);
    if (scaleAddress != null && !scaleAddress.isEmpty()) {
        // Send registration data to server
        // POST /api/scale/register
        // {
        //   "macAddress": "AA:BB:CC:DD:EE:FF",
        //   "name": "Produce Scale Counter 1",
        //   "cashRegisterId": 1,
        //   "model": "Aclas FSC-2000"
        // }
        registerScale(scaleAddress);
    }
}

private void registerScale(String macAddress) {
    // Implementation to send registration to server
    // Update scale_devices table
}
```

### 2.2 Scale Usage Tracking
Enhance the mobile app to log scale usage:

```java
// In Transaction.java, after ticket creation
private void logScaleUsage(Ticket ticket) {
    if (scaleManager != null && scaleManager.isConnected()) {
        String scaleAddress = Configure.getScaleAddress(this);
        if (scaleAddress != null && !scaleAddress.isEmpty()) {
            // Log each scaled product transaction
            for (TicketLine line : ticket.getLines()) {
                if (line.getProduct().isScaled()) {
                    // POST /api/scale/log-usage
                    // {
                    //   "scaleMac": "AA:BB:CC:DD:EE:FF",
                    //   "ticketId": ticket.getId(),
                    //   "productId": line.getProduct().getId(),
                    //   "weightKg": Math.abs(line.getQuantity())
                    // }
                    logScaleTransaction(scaleAddress, ticket.getId(), 
                        line.getProduct().getId(), Math.abs(line.getQuantity()));
                }
            }
        }
    }
}
```

## 3. Server-Side Analytics Implementation

### 3.1 API Endpoints for Weight Analytics

#### Weight Sales Summary
```php
// GET /api/analytics/weight-sales-summary
$app->GET('/api/analytics/weight-sales-summary', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    $dateStart = $queryParams['dateStart'] ?? null;
    $dateStop = $queryParams['dateStop'] ?? null;
    $cashRegisterId = $queryParams['cashRegisterId'] ?? null;
    
    $sql = "
        SELECT 
            p.id,
            p.label,
            p.pricesell,
            SUM(tl.quantity) as total_weight_kg,
            COUNT(DISTINCT tl.ticket_id) as transaction_count,
            SUM(tl.finaltaxedprice) as total_revenue,
            AVG(tl.quantity) as avg_weight_per_transaction
        FROM ticketlines tl
        JOIN products p ON tl.product_id = p.id
        JOIN tickets t ON tl.ticket_id = t.id
        WHERE p.scaled = true
    ";
    
    $params = [];
    if ($dateStart) {
        $sql .= " AND t.date >= TO_TIMESTAMP(:dateStart)";
        $params['dateStart'] = $dateStart;
    }
    if ($dateStop) {
        $sql .= " AND t.date <= TO_TIMESTAMP(:dateStop)";
        $params['dateStop'] = $dateStop;
    }
    if ($cashRegisterId) {
        $sql .= " AND t.cashregister_id = :cashRegisterId";
        $params['cashRegisterId'] = $cashRegisterId;
    }
    
    $sql .= " GROUP BY p.id, p.label, p.pricesell ORDER BY total_weight_kg DESC";
    
    // Execute query and return results
});
```

#### Scale Performance Metrics
```php
// GET /api/analytics/scale-performance
$app->GET('/api/analytics/scale-performance', function ($request, $response, $args) {
    $queryParams = $request->getQueryParams();
    $dateStart = $queryParams['dateStart'] ?? null;
    $dateStop = $queryParams['dateStop'] ?? null;
    
    $sql = "
        SELECT 
            sd.name,
            sd.mac_address,
            sd.model,
            COUNT(sul.id) as usage_count,
            SUM(sul.weight_kg) as total_weight_processed,
            AVG(sul.weight_kg) as avg_weight_per_transaction,
            MIN(sul.timestamp) as first_used,
            MAX(sul.timestamp) as last_used
        FROM scale_usage_log sul
        JOIN scale_devices sd ON sul.scale_id = sd.id
        WHERE 1=1
    ";
    
    $params = [];
    if ($dateStart) {
        $sql .= " AND sul.timestamp >= TO_TIMESTAMP(:dateStart)";
        $params['dateStart'] = $dateStart;
    }
    if ($dateStop) {
        $sql .= " AND sul.timestamp <= TO_TIMESTAMP(:dateStop)";
        $params['dateStop'] = $dateStop;
    }
    
    $sql .= " GROUP BY sd.id, sd.name, sd.mac_address, sd.model ORDER BY usage_count DESC";
    
    // Execute query and return results
});
```

## 4. Backoffice Reporting Implementation

### 4.1 Weight-Based Sales Dashboard
Create a comprehensive dashboard for weight-based product analytics:

#### JavaScript Implementation
```javascript
// File: /OrtusPOSBackOffice/src/screens/weightsalesdashboard.js
function weightsalesdashboard_show() {
    vue.screen.data = {
        "dateRange": {
            "start": new Date(new Date().getTime() - 604800000), // 7 days ago
            "stop": new Date()
        },
        "summary": null,
        "topProducts": [],
        "scalePerformance": [],
        "dailyTrends": []
    };
    vue.screen.component = "vue-weightsalesdashboard";
}

function weightsalesdashboard_loadData() {
    const start = Math.floor(vue.screen.data.dateRange.start.getTime() / 1000);
    const stop = Math.floor(vue.screen.data.dateRange.stop.getTime() / 1000);
    
    // Load weight sales summary
    srvcall_get(`api/analytics/weight-sales-summary?dateStart=${start}&dateStop=${stop}`, 
        function(request, status, response) {
            if (status === 200) {
                const data = JSON.parse(response);
                vue.screen.data.summary = data.summary;
                vue.screen.data.topProducts = data.products.slice(0, 10);
            }
        });
    
    // Load scale performance
    srvcall_get(`api/analytics/scale-performance?dateStart=${start}&dateStop=${stop}`, 
        function(request, status, response) {
            if (status === 200) {
                const data = JSON.parse(response);
                vue.screen.data.scalePerformance = data.scales;
            }
        });
}
```

#### Vue Component
```vue
<template id="vue-weightsalesdashboard">
    <div class="weightsalesdashboard">
        <h1>Weight-Based Product Analytics</h1>
        
        <!-- Date Range Selector -->
        <div class="date-range-selector">
            <label>Period:</label>
            <input type="date" v-model="data.dateRange.start">
            <input type="date" v-model="data.dateRange.stop">
            <button @click="loadData">Update</button>
        </div>
        
        <!-- Summary Cards -->
        <div v-if="data.summary" class="summary-cards">
            <div class="card">
                <h3>Total Weight Sold</h3>
                <p class="value">{{ data.summary.total_weight_kg | formatWeight }} kg</p>
            </div>
            <div class="card">
                <h3>Total Revenue</h3>
                <p class="value">{{ data.summary.total_revenue | formatCurrency }}</p>
            </div>
            <div class="card">
                <h3>Transactions</h3>
                <p class="value">{{ data.summary.total_transactions }}</p>
            </div>
        </div>
        
        <!-- Top Products Chart -->
        <div v-if="data.topProducts.length > 0" class="chart-section">
            <h2>Top Weight-Based Products</h2>
            <div class="chart-container">
                <!-- Chart implementation using Chart.js or similar -->
                <canvas id="topProductsChart"></canvas>
            </div>
        </div>
        
        <!-- Scale Performance Table -->
        <div v-if="data.scalePerformance.length > 0" class="table-section">
            <h2>Scale Performance</h2>
            <table class="performance-table">
                <thead>
                    <tr>
                        <th>Scale Name</th>
                        <th>Model</th>
                        <th>Transactions</th>
                        <th>Total Weight (kg)</th>
                        <th>Avg Weight/Trans</th>
                        <th>Last Used</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="scale in data.scalePerformance" :key="scale.mac_address">
                        <td>{{ scale.name }}</td>
                        <td>{{ scale.model }}</td>
                        <td>{{ scale.usage_count }}</td>
                        <td>{{ scale.total_weight_processed | formatWeight }}</td>
                        <td>{{ scale.avg_weight_per_transaction | formatWeight }}</td>
                        <td>{{ scale.last_used | formatDate }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
```

## 5. Daily and Periodic Reporting

### 5.1 Automated Daily Reports
Create scheduled tasks for generating daily summaries:

```php
// File: /OrtusPOSServer/src/tasks/daily_weight_report.php
class DailyWeightReportTask {
    public function execute() {
        $yesterday = date('Y-m-d', strtotime('-1 day'));
        $start = strtotime($yesterday . ' 00:00:00');
        $stop = strtotime($yesterday . ' 23:59:59');
        
        // Generate daily summary
        $summary = $this->getWeightSalesSummary($start, $stop);
        
        // Store in summary table for performance
        $this->storeDailySummary($yesterday, $summary);
        
        // Send email reports to managers if configured
        $this->sendDailyReport($summary);
    }
    
    private function getWeightSalesSummary($start, $stop) {
        // Query implementation similar to API endpoint
    }
    
    private function storeDailySummary($date, $summary) {
        // Store in daily_weight_summary table
    }
    
    private function sendDailyReport($summary) {
        // Email implementation
    }
}
```

### 5.2 Inventory Integration for Weight-Based Products
Enhance inventory management to handle weight-based products:

```sql
-- Enhanced stock table for weight-based products
ALTER TABLE stock ADD COLUMN 
    is_weight_based BOOLEAN DEFAULT FALSE;

ALTER TABLE stock ADD COLUMN 
    current_weight_kg DECIMAL(10,3) DEFAULT 0.000;

ALTER TABLE stock ADD COLUMN 
    security_weight_kg DECIMAL(10,3); -- Minimum weight threshold

ALTER TABLE stock ADD COLUMN 
    max_weight_kg DECIMAL(10,3); -- Maximum weight capacity
```

## 6. Business Intelligence and Analytics

### 6.1 Key Performance Indicators (KPIs)
Track important metrics for weight-based product sales:

1. **Revenue Metrics**:
   - Total revenue from weight-based products
   - Revenue per kilogram sold
   - Growth rate compared to previous periods

2. **Operational Metrics**:
   - Average transaction size (weight)
   - Peak selling hours for weight-based products
   - Scale utilization rates

3. **Product Performance**:
   - Best-selling weight-based products
   - Seasonal trends
   - Price elasticity analysis

### 6.2 Advanced Analytics
Implement machine learning-based predictions:

```python
# File: /OrtusPOSServer/analytics/weight_forecasting.py
import pandas as pd
from sklearn.linear_model import LinearRegression

def forecast_weight_demand(historical_data):
    """
    Forecast future weight-based product demand
    """
    # Convert to DataFrame
    df = pd.DataFrame(historical_data)
    
    # Feature engineering
    df['date'] = pd.to_datetime(df['date'])
    df['day_of_week'] = df['date'].dt.dayofweek
    df['month'] = df['date'].dt.month
    
    # Train model
    model = LinearRegression()
    X = df[['day_of_week', 'month']]
    y = df['total_weight_kg']
    model.fit(X, y)
    
    # Make predictions
    return model.predict(X)
```

## 7. Implementation Timeline

### Phase 1: Foundation (2-3 weeks)
1. Database schema enhancements
2. Scale registration system
3. Basic API endpoints
4. Mobile app integration for scale logging

### Phase 2: Reporting (3-4 weeks)
1. Backoffice dashboard implementation
2. Comprehensive reporting features
3. Charting and visualization
4. User interface design

### Phase 3: Advanced Analytics (2-3 weeks)
1. Daily summary generation
2. Inventory integration
3. Performance optimization
4. Testing and validation

### Phase 4: Business Intelligence (2-3 weeks)
1. KPI dashboard
2. Predictive analytics
3. Email reporting
4. User training and documentation

## 8. Benefits Realization

### Immediate Benefits (1-2 months)
- Accurate tracking of weight-based product sales
- Better inventory management for perishable goods
- Improved customer experience with accurate pricing

### Medium-term Benefits (3-6 months)
- Data-driven decisions for product mix optimization
- Identification of high-value customer segments
- Operational efficiency improvements

### Long-term Benefits (6+ months)
- Predictive analytics for inventory planning
- Automated purchasing recommendations
- Advanced business intelligence insights

## 9. Maintenance and Monitoring

### 9.1 System Health Monitoring
- Regular database performance checks
- API response time monitoring
- Scale connectivity status tracking

### 9.2 Data Quality Assurance
- Regular validation of weight data integrity
- Cross-reference with physical inventory counts
- Anomaly detection for unusual patterns

### 9.3 User Support
- Comprehensive documentation
- Training materials for staff
- Help desk support for reporting issues

This comprehensive integration will transform weight-based product sales from a simple feature into a powerful business intelligence tool that provides valuable insights for decision-making.