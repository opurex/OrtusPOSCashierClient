package com.opurex.ortus.client.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.models.Ticket;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class P2PPaymentService extends Service {
    private static final String TAG = "P2PPaymentService";
    private static final String NAME = "OrtusP2P";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID

    public static final String ACTION_TICKET_RECEIVED = "com.opurex.ortus.client.ACTION_TICKET_RECEIVED";

    private BluetoothAdapter mBluetoothAdapter;
    private AcceptThread mAcceptThread;

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    private synchronized void startServer() {
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        mAcceptThread = new AcceptThread();
        mAcceptThread.start();
    }

    private synchronized void stopServer() {
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Socket listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (Exception e) {
                    Log.e(TAG, "Socket accept() failed", e);
                    break;
                }

                if (socket != null) {
                    handleConnectedSocket(socket);
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "Socket close() failed", e);
            }
        }
    }

    private void handleConnectedSocket(BluetoothSocket socket) {
        new Thread(() -> {
            try (InputStream inputStream = socket.getInputStream()) {

                java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                    // Check if we have a full JSON object (heuristic)
                    if (inputStream.available() == 0) break;
                }
                String data = result.toString("UTF-8");
                Log.d(TAG, "Received data length: " + data.length());

                if (data.isEmpty()) return;

                JSONObject json = new JSONObject(data);
                Ticket ticket = Ticket.fromJSON(getApplicationContext(), json);

                synchronized (Data.Session.currentSession()) {
                    Data.Session.currentSession().updateLocalTicket(ticket);
                    Data.Session.save();
                }

                // Notify UI
                Intent intent = new Intent(ACTION_TICKET_RECEIVED);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            } catch (Exception e) {
                Log.e(TAG, "Error handling socket", e);
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {}
            }
        }).start();
    }

    public static void sendTicket(BluetoothSocket socket, Ticket ticket) {
        new Thread(() -> {
            try (OutputStream outputStream = socket.getOutputStream()) {
                JSONObject json = ticket.toJSON(true);
                outputStream.write(json.toString().getBytes());
                outputStream.flush();
                Log.d(TAG, "Ticket sent");
            } catch (Exception e) {
                Log.e(TAG, "Error sending ticket", e);
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {}
            }
        }).start();
    }
}
