# OrtusPOS Web Client: Scale Functionality

This document explains how the "scale" functionality is handled on the client side within the `OrtusWebClient` and how it interacts with the API.

It's important to note that the client does not directly interact with a physical weighing scale. Instead, the "scale" functionality refers to a system for defining how products are measured and priced. The client provides a user interface for setting these product scaling properties, which are then sent to the server API.

This functionality is primarily managed in two main areas:

## 1. Product Edit Form

When you create or edit a product, the `OrtusWebClient` displays specific form fields to manage how the product is priced based on its "scale" properties. This is defined in `src/views/products.js` within the `vue-product-form` component.

The key fields involved are:

-   **`scaled` (Bulk sale):** This is a checkbox.
    -   If checked (`true`), it indicates to the Point of Sale (POS) system that the quantity for this item should be entered manually at the time of sale (e.g., weighing loose produce like bananas). The `scaleValue` field becomes meaningless in this context.
    -   If unchecked (`false`), the product is sold as a complete package for a fixed price, even if it has a defined weight or volume.

-   **`scaleType` (Weight/Volume):** This is a dropdown menu that allows the user to select the unit of measure for the product. The available options are:
    -   "piece" (`SCALE_TYPE_NONE`)
    -   "Kilogram" (`SCALE_TYPE_WEIGHT`)
    -   "Liter" (`SCALE_TYPE_VOLUME`)
    -   "Hour" (`SCALE_TYPE_TIME`)

-   **`scaleValue` (Content):** This is a number input field.
    -   It is primarily used when `scaled` is `false` (i.e., for pre-packaged items). For example, a 200g bag of coffee would have a `scaleValue` of `0.2` (if the base unit is kilograms).
    -   This field is typically hidden or disabled when `scaled` is `true`.

**Real-time Reference Price Display:**
A crucial feature of the product form is its ability to calculate and display a "Reference Price" in real-time. As you adjust the product's selling price, content, or unit, the client immediately calculates and shows the price per unit (e.g., "€12.50 per kilogram"). This provides instant feedback to the user, helping them set accurate pricing.

## 2. CSV File Import

The client also understands and processes these product scaling properties when importing products from a CSV file. This logic is implemented in `src/csvparser.js`.

The `CsvParser` includes a `convertScaleType` function that intelligently converts human-readable text from your CSV file into the system's internal numeric `scaleType` codes. For example, it recognizes and converts:
-   `kilogramme`, `kg`, `poid`, `poids` into the `SCALE_TYPE_WEIGHT` code (1).
-   `litre`, `l` into the `SCALE_TYPE_VOLUME` code (2).
-   `heure`, `h`, `horaire` into the `SCALE_TYPE_TIME` code (3).
-   `piece`, `p`, `u`, `unité` into the `SCALE_TYPE_NONE` code (0).

## How it Connects to the API

The interaction with the backend API for "scale" functionality is integrated into the standard product management API:

1.  **Data Capture:** The user sets the `scaled`, `scaleType`, and `scaleValue` properties for a product using either the product form in the web client or by providing them in a CSV file for import.
2.  **API Request:** When the user saves the product (or initiates a CSV import), the `OrtusWebClient` bundles these three fields along with all other product-related data (such as name, selling price, barcode, etc.).
3.  **Standard Product Endpoint:** This complete product data payload is then sent via a standard HTTP request (e.g., `POST` or `PUT`) to the server's product API endpoint (e.g., `/api/product`).
4.  **Server-Side Processing:** The server receives this data and processes it, storing the `scaled`, `scaleType`, and `scaleValue` in the product's record in the database, as previously discussed.

In summary, the `OrtusWebClient` provides a user-friendly interface to define and manage these product pricing attributes. The actual calculation of a line item's price based on a manually entered weight (for `scaled: true` products) would typically occur within the separate POS terminal application, not within this back-office web client.