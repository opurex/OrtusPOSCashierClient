package com.opurex.ortus.client.network;

import android.content.Context;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalHotspotConnector implements ConnectionHandler {
    private static final String TAG = "LocalHotspotConnector";
    private WifiManager wifiManager;
    private WifiManager.LocalOnlyHotspotReservation hotspotReservation;
    private ServerSocket serverSocket;
    private ConnectionCallback callback;
    private boolean isRunning = false;
    private Context context;

    @Override
    public void initialize(Context context, ConnectionCallback callback) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.callback = callback;
    }

    @Override
    public void startListening(int port) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                    @Override
                    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                        super.onStarted(reservation);
                        hotspotReservation = reservation;
                        isRunning = true;

                        String ssid;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            SoftApConfiguration config = reservation.getSoftApConfiguration();
                            ssid = config != null ? config.getSsid() : "Unknown";
                        } else {
                            WifiConfiguration config = reservation.getWifiConfiguration();
                            ssid = config != null ? config.SSID : "Unknown";
                        }

                        Log.d(TAG, "Hotspot started. SSID: " + ssid);
                        startServer(port);
                        if (callback != null) {
                            callback.onStatusChanged(ConnectionStatus.CONNECTED);
                        }
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                        isRunning = false;
                        Log.d(TAG, "Hotspot stopped");
                        if (callback != null) {
                            callback.onStatusChanged(ConnectionStatus.DISCONNECTED);
                        }
                    }

                    @Override
                    public void onFailed(int reason) {
                        super.onFailed(reason);
                        isRunning = false;
                        Log.e(TAG, "Hotspot failed: " + reason);
                        if (callback != null) {
                            callback.onConnectionError("Hotspot failed with reason: " + reason);
                        }
                    }
                }, new Handler(Looper.getMainLooper()));
            } catch (SecurityException e) {
                Log.e(TAG, "Permission denied for hotspot", e);
                if (callback != null) {
                    callback.onConnectionError("Hotspot permission denied: " + e.getMessage());
                }
            }
        } else {
            if (callback != null) {
                callback.onConnectionError("Hotspot not supported on this Android version");
            }
        }
    }

    private void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (isRunning && !serverSocket.isClosed()) {
                    try {
                        Socket client = serverSocket.accept();
                        Log.d(TAG, "Client connected to hotspot: " + client.getInetAddress());
                        if (callback != null) {
                            callback.onClientConnected(client, "HOTSPOT:" + client.getInetAddress());
                        }
                    } catch (IOException e) {
                        if (isRunning) {
                            Log.e(TAG, "Error accepting connection on hotspot", e);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error starting server socket on hotspot", e);
            }
        }).start();
    }

    @Override
    public void stopListening() {
        isRunning = false;
        if (hotspotReservation != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hotspotReservation.close();
            }
            hotspotReservation = null;
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
