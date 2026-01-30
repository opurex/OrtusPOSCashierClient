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
import com.opurex.ortus.client.widgets.MaterialProgressDialog;

import java.util.Iterator;
import java.util.List;

public class ReceiptDetailActivity extends POSConnectedTrackedActivity {

    private Receipt receipt;
    private MaterialProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_detail_material);

        setupToolbar();
        loadReceipt(getIntent());
        setupButtons();
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Receipt Details");
        }
    }

    private void loadReceipt(Intent intent) {
        receipt = findReceiptFromIntent(intent);

        MaterialButton btnPrint = findViewById(R.id.btn_print);
        MaterialButton btnRefund = findViewById(R.id.btn_refund);

        if (receipt != null) {
            displayReceiptDetails();
            btnPrint.setEnabled(true);
            btnRefund.setEnabled(true);
        } else {
            Toast.makeText(this, "Receipt not found", Toast.LENGTH_LONG).show();
            btnPrint.setEnabled(false);
            btnRefund.setEnabled(false);
        }
    }

    private Receipt findReceiptFromIntent(Intent intent) {
        String receiptId = intent.getStringExtra("RECEIPT_ID");
        int position = intent.getIntExtra("RECEIPT_POSITION", -1);

        List<Receipt> receipts = com.opurex.ortus.client.data.Data.Receipt.getReceipts(this);

        if (receiptId != null) {
            for (Receipt r : receipts) {
                if (r != null && r.getTicket() != null &&
                        receiptId.equals(r.getTicket().getId())) {
                    return r;
                }
            }
        }

        if (position >= 0 && position < receipts.size()) {
            return receipts.get(position);
        }

        return null;
    }

    private void setupButtons() {
        MaterialButton btnPrint = findViewById(R.id.btn_print);
        MaterialButton btnRefund = findViewById(R.id.btn_refund);

        btnPrint.setOnClickListener(v -> printReceipt());
        btnRefund.setOnClickListener(v -> refundReceipt());
    }

    private void displayReceiptDetails() {
        Ticket ticket = receipt.getTicket();
        if (ticket == null) return;

        // Header
        ((TextView) findViewById(R.id.receipt_ticket_id))
                .setText(getString(R.string.ticket_label, ticket.getTicketId()));

        ((TextView) findViewById(R.id.receipt_date_time))
                .setText(StringUtils.formatDateTimeNumeric(
                        this, receipt.getPaymentTime() * 1000));

        ((TextView) findViewById(R.id.receipt_amount))
                .setText(getString(R.string.ticket_total, ticket.getTicketPrice()));

        // Totals (FIXED ORDER)
        double subtotalValue = ticket.getSubtotal();
        double taxValue = ticket.getTaxCost();
        double discountValue = ticket.getFinalDiscount();
        double totalValue = ticket.getTicketPrice();

        ((TextView) findViewById(R.id.receipt_subtotal_right))
                .setText(String.format("%.2f KSH", subtotalValue));

        ((TextView) findViewById(R.id.receipt_tax_right))
                .setText(String.format("%.2f KSH", taxValue));

        ((TextView) findViewById(R.id.receipt_discount_right))
                .setText(String.format("%.2f KSH", discountValue));

        ((TextView) findViewById(R.id.receipt_total_summary))
                .setText(String.format("%.2f KSH", totalValue));

        // Payment mode
        TextView paymentMode = findViewById(R.id.receipt_payment_mode);
        Iterator<Payment> it = receipt.getPayments().iterator();
        if (!it.hasNext()) {
            paymentMode.setText("-");
        } else {
            Payment first = it.next();
            boolean multiple = false;
            while (it.hasNext()) {
                if (!it.next().getMode().getCode()
                        .equals(first.getMode().getCode())) {
                    multiple = true;
                    break;
                }
            }
            paymentMode.setText(multiple
                    ? getString(R.string.ticket_pm_multiple)
                    : first.getMode().getLabel());
        }

        // Cashier & customer
        ((TextView) findViewById(R.id.receipt_user))
                .setText("Cashier: " + receipt.getUser().getName());

        Customer cust = ticket.getCustomer();
        ((TextView) findViewById(R.id.receipt_customer))
                .setText("Customer: " + (cust != null ? cust.getName() : "None"));

        // Products
        LinearLayout container = findViewById(R.id.products_container);
        container.removeAllViews();

        for (TicketLine line : ticket.getLines()) {
            TextView tv = new TextView(this);
            tv.setText(String.format(
                    "%s x %.3f - %.2f KSH",
                    line.getProduct().getLabel(),
                    line.getQuantity(),
                    line.getTotalDiscPIncTax()));
            tv.setTextSize(14);
            tv.setPadding(0, 0, 0, 8);
            container.addView(tv);
        }
    }

    private void printReceipt() {
        if (receipt == null) return;

        showProgressDialog();
        if (!super.printReceipt(receipt)) {
            askReprint();
        } else {
            new android.os.Handler().postDelayed(
                    this::dismissProgressDialog, 2000);
        }
    }

    private void showProgressDialog() {
        dismissProgressDialog();
        progressDialog = MaterialProgressDialog.show(this, "Printing receipt...", false);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void askReprint() {
        dismissProgressDialog();
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Print Failed")
                .setMessage("Retry printing receipt?")
                .setPositiveButton("Retry", (d, w) -> printReceipt())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refundReceipt() {
        if (receipt == null) return;

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Refund")
                .setMessage("Refund ticket " +
                        receipt.getTicket().getTicketId() + "?")
                .setPositiveButton("Yes", (d, w) -> {
                    Intent i = new Intent();
                    i.putExtra(
                            ReceiptSelect.TICKET_ID_KEY,
                            receipt.getTicket().getId());
                    setResult(Transaction.PAST_TICKET_FOR_RESULT, i);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        loadReceipt(intent);
    }

    @Override
    public void onDeviceManagerEvent(
            com.opurex.ortus.client.drivers.POSDeviceManager manager,
            com.opurex.ortus.client.drivers.utils.DeviceManagerEvent event) {
        // No-op
    }
}
