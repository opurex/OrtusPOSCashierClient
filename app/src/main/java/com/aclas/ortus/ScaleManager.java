package com.aclas.ortus;

import android.content.Context;

import com.example.scaler.AclasScaler;

import java.util.ArrayList;
import java.util.List;

public final class ScaleManager {

    private static ScaleManager instance;

    private AclasScaler scaler;
    private AclasScaler.WeightInfoNew weight;

    private String mac = "";
    private String name = "";

    private final List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        void onWeight(AclasScaler.WeightInfoNew info);
        void onConnected(String mac, String name);
        void onDisconnected();
    }

    public static synchronized ScaleManager get(Context ctx) {
        if (instance == null) {
            instance = new ScaleManager(ctx.getApplicationContext());
        }
        return instance;
    }

    private ScaleManager(Context ctx) {
        scaler = new AclasScaler(AclasScaler.Type_FSC, ctx, scalerListener);
      //  scaler.setBluetoothListener(btListener);
       // scaler.setAclasPSXListener(psListener);
        scaler.setLog(true);
        weight = scaler.new WeightInfoNew();
    }

    public void connect(String mac, String name) {
        this.mac = mac;
        this.name = name;
        new Thread(() -> scaler.AclasConnect(mac)).start();
    }

    public void scan(boolean enable) {
        scaler.startScanBluetooth(enable);
    }

    public AclasScaler.WeightInfoNew getWeight() {
        return weight;
    }

    public void addListener(Listener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    /* --- vendor listeners --- */

    private final AclasScaler.AclasScalerListener scalerListener =
            new AclasScaler.AclasScalerListener() {
                @Override
                public void onConnected() {
                    for (Listener l : listeners)
                        l.onConnected(mac, name);
                }

                @Override
                public void onUpdateProcess(int i, int i1) {

                }

                @Override
                public void onDisConnected() {
                    for (Listener l : listeners)
                        l.onDisconnected();
                }

                @Override
                public void onRcvData(AclasScaler.WeightInfoNew info) {
                    weight.setData(info);
                    for (Listener l : listeners)
                        l.onWeight(weight);
                }

                @Override
                public void onError(int i, String s) {}
            };

//    private final AclasScaler.AclasBluetoothListener btListener =
//            s -> {
//        /* forwarded to UI */
//    };
//
//    private final AclasScaler.AclasScalerPSXListener psListener =
//            st -> {};
}
