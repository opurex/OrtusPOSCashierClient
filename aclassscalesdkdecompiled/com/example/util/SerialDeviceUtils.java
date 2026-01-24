package com.example.util;

import android.util.Log;
import java.io.File;
import java.util.Vector;

public class SerialDeviceUtils {
   private static final String f = "ro.mt.inner";
   private static final String[] d = new String[]{FutureThreadPool.ALLATORIxDEMO("5z\u007fh5jngOM_L"), PacketHandler.ALLATORIxDEMO("\u0015__M\u0015ONBohx"), FutureThreadPool.ALLATORIxDEMO("1~{l1njcM"), PacketHandler.ALLATORIxDEMO("\u0015__M\u0015ONBrhv"), FutureThreadPool.ALLATORIxDEMO("1~{l1njcS\\Z"), PacketHandler.ALLATORIxDEMO("\u0015__M\u0015ONB{xw"), FutureThreadPool.ALLATORIxDEMO("5z\u007fh5jngWJ")};
   private static final String ALLATORIxDEMO = "SerialDeviceManager";

   public static String[] getScaleSerialPathList() {
      Vector var0 = new Vector();
      File var1 = null;
      String var2;
      Vector var7;
      if ((var2 = getScaleSerialPath()) != null && var2.length() > 0) {
         var7 = var0;
         var0.add(var2);
      } else {
         int var3;
         for(int var10000 = var3 = 0; var10000 < d.length; var10000 = var3) {
            String var4 = d[var3];

            int var5;
            for(var10000 = var5 = 20; var10000 >= 0; var10000 = var5) {
               if ((var1 = new File((new StringBuilder()).insert(0, var4).append(String.valueOf(var5)).toString())).exists() && var1.canWrite()) {
                  String var6 = var1.getAbsolutePath();
                  Log.i("SerialDeviceManager", var6);
                  var0.add(var6);
               }

               --var5;
            }

            ++var3;
         }

         var7 = var0;
      }

      return (String[])var7.toArray(new String[var0.size()]);
   }

   public static String getScaleSerialPath() {
      String var0;
      if ((var0 = SystemProperties.getString("ro.mt.inner", (String)null)) != null) {
         Log.i("SerialDeviceManager", (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("]^NhYZV^i^HR[WjZNS\u001aIU\u0015WO\u0014RTU_I\u001a__]\u0000")).append(var0).toString());
         String[] var1 = var0.split(FutureThreadPool.ALLATORIxDEMO("8"));
         if (var1.length > 2 && var1[2].startsWith(d[0])) {
            return var1[2];
         }
      }

      return null;
   }
}
