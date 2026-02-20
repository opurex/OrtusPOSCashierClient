package com.opurex.ortus.client.network;

import android.content.Context;
import java.util.EnumMap;
import java.util.Map;

public class MultiConnectionManager {
    private final Context context;
    private final ConnectionCallback masterCallback;
    private final Map<ConnectionType, ConnectionHandler> connectors;

    public MultiConnectionManager(Context context, ConnectionCallback callback) {
        this.context = context;
        this.masterCallback = callback;
        this.connectors = new EnumMap<>(ConnectionType.class);

        connectors.put(ConnectionType.LAN_NSD, new LAN_NSDConnector());
        connectors.put(ConnectionType.LOCAL_HOTSPOT, new LocalHotspotConnector());
        connectors.put(ConnectionType.WIFI_DIRECT, new WiFiDirectConnector());
    }

    public void startAllConnections(int port) {
        for (Map.Entry<ConnectionType, ConnectionHandler> entry : connectors.entrySet()) {
            ConnectionHandler connector = entry.getValue();
            connector.initialize(context, masterCallback);
            connector.startListening(port);
        }
    }

    public void stopAllConnections() {
        for (ConnectionHandler connector : connectors.values()) {
            connector.stopListening();
            connector.cleanup();
        }
    }

    public void cleanup() {
        stopAllConnections();
    }
}
