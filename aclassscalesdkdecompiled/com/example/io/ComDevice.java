package com.example.io;

import android.content.Context;

public interface ComDevice {
   int Type_FSC = 7;
   int WiFi = 4;
   int BlueTooth = 3;
   int USB = 1;
   int Wireless = 5;
   int Type_BLE = 6;
   int Ethernet = 2;
   int SerialPort = 0;

   void setLog(boolean var1);

   int close();

   int init(Context var1, ComDeviceListener var2);

   int open(String var1);

   int writeData(byte[] var1);

   int readData(byte[] var1, int var2, int var3);

   boolean isReady();

   int type();

   public interface ComDeviceListener {
      void onConnecting();

      void onRecv(int var1, String var2);

      void onDisconnect();

      void onConnectFinished(int var1);
   }
}
