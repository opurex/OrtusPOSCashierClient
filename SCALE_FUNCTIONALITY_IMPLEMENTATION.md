# Scale Functionality Implementation in OrtusPOS Android App

## Overview
This document provides a comprehensive analysis of the scale functionality implementation in the OrtusPOS Android application, specifically focusing on how bulk products are handled during sales.

## Architecture Components

### 1. Product Model
- The `Product` class contains a `scaled` boolean property that determines if a product is sold by weight/volume
- When `scaled = true`, the product is treated as a bulk item (e.g., fruits, vegetables, meat)
- When `scaled = false`, the product is sold as a fixed unit/packaging
- Proper getter (`isScaled()`) and setter (`setScaled()`) methods are implemented
- The field is correctly serialized/deserialized from JSON

### 2. Bluetooth Scale Integration
- Integration with Aclas Bluetooth scales via `BluetoothScaleHelper` and `ScaleManager` classes
- Uses the `AclasScaler` library to connect to Bluetooth scales and receive real-time weight measurements
- Proper connection state management with listeners for connected/disconnected/error states
- Zero and tare functionality implemented for scale operations

### 3. User Interface Components
- `ProductScaleDialog` appears when a scaled product is selected
- Allows both manual input of weight and automatic weight reception from connected Bluetooth scale
- Real-time price calculation displayed as `unit_price × weight`
- Connection status indicator and scale operation buttons (zero/tare)

### 4. Transaction Workflow
- When a scaled product is selected, `askForAScaledProduct()` is called
- Opens `ProductScaleDialog` with the product and scale manager
- Dialog returns the weight to `onPsdPositiveClick()` callback
- Calls `addAScaledProductToTicket()` or `addAScaledProductReturnToTicket()` based on transaction type
- Menu options allow connecting/disconnecting from Bluetooth scales

### 5. Ticket Management
- `Ticket` class has dedicated methods: `addScaledProduct(Product p, double scale)` and `addScaledProductReturn(Product p, double scale)`
- Scaled products are added using `addLineProductScaled(p, scale)` which creates a `TicketLine(p, scale, getTariffArea())`
- The quantity field stores actual weight/volume instead of count for scaled products
- Special handling prevents splitting scaled product lines (since they represent specific weights)
- `CannotSplitScaledProductException` prevents inappropriate operations on scaled products

### 6. Price Calculation
- Formula: `unit_price × weight = total_price`
- Implemented consistently across the application
- Real-time price calculation in the ProductScaleDialog
- Proper handling in TicketLine's `CalculPrice.inQuantity()` method
- Receipt formatting shows weight, price per unit, and total price for scaled products

## Implementation Verification

### ✅ Correctness Checks Performed:
1. **Product Model**: Proper implementation of scaled property with correct getters/setters
2. **Ticket System**: Correct handling of scaled products in both Ticket and TicketLine classes
3. **Workflow**: Proper identification of scaled products and triggering of scale dialog
4. **UI Components**: ProductScaleDialog handles both manual and automatic weight input
5. **Bluetooth Integration**: AclasScaler library integration with proper connection management
6. **Price Calculation**: Accurate multiplication of unit price by weight throughout the system

### ✅ Key Features Verified:
- Data flow: Product → Transaction → ProductScaleDialog → Ticket → Receipt
- Real-time weight updates from connected Bluetooth scales
- Accurate price calculation based on weight
- Clear UI indicators and real-time feedback
- Proper exception handling for edge cases
- Support for returning scaled products with negative quantities

## Usage Flow for Scaled Products

1. **Product Selection**: User selects a product marked with `scaled = true`
2. **Scale Dialog**: ProductScaleDialog opens showing weight input and real-time price calculation
3. **Weight Input**: Either manual entry or automatic from connected Bluetooth scale
4. **Price Calculation**: Real-time calculation as `unit_price × weight`
5. **Add to Ticket**: Weight is stored as quantity in the ticket line
6. **Receipt Generation**: Shows weight, unit price, and total price for scaled products

## Conclusion

The scale functionality implementation in the OrtusPOS Android app is **correct and well-designed**. The system properly distinguishes between scaled (bulk) and non-scaled (fixed unit) products, handles both manual and automatic weight input, and accurately calculates prices based on weight. The implementation follows good software engineering practices with proper separation of concerns and clean architecture.

The system supports the complete workflow from product definition to sale completion, including Bluetooth scale integration, real-time price calculation, proper ticket management, and accurate receipt generation for scaled products.