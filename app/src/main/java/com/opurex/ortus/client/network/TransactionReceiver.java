package com.opurex.ortus.client.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TransactionReceiver {
    private static final String TAG = "TransactionReceiver";
    private final MultiConnectionManager connectionManager;
    private final TransactionProcessor processor;

    public TransactionReceiver(Context context, TransactionProcessor processor) {
        this.processor = processor;
        this.connectionManager = new MultiConnectionManager(context, new ConnectionCallback() {
            @Override
            public void onClientConnected(Socket clientSocket, String clientInfo) {
                handleClientConnection(clientSocket, clientInfo);
            }

            @Override
            public void onConnectionError(String error) {
                Log.e(TAG, "Connection error: " + error);
            }

            @Override
            public void onStatusChanged(ConnectionStatus status) {
                Log.d(TAG, "Connection status changed: " + status);
            }
        });
    }

    private void handleClientConnection(Socket clientSocket, String clientInfo) {
        new Thread(() -> {
            try (Socket socket = clientSocket;
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String json = reader.readLine();
                if (json != null) {
                    Gson gson = new Gson();
                    NetworkTransaction transaction = gson.fromJson(json, NetworkTransaction.class);
                    Log.d(TAG, "Received transaction from " + clientInfo);

                    if (processor != null) {
                        processor.processTransaction(transaction, clientInfo);
                    }

                    TransactionResponse response = new TransactionResponse("ACK", "Transaction processed");
                    writer.write(gson.toJson(response));
                    writer.newLine();
                    writer.flush();
                }

            } catch (IOException e) {
                Log.e(TAG, "Error handling client connection", e);
            }
        }).start();
    }

    public void start(int port) {
        connectionManager.startAllConnections(port);
    }

    public void stop() {
        connectionManager.stopAllConnections();
    }
}
