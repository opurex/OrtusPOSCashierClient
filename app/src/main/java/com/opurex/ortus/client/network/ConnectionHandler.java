package com.opurex.ortus.client.network;

import android.content.Context;

public interface ConnectionHandler {
    void initialize(Context context, ConnectionCallback callback);
    void startListening(int port);
    void stopListening();
    boolean isConnected();
    void cleanup();
}
