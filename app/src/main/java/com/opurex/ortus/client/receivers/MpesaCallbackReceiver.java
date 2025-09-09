package com.opurex.ortus.client.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.opurex.ortus.client.payment.MpesaPaymentProcessor;

/**
 * BroadcastReceiver to handle M-Pesa payment callbacks
 */
public class MpesaCallbackReceiver extends BroadcastReceiver {
    private static final String TAG = "MpesaCallbackReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received M-Pesa callback");
        
        // Extract payment status and transaction ID from intent
        boolean success = intent.getBooleanExtra("success", false);
        String transactionId = intent.getStringExtra("transactionId");
        String message = intent.getStringExtra("message");
        
        Log.d(TAG, "Payment status: " + (success ? "SUCCESS" : "FAILED") + 
              ", Transaction ID: " + transactionId + 
              ", Message: " + message);
        
        // Handle M-Pesa callback
        MpesaPaymentProcessor.handleCallback(context, success, transactionId, message);
    }
}