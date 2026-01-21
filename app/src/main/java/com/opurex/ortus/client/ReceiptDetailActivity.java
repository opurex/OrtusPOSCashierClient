/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.opurex.ortus.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.opurex.ortus.client.activities.POSConnectedTrackedActivity;
import com.opurex.ortus.client.models.Customer;
import com.opurex.ortus.client.models.Payment;
import com.opurex.ortus.client.models.Receipt;
import com.opurex.ortus.client.models.Ticket;
import com.opurex.ortus.client.models.TicketLine;
import com.opurex.ortus.client.utils.StringUtils;

import java.util.List;

public class ReceiptDetailActivity extends POSConnectedTrackedActivity {

    private Receipt receipt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_detail_material);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Receipt Details");
        }

        // Get receipt from intent
        Intent intent = getIntent();
        String receiptId = intent.getStringExtra("RECEIPT_ID");

        // First, try to find the receipt by ID
        this.receipt = null; // Reset receipt to null first
        if (receiptId != null && !receiptId.isEmpty()) {
            List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
            for (Receipt r : receipts) {
                if (r != null && r.getTicket() != null && r.getTicket().getId() != null &&
                    r.getTicket().getId().equals(receiptId)) {
                    this.receipt = r;
                    break;
                }
            }
        }

        // If not found by ID, try to get from position (fallback)
        if (receipt == null) {
            int position = intent.getIntExtra("RECEIPT_POSITION", -1);
            if (position >= 0) {
                List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
                if (position < receipts.size()) {
                    this.receipt = receipts.get(position);
                }
            }
        }

        // If still null, try with the position as a fallback
        if (receipt == null) {
            // Try to get receipt by position as fallback
            int position = intent.getIntExtra("RECEIPT_POSITION", -1);
            if (position >= 0) {
                List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
                if (position < receipts.size()) {
                    this.receipt = receipts.get(position);
                    // Show a message indicating it's using position fallback
                    Toast.makeText(this, "Using position fallback: " + position + "/" + receipts.size(),
                                  Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Position out of bounds: " + position + ", total: " + receipts.size(),
                                  Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Could not find receipt with ID: " + receiptId +
                              " and no valid position provided",
                              Toast.LENGTH_LONG).show();
            }
        }

        if (receipt != null) {
            displayReceiptDetails();

            // Enable action buttons since receipt is loaded
            MaterialButton btnPrint = findViewById(R.id.btn_print);
            MaterialButton btnRefund = findViewById(R.id.btn_refund);

            btnPrint.setEnabled(true);
            btnRefund.setEnabled(true);
        } else {
            // Receipt not found - show error message and disable buttons
            Toast.makeText(this, "Receipt not found", Toast.LENGTH_LONG).show();

            MaterialButton btnPrint = findViewById(R.id.btn_print);
            MaterialButton btnRefund = findViewById(R.id.btn_refund);

            btnPrint.setEnabled(false);
            btnRefund.setEnabled(false);
        }

        // Setup action buttons
        MaterialButton btnPrint = findViewById(R.id.btn_print);
        MaterialButton btnRefund = findViewById(R.id.btn_refund);

        btnPrint.setOnClickListener(v -> printReceipt());
        btnRefund.setOnClickListener(v -> refundReceipt());

        // Make sure buttons are enabled if receipt was loaded earlier
        if (receipt != null) {
            btnPrint.setEnabled(true);
            btnRefund.setEnabled(true);
        } else {
            btnPrint.setEnabled(false);
            btnRefund.setEnabled(false);
        }
    }

    private void displayReceiptDetails() {
        // Set header information
        TextView ticketId = findViewById(R.id.receipt_ticket_id);
        TextView dateTime = findViewById(R.id.receipt_date_time);
        TextView amount = findViewById(R.id.receipt_amount);
        TextView paymentMode = findViewById(R.id.receipt_payment_mode);
        TextView user = findViewById(R.id.receipt_user);
        TextView customer = findViewById(R.id.receipt_customer);

        Ticket ticket = receipt.getTicket();
        ticketId.setText(getString(R.string.ticket_label, ticket.getTicketId()));
        dateTime.setText(StringUtils.formatDateTimeNumeric(this, receipt.getPaymentTime() * 1000));
        amount.setText(getString(R.string.ticket_total, ticket.getTicketPrice()));

        // Determine payment mode
        StringBuilder pmBuilder = new StringBuilder();
        boolean multiplePm = false;
        String firstPm = null;
        for (Payment p : receipt.getPayments()) {
            if (firstPm != null && !p.getMode().getCode().equals(firstPm)) {
                multiplePm = true;
                break;
            }
            firstPm = p.getMode().getCode();
        }
        if (multiplePm) {
            pmBuilder.append(getString(R.string.ticket_pm_multiple));
        } else {
            if (firstPm != null) {
                // Use iterator to get first payment since getPayments() returns Collection, not List
                if (!receipt.getPayments().isEmpty()) {
                    Payment firstPayment = receipt.getPayments().iterator().next();
                    pmBuilder.append(firstPayment.getMode().getLabel());
                }
            }
        }
        paymentMode.setText(pmBuilder.toString());

        user.setText("Cashier: " + receipt.getUser().getName());

        Customer cust = ticket.getCustomer();
        if (cust != null) {
            customer.setText("Customer: " + cust.getName());
        } else {
            customer.setText("Customer: None");
        }

        // Display products
        LinearLayout productsContainer = findViewById(R.id.products_container);
        productsContainer.removeAllViews(); // Clear previous items

        for (TicketLine line : ticket.getLines()) {
            // Create a view for each product line
            TextView productView = new TextView(this);
            // Format the product line with better readability
            String productText = String.format("%s x %.3f - %.2f KSH",
                                             line.getProduct().getLabel(),
                                             line.getQuantity(),
                                             line.getTotalDiscPIncTax());
            productView.setText(productText);
            productView.setTextSize(14);
            productView.setTextColor(getColor(com.opurex.ortus.client.R.color.colorOnSurface));
            productView.setPadding(0, 0, 0, 8); // Add bottom padding

            productsContainer.addView(productView);
        }

        // Display totals
        TextView subtotal = findViewById(R.id.receipt_subtotal);
        TextView tax = findViewById(R.id.receipt_tax);
        TextView discount = findViewById(R.id.receipt_discount);
        TextView total = findViewById(R.id.receipt_total_summary);

        // Calculate values properly
        double subtotalValue = ticket.getSubtotal();
        double taxValue = ticket.getTaxCost();
        // Use the same discount calculation as in the Ticket class
        double discountValue = ticket.getFinalDiscount();
        double totalValue = ticket.getTicketPrice();

        subtotal.setText(String.format("%.2f KSH", subtotalValue));
        tax.setText(String.format("%.2f KSH", taxValue));
        discount.setText(String.format("%.2f KSH", discountValue));
        total.setText(String.format("%.2f KSH", totalValue));
    }

    private void printReceipt() {
        if (receipt != null) {
            showProgressDialog();
            if (!this.printReceipt(receipt)) {
                askReprint();
            } else {
                // Receipt printed successfully, dismiss the progress dialog after a delay
                new android.os.Handler().postDelayed(() -> dismissProgressDialog(), 2000);
            }
        }
    }

    private android.app.ProgressDialog progressDialog;

    private void showProgressDialog() {
        dismissProgressDialog(); // Dismiss any existing dialog
        progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Printing receipt...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void askReprint() {
        dismissProgressDialog(); // Dismiss the progress dialog first
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Print Failed");
        builder.setMessage("Could not print the receipt. Would you like to try again?");
        builder.setPositiveButton("Retry", (dialog, which) -> {
            dialog.dismiss();
            showProgressDialog();
            if (!this.printReceipt(receipt)) {
                askReprint();
            } else {
                // Receipt printed successfully
                new android.os.Handler().postDelayed(() -> dismissProgressDialog(), 2000);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void refundReceipt() {
        if (receipt != null) {
            // Show confirmation dialog before proceeding with refund
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Confirm Refund");
            builder.setMessage("Are you sure you want to refund this receipt?\n\nTicket ID: " +
                             receipt.getTicket().getTicketId() +
                             "\nAmount: " + String.format("%.2f KSH", receipt.getTicket().getTicketPrice()));
            builder.setPositiveButton("Yes, Refund", (dialog, which) -> {
                dialog.dismiss();
                // Proceed with refund
                Intent intent = new Intent();
                intent.putExtra(ReceiptSelect.TICKET_ID_KEY, receipt.getTicket().getId());
                setResult(Transaction.PAST_TICKET_FOR_RESULT, intent);
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareReceipt();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.receipt_detail_menu, menu);
        return true;
    }

    private void shareReceipt() {
        if (receipt != null) {
            Ticket ticket = receipt.getTicket();

            // Create a formatted receipt text
            StringBuilder receiptText = new StringBuilder();
            receiptText.append("=== RECEIPT DETAILS ===\n\n");
            receiptText.append("Ticket ID: ").append(ticket.getTicketId()).append("\n");
            receiptText.append("Date/Time: ").append(StringUtils.formatDateTimeNumeric(this, receipt.getPaymentTime() * 1000)).append("\n");
            receiptText.append("Cashier: ").append(receipt.getUser().getName()).append("\n");

            Customer cust = ticket.getCustomer();
            if (cust != null) {
                receiptText.append("Customer: ").append(cust.getName()).append("\n");
            }

            receiptText.append("\n--- PRODUCTS ---\n");
            for (TicketLine line : ticket.getLines()) {
                receiptText.append(String.format("%s x %.3f - %.2f KSH\n",
                                               line.getProduct().getLabel(),
                                               line.getQuantity(),
                                               line.getTotalDiscPIncTax()));
            }

            receiptText.append("\n--- SUMMARY ---\n");
            // Calculate values properly for sharing
            double subtotalValue = ticket.getSubtotal();
            double taxValue = ticket.getTaxCost();
            // Use the same discount calculation as in the Ticket class
            double discountValue = ticket.getFinalDiscount();
            double totalValue = ticket.getTicketPrice();

            receiptText.append(String.format("Subtotal: %.2f KSH\n", subtotalValue));
            receiptText.append(String.format("Tax: %.2f KSH\n", taxValue));
            receiptText.append(String.format("Discount: %.2f KSH\n", discountValue));
            receiptText.append(String.format("Total: %.2f KSH\n", totalValue));

            // Create share intent
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText.toString());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Receipt #" + ticket.getTicketId());
            shareIntent.setType("text/plain");

            Intent chooser = Intent.createChooser(shareIntent, "Share Receipt via");
            startActivity(chooser);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Store the new intent

        // Get receipt from the new intent
        String receiptId = intent.getStringExtra("RECEIPT_ID");

        // First, try to find the receipt by ID
        this.receipt = null; // Reset receipt to null first
        if (receiptId != null && !receiptId.isEmpty()) {
            List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
            for (Receipt r : receipts) {
                if (r != null && r.getTicket() != null && r.getTicket().getId() != null &&
                    r.getTicket().getId().equals(receiptId)) {
                    this.receipt = r;
                    break;
                }
            }
        }

        // If not found by ID, try to get from position (fallback)
        if (receipt == null) {
            int position = intent.getIntExtra("RECEIPT_POSITION", -1);
            if (position >= 0) {
                List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
                if (position < receipts.size()) {
                    this.receipt = receipts.get(position);
                }
            }
        }

        if (receipt != null) {
            displayReceiptDetails();

            // Enable action buttons since receipt is loaded
            MaterialButton btnPrint = findViewById(R.id.btn_print);
            MaterialButton btnRefund = findViewById(R.id.btn_refund);

            btnPrint.setEnabled(true);
            btnRefund.setEnabled(true);
        } else {
            // Try to get receipt by position as fallback
            int position = intent.getIntExtra("RECEIPT_POSITION", -1);
            if (position >= 0) {
                List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);
                if (position < receipts.size()) {
                    this.receipt = receipts.get(position);
                    // Show a message indicating it's using position fallback
                    Toast.makeText(this, "Using position fallback: " + position + "/" + receipts.size(),
                                  Toast.LENGTH_SHORT).show();

                    // Update UI since we now have a valid receipt
                    displayReceiptDetails();
                    MaterialButton btnPrint = findViewById(R.id.btn_print);
                    MaterialButton btnRefund = findViewById(R.id.btn_refund);
                    btnPrint.setEnabled(true);
                    btnRefund.setEnabled(true);
                } else {
                    Toast.makeText(this, "Position out of bounds: " + position + ", total: " + receipts.size(),
                                  Toast.LENGTH_LONG).show();

                    MaterialButton btnPrint = findViewById(R.id.btn_print);
                    MaterialButton btnRefund = findViewById(R.id.btn_refund);
                    btnPrint.setEnabled(false);
                    btnRefund.setEnabled(false);
                }
            } else {
                Toast.makeText(this, "Receipt not found: " + receiptId +
                              " and no valid position provided",
                              Toast.LENGTH_LONG).show();

                MaterialButton btnPrint = findViewById(R.id.btn_print);
                MaterialButton btnRefund = findViewById(R.id.btn_refund);
                btnPrint.setEnabled(false);
                btnRefund.setEnabled(false);
            }
        }
    }

    @Override
    public void onDeviceManagerEvent(com.opurex.ortus.client.drivers.POSDeviceManager manager, com.opurex.ortus.client.drivers.utils.DeviceManagerEvent event) {
        // Handle device manager events if needed
        // This is an abstract method that must be implemented
    }
}