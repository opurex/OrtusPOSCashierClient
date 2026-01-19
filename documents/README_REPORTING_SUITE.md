# OrtusPOS Admin Reports and Testing Suite

## Overview
This package provides comprehensive admin reporting capabilities for the OrtusPOS system, with special focus on weighted product sales from Aclas Bluetooth scales. It includes API endpoints, testing tools, validation guides, and integration with existing BackOffice reporting.

## Package Contents

### 1. API Documentation and Implementation
- `API_DOCUMENTATION_AND_TESTING.md` - Complete API documentation
- `/OrtusPOSServer/src/http/routes/adminreports.php` - New API endpoints for admin reports
- `/OrtusPOSServer/src/http/routes.php` - Updated routes file including admin reports

### 2. Admin Reporting Interface
- `ADMIN_REPORTING_INTERFACE.md` - Comprehensive admin dashboard design
- Weighted product sales monitoring
- Scale performance analytics
- Customer behavior analysis
- Advanced filtering capabilities

### 3. Testing and Validation
- `TESTING_AND_VALIDATION_GUIDE.md` - Complete testing procedures
- API endpoint testing scripts
- Integration testing scenarios
- Performance benchmarking
- Security validation

### 4. Tools and Utilities
- `tests/ortuspos_admin_reports.postman_collection.json` - Postman collection for API testing
- `tests/ortuspos_local_environment.postman_environment.json` - Postman environment configuration
- `/OrtusPOSServer/test_admin_reports_api.php` - PHP test script
- Sample data generation utilities

## Key Features

### 1. Weighted Product Reporting
- **Real-time Sales Monitoring**: Live feed of weighted product transactions
- **Performance Analytics**: Revenue, volume, and transaction metrics by product
- **Customer Insights**: Buying patterns and preferences for weighted items
- **Inventory Management**: Low stock alerts and overstock warnings

### 2. Scale Integration Analytics
- **Usage Statistics**: Transaction counts and weight processed per scale
- **Performance Monitoring**: Uptime, error rates, and reliability metrics
- **Maintenance Alerts**: Predictive maintenance scheduling
- **Troubleshooting Tools**: Diagnostic data for scale issues

### 3. Advanced Filtering and Export
- **Multi-dimensional Filtering**: Date ranges, products, locations, customers
- **Export Options**: PDF, Excel, CSV, JSON, XML formats
- **Scheduled Reports**: Automated delivery via email or FTP
- **Custom Report Builder**: Drag-and-drop interface for ad-hoc reports

### 4. Dashboard and Visualization
- **Interactive Widgets**: Real-time data displays
- **Charts and Graphs**: Sales trends, performance metrics, inventory levels
- **Alert Systems**: Notifications for critical events
- **Mobile Responsive**: Access from any device

## Installation and Setup

### 1. Server Requirements
- PHP 7.4 or higher
- PostgreSQL 10+ database
- Apache or Nginx web server
- Bluetooth-enabled Android devices for scale integration

### 2. Database Configuration
Ensure the following indexes exist for optimal performance:

```sql
-- Indexes for weighted product reporting
CREATE INDEX IF NOT EXISTS idx_products_scaled ON products(scaled) WHERE scaled = true;
CREATE INDEX IF NOT EXISTS idx_ticketlines_product ON ticketlines(product_id);
CREATE INDEX IF NOT EXISTS idx_ticketlines_ticket ON ticketlines(ticket_id);
CREATE INDEX IF NOT EXISTS idx_tickets_date ON tickets(date);
CREATE INDEX IF NOT EXISTS idx_tickets_cashregister ON tickets(cashregister_id);
```

### 3. API Endpoint Registration
The new admin reports endpoints are automatically registered through the routes system:

- `/api/admin/system/health` - System health check
- `/api/admin/test` - API connectivity test
- `/api/admin/reports/weighted-sales-summary` - Weighted product sales summary
- `/api/admin/reports/scale-performance` - Scale performance metrics
- `/api/admin/reports/weighted-transactions` - Detailed transaction data
- `/api/admin/reports/weighted-inventory` - Inventory status for weighted products

## Usage Examples

### 1. Accessing Weighted Product Reports
```bash
# Get weighted product sales summary for September 2025
curl "http://localhost:8080/api/admin/reports/weighted-sales-summary?startDate=1725148800&endDate=1727740800" \
  -H "Authorization: Bearer YOUR_AUTH_TOKEN"
```

### 2. Monitoring Scale Performance
```bash
# Get scale performance report
curl "http://localhost:8080/api/admin/reports/scale-performance?startDate=1725148800&endDate=1727740800" \
  -H "Authorization: Bearer YOUR_AUTH_TOKEN"
```

### 3. Testing with Postman
1. Import the Postman collection: `tests/ortuspos_admin_reports.postman_collection.json`
2. Import the environment: `tests/ortuspos_local_environment.postman_environment.json`
3. Update the `auth_token` variable with a valid admin token
4. Run collections to test all endpoints

## Testing and Validation

### 1. Automated Testing
Run the PHP test script:
```bash
cd /path/to/OrtusPOSServer
php test_admin_reports_api.php
```

### 2. Integration Testing
Follow the procedures in `TESTING_AND_VALIDATION_GUIDE.md`:
- Verify Aclas scale integration
- Validate database consistency
- Test performance under load
- Confirm security access controls

### 3. User Acceptance Testing
- Test BackOffice report displays
- Verify dashboard widget functionality
- Confirm export capabilities
- Validate mobile responsiveness

## Performance Optimization

### 1. Database Indexing
Recommended indexes for optimal query performance:
```sql
-- For weighted product queries
CREATE INDEX idx_ticketlines_scaled_products ON ticketlines(product_id) 
WHERE EXISTS (SELECT 1 FROM products p WHERE p.id = ticketlines.product_id AND p.scaled = true);

-- For date range filtering
CREATE INDEX idx_tickets_date_scaled ON tickets(date) 
WHERE EXISTS (SELECT 1 FROM ticketlines tl JOIN products p ON tl.product_id = p.id 
              WHERE tl.ticket_id = tickets.id AND p.scaled = true);

-- For category filtering
CREATE INDEX idx_products_category_scaled ON products(category_id) WHERE scaled = true;
```

### 2. Caching Strategy
- In-memory caching for frequently accessed reports
- Database query result caching
- CDN for static report assets
- Browser caching for dashboard elements

## Security Considerations

### 1. Access Control
- Role-based access to reports (Admin, Manager, Supervisor roles)
- Authentication required for all admin endpoints
- Audit trails for report access and exports
- Data encryption for sensitive information

### 2. Data Protection
- Masking of personally identifiable information (PII)
- Secure transmission of financial data
- Compliance with data privacy regulations
- Regular security scanning and updates

## Maintenance and Updates

### 1. Regular Maintenance Tasks
- **Daily**: Monitor API performance and error rates
- **Weekly**: Validate data consistency and accuracy
- **Monthly**: Review and optimize database queries
- **Quarterly**: Update documentation and user guides

### 2. Version Updates
- Backward compatibility maintained for API endpoints
- Migration scripts for database schema changes
- Deprecation notices for legacy features
- Upgrade guides for new functionality

## Support and Troubleshooting

### 1. Common Issues
Refer to `TESTING_AND_VALIDATION_GUIDE.md` for troubleshooting:
- Report generation failures
- Scale integration problems
- Performance degradation
- Data inconsistency issues

### 2. Monitoring and Alerts
- API response time monitoring
- Database query performance tracking
- Server resource utilization alerts
- Error rate anomaly detection

## Contributing

### 1. Development Guidelines
- Follow existing code style and conventions
- Write comprehensive unit and integration tests
- Document all new features and changes
- Submit pull requests with detailed descriptions

### 2. Reporting Issues
- Provide detailed reproduction steps
- Include relevant log files and error messages
- Specify environment configuration and versions
- Attach screenshots for UI-related issues

This comprehensive admin reporting suite transforms OrtusPOS into a powerful business intelligence platform, providing deep insights into weighted product sales and Aclas Bluetooth scale integration performance.