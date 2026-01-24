package com.feasycom.ble.sdk;

public interface DataReceivedCallback {
   void onReceiveBroadcastData(String var1);

   void onReceiveGattData(byte[] var1);
}
