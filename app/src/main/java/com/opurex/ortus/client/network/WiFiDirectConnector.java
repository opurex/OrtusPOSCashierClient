package com.opurex.ortus.client.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WiFiDirectConnector implements ConnectionHandler {
    private static final String TAG = "WiFiDirectConnector";
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private ServerSocket serverSocket;
    private ConnectionCallback callback;
    private boolean isRunning = false;
    private Context context;

    @Override
    public void initialize(Context context, ConnectionCallback callback) {
        this.context = context;
        this.callback = callback;
        this.wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        if (wifiP2pManager != null) {
            this.channel = wifiP2pManager.initialize(context, context.getMainLooper(), null);
        }
    }

    @Override
    public void startListening(int port) {
        if (wifiP2pManager == null || channel == null) {
            if (callback != null) {
                callback.onConnectionError("WiFi Direct not supported");
            }
            return;
        }

        registerP2PService(port);
        createP2PGroup();

        isRunning = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (isRunning && !serverSocket.isClosed()) {
                    try {
                        Socket client = serverSocket.accept();
                        Log.d(TAG, "Client connected via WiFi Direct: " + client.getInetAddress());
                        if (callback != null) {
                            callback.onClientConnected(client, "WIFI-DIRECT:" + client.getInetAddress());
                        }
                    } catch (IOException e) {
                        if (isRunning) {
                            Log.e(TAG, "Error accepting connection on WiFi Direct", e);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error starting server socket on WiFi Direct", e);
            }
        }).start();
    }

    private void createP2PGroup() {
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "WiFi Direct Group created successfully");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "WiFi Direct Group creation failed: " + reason);
                if (reason != WifiP2pManager.P2P_UNSUPPORTED && reason != WifiP2pManager.BUSY) {
                    // Group might already exist, which is fine
                }
            }
        });
    }

    private void registerP2PService(int port) {
        Map<String, String> record = new HashMap<>();
        record.put("listenport", String.valueOf(port));
        record.put("service", "ortuspos_transaction");

        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance(
                "OrtusPOS", "_presence._tcp", record);

        wifiP2pManager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "WiFi Direct local service added");
                if (callback != null) {
                    callback.onStatusChanged(ConnectionStatus.CONNECTED);
                }
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "WiFi Direct local service add failed: " + reason);
                if (callback != null) {
                    callback.onConnectionError("WiFi Direct service registration failed: " + reason);
                }
            }
        });
    }

    @Override
    public void stopListening() {
        isRunning = false;
        if (wifiP2pManager != null && channel != null) {
            wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "WiFi Direct Group removed");
                }

                @Override
                public void onFailure(int reason) {
                    Log.e(TAG, "WiFi Direct Group removal failed: " + reason);
                }
            });
            wifiP2pManager.clearLocalServices(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "WiFi Direct local services cleared");
                }

                @Override
                public void onFailure(int reason) {
                    Log.e(TAG, "WiFi Direct clear local services failed: " + reason);
                }
            });
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing server socket", e);
            }
        }
    }

    @Override
    public boolean isConnected() {
        return isRunning;
    }

    @Override
    public void cleanup() {
        stopListening();
    }
}
