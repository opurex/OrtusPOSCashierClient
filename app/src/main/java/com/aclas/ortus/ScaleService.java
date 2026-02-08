package com.aclas.ortus.scale;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.scaler.AclasScaler;
import com.example.data.St_PSData;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.scale.LogUtil;
import com.opurex.ortus.client.utils.scale.SharePrefenceUtil;

/**
 * Foreground service that owns the Bluetooth scale connection.
 * POS-safe: survives dialogs, screens, and UI lifecycle.
 */
public class ScaleService extends Service {

    public static final String ACTION_WEIGHT = "com.aclas.scale.WEIGHT";
    public static final String EXTRA_WEIGHT = "weight";
    public static final String EXTRA_UNIT = "unit";

    private static final String CHANNEL_ID = "scale_service_channel";
    private static final int NOTIF_ID = 9001;

    private final IBinder binder = new LocalBinder();

    private AclasScaler scaler;
    private AclasScaler.WeightInfoNew weight;

    private String mac = "";
    private String name = "";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /* ===================== SERVICE LIFECYCLE ===================== */

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.info("ScaleService created");

        createNotificationChannel();
        startForeground(NOTIF_ID, buildNotification());

        scaler = new AclasScaler(
                AclasScaler.Type_FSC,
                this,
                scalerListener
        );

        scaler.setAclasPSXListener(psListener);
        scaler.setLog(true);

        weight = scaler.new WeightInfoNew();

        // Auto-reconnect last known scale
        mac = SharePrefenceUtil.getAddress();
        name = SharePrefenceUtil.getName();

        if (mac != null && mac.contains(":")) {
            connect(mac, name);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String m = intent.getStringExtra("mac");
            String n = intent.getStringExtra("name");

            if (m != null && !m.isEmpty()) {
                connect(m, n);
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        LogUtil.info("ScaleService destroyed");
        if (scaler != null) {
            scaler.AclasDisconnect();
        }
        super.onDestroy();
    }

    /* ===================== PUBLIC API ===================== */

    public class LocalBinder extends Binder {
        public ScaleService getService() {
            return ScaleService.this;
        }
    }

    public void connect(String mac, String name) {
        this.mac = mac;
        this.name = name;

        SharePrefenceUtil.setAddress(mac);
        SharePrefenceUtil.setName(name);

        new Thread(() -> {
            LogUtil.info("Connecting to scale: " + mac);
            scaler.AclasConnect(mac);
        }).start();
    }

    public AclasScaler.WeightInfoNew getCurrentWeight() {
        return weight;
    }

    /* ===================== ACLAS LISTENERS ===================== */

    private final AclasScaler.AclasScalerListener scalerListener =
            new AclasScaler.AclasScalerListener() {

                @Override
                public void onConnected() {
                    LogUtil.info("Scale connected");
                }

                @Override
                public void onUpdateProcess(int i, int i1) {

                }

                @Override
                public void onDisConnected() {
                    LogUtil.error("Scale disconnected – retrying");

                    // Auto-reconnect after delay
                    mainHandler.postDelayed(() -> {
                        if (mac != null && mac.contains(":")) {
                            scaler.AclasConnect(mac);
                        }
                    }, 2000);
                }

                @Override
                public void onRcvData(AclasScaler.WeightInfoNew info) {
                    weight.setData(info);

                    Intent i = new Intent(ACTION_WEIGHT);
                    i.putExtra(EXTRA_WEIGHT, info.netWeight);
                    i.putExtra(EXTRA_UNIT, info.unit);
                    sendBroadcast(i);
                }

                @Override
                public void onError(int err, String msg) {
                    LogUtil.error("Scale error " + err + " : " + msg);
                }
            };

    private final AclasScaler.AclasScalerPSXListener psListener =
            new AclasScaler.AclasScalerPSXListener() {
                @Override
                public void onRcvData(St_PSData data) {
                    // Optional: price / total / key data
                }
            };

    /* ===================== NOTIFICATION ===================== */

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Scale Connected")
                .setContentText("POS weighing service running")
             //   .setSmallIcon(R.drawable.ic_scale)
                .setOngoing(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    "Scale Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(ch);
        }
    }
}
