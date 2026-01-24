package com.example.io;

import android.content.Context;
import android.util.Log;
import com.example.util.AclasLogUtil;
import com.example.util.FutureThreadPool;
import com.example.util.PacketHandler;
import com.example.util.SerialDeviceUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class SerialPort implements ComDevice {
   private static final String TAG = "SerialPort";
   private FileOutputStream mFileOutputStream;
   private Context appContex = null;
   private boolean devReady = false;
   private FileDescriptor mFd;
   public boolean bLogFalg = false;
   private ComDevice.ComDeviceListener listener = null;
   private FileInputStream mFileInputStream;
   private Vector mDrivers = null;

   // $FF: synthetic method
   private String[] i() {
      return SerialDeviceUtils.getScaleSerialPathList();
   }

   public int init(Context arg0, ComDevice.ComDeviceListener arg1) {
      this.appContex = arg0;
      this.listener = arg1;
      this.devReady = false;
      this.mFileInputStream = null;
      this.mFileOutputStream = null;
      return 0;
   }

   public native void closeDev();

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      this.ALLATORIxDEMO("SerialPort", arg0);
   }

   public boolean isReady() {
      return this.devReady;
   }

   public int open(String arg0) {
      boolean var2 = true;
      String[] var3;
      if ((var3 = arg0.split(PacketHandler.ALLATORIxDEMO("\u0016"))).length < 1) {
         return -1;
      } else {
         String var4 = var3[0];
         File var5;
         if (!(var5 = new File(var4)).exists()) {
            return -1;
         } else {
            if (!var5.canRead() || !var5.canWrite()) {
               try {
                  Process var6 = Runtime.getRuntime().exec(FutureThreadPool.ALLATORIxDEMO("5mcmn{w1xwt1ik"));
                  String var7 = (new StringBuilder()).insert(0, PacketHandler.ALLATORIxDEMO("XRVU_\u001a\r\f\r\u001a")).append(var5.getAbsolutePath()).append(FutureThreadPool.ALLATORIxDEMO("\u0010{bwn\u0014")).toString();
                  var6.getOutputStream().write(var7.getBytes());
                  if (var6.waitFor() != 0 || !var5.canRead() || !var5.canWrite()) {
                     return -2;
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
                  return -2;
               }
            }

            if (this.listener != null) {
               this.listener.onConnecting();
            }

            int var10 = 115200;
            int var11 = 0;
            if (var3.length > 1) {
               var10 = Integer.parseInt(var3[1]);
            }

            if (var3.length > 2) {
               var11 = Integer.parseInt(var3[2]);
            }

            this.mFd = openDev(var4, var10, var11);
            byte var9;
            SerialPort var10000;
            if (this.mFd == null) {
               Log.e("SerialPort", PacketHandler.ALLATORIxDEMO("U[OSM_\u001bUK_U\u001aI_OOITH\u001aUOWV"));
               var9 = -1;
               var10000 = this;
            } else {
               var10000 = this;
               this.mFileInputStream = new FileInputStream(this.mFd);
               this.mFileOutputStream = new FileOutputStream(this.mFd);
               this.devReady = true;
               var9 = 0;
            }

            if (var10000.listener != null) {
               this.listener.onConnectFinished(var9);
            }

            return var9;
         }
      }
   }

   public int readData(byte[] arg0, int arg1, int arg2) {
      if (this.mFileInputStream == null) {
         return -1;
      } else {
         try {
            byte var4 = 10;
            int var5;
            int var6 = var5 = arg2 / var4;
            boolean var7 = true;
            int var8 = 0;

            int var12;
            int var10000;
            while(true) {
               label37: {
                  if ((var12 = this.mFileInputStream.read(arg0, var8, arg1 - var8)) > 0) {
                     var6 = var5;
                     if ((var8 += var12) >= arg1) {
                        var10000 = var8;
                        break;
                     }
                  } else {
                     --var6;
                     if (var6 > 0) {
                        label34: {
                           try {
                              Thread.sleep((long)var4);
                           } catch (InterruptedException var10) {
                              var10.printStackTrace();
                              break label34;
                           }

                           var10000 = var6;
                           break label37;
                        }
                     }
                  }

                  var10000 = var6;
               }

               if (var10000 <= 0) {
                  var10000 = var8;
                  break;
               }
            }

            if (var10000 > 0) {
               var12 = var8;
            }

            return var12;
         } catch (IOException var11) {
            var11.printStackTrace();
            this.close();
            return -2;
         }
      }
   }

   // $FF: synthetic method
   private String[] ALLATORIxDEMO() {
      Vector var1 = new Vector();

      try {
         Iterator var2 = this.ALLATORIxDEMO().iterator();

         while(var2.hasNext()) {
            Iterator var4;
            Iterator var10000 = var4 = ((<undefinedtype>)var2.next()).ALLATORIxDEMO().iterator();

            while(var10000.hasNext()) {
               String var5 = ((File)var4.next()).getAbsolutePath();
               var10000 = var4;
               var1.add(var5);
            }
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   // $FF: synthetic method
   private static native FileDescriptor openDev(String var0, int var1, int var2);

   public int writeData(byte[] arg0) {
      if (this.mFileOutputStream == null) {
         return -1;
      } else {
         try {
            this.mFileOutputStream.write(arg0);
            return arg0.length;
         } catch (IOException var3) {
            var3.printStackTrace();
            this.close();
            return -1;
         }
      }
   }

   public int getDevList(List arg0) {
      String[] var2;
      int var3;
      for(int var10000 = var3 = (var2 = this.i()).length - 1; var10000 >= 0; var10000 = var3) {
         arg0.add(var2[var3--]);
      }

      return arg0.size();
   }

   static {
      System.loadLibrary(FutureThreadPool.ALLATORIxDEMO("_yr{mI}{r\u007fRs|"));
   }

   public int close() {
      this.closeDev();
      this.devReady = false;

      SerialPort var10000;
      label17: {
         try {
            this.mFileInputStream.close();
            this.mFileOutputStream.close();
         } catch (Exception var2) {
            var10000 = this;
            var2.printStackTrace();
            break label17;
         }

         var10000 = this;
      }

      var10000.mFileInputStream = null;
      this.mFileOutputStream = null;
      if (this.listener != null) {
         this.listener.onDisconnect();
      }

      return 0;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0, String arg1) {
      if (this.bLogFalg) {
         AclasLogUtil.info(arg0, arg1);
      }

   }

   // $FF: synthetic method
   private Vector ALLATORIxDEMO() throws IOException {
      if (this.mDrivers == null) {
         this.mDrivers = new Vector();
         LineNumberReader var1 = new LineNumberReader(new FileReader(PacketHandler.ALLATORIxDEMO("\u0015KHTY\u0014NOC\u0014^ISM_II")));

         String var2;
         while((var2 = var1.readLine()) != null) {
            String var3 = var2.substring(0, 21).trim();
            String[] var4;
            if ((var4 = var2.split(FutureThreadPool.ALLATORIxDEMO("Bi5"))).length >= 5) {
               String var5 = var4[var4.length - 1];
               String var6 = var4[var4.length - 4];
               if (var5.equals(PacketHandler.ALLATORIxDEMO("H_ISZV"))) {
                  this.ALLATORIxDEMO("SerialPort", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("}{nZhwl{hm:Xuktz:p\u007fi:zhwl{h>")).append(var3).append(PacketHandler.ALLATORIxDEMO("\u001bUU\u001a")).append(var6).toString());
                  this.mDrivers.add(new Object(var3, var6) {
                     private String i;
                     Vector d = null;
                     private String ALLATORIxDEMO;

                     public String ALLATORIxDEMO() {
                        return this.ALLATORIxDEMO;
                     }

                     public {
                        this.ALLATORIxDEMO = arg1;
                        this.i = arg2;
                     }

                     public Vector ALLATORIxDEMO() {
                        if (this.d == null) {
                           this.d = new Vector();
                           File[] var2 = (new File(PacketHandler.ALLATORIxDEMO("\u0014^^L"))).listFiles();

                           int var3;
                           for(int var10000 = var3 = 0; var10000 < var2.length; var10000 = var3) {
                              if (var2[var3].getAbsolutePath().startsWith(this.i)) {
                                 SerialPort.this.ALLATORIxDEMO("SerialPort", (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO("@+m/a+$\u001fk,j=$7a.$=a/m:ac$")).append(var2[var3]).toString());
                                 this.d.add(var2[var3]);
                              }

                              ++var3;
                           }
                        }

                        return this.d;
                     }
                  });
               }
            }
         }

         var1.close();
      }

      return this.mDrivers;
   }

   public int type() {
      return 0;
   }

   public void setLog(boolean arg0) {
      this.bLogFalg = arg0;
   }
}
