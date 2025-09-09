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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.opurex.ortus.client.drivers.printer.Printer;

/**
 * A test document for verifying OCPP M083 printer functionality
 */
public class OCPM083TestDocument implements PrintableDocument {
    
    @Override
    public boolean print(Printer printer, Context context) {
        try {
            printer.initPrint();
            
            // Print header
            printer.printLine("OCPP M083 Printer Test");
            printer.printLine("=====================");
            printer.printLine();
            
            // Print text
            printer.printLine("This is a test print from Ortus POS");
            printer.printLine("Testing OCPP M083 Bluetooth Printer");
            printer.printLine();
            
            // Print some special characters
            printer.printLine("Special characters: éèàùç");
            printer.printLine();
            
            // Print a simple graphic
            Bitmap testBitmap = createTestBitmap();
            printer.printBitmap(testBitmap);
            testBitmap.recycle();
            
            // Print footer
            printer.printLine("---------------------");
            printer.printLine("Test completed");
            printer.printLine();
            
            // Cut the paper
            printer.cut();
            
            // Flush the print buffer
            printer.flush();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Creates a simple test bitmap for printing
     * @return A bitmap with a simple pattern
     */
    private Bitmap createTestBitmap() {
        int width = 200;
        int height = 100;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        
        // Fill with white background
        canvas.drawColor(Color.WHITE);
        
        // Draw a simple pattern
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        
        // Draw text
        canvas.drawText("OCPP M083 Test", 10, 30, paint);
        
        // Draw rectangles
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(new Rect(10, 40, 190, 90), paint);
        
        // Draw lines
        canvas.drawLine(10, 65, 190, 65, paint);
        canvas.drawLine(100, 40, 100, 90, paint);
        
        return bitmap;
    }
}