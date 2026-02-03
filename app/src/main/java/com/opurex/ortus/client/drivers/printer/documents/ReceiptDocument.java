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

/** Proxy class to print receipts.
 * It is not directly set into Receipt for changes to the render
 * not break the serialization (and kill local receipts on update). */


import android.content.Context;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.data.DataSavable.CashRegisterData;
import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.models.Currency;
import com.opurex.ortus.client.drivers.printer.Printer;
import com.opurex.ortus.client.drivers.printer.PrinterHelper;
import com.opurex.ortus.client.models.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class ReceiptDocument implements PrintableDocument {

    private Receipt r;

    public ReceiptDocument(Receipt r) {
        this.r = r;
    }

    private void printAndSave(Printer printer, ReceiptBuffer buffer, String line) {
        printer.printLine(line);
        buffer.add(line);
    }

    private void printEmpty(Printer printer, ReceiptBuffer buffer) {
        printer.printLine();
        buffer.addEmpty();
    }

    @Override
    public boolean print(Printer printer, Context ctx) {

        if (!printer.isConnected()) {
            return false;
        }

        ReceiptBuffer buffer = new ReceiptBuffer();
        DecimalFormat priceFormat = new DecimalFormat("#0.00");

        Currency mainCurrency = Data.Currency.getMain(ctx);
        String currencySymbol = mainCurrency != null
                ? mainCurrency.getSymbol()
                : "Ksh";

        Customer c = r.getTicket().getCustomer();

        printer.initPrint();

        PrinterHelper.printLogo(printer, ctx);
        PrinterHelper.printHeader(printer, ctx);

        CashRegisterData crData = new CashRegisterData();
        CashRegister cr = crData.current(ctx);

        DateFormat df = DateFormat.getDateTimeInstance();
        String date = df.format(new Date(r.getPaymentTime() * 1000));

        printAndSave(printer, buffer,
                PrinterHelper.padAfter(ctx.getString(R.string.tkt_date), 7)
                        + PrinterHelper.padBefore(date, 25));

        if (c != null) {
            printAndSave(printer, buffer,
                    PrinterHelper.padAfter(ctx.getString(R.string.tkt_cust), 9)
                            + PrinterHelper.padBefore(c.getName(), 23));
        } else {
            printAndSave(printer, buffer,
                    PrinterHelper.padAfter(ctx.getString(R.string.tkt_cust), 9)
                            + PrinterHelper.padBefore("Walk-in Customer", 23));
        }

        printAndSave(printer, buffer,
                PrinterHelper.padAfter(ctx.getString(R.string.tkt_number), 16)
                        + PrinterHelper.padBefore(
                        cr.getMachineName() + " - " + r.getTicketNumber(), 16));

        printEmpty(printer, buffer);

        printAndSave(printer, buffer,
                PrinterHelper.padAfter(ctx.getString(R.string.tkt_line_article), 10)
                        + PrinterHelper.padBefore(ctx.getString(R.string.tkt_line_price), 7)
                        + PrinterHelper.padBefore("", 5)
                        + PrinterHelper.padBefore(ctx.getString(R.string.tkt_line_total), 10));

        printAndSave(printer, buffer, "--------------------------------");

        for (TicketLine line : r.getTicket().getLines()) {

            printAndSave(printer, buffer,
                    PrinterHelper.padAfter(line.getProduct().getLabel(), 32));

            String txt = PrinterHelper.padBefore(
                    priceFormat.format(line.getProductIncTax()), 17);

            txt += PrinterHelper.padBefore("x" + line.getQuantity(), 5);
            txt += PrinterHelper.padBefore(
                    priceFormat.format(line.getTotalDiscPIncTax()), 10);

            printAndSave(printer, buffer, txt);

            if (line.getDiscountRate() != 0) {
                printAndSave(printer, buffer,
                        PrinterHelper.padBefore(
                                ctx.getString(R.string.include_discount)
                                        + (line.getDiscountRate() * 100) + "%", 32));
            }
        }

        printAndSave(printer, buffer, "--------------------------------");

        printAndSave(printer, buffer,
                PrinterHelper.padAfter(ctx.getString(R.string.tkt_total), 15)
                        + PrinterHelper.padBefore(
                        priceFormat.format(r.getTicket().getTicketPrice())
                                + currencySymbol, 17));

        PrinterHelper.printFooter(printer, ctx);
        printer.cut();
        printer.flush();

        // ===== PDF COPY =====
        try {
            ReceiptPdfWriter.write(
                    ctx,
                    buffer.getLines(),
                    r.getTicketNumber()
            );
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for emulator visibility
        }

        return true;
    }
}
