package com.example.util;

import android.util.Log;
import com.aclas.ndklib.BuildConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class LogThread extends Thread {
   private File D = null;
   private final int l = 4194304;
   private final String G = "AclasLog0.txt";
   private String H = "";
   private final String j = "AclasLog1.txt";
   private static final String h = "AclasLogThread";
   private String i = "AclasLog0.txt";
   private ArrayList f = new ArrayList();
   private boolean d = false;
   private File ALLATORIxDEMO = null;

   // $FF: synthetic method
   private String ALLATORIxDEMO() {
      String var1 = "";
      synchronized(this.f) {
         for(LogThread var10000 = this; !var10000.f.isEmpty(); var10000 = this) {
            var1 = (new StringBuilder()).insert(0, var1).append((String)this.f.remove(0)).toString();
         }

         return var1;
      }
   }

   public void StartThread() {
      this.d = true;
      super.start();
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      if (arg0.length() != 0) {
         try {
            if (this.ALLATORIxDEMO == null) {
               Log.d("AclasLogThread", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("\u0000y*r|")).append(this.H).append(FutureThreadPool.ALLATORIxDEMO(":}h{{j\u007f>|wv{:P{s\u007f$")).append(this.i).toString());
               this.ALLATORIxDEMO = new File(this.H, this.i);
            }

            if (!this.ALLATORIxDEMO.exists()) {
               this.ALLATORIxDEMO.createNewFile();
            }

            BufferedWriter var2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.ALLATORIxDEMO, true), BuildConfig.ALLATORIxDEMO("\u0013x/u)r#")));
            var2.append(arg0);
            var2.flush();
            var2.close();
            if (this.ALLATORIxDEMO.length() > 4194304L) {
               if (this.D == null) {
                  this.D = new File(this.H, "AclasLog1.txt");
               }

               this.ALLATORIxDEMO.renameTo(this.D);
               this.ALLATORIxDEMO = null;
               return;
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   public void run() {
      LogThread var10000 = this;
      super.run();
      Log.d("AclasLogThread", FutureThreadPool.ALLATORIxDEMO("Mn\u007fhj:jrl\u007f\u007f~>7373"));

      while(var10000.d) {
         try {
            Thread.sleep(100L);
            this.ALLATORIxDEMO(this.ALLATORIxDEMO());
         } catch (Exception var2) {
            Log.e("AclasLogThread", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("d3xfs>f|")).append(var2.toString()).toString());
            var10000 = this;
            continue;
         }

         var10000 = this;
      }

      Log.d("AclasLogThread", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("[bwn>nvh{{z:msd\u007f$")).append(this.ALLATORIxDEMO()).toString());
   }

   public LogThread(String arg0) {
      this.H = arg0;
      this.d = false;
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO() {
      boolean var1 = true;
      synchronized(this.f) {
         int var5 = this.f.size();
         return var5;
      }
   }

   public void addString(String arg0) {
      synchronized(this.f) {
         this.f.add(arg0);
      }
   }

   public void StopThread() {
      this.d = false;
      Log.d("AclasLogThread", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO(";kE2y6B.d#w\"65b'd2;kt\u0000z'q\u0014c(6 w*e#65\u007f<s|")).append(this.ALLATORIxDEMO()).toString());
      this.interrupt();

      try {
         this.join();
      } catch (Exception var2) {
         Log.e("AclasLogThread", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("37MnqjJrl\u007f\u007f~>_lh37$")).append(var2.toString()).toString());
      }

      Log.d("AclasLogThread", BuildConfig.ALLATORIxDEMO(";kE2y6B.d#w\"6#x\";k"));
   }
}
