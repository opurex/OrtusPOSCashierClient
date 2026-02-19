package com.opurex.ortus.client.network;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LAN_NSDConnector implements ConnectionHandler {
    private static final String TAG = "LAN_NSDConnector";
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;
    private ServerSocket serverSocket;
    private int localPort;
    private ConnectionCallback callback;
    private boolean isRunning = false;

    @Override
    public void initialize(Context context, ConnectionCallback callback) {
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.callback = callback;
        initializeRegistrationListener();
    }

    @Override
    public void startListening(int port) {
        try {
            serverSocket = new ServerSocket(port);
            localPort = serverSocket.getLocalPort();
            isRunning = true;

            NsdServiceInfo serviceInfo = new NsdServiceInfo();
            serviceInfo.setServiceName("OrtusPOS_Master");
            serviceInfo.setServiceType("_ortuspos._tcp");
            serviceInfo.setPort(localPort);

            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);

            new Thread(this::acceptConnections).start();
        } catch (IOException e) {
            Log.e(TAG, "Error starting server socket", e);
            if (callback != null) {
                callback.onConnectionError("LAN_NSD Socket error: " + e.getMessage());
            }
        }
    }

    @Override
    public void stopListening() {
        isRunning = false;
        if (nsdManager != null && registrationListener != null) {
            try {
                nsdManager.unregisterService(registrationListener);
            } catch (Exception e) {
                Log.e(TAG, "Error unregistering service", e);
            }
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

    private void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Service registered: " + serviceInfo.getServiceName());
                if (callback != null) {
                    callback.onStatusChanged(ConnectionStatus.CONNECTED);
                }
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Registration failed: " + errorCode);
                if (callback != null) {
                    callback.onConnectionError("NSD Registration failed: " + errorCode);
                }
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Service unregistered: " + serviceInfo.getServiceName());
                if (callback != null) {
                    callback.onStatusChanged(ConnectionStatus.DISCONNECTED);
                }
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Unregistration failed: " + errorCode);
            }
        };
    }

    private void acceptConnections() {
        while (isRunning && serverSocket != null && !serverSocket.isClosed()) {
            try {
                Socket client = serverSocket.accept();
                Log.d(TAG, "Client connected: " + client.getInetAddress());
                if (callback != null) {
                    callback.onClientConnected(client, "LAN-NSD:" + client.getInetAddress());
                }
            } catch (IOException e) {
                if (isRunning) {
                    Log.e(TAG, "Error accepting connection", e);
                }
            }
        }
    }
}
