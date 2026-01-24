package a.a.a;

import android.content.SharedPreferences;

public class a {
   public static volatile a a;
   public String b = "3.4.4";
   public SharedPreferences c = null;

   public static a a() {
      if (a == null) {
         Class var0 = a.class;
         synchronized(a.class){}

         Throwable var10000;
         boolean var10001;
         a var14;
         try {
            var14 = a;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            throw var10000;
         }

         if (var14 == null) {
            try {
               a = new a();
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               throw var10000;
            }
         }

         try {
            return a;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            throw var10000;
         }
      } else {
         return a;
      }
   }

   public void a(String var1) {
      SharedPreferences var10000;
      boolean var10001;
      try {
         var10000 = this.c;
      } catch (Exception var8) {
         var10001 = false;
         return;
      }

      SharedPreferences var2 = var10000;
      if (var10000 != null) {
         a var11;
         SharedPreferences.Editor var14;
         try {
            var11 = this;
            var14 = var2.edit();
         } catch (Exception var7) {
            var10001 = false;
            return;
         }

         SharedPreferences.Editor var9 = var14;

         int var12;
         try {
            var12 = var11.c.getInt(var1, 0);
         } catch (Exception var6) {
            var10001 = false;
            return;
         }

         int var10 = var12;
         if (var12 == 0) {
            try {
               var9.putInt(var1, 1);
            } catch (Exception var5) {
               var10001 = false;
               return;
            }
         } else {
            SharedPreferences.Editor var13 = var9;
            String var15 = var1;
            int var10002 = var10 + 1;

            try {
               var13.putInt(var15, var10002);
            } catch (Exception var4) {
               var10001 = false;
               return;
            }
         }

         try {
            var9.commit();
         } catch (Exception var3) {
            var10001 = false;
         }
      }

   }
}
