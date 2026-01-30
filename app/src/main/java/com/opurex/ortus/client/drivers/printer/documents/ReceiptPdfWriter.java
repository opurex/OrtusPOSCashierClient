package com.opurex.ortus.client.drivers.printer.documents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ReceiptPdfWriter {

    private ReceiptPdfWriter() {}

    public static Uri write(Context ctx, List<String> lines, String receiptId)
            throws Exception {

        PdfDocument pdf = new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(226, 1200, 1).create();

        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setTextSize(10);
        paint.setTypeface(Typeface.MONOSPACE);

        int y = 20;
        for (String line : lines) {
            canvas.drawText(line, 10, y, paint);
            y += 14;
        }

        pdf.finishPage(page);

        String fileName = "receipt_" + receiptId + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date()) + ".pdf";

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS + "/OrtusPOS/Receipts"
        );

        ContentResolver resolver = ctx.getContentResolver();
        Uri uri = resolver.insert(
                MediaStore.Files.getContentUri("external"),
                values
        );

        OutputStream os = resolver.openOutputStream(uri);
        pdf.writeTo(os);
        os.close();
        pdf.close();

        return uri;
    }
}
