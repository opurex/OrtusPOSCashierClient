package com.example.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AclasLogUtil {
   private static String f = "";
   private static LogThread d = null;
   private static final String ALLATORIxDEMO = "AclasLog";
   public static boolean PRINT = false;

   public static void logd(String arg0, String arg1) {
      if (PRINT) {
         Log.d(arg0, arg1);
         ALLATORIxDEMO(arg0, arg1);
      }

   }

   public static void info(String arg0, String arg1) {
      if (PRINT) {
         Log.i(arg0, arg1);
         ALLATORIxDEMO(arg0, arg1);
      }

   }

   // $FF: synthetic method
   private static String i(Context arg0) {
      return arg0.getApplicationContext().getFilesDir().getAbsolutePath();
   }

   // $FF: synthetic method
   private static boolean ALLATORIxDEMO() {
      return Environment.getExternalStorageState().equals(FutureThreadPool.ALLATORIxDEMO("suktj\u007fz"));
   }

   // $FF: synthetic method
   private static String ALLATORIxDEMO() {
      String var0 = PacketHandler.ALLATORIxDEMO("rs\u0000VW\u0001IH\u001a");
      SimpleDateFormat var1 = new SimpleDateFormat(var0, Locale.getDefault());
      String var2 = String.format(FutureThreadPool.ALLATORIxDEMO(";*-~>"), System.currentTimeMillis() % 1000L);
      return (new StringBuilder()).insert(0, var1.format(new Date())).append(var2).toString();
   }

   public static void StartThread() {
      if (f.length() > 0 && PRINT) {
         if (d == null) {
            Log.d("AclasLog", PacketHandler.ALLATORIxDEMO("iO[INoRI_Z^"));
            d = new LogThread(f);
         }

         if (!d.isAlive()) {
            d.StartThread();
            return;
         }
      } else {
         Log.d("AclasLog", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("Ij{lnJrl\u007f\u007f~>\u007flh>JLSPN$")).append(PRINT).append(PacketHandler.ALLATORIxDEMO("\u001b\\TV_\u0000")).append(f).toString());
      }

   }

   public static void loge(String arg0, String arg1) {
      Log.e(arg0, arg1);
      ALLATORIxDEMO(arg0, arg1);
   }

   // $FF: synthetic method
   private static void ALLATORIxDEMO(String arg0, String arg1) {
      try {
         if (d != null) {
            String var2 = (new StringBuilder()).insert(0, ALLATORIxDEMO()).append(arg0).append(FutureThreadPool.ALLATORIxDEMO(">")).append(arg1).append(PacketHandler.ALLATORIxDEMO("60")).toString();
            d.addString(var2);
            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void StopThread() {
      if (d != null) {
         Log.d("AclasLog", FutureThreadPool.ALLATORIxDEMO("IjunNvh{{z"));
         d.StopThread();
         d = null;
         Log.d("AclasLog", PacketHandler.ALLATORIxDEMO("iOUKnSH^[_\u001aUOWV"));
      }

   }

   public static void setSaveLog(Context arg0) {
      f = ALLATORIxDEMO(arg0);
      Log.i("AclasLog", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("m\u007fjI\u007fl{Vq}$")).append(f).toString());
   }

   // $FF: synthetic method
   private static String ALLATORIxDEMO(Context arg0) {
      StringBuilder var1 = new StringBuilder();
      if (ALLATORIxDEMO()) {
         var1.append(arg0.getExternalFilesDir(PacketHandler.ALLATORIxDEMO("{XVZI")));
      } else {
         var1.append(i(arg0)).append(File.separator).append(FutureThreadPool.ALLATORIxDEMO("_yr{m"));
      }

      File var2;
      if (!(var2 = new File(var1.toString())).exists()) {
         var2.mkdirs();
      }

      return var1.toString();
   }

   public static void error(String arg0, String arg1) {
      ALLATORIxDEMO(arg0, arg1);
      Log.e(arg0, arg1);
   }
}
