package com.opurex.ortus.client.network;

import java.net.Socket;

public interface ConnectionCallback {
    void onClientConnected(Socket clientSocket, String clientInfo);
    void onConnectionError(String error);
    void onStatusChanged(ConnectionStatus status);
}
