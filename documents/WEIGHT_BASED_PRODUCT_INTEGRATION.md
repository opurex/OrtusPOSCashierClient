# Weight-Based Product Integration and Reporting System

## Current System Overview

### Database Structure
The OrtusPOS system already has the necessary database structure to support weight-based products:

1. **Products Table** (`products`):
   - `scaled` (boolean): Flag indicating if product is sold by weight
   - `scaletype` (smallint): Type of scale (0 = weight, 1 = volume, etc.)
   - `scalevalue` (double precision): Scale capacity/unit value

2. **Ticket Lines Table** (`ticketlines`):
   - `quantity` (double precision): For scaled products, contains actual weight in kg
   - `product_id` (integer): Links to products table
   - All standard sales data fields (price, tax, etc.)

### BackOffice Integration
The backoffice already supports:
- Product creation with "Sold by Weight" flag
- Scale type and capacity configuration
- Basic sales reporting (by product, category, etc.)

### Android App Integration
The mobile app already supports:
- Bluetooth scale integration with Aclas scales
- Weight-based product identification
- Real-time weight capture and price calculation
- Receipt printing with weight information

## Proposed Enhancements

### 1. Enhanced Sales Reporting for Weight-Based Products

#### New Report: "Weight-Based Product Sales Analysis"
This report will provide detailed analytics for all weight-based products:

**Key Metrics:**
- Total weight sold per product (kg)
- Average weight per transaction
- Revenue generated from weight-based products
- Comparison with non-weight-based products
- Peak sales periods for weight-based items

**Implementation Steps:**
1. Create new API endpoint `/api/reports/weight-sales`
2. Add new backoffice screen `weightreports.js`
3. Enhance existing sales reports to distinguish weight-based products

#### Database Query Examples:
```sql
-- Total weight sold for each scaled product in a date range
SELECT 
    p.label,
    SUM(tl.quantity) as total_weight_kg,
    COUNT(tl.ticket_id) as transaction_count,
    SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true 
AND t.date BETWEEN '2025-09-01' AND '2025-09-30'
GROUP BY p.id, p.label
ORDER BY total_weight_kg DESC;

-- Average weight per transaction for scaled products
SELECT 
    p.label,
    AVG(tl.quantity) as avg_weight_kg,
    MIN(tl.quantity) as min_weight_kg,
    MAX(tl.quantity) as max_weight_kg
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
WHERE p.scaled = true
GROUP BY p.id, p.label;
```

### 2. Daily Weight Activity Report

#### Report Features:
- Daily weight sold by product
- Hourly sales patterns for weight-based items
- Comparison with previous periods
- Staff performance metrics (if applicable)

#### Implementation:
1. Add scheduled task to generate daily summaries
2. Create summary table for performance optimization
3. Add backoffice dashboard widget

### 3. Inventory Integration for Weight-Based Products

#### Current Limitation:
The existing stock management system treats all products as discrete units.

#### Enhancement Proposal:
- Track inventory in weight units (kg) for scaled products
- Implement weight-based reorder points
- Generate purchasing recommendations based on weight consumption

### 4. Aclas Scale Integration with Server

#### Current State:
- Mobile app communicates directly with Aclas scale via Bluetooth
- Sales data is synchronized to server
- Weight information is stored in ticketlines.quantity

#### Enhancement Opportunities:
1. **Scale Registration**:
   - Register scale devices with unique identifiers
   - Track which scales are used at which cash registers
   - Monitor scale performance and usage statistics

2. **Scale Analytics**:
   - Track number of weighings per day
   - Monitor average transaction values for weight-based sales
   - Identify peak usage times for scales

3. **Scale Maintenance Alerts**:
   - Track scale usage hours
   - Generate maintenance reminders
   - Monitor for calibration needs

## Implementation Plan

### Phase 1: Enhanced Reporting (2-3 weeks)
1. Create new API endpoints for weight-based analytics
2. Develop backoffice screens for weight reports
3. Integrate with existing reporting framework
4. Test with sample data

### Phase 2: Inventory Integration (3-4 weeks)
1. Modify stock management to handle weight-based products
2. Create new inventory reports
3. Implement reorder logic for weight-based items
4. Test with actual inventory scenarios

### Phase 3: Scale Management (2-3 weeks)
1. Add scale registration and tracking
2. Create scale performance dashboards
3. Implement maintenance alerts
4. Integrate with existing device management

## Technical Implementation Details

### API Endpoints to Create

1. **GET /api/reports/weight-sales-summary**
   - Parameters: dateStart, dateStop, cashRegister (optional)
   - Returns: Summary of weight-based product sales

2. **GET /api/reports/weight-sales-by-product**
   - Parameters: dateStart, dateStop, productId (optional)
   - Returns: Detailed breakdown by product

3. **GET /api/reports/scale-usage**
   - Parameters: dateStart, dateStop, scaleId (optional)
   - Returns: Scale usage statistics

### Database Modifications

1. **New Table: scale_devices**
   ```sql
   CREATE TABLE scale_devices (
       id SERIAL PRIMARY KEY,
       mac_address VARCHAR(17) UNIQUE NOT NULL,
       name VARCHAR(255),
       cashregister_id INTEGER REFERENCES cashregisters(id),
       registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       last_used TIMESTAMP
   );
   ```

2. **New Table: scale_usage_log**
   ```sql
   CREATE TABLE scale_usage_log (
       id SERIAL PRIMARY KEY,
       scale_id INTEGER REFERENCES scale_devices(id),
       ticket_id INTEGER REFERENCES tickets(id),
       weight_kg DECIMAL(10,3),
       timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

### BackOffice Screens to Create

1. **Weight Reports Dashboard** (`weightreports.js`)
   - Overview of weight-based product performance
   - Charts showing trends over time
   - Comparison with non-weight-based products

2. **Scale Management** (`scaledashboard.js`)
   - List of registered scales
   - Usage statistics and performance metrics
   - Maintenance alerts and history

## Benefits of Implementation

### Business Intelligence
- Better understanding of weight-based product performance
- Data-driven decisions for product mix and pricing
- Identification of high-value customer segments

### Operational Efficiency
- Automated inventory management for weight-based products
- Proactive scale maintenance
- Performance optimization for staff

### Customer Experience
- More accurate inventory availability
- Faster checkout with properly maintained scales
- Better product selection based on sales data

## Implementation Considerations

### Data Privacy
- Ensure customer transaction data is protected
- Comply with local data protection regulations
- Implement appropriate access controls

### Performance
- Optimize database queries for large datasets
- Implement caching for frequently accessed reports
- Consider data archiving for historical reports

### Scalability
- Design for multiple cash registers and scales
- Support for different scale manufacturers
- Flexible reporting periods and filters

This comprehensive approach will transform the weight-based product handling from a simple feature to a robust analytics and management system that provides valuable business insights.