package com.opurex.ortus.client.activities.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.opurex.ortus.client.R;
import com.opurex.ortus.client.activities.TrackedActivity;
import com.opurex.ortus.client.Configure;

/**
 * Test activity for M-Pesa integration
 */
public class MpesaTestActivity extends TrackedActivity {
    private static final String TAG = "MpesaTestActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_test);
        
        Button testButton = findViewById(R.id.test_mpesa_button);
        testButton.setOnClickListener(v -> testMpesaIntegration());
        
        Button simulateSuccessButton = findViewById(R.id.simulate_success_button);
        simulateSuccessButton.setOnClickListener(v -> simulatePaymentSuccess());
        
        Button simulateFailureButton = findViewById(R.id.simulate_failure_button);
        simulateFailureButton.setOnClickListener(v -> simulatePaymentFailure());
    }
    
    private void testMpesaIntegration() {
        // Check if MPESA is properly configured
        String businessShortCode = Configure.getMpesaBusinessShortCode(this);
        String passkey = Configure.getMpesaPasskey(this);
        String testPhone = Configure.getMpesaTestPhoneNumber(this);
        
        if (businessShortCode == null || businessShortCode.isEmpty()) {
            Toast.makeText(this, "MPESA Business Short Code not configured. Please check settings.", Toast.LENGTH_LONG).show();
            return;
        }
        
        if (passkey == null || passkey.isEmpty()) {
            Toast.makeText(this, "MPESA Passkey not configured. Please check settings.", Toast.LENGTH_LONG).show();
            return;
        }
        
        if (testPhone == null || testPhone.isEmpty()) {
            Toast.makeText(this, "Test phone number not configured. Please check settings.", Toast.LENGTH_LONG).show();
            return;
        }
        
        Toast.makeText(this, "MPESA is properly configured. Test during an actual transaction.", Toast.LENGTH_LONG).show();
        Log.d(TAG, "MPESA configuration verified");
    }
    
    private void simulatePaymentSuccess() {
        try {
            // Send a test broadcast to simulate successful M-Pesa callback
            Intent intent = new Intent("com.opurex.ortus.client.MPESA_CALLBACK");
            intent.putExtra("success", true);
            intent.putExtra("transactionId", "TEST123456");
            intent.putExtra("message", "Test payment successful");
            sendBroadcast(intent);
            
            Toast.makeText(this, "Simulated successful payment callback sent", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Simulated successful M-Pesa callback sent");
        } catch (Exception e) {
            Log.e(TAG, "Error simulating successful payment", e);
            Toast.makeText(this, "Simulation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void simulatePaymentFailure() {
        try {
            // Send a test broadcast to simulate failed M-Pesa callback
            Intent intent = new Intent("com.opurex.ortus.client.MPESA_CALLBACK");
            intent.putExtra("success", false);
            intent.putExtra("transactionId", "TEST123456");
            intent.putExtra("message", "Test payment failed");
            sendBroadcast(intent);
            
            Toast.makeText(this, "Simulated failed payment callback sent", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Simulated failed M-Pesa callback sent");
        } catch (Exception e) {
            Log.e(TAG, "Error simulating failed payment", e);
            Toast.makeText(this, "Simulation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}