package com.opurex.ortus.client.payment;

import android.content.Context;
import android.widget.Toast;

import com.opurex.ortus.client.payment.FlavorPaymentProcessor;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Payment;
import com.opurex.ortus.client.activities.TrackedActivity;

import android.util.Log;

public class MpesaPaymentProcessor extends FlavorPaymentProcessor {
    private static final String TAG = "MpesaPaymentProcessor";
    
    // Static reference to the current instance for callback handling
    private static MpesaPaymentProcessor currentInstance;
    
    private String businessShortCode;
    private String passkey;
    private String callbackUrl;
    private String consumerKey;
    private String consumerSecret;
    private String merchantRequestID;
    private String checkoutRequestID;
    
    // MPESA SDK credentials (you'll need to configure these in the app settings)
    private static final String CONSUMER_KEY = "YOUR_CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "YOUR_CONSUMER_SECRET";

    public MpesaPaymentProcessor(TrackedActivity parentActivity, PaymentListener listener, Payment payment) {
        super(parentActivity, listener, payment);

        // Set current instance for callback handling
        currentInstance = this;

        // Get M-Pesa configuration from settings
        businessShortCode = com.opurex.ortus.client.Configure.getMpesaBusinessShortCode(parentActivity);
        passkey = com.opurex.ortus.client.Configure.getMpesaPasskey(parentActivity);
        callbackUrl = com.opurex.ortus.client.Configure.getMpesaCallbackUrl(parentActivity);
        consumerKey = CONSUMER_KEY; // In a real implementation, get this from settings
        consumerSecret = CONSUMER_SECRET; // In a real implementation, get this from settings

        Log.d(TAG, "M-Pesa payment processor initialized");
    }

    @Override
    public void handleIntent(int requestCode, int resultCode, android.content.Intent data) {
        // M-Pesa payments don't require intent handling as they use STK push
        // This method is for card payment systems that need to handle intents
    }

    @Override
    public Status initiatePayment() {
        try {
            // Validate configuration
            if (businessShortCode == null || businessShortCode.isEmpty()) {
                Log.e(TAG, "M-Pesa business short code not configured");
                Toast.makeText(parentActivity, "M-Pesa business short code not configured", Toast.LENGTH_LONG).show();
                return Status.PENDING;
            }

            if (passkey == null || passkey.isEmpty()) {
                Log.e(TAG, "M-Pesa passkey not configured");
                Toast.makeText(parentActivity, "M-Pesa passkey not configured", Toast.LENGTH_LONG).show();
                return Status.PENDING;
            }

            if (consumerKey == null || consumerKey.isEmpty()) {
                Log.e(TAG, "M-Pesa consumer key not configured");
                Toast.makeText(parentActivity, "M-Pesa consumer key not configured", Toast.LENGTH_LONG).show();
                return Status.PENDING;
            }

            if (consumerSecret == null || consumerSecret.isEmpty()) {
                Log.e(TAG, "M-Pesa consumer secret not configured");
                Toast.makeText(parentActivity, "M-Pesa consumer secret not configured", Toast.LENGTH_LONG).show();
                return Status.PENDING;
            }

            // STUB IMPLEMENTATION - M-Pesa SDK is not available
            Log.w(TAG, "M-Pesa SDK not available - simulating payment failure");
            Toast.makeText(parentActivity, "M-Pesa payment not available - SDK not found", Toast.LENGTH_LONG).show();
            return Status.PENDING;
        } catch (Exception e) {
            Log.e(TAG, "Error initiating M-Pesa payment", e);
            Toast.makeText(parentActivity, "Error initiating M-Pesa payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return Status.PENDING;
        }
    }

    /**
     * Format phone number to 254... format
     */
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return "254" + phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("+")) {
            return phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("254")) {
            return phoneNumber;
        }
        return "254" + phoneNumber;
    }

    /**
     * Handle payment confirmation from callback
     */
    public void handlePaymentConfirmation(boolean success, String transactionId) {
        if (success) {
            // Payment successful, register it
            Log.d(TAG, "M-Pesa payment successful for transaction: " + transactionId);
            Toast.makeText(parentActivity, "M-Pesa payment successful", Toast.LENGTH_LONG).show();
            listener.registerPayment(payment);
        } else {
            // Payment failed, handle accordingly
            Log.e(TAG, "M-Pesa payment failed for transaction: " + transactionId);
            Toast.makeText(parentActivity, "M-Pesa payment failed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Static method to handle callbacks from BroadcastReceiver
     */
    public static void handleCallback(Context context, boolean success, String transactionId, String message) {
        if (currentInstance != null) {
            currentInstance.handlePaymentConfirmation(success, transactionId);

            // Clear the current instance reference
            currentInstance = null;
        } else {
            Log.w(TAG, "No active M-Pesa payment processor instance to handle callback");
        }
    }

    // Getter methods for transaction details that can be used in receipt printing
    public String getMerchantRequestID() {
        return merchantRequestID;
    }

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }
}