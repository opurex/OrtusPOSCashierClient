package com.example.util;

public class SystemProperties {
   private static final String ALLATORIxDEMO = "android.os.SystemProperties";

   public static void setString(String arg0, String arg1) {
      ALLATORIxDEMO(arg0, arg1);
   }

   // $FF: synthetic method
   private static void ALLATORIxDEMO(String arg0, String arg1) {
      try {
         Class var2;
         (var2 = Class.forName("android.os.SystemProperties")).getDeclaredMethod(FutureThreadPool.ALLATORIxDEMO("m\u007fj"), String.class, String.class).invoke(var2, arg0, arg1);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public static String getString(String arg0, String arg1) {
      return ALLATORIxDEMO(arg0, arg1);
   }

   public static long getLong(String arg0, long arg1) {
      String var5 = getString(arg0, String.valueOf(arg1));

      try {
         long var3 = Long.parseLong(var5);
         return var3;
      } catch (NumberFormatException var7) {
         var7.printStackTrace();
         return arg1;
      }
   }

   public static void setBoolean(String arg0, boolean arg1) {
      ALLATORIxDEMO(arg0, String.valueOf(arg1));
   }

   public static int getInt(String arg0, int arg1) {
      String var3 = getString(arg0, String.valueOf(arg1));

      try {
         int var2 = Integer.parseInt(var3);
         return var2;
      } catch (NumberFormatException var5) {
         var5.printStackTrace();
         return arg1;
      }
   }

   public static String getString(String arg0) {
      return getString(arg0, "");
   }

   public static void setLong(String arg0, long arg1) {
      ALLATORIxDEMO(arg0, String.valueOf(arg1));
   }

   public static boolean getBoolean(String arg0, boolean arg1) {
      String var3 = getString(arg0, String.valueOf(arg1));

      try {
         boolean var2 = Boolean.parseBoolean(var3);
         return var2;
      } catch (NumberFormatException var5) {
         var5.printStackTrace();
         return arg1;
      }
   }

   // $FF: synthetic method
   private static String ALLATORIxDEMO(String arg0, String arg1) {
      Object var2 = null;

      Object var10000;
      try {
         Class var3;
         var2 = (var3 = Class.forName("android.os.SystemProperties")).getDeclaredMethod(Packet.ALLATORIxDEMO("\u0012s\u0001"), String.class, String.class).invoke(var3, arg0, arg1);
      } catch (Exception var4) {
         var10000 = var2;
         var4.printStackTrace();
         return var10000 != null && var2 instanceof String ? (String)var2 : "";
      }

      var10000 = var2;
      return var10000 != null && var2 instanceof String ? (String)var2 : "";
   }

   public static void setInt(String arg0, int arg1) {
      ALLATORIxDEMO(arg0, String.valueOf(arg1));
   }
}
