package com.feasycom.common.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import com.feasycom.ble.controler.FscBleCentralApiImp;

public class MsgLogger {
   @SuppressLint({"DefaultLocale"})
   private static String getTag() {
      StackTraceElement var0;
      String var10000 = (var0 = (new Throwable()).getStackTrace()[2]).getClassName();
      String var1 = var10000.substring(var10000.lastIndexOf(".") + 1);
      Object[] var2;
      Object[] var3 = var2 = new Object[3];
      var2[0] = var1;
      var2[1] = var0.getMethodName();
      var3[2] = var0.getLineNumber();
      String.format("%s.%s(L:%d)", var2);
      return "FeasyBlue";
   }

   public static void d(String var0) {
      if (FscBleCentralApiImp.isDebug) {
         Log.d(getTag(), var0);
      }

   }

   public static void d(String var0, String var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.d(var0, var1);
      }

   }

   public static void i(String var0) {
      if (FscBleCentralApiImp.isDebug) {
         Log.i(getTag(), var0);
      }

   }

   public static void i(String var0, String var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.i(var0, var1);
      }

   }

   public static void w(String var0) {
      if (FscBleCentralApiImp.isDebug) {
         Log.w(getTag(), var0);
      }

   }

   public static void w(String var0, String var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.w(var0, var1);
      }

   }

   public static void w(Throwable var0) {
      if (FscBleCentralApiImp.isDebug) {
         Log.w(getTag(), var0);
      }

   }

   public static void w(String var0, Throwable var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.w(var0, var1);
      }

   }

   public static void e(String var0) {
      if (FscBleCentralApiImp.isDebug) {
         Log.e(getTag(), var0);
      }

   }

   public static void e(String var0, String var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.e(var0, var1);
      }

   }

   public static void e(String var0, String var1, Throwable var2) {
      if (FscBleCentralApiImp.isDebug) {
         Log.e(var0, var1);
      }

   }

   public static void e(String var0, Throwable var1) {
      if (FscBleCentralApiImp.isDebug) {
         Log.e(var0, var0, var1);
      }

   }

   public static void e(Throwable var0) {
      if (FscBleCentralApiImp.isDebug) {
         e(getTag(), var0);
      }

   }
}
