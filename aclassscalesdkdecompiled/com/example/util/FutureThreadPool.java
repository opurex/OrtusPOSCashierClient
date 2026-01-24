package com.example.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureThreadPool {
   private static ExecutorService d;
   private static volatile FutureThreadPool ALLATORIxDEMO;

   // $FF: synthetic method
   private FutureThreadPool() {
   }

   public FutureTask executeTask(Runnable arg0, Object arg1) {
      FutureTask var3 = new FutureTask(arg0, arg1);
      d.submit(var3);
      return var3;
   }

   public void executeTask(Runnable arg0) {
      d.execute(arg0);
   }

   public FutureTask executeTask(Callable arg0) {
      FutureTask var2 = new FutureTask(arg0);
      d.submit(var2);
      return var2;
   }

   public static String ALLATORIxDEMO(String arg0) {
      int var10000 = 1 << 3 ^ 4;
      int var10001 = 3 << 3 ^ 3 ^ 5;
      int var10002 = 3 << 3 ^ 2;
      int var10003 = arg0.length();
      char[] var10004 = new char[var10003];
      boolean var10006 = true;
      int var5 = var10003 - 1;
      var10003 = var10002;
      int var3;
      var10002 = var3 = var5;
      char[] var1 = var10004;
      int var4 = var10003;
      var10000 = var10002;

      for(int var2 = var10001; var10000 >= 0; var10000 = var3) {
         var10001 = var3;
         char var6 = arg0.charAt(var3);
         --var3;
         var1[var10001] = (char)(var6 ^ var2);
         if (var3 < 0) {
            break;
         }

         var10002 = var3--;
         var1[var10002] = (char)(arg0.charAt(var10002) ^ var4);
      }

      return new String(var1);
   }

   public static FutureThreadPool getInstance() {
      if (ALLATORIxDEMO == null) {
         Class var0 = FutureThreadPool.class;
         synchronized(FutureThreadPool.class) {
            ALLATORIxDEMO = new FutureThreadPool();
            d = Executors.newSingleThreadExecutor();
         }
      }

      return ALLATORIxDEMO;
   }
}
