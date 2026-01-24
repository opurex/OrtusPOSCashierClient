package com.example.io;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.example.util.FutureThreadPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BlueTooth implements ComDevice {
   private Context D;
   boolean l = false;
   private static final String G = "00001101-0000-1000-8000-00805F9B34FB";
   BluetoothSocket H;
   public String strParam = "";
   BluetoothAdapter j;
   BluetoothDevice h;
   OutputStream i;
   ComDevice.ComDeviceListener f;
   private final String d = "BlueToothSDK";
   InputStream ALLATORIxDEMO;

   public int open(String arg0) {
      if (this.j == null) {
         return -1;
      } else {
         if (arg0 == null && this.strParam.length() > 0) {
            arg0 = this.strParam;
         }

         if (arg0 == null) {
            return -1;
         } else {
            short var2 = 10000;
            String var3 = null;
            if (arg0.contains(FutureThreadPool.ALLATORIxDEMO("2"))) {
               String[] var4;
               if ((var4 = arg0.split(FutureThreadPool.ALLATORIxDEMO("2"))).length > 1) {
                  var3 = var4[1];
               }
            } else {
               var3 = arg0;
            }

            this.strParam = arg0;
            if (this.f != null) {
               this.f.onConnecting();
            }

            if (!this.getBluetoothStateON(var2)) {
               if (this.f != null) {
                  this.f.onConnectFinished(-1);
               }

               return -1;
            } else {
               this.h = this.j.getRemoteDevice(var3);
               if (this.h == null) {
                  if (this.f != null) {
                     this.f.onConnectFinished(-1);
                  }

                  return -1;
               } else {
                  if (ActivityCompat.checkSelfPermission(this.D, FutureThreadPool.ALLATORIxDEMO("\u007ftzhqsz4n\u007flwwimsqt0XRO[NQUJRAI][P")) == 0) {
                     try {
                        if (this.j.isDiscovering()) {
                           this.j.cancelDiscovery();
                        }

                        ParcelUuid[] var11;
                        if ((var11 = this.h.getUuids()) == null || var11 != null && var11.length == 0) {
                           Log.e("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("xj^{l0}{nKow~m27:porv373"));
                        }

                        if (var11 != null) {
                           Log.d("BlueToothSDK", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("kow~$")).append(var11[0].toString()).toString());
                           this.H = this.h.createInsecureRfcommSocketToServiceRecord(UUID.fromString(var11[0].toString()));
                        } else {
                           Log.e("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("qj{t>xro{nqujr>oksz:porv?;?;?;?;?;?;?;?;?;?"));
                        }

                        byte var5 = -1;

                        try {
                           this.H.connect();
                           if (this.H.isConnected()) {
                              Log.d("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("|vk\u007fjuqnv:}upt{yj\u007fz737373737373"));
                              this.ALLATORIxDEMO = this.H.getInputStream();
                              this.i = this.H.getOutputStream();
                              this.l = true;
                           } else {
                              Log.e("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("|vk\u007fjuqnv:}upt{yj\u007fz:{hlul737373737373"));
                              if (this.f != null) {
                                 this.f.onConnectFinished(1);
                              }

                              var5 = 1;
                           }
                        } catch (IOException var9) {
                           var9.printStackTrace();
                           if (this.f != null) {
                              this.f.onConnectFinished(-2);
                           }

                           try {
                              this.H.close();
                           } catch (IOException var8) {
                              var8.printStackTrace();
                           }

                           var5 = -3;
                        }

                        return var5;
                     } catch (Exception var10) {
                        Log.e("BlueToothSDK", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("_fy{jjsqt$")).append(var10.toString()).toString());
                     }
                  }

                  if (this.f != null) {
                     this.f.onConnectFinished(0);
                  }

                  return 0;
               }
            }
         }
      }
   }

   public boolean getBluetoothStateON(int arg0) {
      int var2;
      for(int var10000 = var2 = arg0 / 50; var10000 > 0; var10000 = var2) {
         if (this.j.getState() == 12) {
            return true;
         }

         try {
            Thread.sleep(50L);
         } catch (InterruptedException var5) {
         }

         --var2;
      }

      return false;
   }

   public void setLog(boolean arg0) {
   }

   public int close() {
      BlueTooth var10000;
      label52: {
         this.l = false;
         if (this.H != null) {
            label35: {
               if (this.ALLATORIxDEMO != null) {
                  label33: {
                     try {
                        this.ALLATORIxDEMO.close();
                     } catch (IOException var2) {
                        var2.printStackTrace();
                        break label33;
                     }

                     var10000 = this;
                     break label35;
                  }
               }

               var10000 = this;
            }

            IOException var6;
            label56: {
               boolean var10001;
               label47: {
                  if (var10000.i != null) {
                     label45: {
                        try {
                           this.i.close();
                        } catch (IOException var5) {
                           var5.printStackTrace();
                           break label45;
                        }

                        var10000 = this;
                        break label47;
                     }
                  }

                  try {
                     var10000 = this;
                  } catch (IOException var4) {
                     var6 = var4;
                     var10001 = false;
                     break label56;
                  }
               }

               try {
                  var10000.H.close();
               } catch (IOException var3) {
                  var6 = var3;
                  var10001 = false;
                  break label56;
               }

               var10000 = this;
               break label52;
            }

            IOException var1 = var6;
            var1.printStackTrace();
         }

         var10000 = this;
      }

      var10000.ALLATORIxDEMO = null;
      this.i = null;
      if (this.f != null) {
         this.f.onDisconnect();
      }

      return 0;
   }

   public int type() {
      return 3;
   }

   public int writeData(byte[] arg0) {
      if (this.i == null) {
         return -1;
      } else {
         if (!this.H.isConnected() && this.f != null) {
         }

         try {
            this.i.write(arg0);
            return arg0.length;
         } catch (IOException var3) {
            var3.printStackTrace();
            if (this.f != null) {
            }

            return -1;
         }
      }
   }

   public boolean isReady() {
      return this.l;
   }

   public int readData(byte[] arg0, int arg1, int arg2) {
      if (this.ALLATORIxDEMO == null) {
         return -1;
      } else {
         int var4 = -1;
         int var5 = (var5 = arg2 / 10) >= 1 ? var5 : 1;

         while(var5-- > 0) {
            try {
               if (this.ALLATORIxDEMO.available() > 0) {
                  var4 = this.ALLATORIxDEMO.read(arg0, 0, arg1);
                  return var4;
               }

               try {
                  Thread.sleep(10L);
               } catch (Exception var7) {
               }
            } catch (IOException var8) {
               var8.printStackTrace();
               if (this.f != null) {
               }

               return -1;
            }
         }

         return var4;
      }
   }

   public int getDevList(List arg0) {
      Log.d("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("}{nZ\u007fhVwij"));
      if (this.j == null) {
         return -1;
      } else {
         if (arg0 == null) {
            arg0 = new ArrayList();
         }

         ((List)arg0).clear();
         if (ActivityCompat.checkSelfPermission(this.D, FutureThreadPool.ALLATORIxDEMO("{p~luw~0j{hssmiwup4\\VK_JUQNVE]UPT[YJ")) == 0) {
            Set var2;
            Iterator var3;
            if ((var2 = this.j.getBondedDevices()).size() > 0) {
               for(Iterator var10000 = var3 = var2.iterator(); var10000.hasNext(); var10000 = var3) {
                  BluetoothDevice var4 = (BluetoothDevice)var3.next();
                  Log.d("BlueToothSDK", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("}{nS{tul^{lwy{Yr{mi$")).append(var4.getBluetoothClass().getMajorDeviceClass()).toString());
                  ((List)arg0).add((new StringBuilder()).insert(0, var4.getName()).append(FutureThreadPool.ALLATORIxDEMO("2")).append(var4.getAddress()).toString());
                  int var5 = var4.getBluetoothClass().getMajorDeviceClass();
                  Log.d("BlueToothSDK", (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("y\u007fj^{lRsmn>~{lwy{:p{s\u007f$")).append(var4.getName()).append(FutureThreadPool.ALLATORIxDEMO(">yr{mi$")).append(Integer.toHexString(var5)).toString());
               }
            } else {
               Log.e("BlueToothSDK", FutureThreadPool.ALLATORIxDEMO("y\u007fj^{lRsmn>j\u007fsl\u007fz^{lwy{i>'#*?;?;?;?;?;?;?;?;?"));
            }
         }

         return ((List)arg0).size();
      }
   }

   public int init(Context arg0, ComDevice.ComDeviceListener arg1) {
      this.D = arg0;
      this.f = arg1;
      this.l = false;
      this.j = BluetoothAdapter.getDefaultAdapter();
      if (this.j == null) {
         return -1;
      } else {
         this.h = null;
         return 0;
      }
   }
}
