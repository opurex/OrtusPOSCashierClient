package com.feasycom.logger;

import a.a.b.b;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogStrategy implements LogStrategy {
   @NonNull
   private final Handler handler;

   public DiskLogStrategy(@NonNull Handler var1) {
      this.handler = (Handler)b.a((Object)var1);
   }

   public void log(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      Handler var10000 = this.handler;
      var10000.sendMessage(var10000.obtainMessage(var1, var3));
   }

   public static class a extends Handler {
      @NonNull
      public final String a;
      public final int b;

      public a(@NonNull Looper var1, @NonNull String var2, int var3) {
         super((Looper)b.a((Object)var1));
         this.a = (String)b.a((Object)var2);
         this.b = var3;
      }

      public void handleMessage(@NonNull Message var1) {
         String var9 = (String)var1.obj;
         Object var2 = null;
         File var3 = this.a(this.a, "logs");

         FileWriter var4;
         label36: {
            FileWriter var10000;
            a var10002;
            FileWriter var10003;
            String var10004;
            FileWriter var10;
            label35: {
               label41: {
                  boolean var10001;
                  try {
                     var10000 = new FileWriter;
                  } catch (IOException var8) {
                     var10001 = false;
                     break label41;
                  }

                  var10 = var4 = var10000;

                  try {
                     var10002 = this;
                     var10003 = var4;
                     var10004 = var9;
                     var4.<init>(var3, true);
                     break label35;
                  } catch (IOException var7) {
                     var10001 = false;
                  }
               }

               var4 = (FileWriter)var2;
               break label36;
            }

            try {
               var10002.a(var10003, var10004);
               var10.flush();
               var10000.close();
               return;
            } catch (IOException var6) {
            }
         }

         if (var4 != null) {
            try {
               var4.flush();
               var4.close();
            } catch (IOException var5) {
            }
         }

      }

      public final void a(@NonNull FileWriter var1, @NonNull String var2) {
         b.a((Object)var1);
         b.a((Object)var2);
         var1.append(var2);
      }

      public final File a(@NonNull String var1, @NonNull String var2) {
         b.a((Object)var1);
         b.a((Object)var2);
         File var3;
         File var10000 = var3 = new File;
         var10000.<init>(var1);
         var10000.exists();
         int var7 = 0;
         File var4 = null;
         File var5;
         var10000 = var5 = new File;
         Object[] var6;
         Object[] var10001 = var6 = new Object[2];
         var6[0] = var2;
         var10001[1] = var7;
         var10000.<init>(var3, String.format("%s_%s.csv", var6));

         while(var5.exists()) {
            ++var7;
            var10000 = var4 = new File;
            var10001 = var6 = new Object[2];
            var6[0] = var2;
            var10001[1] = var7;
            var10000.<init>(var3, String.format("%s_%s.csv", var6));
            var10000 = var4;
            var4 = var5;
            var5 = var10000;
         }

         if (var4 != null) {
            return var4.length() >= (long)this.b ? var5 : var4;
         } else {
            return var5;
         }
      }
   }
}
