package com.example.scaler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.aclas.ndklib.BuildConfig;
import com.example.data.St_Dialog06;
import com.example.data.St_PSData;
import com.example.data.St_Plu;
import com.example.io.BlueTooth;
import com.example.io.BluetoothCtrl;
import com.example.io.BluetoothSppClient;
import com.example.io.ComDevice;
import com.example.io.FscBlueTooth;
import com.example.io.SerialPort;
import com.example.io.USBPort;
import com.example.util.AclasLogUtil;
import com.example.util.FutureThreadPool;
import com.example.util.Packet;
import com.example.util.PacketHandler;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

public class AclasScaler {
   private String la = "";
   private final int ba = 100;
   private static final int aa = 13;
   private boolean da = true;
   private <undefinedtype> Da = null;
   private <undefinedtype> Ea = null;
   private static final int Ia = 37;
   private final int Aa = 2;
   private boolean Ba = false;
   private AclasScalerListener Ma = null;
   private final int Ka = 4;
   private final String Ja = "Error";
   private boolean ga = false;
   private static final int Ca = 24;
   private boolean ka = false;
   private static final int ha = 19;
   private final int Fa = 0;
   private static final String ca = "COMMU";
   private ArrayList ma = null;
   private boolean Ga = false;
   private static final String fa = "2.001";
   private static final int ja = 4;
   private static final int Ha = 3;
   private byte[] O = new byte[64];
   private static final int W = 11;
   private AclasBluetoothListener Q = null;
   private static final int r = 17;
   public static final int Err_ReadTimeOut = -4;
   private AclasScalerPSXListener T = null;
   public static boolean m_bFlagHeartClock = false;
   public static final int Err_DisconnectNeed = -8;
   private static final String t = "AclasScale";
   private static final int n = 10;
   private static final int X = 1;
   private final int Y = 1200;
   public static final int Type_FSC = 4;
   public boolean bLogFalg = true;
   private ComDevice.ComDeviceListener v = new ComDevice.ComDeviceListener() {
      public void onConnecting() {
         if (AclasScaler.this.Ma != null) {
         }

      }

      public void onRecv(int arg0, String arg1) {
         if (AclasScaler.this.Q != null) {
            if (arg0 == 0) {
               AclasScaler.this.Q.onSearchBluetooth(arg1);
               return;
            }

            if (arg0 == 1) {
               AclasScaler.this.Q.onSearchFinish();
            }
         }

      }

      public void onDisconnect() {
         if (AclasScaler.this.Ma != null && AclasScaler.this.Ba) {
            AclasScaler.this.Ma.onDisConnected();
         }

      }

      public void onConnectFinished(int arg0) {
         if (AclasScaler.this.Ma != null) {
            if (arg0 == 0) {
               if (AclasScaler.this.Ea == null) {
                  AclasScaler.this.Ea = new Thread(null) {
                     private byte[] E;
                     private int M;
                     private boolean A;
                     private byte[] a;
                     private boolean K;
                     private boolean J;
                     private int F;
                     private AclasScaler.WeightInfo m;
                     private boolean b;
                     private int B;
                     private AclasScaler.WeightInfoNew L;
                     private int D;
                     private boolean l;
                     private final int G;
                     private byte[] H;
                     private byte[] j;
                     private byte[] h;
                     private int i;
                     private final int f;
                     // $FF: synthetic field
                     final AclasScaler d;
                     private St_Dialog06 ALLATORIxDEMO;

                     public int i() {
                        return this.B;
                     }

                     public synchronized void start() {
                        this.J = true;
                        super.start();
                     }

                     public synchronized void F() {
                        this.b = false;
                        this.i = 0;
                     }

                     public synchronized void h() {
                        this.J = false;
                        this.notifyAll();
                        this.interrupt();

                        <undefinedtype> var10000;
                        label19: {
                           try {
                              if (AclasScaler.ALLATORIxDEMO(this.d).type() == 1) {
                                 ((USBPort)AclasScaler.ALLATORIxDEMO(this.d)).doClose();
                              }

                              this.join();
                              AclasScaler.ALLATORIxDEMO(this.d, FutureThreadPool.ALLATORIxDEMO("h{{znvh{{z:tuwt>\u007fp~373737373737373"));
                           } catch (Exception var2) {
                              var10000 = this;
                              var2.printStackTrace();
                              break label19;
                           }

                           var10000 = this;
                        }

                        AclasScaler.ALLATORIxDEMO(var10000.d, FutureThreadPool.ALLATORIxDEMO("l\u007f\u007f~jrl\u007f\u007f~>Ijun737373737373737{tz"));
                        this.A = false;
                        Arrays.fill(AclasScaler.ALLATORIxDEMO(this.d), (byte)0);
                     }

                     // $FF: synthetic method
                     private boolean i(byte[] arg0, int arg1) {
                        boolean var3 = true;

                        int var4;
                        for(int var10000 = var4 = 0; var10000 < arg1; var10000 = var4) {
                           if (arg0[var4] != 0) {
                              var3 = false;
                              return false;
                           }

                           ++var4;
                        }

                        return var3;
                     }

                     // $FF: synthetic method
                     static void ALLATORIxDEMO(Object arg0) {
                        arg0.i();
                     }

                     public void run() {
                        super.run();
                        boolean var1 = false;

                        label36:
                        while(true) {
                           <undefinedtype> var10000 = this;

                           while(var10000.J) {
                              int var4;
                              if ((var4 = this.ALLATORIxDEMO()) > 0) {
                                 this.B += var4;
                              }

                              this.ALLATORIxDEMO();
                              if (!this.J) {
                                 continue label36;
                              }

                              if (!this.ALLATORIxDEMO() && AclasScaler.F(this.d)) {
                                 this.l = true;
                              }

                              try {
                                 sleep(5L);
                              } catch (Exception var3) {
                                 var10000 = this;
                                 var3.printStackTrace();
                                 continue;
                              }

                              var10000 = this;
                           }

                           AclasScaler.ALLATORIxDEMO(this.d, FutureThreadPool.ALLATORIxDEMO("{bwn>h{{z:jrl\u007f\u007f~373737373737373"));
                           return;
                        }
                     }

                     // $FF: synthetic method
                     private synchronized void i() {
                        this.A = false;
                     }

                     public synchronized byte[] ALLATORIxDEMO(int arg0) {
                        return this.ALLATORIxDEMO(arg0, false);
                     }

                     public synchronized boolean ALLATORIxDEMO() {
                        return this.A;
                     }

                     // $FF: synthetic method
                     private int ALLATORIxDEMO() {
                        int var1;
                        if ((var1 = AclasScaler.ALLATORIxDEMO(this.d).readData(this.H, 512, 20)) > 0) {
                           this.M = var1 + this.M < 1024 ? this.M : 0;
                           System.arraycopy(this.H, 0, this.h, this.M, var1);
                           this.M += var1;
                           AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("l\u007f\u007f~Z{j{$")).append(var1).append(FutureThreadPool.ALLATORIxDEMO(">nqn\u007fv$")).append(this.M).append(FutureThreadPool.ALLATORIxDEMO(":z{j{$")).append(AclasScaler.bytesToHexString(this.H, var1)).toString());
                        }

                        return var1;
                     }

                     // $FF: synthetic method
                     {
                        this();
                     }

                     // $FF: synthetic method
                     private boolean ALLATORIxDEMO(byte[] arg0, byte[] arg1, int arg2) {
                        boolean var4 = false;

                        int var5;
                        for(int var10000 = var5 = 0; var10000 < arg2; var10000 = var5) {
                           if (arg0[var5] != arg1[var5]) {
                              return var4;
                           }

                           if (var5 == arg2 - 1) {
                              var4 = true;
                           }

                           ++var5;
                        }

                        return var4;
                     }

                     // $FF: synthetic method
                     private synchronized void ALLATORIxDEMO(byte[] arg0, int arg1) {
                        if (this.i(arg0, arg1)) {
                           AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("nojH{nZ{j{>h{nkhp:wiD\u007flu>v{t$")).append(arg1).toString());
                        } else {
                           if (arg0 != null && arg0.length <= 1024) {
                              int var3 = 0;
                              byte[] var10000;
                              if (this.b) {
                                 if (arg1 + this.i <= 512) {
                                    var10000 = arg0;
                                    var3 = this.i;
                                    this.i += arg1;
                                 } else {
                                    AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("jknL\u007fj^\u007fn\u007f:[hl:nh{V{t$")).append(this.i).append(FutureThreadPool.ALLATORIxDEMO(">t{mWt$")).append(arg1).toString());
                                    this.i = arg1;
                                    var10000 = arg0;
                                 }
                              } else {
                                 this.i = arg1;
                                 var10000 = arg0;
                              }

                              System.arraycopy(var10000, 0, this.E, var3, arg1);
                              this.b = true;
                              AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("nojH{nZ{j{>nqn\u007fv$")).append(this.i).append(FutureThreadPool.ALLATORIxDEMO(">t{mWt$")).append(arg1).append(FutureThreadPool.ALLATORIxDEMO(":z{j{$")).append(AclasScaler.bytesToHexString(arg0, arg1)).toString());
                              this.notify();
                           }

                        }
                     }

                     // $FF: synthetic method
                     private {
                        this.d = arg0;
                        this.f = 1024;
                        this.G = 512;
                        this.B = 0;
                        this.J = false;
                        this.h = new byte[1024];
                        this.H = new byte[512];
                        this.j = new byte[1024];
                        this.a = new byte[1024];
                        this.m = new AclasScaler.WeightInfo();
                        this.L = this.d.new WeightInfoNew();
                        this.D = 0;
                        this.F = 0;
                        this.M = 0;
                        this.b = false;
                        this.K = false;
                        this.E = new byte[1024];
                        this.i = -1;
                        this.A = false;
                        this.l = false;
                        this.ALLATORIxDEMO = new St_Dialog06();
                     }

                     // $FF: synthetic method
                     private boolean ALLATORIxDEMO(byte[] arg0, int arg1) {
                        boolean var3 = false;

                        int var4;
                        for(int var10000 = var4 = 0; var10000 < arg1 - 3; var10000 = var4) {
                           if (arg0[var4] == 103 && arg0[var4 + 1] == 111 && arg0[var4 + 2] == 116 && arg0[var4 + 3] == 111) {
                              var3 = true;
                              AclasScaler.ALLATORIxDEMO(this.d, FutureThreadPool.ALLATORIxDEMO("[tj\u007fl:Kjz{j\u007f>Wq~{"));
                           }

                           ++var4;
                        }

                        return var3;
                     }

                     public synchronized byte[] ALLATORIxDEMO(int arg0, boolean arg1) {
                        this.K = true;
                        byte[] var3 = null;
                        AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjH{nZ{j{>xXv\u007f}L\u007fjolt$")).append(this.b).append(FutureThreadPool.ALLATORIxDEMO(":{tj\u007fl:wV{tL\u007fjolt$")).append(this.i).append(FutureThreadPool.ALLATORIxDEMO(">yr\u007f\u007fh$")).append(arg1).toString());
                        if (arg1) {
                           this.b = false;
                           this.i = 0;
                        }

                        <undefinedtype> var10000;
                        label29: {
                           if (!this.b) {
                              try {
                                 AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("}{nL\u007fj^\u007fn\u007f:i{wn$")).append(arg0).toString());
                                 this.wait((long)arg0);
                                 AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjH{nZ{j{>m\u007fsj:\u007f|j\u007fl:wV{tL\u007fjolt$")).append(this.i).toString());
                                 if (this.i > 0) {
                                    var3 = new byte[this.i];
                                    System.arraycopy(this.E, 0, var3, 0, this.i);
                                    this.b = false;
                                    this.i = 0;
                                 } else {
                                    AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("}{nL\u007fj^\u007fn\u007f:wV{tL\u007fjolt$")).append(this.i).toString());
                                 }
                              } catch (InterruptedException var5) {
                                 var10000 = this;
                                 AclasScaler.i(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjH{nZ{j{$")).append(var5.toString()).toString());
                                 break label29;
                              }
                           } else if (this.i > 0) {
                              var3 = new byte[this.i];
                              System.arraycopy(this.E, 0, var3, 0, this.i);
                              this.b = false;
                              this.i = 0;
                           }

                           var10000 = this;
                        }

                        var10000.K = false;
                        AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjH{nZ{j{>h{nkhp:|H{n$")).append(var3 == null ? FutureThreadPool.ALLATORIxDEMO("tkvr") : String.valueOf(var3.length)).toString());
                        return var3;
                     }

                     public synchronized void ALLATORIxDEMO(boolean arg0) {
                        this.A = arg0;
                     }

                     // $FF: synthetic method
                     private void ALLATORIxDEMO() {
                        int var1 = 3;

                        do {
                           if (!this.J) {
                              return;
                           }

                           if (!this.ALLATORIxDEMO()) {
                              if (this.M <= 0) {
                                 break;
                              }

                              Integer var2 = -1;
                              Integer var3 = -1;
                              AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("J\u007fhm\u007f>")).append(String.valueOf(var1)).append(FutureThreadPool.ALLATORIxDEMO(":i{wn$")).append(this.K).append(FutureThreadPool.ALLATORIxDEMO(":|\u007fxul\u007f>j\u007fhm\u007fV\u007f}h>sR\u007fp}jr$")).append(this.M).append(FutureThreadPool.ALLATORIxDEMO(":$")).append(AclasScaler.bytesToHexString(this.h, this.M)).toString());
                              int var4;
                              if ((var4 = AclasScaler.ALLATORIxDEMO(this.d, this.h, this.M, this.a, this.a.length)) > 2) {
                                 AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("i{wn$")).append(this.K).append(FutureThreadPool.ALLATORIxDEMO(">{xn{h>j\u007fhm\u007fV\u007f}h>v{t$")).append(var4).append(FutureThreadPool.ALLATORIxDEMO(">H{nz{j{$")).append(AclasScaler.bytesToHexString(this.a, var4)).toString());
                                 var2 = AclasScaler.ALLATORIxDEMO(this.d, this.a[var4 - 2]);
                                 var3 = AclasScaler.ALLATORIxDEMO(this.d, this.a[var4 - 1]);
                                 if (var2 > 0 && this.K) {
                                    this.ALLATORIxDEMO(this.h, var2);
                                 }

                                 int var5;
                                 if (var2 + var3 < this.M) {
                                    var5 = this.M - (var2 + var3);

                                    int var6;
                                    for(int var10000 = var6 = 0; var10000 < var5; var10000 = var6) {
                                       byte[] var7 = this.h;
                                       byte[] var10001 = this.h;
                                       int var10003 = var2 + var3;
                                       int var9 = var6;
                                       byte var10 = var10001[var10003 + var6];
                                       ++var6;
                                       var7[var9] = var10;
                                    }

                                    AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("\u007f|j\u007fl:n{li{R{yl:r\u007fxn>v{t$")).append(var5).append(FutureThreadPool.ALLATORIxDEMO(">sWtz\u007ffUkn$")).append(var2).append(FutureThreadPool.ALLATORIxDEMO(":r\u007fxnz{j{$")).append(AclasScaler.bytesToHexString(this.h, var5 + var2)).toString());
                                 }

                                 this.M -= var3 + var2;
                                 if (this.M >= 512) {
                                    AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("7373sR\u007fp}jr>yr\u007f\u007fh$73737r\u007fxn$")).append(this.M).append(FutureThreadPool.ALLATORIxDEMO(":qojSp~{b$")).append(var2).toString());
                                    this.M = 0;
                                 }

                                 if (var3 != 2 && var3 != 1) {
                                    if (var3 < 7 || this.a[0] != 2 || this.a[1] != 85 || this.a[2] != -7 && this.a[2] != 56 && this.a[2] != 58) {
                                       if (this.a[0] == 18 && var3 >= 16) {
                                          if (!AclasScaler.G(this.d)) {
                                             AclasScaler.ALLATORIxDEMO(this.d, true);
                                          }

                                          AclasScaler.ALLATORIxDEMO(this.d, this.a, var4);
                                       } else if (var3 >= 16 && this.a[0] == 2 && this.a[1] == 85 && this.a[2] == -12) {
                                          if (!AclasScaler.G(this.d)) {
                                             AclasScaler.ALLATORIxDEMO(this.d, true);
                                          }

                                          if (AclasScaler.ALLATORIxDEMO(this.d, this.a, AclasScaler.ALLATORIxDEMO(this.d)) && AclasScaler.ALLATORIxDEMO(this.d) != null) {
                                             AclasScaler.ALLATORIxDEMO(this.d).onRcvData(AclasScaler.ALLATORIxDEMO(this.d));
                                          }
                                       } else {
                                          var5 = AclasScaler.ALLATORIxDEMO(this.d, this.a, var4 - 2, this.m);
                                          AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjM{syrjT{m>spV{t$")).append(var4).append(FutureThreadPool.ALLATORIxDEMO(">h{n$")).append(var5).toString());
                                          if (var5 == 0) {
                                             if (AclasScaler.i(this.d) == -1 && this.a[0] == -85) {
                                                AclasScaler.h(this.d, 0);
                                                AclasScaler.ALLATORIxDEMO(this.d, false);
                                             }

                                             if (AclasScaler.ALLATORIxDEMO(this.d) != null) {
                                                this.L.setData(this.m);
                                                AclasScaler.ALLATORIxDEMO(this.d, this.L.isTare);
                                                if (AclasScaler.h(this.d) && this.L.isZeroMode && AclasScaler.ALLATORIxDEMO(this.d).isStable && !AclasScaler.ALLATORIxDEMO(this.d).isZeroMode) {
                                                   AclasScaler.F(this.d, true);
                                                }

                                                AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fjM{syrjT{m>wAxJ{l\u007fXv\u007f}$")).append(AclasScaler.h(this.d)).append(FutureThreadPool.ALLATORIxDEMO(">wAxP\u007f{~L\u007f\u007f~J{l\u007f$")).append(AclasScaler.ALLATORIxDEMO(this.d)).toString());
                                                AclasScaler.ALLATORIxDEMO(this.d).onRcvData(this.L);
                                                AclasScaler.ALLATORIxDEMO(this.d).setData(this.L);
                                             }
                                          } else if (var4 <= 6 || this.a[5] != 80 || this.a[6] != 7) {
                                             if (var4 > 3 && (this.a[2] == 65 || this.a[2] == 84)) {
                                                if (this.l) {
                                                   this.l = false;
                                                }

                                                this.ALLATORIxDEMO(this.a, var4);
                                             } else if (var4 > 15 && this.a[5] == -124 && this.a[6] == 0) {
                                                AclasScaler.ALLATORIxDEMO(this.d, FutureThreadPool.ALLATORIxDEMO("n{li{^\u007fn\u007f:&..*$"), this.a, var4);
                                                if (AclasScaler.ALLATORIxDEMO(this.d) != null && this.ALLATORIxDEMO.parseData(this.a, var4)) {
                                                   AclasScaler.ALLATORIxDEMO(this.d).onRecvMsg(this.ALLATORIxDEMO);
                                                }
                                             } else {
                                                this.ALLATORIxDEMO(this.a, var4);
                                             }
                                          }
                                       }
                                    } else {
                                       if (!AclasScaler.G(this.d)) {
                                          AclasScaler.ALLATORIxDEMO(this.d, true);
                                       }

                                       this.ALLATORIxDEMO(this.a, var4);
                                       AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("j\u007fhm\u007fZ{j{>+>sR\u007fp}jr$")).append(this.M).toString());
                                    }
                                 } else {
                                    this.ALLATORIxDEMO(this.a, var4);
                                    AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("j\u007fhm\u007fZ{j{>*>sR\u007fp}jr$")).append(this.M).toString());
                                 }
                              } else {
                                 if (!this.ALLATORIxDEMO(this.h, this.M)) {
                                    <undefinedtype> var8;
                                    if (this.D != this.M) {
                                       this.D = this.M;
                                       this.F = 0;
                                       var8 = this;
                                       System.arraycopy(this.h, 0, this.j, 0, this.M);
                                    } else {
                                       if (this.ALLATORIxDEMO(this.h, this.j, this.M) && this.F++ > 10) {
                                          AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("J\u007fhm\u007f>")).append(String.valueOf(var1)).append(FutureThreadPool.ALLATORIxDEMO(":i{wn$")).append(this.K).append(FutureThreadPool.ALLATORIxDEMO(">j\u007fhm\u007fV\u007f}h>tkvr:\u007ftz:}v{{l:l\u007fn\u007f\u007fn>~\u007fn\u007f")).append(this.M).append(FutureThreadPool.ALLATORIxDEMO(":wYpnPorvZ{j{$")).append(this.F).toString());
                                          if (this.K) {
                                             this.ALLATORIxDEMO(this.h, this.M);
                                          }

                                          this.D = this.M = 0;
                                       }

                                       var8 = this;
                                    }

                                    AclasScaler.ALLATORIxDEMO(var8.d, (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("J\u007fhm\u007f>")).append(String.valueOf(var1)).append(FutureThreadPool.ALLATORIxDEMO(":n{li{R{yl:porv>{p~>xl\u007f\u007fq2sR\u007fp}jr$")).append(this.M).append(FutureThreadPool.ALLATORIxDEMO(":wYpnPorvZ{j{$")).append(this.F).toString());
                                    return;
                                 }

                                 this.ALLATORIxDEMO(this.h, this.M);
                                 this.ALLATORIxDEMO(true);
                                 this.M = 0;
                              }
                           } else if (this.M > 0) {
                              this.ALLATORIxDEMO(this.h, this.M);
                              this.M = 0;
                           }
                        } while(this.M > 0 && var1-- > 0);

                     }
                  };
                  AclasScaler.this.Ea.start();
               }

               if (AclasScaler.this.Da == null) {
                  AclasScaler.this.Da = new Thread(null) {
                     ArrayList j;
                     ReentrantLock h;
                     private final int i;
                     private int f;
                     // $FF: synthetic field
                     final AclasScaler d;
                     private boolean ALLATORIxDEMO;

                     public void run() {
                        <undefinedtype> var10000 = this;
                        super.run();

                        while(var10000.ALLATORIxDEMO) {
                           this.h();
                           this.ALLATORIxDEMO();

                           try {
                              sleep(35L);
                           } catch (Exception var2) {
                              var10000 = this;
                              continue;
                           }

                           var10000 = this;
                        }

                     }

                     // $FF: synthetic method
                     private {
                        this.d = arg0;
                        this.f = 0;
                        this.ALLATORIxDEMO = false;
                        this.h = new ReentrantLock();
                        this.j = new ArrayList();
                        this.i = 90;
                     }

                     public void h(byte[] arg0) {
                        this.h.lock();
                        this.j.add(0, arg0);
                        this.h.unlock();
                     }

                     // $FF: synthetic method
                     private void i(byte[] arg0) {
                        if (AclasScaler.h(this.d) > 80 && arg0[0] != 65) {
                           AclasScaler.i(this.d, 20);
                        }

                        int var2 = AclasScaler.ALLATORIxDEMO(this.d).writeData(arg0);
                        AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("$.v0p<@8p8$5a7>")).append(var2).append(Packet.ALLATORIxDEMO("6\u0011w\u0001wO")).append(AclasScaler.bytesToHexString(arg0, arg0.length)).toString());
                     }

                     public synchronized void F() {
                        this.ALLATORIxDEMO = false;
                        this.i();
                        this.interrupt();

                        try {
                           this.join();
                        } catch (Exception var2) {
                        }
                     }

                     // $FF: synthetic method
                     private void h() {
                        byte[] var1;
                        if ((var1 = this.ALLATORIxDEMO()) != null) {
                           this.i(var1);
                        }

                     }

                     public synchronized void start() {
                        this.ALLATORIxDEMO = true;
                        super.start();
                     }

                     // $FF: synthetic method
                     {
                        this();
                     }

                     // $FF: synthetic method
                     private byte[] ALLATORIxDEMO() {
                        byte[] var1 = null;
                        this.h.lock();
                        if (this.j.size() > 0) {
                           var1 = (byte[])this.j.get(0);
                           this.j.remove(0);
                        }

                        this.h.unlock();
                        return var1;
                     }

                     // $FF: synthetic method
                     private void i() {
                        this.h.lock();
                        this.j.clear();
                        this.h.unlock();
                     }

                     public void ALLATORIxDEMO(byte[] arg0) {
                        this.h.lock();
                        this.j.add(arg0);
                        AclasScaler.ALLATORIxDEMO(this.d, (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("ye)t<j=@8p8>")).append(AclasScaler.bytesToHexString(arg0, arg0.length)).toString());
                        this.h.unlock();
                     }

                     // $FF: synthetic method
                     private void ALLATORIxDEMO() {
                        if (AclasScaler.m_bFlagHeartClock) {
                           int var1 = 0;
                           if (AclasScaler.ALLATORIxDEMO(this.d) != null) {
                              var1 = AclasScaler.ALLATORIxDEMO(this.d).i();
                           }

                           if (var1 == this.f) {
                              if (!AclasScaler.i(this.d)) {
                                 AclasScaler.ALLATORIxDEMO(this.d);
                                 if (AclasScaler.h(this.d) == 90) {
                                    if (AclasScaler.F(this.d) != 3) {
                                       this.i(this.d.c);
                                       AclasScaler.ALLATORIxDEMO(this.d).ALLATORIxDEMO(100, true);
                                       return;
                                    }
                                 } else if (AclasScaler.h(this.d) > 100) {
                                    AclasScaler.i(this.d, true);
                                    AclasScaler.ALLATORIxDEMO(this.d, Packet.ALLATORIxDEMO("U\u0014xUx\u001abUd\u0010w\u00116\u0014x\fb\u001d\u007f\u001bqUS\u0007d*R\u001ce\u0016y\u001bx\u0010u\u0001"));
                                    if (AclasScaler.ALLATORIxDEMO(this.d) != null && AclasScaler.F(this.d) != 3) {
                                       AclasScaler.ALLATORIxDEMO(this.d).onError(-7, BluetoothCtrl.ALLATORIxDEMO("G8jyj6pyv<e=$8j p1m7c"));
                                       return;
                                    }
                                 }
                              }
                           } else {
                              this.f = var1;
                              AclasScaler.i(this.d, false);
                              AclasScaler.ALLATORIxDEMO((AclasScaler)this.d, (int)0);
                           }

                        }
                     }
                  };
                  AclasScaler.this.Da.start();
               }

               AclasScaler.this.h(PacketHandler.ALLATORIxDEMO("UUyTTU_XN}SUSHR^^\u001bYI_ZN^\u001aORI_Z^\u0016\u0017"));
               return;
            }

            if (arg0 == 4) {
               AclasScaler.this.u.sendEmptyMessage(2);
               return;
            }

            if (arg0 == 8) {
               AclasScaler.this.Ma.onError(-8, FutureThreadPool.ALLATORIxDEMO("omx>xluu\u007fp6>yrum\u007f>~{lwy{"));
               AclasScaler.this.u.sendEmptyMessage(3);
               return;
            }

            if (arg0 == 2) {
               if (AclasScaler.this.Ma != null) {
                  AclasScaler.this.Ma.onConnected();
                  return;
               }
            } else {
               AclasScaler.this.Ma.onError(-2, (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("UUyTTU_XN}SUSHR^^\u001b\u007fIH\u001bYT^^\u0000")).append(arg0).toString());
            }
         }

      }
   };
   private static final int y = 28;
   private boolean s = false;
   private static final int x = 23;
   private boolean S = false;
   private boolean w = false;
   private static final int o = 29;
   public static final int Err_Disconnect = -7;
   private final int Z = 1;
   private int p = -1;
   private static final int R = 36;
   private static final int q = 22;
   private AclasDialogListener V = null;
   public static final int Type_SerialPort = 0;
   public static final int Err_Device = -1;
   private static final int U = 40;
   private static final int z = 21;
   private boolean P = false;
   Handler u = new Handler(Looper.getMainLooper()) {
      public void handleMessage(Message arg0) {
         switch (arg0.what) {
            case 0:
               while(false) {
               }

               AclasScaler.this.AclasReadTareValue();
               return;
            case 1:
               if (AclasScaler.this.Ea != null) {
                  null.ALLATORIxDEMO(AclasScaler.this.Ea);
               }

               AclasScaler.this.Ga = false;
               return;
            case 2:
               AclasScaler.this.h((new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("v{p~r\u007fl:ki|:n\u007flwwimsqt>h{yqtp\u007f}n$")).append(AclasScaler.this.j).toString());
               AclasScaler.this.i(AclasScaler.this.j);
               return;
            case 3:
               AclasScaler.this.h(PacketHandler.ALLATORIxDEMO("S[U^W_I\u001au_^^xVTI^\u001a_U\u001b{XVZI\u007fSHYTTU_XN"));
               AclasScaler.this.AclasDisconnect();
            default:
         }
      }
   };
   public static final int Err_Connect = -2;
   private static final int N = 18;
   private String k = "";
   private static final int C = 1;
   final byte[] c = new byte[]{65, 67, 76, 65, 83};
   private int g = 0;
   private static final int e = 25;
   private static final int I = 51;
   public static final int Err_ReadData = -3;
   private boolean E = false;
   private static final int M = 5;
   private boolean A = false;
   private final int a = 3;
   private static final int K = 0;
   private static final int J = 38;
   private static final int F = 16;
   private WeightInfoNew m = new WeightInfoNew();
   private ComDevice b = null;
   public static final int Type_BLE = 3;
   final byte[] B = new byte[]{3, 5, 1, 4, 12, 4, 3, 4, 1, 4};
   private static final int L = 2;
   private static final byte[] D = new byte[]{68, 17, 0, 0, 0, 0, -17, 85, 58, 0, 0, 6, 107};
   private int l = -1;
   private SharedPreferences G = null;
   private St_PSData H = new St_PSData();
   public static final int Err_Aclas = -6;
   private String j = "";
   private static final int h = 27;
   private static final int i = 46;
   public static final int Type_BlueTooth = 2;
   private static final int f = 12;
   public static final int Err_WriteData = -5;
   public static final int Type_USB = 1;
   private static final int d = 20;
   private static final String ALLATORIxDEMO = "KEYPWD";

   // $FF: synthetic method
   private boolean h(int arg0, byte[] arg1) {
      boolean var3 = false;
      byte[] var4;
      if ((var4 = this.ALLATORIxDEMO(arg0, arg1)) != null && arg0 != 4) {
         var3 = var4[0] == 2;
      }

      return var3;
   }

   public boolean AclasZero() {
      boolean var1 = false;
      if (this.Da != null && this.Ea != null) {
         AclasScaler var10000;
         label50: {
            byte[] var2;
            if (this.p != -1 && this.b.type() != 6) {
               this.sendDataToDevice(12, (byte[])null);
               if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && var2.length > 6 && var2[5] == -128 && var2[6] == 14) {
                  var1 = true;
               }
            } else {
               byte[] var3;
               if (this.b.type() == 7) {
                  var2 = new byte[]{60, 90, 75, 62, 9};
                  var10000 = this;
                  this.writeDatas(var2);
                  var3 = this.Ea.ALLATORIxDEMO(1200);
                  var1 = true;
                  break label50;
               }

               var2 = new byte[]{-3};
               this.writeDatas(var2);
               if ((var3 = this.Ea.ALLATORIxDEMO(1200)) != null) {
                  if (var3.length > 6 && var3[5] == -128 && var3[6] == 14) {
                     var1 = true;
                  }

                  this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("W\u0016z\u0014e/s\u0007yO")).append(bytesToHexString(var3, var3.length)).toString());
               } else {
                  var1 = true;
               }
            }

            var10000 = this;
         }

         if (var10000.p == 2) {
            this.u.sendEmptyMessage(0);
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private String m() {
      return this.ALLATORIxDEMO("COMMU");
   }

   public boolean AclasReadPSData(St_PSData arg0) {
      boolean var2 = false;
      if (!this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("iXVZ_\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001aKVN\u001b"));
         return var2;
      } else {
         if (this.Da != null && this.Ea != null) {
            if (arg0 != null) {
               this.Ea.ALLATORIxDEMO(true);
            }

            byte[] var3 = new byte[]{85, -12, 0, 0, 9, -82};
            if ((var2 = this.i(0)) && (var2 = this.i(1))) {
               if (arg0 != null) {
                  byte[] var4;
                  if ((var4 = this.ALLATORIxDEMO(4, var3)) != null && var4.length > 10) {
                     this.ALLATORIxDEMO(var4, arg0);
                  }
               } else {
                  this.writeDatas(var3);
                  var2 = true;
                  this.Ea.ALLATORIxDEMO(1200);
               }

               var2 = this.i(2);
            }

            if (arg0 != null) {
               this.Ea.ALLATORIxDEMO(false);
            }
         }

         return var2;
      }
   }

   // $FF: synthetic method
   private void F(String arg0) {
      this.ALLATORIxDEMO("COMMU", arg0);
   }

   // $FF: synthetic method
   static ComDevice ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.b;
   }

   // $FF: synthetic method
   private native byte[] parseHecr(byte[] var1, int var2, Integer var3, Integer var4);

   // $FF: synthetic method
   private byte ALLATORIxDEMO(byte[] arg0, int arg1) {
      return this.ALLATORIxDEMO(arg0, 0, arg1);
   }

   // $FF: synthetic method
   static int ALLATORIxDEMO(AclasScaler arg0, byte[] arg1, int arg2, byte[] arg3, int arg4) {
      return arg0.parseHecrNew(arg1, arg2, arg3, arg4);
   }

   // $FF: synthetic method
   static void i(AclasScaler arg0, String arg1) {
      arg0.ALLATORIxDEMO(arg1);
   }

   // $FF: synthetic method
   private boolean i(int arg0, byte[] arg1) {
      boolean var3 = false;
      if (!this.P) {
         var3 = this.ALLATORIxDEMO(true);
      }

      var3 = this.ALLATORIxDEMO(arg0, arg1);
      if (!this.P) {
         this.ALLATORIxDEMO(false);
      }

      return var3;
   }

   // $FF: synthetic method
   static WeightInfoNew ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.m;
   }

   public boolean AclasGetScaleStatus(ScaleStatus arg0) {
      if (this.AclasIsPSX()) {
         this.h(Packet.ALLATORIxDEMO("%E-6\u0011yUx\u001abUe\u0000f\u0005y\u0007bUb\u001d\u007f\u00066\u0013c\u001bu\u0001\u007f\u001axT"));
         return false;
      } else {
         boolean var2 = false;
         if (this.Da != null && this.Ea != null) {
            this.sendDataToDevice(1, (byte[])null);
            byte[] var3;
            OPBoxInfo var4;
            if ((var3 = this.Ea.ALLATORIxDEMO(1200)) != null && (var4 = this.getOpStatus(var3)) != null && arg0 != null) {
               var2 = true;
               arg0.bHardSw = var4.hardSwFlag == 1;
               arg0.iRange = var4.rangF;
               arg0.iPrecision = this.ALLATORIxDEMO(var4.precisionF);
               arg0.iProtocol = var4.iProtocolType;
               arg0.iGravity = var4.gravity;
               arg0.iAdZero = var4.iAdZero;
               arg0.iAdFull = var4.iAdFull;
               this.p = var4.iProtocolType;
               this.ALLATORIxDEMO(false);
            }
         }

         return var2;
      }
   }

   // $FF: synthetic method
   private native int openFileUpdate(byte[] var1, int var2);

   // $FF: synthetic method
   private String G() {
      String var1 = "Error";
      byte[] var2 = new byte[]{1};
      this.ALLATORIxDEMO();
      this.sendDataToDevice(24, var2);
      int var3 = 4;
      boolean var4 = false;

      do {
         byte[] var5;
         if ((var5 = this.Ea.ALLATORIxDEMO(1200)) != null && var5.length >= 13) {
            int var6;
            int var10000 = var6 = 0;

            boolean var10;
            while(true) {
               if (var10000 >= var5.length - 7) {
                  var10 = var4;
                  break;
               }

               if (var5[var6] == 85 && var5[var6 + 1] == 56 && var5[var6 + 2] == 0 && var5[var6 + 3] == 0 && var5[var6 + 4] == 7) {
                  byte[] var7 = new byte[3];
                  System.arraycopy(var5, var6 + 5, var7, 0, 3);

                  try {
                     var1 = new String(var7, PacketHandler.ALLATORIxDEMO("oo|\u0016\u0002"));
                     var4 = true;
                  } catch (Exception var9) {
                     var10 = var4;
                     this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0007s\u0014r%E-@\u0010d\u0006\u007f\u001axUS\u0007d\u001adO")).append(var9.toString()).toString());
                     break;
                  }

                  var10 = var4;
                  break;
               }

               ++var6;
               var10000 = var6;
            }

            if (var10) {
               return var1;
            }
         }

         --var3;
      } while(var3 > 0);

      return var1;
   }

   public boolean AclasReset() {
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
         return false;
      } else {
         boolean var1 = false;
         if (this.Da != null && this.Ea != null) {
            this.sendDataToDevice(16, (byte[])null);
            byte[] var2;
            if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && var2.length > 6 && var2[5] == -128 && var2[6] == 14) {
               var1 = true;
            }
         }

         return var1;
      }
   }

   // $FF: synthetic method
   private byte ALLATORIxDEMO(int arg0) {
      boolean var2 = false;
      return (byte)(arg0 / 10 * 16 + arg0 % 10);
   }

   public boolean AclasSendPluData(final ArrayList arg0) {
      boolean var2 = false;
      if (arg0 != null && this.Da != null && this.Ea != null) {
         FutureTask var3 = FutureThreadPool.getInstance().executeTask(new Callable() {
            public Boolean ALLATORIxDEMO() throws Exception {
               Boolean var1 = false;
               if (AclasScaler.this.h(0, (byte[])null) && AclasScaler.this.h(1, (byte[])null)) {
                  int var2 = arg0.size();
                  boolean var3 = true;
                  int var4;
                  int var10000 = var4 = 0;

                  boolean var7;
                  while(true) {
                     if (var10000 >= var2) {
                        var7 = var3;
                        break;
                     }

                     St_Plu var5 = (St_Plu)arg0.get(var4);
                     byte[] var6;
                     if ((var6 = AclasScaler.this.ALLATORIxDEMO((byte)119, (byte)-7, var5.m_iIndex, var5.m_iPrice)) != null) {
                        if (!AclasScaler.this.h(3, var6)) {
                           var7 = var3 = false;
                           AclasScaler.this.h((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("E:h8w\na7`\th,@8p8$\u001cv+k+%x%x%x%x$0>")).append(var4).append(BluetoothCtrl.ALLATORIxDEMO("$0j=a!>")).append(var5.m_iIndex).toString());
                           break;
                        }

                        AclasScaler.this.h((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("\u0018g5e*W<j=T5q\u001de-ey$0j=a!>")).append(var5.m_iIndex).append(BluetoothCtrl.ALLATORIxDEMO("$)v0g<>")).append(var5.m_iPrice).toString());
                     }

                     ++var4;
                     var10000 = var4;
                  }

                  var1 = var7;
                  AclasScaler.this.h(2, (byte[])null);
               }

               return var1;
            }
         });

         try {
            var2 = (Boolean)var3.get();
            return var2;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return var2;
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO(int arg0, byte[] arg1, byte[] arg2) {
      byte[] var4 = null;
      this.sendDataToDevice(arg0, arg2);
      byte[] var5;
      int var6;
      if ((var5 = this.Ea.ALLATORIxDEMO(1200)) != null && var5.length > 12 && var5[5] == arg1[0] && var5[6] == arg1[1] && (var6 = this.ALLATORIxDEMO(var5[11])) > 0 && var5.length >= 15 + var6) {
         var4 = new byte[var6];
         System.arraycopy(var5, 13, var4, 0, var6);
      }

      return var4;
   }

   // $FF: synthetic method
   static boolean G(AclasScaler arg0) {
      return arg0.ga;
   }

   // $FF: synthetic method
   static int F(AclasScaler arg0) {
      return arg0.l;
   }

   // $FF: synthetic method
   private boolean i(byte[] arg0) {
      return this.ALLATORIxDEMO((byte[])arg0, (byte[])null);
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1, int arg2) {
      int var4 = 0;
      int var5;
      if (arg0.length >= arg1 + arg2 && arg1 >= 0 && arg2 > 0) {
         for(int var10000 = var5 = 0; var10000 < arg2; var10000 = var5) {
            var4 <<= 8;
            int var6 = this.ALLATORIxDEMO(arg0[arg1 + var5]);
            ++var5;
            var4 += var6 % 256;
         }
      }

      return var4;
   }

   // $FF: synthetic method
   private void h(String arg0) {
      if (this.bLogFalg) {
         AclasLogUtil.info("AclasScale", arg0);
      }

   }

   // $FF: synthetic method
   static void ALLATORIxDEMO(AclasScaler arg0, byte[] arg1, int arg2) {
      arg0.ALLATORIxDEMO(arg1, arg2);
   }

   // $FF: synthetic method
   private native int parseHecrNew(byte[] var1, int var2, byte[] var3, int var4);

   // $FF: synthetic method
   static boolean F(AclasScaler arg0, boolean arg1) {
      return arg0.A = arg1;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(Context arg0) {
      if (arg0 != null) {
         this.G = arg0.getSharedPreferences("AclasScale", 0);
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(byte[] arg0, int arg1) {
      if (this.T != null && this.ALLATORIxDEMO(arg0)) {
         System.arraycopy(arg0, 0, this.O, 0, arg1);
         int var3 = 2;
         int var4;
         int var5 = (var4 = this.ALLATORIxDEMO(arg0[var3])) / 16;
         ++var3;
         int var6 = var4 % 16;
         int var10005 = var3;
         var3 += 4;
         double var7 = this.ALLATORIxDEMO(arg0, var10005, 4, var5);
         int var10003 = var3;
         var3 += 4;
         double var9 = this.ALLATORIxDEMO(arg0, var10003, 4, var6);
         byte var11 = arg0[var3++];
         int var12 = -1;
         if (var11 != -1) {
            var12 = this.ALLATORIxDEMO((byte)(var11 & 127));
         }

         this.H.iDotAmount = var6;
         this.H.iDotPrice = var5;
         this.H.dPrice = var7;
         this.H.dAmount = var9;
         this.H.iKeyVal = var12;
         String var13 = String.format(Packet.ALLATORIxDEMO("\u0005d\u001cu\u0010,P8FpUw\u0018y\u0000x\u0001,P8FpU}\u0010oO3\u0011"), var7, var9, var12);
         this.h(var13);
         this.T.onRcvData(this.H);
      }
   }

   public byte[] getHardInfo() {
      String var1 = PacketHandler.ALLATORIxDEMO("}^Ns[I^rT]U");
      return this.sendAndRcv(var1.getBytes());
   }

   public byte[] AclasReadFirmwareName() {
      Object var1 = null;
      String var2 = Packet.ALLATORIxDEMO("e\u001ap\u0001a\u0014d\u0010\u007f\u001bp\u001a");
      return this.sendAndRcv(var2.getBytes());
   }

   static {
      System.loadLibrary(PacketHandler.ALLATORIxDEMO("{XVZIhYZV^vRX"));
   }

   // $FF: synthetic method
   private native int openFileUpdateUart(byte[] var1, int var2);

   // $FF: synthetic method
   static int h(AclasScaler arg0, int arg1) {
      return arg0.p = arg1;
   }

   public boolean AclasGetHardSWFlag() {
      boolean var1 = false;
      if (this.Da != null && this.Ea != null) {
         this.sendDataToDevice(1, (byte[])null);
         byte[] var2;
         OPBoxInfo var3;
         if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && (var3 = this.getOpStatus(var2)) != null) {
            var1 = var3.hardSwFlag > 0;
            Log.d("AclasScale", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("q\u0010b:f&b\u0014b\u0000eO")).append(var1).toString());
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private native WeightInfo getWeight(byte[] var1);

   // $FF: synthetic method
   private boolean i() {
      boolean var1 = false;
      if (this.w) {
         return true;
      } else {
         AclasScaler var10000;
         if (this.i(this.c)) {
            var10000 = this;
         } else {
            var10000 = this;
            this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("zYW[HyS_XQ\u000b\u001a^HIUI\u001b\u001a\u001b\u001a\u001b\u001a\u001b"));
         }

         if (var10000.i(this.B)) {
            var1 = true;
            return true;
         } else {
            this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("W\u0016z\u0014e6~\u0010u\u001e'Us\u0007d\u001adT7T7T7T"));
            return var1;
         }
      }
   }

   // $FF: synthetic method
   private String e() {
      String var1 = PacketHandler.ALLATORIxDEMO("BCvw_^srVWHI");
      return (new SimpleDateFormat(var1, Locale.getDefault())).format(new Date());
   }

   public void startScanBluetooth(boolean arg0) {
      if (this.b != null) {
         if (this.b.type() == 6) {
            ((BluetoothSppClient)this.b).scanDevices(arg0);
            return;
         }

         if (this.b.type() == 7) {
            ((FscBlueTooth)this.b).scanDevices(arg0);
         }
      }

   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1) {
      byte var3 = -1;

      int var4;
      for(int var10000 = var4 = 0; var10000 < arg0.length - 9; var10000 = var4) {
         if (arg0[var4 + 0] == 85 && arg0[var4 + 1] == -7 && arg0[var4 + 2] == 1) {
            return var4;
         }

         ++var4;
      }

      return var3;
   }

   // $FF: synthetic method
   private native void sendDataToDevice(int var1, byte[] var2);

   // $FF: synthetic method
   private int h(String arg0) {
      int var2 = this.AclasGetHardSWFlag() ? 0 : -1;
      if (var2 != 0 && arg0 != null && !arg0.isEmpty()) {
         if (this.checkPasswardNew(arg0.getBytes(), arg0.length(), this.ALLATORIxDEMO("")) == 3) {
            String var3;
            if ((var3 = this.F()) != null) {
               if (var3.contains(arg0)) {
                  var2 = -3;
               } else {
                  this.i((new StringBuilder()).insert(0, var3).append(arg0).toString());
                  var2 = 0;
               }
            } else {
               var2 = -2;
            }
         } else {
            var2 = -4;
         }
      }

      return var2;
   }

   // $FF: synthetic method
   static int h(AclasScaler arg0) {
      return arg0.g;
   }

   public ArrayList getCommList() {
      AclasScaler var10000;
      if (this.ma == null) {
         this.ma = new ArrayList();
         var10000 = this;
      } else {
         this.ma.clear();
         var10000 = this;
      }

      int var1 = var10000.getCommList(this.ma);
      this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("q\u0010b6y\u0018{9\u007f\u0006bO")).append(var1).toString());
      return this.ma;
   }

   public void setDlgListener(AclasDialogListener arg0) {
      this.V = arg0;
   }

   // $FF: synthetic method
   private double ALLATORIxDEMO(byte[] arg0, int arg1, int arg2, int arg3) {
      double var5 = 0.0;
      int var7;
      if ((var7 = this.ALLATORIxDEMO(arg0, arg1, arg2)) > 0) {
         var5 = (double)var7 * Math.pow(10.0, (double)(arg3 * -1));
      }

      return var5;
   }

   // $FF: synthetic method
   private boolean h(int arg0) {
      return this.ALLATORIxDEMO(arg0, (byte[])null);
   }

   public static String bytesToHexString(byte[] arg0, int arg1) {
      return bytesToHexString(arg0, 0, arg1);
   }

   public int getCommList(List arg0) {
      if (this.b != null) {
         switch (this.b.type()) {
            case 0:
               while(false) {
               }

               return ((SerialPort)this.b).getDevList(arg0);
            case 1:
               return ((USBPort)this.b).getDevList(arg0);
            case 2:
            case 4:
            case 5:
            default:
               break;
            case 3:
               return ((BlueTooth)this.b).getDevList(arg0);
            case 6:
               BluetoothSppClient var10000 = (BluetoothSppClient)this.b;
               this.h(PacketHandler.ALLATORIxDEMO("]^NxUVWwSHN\u001bxW_"));
               return var10000.getDevList(arg0);
            case 7:
               return ((FscBlueTooth)this.b).getDevList(arg0);
         }
      }

      return 0;
   }

   // $FF: synthetic method
   private String F() {
      return this.ALLATORIxDEMO("KEYPWD");
   }

   public String AclasReadPSXType() {
      String var1 = "Error";
      this.writeDatas(D);
      int var2 = 3;
      boolean var3 = false;

      do {
         byte[] var4;
         if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null && var4.length > 11) {
            int var5 = var4.length;
            int var6;
            int var10000 = var6 = 0;

            boolean var7;
            while(true) {
               if (var10000 >= var5 - 11) {
                  var7 = var3;
                  break;
               }

               if (var4[var6] == 85 && var4[var6 + 1] == 58 && var4[var6 + 2] == 0 && var4[var6 + 3] == 0 && var4[var6 + 4] == 6) {
                  var1 = new String(var4, var6 + 5, 6);
                  var7 = var3 = true;
                  this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("4u\u0019w\u0006D\u0010w\u0011F&N!o\u0005sU\u007f\u001br\u0010nO")).append(3 - var2).append(PacketHandler.ALLATORIxDEMO("\u001aR\u0000")).append(var6).append(Packet.ALLATORIxDEMO("6!o\u0005sO")).append(var1).toString());
                  break;
               }

               ++var6;
               var10000 = var6;
            }

            if (var7) {
               return var1;
            }
         }

         --var2;
      } while(var2 > 0);

      return var1;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(byte[] arg0, St_PSData arg1) {
      boolean var3 = false;
      if (arg0 != null && arg0.length >= 16 && arg0[0] == 2 && arg0[2] == -12 && arg1 != null) {
         arg1.iKeyVal = -1;
         arg1.iDotPrice = 2;
         int var4 = 7;
         byte var10006 = (byte)var4;
         var4 += 4;
         arg1.dPrice = this.ALLATORIxDEMO(arg0, var10006, 4, 2);
         arg1.dAmount = this.ALLATORIxDEMO(arg0, var4, 4, 2);
         arg1.iDotAmount = 2;
         var3 = true;
      }

      return var3;
   }

   // $FF: synthetic method
   private byte ALLATORIxDEMO(byte[] arg0, int arg1, int arg2) {
      byte var4 = 0;
      if (arg0 != null) {
         int var5 = arg0.length < arg2 ? arg0.length : arg2;
         int var6 = 0;

         int var7;
         for(int var10000 = var7 = 0; var10000 < var5; var10000 = var7) {
            if ((var6 += this.ALLATORIxDEMO(arg0[var7 + arg1])) > 255) {
               var6 %= 256;
            }

            ++var7;
         }

         var4 = (byte)(var6 = 256 - var6);
      }

      return var4;
   }

   // $FF: synthetic method
   static boolean ALLATORIxDEMO(AclasScaler arg0, byte[] arg1, St_PSData arg2) {
      return arg0.ALLATORIxDEMO(arg1, arg2);
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte arg0) {
      int var2 = 0;
      byte var3 = 1;
      if ((arg0 & 128) != 0) {
         var2 += var3 << 7;
      }

      return var2 + (arg0 & 127);
   }

   // $FF: synthetic method
   static int i(AclasScaler arg0) {
      return arg0.p;
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO(byte arg0, byte arg1, int arg2, int arg3) {
      byte[] var5 = null;
      if (arg2 >= 0) {
         int var6 = arg0 == 85 ? 6 : 10;
         var5 = new byte[var6];
         int var7 = 0;
         var5[var7++] = arg0;
         var5[var7++] = arg1;
         int var8 = 220 + arg2 * 4;
         int var10003 = var7;
         int var10004 = var8 / 256;
         ++var7;
         var5[var10003] = (byte)var10004;
         int var10002 = var7;
         var10003 = var8 % 256;
         ++var7;
         var5[var10002] = (byte)var10003;
         var5[var7++] = 4;
         int var10001;
         if (arg0 == 119) {
            var10004 = var7;
            double var13 = (double)arg3;
            ++var7;
            var5[var10004] = (byte)((int)(var13 / Math.pow(2.0, 24.0)));
            var10003 = var7;
            double var12 = (double)arg3;
            ++var7;
            var5[var10003] = (byte)((int)(var12 / Math.pow(2.0, 16.0)));
            var10002 = var7;
            double var11 = (double)arg3;
            ++var7;
            var5[var10002] = (byte)((int)(var11 / Math.pow(2.0, 8.0)));
            var10001 = var7;
            double var9 = (double)arg3;
            ++var7;
            var5[var10001] = (byte)((int)(var9 % Math.pow(2.0, 8.0)));
         }

         var10001 = var7;
         byte var10 = this.ALLATORIxDEMO(var5, var7);
         ++var7;
         var5[var10001] = var10;
      }

      return var5;
   }

   // $FF: synthetic method
   static int i(AclasScaler arg0, int arg1) {
      return arg0.g -= arg1;
   }

   public boolean AclasIsPSX() {
      if (!this.ga) {
         this.AclasReadId();
      }

      return this.E;
   }

   // $FF: synthetic method
   private int i(String arg0) {
      boolean var2 = true;
      AclasLogUtil.StartThread();
      AclasScaler var10000;
      int var3;
      if ((this.b.isReady() && this.b.type() != 7 ? (var3 = 0) : (var3 = this.b.open(arg0))) == 0) {
         if (this.b.type() != 3 && this.b.type() != 6 && this.b.type() != 7) {
            var10000 = this;
            this.w = false;
         } else {
            var10000 = this;
            this.w = true;
         }

         if (!var10000.i()) {
            var3 = -6;
         }
      }

      if (this.Ga) {
         var3 = 1;
      }

      label50: {
         if (var3 != 0 && var3 != 1) {
            this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("XUUT^YO~^LRY^\u0000")).append(arg0).append(Packet.ALLATORIxDEMO("Ud\u0010bO")).append(var3).append(PacketHandler.ALLATORIxDEMO("\u001aLSWV\u001b{XVZI\u007fSHYTTU_XN")).toString());
            this.AclasDisconnect();
         } else {
            this.Ba = true;
            if (this.Ma != null) {
               var10000 = this;
               this.Ma.onConnected();
               break label50;
            }
         }

         var10000 = this;
      }

      var10000.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("X;4u\u0019w\u0006U\u001ax\u001bs\u0016bO")).append(arg0).append(PacketHandler.ALLATORIxDEMO("\u001aI_O\u0000")).append(var3).toString());
      return var3;
   }

   // $FF: synthetic method
   private native int closeFileUpdate();

   public boolean AclasSetPreTare(int arg0) {
      if (this.AclasIsPSX()) {
         this.h(Packet.ALLATORIxDEMO("%E-6\u0011yUx\u001abUe\u0000f\u0005y\u0007bUb\u001d\u007f\u00066\u0013c\u001bu\u0001\u007f\u001axT"));
         return false;
      } else {
         boolean var2 = false;
         if (this.Da != null && this.Ea != null) {
            boolean var3 = this.S;
            if (this.m.unit.equalsIgnoreCase(PacketHandler.ALLATORIxDEMO("t`"))) {
               arg0 *= 10;
            }

            byte[] var4 = new byte[]{(byte)(arg0 >> 8), (byte)arg0, (byte)(arg0 >> 16)};
            this.sendDataToDevice(27, var4);
            byte[] var5;
            if ((var5 = this.Ea.ALLATORIxDEMO(1200)) != null) {
               if (var5.length > 6 && var5[5] == -128 && var5[6] == 14) {
                  var2 = true;
               }

               if (var3 && arg0 > 0) {
                  this.A = true;
               }
            }
         }

         return var2;
      }
   }

   // $FF: synthetic method
   private void h() {
      byte[] var1 = new byte[]{68, 17, 0, 0, 0, 0, -17, 119, -7, 0, 0, 4, 0, 0, 0, 0, -116};
      this.writeDatas(var1);
      this.Ea.ALLATORIxDEMO(40);
      this.Ea.ALLATORIxDEMO(40);
   }

   public void AclasSetPSXType(boolean arg0) {
      this.ga = false;
      this.ALLATORIxDEMO(arg0);
   }

   // $FF: synthetic method
   static int ALLATORIxDEMO(AclasScaler arg0, byte[] arg1, int arg2, WeightInfo arg3) {
      return arg0.getWeightNew(arg1, arg2, arg3);
   }

   public int AclasConnect(String arg0, int arg1, int arg2) {
      int var4 = -1;
      if (this.b != null && this.b.type() == 0) {
         String var5 = (new StringBuilder()).insert(0, arg0).append(Packet.ALLATORIxDEMO("Y")).append(String.valueOf(arg1)).append(PacketHandler.ALLATORIxDEMO("\u0016")).append(String.valueOf(arg2)).toString();
         var4 = this.i(var5);
      }

      return var4;
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO(String arg0) {
      String var2 = null;
      if (this.G != null) {
         var2 = this.G.getString(arg0, "");
      }

      return var2;
   }

   // $FF: synthetic method
   static byte[] ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.O;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, byte[] arg1) {
      String var3 = bytesToHexString(arg1, arg1.length);
      this.h((new StringBuilder()).insert(0, arg0).append(Packet.ALLATORIxDEMO("6O")).append(var3).toString());
   }

   // $FF: synthetic method
   static boolean F(AclasScaler arg0) {
      return arg0.ALLATORIxDEMO();
   }

   public void AclasSetMulTare(boolean arg0) {
      if (this.Da != null && this.Ea != null) {
         byte[] var2;
         (var2 = new byte[1])[0] = (byte)(arg0 ? 1 : 0);
         this.sendDataToDevice(36, var2);
         this.Ea.ALLATORIxDEMO(500);
         var2[0] = 32;
         this.sendDataToDevice(36, var2);
         this.Ea.ALLATORIxDEMO(500);
      }

   }

   public int AclasGetRange() {
      int var1 = -1;
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
         return -1;
      } else {
         if (this.Da != null && this.Ea != null) {
            this.sendDataToDevice(1, (byte[])null);
            byte[] var2;
            OPBoxInfo var3;
            if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && (var3 = this.getOpStatus(var2)) != null) {
               this.ALLATORIxDEMO(false);
               var1 = var3.rangF;
               this.p = var3.iProtocolType;
            }
         }

         return var1;
      }
   }

   public boolean AclasIsConnect() {
      boolean var1 = false;
      if (this.b != null && this.b.isReady()) {
         var1 = true;
      }

      return var1;
   }

   public String AclasSDKVersion() {
      return "2.001";
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(byte[] arg0, byte[] arg1) {
      if (!this.da) {
         return true;
      } else {
         boolean var3 = false;
         int var4 = 1;
         if (arg1 == null) {
            arg1 = arg0;
         }

         boolean var8;
         while(true) {
            this.writeDatas(arg0);
            if (this.w) {
               var8 = var3 = true;
               this.Ea.ALLATORIxDEMO(100);
               break;
            }

            label75: {
               if (this.Ea != null) {
                  byte[] var5;
                  if ((var5 = this.Ea.ALLATORIxDEMO(1200)) != null && var5.length < arg1.length) {
                     this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("A\u001cz\u00196\u0007s\u0014rUw\u0012w\u001cx[S\u0007dUR\u0014b\u0014"), var5);
                     this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("XUVJZH^\u001a_[O["), arg1);
                     var5 = this.Ea.ALLATORIxDEMO(1200);
                  }

                  int var6;
                  if (var5 != null && var5.length >= arg1.length) {
                     for(int var10000 = var6 = 0; var10000 < var5.length - arg1.length; var10000 = var6) {
                        label66: {
                           int var7;
                           for(var10000 = var7 = 0; var10000 < arg1.length && var5[var6 + var7] == arg1[var7]; var10000 = var7) {
                              if (var7 == arg1.length - 1) {
                                 var8 = var3 = true;
                                 break label66;
                              }

                              ++var7;
                           }

                           var8 = var3;
                        }

                        if (var8) {
                           var8 = var3;
                           break label75;
                        }

                        ++var6;
                     }
                  }
               }

               var8 = var3;
            }

            if (var8) {
               var8 = var3;
               break;
            }

            if (var4-- <= 0) {
               var8 = var3;
               break;
            }
         }

         return var8 || this.Ga;
      }
   }

   // $FF: synthetic method
   private void i(String arg0) {
      this.ALLATORIxDEMO("KEYPWD", arg0);
   }

   // $FF: synthetic method
   private byte[] i(String arg0) {
      byte[] var2 = null;
      int var3;
      if ((var3 = arg0.length() / 2) > 0) {
         var2 = new byte[var3];

         int var4;
         for(int var10000 = var4 = 0; var10000 < var3; var10000 = var4) {
            String var5 = arg0.substring(var4 * 2, var4 * 2 + 2);

            try {
               Integer var6 = Integer.valueOf(var5, 16);
               var2[var4] = (byte)var6;
            } catch (Exception var7) {
               this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0012s\u0001T6RO")).append(var7.toString()).toString());
            }

            ++var4;
         }
      }

      return var2;
   }

   // $FF: synthetic method
   static boolean i(AclasScaler arg0, boolean arg1) {
      return arg0.s = arg1;
   }

   // $FF: synthetic method
   static St_PSData ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.H;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(byte[] arg0) {
      boolean var2 = false;
      var2 = arg0[11] != -1;
      int var4;
      if (!var2) {
         for(int var10000 = var4 = 3; var10000 < 12; var10000 = var4) {
            if (arg0[var4] != this.O[var4]) {
               var2 = true;
               return true;
            }

            ++var4;
         }
      }

      return var2;
   }

   public int AclasConnect(int arg0) {
      if (arg0 >= 0 && this.b.type() == 1) {
         USBPort var2 = (USBPort)this.b;
         ArrayList var3 = new ArrayList();
         if (arg0 >= var2.getDevList(var3)) {
            return -1;
         } else {
            this.j = (String)var3.get(arg0);
            return this.i(this.j);
         }
      } else {
         return -1;
      }
   }

   // $FF: synthetic method
   static boolean ALLATORIxDEMO(AclasScaler arg0, boolean arg1) {
      return arg0.S = arg1;
   }

   public boolean AclasReadPluData(final ArrayList arg0) {
      boolean var2 = false;
      if (arg0 != null && this.Da != null && this.Ea != null) {
         FutureTask var3 = FutureThreadPool.getInstance().executeTask(new Callable() {
            public Boolean ALLATORIxDEMO() throws Exception {
               Boolean var1 = false;
               if (AclasScaler.this.h(0, (byte[])null) && AclasScaler.this.h(1, (byte[])null)) {
                  int var2 = arg0.size();
                  boolean var3 = true;
                  int var4;
                  int var10000 = var4 = 0;

                  boolean var8;
                  while(true) {
                     if (var10000 >= var2) {
                        var8 = var3;
                        break;
                     }

                     St_Plu var5 = (St_Plu)arg0.get(var4);
                     byte[] var6;
                     if ((var6 = AclasScaler.this.ALLATORIxDEMO((byte)85, (byte)-7, var5.m_iIndex, 0)) == null) {
                        var8 = var3 = false;
                        break;
                     }

                     byte[] var7;
                     if ((var7 = AclasScaler.this.ALLATORIxDEMO(4, var6)) != null && var7.length == 13 && AclasScaler.this.ALLATORIxDEMO(var7, 1, 9) == var7[10]) {
                        var5.m_iPrice = AclasScaler.this.ALLATORIxDEMO(var7, 6, 4);
                        AclasScaler.this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("i_Z^\u001bJWO")).append(var5.m_iIndex).append(FutureThreadPool.ALLATORIxDEMO(">jls}\u007f$")).append(var5.m_iPrice).toString());
                     }

                     ++var4;
                     var10000 = var4;
                  }

                  var1 = var8;
                  AclasScaler.this.h(2, (byte[])null);
               }

               return var1;
            }
         });

         try {
            var2 = (Boolean)var3.get();
            return var2;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return var2;
   }

   // $FF: synthetic method
   static int ALLATORIxDEMO(AclasScaler arg0, int arg1) {
      return arg0.g = arg1;
   }

   public String AclasReadTareValue() {
      String var1 = "";
      if (this.Da != null && this.Ea != null) {
         this.sendDataToDevice(29, (byte[])null);
         byte[] var2;
         if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null) {
            int var3 = -1;
            int var4 = -1;

            int var5;
            for(int var10000 = var5 = 0; var10000 < var2.length - 1; var10000 = var5) {
               if (var3 == -1) {
                  if (var2[var5] == 1 && var2[var5 + 1] == 2) {
                     var3 = var5 + 4;
                  }
               } else if (var2[var5] == 3 && var2[var5 + 1] == 4) {
                  var4 = var5 - 1;
               }

               ++var5;
            }

            if (var3 > 0 && var4 > 0) {
               byte[] var6 = new byte[var5 = var4 - var3];
               System.arraycopy(var2, var3, var6, 0, var5);
               var1 = new String(var6);
            }

            this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("{XVZIi_Z^o[I_m[WO^\u0000")).append(var1).append(Packet.ALLATORIxDEMO("U")).append(bytesToHexString(var2, var2.length)).toString());
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(String arg0) {
      boolean var2 = false;
      File var3;
      if ((var3 = new File(arg0)).exists()) {
         var2 = var3.canRead() && var3.canWrite();
      }

      return var2;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, String arg1) {
      if (this.G != null) {
         this.G.edit().putString(arg0, arg1).commit();
      }

   }

   // $FF: synthetic method
   static boolean h(AclasScaler arg0) {
      return arg0.S;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      AclasLogUtil.error("AclasScale", arg0);
   }

   public byte[] sendAndRcv(byte[] arg0) {
      this.writeDatas(arg0);
      Object var2 = null;
      int var3 = 0;

      byte[] var4;
      while(((var4 = this.Ea.ALLATORIxDEMO(1200)) == null || var4.length > 2 && var4[0] == 65 && var4[1] == 67) && var3++ < 1) {
      }

      return var4;
   }

   public int AclasConnect() {
      int var1 = -1;
      if (this.b != null) {
         if (this.b.type() == 0) {
            String var2;
            if ((var2 = this.m()) != null && var2.length() > 0) {
               if ((var1 = this.AclasConnect(var2, 9600, 0)) != 0) {
                  this.F("");
                  this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("zYW[HyTTU_XN\u001b_IH\u001bYW_ZH\u001byTWViOHRT\\\u0000")).append(var1).append(Packet.ALLATORIxDEMO("U")).append(var2).toString());
               }
            } else {
               ArrayList var3 = new ArrayList();
               int var4 = this.getCommList(var3);

               int var5;
               for(int var10000 = var5 = 0; var10000 < var4; var10000 = var5) {
                  String var6 = (String)var3.get(var5);
                  if (this.ALLATORIxDEMO(var6) == 0) {
                     var1 = this.AclasConnect(var6, 9600, 0);
                  }

                  if (var1 == 0) {
                     this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("{XVZIxUUT^YO\u001aH_OyTWViOHRT\\\u0000")).append(var6).toString());
                     this.F(var6);
                     break;
                  }

                  ++var5;
               }
            }
         } else if (this.b.type() == 1) {
            var1 = this.AclasConnect(0);
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private native int checkPasswardNew(byte[] var1, int var2, byte[] var3);

   // $FF: synthetic method
   static AclasScalerPSXListener ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.T;
   }

   public void setLog(boolean arg0) {
      this.bLogFalg = arg0;
      AclasLogUtil.PRINT = this.bLogFalg;
      if (this.b != null) {
         this.b.setLog(arg0);
      }

      Log.d("AclasScale", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0006s\u0001Z\u001aqO")).append(arg0).toString());
   }

   // $FF: synthetic method
   static int ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.g++;
   }

   // $FF: synthetic method
   private native int getWeightNew(byte[] var1, int var2, WeightInfo var3);

   public int AclasGetTareMode() {
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
         return -1;
      } else {
         byte var1 = -1;
         if (this.Da != null && this.Ea != null) {
            this.sendDataToDevice(37, (byte[])null);
            byte[] var2;
            if ((var2 = this.Ea.ALLATORIxDEMO(300, false)) != null && var2.length > 1 && var2[0] == -91) {
               var1 = var2[1];
            }
         }

         return var1;
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(String arg0) {
      int var2 = -1;
      this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("U\u001ds\u0016}&s\u0007\u007f\u0014z%y\u0007bU;X;X;Xe\u0001w\u0007bX;X;U")).append(arg0).toString());
      if (this.b != null && this.b.type() == 0) {
         String var3 = (new StringBuilder()).insert(0, arg0).append(PacketHandler.ALLATORIxDEMO("\u0016\u0002\f\u000b\n\u0017\n")).toString();
         if ((var2 = this.b.open(var3)) == 0) {
            this.writeDatas(this.c);
            if (this.Ea != null) {
               byte[] var4;
               if ((var4 = this.Ea.ALLATORIxDEMO(100)) != null && var4.length < this.c.length) {
                  this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("U\u001ds\u0016}&s\u0007\u007f\u0014z%y\u0007bU;X;X;Xa\u001cz\u00196\u0007s\u0014rUw\u0012w\u001cxT6Us\u0007dUr\u0014b\u0014,U"), var4);
                  var4 = this.Ea.ALLATORIxDEMO(100);
               }

               if (var4 != null && var4.length > this.c.length && var4[0] == this.c[0]) {
                  this.h(PacketHandler.ALLATORIxDEMO("xR^YPi^HR[WjTHO\u001aI_Z^\u001bINYX_HI"));
                  var2 = 0;
               } else {
                  if (var4 != null) {
                     this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("6~\u0010u\u001eE\u0010d\u001cw\u0019F\u001ad\u00016\u0010d\u00077T6\u0007s\u0014rUz\u0010xO")).append(var4.length).append(PacketHandler.ALLATORIxDEMO("\u001b^ZNZ\u0000")).append(bytesToHexString(var4, var4.length)).toString());
                  }

                  var2 = -1;
                  this.AclasDisconnect();
                  this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("U\u001ds\u0016}&s\u0007\u007f\u0014z%y\u0007bUp\u0014z\u0006sU")).append(arg0).toString());
               }
            }
         } else {
            this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("yS_XQh_ISZVkUIN\u001b\u0017\u0016\u0017\u0016\u0017\u0016UK_U\u001a^HIUI\u0017\u0016\u0017\u0016\u001a")).append(arg0).append(Packet.ALLATORIxDEMO("Ud\u0010bO")).append(var2).toString());
         }
      }

      this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("xR^YPi^HR[WjTHO\u001a\u0016\u0017\u0016\u0017\u0016\u0017^T_\u0017\u0016\u0017\u0016\u001a")).append(arg0).append(Packet.ALLATORIxDEMO("Ud\u0010bO")).append(var2).toString());
      return var2;
   }

   // $FF: synthetic method
   private byte[] i(int arg0, byte[] arg1) {
      return this.ALLATORIxDEMO(arg0, arg1, (byte[])null);
   }

   public static String bytesToHexString(byte[] arg0, int arg1, int arg2, boolean arg3) {
      StringBuilder var4 = new StringBuilder("");
      if (arg0 != null && arg0.length > 0) {
         arg2 = arg1 + arg2 > arg0.length ? arg0.length - arg1 : arg2;

         int var5;
         for(int var10000 = var5 = 0; var10000 < arg2; var10000 = var5) {
            String var7;
            if ((var7 = Integer.toHexString(arg0[var5 + arg1] & 255)).length() < 2) {
               var4.append(0);
            }

            var4.append(var7);
            if (arg3) {
               var4.append(PacketHandler.ALLATORIxDEMO("\u001a"));
            }

            ++var5;
         }

         return var4.toString();
      } else {
         return null;
      }
   }

   public void setBluetoothListener(AclasBluetoothListener arg0) {
      this.Q = arg0;
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO(int arg0, byte[] arg1) {
      byte[] var3 = null;
      if (this.Da != null && this.Ea != null) {
         byte[] var4 = new byte[]{68};
         byte[] var5 = new byte[]{17, 0, 0, 0, 0, -17};
         byte[] var6 = new byte[]{51, 0, 0, 0, 0, -51};
         int var7 = 2;

         do {
            AclasScaler var10000;
            switch (arg0) {
               case 0:
                  var10000 = this;

                  while(false) {
                  }

                  this.writeDatas(var4);
                  break;
               case 1:
                  var10000 = this;
                  this.writeDatas(var5);
                  break;
               case 2:
                  var10000 = this;
                  this.writeDatas(var6);
                  break;
               case 3:
               case 4:
                  var10000 = this;
                  this.writeDatas(arg1);
                  break;
               default:
                  var10000 = this;
            }

            if ((var3 = var10000.Ea.ALLATORIxDEMO(300)) != null) {
               this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0006s\u001br%z\u0000U\u0018rUd\u0010bO")).append(var3.length).append(PacketHandler.ALLATORIxDEMO("\u001a")).append(bytesToHexString(var3, var3.length)).toString());
               return var3;
            }

            --var7;
         } while(var7 > 0);
      }

      return var3;
   }

   public void setAclasPSXListener(AclasScalerPSXListener arg0) {
      this.T = arg0;
   }

   // $FF: synthetic method
   static AclasDialogListener ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.V;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, byte[] arg1, int arg2) {
      String var4 = bytesToHexString(arg1, arg2);
      this.h((new StringBuilder()).insert(0, arg0).append(Packet.ALLATORIxDEMO("6O")).append(var4).toString());
   }

   // $FF: synthetic method
   private boolean i(int arg0) {
      return this.h(arg0, (byte[])null);
   }

   public byte[] AclasReadWeight(byte[] arg0) {
      byte[] var2 = null;
      if (this.Da != null && this.Ea != null && arg0 != null) {
         this.Ea.ALLATORIxDEMO(true);
         byte[] var4;
         if ((var4 = this.sendAndRcv(new byte[]{4, 2, 48, 56, 3})) != null) {
            this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("zYW[Hh^[_m^S\\RO\u001a\n\u001aI_O\u0000"), var4);
            if (var4.length == 7) {
               if (var4[4] == 48 && var4[5] == 48) {
                  int var5;
                  byte[] var6 = new byte[var5 = arg0.length + 7];
                  var6[0] = 4;
                  var6[1] = 2;
                  var6[2] = 48;
                  var6[3] = 49;
                  var6[4] = 27;
                  System.arraycopy(arg0, 0, var6, 5, arg0.length);
                  var6[var5 - 2] = 27;
                  var6[var5 - 1] = 3;
                  if ((var4 = this.sendAndRcv(var6)) != null) {
                     this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("W\u0016z\u0014e's\u0014r\"s\u001cq\u001dbU$Ud\u0010bO"), var4);
                     if ((var2 = this.sendAndRcv(new byte[]{4, 5})) != null) {
                        this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("zYW[Hh^[_m^S\\RO\u001a\b\u001aI_O\u0000"), var2);
                        if (var2.length == 1 && var2[0] == 21) {
                           var2 = this.AclasReadStatus();
                        }

                        byte[] var8 = new byte[]{4};
                        this.sendAndRcv(var8);
                     }
                  }
               } else {
                  var2 = var4;
               }
            }
         }

         this.Ea.ALLATORIxDEMO(false);
      }

      return var2;
   }

   // $FF: synthetic method
   static void ALLATORIxDEMO(AclasScaler arg0, boolean arg1) {
      arg0.ALLATORIxDEMO(arg1);
   }

   public boolean AclasTare() {
      boolean var1 = false;
      if (this.Da != null && this.Ea != null) {
         AclasScaler var10000;
         label50: {
            byte[] var2;
            if (this.p != -1 && this.b.type() != 6) {
               this.sendDataToDevice(13, (byte[])null);
               if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && var2.length > 6 && var2[5] == -128 && var2[6] == 14) {
                  var1 = true;
               }
            } else {
               byte[] var3;
               if (this.b.type() == 7) {
                  var2 = new byte[]{60, 84, 75, 62, 9};
                  var10000 = this;
                  this.writeDatas(var2);
                  var3 = this.Ea.ALLATORIxDEMO(1200);
                  var1 = true;
                  break label50;
               }

               var2 = new byte[]{-2};
               this.writeDatas(var2);
               if ((var3 = this.Ea.ALLATORIxDEMO(1200)) != null) {
                  if (var3.length > 6 && var3[5] == -128 && var3[6] == 14) {
                     var1 = true;
                  }

                  this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("W\u0016z\u0014e!w\u0007sO")).append(bytesToHexString(var3, var3.length)).toString());
               } else {
                  var1 = true;
               }
            }

            var10000 = this;
         }

         if (var10000.p == 2) {
            this.u.sendEmptyMessage(0);
         }
      }

      return var1;
   }

   public int AclasConnect(String arg0) {
      return this.b.type() != 3 && this.b.type() != 6 && this.b.type() != 7 ? -1 : this.i(arg0);
   }

   // $FF: synthetic method
   private native OPBoxInfo getOpStatus(byte[] var1);

   public String AclasReadId() {
      String var1 = "Error";
      if (this.Da != null && this.Ea != null) {
         if (!this.ga) {
            if ((var1 = this.ALLATORIxDEMO()).equals("Error")) {
               return this.h();
            }

            this.ALLATORIxDEMO(false);
            return var1;
         }

         if (!this.E) {
            return this.ALLATORIxDEMO();
         }

         var1 = this.h();
      }

      return var1;
   }

   // $FF: synthetic method
   static boolean i(AclasScaler arg0) {
      return arg0.s;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(int arg0) {
      return this.i(arg0, (byte[])null);
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(int arg0, byte[] arg1) {
      boolean var3 = false;
      if (this.Da != null && this.Ea != null) {
         this.sendDataToDevice(arg0, arg1);
         byte[] var4;
         if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null && var4.length > 6 && var4[5] == -128 && var4[6] == 14) {
            var3 = true;
         }
      }

      return var3;
   }

   public boolean AclasSetFrequence(int arg0) {
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
         return false;
      } else {
         arg0 = Math.min(10, Math.max(0, arg0));
         byte[] var2 = new byte[]{(byte)arg0};
         return this.ALLATORIxDEMO(51, var2);
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(int arg0) {
      boolean var2;
      switch (arg0) {
         case 16:
            while(false) {
            }

            var2 = true;
            return 2;
         case 17:
            var2 = true;
            return 4;
         case 18:
            var2 = true;
            return 3;
         case 33:
            var2 = false;
            return 0;
         case 48:
            var2 = true;
            return 1;
         default:
            return arg0;
      }
   }

   // $FF: synthetic method
   private byte[] ALLATORIxDEMO(String arg0) {
      Object var2 = null;
      if (arg0 != null) {
         if (arg0.length() == 0) {
            arg0 = Packet.ALLATORIxDEMO("'L/M&A&M");
         }

         try {
            Long var3 = Long.valueOf(arg0);
            int var4 = (int)(var3 % 256L);
            int var5 = (int)(var3 / 256L % 256L);
            String var6 = this.e();
            int var7 = 2000 + Integer.valueOf(var6.substring(0, 2));
            int var8 = Integer.valueOf(var6.substring(2, 4));
            int var9 = Integer.valueOf(var6.substring(4, 6));
            byte[] var12 = new byte[8];
            int var10 = 0;
            var12[var10++] = 5;
            var12[var10++] = -110;
            int var10006 = var10;
            byte var16 = (byte)var5;
            ++var10;
            var12[var10006] = var16;
            int var10005 = var10;
            byte var15 = (byte)var4;
            ++var10;
            var12[var10005] = var15;
            var12[var10++] = this.ALLATORIxDEMO(var7 / 100);
            var12[var10++] = this.ALLATORIxDEMO(var7 % 100);
            int var10002 = var10;
            byte var14 = this.ALLATORIxDEMO(var8);
            ++var10;
            var12[var10002] = var14;
            int var10001 = var10;
            byte var13 = this.ALLATORIxDEMO(var9);
            ++var10;
            var12[var10001] = var13;
            return var12;
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }

      return (byte[])var2;
   }

   // $FF: synthetic method
   static boolean ALLATORIxDEMO(AclasScaler arg0) {
      return arg0.A;
   }

   public String AclasFirmwareVersion() {
      String var1 = "";
      if (this.b != null && this.b.type() == 7) {
         var1 = ((FscBlueTooth)this.b).getFirmwareVersion();
      }

      return var1;
   }

   // $FF: synthetic method
   static void ALLATORIxDEMO(AclasScaler arg0, String arg1, byte[] arg2, int arg3) {
      arg0.ALLATORIxDEMO(arg1, arg2, arg3);
   }

   // $FF: synthetic method
   private String h() {
      String var1 = "Error";
      byte[] var2 = new byte[]{1};
      int var3 = 2;
      this.ALLATORIxDEMO();
      this.sendDataToDevice(25, var2);

      AclasScaler var10000;
      while(true) {
         byte[] var4;
         if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null && var4.length >= 10) {
            int var5;
            if ((var5 = this.ALLATORIxDEMO(var4, var4.length)) >= 0) {
               byte var6;
               if ((var6 = this.ALLATORIxDEMO(var4, var5, 9)) == var4[9 + var5]) {
                  var10000 = this;
                  var1 = String.valueOf(this.ALLATORIxDEMO(var4, 5 + var5, 4));
                  this.ALLATORIxDEMO(true);
                  break;
               }

               this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("H^[_jhbr^\u001bXxR^YP\u0000")).append(var6).append(Packet.ALLATORIxDEMO("6\u0011w\u0001wO")).append(var4[10 + var5]).toString());
            } else {
               this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("]^Nkics_sU^^B\u001b_IH"));
            }
         }

         --var3;
         if (var3 <= 0) {
            var10000 = this;
            break;
         }
      }

      var10000.h();
      return var1;
   }

   // $FF: synthetic method
   private String i() {
      String var1 = "Error";
      this.sendDataToDevice(24, (byte[])null);
      byte[] var2;
      if ((var2 = this.Ea.ALLATORIxDEMO(1200)) != null && var2.length > 15 && var2[5] == -111 && var2[6] == 18) {
         byte[] var3 = new byte[]{var2[13], var2[14]};
         String var4;
         if ((var4 = bytesToHexString(var3, 2).replaceAll(Packet.ALLATORIxDEMO("U"), "")).length() > 2) {
            var1 = (new StringBuilder()).insert(0, var4.substring(0, 1)).append(PacketHandler.ALLATORIxDEMO("\u0014")).append(var4.substring(1, var4.length())).toString();
            this.k = var1;
         }
      }

      return var1;
   }

   // $FF: synthetic method
   private native int closeFileUpdateUart();

   public String AclasReadVersion() {
      String var1 = "Error";
      if (this.Da != null && this.Ea != null) {
         if (!this.ga) {
            if ((var1 = this.i()).equals("Error")) {
               return this.G();
            }
         } else {
            if (!this.AclasIsPSX()) {
               return this.i();
            }

            var1 = this.G();
         }
      }

      return var1;
   }

   public AclasScaler(int arg0, Context arg1, AclasScalerListener arg2) {
      AclasLogUtil.setSaveLog(arg1);
      AclasScaler var10000;
      switch (arg0) {
         case 0:
            var10000 = this;

            while(false) {
            }

            this.b = new SerialPort();
            break;
         case 1:
            var10000 = this;
            this.b = new USBPort();
            break;
         case 2:
            var10000 = this;
            this.b = new BlueTooth();
            break;
         case 3:
            var10000 = this;
            this.b = new BluetoothSppClient();
            break;
         case 4:
            var10000 = this;
            this.b = new FscBlueTooth();
            break;
         default:
            var10000 = this;
      }

      var10000.Ma = arg2;
      if (this.b != null) {
         int var4 = this.b.init(arg1, this.v);
         Log.d("AclasScale", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0018I\u0016y\u0018r\u0010`\u001cu\u00106\u001cx\u001cbUd\u0010bO")).append(var4).toString());
         if (var4 != 0 && this.Ma != null) {
            this.Ma.onError(-1, PacketHandler.ALLATORIxDEMO("~^LRY^\u001aRTRNRUU\u001a^HIUI\u001b"));
            Log.e("AclasScale", Packet.ALLATORIxDEMO("R\u0010`\u001cu\u00106\u001cx\u001cb\u001cy\u001b6\u0010d\u0007y\u00077T"));
         } else {
            this.l = arg0;
            Log.d("AclasScale", PacketHandler.ALLATORIxDEMO("\u007f_MSX_\u001bSUSOSTT"));
         }
      } else {
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("{*u\u001a{\u0011s\u0003\u007f\u0016sUx\u0000z\u00196\u0001o\u0005sO")).append(arg0).toString());
      }

      this.ALLATORIxDEMO(arg1);
   }

   public int AclasSetGravity(int arg0, String arg1) {
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
         return -1;
      } else {
         boolean var3 = true;
         int var4;
         if ((var4 = this.h(arg1)) == 0) {
            var4 = this.AclasSetGravity(arg0) ? 0 : -5;
         }

         return var4;
      }
   }

   // $FF: synthetic method
   private void i() {
      if (this.l == 3) {
         this.writeDatas(this.c);
      }

   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO() {
      boolean var1 = false;
      if (this.p == 1 || this.p == -1 || this.p == 2) {
         if (this.A) {
            this.A = false;
            var1 = true;
            this.h(Packet.ALLATORIxDEMO("\u0016~\u0010u\u001eD\u0010w\u0011B\u0014d\u00106\u0018I\u0017X\u0010s\u0011D\u0010w\u0011B\u0014d\u0010"));
         }

         if (this.S ^ this.ka) {
            this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("XR^YPh^[_nZH^\u001aVeYnZH^|W[\\\u0000")).append(this.S).append(Packet.ALLATORIxDEMO("UF\u0007sO")).append(this.ka).toString());
            this.ka = this.S;
            var1 = true;
         }

         if (var1) {
            this.u.sendEmptyMessage(0);
         }
      }

      return var1;
   }

   public byte[] AclasReadWeight(byte[] arg0, byte[] arg1) {
      byte[] var3 = null;
      if (this.Da != null && this.Ea != null && arg0 != null && arg1 != null) {
         this.Ea.ALLATORIxDEMO(true);
         byte[] var5;
         if ((var5 = this.sendAndRcv(new byte[]{4, 2, 48, 56, 3})) != null) {
            this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("zYW[Hh^[_m^S\\RO\u001a\n\u001aI_O\u0000"), var5);
            if (var5.length == 7) {
               if (var5[4] == 48 && var5[5] == 48) {
                  byte[] var7 = new byte[arg0.length + 7 + arg1.length];
                  boolean var8 = false;
                  var7[0] = 4;
                  var7[1] = 2;
                  var7[2] = 48;
                  var7[3] = 51;
                  var7[4] = 27;
                  System.arraycopy(arg0, 0, var7, 5, arg0.length);
                  int var11 = 5 + arg0.length;
                  var7[var11++] = 27;
                  System.arraycopy(arg1, 0, var7, var11, arg1.length);
                  var11 += arg1.length;
                  var7[var11++] = 3;
                  if ((var5 = this.sendAndRcv(var7)) != null) {
                     this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("W\u0016z\u0014e's\u0014r\"s\u001cq\u001dbU$Ud\u0010bO"), var5);
                     if ((var3 = this.sendAndRcv(new byte[]{4, 5})) != null) {
                        this.ALLATORIxDEMO(PacketHandler.ALLATORIxDEMO("zYW[Hh^[_m^S\\RO\u001a\b\u001aI_O\u0000"), var3);
                        if (var3.length == 1 && var3[0] == 21) {
                           var3 = this.AclasReadStatus();
                        }

                        byte[] var10 = new byte[]{4};
                        this.sendAndRcv(var10);
                     }
                  }
               } else {
                  var3 = var5;
               }
            }
         }

         this.Ea.ALLATORIxDEMO(false);
      }

      return var3;
   }

   // $FF: synthetic method
   private native int checkPassward(byte[] var1);

   public boolean AclasSetGravity(int arg0) {
      if (this.AclasIsPSX()) {
         this.h(Packet.ALLATORIxDEMO("%E-6\u0011yUx\u001abUe\u0000f\u0005y\u0007bUb\u001d\u007f\u00066\u0013c\u001bu\u0001\u007f\u001axT"));
         return false;
      } else {
         boolean var2 = false;
         if (!this.P) {
            this.ALLATORIxDEMO(true);
         }

         if (this.Da != null && this.Ea != null) {
            Object var3 = null;
            AclasScaler var10000;
            byte[] var5;
            if (arg0 > 65536) {
               var5 = new byte[3];
               var10000 = this;
               var5[0] = (byte)(arg0 >> 16);
               var5[1] = (byte)(arg0 >> 8);
               var5[2] = (byte)arg0;
            } else {
               var5 = new byte[1];
               var10000 = this;
               var5[0] = (byte)arg0;
            }

            var10000.sendDataToDevice(10, var5);
            byte[] var4;
            if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null && var4.length > 6 && var4[5] == -128 && var4[6] == 14) {
               var2 = true;
            }
         }

         if (!this.P) {
            this.ALLATORIxDEMO(false);
         }

         return var2;
      }
   }

   // $FF: synthetic method
   private String ALLATORIxDEMO() {
      String var1 = "Error";
      byte[] var2 = new byte[]{-111, 27};
      byte[] var3;
      if ((var3 = this.i(25, var2)) != null) {
         byte[] var4 = new byte[]{-111, 29};
         byte[] var5 = this.i(46, var4);
         int var6 = -1;
         int var7 = 0;
         int var8;
         int var10000;
         if (var5 == null) {
            var6 = 0;
         } else {
            for(var10000 = var8 = 0; var10000 < var5.length; var10000 = var8) {
               byte var9;
               if ((var9 = var5[var8]) >= 48 || var9 <= 57 || var9 >= 65 || var9 <= 90 || var9 >= 97 || var9 <= 122) {
                  if (var6 == -1) {
                     if (var9 < 48 && var9 > 57) {
                        var6 = 0;
                     } else {
                        var6 = var3.length;
                     }
                  }

                  ++var7;
               }

               ++var8;
            }
         }

         byte[] var14 = new byte[var3.length + var7];
         if (var6 == 0) {
            if (var7 > 0) {
               System.arraycopy(var5, 0, var14, 0, var7);
            }

            System.arraycopy(var3, 0, var14, var7, var3.length);
         } else {
            System.arraycopy(var3, 0, var14, 0, var3.length);
            if (var7 > 0) {
               System.arraycopy(var5, 0, var14, var3.length, var7);
            }
         }

         try {
            byte[] var15 = (var1 = new String(var14, PacketHandler.ALLATORIxDEMO("oo|\u0016\u0002"))).getBytes();
            int var10 = 0;

            int var11;
            byte var12;
            for(var10000 = var11 = var15.length - 1; var10000 > -1 && (var12 = var15[var11]) >= 48 && var12 <= 57; ++var10) {
               --var11;
               var10000 = var11;
            }

            if (var10 > 0) {
               this.la = var1.substring(var15.length - var10);
            }

            this.h((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("d\u0010w\u0011Y&N<rO")).append(var1).toString());
         } catch (Exception var13) {
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("zYW[Hh^[_s_\u001a^HIUI\u0000")).append(var13.toString()).toString());
         }
      } else {
         this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("\u0007s\u0014rU/D'\u00176\u001bc\u0019zT"));
      }

      return var1;
   }

   public byte[] AclasReadStatus() {
      byte[] var1 = new byte[]{4, 2, 48, 56, 3};
      return this.sendAndRcv(var1);
   }

   public boolean AclasReadWeight(WeightInfoNew arg0) {
      boolean var2 = false;
      if (this.Da != null && this.Ea != null) {
         if (arg0 != null) {
            this.Ea.ALLATORIxDEMO(true);
         }

         WeightInfoNew var10000;
         byte[] var3;
         label39: {
            if (this.AclasIsPSX()) {
               var3 = new byte[]{5};
               this.writeDatas(var3);
               byte[] var4;
               if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null && var4[0] == 6) {
                  byte[] var5 = new byte[]{17};
                  this.writeDatas(var5);
                  this.Ea.ALLATORIxDEMO(1200);
                  var2 = true;
               }
            } else {
               if (this.p == 0) {
                  var10000 = arg0;
                  this.sendDataToDevice(40, (byte[])null);
                  var2 = true;
                  break label39;
               }

               var3 = new byte[]{17};
               this.writeDatas(var3);
               var2 = true;
            }

            var10000 = arg0;
         }

         if (var10000 != null) {
            if ((var3 = this.Ea.ALLATORIxDEMO(1200)) != null && var3.length > 10) {
               WeightInfo var6;
               if ((var6 = this.getWeight(var3)) != null) {
                  arg0.setData(var6);
                  var2 = true;
               } else {
                  var2 = false;
               }
            } else {
               var2 = false;
            }

            this.Ea.ALLATORIxDEMO(false);
         }
      }

      return var2;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(boolean arg0) {
      if (!this.ga) {
         if (this.E ^ arg0) {
            this.E = arg0;
         }

         this.h((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("I^NkicnBJ^\u0017\u0016\u0017\u0016\u0017\u007f_MSX_\u001bNBJ^\u0000")).append(arg0 ? Packet.ALLATORIxDEMO("%E-") : PacketHandler.ALLATORIxDEMO("uhb")).toString());
         this.ga = true;
      } else {
         if (this.E ^ arg0) {
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0006s\u0001F&N!o\u0005sUf\u0007s!o\u0005sUF&NO")).append(this.E).append(PacketHandler.ALLATORIxDEMO("\u001bSUJNNkic\u0000")).append(arg0).toString());
         }

      }
   }

   public void AclasDisconnect() {
      this.h(Packet.ALLATORIxDEMO("4u\u0019w\u0006R\u001ce\u0016y\u001bx\u0010u\u00016\u0006b\u0014d\u00016E"));
      this.i();
      if (this.Da != null) {
         this.Da.F();
         this.h(PacketHandler.ALLATORIxDEMO("zYW[H~RIXUUT^YO\u001ahNTJ\u001bWdNSH^[_mISO_\u001b_U^"));
         this.Da = null;
      }

      if (this.Ea != null) {
         this.Ea.h();
         this.h(Packet.ALLATORIxDEMO("4u\u0019w\u0006R\u001ce\u0016y\u001bx\u0010u\u00016&b\u001afU{*b\u001dd\u0010w\u0011D\u0010w\u00116\u0010x\u0011"));
         this.Ea = null;
      }

      if (this.b != null) {
         this.b.close();
         this.h(PacketHandler.ALLATORIxDEMO("{XVZI\u007fSHYTTU_XN\u001bYWUH_\u001bWdYTW__MSX_\u001b_U^"));
      }

      this.h(Packet.ALLATORIxDEMO("4u\u0019w\u0006R\u001ce\u0016y\u001bx\u0010u\u00016\u0010x\u001169y\u0012B\u001dd\u0010w\u00116\u0002\u007f\u0019zUe\u0001y\u0005"));
      this.Ba = false;
      AclasLogUtil.StopThread();
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO() {
      int var1 = 3;
      byte[] var2 = new byte[]{0, 0, 0};
      int var10000 = var1;

      while(true) {
         --var1;
         if (var10000 <= 0) {
            return;
         }

         var10000 = var1;
         this.writeDatas(var2);
         this.Ea.ALLATORIxDEMO(20);
      }
   }

   public void AclasSetMulTare(int arg0) {
      if (this.AclasIsPSX()) {
         this.h(PacketHandler.ALLATORIxDEMO("jhb\u001b^T\u001aUUO\u001aHOKJTHO\u001aORRI\u001b\\NTXNRUU\u001b"));
      } else {
         if (this.Da != null && this.Ea != null) {
            this.Ea.ALLATORIxDEMO(50);
            byte[] var2 = new byte[]{(byte)arg0};
            this.sendDataToDevice(36, var2);
            this.Ea.ALLATORIxDEMO(500);
            var2[0] = 32;
            this.sendDataToDevice(36, var2);
            this.Ea.ALLATORIxDEMO(500);
         }

      }
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(boolean arg0) {
      boolean var2 = false;
      if (this.Da != null && this.Ea != null) {
         byte[] var3 = new byte[]{16};
         byte var10001;
         byte[] var10002;
         if (arg0) {
            var10001 = 4;
            var10002 = var3;
         } else {
            var10001 = 5;
            var10002 = var3;
         }

         this.sendDataToDevice(var10001, var10002);
         byte[] var4;
         if ((var4 = this.Ea.ALLATORIxDEMO(1200)) != null) {
            if (var4.length > 6 && var4[5] == -128 && var4[6] == 14) {
               var2 = true;
               return true;
            }
         } else {
            this.ALLATORIxDEMO(Packet.ALLATORIxDEMO("W\u0016z\u0014e x9y\u0016}&y\u0013bUq\u0010b's\u0001R\u0014b\u00146\u001bc\u0019zT7T"));
         }
      }

      return var2;
   }

   // $FF: synthetic method
   static int ALLATORIxDEMO(AclasScaler arg0, byte arg1) {
      return arg0.ALLATORIxDEMO(arg1);
   }

   public void writeDatas(byte[] arg0) {
      if (this.Ea != null) {
         this.Ea.F();
      }

      if (arg0 != null && this.Da != null && arg0.length > 0) {
         this.Da.ALLATORIxDEMO(arg0);
      }

   }

   public static String bytesToHexString(byte[] arg0, int arg1, int arg2) {
      return bytesToHexString(arg0, arg1, arg2, true);
   }

   public class WeightInfoNew {
      public float netWeight;
      public int iMode;
      public boolean isZeroMode;
      public boolean isTare;
      public boolean isStable;
      public int iDecimal;
      public boolean isOverWeight;
      public String unit;
      public int iUnitId;
      public float tareWeight;

      public void init() {
         this.isStable = false;
         this.isTare = false;
         this.isOverWeight = false;
         this.isZeroMode = false;
         this.netWeight = 0.0F;
         this.tareWeight = 0.0F;
         this.unit = "";
         this.iDecimal = 0;
         this.iMode = 0;
         this.iUnitId = 0;
      }

      public WeightInfoNew() {
         this.init();
      }

      @SuppressLint({"DefaultLocale"})
      public WeightInfoNew(WeightInfo arg1) {
         this.setData(arg1);
      }

      public boolean setData(WeightInfoNew arg0) {
         if (arg0 != null && !this.equal(arg0)) {
            this.isStable = arg0.isStable;
            this.isTare = arg0.isTare;
            this.isOverWeight = arg0.isOverWeight;
            this.isZeroMode = arg0.isZeroMode;
            this.netWeight = arg0.netWeight;
            this.tareWeight = arg0.tareWeight;
            this.unit = arg0.unit;
            this.iDecimal = arg0.iDecimal;
            this.iMode = arg0.iMode;
            this.iUnitId = arg0.iUnitId;
            return true;
         } else {
            return false;
         }
      }

      public boolean equal(WeightInfoNew arg0) {
         if (this == arg0) {
            return true;
         } else if (arg0 == null) {
            return false;
         } else {
            return arg0.isStable == this.isStable && arg0.isTare == this.isTare && arg0.isOverWeight == this.isOverWeight && arg0.isZeroMode == this.isZeroMode && (double)Math.abs(arg0.netWeight - this.netWeight) < 1.0E-6 && (double)Math.abs(arg0.tareWeight - this.tareWeight) < 1.0E-6 && arg0.unit.equals(this.unit) && arg0.unit.equals(this.unit);
         }
      }

      public String toString() {
         if (this.unit.equalsIgnoreCase(FutureThreadPool.ALLATORIxDEMO("[z"))) {
            return String.format(BuildConfig.ALLATORIxDEMO("3h& "), this.netWeight) + this.unit;
         } else {
            String var1 = String.format(FutureThreadPool.ALLATORIxDEMO(";4-|>"), this.netWeight) + this.unit;
            switch (this.iDecimal) {
               case 0:
                  return String.format(BuildConfig.ALLATORIxDEMO("crf"), (int)this.netWeight) + this.unit;
               case 1:
                  return String.format(FutureThreadPool.ALLATORIxDEMO(";4/|>"), this.netWeight) + this.unit;
               case 2:
                  return String.format(BuildConfig.ALLATORIxDEMO("c8tpf"), this.netWeight) + this.unit;
               case 3:
                  var1 = String.format(FutureThreadPool.ALLATORIxDEMO(";4-|>"), this.netWeight) + this.unit;
               default:
                  return var1;
            }
         }
      }

      public void setData(WeightInfo arg0) {
         this.isStable = arg0.isStable;
         this.isTare = arg0.isTare;
         this.isOverWeight = arg0.isOverWeight;
         this.isZeroMode = arg0.isZeroMode;
         this.netWeight = arg0.netWeight;
         this.tareWeight = arg0.tareWeight;
         this.unit = arg0.unit.toLowerCase();
         this.iDecimal = arg0.iDecimal;
         this.iMode = arg0.iMode;
         this.iUnitId = arg0.iUnitId;
      }
   }

   public static class OPBoxInfo {
      public int rangF = 0;
      public int iAdFull = 0;
      public int iProtocolType = -1;
      public int precisionF = 0;
      public int opRealUnit = 0;
      public int hardSwFlag = 0;
      public int gravity = 0;
      public int iAdZero = 0;
      public int forceFlag = 0;
   }

   public class ScaleStatus {
      public int iAdZero;
      public int iProtocol;
      public int iGravity;
      public int iRange;
      public int iPrecision;
      public int iAdFull;
      public boolean bHardSw;
   }

   protected static class WeightInfo {
      public int iMode = 0;
      public float tareWeight = 0.0F;
      public boolean isUnderWeight = false;
      public boolean isOverWeight = false;
      public String strAdvance;
      public int weightAD = 0;
      public int originAD = 0;
      public boolean isZeroMode = false;
      public int zeroAD = 0;
      public int filterAD = 0;
      public boolean canSale = false;
      public float grossWeight = 0.0F;
      public float netWeight = 0.0F;
      public boolean isPreTare = false;
      public boolean isTare = false;
      public int iTypeProtocol;
      public String unit = "";
      public boolean isStable = false;
      public int iDecimal = 0;
      public int iUnitId = 0;
      public boolean isZero = false;

      public WeightInfo() {
      }

      public boolean equal(WeightInfo arg0) {
         if (this == arg0) {
            return true;
         } else if (arg0 == null) {
            return false;
         } else {
            return arg0.isStable == this.isStable && arg0.isTare == this.isTare && arg0.isPreTare == this.isPreTare && arg0.isZero == this.isZero && arg0.isOverWeight == this.isOverWeight && arg0.isUnderWeight == this.isUnderWeight && arg0.canSale == this.canSale && arg0.isZeroMode == this.isZeroMode && (double)Math.abs(arg0.netWeight - this.netWeight) < 1.0E-6 && (double)Math.abs(arg0.tareWeight - this.tareWeight) < 1.0E-6 && (double)Math.abs(arg0.grossWeight - this.grossWeight) < 1.0E-6 && arg0.unit.equals(this.unit) && arg0.originAD == this.originAD && arg0.filterAD == this.filterAD && arg0.zeroAD == this.zeroAD && arg0.weightAD == this.weightAD;
         }
      }

      public String toString() {
         return String.format(BuildConfig.ALLATORIxDEMO("3h% "), this.netWeight) + this.unit + (this.iMode == 127 ? (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("\u001a}{\u007f\u0000")).append(this.filterAD).append(BuildConfig.ALLATORIxDEMO("L")).append(PacketHandler.ALLATORIxDEMO("t{\u007f\u0000")).append(this.originAD).append(BuildConfig.ALLATORIxDEMO("f")).append(PacketHandler.ALLATORIxDEMO("a{\u007f\u0000")).append(this.zeroAD).append(BuildConfig.ALLATORIxDEMO("f")).append(PacketHandler.ALLATORIxDEMO("l{\u007f\u0000")).append(this.weightAD).toString() : "");
      }
   }

   public interface AclasScalerPSXListener {
      void onRcvData(St_PSData var1);
   }

   public interface AclasScalerListener {
      void onRcvData(WeightInfoNew var1);

      void onDisConnected();

      void onError(int var1, String var2);

      void onUpdateProcess(int var1, int var2);

      void onConnected();
   }

   public interface AclasBluetoothListener {
      void onSearchFinish();

      void onSearchBluetooth(String var1);
   }
}
