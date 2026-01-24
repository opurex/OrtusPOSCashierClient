package com.example.io;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Build.VERSION;
import android.util.Log;
import com.example.util.AclasLogUtil;
import com.example.util.FutureThreadPool;
import com.example.util.Packet;
import com.example.util.PacketHandler;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class USBPort implements ComDevice {
   private BroadcastReceiver c = new BroadcastReceiver() {
      public void onReceive(Context arg0, Intent arg1) {
         String var3;
         if ((var3 = arg1.getAction()).compareTo(USBPort.this.j.getPackageName()) == 0) {
            synchronized(this) {
               <undefinedtype> var10000;
               if (arg1.getBooleanExtra(BluetoothCtrl.ALLATORIxDEMO(")a+i0w*m6j"), true)) {
                  USBPort.this.e = 1;
                  USBPort.this.ALLATORIxDEMO((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("+a(q<w-T<v4m*w0k7$+a(q<w-T<v4m*w0k7V<w,h->")).append(USBPort.this.e).append(BluetoothCtrl.ALLATORIxDEMO("ym7p<j->")).append(arg1.toString()).toString());
                  var10000 = this;
               } else {
                  USBPort.this.e = 0;
                  USBPort.this.ALLATORIxDEMO((new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("v<u,a*p\ta+i0w*m6jyv<b,w<>")).append(USBPort.this.e).toString());
                  var10000 = this;
               }

               USBPort.this.ALLATORIxDEMO(false);
               if (USBPort.this.ALLATORIxDEMO != null) {
                  USBPort.this.ALLATORIxDEMO.onConnectFinished(USBPort.this.e == 1 ? 4 : -1);
               }

            }
         } else {
            UsbDevice var4;
            if (var3.compareTo(BluetoothCtrl.ALLATORIxDEMO("e7`+k0`wl8v=s8v<*,w;*8g-m6jwQ\nF\u0006@\u001cR\u0010G\u001c[\u001dA\rE\u001aL\u001c@")) == 0 && (var4 = (UsbDevice)arg1.getParcelableExtra(BluetoothCtrl.ALLATORIxDEMO("=a/m:a"))) != null && USBPort.this.G != null && var4.getDeviceName().compareTo(USBPort.this.G.getDeviceName()) == 0) {
               USBPort.this.ALLATORIxDEMO(BluetoothCtrl.ALLATORIxDEMO("Q*fyg1e7j<hym*$;v6o<jx$\u001ah6w<$=a/m:ayj6s"));
               if (USBPort.this.e == 1 && USBPort.this.ALLATORIxDEMO != null) {
                  USBPort.this.ALLATORIxDEMO.onConnectFinished(8);
               }

               USBPort.this.close();
            }

         }
      }
   };
   private UsbInterface g = null;
   private int e = -1;
   final int I = 64;
   private int E = 500;
   private List M = new ArrayList();
   private boolean A = false;
   private boolean a = false;
   private UsbEndpoint K = null;
   public boolean bLogFalg = false;
   final int J = 5;
   private byte[] F = new byte[128];
   private UsbEndpoint m = null;
   private final String b = "USB";
   private PendingIntent B = null;
   private UsbManager L;
   private int D;
   ByteBuffer[] l = new ByteBuffer[5];
   private UsbDevice G;
   private UsbDeviceConnection H = null;
   private Context j = null;
   private <undefinedtype> h = null;
   UsbRequest[] i = new UsbRequest[5];
   private boolean f = false;
   private byte[] d;
   private ComDevice.ComDeviceListener ALLATORIxDEMO = null;

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1) {
      int var10000;
      int var3;
      int var4;
      label33: {
         var3 = -1;
         var4 = 0;
         UsbRequest var5;
         int var6;
         if ((var5 = this.H.requestWait()) != null) {
            for(var10000 = var6 = 0; var10000 < 5; var10000 = var6) {
               if (var5 == this.i[var6]) {
                  this.d = this.l[var6].array();
                  this.D = this.l[var6].position();
                  USBPort var7;
                  if (this.D > 0) {
                     if (var4 + this.D < arg1) {
                        var7 = this;
                        System.arraycopy(this.d, 0, arg0, var4, this.D);
                        var4 += this.D;
                        this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u007fO")).append(var6).append(PacketHandler.ALLATORIxDEMO("\u001bH^[_\u0000")).append(this.D).append(Packet.ALLATORIxDEMO("UB\u001ab\u0014zO")).append(var4).append(PacketHandler.ALLATORIxDEMO("\u001a")).append(bytesToHexString(this.d, this.D)).toString());
                     } else {
                        var7 = this;
                        this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("s\u0007dU\u007fO")).append(var6).append(PacketHandler.ALLATORIxDEMO("\u001bH^[_\u0000")).append(this.D).append(Packet.ALLATORIxDEMO("6\u001cF\u001aeO")).append(var4).append(PacketHandler.ALLATORIxDEMO("\u001aRwZBw_U\u0000")).append(arg1).append(Packet.ALLATORIxDEMO("U")).append(bytesToHexString(this.d, this.D)).toString());
                     }
                  } else {
                     var7 = this;
                     this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("\u0017\u0016\u0017R\u0000")).append(var6).append(Packet.ALLATORIxDEMO("6\u0007s\u0014rO")).append(this.D).append(PacketHandler.ALLATORIxDEMO("\u001aoUO[W\u0000")).append(var4).toString());
                  }

                  var7.i[var6].queue(this.l[var6], 64);
                  var10000 = var4;
                  break label33;
               }

               ++var6;
            }
         }

         var10000 = var4;
      }

      if (var10000 > 0) {
         var3 = var4;
      }

      return var3;
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(int arg0, int arg1) {
      int var3 = 0;
      if ((arg0 & 4) == 4) {
         var3 |= 64;
      }

      if ((arg0 & 2) == 2) {
         var3 |= 32;
      }

      if ((arg1 & 4) == 4) {
         var3 &= -65;
      }

      if ((arg1 & 2) == 2) {
         var3 &= -33;
      }

      return this.ALLATORIxDEMO(var3);
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(int arg0) {
      return this.ALLATORIxDEMO(164, ~arg0, 0);
   }

   public int open(String arg0) {
      if (this.L != null && this.M != null && !arg0.isEmpty()) {
         int var2;
         int var10000 = var2 = 0;

         USBPort var4;
         while(true) {
            if (var10000 >= this.M.size()) {
               var4 = this;
               break;
            }

            if (((<undefinedtype>)this.M.get(var2)).ALLATORIxDEMO(arg0)) {
               this.h = (<undefinedtype>)this.M.get(var2);
               this.G = this.h.ALLATORIxDEMO;
               this.g = this.h.i;
               this.h.f = this.L.hasPermission(this.h.ALLATORIxDEMO) ? 1 : -1;
               this.e = this.h.f;
               this.a = this.g.getInterfaceClass() == 10;
               StringBuilder var10002 = (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("Y\u0005s\u001b6\u0013\u007f\u001brUr\u0010`Up\u0007y\u00186\u0019\u007f\u0006bUd\u0010g\u0000s\u0006b%s\u0007{\u001ce\u0006\u007f\u001ax's\u0006c\u0019bO"));
               var4 = this;
               this.ALLATORIxDEMO(var10002.append(this.e).append(PacketHandler.ALLATORIxDEMO("\u001aX^X\u0000")).append(this.a).toString());
               break;
            }

            ++var2;
            var10000 = var2;
         }

         if (var4.g != null && this.G != null) {
            for(var10000 = var2 = 0; var10000 < this.g.getEndpointCount(); var10000 = var2) {
               UsbEndpoint var3 = this.g.getEndpoint(var2);
               this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("p\u001cx\u00116\u0010fU\u007fO")).append(var2).append(PacketHandler.ALLATORIxDEMO("\u001a\u007fSI\u0000")).append(var3.getDirection()).append(Packet.ALLATORIxDEMO("UW\u0001b\u0007\u007fO")).append(var3.getAttributes()).append(PacketHandler.ALLATORIxDEMO("\u001bwCjh\u0000")).append(var3.getMaxPacketSize()).toString());
               if (var3.getDirection() != 128 || var3.getMaxPacketSize() < 32 || var3.getAttributes() != 2 && var3.getAttributes() != 3) {
                  if (var3.getDirection() == 0 && (var3.getAttributes() == 2 || var3.getAttributes() == 3)) {
                     this.m = var3;
                     this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("]SU^\u001b_K\u001aTOOJNNd_K\u001aR\u0000")).append(var2).append(Packet.ALLATORIxDEMO("UR\u001cdO")).append(var3.getDirection()).append(PacketHandler.ALLATORIxDEMO("\u001azNOHR\u0000")).append(var3.getAttributes()).toString());
                  }
               } else {
                  this.K = var3;
                  this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0013\u007f\u001brUs\u00056\u001cx\u0005c\u0001I\u0010fU\u007fO")).append(var2).append(PacketHandler.ALLATORIxDEMO("\u001a\u007fSI\u0000")).append(var3.getDirection()).append(Packet.ALLATORIxDEMO("UW\u0001b\u0007\u007fO")).append(var3.getAttributes()).toString());
               }

               ++var2;
            }

            if (this.K != null && this.m != null) {
               if (this.ALLATORIxDEMO != null) {
                  this.ALLATORIxDEMO.onConnecting();
               }

               this.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0017s\u0013y\u0007sUd\u0010g\u0000s\u0006b%s\u0007{\u001ce\u0006\u007f\u001axO")).append(this.e).append(PacketHandler.ALLATORIxDEMO("\u001a\u007f_MJ^HVSHIRUU\u0000")).append(this.h.f).toString());
               if (this.e != 1) {
                  this.ALLATORIxDEMO(true);
                  this.requestPermission();
                  return -4;
               } else {
                  return this.ALLATORIxDEMO();
               }
            } else {
               this.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u001cx\u0005c\u00016\u0010fU+U")).append(this.K).append(PacketHandler.ALLATORIxDEMO("\u001aTOOJNN\u001b_K\u001a\u0006\u001a")).append(this.m).toString());
               return -1;
            }
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public boolean isReady() {
      return this.A;
   }

   public static UsbManager getUsbManager(Context arg0) {
      return (UsbManager)arg0.getSystemService(Packet.ALLATORIxDEMO("\u0000e\u0017"));
   }

   public int getDevList(List arg0) {
      if (this.M == null) {
         return -1;
      } else {
         if (arg0 == null) {
            arg0 = new ArrayList();
         }

         ((List)arg0).clear();

         int var2;
         for(int var10000 = var2 = 0; var10000 < this.M.size(); var10000 = var2) {
            Object var10001 = this.M.get(var2);
            ++var2;
            ((List)arg0).add(((<undefinedtype>)var10001).toString());
         }

         return this.M.size();
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO() {
      boolean var1 = true;
      if (this.e == -1) {
         return -4;
      } else {
         this.H = this.L.openDevice(this.G);
         byte var10000;
         byte var3;
         if (this.H == null) {
            Log.e("USB", PacketHandler.ALLATORIxDEMO("XH^[O_\u001bYTTU_XN\u001b_IHTH"));
            var3 = -2;
            var10000 = var3;
         } else if (this.H.claimInterface(this.g, true)) {
            var10000 = 0;
            this.A = true;
            var3 = 0;
         } else {
            Log.e("USB", Packet.ALLATORIxDEMO("\u0016z\u0014\u007f\u00186\u001cx\u0001s\u0007p\u0014u\u00106\u0013w\u001cz\u0010rT"));
            var3 = -3;
            var10000 = var3;
         }

         if (var10000 == 0) {
            USBPort var4;
            if (this.ALLATORIxDEMO()) {
               var4 = this;
               boolean var2 = this.ALLATORIxDEMO(9600, (byte)8, (byte)1, (byte)0, (byte)0);
               this.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("TJ^T\u007f_MSX_\u001boZHOsUSO\u001ah_OyTT]S\\\u0000")).append(var2).toString());
            } else {
               Log.e("USB", Packet.ALLATORIxDEMO("C\u0014d\u0001_\u001b\u007f\u00016\u0013w\u0019e\u00107T7T7T7T7T7T"));
               var4 = this;
            }

            var4.ALLATORIxDEMO();
         }

         if (this.ALLATORIxDEMO != null) {
            this.ALLATORIxDEMO.onConnectFinished(var3);
         }

         return var3;
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(int arg0, int arg1, int arg2) {
      if (this.H == null) {
         return -1;
      } else {
         boolean var4 = false;
         return this.H.controlTransfer(64, arg0, arg1, arg2, (byte[])null, 0, this.E);
      }
   }

   public int getDevList(UsbManager arg0, List arg1, boolean arg2) {
      if (arg0 == null) {
         return -1;
      } else {
         if (arg1 == null) {
            arg1 = new ArrayList();
         }

         Collection var4;
         Collection var10000 = var4 = arg0.getDeviceList().values();
         this.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("]^N\u007f_MSX_wSHN\u001b\u0000")).append(var4.size()).toString());
         Iterator var5 = var10000.iterator();

         while(var5.hasNext()) {
            UsbDevice var6 = (UsbDevice)var5.next();
            int var7 = var6.getInterfaceCount();
            this.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("1s\u0003\u007f\u0016s<rO")).append(var6.getDeviceId()).append(PacketHandler.ALLATORIxDEMO("\u001akS_\u0000")).append(var6.getProductId()).append(Packet.ALLATORIxDEMO("U@\u001crO")).append(var6.getVendorId()).append(PacketHandler.ALLATORIxDEMO("\u001bTZW^\u0000")).append(var6.getDeviceName()).append(Packet.ALLATORIxDEMO("6\u001cp\u0016U\u001bbO")).append(var7).toString());

            int var8;
            for(int var12 = var8 = 0; var12 < var7; var12 = var8) {
               UsbInterface var9;
               String var10;
               USBPort var13;
               if ((var10 = (var9 = var6.getInterface(var8)).getName()) == null) {
                  var10 = PacketHandler.ALLATORIxDEMO("UOWV");
                  var13 = this;
               } else {
                  var10 = var10.replaceAll(Packet.ALLATORIxDEMO("U"), "");
                  var13 = this;
               }

               var13.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("~^LRY^s_\u0000")).append(var6.getDeviceId()).append(Packet.ALLATORIxDEMO("UF\u001crO")).append(var6.getProductId()).append(PacketHandler.ALLATORIxDEMO("\u001amS_\u0000")).append(var6.getVendorId()).append(Packet.ALLATORIxDEMO("6\u001cp\u0016U\u001bbO")).append(var7).append(PacketHandler.ALLATORIxDEMO("\u001aR\u0000")).append(var8).append(Packet.ALLATORIxDEMO("U\u007f\u0013uO")).append(var9.getInterfaceClass()).append(PacketHandler.ALLATORIxDEMO("\u001aR\\XtZW^\u0000")).append(var10).toString());
               if (arg2 || var6.getProductId() == 29987 && var6.getVendorId() == 6790 || var6.getProductId() == 21795 && var6.getVendorId() == 6790 || var6.getProductId() == 8963 && var6.getVendorId() == 1659 || var9.getInterfaceClass() == 7 && (var6.getProductId() == 22592 && var6.getVendorId() == 1155 || var6.getProductId() == 274 && var6.getVendorId() == 26488) || var9.getInterfaceClass() == 3 && var6.getProductId() == 274 && var6.getVendorId() == 26488 && var10.equals(Packet.ALLATORIxDEMO("4u\u0019w\u0006;=_1;&U4Z0")) || var9.getInterfaceClass() == 10) {
                  Object var11 = new Object(null) {
                     UsbInterface i;
                     int f;
                     UsbDevice ALLATORIxDEMO;

                     public boolean ALLATORIxDEMO(String arg0) {
                        if (!arg0.isEmpty() && this.ALLATORIxDEMO != null && this.i != null) {
                           return this.toString().compareToIgnoreCase(arg0) == 0;
                        } else {
                           return false;
                        }
                     }

                     public String toString() {
                        if (this.ALLATORIxDEMO != null && this.i != null) {
                           String var1 = Integer.toHexString(this.ALLATORIxDEMO.getProductId());
                           String var2 = Integer.toHexString(this.ALLATORIxDEMO.getVendorId());
                           int var3 = this.i.getInterfaceClass();
                           String var4 = "";
                           switch (var3) {
                              case 3:
                                 while(false) {
                                 }

                                 var4 = BluetoothCtrl.ALLATORIxDEMO("L0`");
                                 break;
                              case 7:
                                 var4 = BluetoothCtrl.ALLATORIxDEMO("T+m7p<v");
                                 break;
                              case 10:
                                 var4 = FutureThreadPool.ALLATORIxDEMO("]^]");
                                 break;
                              default:
                                 var4 = (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("OMXJcn\u007f$")).append(String.valueOf(var3)).toString();
                           }

                           return (new StringBuilder()).insert(0, var4).append(BluetoothCtrl.ALLATORIxDEMO("(")).append(var2).append(FutureThreadPool.ALLATORIxDEMO("2")).append(var1).append(BluetoothCtrl.ALLATORIxDEMO("(")).append(this.ALLATORIxDEMO.getDeviceName()).toString();
                        } else {
                           return "";
                        }
                     }
                  };
                  ((<undefinedtype>)var11).ALLATORIxDEMO = var6;
                  ((<undefinedtype>)var11).i = var9;
                  ((<undefinedtype>)var11).f = arg0.hasPermission(var6) ? 1 : -1;
                  if (var9.getInterfaceClass() == 7) {
                     var13 = this;
                     ((List)arg1).add(var11);
                  } else {
                     ((List)arg1).add(0, var11);
                     var13 = this;
                  }

                  var13.ALLATORIxDEMO("USB", (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("~^LRY^s_\u0000")).append(var6.getDeviceId()).append(Packet.ALLATORIxDEMO("UF\u001crO")).append(var6.getProductId()).append(PacketHandler.ALLATORIxDEMO("\u001amS_\u0000")).append(var6.getVendorId()).append(Packet.ALLATORIxDEMO("Uu\u0019eO")).append(var9.getInterfaceClass()).toString());
               }

               ++var8;
            }
         }

         return ((List)arg1).size();
      }
   }

   public int getDevListName(List arg0) {
      if (this.M == null) {
         return -1;
      } else {
         if (arg0 == null) {
            arg0 = new ArrayList();
         }

         ((List)arg0).clear();

         int var2;
         for(int var10000 = var2 = 0; var10000 < this.M.size(); var10000 = var2) {
            Object var10001 = this.M.get(var2);
            ++var2;
            ((List)arg0).add(((<undefinedtype>)var10001).toString());
         }

         return this.M.size();
      }
   }

   public static String bytesToHexString(byte[] arg0, int arg1) {
      StringBuilder var2 = new StringBuilder("");
      if (arg0 != null && arg0.length > 0) {
         if (arg1 > arg0.length) {
            arg1 = arg0.length;
         }

         int var3;
         for(int var10000 = var3 = 0; var10000 < arg1; var10000 = var3) {
            String var5;
            if ((var5 = Integer.toHexString(arg0[var3] & 255)).length() < 2) {
               var2.append(0);
            }

            var2.append(var5);
            ++var3;
            var2.append(PacketHandler.ALLATORIxDEMO("\u001a"));
         }

         return var2.toString();
      } else {
         return null;
      }
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO() {
      byte[] var3 = new byte[8];
      this.ALLATORIxDEMO(161, 0, 0);
      int var1 = this.ALLATORIxDEMO(95, 0, 0, var3, 2);
      if (var1 < 0) {
         return false;
      } else {
         this.ALLATORIxDEMO(154, 4882, 55682);
         this.ALLATORIxDEMO(154, 3884, 4);
         var1 = this.ALLATORIxDEMO(149, 9496, 0, var3, 2);
         if (var1 < 0) {
            return false;
         } else {
            this.ALLATORIxDEMO(154, 10023, 0);
            this.ALLATORIxDEMO(164, 255, 0);
            return true;
         }
      }
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(int arg0, int arg1, int arg2, byte[] arg3, int arg4) {
      if (this.H == null) {
         return -1;
      } else {
         boolean var6 = false;
         return this.H.controlTransfer(192, arg0, arg1, arg2, arg3, arg4, 50);
      }
   }

   public void setLog(boolean arg0) {
      this.bLogFalg = arg0;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      this.ALLATORIxDEMO("USB", arg0);
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO() {
      int var1;
      if (!this.a) {
         for(int var10000 = var1 = 0; var10000 < 5; var10000 = var1) {
            this.i[var1] = new UsbRequest();
            this.i[var1].initialize(this.H, this.K);
            this.l[var1] = ByteBuffer.allocate(64);
            this.i[var1].queue(this.l[var1++], 64);
         }
      }

      this.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO(";X;X;\u001cx\u001cb<x\u0005c\u0001;X;Xu\u0011uO")).append(this.a).toString());
   }

   public void requestPermission() {
      if (this.e != 1) {
         this.e = -1;
         this.h.f = -1;
         this.L.requestPermission(this.G, this.B);
      }
   }

   public int init(Context arg0, ComDevice.ComDeviceListener arg1) {
      this.j = arg0;
      this.ALLATORIxDEMO = arg1;
      if (this.L == null) {
         this.L = (UsbManager)arg0.getSystemService(PacketHandler.ALLATORIxDEMO("OHX"));
         if (this.L == null) {
            this.ALLATORIxDEMO("USB", Packet.ALLATORIxDEMO("_\u001b\u007f\u00016 E76%y\u0007bUp\u0014\u007f\u0019s\u00117UQ\u0010bUC\u0006t8w\u001bw\u0012s\u00076\u0013w\u001cz\u0010rT"));
            return -1;
         }
      }

      return this.getDevList(this.L, this.M) < 0 ? -1 : 0;
   }

   // $FF: synthetic method
   private boolean ALLATORIxDEMO(int arg0, byte arg1, byte arg2, byte arg3, byte arg4) {
      int var6 = 0;
      int var7 = 0;
      char var8 = 0;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      char var10000;
      switch (arg3) {
         case 0:
            while(false) {
            }

            var8 = 0;
            var10000 = (char)arg2;
            break;
         case 1:
            var8 = (char)(var8 | 8);
            var10000 = (char)arg2;
            break;
         case 2:
            var8 = (char)(var8 | 24);
            var10000 = (char)arg2;
            break;
         case 3:
            var8 = (char)(var8 | 40);
            var10000 = (char)arg2;
            break;
         case 4:
            var8 = (char)(var8 | 56);
            var10000 = (char)arg2;
            break;
         default:
            var8 = 0;
            var10000 = (char)arg2;
      }

      if (var10000 == 2) {
         var8 = (char)(var8 | 4);
      }

      switch (arg1) {
         case 5:
            while(false) {
            }

            var10000 = var8 = (char)(var8 | 0);
            break;
         case 6:
            var10000 = var8 = (char)(var8 | 1);
            break;
         case 7:
            var10000 = var8 = (char)(var8 | 2);
            break;
         case 8:
            var10000 = var8 = (char)(var8 | 3);
            break;
         default:
            var10000 = var8 = (char)(var8 | 3);
      }

      var8 = (char)(var10000 | 192);
      short var13 = 156;
      var6 = var6 | var13 | var8 << 8;
      short var14;
      byte var15;
      byte var16;
      switch (arg0) {
         case 50:
            while(false) {
            }

            var15 = 0;
            var14 = 22;
            var16 = (byte)var7;
            break;
         case 75:
            var15 = 0;
            var14 = 100;
            var16 = (byte)var7;
            break;
         case 110:
            var15 = 0;
            var14 = 150;
            var16 = (byte)var7;
            break;
         case 135:
            var15 = 0;
            var14 = 169;
            var16 = (byte)var7;
            break;
         case 150:
            var15 = 0;
            var14 = 178;
            var16 = (byte)var7;
            break;
         case 300:
            var15 = 0;
            var14 = 217;
            var16 = (byte)var7;
            break;
         case 600:
            var15 = 1;
            var14 = 100;
            var16 = (byte)var7;
            break;
         case 1200:
            var15 = 1;
            var14 = 178;
            var16 = (byte)var7;
            break;
         case 1800:
            var15 = 1;
            var14 = 204;
            var16 = (byte)var7;
            break;
         case 2400:
            var15 = 1;
            var14 = 217;
            var16 = (byte)var7;
            break;
         case 4800:
            var15 = 2;
            var14 = 100;
            var16 = (byte)var7;
            break;
         case 9600:
            var15 = 2;
            var14 = 178;
            var16 = (byte)var7;
            break;
         case 19200:
            var15 = 2;
            var14 = 217;
            var16 = (byte)var7;
            break;
         case 38400:
            var15 = 3;
            var14 = 100;
            var16 = (byte)var7;
            break;
         case 57600:
            var15 = 3;
            var14 = 152;
            var16 = (byte)var7;
            break;
         case 115200:
            var15 = 3;
            var14 = 204;
            var16 = (byte)var7;
            break;
         case 230400:
            var15 = 3;
            var14 = 230;
            var16 = (byte)var7;
            break;
         case 460800:
            var15 = 3;
            var14 = 243;
            var16 = (byte)var7;
            break;
         case 500000:
            var15 = 3;
            var14 = 244;
            var16 = (byte)var7;
            break;
         case 921600:
            var15 = 7;
            var14 = 243;
            var16 = (byte)var7;
            break;
         case 1000000:
            var15 = 3;
            var14 = 250;
            var16 = (byte)var7;
            break;
         case 2000000:
            var15 = 3;
            var14 = 253;
            var16 = (byte)var7;
            break;
         case 3000000:
            var15 = 3;
            var14 = 254;
            var16 = (byte)var7;
            break;
         default:
            var15 = 2;
            var14 = 178;
            var16 = (byte)var7;
      }

      var7 = var16 | 136 | var15 | var14 << 8;
      int var12 = this.ALLATORIxDEMO(161, var6, var7);
      if (arg4 == 1) {
         this.ALLATORIxDEMO(6, 0);
      }

      return var12 >= 0;
   }

   public void doClose() {
      if (this.H != null && this.g != null) {
         this.H.releaseInterface(this.g);
         this.H.close();
         this.H = null;
         this.g = null;
      }

   }

   public int close() {
      if (this.e != 1) {
         this.e = 0;
      }

      this.doClose();
      this.K = null;
      this.m = null;
      this.A = false;
      if (this.ALLATORIxDEMO != null) {
         this.ALLATORIxDEMO.onDisconnect();
      }

      return 0;
   }

   public int type() {
      return 1;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, String arg1) {
      if (this.bLogFalg) {
         AclasLogUtil.info(arg0, arg1);
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(boolean arg0) {
      USBPort var10000;
      label26: {
         if (this.B == null) {
            this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("H^KN_HNk_IWRIHSTT\u001bYNHI_UNkHRTO_I\u0014K_IWVSO\u0000")).append(this.h.f).toString());
            if (VERSION.SDK_INT >= 31) {
               var10000 = this;
               this.B = PendingIntent.getBroadcast(this.j, 0, new Intent(this.j.getPackageName()), 67108864);
               break label26;
            }

            this.B = PendingIntent.getBroadcast(this.j, 0, new Intent(this.j.getPackageName()), 0);
         }

         var10000 = this;
      }

      var10000.ALLATORIxDEMO((new StringBuilder()).insert(0, Packet.ALLATORIxDEMO("\u0007s\u0012\u007f\u0006b\u0010d's\u0016s\u001c`\u0010dO")).append(arg0).toString());
      if (arg0) {
         IntentFilter var2 = new IntentFilter(this.j.getPackageName());
         Context var10004;
         if (VERSION.SDK_INT >= 33) {
            var10004 = this.j;
            this.j.registerReceiver(this.c, var2, 4);
         } else if (VERSION.SDK_INT >= 31) {
            var10004 = this.j;
            this.j.registerReceiver(this.c, var2, 2);
         } else {
            this.j.registerReceiver(this.c, var2);
         }
      } else {
         this.j.unregisterReceiver(this.c);
      }
   }

   public int getDevList(UsbManager arg0, List arg1) {
      return this.getDevList(arg0, arg1, this.f);
   }

   public int readData(byte[] arg0, int arg1, int arg2) {
      if (this.H != null && this.K != null) {
         return !this.a ? this.ALLATORIxDEMO(arg0, arg1) : this.H.bulkTransfer(this.K, arg0, arg1, arg2);
      } else {
         return -1;
      }
   }

   public int writeData(byte[] arg0) {
      if (this.H != null && this.m != null && arg0 != null) {
         int var2 = this.H.bulkTransfer(this.m, arg0, arg0.length, 500);
         this.ALLATORIxDEMO((new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("LHRN^~ZNZ\u001aOUO[W\u0000")).append(arg0.length).append(Packet.ALLATORIxDEMO("Ua\u0007\u007f\u0001sO")).append(var2).toString());
         if (arg0.length >= 300) {
            try {
               Thread.sleep(100L);
               return var2;
            } catch (Exception var4) {
            }
         }

         return var2;
      } else {
         return -1;
      }
   }
}
