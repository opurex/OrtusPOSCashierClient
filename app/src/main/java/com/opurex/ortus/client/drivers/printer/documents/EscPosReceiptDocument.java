package com.opurex.ortus.client.drivers.printer.documents;

import android.content.Context;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.data.DataSavable.CashRegisterData;
import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.models.Currency;
import com.opurex.ortus.client.drivers.printer.Printer;
import com.opurex.ortus.client.drivers.printer.PrinterHelper;
import com.opurex.ortus.client.models.CashRegister;
import com.opurex.ortus.client.models.Customer;
import com.opurex.ortus.client.models.Payment;
import com.opurex.ortus.client.models.PaymentMode;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.models.Receipt;
import com.opurex.ortus.client.models.Ticket;
import com.opurex.ortus.client.models.TicketLine;

import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.DecimalFormat;

/** Enhanced receipt document using ESCPOS formatting */
public class EscPosReceiptDocument implements PrintableDocument {
    private Receipt r;

    public EscPosReceiptDocument(Receipt r) {
        this.r = r;
    }

    @Override
    public boolean print(Printer printer, Context ctx) {
        if (!printer.isConnected()) {
            return false;
        }

        DecimalFormat priceFormat = new DecimalFormat("#0.00");
        Currency mainCurrency = Data.Currency.getMain(ctx);
        String currencySymbol = mainCurrency != null ? mainCurrency.getSymbol() : "Ksh";
        Customer c = this.r.getTicket().getCustomer();

        try {
            // Enhanced formatting with ESCPOS tags
            printer.printLine("[C]<u><font size='big'>ORTUS POS RECEIPT</font></u>");
            printer.printLine();

            // Date and time
            CashRegisterData crData = new CashRegisterData();
            CashRegister cr = crData.current(ctx);
            DateFormat df = DateFormat.getDateTimeInstance();
            String date = df.format(new Date(this.r.getPaymentTime() * 1000));
            
            printer.printLine("[L]Date: " + date + "[R]#" + this.r.getTicketNumber());
            
            // Customer info
            if (c != null) {
                printer.printLine("[L]Customer: " + c.getName());
            } else {
                printer.printLine("[L]Customer: Walk-in Customer");
            }

            printer.printLine("[L]Cashier: " + this.r.getUser().getName());
            printer.printLine();

            // Header for items
            printer.printLine("--------------------------------");
            printer.printLine("[L]ITEM[R]QTY[R]PRICE");
            printer.printLine("--------------------------------");

            // Print each ticket line
            for (TicketLine line : this.r.getTicket().getLines()) {
                Product product = line.getProduct();
                
                // Print product name
                printer.printLine("[L]" + product.getLabel());
                
                // Print quantity and price
                String lineTxt = priceFormat.format(line.getProductIncTax());
                lineTxt = "[R]x" + line.getQuantity() + "[R]" + priceFormat.format(line.getTotalDiscPIncTax());
                printer.printLine(lineTxt);
                
                // Print discount if applicable
                if (line.getDiscountRate() != 0) {
                    printer.printLine("[R]-" + (line.getDiscountRate() * 100) + "%");
                }
            }

            printer.printLine("--------------------------------");
            
            // Totals section
            if (this.r.getTicket().getDiscountRate() > 0.0) {
                Ticket ticket = this.r.getTicket();
                printer.printLine("[L]Discount: " + (ticket.getDiscountRate() * 100) + "%");
                printer.printLine("[R]-" + ticket.getFinalDiscount() + currencySymbol);
                printer.printLine("--------------------------------");
            }

            // Tax information
            List<Ticket.TaxLine> taxes = this.r.getTicket().getTaxLines();
            for (Ticket.TaxLine taxLine : taxes) {
                printer.printLine("[L]Tax (" + (taxLine.getRate() * 100) + "%)[R]" + 
                                 priceFormat.format(taxLine.getAmount()) + currencySymbol);
            }

            // Final totals
            printer.printLine();
            printer.printLine("[L]<b>Subtotal:[R]<b>" + 
                             priceFormat.format(this.r.getTicket().getTicketPriceExcTax()) + currencySymbol);
            printer.printLine("[L]<b>Total:[R]<b>" + 
                             priceFormat.format(this.r.getTicket().getTicketPrice()) + currencySymbol);
            printer.printLine("[L]<b>VAT:[R]<b>" + 
                             priceFormat.format(this.r.getTicket().getTaxCost()) + currencySymbol);

            // Payment information
            printer.printLine();
            for (Payment pmt : this.r.getPayments()) {
                printer.printLine("[L]" + pmt.getMode().getLabel() + 
                                 "[R]" + priceFormat.format(pmt.getGiven()) + currencySymbol);
                
                if (pmt.getBackPayment() != null) {
                    Payment backPmt = pmt.getBackPayment();
                    printer.printLine("[L]Change[R]-" + 
                                     priceFormat.format(backPmt.getGiven()) + currencySymbol);
                }
            }

            // Footer
            printer.printLine();
            printer.printLine("[C]Thank you for your business!");
            printer.printLine("[C]Visit us again!");
            
            // Add barcode for receipt ID
            printer.printLine("[C]<barcode type='code128'>" + this.r.getTicketNumber() + "</barcode>");
            
            // Cut the receipt
            printer.cut();
            printer.flush();
            
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for emulator visibility
            return false;
        }

        return true;
    }
}