package com.example.io;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.Build.VERSION;
import com.example.scaler.AclasScaler;
import com.example.util.AclasLogUtil;
import com.example.util.FutureThreadPool;
import com.example.util.Packet;
import com.example.util.PacketHandler;
import com.feasycom.ble.sdk.BindCallback;
import com.feasycom.ble.sdk.BindStatusCallback;
import com.feasycom.ble.sdk.DataReceivedCallback;
import com.feasycom.ble.sdk.FBlueSDK;
import com.feasycom.ble.sdk.ScanResultCallback;
import com.feasycom.common.bean.FscDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FscBlueTooth implements ComDevice {
   private byte[] N = new byte[512];
   private int k = 0;
   private int C = -1;
   private FBlueSDK c = null;
   private final String g = "AclasFscBt";
   private ComDevice.ComDeviceListener e;
   private boolean I = false;
   private Integer E = -1;
   private boolean M = false;
   private final int A = 110;
   private boolean a = false;
   private ArrayList K = new ArrayList();
   private ArrayList J = new ArrayList();
   private SharedPreferences F = null;
   private Context m;
   private boolean b = false;
   private BroadcastReceiver B = new BroadcastReceiver() {
      public void onReceive(Context arg0, Intent arg1) {
         FscBlueTooth.this.ALLATORIxDEMO("AclasFscBt", BluetoothCtrl.ALLATORIxDEMO("H6g8p0k7T+k/m=a+G1e7c<`\u000ba:a0r<vy)t)t)t)"));
      }
   };
   private DataReceivedCallback L = new DataReceivedCallback() {
      public void onReceiveGattData(byte[] arg0) {
         if (arg0 != null) {
            if (!FscBlueTooth.this.I) {
               FscBlueTooth.this.I = true;
               FscBlueTooth.this.i();
            }

            FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(")t)6j\u000ba:a0r<C8p-@8p8)t)5a7>")).append(arg0.length).append(BluetoothCtrl.ALLATORIxDEMO("y@8p8>")).append(FscBlueTooth.this.ALLATORIxDEMO(arg0, arg0.length)).toString());
            FscBlueTooth.this.i(arg0);
         } else {
            FscBlueTooth.this.ALLATORIxDEMO(BluetoothCtrl.ALLATORIxDEMO("6j\u000ba:a0r<C8p-@8p8$7q5h"));
         }
      }

      public void onReceiveBroadcastData(String arg0) {
         if (arg0 != null) {
            if (FscBlueTooth.this.I) {
               FscBlueTooth.this.I = false;
            }

            byte[] var2 = FscBlueTooth.this.hexStringToByteArray(arg0.replace(BluetoothCtrl.ALLATORIxDEMO("$"), ""));
            FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("t)tk7V<g<m/a\u001bv6e=g8w-@8p8)t)5a7>")).append(var2.length).append(BluetoothCtrl.ALLATORIxDEMO("y@8p8>")).append(arg0).toString());
            FscBlueTooth.this.i(var2);
         } else {
            FscBlueTooth.this.ALLATORIxDEMO(BluetoothCtrl.ALLATORIxDEMO("6j\u000ba:a0r<F+k8`:e*p\u001de-eyj,h5%x%"));
         }
      }
   };
   private PacketHandler D = new PacketHandler();
   private ScanResultCallback l = new ScanResultCallback() {
      public void scannerResult(BluetoothDevice arg0, FscDevice arg1, byte[] arg2) {
         <undefinedtype> var10000;
         int var4;
         if ((var4 = FscBlueTooth.this.K.indexOf(arg0)) == -1) {
            var10000 = this;
            FscBlueTooth.this.K.add(arg0);
         } else {
            var10000 = this;
            FscBlueTooth.this.K.remove(var4);
            FscBlueTooth.this.K.add(var4, arg0);
         }

         FscBlueTooth.this.ALLATORIxDEMO(arg1);
      }
   };
   private BindStatusCallback G = new BindStatusCallback() {
      public void onBindStatus(boolean arg0) {
         FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(")t)6j\u001bm7`\np8p,wt)tm*Q7f0j=>")).append(arg0).toString());
         if (arg0) {
            if (!FscBlueTooth.this.ALLATORIxDEMO && FscBlueTooth.this.i() == 1 && FscBlueTooth.this.e != null) {
               FscBlueTooth.this.e.onDisconnect();
            }

            FscBlueTooth.this.ALLATORIxDEMO(0);
            FscBlueTooth.this.b = false;
         }

      }
   };
   private String H = "";
   private byte[] j = new byte[512];
   private BroadcastReceiver h = new BroadcastReceiver() {
      public void onReceive(Context arg0, Intent arg1) {
         int var3 = arg1.getIntExtra(BluetoothCtrl.ALLATORIxDEMO("e7`+k0`wf5q<p6k-lwe=e)p<vwa!p+ewW\rE\rA"), 10);
         int var4 = arg1.getIntExtra(BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*;h,a-k6p1*8`8t-a+*<|-v8*\tV\u001cR\u0010K\fW\u0006W\rE\rA"), 10);
         FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("F5q<p6k-l\np8p<>")).append(var3).append(BluetoothCtrl.ALLATORIxDEMO("$)v<r0k,w\np8p<>")).append(var4).toString());
      }
   };
   private final int i = 400;
   public boolean bLogFalg = true;
   private ArrayList f = new ArrayList();
   private BindCallback d = new BindCallback() {
      public void onBindResult(boolean arg0) {
         FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(")t)6j\u001bm7`\u000ba*q5pt)t>")).append(arg0).toString());
         if (FscBlueTooth.this.e != null) {
            if (arg0) {
               if (!FscBlueTooth.this.ALLATORIxDEMO) {
                  FscBlueTooth.this.e.onConnectFinished(2);
               }

               FscBlueTooth.this.ALLATORIxDEMO(1);
               return;
            }

            FscBlueTooth.this.ALLATORIxDEMO(0);
         }

      }
   };
   private boolean ALLATORIxDEMO = false;

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(String arg0) {
      boolean var2 = false;
      String var3 = this.c.currentBoundDevice();
      if (arg0 != null && var3 != null && !var3.isEmpty()) {
         var2 = arg0.isEmpty();
      }

      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001af\u0010xUu\u001ds\u0016}7\u007f\u001brU~\u0014eUT\u001ac\u001br1s\u0003\u007f\u0016sO")).append(var3).append(Packet.ALLATORIxDEMO("U;X\u007f\u001b[\u0014uO")).append(arg0).append(Packet.ALLATORIxDEMO("Ud\u0010bO")).append(var2).toString());
      return var2;
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(String arg0) {
      return Integer.valueOf(arg0, 16);
   }

   public byte[] hexStringToByteArray(String arg0) {
      byte[] var2 = null;
      if (!arg0.isEmpty() && arg0.length() > 1) {
         var2 = new byte[arg0.length() / 2];

         int var4;
         for(int var10000 = var4 = 0; var10000 < arg0.length() / 2; var10000 = var4) {
            String var5 = arg0.substring(var4 * 2, var4 * 2 + 2);

            try {
               int var6 = Integer.valueOf(var5, 16);
               var2[var4] = (byte)var6;
            } catch (Exception var7) {
               var7.printStackTrace();
            }

            ++var4;
         }
      }

      return var2;
   }

   public int readData(byte[] arg0, int arg1, int arg2) {
      return this.ALLATORIxDEMO(arg0, arg1);
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, int arg1) {
      if (this.F != null) {
         this.F.edit().putInt(arg0, arg1).commit();
      }

   }

   // $FF: synthetic method
   private void i(String arg0, String arg1) {
      if (this.F != null) {
         this.F.edit().putString(arg0, arg1).commit();
      }

   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(final ArrayList arg0, final FscDevice arg1, final boolean arg2) {
      int var4 = -1;
      FutureTask var5 = FutureThreadPool.getInstance().executeTask(new Callable() {
         public Boolean ALLATORIxDEMO() throws Exception {
            FscBlueTooth.this.ALLATORIxDEMO(-1);
            boolean var1 = false;
            boolean var10000;
            if (arg2) {
               FscBlueTooth.this.ALLATORIxDEMO(1);
               var10000 = var1 = true;
               FscBlueTooth.this.i((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("XUUT^YO|NNNH^\u001aYSU^\u007f_MSX_\u001bRZI\u001bXRT___\u0000")).append(FscBlueTooth.this.H).toString());
            } else {
               FscBlueTooth.this.i((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(":k7j<g-B,p,v<$;a?k+ayf0j=@<r0g<>")).append(FscBlueTooth.this.H).toString());
               FscBlueTooth.this.c.bindDevice(arg0, arg1, FscBlueTooth.this.d);
               int var2 = 400;
               FscBlueTooth.this.i((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("YTTU_XN}OOOI_\u001bIO[IN\u001bYTTU\u0000")).append(var1).append(BluetoothCtrl.ALLATORIxDEMO("$\u0014E\u0001>")).append(400).append(PacketHandler.ALLATORIxDEMO("\u0016\u0017")).append(var2).append(BluetoothCtrl.ALLATORIxDEMO("$*h<a)>")).append(110).toString());
               int var3 = -2;
               int var6 = var2;

               <undefinedtype> var7;
               label34: {
                  while(true) {
                     --var2;
                     if (var6 <= 0) {
                        break;
                     }

                     var1 = (var3 = FscBlueTooth.this.i()) == 1;
                     if (var3 >= 0) {
                        if (var3 == 1) {
                           var7 = this;
                           FscBlueTooth.this.ALLATORIxDEMO();
                           break label34;
                        }
                        break;
                     }

                     try {
                        Thread.sleep(110L);
                     } catch (Exception var5) {
                        var6 = var2;
                        continue;
                     }

                     var6 = var2;
                  }

                  var7 = this;
               }

               FscBlueTooth.this.i((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("YTTU_XN}OOOI_\u001b_U^\u001bYTTU\u0000")).append(var3).append(BluetoothCtrl.ALLATORIxDEMO("$\u0014E\u0001>")).append(400).append(PacketHandler.ALLATORIxDEMO("\u0016\u0017")).append(var2).append(BluetoothCtrl.ALLATORIxDEMO("$*h<a)>")).append(110).toString());
               var10000 = var1;
            }

            return var10000;
         }
      });

      FscBlueTooth var10000;
      label22: {
         try {
            boolean var6 = (Boolean)var5.get();
            this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0017bUu\u001ax\u001bs\u0016b3c\u0001c\u0007sU;X;X")).append(var6).toString());
            var4 = var6 ? 0 : -1;
         } catch (Exception var7) {
            var10000 = this;
            break label22;
         }

         var10000 = this;
      }

      if (var10000.e != null) {
         this.e.onConnectFinished(var4);
      }

      return var4;
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO() {
      byte[] var1 = null;
      synchronized(this.N) {
         if (this.C > 0) {
            var1 = new byte[this.C];
            System.arraycopy(this.N, 0, var1, 0, this.C);
            this.C = 0;
         }

         return var1;
      }
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(FscDevice arg0) {
      if (arg0.rssi != 127) {
         String var2 = (arg0.name != null && !arg0.name.isEmpty() ? arg0.name : Packet.ALLATORIxDEMO(" x\u001ex\u001aa\u001b")) + Packet.ALLATORIxDEMO("Y") + arg0.address;
         ArrayList var3;
         synchronized(var3 = this.J) {
            ArrayList var10000;
            int var4;
            if ((var4 = this.J.indexOf(arg0)) == -1) {
               var10000 = var3;
               this.J.add(arg0);
               this.e.onRecv(0, var2);
               this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("p\u001ac\u001br1s\u0003\u007f\u0016sO")).append(var2).append(Packet.ALLATORIxDEMO("6&\u007f\u000fsO")).append(this.J.size()).toString());
            } else {
               this.J.remove(var4);
               this.J.add(var4, arg0);
               this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001dw\u0006p\u001ac\u001br1s\u0003\u007f\u0016sO")).append(var2).append(Packet.ALLATORIxDEMO("6&\u007f\u000fsO")).append(this.J.size()).toString());
               var10000 = var3;
            }

         }
      }
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO(byte[] arg0) {
      Object var2 = null;
      if (arg0 != null && arg0.length > 2) {
         if (arg0[arg0.length - 1] == 10 && arg0[arg0.length - 2] == 13) {
            byte[] var3 = new byte[arg0.length - 2];
            System.arraycopy(arg0, 0, var3, 0, var3.length);
            return var3;
         } else {
            return arg0;
         }
      } else {
         return arg0;
      }
   }

   public boolean isReady() {
      return this.i() == 1;
   }

   // $FF: synthetic method
   private String i(String arg0) {
      StringBuilder var2 = new StringBuilder();

      int var3;
      for(int var10000 = var3 = arg0.length() - 2; var10000 >= 0; var10000 = var3) {
         int var10002 = var3;
         int var10003 = var3;
         var3 -= 2;
         var2.append(arg0, var10002, var10003 + 2);
      }

      return var2.toString();
   }

   public String getFirmwareVersion() {
      String var1 = "";
      if (this.c != null) {
         var1 = this.c.firmwareVersion();
      }

      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0012s\u0001P\u001cd\u0018a\u0014d\u0010@\u0010d\u0006\u007f\u001axO")).append(var1).toString());
      return var1;
   }

   // $FF: synthetic method
   private FscDevice ALLATORIxDEMO(String arg0) {
      FscDevice var2 = null;
      FscBlueTooth var8;
      if (!arg0.isEmpty()) {
         int var4;
         synchronized(this.J) {
            for(int var10000 = var4 = 0; var10000 < this.J.size(); var10000 = var4) {
               FscDevice var5 = (FscDevice)this.J.get(var4);
               if (arg0.equalsIgnoreCase(var5.address)) {
                  var2 = var5;
               }

               ++var4;
            }
         }

         var8 = this;
      } else {
         var8 = this;
      }

      var8.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0012s\u0001R\u0010`\u001cu\u00106\u0018w\u00166<xO")).append(arg0).append(Packet.ALLATORIxDEMO("63\u007f\u001brUR\u0010`O")).append(var2 != null).toString());
      return var2;
   }

   // $FF: synthetic method
   private void i(String arg0) {
      this.ALLATORIxDEMO("AclasFscBt", arg0);
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(int arg0) {
      synchronized(this.E) {
         this.E = arg0;
      }
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(String arg0) {
      String var2 = "";
      int var3;
      if (arg0 != null && arg0.length() == 12) {
         for(int var10000 = var3 = 0; var10000 < 12; var10000 = var3) {
            String var4 = arg0.substring(var3, var3 + 2);
            var2 = (new StringBuilder()).insert(0, var2).append(var4.toUpperCase()).toString();
            if (var3 < 10) {
               var2 = (new StringBuilder()).insert(0, var2).append(Packet.ALLATORIxDEMO("O")).toString();
            }

            var3 += 2;
         }
      }

      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("p\u001ad\u0018w\u0001[\u0014u4r\u0011d\u0010e\u00066\u001cxO")).append(arg0).append(Packet.ALLATORIxDEMO("6\u0007s\u0001c\u0007xO")).append(var2).toString());
      return var2;
   }

   // $FF: synthetic method
   private void F() {
      this.i(Packet.ALLATORIxDEMO("c\u001bd\u0010q\u001ce\u0001s\u0007T\u0007y\u0014r\u0016w\u0006b's\u0016s\u001c`\u0010d\u0006"));
      this.m.unregisterReceiver(this.h);
      if (this.ALLATORIxDEMO()) {
         this.m.unregisterReceiver(this.B);
      }

   }

   // $FF: synthetic method
   private void i(byte[] arg0) {
      byte[] var2;
      if ((var2 = this.ALLATORIxDEMO(arg0)) != null && var2.length > 0) {
         synchronized(this.j) {
            if (this.k + var2.length < this.j.length) {
               System.arraycopy(var2, 0, this.j, this.k, var2.length);
               this.k += var2.length;
            }

         }
      }
   }

   public int getDevList(List arg0) {
      boolean var2 = false;
      arg0.clear();
      if (!this.J.isEmpty()) {
         Iterator var3;
         Iterator var10000 = var3 = this.J.iterator();

         while(var10000.hasNext()) {
            FscDevice var4 = (FscDevice)var3.next();
            String var5 = (var4.name != null && !var4.name.isEmpty() ? var4.name : Packet.ALLATORIxDEMO(" x\u001ex\u001aa\u001b")) + Packet.ALLATORIxDEMO("Y") + var4.address;
            var10000 = var3;
            this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0012s\u0001R\u0010`9\u007f\u0006bO")).append(var5).toString());
            arg0.add(var5);
         }
      } else {
         this.ALLATORIxDEMO("AclasFscBt", Packet.ALLATORIxDEMO("q\u0010b1s\u0003Z\u001ce\u00016\u0010{\u0005b\f"));
      }

      return arg0.size();
   }

   public void scanDevices(boolean arg0) {
      if (!this.i()) {
         this.i(Packet.ALLATORIxDEMO("e\u0016w\u001bR\u0010`\u001cu\u0010eUs\u0007d\u001adUt\u00016\u0011\u007f\u0006w\u0017z\u00107T"));
      } else {
         this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0006u\u0014x1s\u0003\u007f\u0016s\u00066\u0017T\u0010q\u001cx&U\u0014xO")).append(arg0).append(Packet.ALLATORIxDEMO("6\u0018I\u0017P\u0019w\u0012E\u0016w\u001b\u007f\u001bqO")).append(this.M).toString());
         if (arg0 && !this.M) {
            this.M = true;
            this.f.clear();
            this.J.clear();
            this.K.clear();
            this.i(Packet.ALLATORIxDEMO("\u0006u\u0014x1s\u0003\u007f\u0016s\u00066\u0006b\u0014d\u0001E\u0016w\u001b"));
            this.c.disconnect();
            this.a = false;
            this.c.startScanning(this.l, (DataReceivedCallback)null, (BindStatusCallback)null);
         } else {
            if (!arg0) {
               this.i(Packet.ALLATORIxDEMO("e\u0016w\u001bR\u0010`\u001cu\u0010eUe\u0001y\u0005E\u0016w\u001b"));
               if (this.M) {
                  this.M = false;
                  return;
               }
            } else {
               this.i(Packet.ALLATORIxDEMO("e\u0016w\u001bR\u0010`\u001cu\u0010eU;X;\u0006u\u0014x\u001b\u007f\u001bqX;X"));
            }

         }
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1) {
      int var3 = 0;
      synchronized(this.j) {
         if (this.k > 0) {
            var3 = Math.min(this.k, arg1);
            System.arraycopy(this.j, 0, arg0, 0, var3);
            this.k = 0;
         }

         return var3;
      }
   }

   public int close() {
      this.i(Packet.ALLATORIxDEMO("\u0016z\u001ae\u0010"));
      int var1 = this.i();
      this.ALLATORIxDEMO(-1);
      this.ALLATORIxDEMO = true;
      this.scanDevices(false);
      this.c.disconnect();
      this.i(Packet.ALLATORIxDEMO("\u0016z\u001ae\u00106\u0010x\u0011"));
      if (this.e != null && var1 == 1) {
         this.e.onDisconnect();
      }

      return 0;
   }

   // $FF: synthetic method
   private int i() {
      boolean var1 = true;
      synchronized(this.E) {
         int var5 = this.E;
         return var5;
      }
   }

   // $FF: synthetic method
   private void h() {
      this.i(Packet.ALLATORIxDEMO("d\u0010q\u001ce\u0001s\u0007T\u0007y\u0014r\u0016w\u0006b's\u0016s\u001c`\u0010d\u0006"));
      this.m.registerReceiver(this.h, new IntentFilter(Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[t\u0019c\u0010b\u001ay\u0001~[w\u0011w\u0005b\u0010d[w\u0016b\u001cy\u001b8&B4B0I6^4X2S1")));
      if (this.ALLATORIxDEMO()) {
         this.m.registerReceiver(this.B, new IntentFilter(Packet.ALLATORIxDEMO("\u0014x\u0011d\u001a\u007f\u00118\u0019y\u0016w\u0001\u007f\u001ax[[:R0I6^4X2S1")));
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      if (this.bLogFalg) {
         AclasLogUtil.error("AclasFscBt", arg0);
      }

   }

   // $FF: synthetic method
   private boolean i() {
      return BluetoothAdapter.getDefaultAdapter().isEnabled();
   }

   public void setLog(boolean arg0) {
      this.bLogFalg = arg0;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, String arg1) {
      if (this.bLogFalg) {
         AclasLogUtil.info(arg0, arg1);
      }

   }

   // $FF: synthetic method
   private void i() {
      if (this.ALLATORIxDEMO() > 0) {
         byte[] var1 = this.ALLATORIxDEMO();
         boolean var2 = this.c.sendData(var1);
         this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0006s\u001br2w\u0001bO")).append(var2).append(Packet.ALLATORIxDEMO("Uz\u0010xO")).append(var1.length).append(Packet.ALLATORIxDEMO("6\u0011w\u0001wO")).append(this.ALLATORIxDEMO(var1, var1.length)).toString());
      }

   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(String arg0, String arg1) {
      String var3 = "";
      if (this.F != null) {
         var3 = this.F.getString(arg0, arg1);
      }

      return var3;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(Context arg0) {
      if (arg0 != null) {
         this.F = arg0.getSharedPreferences("AclasFscBt", 0);
      }

   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(String arg0, int arg1) {
      int var3 = -1;
      if (this.F != null) {
         var3 = this.F.getInt(arg0, arg1);
      }

      return var3;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO() {
      return VERSION.SDK_INT >= 23;
   }

   public int open(String arg0) {
      FscBlueTooth var10000;
      label47: {
         boolean var2 = true;
         if (arg0 != null && arg0.length() > 0) {
            if (arg0.contains(Packet.ALLATORIxDEMO("Y"))) {
               String[] var3 = arg0.split(Packet.ALLATORIxDEMO("Y"));
               var10000 = this;
               this.H = var3[1];
               break label47;
            }

            if (arg0.contains(Packet.ALLATORIxDEMO("O"))) {
               var10000 = this;
               this.H = arg0;
               break label47;
            }
         } else if (arg0 == null || arg0.isEmpty()) {
            this.H = "";
            this.i(Packet.ALLATORIxDEMO("y\u0005s\u001b6\u0005w\u0007w\u0018s\u0001s\u00076\u0018w\u00166\u0010{\u0005b\f"));
         }

         var10000 = this;
      }

      var10000.ALLATORIxDEMO = false;
      FscDevice var8 = this.ALLATORIxDEMO(this.H);
      boolean var4 = this.ALLATORIxDEMO(this.H);
      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001af\u0010xU{\u0014uO")).append(this.H).append(Packet.ALLATORIxDEMO("Uu\u001ds\u0016}7\u007f\u001brO")).append(var4).toString());
      if (var8 == null && !var4) {
         this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("\u001af\u0010xUq\u0010b1s\u0003\u007f\u0016sUx\u0000z\u0019"));
         return -1;
      } else {
         int var7;
         if ((var7 = this.ALLATORIxDEMO(this.K, var8, var4)) == 0) {
            String var5 = this.c.currentBoundDevice();
            String var6 = this.c.firmwareVersion();
            this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("y\u0005s\u001b6\u0016c\u0007d\u0010x\u0001T\u001ac\u001br1s\u0003\u007f\u0016sO")).append(var5).append(Packet.ALLATORIxDEMO("63AO")).append(var6).toString());
            if (var5 != null && !var5.isEmpty()) {
               this.a = true;
               this.c.startScanning((ScanResultCallback)null, this.L, this.G);
            }
         }

         return var7;
      }
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO() {
      if (((PowerManager)this.m.getSystemService(Packet.ALLATORIxDEMO("\u0005y\u0002s\u0007"))).isIgnoringBatteryOptimizations(this.m.getPackageName())) {
         this.i(Packet.ALLATORIxDEMO("e\u0010b%y\u0002s\u0007[\u0014x\u0014q\u0010dU庂畝巤纺快甐産氕伎卣"));
      } else {
         this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("廡甾泔期很畳畀汶佭匀U;X;X"));
      }
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(byte[] arg0, int arg1) {
      return AclasScaler.bytesToHexString(arg0, 0, arg1, false);
   }

   public int init(Context arg0, ComDevice.ComDeviceListener arg1) {
      boolean var10009 = this.M = false;
      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u007f\u001b\u007f\u00016\u0018I\u0017P\u0019w\u0012E\u0016w\u001b\u007f\u001bqO")).append(this.M).toString());
      this.c = FBlueSDK.getInstance();
      this.a = var10009;
      this.e = arg1;
      this.m = arg0;
      this.ALLATORIxDEMO(arg0);
      this.c.init(this.m);
      boolean var3 = this.i();
      this.i((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u007f\u001b\u007f\u00016\u0018I\u0017P\u0019w\u0012E\u0016w\u001b\u007f\u001bqO")).append(this.M).append(Packet.ALLATORIxDEMO("Ud\u0010bO")).append(var3).toString());
      return var3 ? 0 : -1;
   }

   public List splitHexStringToInt(String arg0) {
      ArrayList var2 = new ArrayList();
      char[] var3;
      int var4 = (var3 = arg0.toCharArray()).length;

      int var5;
      for(int var10000 = var5 = 0; var10000 < var4; var10000 = var5) {
         int var7;
         if ((var7 = Character.digit(var3[var5], 16)) != -1) {
            var2.add(var7);
         }

         ++var5;
      }

      return var2;
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO() {
      boolean var1 = true;
      synchronized(this.N) {
         int var5 = this.C;
         return var5;
      }
   }

   public int type() {
      return 7;
   }

   public int writeData(byte[] arg0) {
      int var2 = -1;
      if (this.E == 1) {
         if (this.c.sendData(arg0)) {
            return var2 = arg0.length;
         }

         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0002d\u001cb\u0010R\u0014b\u001460d\u000762w\u0001bO")).append(this.I).append(Packet.ALLATORIxDEMO("Uz\u0010xO")).append(arg0.length).append(Packet.ALLATORIxDEMO("6\u0011w\u0001wO")).append(this.ALLATORIxDEMO(arg0, arg0.length)).toString());
         if (!this.I) {
            this.ALLATORIxDEMO(arg0);
         }
      }

      return var2;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(byte[] arg0) {
      synchronized(this.N) {
         System.arraycopy(arg0, 0, this.N, 0, arg0.length);
         this.C = arg0.length;
      }
   }
}
