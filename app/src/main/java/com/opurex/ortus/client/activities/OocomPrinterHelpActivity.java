package com.opurex.ortus.client.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.opurex.ortus.client.R;
import java.io.InputStream;

public class OocomPrinterHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oocom_printer_help);
        
        // Set up the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("OOCom Printer Setup Help");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Load help content from raw resource
        TextView helpTextView = findViewById(R.id.help_text);
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.oocom_printer_help);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String helpContent = new String(buffer);
            helpTextView.setText(helpContent);
        } catch (Exception e) {
            helpTextView.setText("Error loading help content: " + e.getMessage());
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}