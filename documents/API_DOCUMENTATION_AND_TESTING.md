# OrtusPOS API Documentation and Testing Guide

## Overview
This document provides comprehensive information about the OrtusPOS API endpoints, testing procedures, and reporting capabilities.

## Available API Endpoints

### Authentication
- `POST /api/login` - User authentication
- `POST /api/logout` - User logout

### Product Management
- `GET /api/product/getAll` - Get all products
- `GET /api/product/getByCategory/{category}` - Get products by category
- `GET /api/product/getByCode/{code}` - Get product by barcode
- `GET /api/product/getByReference/{reference}` - Get product by reference
- `GET /api/product/{id}` - Get product by ID
- `POST /api/product` - Create new product
- `PUT /api/product/{id}` - Update product
- `DELETE /api/product/{id}` - Delete product

### Category Management
- `GET /api/category/getAll` - Get all categories
- `GET /api/category/getChildren/{reference}` - Get child categories
- `GET /api/category/{id}` - Get category by ID

### Customer Management
- `GET /api/customer/getAll` - Get all customers
- `GET /api/customer/{id}` - Get customer by ID

### Stock Management
- `GET /api/stock/getAll` - Get all stock levels
- `GET /api/stock/getByProduct/{productId}` - Get stock by product ID
- `GET /api/stock/{id}` - Get stock by ID
- `POST /api/stock` - Create/update stock level
- `PUT /api/stock/{id}` - Update stock level
- `DELETE /api/stock/{id}` - Delete stock level

### Stock Transaction Management
- `GET /api/stock/transaction/getAll` - Get all stock transactions
- `GET /api/stock/transaction/getByProduct/{productId}` - Get transactions by product
- `GET /api/stock/transaction/{id}` - Get transaction by ID
- `POST /api/stock/transaction` - Create stock transaction
- `PUT /api/stock/transaction/{id}` - Update transaction
- `DELETE /api/stock/transaction/{id}` - Delete transaction

### Stock Reports
- `GET /api/stock/report/low` - Get low stock report
- `GET /api/stock/report/over` - Get over stock report
- `GET /api/stock/report/movement` - Get stock movement report
- `GET /api/stock/transaction/report/movement` - Get transaction movement report
- `GET /api/stock/transaction/report/low` - Get low stock transaction report
- `GET /api/stock/transaction/report/over` - Get over stock transaction report

### Sales/Ticket Management
- `GET /api/ticket/{cashregister}/{number}` - Get specific ticket
- `GET /api/ticket/search` - Search tickets
- `GET /api/ticket/session/{cashregister}/{sequence}` - Get tickets by session

### M-Pesa Transaction Management
- `GET /api/mpesatransaction/getAll` - Get all M-Pesa transactions
- `GET /api/mpesatransaction/successful` - Get successful transactions
- `GET /api/mpesatransaction/failed` - Get failed transactions
- `GET /api/mpesatransaction/{id}` - Get transaction by ID
- `GET /api/mpesatransaction/phone/{phoneNumber}` - Get by phone number
- `GET /api/mpesatransaction/receipt/{receiptNumber}` - Get by receipt number
- `GET /api/mpesatransaction/date/{startDate}/{endDate}` - Get by date range
- `POST /api/mpesatransaction` - Create transaction
- `PUT /api/mpesatransaction/{id}` - Update transaction
- `DELETE /api/mpesatransaction/{id}` - Delete transaction
- `POST /api/mpesatransaction/webhook` - M-Pesa callback endpoint

### Cash Register Management
- `GET /api/cashregister/getAll` - Get all cash registers
- `GET /api/cashregister/getByReference/{reference}` - Get by reference
- `GET /api/cashregister/getByName/{name}` - Get by name
- `GET /api/cashregister/{id}` - Get by ID

### User Management
- `GET /api/user/getAll` - Get all users
- `GET /api/user/{id}` - Get user by ID
- `GET /api/user/getByName/{name}` - Get user by name

### Fiscal Management
- `GET /fiscal/` - Fiscal home page
- `GET /fiscal/z/{sequence}` - Z ticket listing
- `GET /fiscal/sequence/{sequence}/z/` - Z tickets by sequence
- `GET /fiscal/sequence/{sequence}/tickets/` - Tickets by sequence
- `GET /fiscal/archive/{number}` - Download archive
- `GET /fiscal/export` - Export fiscal data

## Testing Procedures

### 1. API Testing Endpoints

#### Health Check
```
GET /api/health
```
Response:
```json
{
  "status": "ok",
  "timestamp": "2025-09-15T10:30:00Z",
  "version": "8.9.0"
}
```

#### Authentication Test
```
POST /api/test/login
Content-Type: application/json

{
  "username": "test_user",
  "password": "test_password"
}
```

### 2. Functional Testing

#### Product Management Tests
1. Create a new product
2. Retrieve the product
3. Update the product
4. Delete the product

#### Stock Management Tests
1. Create stock level for a product
2. Perform stock transactions (receipts, issues)
3. Check stock reports
4. Verify low/over stock alerts

#### Sales Tests
1. Create a new ticket
2. Add products to ticket
3. Process payment
4. Generate receipt
5. Verify ticket reports

#### M-Pesa Integration Tests
1. Simulate M-Pesa payment callback
2. Verify transaction recording
3. Check successful/failed transaction reports
4. Test webhook functionality

### 3. Performance Testing

#### Load Testing Endpoints
```
GET /api/test/load
GET /api/test/concurrent
GET /api/test/stress
```

## Reporting Capabilities

### 1. Stock Reports

#### Low Stock Report
```
GET /api/stock/report/low
```
Parameters:
- `threshold` (optional) - Custom low stock threshold

#### Over Stock Report
```
GET /api/stock/report/over
```
Parameters:
- `threshold` (optional) - Custom over stock threshold

#### Stock Movement Report
```
GET /api/stock/report/movement
```
Parameters:
- `startDate` - Start date (Unix timestamp)
- `endDate` - End date (Unix timestamp)
- `productId` (optional) - Filter by product

### 2. Sales Reports

#### Sales by Product
```
GET /api/ticket/search
```
Parameters:
- `dateStart` - Start date (Unix timestamp)
- `dateStop` - End date (Unix timestamp)
- `cashRegister` (optional) - Filter by cash register

#### Sales by Category
Similar to sales by product but grouped by category

#### Detailed Sales Report
Includes:
- Transaction history
- Product performance
- Customer buying patterns
- Time-based analysis

### 3. M-Pesa Reports

#### Successful Transactions Report
```
GET /api/mpesatransaction/successful
```
Parameters:
- `startDate` - Start date
- `endDate` - End date
- `phoneNumber` (optional) - Filter by phone

#### Failed Transactions Report
```
GET /api/mpesatransaction/failed
```
Parameters:
- `startDate` - Start date
- `endDate` - End date

#### Transaction Summary
```
GET /api/mpesatransaction/total
```
Parameters:
- `startDate` (optional)
- `endDate` (optional)

## Admin Interface Access

### BackOffice Reports
Access through the web interface at:
```
http://your-server-address/backoffice/
```

Available reports:
1. **Sales Reports**
   - Sales by Product
   - Sales by Category
   - Detailed Sales Analysis

2. **Stock Reports**
   - Current Stock Levels
   - Low Stock Alerts
   - Over Stock Alerts
   - Stock Movement History

3. **Financial Reports**
   - Daily Z-Tickets
   - Transaction Summaries
   - Revenue Analysis

4. **User Reports**
   - Staff Performance
   - Login History
   - Activity Logs

### Report Filtering Options

All reports support the following filters:
- **Date Range**: Custom date selection
- **Cash Register**: Filter by specific register
- **Product Category**: Filter by product type
- **Customer Segment**: Filter by customer groups
- **Payment Method**: Filter by payment type

## Testing with Postman/Newman

### Sample Postman Collection
A Postman collection is available at:
```
/tests/ortuspos_api_tests.postman_collection.json
```

### Running Tests
```bash
# Install Newman (Postman CLI)
npm install -g newman

# Run API tests
newman run tests/ortuspos_api_tests.postman_collection.json \
  -e tests/local_environment.postman_environment.json
```

## Data Validation and Integrity

### Validation Endpoints
```
GET /api/test/data-integrity
GET /api/test/consistency-check
GET /api/test/duplicate-detection
```

### Data Quality Reports
- Missing product references
- Inconsistent pricing data
- Duplicate customer records
- Invalid transaction dates

## Performance Monitoring

### Monitoring Endpoints
```
GET /api/monitoring/performance
GET /api/monitoring/database
GET /api/monitoring/memory
GET /api/monitoring/disk
```

Metrics tracked:
- API response times
- Database query performance
- Memory usage
- Disk space utilization
- Concurrent user count

## Security Testing

### Security Endpoints
```
GET /api/security/scan
GET /api/security/vulnerabilities
GET /api/security/audit-log
```

### Penetration Testing
Regular security scans should be performed using:
- OWASP ZAP
- Burp Suite
- Nessus Scanner

## Continuous Integration Testing

### CI/CD Pipeline
Jenkins pipeline configuration:
```
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'composer install'
            }
        }
        stage('Test') {
            steps {
                sh 'vendor/bin/phpunit tests/'
                sh 'newman run tests/api_tests.postman_collection.json'
            }
        }
        stage('Deploy') {
            steps {
                sh 'deploy.sh'
            }
        }
    }
}
```

## Troubleshooting Common Issues

### API Connection Issues
1. Check server status: `GET /api/health`
2. Verify database connectivity
3. Check authentication tokens
4. Review server logs

### Report Generation Issues
1. Check report parameters
2. Verify data availability
3. Review database indexes
4. Check memory limits

### Performance Issues
1. Monitor system resources
2. Check slow query logs
3. Review database indexing
4. Optimize report queries

## Best Practices

### API Usage
1. Always use HTTPS in production
2. Implement proper rate limiting
3. Use authentication tokens securely
4. Validate all input parameters
5. Handle errors gracefully

### Report Generation
1. Use appropriate date ranges
2. Filter data when possible
3. Cache frequently accessed reports
4. Monitor report generation performance
5. Implement pagination for large datasets

### Data Management
1. Regular database backups
2. Data archiving for old records
3. Consistency checks
4. Duplicate detection and removal
5. Performance tuning

This comprehensive API documentation and testing guide ensures that your OrtusPOS system can be properly tested, monitored, and maintained with full reporting capabilities for all aspects of the business, including the new Aclas Bluetooth scale integration for weighted products.