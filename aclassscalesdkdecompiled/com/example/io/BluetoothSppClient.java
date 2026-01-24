package com.example.io;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.aclas.ndklib.BuildConfig;
import com.example.util.AclasLogUtil;
import com.example.util.FutureThreadPool;
import com.example.util.Packet;
import com.example.util.PacketHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

public class BluetoothSppClient implements ComDevice {
   private Context ca;
   private BroadcastReceiver ma = new BroadcastReceiver() {
      public void onReceive(Context arg0, Intent arg1) {
         if (VERSION.SDK_INT < 31 || ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*)a+i0w*m6jwF\u0015Q\u001cP\u0016K\rL\u0006G\u0016J\u0017A\u001aP")) == 0) {
            HashMap var3 = new HashMap();
            BluetoothDevice var4 = (BluetoothDevice)arg1.getParcelableExtra(BluetoothCtrl.ALLATORIxDEMO("e7`+k0`wf5q<p6k-lw`<r0g<*<|-v8*\u001dA\u000fM\u001aA"));
            Bundle var5 = arg1.getExtras();
            var3.put(BluetoothCtrl.ALLATORIxDEMO("\u000bW\nM"), String.valueOf(var5.get(BluetoothCtrl.ALLATORIxDEMO("e7`+k0`wf5q<p6k-lw`<r0g<*<|-v8*\u000bW\nM"))));
            String var6 = var4.getName();
            var3.put(BluetoothCtrl.ALLATORIxDEMO("\u0017E\u0014A"), var6 == null ? BluetoothCtrl.ALLATORIxDEMO(",j2k.j") : var6);
            var3.put(BluetoothCtrl.ALLATORIxDEMO("G\u0016@"), String.valueOf(var5.get(BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*;h,a-k6p1*=a/m:awa!p+ewG\u0015E\nW"))));
            var3.put(BluetoothCtrl.ALLATORIxDEMO("\u001bK\u0017@"), var4.getBondState() == 12 ? BluetoothCtrl.ALLATORIxDEMO("f6q7`") : BluetoothCtrl.ALLATORIxDEMO("q7f6q7`"));
            String var7 = var5.getString("android.bluetooth.device.extra.DEVICE_TYPE");
            var3.put(BluetoothCtrl.ALLATORIxDEMO("\r]\tA"), var7 == null ? BluetoothCtrl.ALLATORIxDEMO("7q5h") : BluetoothSppClient.this.ALLATORIxDEMO(String.valueOf(var7)));
            var3.put(BluetoothCtrl.ALLATORIxDEMO("I\u0018G"), var4.getAddress());
            BluetoothSppClient.this.f.put(var4.getAddress(), var3);
            int var8 = var4.getType();
            String var9 = (var6 == null ? BluetoothCtrl.ALLATORIxDEMO(",j2k.j") : var6) + BluetoothSppClient.this.ALLATORIxDEMO(var8, var6) + var4.getAddress();
            if (!BluetoothSppClient.this.V.contains(var9) && (var9.contains(BluetoothCtrl.ALLATORIxDEMO("\u0016W")) || var9.contains(BluetoothCtrl.ALLATORIxDEMO("\u000eB")) || var9.contains(BluetoothCtrl.ALLATORIxDEMO("B\nG")) || var9.contains(BluetoothCtrl.ALLATORIxDEMO("\tW")))) {
               BluetoothSppClient.this.V.add(0, var9);
               if (BluetoothSppClient.this.N != null) {
                  BluetoothSppClient.this.N.onRecv(0, var9);
               }
            }

            BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("k7V<g<m/at)t)t)t)t)t)t)t)tw0~<>")).append(BluetoothSppClient.this.V.size()).append(BluetoothCtrl.ALLATORIxDEMO("ym7b6>")).append(var9).toString());
         }
      }
   };
   final int Ga = 1;
   private BluetoothGattCharacteristic fa = null;
   private BluetoothDevice ja = null;
   public boolean bLogFalg = false;
   private boolean Ha = false;
   private OutputStream O = null;
   private static final int W;
   final int Q = 2;
   private static final String r = "fea52a22-77be-4dc1-8878-054f95811912";
   final int T = 4;
   private static final String t = "0000fff1-0000-1000-8000-00805f9b34fb";
   private boolean n = false;
   private static final String X = "android.bluetooth.device.extra.DEVICE_TYPE";
   private int Y = 40;
   private int v;
   private InputStream y = null;
   private BluetoothGatt s = null;
   private boolean x = false;
   private BluetoothGattCharacteristic S = null;
   private static final int w = 1;
   private static final int o;
   private static final String Z = "0000ff02-0000-1000-8000-00805f9b34fb";
   private static final String p = "00001101-0000-1000-8000-00805F9B34FB";
   private boolean R = false;
   private static final String q = "fea52a22-77be-4dc1-8878-054f95812b12";
   private ArrayList V = new ArrayList();
   private <undefinedtype> U = null;
   private static final String z = "fea52a22-77be-4dc1-8878-054f95811910";
   private int P = 1;
   private static final String u = "11110001-1111-1111-1111-111111111111";
   ComDevice.ComDeviceListener N;
   private BluetoothSocket k = null;
   private final String C = "AclasBluetooth";
   private BluetoothDevice c = null;
   private final boolean g = true;
   private static final int e = 2;
   private static final String I = "00002902-0000-1000-8000-00805f9b34fb";
   final int E = 5;
   private boolean M = false;
   private BroadcastReceiver A = new BroadcastReceiver() {
      public void onReceive(Context arg0, Intent arg1) {
         BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", FutureThreadPool.ALLATORIxDEMO("\\hq{zy\u007fijH{y{sh\u007fl:xspsmr3737373737373737373737373"));
         BluetoothSppClient.this.n = true;
         BluetoothSppClient.this.d = true;
         BluetoothSppClient.this.F();
         if (null != BluetoothSppClient.this.H) {
            BluetoothSppClient.this.H.onScanFinish();
         }

         if (BluetoothSppClient.this.N != null) {
            BluetoothSppClient.this.N.onRecv(1, (String)null);
         }

      }
   };
   private final String a = "0000ff00-0000-1000-8000-00805f9b34fb";
   private final BluetoothGattCallback K = new BluetoothGattCallback() {
      public void onServicesDiscovered(BluetoothGatt arg0, int arg1) {
         BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(E#d0\u007f%s5R/e%y0s4s\"6k;k;k;k;k;k;k65b'b3e|")).append(arg1).toString());
         if (arg1 == 0 && arg0 != null) {
            if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[f\u0010d\u0018\u007f\u0006e\u001cy\u001b87Z S!Y:B=I6Y;X0U!")) != 0) {
               return;
            }

            Iterator var4 = arg0.getServices().iterator();

            while(var4.hasNext()) {
               BluetoothGattService var5;
               Iterator var7;
               BluetoothGattCharacteristic var8;
               int var9;
               String var10;
               int var11;
               BluetoothGattCharacteristic var12;
               if ((var5 = (BluetoothGattService)var4.next()).getUuid().toString().equals("fea52a22-77be-4dc1-8878-054f95811910")) {
                  var7 = var5.getCharacteristics().iterator();

                  while(var7.hasNext()) {
                     var12 = var8 = (BluetoothGattCharacteristic)var7.next();
                     var9 = var12.getProperties();
                     var10 = var12.getUuid().toString();
                     var11 = var12.getPermissions();
                     BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(E#d0\u007f%s5R/e%y0s4s\"63c/r|")).append(var10).append(Packet.ALLATORIxDEMO("6\u0005d\u001af\u0010d\u0001oO")).append(var9).append(BuildConfig.ALLATORIxDEMO("66s4{/e5\u007f)x|")).append(var11).toString());
                     if (var12.getUuid().toString().equals("fea52a22-77be-4dc1-8878-054f95812b10")) {
                        BluetoothSppClient.this.S = var8;
                        BluetoothSppClient.this.s.setCharacteristicNotification(BluetoothSppClient.this.S, true);
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("\u001ax6y\u001bx\u0010u\u0001\u007f\u001ax&b\u0014b\u0010U\u001dw\u001bq\u00106\u001ax&s\u0007`\u001cu\u0010e1\u007f\u0006u\u001a`\u0010d\u0010rUu\u001dw\u0007D\u0010w\u00116G"));
                     }
                  }
               } else if (var5.getUuid().toString().equals("fea52a22-77be-4dc1-8878-054f95811912")) {
                  var7 = var5.getCharacteristics().iterator();

                  while(var7.hasNext()) {
                     var12 = var8 = (BluetoothGattCharacteristic)var7.next();
                     var9 = var12.getProperties();
                     var10 = var12.getUuid().toString();
                     var11 = var12.getPermissions();
                     BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(E#d0\u007f%s5R/e%y0s4s\"63c/r|")).append(var10).append(Packet.ALLATORIxDEMO("6\u0005d\u001af\u0010d\u0001oO")).append(var9).append(BuildConfig.ALLATORIxDEMO("66s4{/e5\u007f)x|")).append(var11).toString());
                     if (var12.getUuid().toString().equals("fea52a22-77be-4dc1-8878-054f95812b12")) {
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("y\u001bU\u001ax\u001bs\u0016b\u001cy\u001bE\u0001w\u0001s6~\u0014x\u0012sUy\u001bE\u0010d\u0003\u007f\u0016s\u0006R\u001ce\u0016y\u0003s\u0007s\u00116\u0016~\u0014d\"d\u001cb\u0010"));
                        BluetoothSppClient.this.fa = var8;
                        BluetoothSppClient.this.s.setCharacteristicNotification(var8, false);
                     }
                  }
               } else if (var5.getUuid().toString().equals("0000ff00-0000-1000-8000-00805f9b34fb")) {
                  if (VERSION.SDK_INT >= 31) {
                     arg0.requestMtu(200);
                  }

                  var7 = var5.getCharacteristics().iterator();

                  while(var7.hasNext()) {
                     var12 = var8 = (BluetoothGattCharacteristic)var7.next();
                     var9 = var12.getProperties();
                     var10 = var12.getUuid().toString();
                     var11 = var12.getPermissions();
                     BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(E#d0\u007f%s5R/e%y0s4s\"63c/r|")).append(var10).append(Packet.ALLATORIxDEMO("6\u0005d\u001af\u0010d\u0001oO")).append(var9).append(BuildConfig.ALLATORIxDEMO("66s4{/e5\u007f)x|")).append(var11).toString());
                     if (var12.getUuid().toString().equals("0000ff02-0000-1000-8000-00805f9b34fb")) {
                        BluetoothSppClient.this.fa = var8;
                        if (VERSION.SDK_INT >= 31) {
                           BluetoothSppClient.this.s.setCharacteristicNotification(var8, false);
                        }

                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("y\u001bU\u001ax\u001bs\u0016b\u001cy\u001bE\u0001w\u0001s6~\u0014x\u0012sUy\u001bE\u0010d\u0003\u007f\u0016s\u0006R\u001ce\u0016y\u0003s\u0007s\u00116\u0016~\u0014d\"d\u001cb\u0010T'"));
                     } else if (var8.getUuid().toString().equals("0000ff01-0000-1000-8000-00805f9b34fb")) {
                        BluetoothSppClient.this.S = var8;
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", BuildConfig.ALLATORIxDEMO(")x\u0005y(x#u2\u007f)x\u0015b'b#U.w(q#6)x\u0015s4`/u#e\u0002\u007f5u)`#d#rfu.w4D#w\"T\u0014"));
                     }
                  }
               } else if (var5.getUuid().toString().equals("0000fff0-0000-1000-8000-00805f9b34fb")) {
                  if (VERSION.SDK_INT >= 21) {
                     arg0.requestMtu(200);
                  }

                  var7 = var5.getCharacteristics().iterator();

                  while(var7.hasNext()) {
                     var12 = var8 = (BluetoothGattCharacteristic)var7.next();
                     var9 = var12.getProperties();
                     var10 = var12.getUuid().toString();
                     var11 = var12.getPermissions();
                     BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("y\u001bE\u0010d\u0003\u007f\u0016s\u0006R\u001ce\u0016y\u0003s\u0007s\u0011I3O!6\u0000c\u001crO")).append(var10).append(BuildConfig.ALLATORIxDEMO("66d)f#d2o|")).append(var9).append(Packet.ALLATORIxDEMO("6\u0005s\u0007{\u001ce\u0006\u007f\u001axO")).append(var11).toString());
                     if (var12.getUuid().toString().equals("0000fff2-0000-1000-8000-00805f9b34fb")) {
                        BluetoothSppClient.this.fa = var8;
                        BluetoothSppClient.this.s.setCharacteristicNotification(var8, false);
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", BuildConfig.ALLATORIxDEMO("y(U)x(s%b/y(E2w2s\u0005~'x!sfy(E#d0\u007f%s5R/e%y0s4s\"6%~'d\u0011d/b#I\u0000O\u0012"));
                     } else if (var8.getUuid().toString().equals("0000fff1-0000-1000-8000-00805f9b34fb")) {
                        BluetoothSppClient.this.S = var8;
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("\u001ax6y\u001bx\u0010u\u0001\u007f\u001ax&b\u0014b\u0010U\u001dw\u001bq\u00106\u001ax&s\u0007`\u001cu\u0010e1\u007f\u0006u\u001a`\u0010d\u0010rUu\u001dw\u0007D\u0010w\u0011I3O!"));
                     }
                  }
               } else {
                  BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(E#d0\u007f%s5R/e%y0s4s\"6!w2b\u0015s4`/u#63c/r|")).append(var5.getUuid().toString()).toString());
                  Iterator var10000 = var7 = var5.getCharacteristics().iterator();

                  while(var10000.hasNext()) {
                     var12 = (BluetoothGattCharacteristic)var7.next();
                     var9 = var12.getProperties();
                     var10 = var12.getUuid().toString();
                     var11 = var12.getPermissions();
                     var10000 = var7;
                     BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001ax&s\u0007`\u001cu\u0010e1\u007f\u0006u\u001a`\u0010d\u0010rUq\u0014b\u0001E\u0010d\u0003\u007f\u0016sUu\u001dw\u00076\u0000c\u001crO")).append(var10).append(BuildConfig.ALLATORIxDEMO("66d)f#d2o|")).append(var9).append(Packet.ALLATORIxDEMO("6\u0005s\u0007{\u001ce\u0006\u007f\u001axO")).append(var11).toString());
                  }
               }

               if (BluetoothSppClient.this.S != null && BluetoothSppClient.this.fa != null) {
                  BluetoothSppClient.this.ALLATORIxDEMO(true);
                  BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", BuildConfig.ALLATORIxDEMO("y(U)x(s%b/y(E2w2s\u0005~'x!sfy(E#d0\u007f%s5R/e%y0s4s\"6\"s0D#w\"ofb4c#"));
                  if (BluetoothSppClient.this.N != null && BluetoothSppClient.this.Ha) {
                     BluetoothSppClient.this.N.onConnectFinished(2);
                  }

                  BluetoothSppClient.this.B = true;
                  return;
               }
            }
         } else {
            BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("7Ty\u001bE\u0010d\u0003\u007f\u0016s\u0006R\u001ce\u0016y\u0003s\u0007s\u00116X;X;X;Xs\u0007d\u001adX;X6\u0006b\u0014b\u0000eO")).append(arg1).toString());
         }

      }

      public void onCharacteristicChanged(BluetoothGatt arg0, BluetoothGattCharacteristic arg1) {
         if (null != arg1) {
            byte[] var3 = arg1.getValue();
            if (null != var3) {
               int var4 = var3.length;
               BluetoothSppClient.this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(U.w4w%b#d/e2\u007f%U.w(q#r|")).append(var4).toString());
               if (var4 > 0) {
                  BluetoothSppClient.this.ALLATORIxDEMO(var3);
               }
            }
         }

      }

      public void onMtuChanged(BluetoothGatt arg0, int arg1, int arg2) {
         if (arg2 == 0) {
            BluetoothSppClient.this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001ax8b\u0000U\u001dw\u001bq\u0010rO")).append(arg1).toString());
         } else {
            BluetoothSppClient.this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y([2c\u0005~'x!s\"6#d4y46|")).append(arg1).toString());
         }
      }

      public void onCharacteristicRead(BluetoothGatt arg0, BluetoothGattCharacteristic arg1, int arg2) {
         if (arg2 == 0 && arg0 != null && null != arg1) {
            byte[] var4 = arg1.getValue();
            if (null != var4) {
               int var5 = var4.length;
               boolean var6 = false;
               boolean var7 = false;
               if (var5 > 0) {
                  BluetoothSppClient.this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001ax6~\u0014d\u0014u\u0001s\u0007\u007f\u0006b\u001cu's\u0014rO")).append(var5).toString());
               }
            }
         }

      }

      public void onConnectionStateChange(BluetoothGatt arg0, int arg1, int arg2) {
         BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO(")x\u0005y(x#u2\u007f)x\u0015b'b#U.w(q#6f;k;k;k;k;(s1E2w2s|")).append(arg2).append(Packet.ALLATORIxDEMO("U")).append(BluetoothSppClient.this.ALLATORIxDEMO(arg2)).toString());
         if (VERSION.SDK_INT < 31 || ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, BuildConfig.ALLATORIxDEMO("w(r4y/rhf#d+\u007f5e/y(8\u0004Z\u0013S\u0012Y\tB\u000eI\u0005Y\bX\u0003U\u0012")) == 0) {
            if (arg2 == 2) {
               if (BluetoothSppClient.this.Ha && !BluetoothSppClient.this.x && BluetoothSppClient.this.N != null) {
                  BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("\u001ea\u00046\u001ax6y\u001bx\u0010u\u0001\u007f\u001ax&b\u0014b\u0010U\u001dw\u001bq\u00106&B4B0I6Y;X0U!S16D;X;X;X;X;X;X;X;X;X;X;X"));
               }

               BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", BuildConfig.ALLATORIxDEMO("}1gfy(U)x(s%b/y(E2w2s\u0005~'x!s|6\u0015B\u0007B\u0003I\u0005Y\bX\u0003U\u0012S\u00026\"\u007f5u)`#d\u0015s4`/u#ek;k;k;k;k;k;k"));
               BluetoothSppClient.this.x = true;
               BluetoothSppClient.this.l = 1;
               BluetoothSppClient.this.s.discoverServices();
            } else if (arg2 != 0) {
               BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO(")x\u0005y(x#u2\u007f)x\u0015b'b#U.w(q#6k;k;k;k;k;k;k;k6(s1e2w2s|")).append(arg2).toString());
            } else {
               <undefinedtype> var10000;
               label41: {
                  BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("y\u001bU\u001ax\u001bs\u0016b\u001cy\u001bE\u0001w\u0001s6~\u0014x\u0012sUE!W!S*R<E6Y;X0U!S16E"));
                  if (!BluetoothSppClient.this.R) {
                     if (BluetoothSppClient.this.x && BluetoothSppClient.this.N != null) {
                        if (BluetoothSppClient.this.B) {
                           BluetoothSppClient.this.N.onDisconnect();
                        }

                        var10000 = this;
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("y(U)x(s%b/y(E2w2s\u0005~'x!sfE\u0012W\u0012S\u0019R\u000fE\u0005Y\bX\u0003U\u0012S\u00026t6\"s0D#w\"o|")).append(BluetoothSppClient.this.B).toString());
                        break label41;
                     }
                  } else {
                     if (null != BluetoothSppClient.this.s) {
                        BluetoothSppClient.this.s.close();
                        BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", Packet.ALLATORIxDEMO("y\u001bU\u001ax\u001bs\u0016b\u001cy\u001bE\u0001w\u0001s6~\u0014x\u0012sUE!W!S*R<E6Y;X0U!S16A"));
                     }

                     BluetoothSppClient.this.s = null;
                     BluetoothSppClient.this.ja = null;
                     BluetoothSppClient.this.S = null;
                     BluetoothSppClient.this.fa = null;
                  }

                  var10000 = this;
               }

               BluetoothSppClient.this.l = 0;
               BluetoothSppClient.this.x = false;
               BluetoothSppClient.this.B = false;
            }
         }
      }
   };
   private byte[] J = new byte[512];
   private String F;
   private boolean m = true;
   private static final String b = "0000ff01-0000-1000-8000-00805f9b34fb";
   boolean B = false;
   private static final String L = "0000fff2-0000-1000-8000-00805f9b34fb";
   private BluetoothAdapter D = null;
   private int l = -1;
   private static final int G = 3;
   private OnOpenBluetoothListener H = null;
   private final String j = "0000fff0-0000-1000-8000-00805f9b34fb";
   private static final String h = "fea52a22-77be-4dc1-8878-054f95812b10";
   final int i = 3;
   HashMap f = new HashMap();
   private boolean d = true;
   private int ALLATORIxDEMO = 0;

   // $FF: synthetic method
   private String i(int arg0) {
      String var2 = "";
      switch (arg0) {
         case 1:
            while(false) {
            }

            return Packet.ALLATORIxDEMO("66z\u0014e\u0006\u007f\u0016I\u0018y\u0011sY");
         case 2:
            return BuildConfig.ALLATORIxDEMO("6\u0004Z\u0003I+y\"sj");
         case 3:
            return Packet.ALLATORIxDEMO("UR\u0000f\u0019s\rI\u0018y\u0011sY");
         default:
            return (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("fC(}(y1I+y\"sf")).append(String.valueOf(arg0)).toString();
      }
   }

   public void setOnOpenBluetoothListener(OnOpenBluetoothListener arg0) {
      this.H = arg0;
   }

   protected Boolean writeBtData(byte[] arg0) {
      Boolean var2 = false;
      if (null != this.s && null != this.fa && this.B) {
         if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[f\u0010d\u0018\u007f\u0006e\u001cy\u001b87Z S!Y:B=I6Y;X0U!")) != 0) {
            return var2;
         }

         this.fa.setValue(arg0);
         this.fa.setWriteType(1);
         var2 = this.s.writeCharacteristic(this.fa);
         this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("a4\u007f2s\u0004b\u0002w2w|")).append(var2).append(Packet.ALLATORIxDEMO("Uz\u0010xO")).append(arg0.length).toString());
      }

      return var2;
   }

   public BluetoothSppClient() {
      this.v = 4;
   }

   public int open(String arg0) {
      int var2 = -1;
      if (arg0 != null) {
         BluetoothSppClient var10000;
         if (!arg0.contains(BuildConfig.ALLATORIxDEMO("j"))) {
            var10000 = this;
            this.F = arg0;
         } else {
            String[] var3 = arg0.split(Packet.ALLATORIxDEMO("Y"));
            this.F = var3[1];
            if (!var3[0].contains(BuildConfig.ALLATORIxDEMO("\u0004Z\u0003")) && !var3[0].contains(Packet.ALLATORIxDEMO("\u0017z\u0010")) && !var3[0].contains(BuildConfig.ALLATORIxDEMO("\u0004z#"))) {
               var10000 = this;
               this.m = false;
               this.v = 3;
            } else {
               var10000 = this;
               this.m = true;
               this.v = 4;
            }

            var10000.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001af\u0010xO")).append(var3[0]).append(BuildConfig.ALLATORIxDEMO("fT\nS|")).append(this.m).append(Packet.ALLATORIxDEMO("67B*@0DO")).append(this.v).append(BuildConfig.ALLATORIxDEMO("f")).append(arg0).toString());
            var10000 = this;
         }

         label64: {
            if (var10000.F.length() > 0) {
               if (this.N != null) {
                  this.N.onConnecting();
               }

               this.c = this.D.getRemoteDevice(this.F);
               if (this.m) {
                  if (this.U == null) {
                     this.Ha = false;
                     this.R = false;
                     this.U = new Thread() {
                        final int j = 4;
                        final int h = 200;
                        final int i = 4;
                        boolean f = false;
                        final int d = 20;

                        public synchronized void e() {
                           this.f = false;
                           this.notifyAll();

                           try {
                              sleep(100L);
                           } catch (Exception var2) {
                           }
                        }

                        // $FF: synthetic method
                        private void F() {
                           if (BluetoothSppClient.this.s != null) {
                              BluetoothSppClient.this.ALLATORIxDEMO(FutureThreadPool.ALLATORIxDEMO("~q^wi}uh\u007fl:wt>nvh{{z"));
                              if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*)a+i0w*m6jwF\u0015Q\u001cP\u0016K\rL\u0006G\u0016J\u0017A\u001aP")) != 0) {
                                 return;
                              }

                              BluetoothSppClient.this.s.discoverServices();
                           }

                        }

                        // $FF: synthetic method
                        private void h() {
                           if (BluetoothSppClient.this.s != null) {
                              if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, FutureThreadPool.ALLATORIxDEMO("{p~luw~0j{hssmiwup4\\VK_JUQNVE]UPT[YJ")) != 0) {
                                 return;
                              }

                              BluetoothSppClient.this.s.connect();
                              BluetoothSppClient.this.ALLATORIxDEMO(BluetoothCtrl.ALLATORIxDEMO("`6V<g6j7a:pym7$-l+a8`"));
                           }

                        }

                        // $FF: synthetic method
                        private void i() {
                           if (BluetoothSppClient.this.D != null) {
                              if (BluetoothSppClient.this.ja == null) {
                                 BluetoothSppClient.this.ja = BluetoothSppClient.this.D.getRemoteDevice(BluetoothSppClient.this.F);
                              }

                              if (BluetoothSppClient.this.ja != null) {
                                 if (BluetoothSppClient.this.s == null) {
                                    if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, FutureThreadPool.ALLATORIxDEMO("{p~luw~0j{hssmiwup4\\VK_JUQNVE]UPT[YJ")) != 0) {
                                       return;
                                    }

                                    BluetoothSppClient.this.s = BluetoothSppClient.this.ja.connectGatt(BluetoothSppClient.this.ca, true, BluetoothSppClient.this.K);
                                    BluetoothSppClient.this.ALLATORIxDEMO(BluetoothCtrl.ALLATORIxDEMO("g6j7a:p\u001ee-pym7$-l+a8`"));
                                    return;
                                 }

                                 this.h();
                              }
                           }

                        }

                        public void run() {
                           super.run();
                           boolean var1 = false;
                           int var2 = 0;
                           int var3 = 0;
                           int var4 = 0;
                           this.i();

                           label61:
                           while(true) {
                              <undefinedtype> var10000 = this;

                              while(var10000.f && BluetoothSppClient.this.s != null) {
                                 label52: {
                                    if (!var1) {
                                       if (this.ALLATORIxDEMO()) {
                                          var1 = true;
                                          var10000 = this;
                                          break label52;
                                       }

                                       if (BluetoothSppClient.this.l == -1) {
                                          if (var2++ > 20) {
                                             var2 = 0;
                                             var10000 = this;
                                             this.i();
                                             break label52;
                                          }
                                       } else if (BluetoothSppClient.this.l == 1) {
                                          if (var3++ > 4) {
                                             var3 = 0;
                                             var10000 = this;
                                             this.F();
                                             break label52;
                                          }
                                       } else if (BluetoothSppClient.this.l == 0 && !BluetoothSppClient.this.R && var4++ > 8) {
                                          var4 = 0;
                                          var10000 = this;
                                          this.h();
                                          break label52;
                                       }
                                    } else if (BluetoothSppClient.this.l == 0 && !BluetoothSppClient.this.R && var4++ > 4) {
                                       var4 = 0;
                                       this.h();
                                    }

                                    var10000 = this;
                                 }

                                 if (!var10000.f) {
                                    continue label61;
                                 }

                                 try {
                                    sleep(200L);
                                 } catch (Exception var6) {
                                    var10000 = this;
                                    continue;
                                 }

                                 var10000 = this;
                              }

                              BluetoothSppClient.this.ALLATORIxDEMO(FutureThreadPool.ALLATORIxDEMO("{bwn>xjnvh{{z:373737>"));
                              return;
                           }
                        }

                        public synchronized void ALLATORIxDEMO() {
                           this.f = true;
                           BluetoothSppClient.this.l = -1;
                           super.start();
                        }

                        public synchronized boolean i(int arg0) {
                           <undefinedtype> var4;
                           try {
                              if (null != BluetoothSppClient.this.s) {
                                 if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*)a+i0w*m6jwF\u0015Q\u001cP\u0016K\rL\u0006G\u0016J\u0017A\u001aP")) != 0) {
                                    return false;
                                 }

                                 BluetoothSppClient.this.s.close();
                                 BluetoothSppClient.this.s = null;
                              }

                              this.i();
                              this.wait((long)arg0);
                              BluetoothSppClient.this.ALLATORIxDEMO(FutureThreadPool.ALLATORIxDEMO("m\u007fsjYqtp\u007f}n_}\u007fsp:\u007f|j\u007fl:pujsxc>m\u007fsj"));
                           } catch (Exception var3) {
                              BluetoothSppClient var10000 = BluetoothSppClient.this;
                              StringBuilder var10002 = (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("s8m-G6j7a:p\u0018c8m7$-m4a6q->")).append(arg0).append(FutureThreadPool.ALLATORIxDEMO(":z\u007fhH{{zc$"));
                              BluetoothSppClient var10001 = var10000;
                              var4 = this;
                              var10001.ALLATORIxDEMO(var10002.append(BluetoothSppClient.this.B).append(BluetoothCtrl.ALLATORIxDEMO("$")).append(var3.toString()).toString());
                              return BluetoothSppClient.this.B;
                           }

                           var4 = this;
                           return BluetoothSppClient.this.B;
                        }

                        public synchronized boolean ALLATORIxDEMO(int arg0) {
                           <undefinedtype> var4;
                           try {
                              this.wait((long)arg0);
                              BluetoothSppClient.this.ALLATORIxDEMO(FutureThreadPool.ALLATORIxDEMO("i{wn]upt{yj:\u007f|j\u007fl:pujsxc>m\u007fsj"));
                           } catch (Exception var3) {
                              BluetoothSppClient var10000 = BluetoothSppClient.this;
                              StringBuilder var10002 = (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(".e0p\u001ak7j<g-$-m4a6q->")).append(arg0).append(FutureThreadPool.ALLATORIxDEMO(":z\u007fhH{{zc$"));
                              BluetoothSppClient var10001 = var10000;
                              var4 = this;
                              var10001.ALLATORIxDEMO(var10002.append(BluetoothSppClient.this.B).append(BluetoothCtrl.ALLATORIxDEMO("$")).append(var3.toString()).toString());
                              return BluetoothSppClient.this.B;
                           }

                           var4 = this;
                           return BluetoothSppClient.this.B;
                        }

                        public synchronized boolean ALLATORIxDEMO() {
                           boolean var1 = false;
                           if (BluetoothSppClient.this.B) {
                              var1 = true;
                              BluetoothSppClient.this.ALLATORIxDEMO(FutureThreadPool.ALLATORIxDEMO("z\u007fhH{{zc>nlo{:wt>nvh{{z"));
                              this.notify();
                           }

                           return var1;
                        }
                     };
                     this.U.ALLATORIxDEMO();
                     this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("a\u0014\u007f\u0001U\u001ax\u001bs\u0016bUe\u0001w\u0007bX;X"));
                     if (this.U.ALLATORIxDEMO(o)) {
                        this.ALLATORIxDEMO(BuildConfig.ALLATORIxDEMO("1w/b\u0005y(x#u264s2c4xfu)x(s%bk;k"));
                        var2 = 0;
                        var10000 = this;
                     } else {
                        this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("\u0002w\u001cb6y\u001bx\u0010u\u00016\u0010d\u00077T7X;X"));
                        var10000 = this;
                     }

                     var10000.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("$z#6%y(x#u264s2c4x|")).append(var2).append(Packet.ALLATORIxDEMO("UT _9R*@0DO")).append(W).toString());
                     if (null != this.S && null != this.fa) {
                        var10000 = this;
                        this.Ha = true;
                        break label64;
                     }

                     var10000 = this;
                     this.disconnDevice();
                     break label64;
                  }
               } else {
                  if (this.c != null) {
                     boolean var6 = true;
                     boolean var4 = true;
                     if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, BuildConfig.ALLATORIxDEMO("w(r4y/rhf#d+\u007f5e/y(8\u0004Z\u0013S\u0012Y\tB\u000eI\u0005Y\bX\u0003U\u0012")) != 0) {
                        var4 = false;
                     }

                     if (var4) {
                        boolean var5 = this.c.getBondState() == 10;
                        if (var5) {
                           var10000 = this;
                           var6 = this.ALLATORIxDEMO();
                        } else {
                           var6 = true;
                           var10000 = this;
                        }

                        var10000.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO(";s\u0010r%w\u001cdO")).append(var5).append(BuildConfig.ALLATORIxDEMO("66w/d|")).append(var6).toString());
                     }
                  }

                  var2 = this.ALLATORIxDEMO();
               }
            }

            var10000 = this;
         }

         if (var10000.N != null) {
            this.N.onConnectFinished(var2);
         }
      }

      return var2;
   }

   // $FF: synthetic method
   private int i(byte[] arg0, int arg1) {
      int var3 = 0;
      synchronized(this.J) {
         if (this.ALLATORIxDEMO > 0) {
            var3 = Math.min(this.ALLATORIxDEMO, arg1);
            System.arraycopy(this.J, 0, arg0, 0, var3);
            this.ALLATORIxDEMO = 0;
         }

         return var3;
      }
   }

   void ALLATORIxDEMO(HashMap arg0) {
      if (null != this.H) {
         this.H.onScanDevices(arg0);
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      this.ALLATORIxDEMO("AclasBluetooth", arg0);
   }

   public void enableBluetooth() {
      if (null != this.D) {
         if (!this.D.isEnabled()) {
            (new Thread() {
               public void run() {
                  int var1 = 50;
                  if (!BluetoothSppClient.this.D.isEnabled()) {
                     if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, BluetoothCtrl.ALLATORIxDEMO("8j=v6m=*)a+i0w*m6jwF\u0015Q\u001cP\u0016K\rL\u0006G\u0016J\u0017A\u001aP")) != 0) {
                        return;
                     }

                     BluetoothSppClient.this.D.enable();
                     int var10000 = var1;

                     while(true) {
                        --var1;
                        if (var10000 <= 0 || BluetoothSppClient.this.D.isEnabled()) {
                           break;
                        }

                        try {
                           Thread.sleep(100L);
                        } catch (InterruptedException var3) {
                           var10000 = var1;
                           var3.printStackTrace();
                           continue;
                        }

                        var10000 = var1;
                     }
                  }

                  if (null != BluetoothSppClient.this.H) {
                     BluetoothSppClient.this.H.onEnableBluetooth(var1 > 0, "");
                  }

               }
            }).start();
            return;
         }

         if (null != this.H) {
            this.H.onEnableBluetooth(true, "");
            return;
         }
      } else if (null != this.H) {
         this.H.onEnableBluetooth(false, "");
      }

   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(int arg0) {
      String var2 = Packet.ALLATORIxDEMO(" x\u001ex\u001aa\u001b");
      switch (arg0) {
         case 0:
            while(false) {
            }

            return BuildConfig.ALLATORIxDEMO("R\u000fE\u0005Y\bX\u0003U\u0012S\u0002");
         case 1:
            return Packet.ALLATORIxDEMO("U:X;S6B<X2");
         case 2:
            return BuildConfig.ALLATORIxDEMO("\u0005Y\bX\u0003U\u0012S\u0002");
         case 3:
            var2 = Packet.ALLATORIxDEMO("1_&U:X;S6B<X2");
         default:
            return var2;
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1) {
      int var3 = -1;

      try {
         if (this.y != null) {
            var3 = this.y.read(arg0, 0, arg1);
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("d#w\"E2s'{|")).append(var3).toString());
         }
      } catch (Exception var5) {
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("d\u0010w\u0011E\u0001s\u0014{Us\u0007d\u001adO")).append(var5.toString()).toString());
      }

      return var3;
   }

   public void setLog(boolean arg0) {
      this.bLogFalg = arg0;
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(int arg0, String arg1) {
      String var3 = "";
      switch (arg0) {
         case 1:
            while(false) {
            }

            return BuildConfig.ALLATORIxDEMO("6\u0005z'e5\u007f%I+y\"sj");
         case 2:
            if (arg1 == null) {
               return Packet.ALLATORIxDEMO("67Z0I\u0018y\u0011sY");
            } else {
               if (!arg1.contains(BuildConfig.ALLATORIxDEMO("\u0004Z\u0003"))) {
                  return Packet.ALLATORIxDEMO("67Z0I\u0018y\u0011sY");
               }

               return BuildConfig.ALLATORIxDEMO("j");
            }
         case 3:
            return Packet.ALLATORIxDEMO("UR\u0000f\u0019s\rI\u0018y\u0011sY");
         default:
            return (new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("fC(}(y1I+y\"sf")).append(String.valueOf(arg0)).append(Packet.ALLATORIxDEMO("Y")).toString();
      }
   }

   public int getDevList(List arg0) {
      if (this.D == null) {
         return -1;
      } else {
         if (arg0 == null) {
            arg0 = new ArrayList();
         }

         ((List)arg0).clear();
         this.scanDevices(false);
         if (!this.V.isEmpty()) {
            Iterator var2;
            Iterator var10000 = var2 = this.V.iterator();

            while(var10000.hasNext()) {
               String var3 = (String)var2.next();
               var10000 = var2;
               ((List)arg0).add(var3);
            }
         } else {
            this.ALLATORIxDEMO("AclasBluetooth", BuildConfig.ALLATORIxDEMO("q#b\u0002s0Z/e26#{6b?"));
         }

         return ((List)arg0).size();
      }
   }

   public int readData(byte[] arg0, int arg1, int arg2) {
      if (!this.B) {
         return -1;
      } else {
         return this.m ? this.i(arg0, arg1) : this.ALLATORIxDEMO(arg0, arg1);
      }
   }

   protected byte[] readBtData() {
      if (null != this.s && null != this.S) {
         if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[f\u0010d\u0018\u007f\u0006e\u001cy\u001b87Z S!Y:B=I6Y;X0U!")) != 0) {
            return null;
         } else {
            this.s.readCharacteristic(this.S);
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean isReady() {
      return this.B;
   }

   // $FF: synthetic method
   private static String ALLATORIxDEMO(byte[] arg0) {
      StringBuilder var1 = new StringBuilder("");
      if (arg0 != null && arg0.length > 0) {
         int var2 = arg0.length > 32 ? 32 : arg0.length;

         int var3;
         for(int var10000 = var3 = 0; var10000 < var2; var10000 = var3) {
            String var5;
            if ((var5 = Integer.toHexString(arg0[var3] & 255)).length() < 2) {
               var1.append(0);
            }

            var1.append(var5);
            ++var3;
            var1.append(BuildConfig.ALLATORIxDEMO("f"));
         }

         return var1.toString();
      } else {
         return null;
      }
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(byte[] arg0) {
      if (arg0 != null && arg0.length > 0) {
         synchronized(this.J) {
            if (this.ALLATORIxDEMO + arg0.length < this.J.length) {
               System.arraycopy(arg0, 0, this.J, this.ALLATORIxDEMO, arg0.length);
               this.ALLATORIxDEMO += arg0.length;
            }

         }
      }
   }

   public void disconnDevice() {
      this.R = true;
      this.Ha = false;
      if (this.U != null) {
         this.U.e();
         this.U = null;
      }

      if (null != this.s) {
         this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("\u0011\u007f\u0006u\u001ax\u001bR\u0010`\u001cu\u0010"));
         this.ALLATORIxDEMO(false);
         if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, BuildConfig.ALLATORIxDEMO("w(r4y/rhf#d+\u007f5e/y(8\u0004Z\u0013S\u0012Y\tB\u000eI\u0005Y\bX\u0003U\u0012")) != 0) {
            return;
         }

         this.s.disconnect();
         if (!this.x) {
            this.s.close();
            this.s = null;
         }
      }

      this.ALLATORIxDEMO(Packet.ALLATORIxDEMO(";X;\u0011\u007f\u0006u\u001ax\u001bR\u0010`\u001cu\u00106\u0010x\u0011;X;X;X"));
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0) {
      int var2 = -1;

      try {
         if (this.O != null) {
            this.O.write(arg0);
            var2 = arg0.length;
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("a4\u007f2s\u0015b4s'{|")).append(var2).toString());
         }
      } catch (Exception var4) {
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("a\u0007\u007f\u0001s&b\u0007s\u0014{Us\u0007d\u001adO")).append(var4.toString()).toString());
      }

      return var2;
   }

   public BluetoothSppClient(Context arg0) {
      this.D = BluetoothAdapter.getDefaultAdapter();
      this.ca = arg0;
      this.v = 4;
   }

   protected void closeDevice() {
      BluetoothSppClient var10000;
      if (this.k != null) {
         label31: {
            try {
               if (this.y != null) {
                  this.y.close();
                  this.y = null;
               }

               if (this.O != null) {
                  this.O.close();
                  this.O = null;
               }

               this.k.close();
               this.k = null;
            } catch (IOException var2) {
               var10000 = this;
               var2.printStackTrace();
               break label31;
            }

            var10000 = this;
         }
      } else {
         if (null != this.s) {
            this.disconnDevice();
         }

         var10000 = this;
      }

      if (!var10000.d) {
         this.F();
         this.d = true;
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(boolean arg0) {
      if (null != this.s && null != this.S) {
         if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, BuildConfig.ALLATORIxDEMO("w(r4y/rhf#d+\u007f5e/y(8\u0004Z\u0013S\u0012Y\tB\u000eI\u0005Y\bX\u0003U\u0012")) != 0) {
            return;
         }

         BluetoothGattDescriptor var2 = this.S.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
         BluetoothSppClient var10000;
         boolean var3;
         if (null != var2) {
            var2.setValue(arg0 ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            SystemClock.sleep(200L);
            var3 = this.s.writeDescriptor(var2);
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("e\u0010b;y\u0001\u007f\u0013oUa\u0007\u007f\u0001s1s\u0006u\u0007\u007f\u0005b\u001adO")).append(var3).toString());
            var10000 = this;
         } else {
            Log.e("AclasBluetooth", BuildConfig.ALLATORIxDEMO("e#b\by2\u007f ofr#e%d/f2y46(c*zf&v&v$\u007f&t;v&v&k'v&v;~&v&k&v.v# /$%rp$"));
            var10000 = this;
         }

         var3 = var10000.s.setCharacteristicNotification(this.S, arg0);
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("e\u0010b6~\u0014d\u0014u\u0001s\u0007\u007f\u0006b\u001cu;y\u0001\u007f\u0013\u007f\u0016w\u0001\u007f\u001axO")).append(arg0).append(BuildConfig.ALLATORIxDEMO("fd#b|")).append(var3).toString());
         if (arg0 && W < 30) {
            SystemClock.sleep(200L);
            var3 = this.s.setCharacteristicNotification(this.S, arg0);
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("e\u0010b6~\u0014d\u0014u\u0001s\u0007\u007f\u0006b\u001cu;y\u0001\u007f\u0013\u007f\u0016w\u0001\u007f\u001axUw\u0012w\u001cxUd\u0010bO")).append(var3).toString());
         }
      }

   }

   void F() {
      this.ca.unregisterReceiver(this.ma);
      this.ca.unregisterReceiver(this.A);
   }

   static {
      W = VERSION.SDK_INT;
      if (W >= 30) {
         o = 20000;
      } else {
         o = 40000;
      }
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(String arg0) {
      if (!Pattern.compile(BuildConfig.ALLATORIxDEMO("\u0018MkJmKyM\u001ar\u001b=b")).matcher(arg0).matches()) {
         return arg0;
      } else {
         switch (Integer.valueOf(arg0)) {
            case 1:
               while(false) {
               }

               return Packet.ALLATORIxDEMO("T'90R'67z\u0000s\u0001y\u001ab\u001d");
            case 2:
               return BuildConfig.ALLATORIxDEMO("Z)afS(s4q?6\u0004z3s2y)b.");
            case 3:
               return Packet.ALLATORIxDEMO("1c\u0005z\u0010nU{\u001ar\u001067z\u0000s\u0001y\u001ab\u001d");
            default:
               return BuildConfig.ALLATORIxDEMO("T\u00149\u0003R\u00146\u0004z3s2y)b.");
         }
      }
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, String arg1) {
      if (this.bLogFalg) {
         AclasLogUtil.info(arg0, arg1);
      }

   }

   void h() {
      if (VERSION.SDK_INT < 31 || ActivityCompat.checkSelfPermission(this.ca, Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[f\u0010d\u0018\u007f\u0006e\u001cy\u001b87Z S!Y:B=I6Y;X0U!")) == 0) {
         Set var1;
         if ((var1 = this.D.getBondedDevices()) != null && var1.size() > 0) {
            Iterator var2 = var1.iterator();

            while(true) {
               String var6;
               do {
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     BluetoothDevice var3;
                     BluetoothDevice var10000 = var3 = (BluetoothDevice)var2.next();
                     String var4 = var10000.getName();
                     int var5 = var10000.getType();
                     var6 = (var4 == null ? BuildConfig.ALLATORIxDEMO("c(})a(") : var4) + this.ALLATORIxDEMO(var5, var4) + var3.getAddress();
                  } while(this.V.contains(var6));
               } while(!var6.contains(Packet.ALLATORIxDEMO("Y&")) && !var6.contains(BuildConfig.ALLATORIxDEMO("A\u0000")));

               this.V.add(0, var6);
               if (this.N != null) {
                  this.N.onRecv(0, var6);
               }

               this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0017y\u0000x\u0011s\u0011;X;X;X;X;X;X;X;X;\u0006\u007f\u000fsO")).append(this.V.size()).append(BuildConfig.ALLATORIxDEMO("6/x y|")).append(var6).toString());
            }
         }
      }
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO() {
      boolean var1 = false;
      FutureTask var2 = FutureThreadPool.getInstance().executeTask(new Callable() {
         public Boolean ALLATORIxDEMO() throws Exception {
            int var1 = 150;
            boolean var2 = false;
            if (BluetoothSppClient.this.c != null) {
               try {
                  BluetoothCtrl.createBond(BluetoothSppClient.this.c);
                  boolean var3 = true;
                  if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(BluetoothSppClient.this.ca, Packet.ALLATORIxDEMO("w\u001br\u0007y\u001cr[f\u0010d\u0018\u007f\u0006e\u001cy\u001b87Z S!Y:B=I6Y;X0U!")) != 0) {
                     var3 = false;
                  }

                  if (var3) {
                     while(true) {
                        int var10000 = var1;

                        while(true) {
                           --var1;
                           if (var10000 <= 0) {
                              return var2;
                           }

                           if (BluetoothSppClient.this.c.getBondState() == 12) {
                              var2 = true;
                              BluetoothSppClient.this.ALLATORIxDEMO(BuildConfig.ALLATORIxDEMO("6w/d\u0002s0\u007f%sfT\tX\u0002I\u0004Y\bR\u0003Rfb4c#"));
                              return var2;
                           }

                           try {
                              Thread.sleep(50L);
                           } catch (InterruptedException var5) {
                              var5.printStackTrace();
                              break;
                           }

                           var10000 = var1;
                        }
                     }
                  }
               } catch (Exception var6) {
                  var6.printStackTrace();
               }
            }

            return var2;
         }
      });

      try {
         var1 = (Boolean)var2.get();
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("t\u00016\u0005w\u001cdU;X;X")).append(var1).toString());
         return var1;
      } catch (Exception var4) {
         return var1;
      }
   }

   public int writeData(byte[] arg0) {
      if (!this.B) {
         return -1;
      } else {
         boolean var2 = false;
         int var3 = 0;
         boolean var4 = arg0.length > 300;
         int var5 = arg0.length;

         try {
            do {
               int var8 = var5 > this.Y ? this.Y : var5;
               byte[] var6 = new byte[var8];
               System.arraycopy(arg0, var3, var6, 0, var8);
               var5 -= var8;
               var3 += var8;
               this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("a4\u007f2s\u0002w2wfz#x|")).append(arg0.length).append(Packet.ALLATORIxDEMO("6\u0006s\u001brO")).append(var8).append(BuildConfig.ALLATORIxDEMO("6/F)e|")).append(var3).append(Packet.ALLATORIxDEMO("6\u0019s\u0013bO")).append(var5).toString());
               if (this.m) {
                  this.writeBtData(var6);
                  if (var4) {
                     Thread.sleep(10L);
                  }
               } else {
                  this.ALLATORIxDEMO(var6);
               }

               Thread.sleep(10L);
            } while(var5 > 0);

            if (this.m && var4) {
               Thread.sleep(50L);
            }
         } catch (Exception var7) {
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("a4\u007f2s\u0002w2wfs>u#f2\u007f)x|")).append(var7.toString()).toString());
         }

         return var3;
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO() {
      int var1 = -1;
      FutureTask var2 = FutureThreadPool.getInstance().executeTask(new Callable() {
         public Boolean ALLATORIxDEMO() throws Exception {
            boolean var1 = false;
            int var2;
            int var10000 = var2 = BluetoothSppClient.this.P;

            boolean var5;
            while(true) {
               --var2;
               if (var10000 <= 0) {
                  var5 = var1;
                  break;
               }

               var1 = BluetoothSppClient.this.openDevice(var2);
               BluetoothSppClient.this.ALLATORIxDEMO("AclasBluetooth", (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("k)a7@<r0g<$+a->")).append(var1).append(PacketHandler.ALLATORIxDEMO("\u001bSv[CyTTUyUN\u0001\u001a")).append(BluetoothSppClient.this.P).append(BluetoothCtrl.ALLATORIxDEMO("ym\u001aj->")).append(var2).append(PacketHandler.ALLATORIxDEMO("\u001ayv~\u0000")).append(BluetoothSppClient.this.m).toString());
               if (var1) {
                  var5 = var1;
                  break;
               }

               try {
                  Thread.sleep(1000L);
               } catch (Exception var4) {
                  var10000 = var2;
                  continue;
               }

               var10000 = var2;
            }

            return var5;
         }
      });

      try {
         boolean var3;
         var1 = (var3 = (Boolean)var2.get()) ? 0 : -1;
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0017bUu\u001ax\u001bs\u0016b3c\u0001c\u0007sU;X;X")).append(var3).toString());
         return var1;
      } catch (Exception var4) {
         return var1;
      }
   }

   public int close() {
      byte var1 = 0;
      if (this.B) {
         this.B = false;
      }

      this.closeDevice();
      if (this.N != null) {
         this.N.onDisconnect();
      }

      return var1;
   }

   // $FF: synthetic method
   private void i() {
      (new Thread() {
         public void run() {
            try {
               BluetoothCtrl.createBond(BluetoothSppClient.this.c);
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }
      }).start();
   }

   public int type() {
      return 6;
   }

   protected boolean openDevice(int arg0) {
      this.x = this.R = false;
      this.Ha = false;

      try {
         if (VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(this.ca, BuildConfig.ALLATORIxDEMO("w(r4y/rhf#d+\u007f5e/y(8\u0004Z\u0013S\u0012Y\tB\u000eI\u0005Y\bX\u0003U\u0012")) != 0) {
            return false;
         } else {
            if (this.ja == null) {
               this.ja = this.D.getRemoteDevice(this.F);
            }

            UUID var2 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            BluetoothSppClient var10000;
            if (W >= 10) {
               var10000 = this;
               this.k = this.ja.createInsecureRfcommSocketToServiceRecord(var2);
            } else {
               var10000 = this;
               this.k = this.ja.createRfcommSocketToServiceRecord(var2);
            }

            var10000.k.connect();
            this.y = this.getInputStream();
            this.O = this.getOutputStream();
            this.B = true;
            return true;
         }
      } catch (Exception var3) {
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("u\u001ax\u001bs\u0016bUS\u0007dO")).append(var3.toString()).toString());
         this.closeDevice();
         return false;
      }
   }

   protected OutputStream getOutputStream() {
      if (this.k != null) {
         try {
            return this.k.getOutputStream();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }

   public void scanDevices(boolean arg0) {
      this.n = !arg0;
      this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("e%w(R#`/u#e|")).append(arg0).toString());
      if (arg0) {
         this.V.clear();
         this.h();
         this.f.clear();
         this.d = false;
         this.ALLATORIxDEMO();
      } else {
         if (!this.d) {
            this.F();
            this.d = true;
         }

      }
   }

   public BluetoothSppClient(String arg0, Context arg1) {
      this.F = arg0;
      this.ca = arg1;
      this.v = 4;
   }

   public int init(Context arg0, ComDevice.ComDeviceListener arg1) {
      byte var3 = 0;
      this.N = arg1;
      this.ca = arg0;
      this.D = BluetoothAdapter.getDefaultAdapter();
      if (this.D == null) {
         boolean var4 = true;
         return -1;
      } else {
         this.h();
         return var3;
      }
   }

   protected InputStream getInputStream() {
      if (this.k != null) {
         try {
            return this.k.getInputStream();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }

   void ALLATORIxDEMO() {
      this.ca.registerReceiver(this.A, new IntentFilter(Packet.ALLATORIxDEMO("\u0014x\u0011d\u001a\u007f\u00118\u0017z\u0000s\u0001y\u001ab\u001d8\u0014r\u0014f\u0001s\u00078\u0014u\u0001\u007f\u001ax[R<E6Y#S'O*P<X<E=S1")));
      this.ca.registerReceiver(this.ma, new IntentFilter(BuildConfig.ALLATORIxDEMO("'x\"d)\u007f\"8$z3s2y)b.8\"s0\u007f%shw%b/y(8\u0000Y\u0013X\u0002")));
      if (VERSION.SDK_INT < 31 || ActivityCompat.checkSelfPermission(this.ca, Packet.ALLATORIxDEMO("\u0014x\u0011d\u001a\u007f\u00118\u0005s\u0007{\u001ce\u0006\u007f\u001ax[T9C0B:Y!^*E6W;")) == 0) {
         boolean var1 = this.D.startDiscovery();
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, BuildConfig.ALLATORIxDEMO("4s!\u007f5b#d\u0004z3s2y)b.D#u#\u007f0s465b'd2R/e%y0s4o|")).append(var1).toString());
      }
   }

   public interface OnOpenBluetoothListener {
      void onScanFinish();

      void onScanDevices(HashMap var1);

      void onEnableBluetooth(boolean var1, String var2);

      void onPairDevice(boolean var1, String var2);
   }
}
