package com.opurex.ortus.client.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.network.NetworkTransaction;
import com.opurex.ortus.client.network.TransactionProcessor;
import com.opurex.ortus.client.network.TransactionReceiver;

public class TransactionReceiverService extends Service {
    private static final String TAG = "TransactionRecService";
    private static final int DEFAULT_PORT = 9090;
    private TransactionReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service creating");

        TransactionProcessor processor = new TransactionProcessor() {
            @Override
            public void processTransaction(NetworkTransaction transaction, String clientInfo) {
                handleIncomingTransaction(transaction);
            }
        };

        receiver = new TransactionReceiver(this, processor);
        receiver.start(DEFAULT_PORT);
    }

    private void handleIncomingTransaction(NetworkTransaction transaction) {
        if (transaction.isReceipt()) {
            Log.d(TAG, "Processing incoming Receipt");
            Data.Receipt.addReceipt(transaction.getReceipt());
            try {
                Data.Receipt.save();
                Data.TicketId.ticketClosed(this);
            } catch (Exception e) {
                Log.e(TAG, "Error saving received receipt", e);
            }
        } else if (transaction.isTicket()) {
            Log.d(TAG, "Processing incoming Ticket");
            Data.Session.currentSession().addTicketToRunningTickets(transaction.getTicket());
            try {
                Data.Session.save();
            } catch (Exception e) {
                Log.e(TAG, "Error saving received ticket", e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroying");
        if (receiver != null) {
            receiver.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
