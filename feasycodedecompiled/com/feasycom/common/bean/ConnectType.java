package com.feasycom.common.bean;

import androidx.annotation.Keep;

@Keep
public enum ConnectType {
   CONNECT,
   MODIFY,
   OTA;

   static {
      ConnectType var0;
      ConnectType var10000 = var0 = new ConnectType;
      var10000.<init>();
      CONNECT = var10000;
      ConnectType var1;
      var10000 = var1 = new ConnectType;
      var10000.<init>();
      MODIFY = var10000;
      ConnectType var2;
      var10000 = var2 = new ConnectType;
      var10000.<init>();
      OTA = var10000;
   }
}
