# OrtusPOS Admin Reports Dashboard

## Overview
This document describes the comprehensive admin reporting interface for OrtusPOS, including real-time reporting capabilities for weighted products, filtering options, and advanced analytics.

## Admin Dashboard Access

### Web Interface
```
URL: http://your-server-address/admin/
Username: admin
Password: [configured admin password]
```

### Mobile Interface
```
Android App: OrtusPOS Admin
iOS App: OrtusPOS Admin (coming soon)
```

## Main Dashboard Features

### 1. Real-Time Sales Monitoring
- Live sales feed showing recent transactions
- Weighted product sales highlighted
- Revenue tracking by product category
- Top selling items (including weighted products)

### 2. Inventory Status
- Current stock levels
- Low stock alerts (with weight-based products)
- Overstock warnings
- Incoming shipments

### 3. Financial Summary
- Daily/weekly/monthly revenue
- Payment method breakdown
- M-Pesa transaction status
- Cash flow monitoring

## Weighted Product Reporting

### 1. Weight-Based Sales Reports

#### Real-Time Weighted Product Sales
- **Endpoint**: `GET /api/reports/weighted-products/sales`
- **Filters**: Date range, cash register, product category
- **Data Points**: 
  - Total weight sold (kg)
  - Revenue generated
  - Average weight per transaction
  - Peak selling hours

#### Weighted Product Performance
- **Endpoint**: `GET /api/reports/weighted-products/performance`
- **Filters**: Date range, individual products
- **Data Points**:
  - Product name
  - Total weight sold
  - Revenue generated
  - Transaction count
  - Average weight per transaction

#### Example API Response:
```json
{
  "status": "success",
  "data": [
    {
      "product_id": 1001,
      "product_name": "Oranges (Fresh)",
      "total_weight_kg": 25.750,
      "total_revenue": 6437.50,
      "transaction_count": 18,
      "average_weight_kg": 1.431,
      "peak_hours": "10:00-12:00, 16:00-18:00"
    },
    {
      "product_id": 1002,
      "product_name": "Apples (Fresh)",
      "total_weight_kg": 18.300,
      "total_revenue": 6405.00,
      "transaction_count": 15,
      "average_weight_kg": 1.220,
      "peak_hours": "09:00-11:00, 17:00-19:00"
    }
  ],
  "summary": {
    "total_weight_sold_kg": 44.050,
    "total_revenue": 12842.50,
    "total_transactions": 33
  }
}
```

### 2. Scale Integration Reports

#### Scale Usage Statistics
- **Endpoint**: `GET /api/reports/scales/usage`
- **Filters**: Date range, individual scales
- **Data Points**:
  - Scale name/ID
  - Number of weighings
  - Total weight processed
  - Average weight per transaction
  - Uptime statistics

#### Scale Performance Monitoring
- **Endpoint**: `GET /api/reports/scales/performance`
- **Filters**: Date range, scale model
- **Data Points**:
  - Connection reliability
  - Data transmission success rate
  - Error rates
  - Maintenance alerts

### 3. Customer Behavior Analysis

#### Weighted Product Customer Preferences
- **Endpoint**: `GET /api/reports/customers/weighted-preferences`
- **Filters**: Date range, customer segments
- **Data Points**:
  - Customer purchase frequency
  - Preferred weight ranges
  - Seasonal buying patterns
  - Loyalty program participation

## Filtering Capabilities

### 1. Date Range Filtering
```
Parameters:
- startDate: Unix timestamp or ISO date string
- endDate: Unix timestamp or ISO date string
- preset: today|yesterday|this_week|last_week|this_month|last_month|custom
```

### 2. Product Filtering
```
Parameters:
- productId: Specific product ID
- categoryId: Product category ID
- productType: regular|weighted|prepaid
- brand: Product brand (if applicable)
```

### 3. Location/Store Filtering
```
Parameters:
- cashRegisterId: Specific cash register ID
- storeId: Specific store/location ID
- region: Geographic region
```

### 4. Customer Filtering
```
Parameters:
- customerId: Specific customer ID
- customerGroupId: Customer group/segment
- loyaltyTier: Loyalty program tier
```

### 5. Payment Method Filtering
```
Parameters:
- paymentMethod: cash|card|mpesa|credit
- paymentProvider: specific payment provider (for card/M-Pesa)
```

## Advanced Reporting Features

### 1. Custom Report Builder
- Drag-and-drop report designer
- Custom metric selection
- Advanced filtering options
- Scheduled report generation
- Export to PDF/Excel/CSV

### 2. Dashboard Widgets
- Real-time sales meters
- Inventory level gauges
- Customer satisfaction scores
- Employee performance charts

### 3. Alert Systems
- Low stock notifications
- Scale connectivity issues
- Large transaction alerts
- Suspicious activity detection

## Report Export Options

### 1. Export Formats
- **PDF**: Professional printable reports
- **Excel**: Spreadsheet format with formulas
- **CSV**: Comma-separated values for data analysis
- **JSON**: Raw data for integration
- **XML**: Structured data export

### 2. Export Scheduling
- One-time exports
- Daily/weekly/monthly scheduled exports
- Email delivery of exported reports
- FTP/SFTP upload of reports

## API Endpoints for Admin Reports

### 1. Weighted Product Reports

#### Get Weighted Product Sales Summary
```
GET /api/admin/reports/weighted-sales-summary
Parameters:
- startDate (required): Unix timestamp
- endDate (required): Unix timestamp
- cashRegisterId (optional): Filter by cash register
- categoryId (optional): Filter by product category

Response:
{
  "status": "success",
  "data": {
    "summary": {
      "total_weight_kg": 45.750,
      "total_revenue": 11437.50,
      "total_transactions": 32,
      "average_weight_per_transaction": 1.429
    },
    "by_product": [
      {
        "product_id": 1001,
        "product_name": "Oranges (Fresh)",
        "weight_kg": 25.750,
        "revenue": 6437.50,
        "transactions": 18
      }
    ]
  }
}
```

#### Get Scale Performance Report
```
GET /api/admin/reports/scale-performance
Parameters:
- startDate (required): Unix timestamp
- endDate (required): Unix timestamp
- scaleId (optional): Filter by specific scale

Response:
{
  "status": "success",
  "data": {
    "scales": [
      {
        "scale_id": "AA:BB:CC:DD:EE:FF",
        "scale_name": "Aclas-FSC-2000 - Counter 1",
        "transactions": 142,
        "total_weight_processed": 187.500,
        "average_weight": 1.320,
        "uptime_percentage": 99.2,
        "error_count": 1
      }
    ]
  }
}
```

#### Get Weighted Product Transaction Details
```
GET /api/admin/reports/weighted-transactions
Parameters:
- startDate (required): Unix timestamp
- endDate (required): Unix timestamp
- productId (optional): Filter by specific product
- cashRegisterId (optional): Filter by cash register

Response:
{
  "status": "success",
  "data": [
    {
      "transaction_id": 12345,
      "product_name": "Oranges (Fresh)",
      "weight_kg": 1.750,
      "unit_price": 250.00,
      "total_price": 437.50,
      "timestamp": "2025-09-15T10:30:00Z",
      "cash_register": "Counter 1",
      "staff_member": "John Doe"
    }
  ]
}
```

### 2. Inventory Reports

#### Get Weight-Based Inventory Status
```
GET /api/admin/reports/weighted-inventory
Parameters:
- lowStockThreshold (optional): Custom low stock threshold
- overStockThreshold (optional): Custom over stock threshold

Response:
{
  "status": "success",
  "data": {
    "low_stock": [
      {
        "product_id": 1001,
        "product_name": "Oranges (Fresh)",
        "current_weight_kg": 2.500,
        "security_level_kg": 5.000,
        "days_until_reorder": 2
      }
    ],
    "over_stock": [
      {
        "product_id": 1002,
        "product_name": "Apples (Fresh)",
        "current_weight_kg": 50.000,
        "max_level_kg": 30.000,
        "excess_weight_kg": 20.000
      }
    ]
  }
}
```

## Filtering Implementation Examples

### 1. Frontend JavaScript Filtering
```javascript
// Example of how to implement date range filtering
function getWeightedProductReport(startDate, endDate, filters = {}) {
    const params = new URLSearchParams({
        startDate: Math.floor(startDate.getTime() / 1000),
        endDate: Math.floor(endDate.getTime() / 1000),
        ...filters
    });
    
    return fetch(`/api/admin/reports/weighted-sales-summary?${params}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                return data.data;
            } else {
                throw new Error(data.message || 'Failed to fetch report');
            }
        });
}

// Example of how to implement product filtering
function filterByProduct(productId) {
    const filters = {};
    if (productId) {
        filters.productId = productId;
    }
    
    return getWeightedProductReport(
        new Date(new Date().getTime() - 604800000), // Last 7 days
        new Date(),
        filters
    );
}
```

### 2. Backend PHP Filtering Implementation
```php
// Example of how to implement filtering in the backend
function getWeightedSalesData($startDate, $endDate, $filters = []) {
    $sql = "
        SELECT 
            p.id,
            p.label as product_name,
            SUM(tl.quantity) as total_weight_kg,
            COUNT(*) as transaction_count,
            SUM(tl.finaltaxedprice) as total_revenue
        FROM ticketlines tl
        JOIN products p ON tl.product_id = p.id
        JOIN tickets t ON tl.ticket_id = t.id
        WHERE p.scaled = true
        AND t.date >= TO_TIMESTAMP(:startDate)
        AND t.date <= TO_TIMESTAMP(:endDate)
    ";
    
    $params = [
        'startDate' => $startDate,
        'endDate' => $endDate
    ];
    
    // Apply optional filters
    if (!empty($filters['cashRegisterId'])) {
        $sql .= " AND t.cashregister_id = :cashRegisterId";
        $params['cashRegisterId'] = $filters['cashRegisterId'];
    }
    
    if (!empty($filters['productId'])) {
        $sql .= " AND p.id = :productId";
        $params['productId'] = $filters['productId'];
    }
    
    if (!empty($filters['categoryId'])) {
        $sql .= " AND p.category_id = :categoryId";
        $params['categoryId'] = $filters['categoryId'];
    }
    
    $sql .= " GROUP BY p.id, p.label ORDER BY total_weight_kg DESC";
    
    $stmt = $this->db->prepare($sql);
    $stmt->execute($params);
    return $stmt->fetchAll(PDO::FETCH_ASSOC);
}
```

## Report Visualization

### 1. Charts and Graphs
- **Line Charts**: Sales trends over time
- **Bar Charts**: Product performance comparison
- **Pie Charts**: Revenue distribution by category
- **Heat Maps**: Peak selling hours
- **Gauges**: Inventory levels

### 2. Interactive Dashboards
- Real-time data updates
- Click-through drill-down capabilities
- Customizable widget layouts
- Responsive design for all devices

### 3. Export Templates
- Professional PDF layouts
- Excel templates with formatting
- CSV exports with proper headers
- JSON exports with schema documentation

## Security and Access Control

### 1. Role-Based Access
- **Admin**: Full access to all reports
- **Manager**: Access to store-level reports
- **Supervisor**: Limited access to specific reports
- **Staff**: No access to reports

### 2. Data Protection
- Encrypted data transmission
- Secure authentication tokens
- Audit trails for report access
- Compliance with data privacy regulations

## Performance Optimization

### 1. Database Indexing
```sql
-- Recommended indexes for reporting queries
CREATE INDEX idx_ticketlines_scaled_products ON ticketlines(product_id) WHERE EXISTS (
    SELECT 1 FROM products p WHERE p.id = ticketlines.product_id AND p.scaled = true
);

CREATE INDEX idx_tickets_date_scaled ON tickets(date) WHERE EXISTS (
    SELECT 1 FROM ticketlines tl 
    JOIN products p ON tl.product_id = p.id 
    WHERE tl.ticket_id = tickets.id AND p.scaled = true
);

CREATE INDEX idx_products_category_scaled ON products(category_id) WHERE scaled = true;
```

### 2. Caching Strategy
- In-memory caching for frequently accessed reports
- Database query result caching
- CDN for static report assets
- Browser caching for dashboard elements

### 3. Pagination and Limits
- Server-side pagination for large datasets
- Default limits on data returned
- Progressive loading for infinite scroll
- Efficient data retrieval algorithms

## Testing and Validation

### 1. Report Accuracy Testing
- Unit tests for report calculations
- Integration tests for data pipelines
- End-to-end tests for report generation
- Data validation against source systems

### 2. Performance Testing
- Load testing for concurrent users
- Stress testing for peak usage periods
- Response time monitoring
- Database query performance analysis

### 3. User Acceptance Testing
- Stakeholder review of report formats
- Usability testing with actual users
- Feedback collection and iteration
- Accessibility compliance verification

## Maintenance and Updates

### 1. Regular Report Maintenance
- Monthly review of report accuracy
- Quarterly update of reporting requirements
- Annual audit of data sources
- Continuous improvement based on user feedback

### 2. Technology Updates
- Database schema evolution
- API version management
- Frontend framework updates
- Security patch deployment

This comprehensive admin reporting interface provides full visibility into all aspects of the OrtusPOS system, with special emphasis on weighted product sales from Aclas Bluetooth scales, enabling data-driven decision making for business optimization.