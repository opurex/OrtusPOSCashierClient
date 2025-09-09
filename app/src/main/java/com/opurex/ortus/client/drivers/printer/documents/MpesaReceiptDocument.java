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

/** 
 * Custom ReceiptDocument that includes MPESA transaction details
 */
public class MpesaReceiptDocument implements PrintableDocument
{
    private Receipt r;
    private String merchantRequestID;
    private String checkoutRequestID;

    public MpesaReceiptDocument (Receipt r, String merchantRequestID, String checkoutRequestID) {
        this.r = r;
        this.merchantRequestID = merchantRequestID;
        this.checkoutRequestID = checkoutRequestID;
    }

    public boolean print(Printer printer, Context ctx) {
        if (!printer.isConnected()) {
            return false;
        }
        DecimalFormat priceFormat = new DecimalFormat("#0.00");
        Currency mainCurrency = Data.Currency.getMain(ctx);
        String currencySymbol = mainCurrency != null ? mainCurrency.getSymbol() : "Ksh";
        Customer c = this.r.getTicket().getCustomer();
        printer.initPrint();
        PrinterHelper.printLogo(printer, ctx);
        PrinterHelper.printHeader(printer, ctx);
        // Title
        CashRegisterData crData = new CashRegisterData();
        CashRegister cr = crData.current(ctx);
        DateFormat df = DateFormat.getDateTimeInstance();
        String date = df.format(new Date(this.r.getPaymentTime() * 1000));
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_date), 7)
                + PrinterHelper.padBefore(date, 25));
        if (c != null) {
            printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_cust), 9)
                    + PrinterHelper.padBefore(c.getName(), 23));
        }
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_number), 16) +
                PrinterHelper.padBefore(cr.getMachineName() + " - " + this.r.getTicketNumber(), 16));
        printer.printLine();
        // Content
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_line_article), 10)
                + PrinterHelper.padBefore(ctx.getString(R.string.tkt_line_price), 7)
                + PrinterHelper.padBefore("", 5)
                + PrinterHelper.padBefore(ctx.getString(R.string.tkt_line_total), 10));
        printer.printLine();
        printer.printLine("--------------------------------");
        String lineTxt;
        for (TicketLine line : this.r.getTicket().getLines()) {
            printer.printLine(PrinterHelper.padAfter(line.getProduct().getLabel(), 32));
            lineTxt = priceFormat.format(line.getProductIncTax());
            lineTxt = PrinterHelper.padBefore(lineTxt, 17);
            lineTxt += PrinterHelper.padBefore("x" + line.getQuantity(), 5);
            lineTxt += PrinterHelper.padBefore(priceFormat.format(line.getTotalDiscPIncTax()), 10);
            printer.printLine(lineTxt);
            if (line.getDiscountRate() != 0) {
                printer.printLine(PrinterHelper.padBefore(ctx.getString(R.string.include_discount) + Double.toString(line.getDiscountRate() * 100) + "%", 32));
            }
        }
        printer.printLine("--------------------------------");
        if (this.r.getTicket().getDiscountRate() > 0.0) {
            Ticket ticket = this.r.getTicket();
            String line = PrinterHelper.padAfter(ctx.getString(R.string.tkt_discount_label), 16);
            line += PrinterHelper.padBefore((ticket.getDiscountRate() * 100) + "%", 6);
            line += PrinterHelper.padBefore("-" + ticket.getFinalDiscount() + currencySymbol, 10);
            printer.printLine(line);
            printer.printLine("--------------------------------");
        }
        // Taxes
        printer.printLine();
        DecimalFormat rateFormat = new DecimalFormat("#0.#");
        List<Ticket.TaxLine> taxes = this.r.getTicket().getTaxLines();
        for (Ticket.TaxLine line : taxes) {
            double rate = line.getRate();
            double dispRate = rate * 100;
            printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_tax)
                    + rateFormat.format(dispRate) + "%", 20)
                    + PrinterHelper.padBefore(priceFormat.format(line.getAmount()) + currencySymbol, 12));
        }
        printer.printLine();
        // Total
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_subtotal), 15)
                + PrinterHelper.padBefore(priceFormat.format(this.r.getTicket().getTicketPriceExcTax()) + currencySymbol, 17));
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_total), 15)
                + PrinterHelper.padBefore(priceFormat.format(this.r.getTicket().getTicketPrice()) + currencySymbol, 17));
        printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_inc_vat), 15)
                + PrinterHelper.padBefore(priceFormat.format(this.r.getTicket().getTaxCost()) + currencySymbol, 17));
        // Payments
        printer.printLine();
        printer.printLine();
        for (Payment pmt : this.r.getPayments()) {
            Payment retPmt = pmt.getBackPayment();
            if (pmt.getMode().getLabel().length() > 20) {
                printer.printLine(PrinterHelper.padAfter(pmt.getMode().getLabel(), 32));
                printer.printLine(PrinterHelper.padBefore(priceFormat.format(pmt.getGiven()) + currencySymbol, 32));
            } else {
                printer.printLine(
                       PrinterHelper.padAfter(pmt.getMode().getLabel(), 20)
                        + PrinterHelper.padBefore(priceFormat.format(pmt.getGiven()) + currencySymbol, 12)
                );
            }
            if (retPmt != null) {
                PaymentMode retMode = retPmt.getMode();
                if (retMode.getBackLabel().length() > 18) {
                    printer.printLine(PrinterHelper.padAfter("  " + retMode.getBackLabel(), 32));
                    printer.printLine(PrinterHelper.padBefore(priceFormat.format((-retPmt.getGiven())) + currencySymbol, 32)
                    );
                } else {
                    printer.printLine(
                            PrinterHelper.padAfter("  " + retMode.getBackLabel(), 20)
                            + PrinterHelper.padBefore(priceFormat.format((-retPmt.getGiven())) + currencySymbol, 12)
                    );
                }
            }
        }
        
        // MPESA Transaction Details
        if (merchantRequestID != null || checkoutRequestID != null) {
            printer.printLine();
            printer.printLine("----- MPESA TRANSACTION -----");
            if (merchantRequestID != null) {
                printer.printLine("Merchant Request ID:");
                printer.printLine(merchantRequestID);
            }
            if (checkoutRequestID != null) {
                printer.printLine("Checkout Request ID:");
                printer.printLine(checkoutRequestID);
            }
            printer.printLine("-----------------------------");
        }
        
        if (c != null) {
            double refill = 0.0;
            for (TicketLine l : this.r.getTicket().getLines()) {
                Product p = l.getProduct();
                if (p.isPrepaid()) {
                    refill += l.getProductIncTax() * l.getQuantity();
                }
            }
            printer.printLine();
            if (refill > 0.0) {
                printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_refill), 16)
                        + PrinterHelper.padBefore(priceFormat.format(refill) + currencySymbol, 16));
            }
            printer.printLine(PrinterHelper.padAfter(ctx.getString(R.string.tkt_prepaid_amount), 32));
            printer.printLine(PrinterHelper.padBefore(priceFormat.format(c.getPrepaid()) + currencySymbol, 32));
        }
        printer.printLine();
        PrinterHelper.printFooter(printer, ctx);
        if (this.r.hasDiscount()) {
            printer.printLine();
            PrinterHelper.printDiscount(printer, ctx, this.r.getDiscount());
        }
        printer.cut();
        printer.flush();
        // End
        return true;
    }
}