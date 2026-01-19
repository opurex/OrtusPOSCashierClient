# OrtusPOS Admin Reports Testing and Validation Guide

## Overview
This document provides comprehensive guidance for testing and validating the newly implemented admin reports functionality, including API endpoints for weighted product sales, scale performance, and comprehensive analytics.

## Prerequisites

### 1. Environment Setup
- OrtusPOSServer running and accessible
- Database populated with test data
- Aclas Bluetooth scales properly configured
- Test products marked as "scaled" in the system
- Sample transactions with weighted products

### 2. Test Data Requirements
- At least 5 weighted products (e.g., Oranges, Apples, Bananas)
- Minimum 50 sample transactions with weighted products
- Different cash registers with varying transaction volumes
- Sample inventory data with low stock and over stock scenarios

## API Endpoint Testing

### 1. Health Check Endpoints

#### Test: API Health Check
```
GET /api/admin/system/health
```

**Expected Response:**
```json
{
  "status": "success",
  "data": {
    "timestamp": "2025-09-15T10:30:00Z",
    "database": {
      "status": "healthy",
      "connection_time_ms": 5
    },
    "memory": {
      "used_mb": 32.5,
      "peak_mb": 45.2,
      "limit_mb": "128M"
    }
  }
}
```

#### Test: API Connectivity
```
GET /api/admin/test
```

**Expected Response:**
```json
{
  "status": "success",
  "message": "API is working correctly",
  "timestamp": "2025-09-15T10:30:00Z",
  "version": "1.0.0"
}
```

### 2. Weighted Product Reports

#### Test: Weighted Sales Summary
```
GET /api/admin/reports/weighted-sales-summary?startDate=1725148800&endDate=1727740800
```

**Expected Response:**
```json
{
  "status": "success",
  "data": {
    "summary": {
      "total_weight_kg": 45.750,
      "total_revenue": 11437.50,
      "total_transactions": 32
    },
    "by_product": [
      {
        "product_id": 1001,
        "product_name": "Oranges (Fresh)",
        "total_weight_kg": 25.750,
        "transaction_count": 18,
        "total_revenue": 6437.50,
        "average_weight_per_transaction": 1.431
      }
    ]
  }
}
```

**Validation Steps:**
1. Verify date range filtering works correctly
2. Confirm transaction counts match database records
3. Validate revenue calculations
4. Check product grouping accuracy
5. Test with various filter combinations

#### Test: Scale Performance Report
```
GET /api/admin/reports/scale-performance?startDate=1725148800&endDate=1727740800
```

**Expected Response:**
```json
{
  "status": "success",
  "data": [
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
```

**Validation Steps:**
1. Verify scale identification accuracy
2. Confirm transaction counting matches actual usage
3. Validate weight processing totals
4. Check uptime percentage calculations
5. Test filtering by specific scale ID

#### Test: Weighted Transactions Detail
```
GET /api/admin/reports/weighted-transactions?startDate=1725148800&endDate=1727740800&productId=1001
```

**Expected Response:**
```json
{
  "status": "success",
  "data": [
    {
      "transaction_id": 12345,
      "ticket_number": "TKT-0012345",
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

**Validation Steps:**
1. Verify transaction detail accuracy
2. Confirm weight precision (3 decimal places)
3. Validate pricing calculations
4. Check date/time formatting consistency
5. Test pagination for large result sets

#### Test: Weighted Inventory Status
```
GET /api/admin/reports/weighted-inventory
```

**Expected Response:**
```json
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

**Validation Steps:**
1. Verify low stock detection accuracy
2. Confirm over stock identification
3. Validate threshold calculations
4. Check inventory level precision
5. Test custom threshold parameters

## Integration Testing

### 1. Aclas Scale Integration

#### Test Scenario: Real-time Weight Capture
**Objective:** Verify that weight data from Aclas scales is properly captured and stored.

**Steps:**
1. Connect Aclas Bluetooth scale to Android POS device
2. Configure scale in OrtusPOS settings
3. Create test transaction with scaled product
4. Place item on scale and capture weight
5. Complete transaction and verify data storage

**Expected Results:**
- Weight displays in real-time on POS screen
- Price calculates automatically based on product pricing
- Transaction records weight in `ticketlines.quantity` field
- Receipt prints with weight and price per kg information

#### Test Scenario: Scale Disconnect Handling
**Objective:** Verify graceful handling of scale disconnection.

**Steps:**
1. Begin transaction with scaled product
2. Disconnect Aclas scale during weighing
3. Observe system behavior
4. Complete transaction manually

**Expected Results:**
- System falls back to manual weight entry
- No transaction data loss
- Clear error messaging to staff
- Transaction completion possible without scale

### 2. Database Integration

#### Test Scenario: Data Consistency
**Objective:** Verify that weighted product transaction data is consistently stored.

**Steps:**
1. Process 10 sample transactions with weighted products
2. Query database directly for transaction records
3. Compare stored data with POS interface display
4. Validate referential integrity

**Expected Results:**
- All 10 transactions appear in database
- Weight values match exactly (1.750 kg precision)
- Pricing calculations match stored values
- Foreign key relationships intact

#### Test Scenario: Reporting Data Accuracy
**Objective:** Verify that report data accurately reflects transaction history.

**Steps:**
1. Generate sales summary report for test period
2. Manually count transactions for verification
3. Compare revenue totals with individual transaction sums
4. Validate report filtering functionality

**Expected Results:**
- Transaction counts match database records
- Revenue totals equal sum of individual prices
- Filters correctly narrow result sets
- Date ranges applied accurately

## Performance Testing

### 1. API Response Times

#### Test: Endpoint Latency
**Objective:** Measure and validate API response times under normal load.

**Endpoints to Test:**
- `/api/admin/system/health`
- `/api/admin/reports/weighted-sales-summary`
- `/api/admin/reports/scale-performance`
- `/api/admin/reports/weighted-transactions`
- `/api/admin/reports/weighted-inventory`

**Expected Performance:**
- Simple endpoints: < 100ms
- Summary reports: < 500ms
- Detailed reports: < 1000ms
- Inventory reports: < 300ms

### 2. Database Query Performance

#### Test: Query Execution Times
**Objective:** Ensure database queries execute efficiently.

**Queries to Monitor:**
```sql
-- Weighted product sales summary
SELECT p.id, p.label, SUM(tl.quantity) as total_weight_kg, 
       COUNT(*) as transaction_count, SUM(tl.finaltaxedprice) as total_revenue
FROM ticketlines tl
JOIN products p ON tl.product_id = p.id
JOIN tickets t ON tl.ticket_id = t.id
WHERE p.scaled = true
AND t.date >= TO_TIMESTAMP(:startDate)
AND t.date <= TO_TIMESTAMP(:endDate)
GROUP BY p.id, p.label
ORDER BY total_weight_kg DESC;
```

**Performance Targets:**
- Simple queries: < 50ms
- Aggregation queries: < 200ms
- Complex joins: < 500ms

## Security Testing

### 1. Authentication and Authorization

#### Test: Protected Endpoint Access
**Objective:** Verify that admin reports require proper authentication.

**Steps:**
1. Attempt to access admin endpoints without authentication
2. Access endpoints with invalid authentication tokens
3. Access endpoints with insufficient user privileges
4. Verify proper error responses

**Expected Results:**
- Unauthenticated requests return 401 Unauthorized
- Invalid tokens return appropriate error codes
- Insufficient privileges return 403 Forbidden
- Valid admin credentials grant access

### 2. Data Protection

#### Test: Sensitive Data Exposure
**Objective:** Ensure that reports don't expose sensitive information.

**Steps:**
1. Generate reports with customer data
2. Verify customer PII is properly protected
3. Check that financial data is appropriately formatted
4. Validate that only authorized users can access reports

**Expected Results:**
- Customer personal information masked in reports
- Financial data formatted for business use
- Access logs recorded for audit trail
- Role-based access control enforced

## User Interface Testing

### 1. BackOffice Reports Integration

#### Test: Report Display in BackOffice
**Objective:** Verify that reports display correctly in the web interface.

**Steps:**
1. Log into BackOffice as administrator
2. Navigate to Reports section
3. Select Weighted Product Sales report
4. Apply various date ranges and filters
5. Export report in different formats

**Expected Results:**
- Reports load without errors
- Filtering works as expected
- Charts display correctly
- Export functionality works for all formats
- Mobile responsiveness maintained

### 2. Dashboard Widgets

#### Test: Real-time Dashboard Elements
**Objective:** Verify that dashboard widgets update correctly.

**Steps:**
1. Configure dashboard with weighted product widgets
2. Process live transactions with weighted products
3. Observe dashboard updates
4. Verify real-time accuracy

**Expected Results:**
- Dashboard updates within 5 seconds of transaction
- Widget data matches underlying reports
- Performance maintained with multiple widgets
- Error handling for disconnected scales

## Validation Checklist

### Pre-Deployment Validation
- [ ] All API endpoints return expected response formats
- [ ] Database queries execute within performance targets
- [ ] Report data accuracy verified against source data
- [ ] Security access controls functioning properly
- [ ] Error handling works for edge cases
- [ ] Scale integration handles connection/disconnection gracefully
- [ ] Mobile POS interface integrates smoothly with reporting
- [ ] BackOffice web interface displays reports correctly
- [ ] Export functionality works for all supported formats
- [ ] Documentation is complete and accurate

### Post-Deployment Validation
- [ ] Production environment performance meets targets
- [ ] Real-world transaction data appears in reports correctly
- [ ] Scale integration works with actual Aclas hardware
- [ ] User acceptance testing completed successfully
- [ ] Performance monitoring in place
- [ ] Error logging and alerting configured
- [ ] Backup and recovery procedures verified
- [ ] Training materials prepared for staff

## Troubleshooting Common Issues

### 1. Report Generation Failures
**Symptoms:** Reports return errors or empty data
**Solutions:**
- Check database connectivity and permissions
- Verify date range parameters are valid
- Ensure required indexes exist on database tables
- Review server logs for error details

### 2. Scale Integration Problems
**Symptoms:** Weight data not captured or stale
**Solutions:**
- Verify Bluetooth pairing between scale and device
- Check scale power and connection status
- Confirm scale address configured in system settings
- Review scale communication logs

### 3. Performance Degradation
**Symptoms:** Slow report generation or API responses
**Solutions:**
- Analyze database query execution plans
- Check server resource utilization (CPU, memory, disk)
- Review and optimize database indexes
- Implement result caching for frequently accessed reports

### 4. Data Inconsistency
**Symptoms:** Reports show different data than expected
**Solutions:**
- Verify timezone settings are consistent
- Check for data synchronization delays
- Validate transaction processing workflows
- Review data transformation logic

## Maintenance and Monitoring

### 1. Regular Health Checks
- Daily: API endpoint availability testing
- Weekly: Database query performance analysis
- Monthly: Report accuracy validation against source data
- Quarterly: Security access control review

### 2. Performance Monitoring
- Monitor API response times
- Track database query execution statistics
- Watch server resource utilization
- Log and analyze error rates

### 3. Data Quality Assurance
- Regular data consistency checks
- Validation of referential integrity
- Monitoring for data anomalies
- Backup verification procedures

This comprehensive testing and validation guide ensures that the OrtusPOS admin reporting functionality works reliably and provides accurate, timely information for managing weighted product sales and Aclas Bluetooth scale integration.