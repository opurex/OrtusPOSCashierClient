package com.feasycom.ble.sdk;

import android.app.UiModeManager;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.feasycom.ble.controler.FscBleCentralApi;
import com.feasycom.ble.controler.FscBleCentralApiImp;
import com.feasycom.ble.controler.FscBleCentralCallbacksImp;
import com.feasycom.ble.utils.CryptoUtils;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.utils.ExpandKt;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.ota.utils.FileUtil;
import java.util.List;

public class FBlueSDK {
   private static final String TAG = "FBlueSDK";
   private static final long DATA_STABILITY_TIME = 2000L;
   private static final int BIND_TIMEOUT_MS = 30000;
   private static final int USER_DATA_LENGTH = 22;
   private static FBlueSDK instance;
   private FscBleCentralApi mFscBleCentralApi;
   private Context mContext;
   private PacketHandler packetHandler = new PacketHandler();
   private boolean isConnected = false;
   private BluetoothDevice bluetoothDevice = null;
   private boolean isReceiverRegistered = false;
   private BindCallback mBindCallback;
   private Handler mHandler = new Handler(Looper.getMainLooper());
   private String lastCombinedData = "";
   private long lastDataTimestamp = 0L;
   private Runnable bindTimeout = new Runnable() {
      public void run() {
         FBlueSDK.this.bindDeviceFailed();
         FBlueSDK.this.mBindCallback.onBindResult(false);
      }
   };
   private final BroadcastReceiver bondStateReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         String var5 = var2.getAction();
         MsgLogger.e("FBlueSDK", "bondStateReceiver action => " + var5);
         if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(var5)) {
            BluetoothDevice var6 = (BluetoothDevice)var2.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            int var10000 = var2.getIntExtra("android.bluetooth.device.extra.BOND_STATE", 10);
            MsgLogger.e("FBlueSDK", "bondStateReceiver bondState => " + var10000);
            if (var10000 == 12) {
               String var7 = "FBlueSDK";
               StringBuilder var3 = (new StringBuilder()).append("bondStateReceiver Paired successfully with device ");
               String var4;
               if (var6 != null) {
                  var4 = var6.getAddress();
               } else {
                  var4 = "unknown";
               }

               MsgLogger.d(var7, var3.append(var4).toString());
               if (var6 != null) {
                  FBlueSDK.this.readSpecificCharacteristic(var6);
               }
            }
         }

      }
   };

   public static FBlueSDK getInstance() {
      if (instance == null) {
         instance = new FBlueSDK();
      }

      return instance;
   }

   private void parseScanRecord(byte[] var1, DataReceivedCallback var2, BindStatusCallback var3) {
      String var7 = FileUtil.bytesToHex(var1, var1.length);

      int var5;
      for(int var4 = 0; var4 < var7.length() && (var5 = var4 + 2) <= var7.length(); var4 = var5) {
         var4 = ExpandKt.hexStringToInt(var7.substring(var4, var5));
         int var6;
         if ((var6 = var5 + 2) > var7.length()) {
            break;
         }

         int var10001 = var4;
         String var8 = var7.substring(var5, var6);
         if ((var5 = var6 + (var10001 - 1) * 2) > var7.length()) {
            break;
         }

         var8.hashCode();
         if (var8.equals("FF")) {
            this.parseManufacturerSpecificData(var7.substring(var6, var5), var2, var3);
         }
      }

   }

   private void parseManufacturerSpecificData(String var1, DataReceivedCallback var2, BindStatusCallback var3) {
      Log.e("FBlueSDK", "parseManufacturerSpecificData data => " + var1);
      String var10000 = var1.substring(2, 4);
      var10000.hashCode();
      if (var10000.equals("A1")) {
         Exception var43;
         label234: {
            boolean var10001;
            String var10002;
            String var10003;
            String var10004;
            String var10005;
            String var10006;
            FBlueSDK var44;
            String var45;
            try {
               var44 = this;
               var45 = var1;
               var10002 = var1;
               var10003 = var1;
               var10004 = var1;
               var10005 = var1;
               var10006 = var1.substring(0, 2);
            } catch (Exception var40) {
               var43 = var40;
               var10001 = false;
               break label234;
            }

            var1 = var10006;

            try {
               var10005 = var10005.substring(2, 4);
            } catch (Exception var39) {
               var43 = var39;
               var10001 = false;
               break label234;
            }

            String var4 = var10005;

            try {
               var10004 = var10004.substring(4, 6);
            } catch (Exception var38) {
               var43 = var38;
               var10001 = false;
               break label234;
            }

            String var5 = var10004;

            try {
               var10003 = var10003.substring(6, 8);
            } catch (Exception var37) {
               var43 = var37;
               var10001 = false;
               break label234;
            }

            String var6 = var10003;

            int var46;
            try {
               var46 = ExpandKt.hexStringToInt(var10002.substring(8, 10));
            } catch (Exception var36) {
               var43 = var36;
               var10001 = false;
               break label234;
            }

            var46 = var46 * 2 + 10;

            try {
               var45 = var45.substring(10, var46);
            } catch (Exception var35) {
               var43 = var35;
               var10001 = false;
               break label234;
            }

            String var7 = var45;

            boolean var47;
            try {
               var47 = var44.isConnected;
            } catch (Exception var34) {
               var43 = var34;
               var10001 = false;
               break label234;
            }

            if (var47) {
               return;
            }

            FBlueSDK var49;
            try {
               var44 = this;
               var49 = this;
               var46 = PreferenceUtilKt.getInt(this.mContext, "session_key", -1);
            } catch (Exception var33) {
               var43 = var33;
               var10001 = false;
               break label234;
            }

            int var8 = var46;

            try {
               var10002 = ExpandKt.bytesToHex(CryptoUtils.Companion.decrypt(ExpandKt.hexStringToByteArray(var7), var8)).replace(" ", "");
            } catch (Exception var32) {
               var43 = var32;
               var10001 = false;
               break label234;
            }

            var7 = var10002;

            StringBuilder var48;
            try {
               var48 = new StringBuilder;
            } catch (Exception var31) {
               var43 = var31;
               var10001 = false;
               break label234;
            }

            StringBuilder var42 = var48;

            String var10007;
            StringBuilder var50;
            StringBuilder var51;
            try {
               var10003 = var7;
               var50 = var42;
               var10005 = var6;
               var51 = var42;
               var10007 = var5;
               var42.<init>();
               var42.append(var4);
            } catch (Exception var30) {
               var43 = var30;
               var10001 = false;
               break label234;
            }

            try {
               var51.append(var10007);
            } catch (Exception var29) {
               var43 = var29;
               var10001 = false;
               break label234;
            }

            try {
               var50.append(var10005);
            } catch (Exception var28) {
               var43 = var28;
               var10001 = false;
               break label234;
            }

            try {
               var48.append(var10003);
            } catch (Exception var27) {
               var43 = var27;
               var10001 = false;
               break label234;
            }

            try {
               var45 = var49.packetHandler.addPacket(var42.toString());
            } catch (Exception var26) {
               var43 = var26;
               var10001 = false;
               break label234;
            }

            var4 = var45;

            try {
               var47 = var44.isBlank(var45);
            } catch (Exception var25) {
               var43 = var25;
               var10001 = false;
               break label234;
            }

            if (!var47) {
               label230: {
                  try {
                     var44 = this;
                     var45 = var4;
                     var2.onReceiveBroadcastData(var4);
                     var10002 = PreferenceUtilKt.getStr(this.mContext, "binding_frames", "");
                  } catch (Exception var24) {
                     var43 = var24;
                     var10001 = false;
                     break label230;
                  }

                  String var41 = var10002;

                  try {
                     var47 = var44.isDataStable(var45);
                  } catch (Exception var23) {
                     var43 = var23;
                     var10001 = false;
                     break label230;
                  }

                  label219: {
                     BindStatusCallback var53;
                     if (var47) {
                        try {
                           var10000 = PreferenceUtilKt.getStr(this.mContext, "big_endian_mac", "");
                        } catch (Exception var22) {
                           var43 = var22;
                           var10001 = false;
                           break label230;
                        }

                        var4 = var10000;
                        if (var10000 == null) {
                           break label219;
                        }

                        int var52;
                        try {
                           var52 = var4.length();
                        } catch (Exception var21) {
                           var43 = var21;
                           var10001 = false;
                           break label230;
                        }

                        if (var52 != 12) {
                           break label219;
                        }

                        try {
                           var47 = var1.equals(var41);
                        } catch (Exception var20) {
                           var43 = var20;
                           var10001 = false;
                           break label230;
                        }

                        if (var47) {
                           try {
                              var47 = this.mFscBleCentralApi.isConnected(ExpandKt.formatMacAddress(var4));
                           } catch (Exception var19) {
                              var43 = var19;
                              var10001 = false;
                              break label230;
                           }

                           if (!var47) {
                              try {
                                 this.resetData();
                                 this.mFscBleCentralApi.connect(ExpandKt.formatMacAddress(var4), Boolean.FALSE);
                                 return;
                              } catch (Exception var9) {
                                 var43 = var9;
                                 var10001 = false;
                                 break label230;
                              }
                           } else {
                              try {
                                 Log.e("FBlueSDK", "BLE_VVV app和电子秤已建立连接");
                                 return;
                              } catch (Exception var18) {
                                 var43 = var18;
                                 var10001 = false;
                                 break label230;
                              }
                           }
                        }

                        try {
                           var53 = var3;
                           var49 = this;
                           Log.e("FBlueSDK", "BLE_VVV 随机帧和绑定帧不一样");
                        } catch (Exception var15) {
                           var43 = var15;
                           var10001 = false;
                           break label230;
                        }

                        try {
                           PreferenceUtilKt.putStr(var49.mContext, "bind_address", "");
                        } catch (Exception var14) {
                           var43 = var14;
                           var10001 = false;
                           break label230;
                        }
                     } else {
                        try {
                           var10000 = var1;
                           var45 = var41;
                           Log.e("FBlueSDK", "BLE_VVV 数据在500ms内一直在变化");
                        } catch (Exception var13) {
                           var43 = var13;
                           var10001 = false;
                           break label230;
                        }

                        try {
                           var47 = var10000.equals(var45);
                        } catch (Exception var12) {
                           var43 = var12;
                           var10001 = false;
                           break label230;
                        }

                        if (var47) {
                           return;
                        }

                        try {
                           var53 = var3;
                           PreferenceUtilKt.putStr(this.mContext, "bind_address", "");
                        } catch (Exception var11) {
                           var43 = var11;
                           var10001 = false;
                           break label230;
                        }
                     }

                     try {
                        var53.onBindStatus(true);
                        return;
                     } catch (Exception var10) {
                        var43 = var10;
                        var10001 = false;
                        break label230;
                     }
                  }

                  try {
                     Log.e("FBlueSDK", "BLE_VVV mac地址为空或者mac地址的长度不等于12");
                     return;
                  } catch (Exception var17) {
                     var43 = var17;
                     var10001 = false;
                  }
               }
            } else {
               try {
                  Log.e("FBlueSDK", "BLE_VVV combinedData => 组合数据为null");
                  return;
               } catch (Exception var16) {
                  var43 = var16;
                  var10001 = false;
               }
            }
         }

         var43.printStackTrace();
      }

   }

   private boolean isDataStable(String var1) {
      long var2 = System.currentTimeMillis();
      if (var1.equals(this.lastCombinedData) && var2 - this.lastDataTimestamp >= 2000L) {
         return true;
      } else {
         if (!var1.equals(this.lastCombinedData)) {
            this.lastCombinedData = var1;
            this.lastDataTimestamp = var2;
         }

         return false;
      }
   }

   private void resetData() {
      this.lastCombinedData = "";
      this.lastDataTimestamp = 0L;
   }

   private void readSpecificCharacteristic(final BluetoothDevice var1) {
      this.mFscBleCentralApi.connect(var1.getAddress(), Boolean.FALSE);
      this.mFscBleCentralApi.setCallbacks(new FscBleCentralCallbacksImp() {
         public void readResponse(String var1x, BluetoothGattCharacteristic var2, String var3, String var4, byte[] var5) {
            FBlueSDK.this.mHandler.removeCallbacks(FBlueSDK.this.bindTimeout);
            FBlueSDK var10000 = FBlueSDK.this;
            if (var10000.isCompanyBluetoothModule(var10000.byteArrayToMacAddress(var5))) {
               FBlueSDK.this.bindDeviceSuccess(var1x);
               if (FBlueSDK.this.mBindCallback != null) {
                  FBlueSDK.this.mBindCallback.onBindResult(true);
               }
            } else {
               FBlueSDK.this.bindDeviceFailed();
               if (FBlueSDK.this.mBindCallback != null) {
                  FBlueSDK.this.mBindCallback.onBindResult(false);
               }
            }

            FBlueSDK.this.unpairDevice(var1);
            if (FBlueSDK.this.mFscBleCentralApi != null) {
               FBlueSDK.this.mFscBleCentralApi.disconnect(var1.getAddress());
            }

         }
      });
   }

   private boolean isCompanyBluetoothModule(String var1) {
      return var1 != null && var1.contains("DC:0D:30");
   }

   private void bindDeviceSuccess(String var1) {
      PreferenceUtilKt.putStr(this.mContext, "bind_address", var1);
      MsgLogger.i("FBlueSDK", "Device bound successfully: " + var1);
   }

   private void bindDeviceFailed() {
      PreferenceUtilKt.putStr(this.mContext, "bind_address", "");
   }

   public static boolean isTablet(Context var0) {
      UiModeManager var1;
      if ((var1 = (UiModeManager)var0.getSystemService("uimode")) != null && var1.getCurrentModeType() == 4) {
         return true;
      } else {
         return (var0.getResources().getConfiguration().screenLayout & 15) >= 3;
      }
   }

   public void init(Context var1) {
      this.mContext = var1;
      (this.mFscBleCentralApi = FscBleCentralApiImp.getInstance(var1)).initialize();
   }

   public void startScanning(final ScanResultCallback var1, final DataReceivedCallback var2, final BindStatusCallback var3) {
      FscBleCentralApi var4;
      if ((var4 = this.mFscBleCentralApi) != null) {
         var4.setCallbacks(new FscBleCentralCallbacksImp() {
            public void blePeripheralFound(BluetoothDevice var1x, FscDevice var2x, int var3x, byte[] var4) {
               String var6;
               if (var1 != null && (var6 = FileUtil.bytesToHex(var4, var4.length)).length() >= 16 && var6.contains("FFA055")) {
                  var1.scannerResult(var1x, var2x, var4);
               }

               if (var2 != null && var3 != null && PreferenceUtilKt.getStr(FBlueSDK.this.mContext, "bind_address", "").equals(var2x.getAddress())) {
                  FBlueSDK var10000 = FBlueSDK.this;
                  <undefinedtype> var10002 = this;
                  DataReceivedCallback var5 = var2;
                  var10000.parseScanRecord(var4, var5, var3);
               }

            }

            public void blePeripheralConnected(BluetoothGatt var1x, String var2x, ConnectType var3x) {
               FBlueSDK.this.isConnected = true;
            }

            public void blePeripheralDisconnected(BluetoothGatt var1x, String var2x, int var3x) {
               FBlueSDK.this.isConnected = false;
            }

            public void packetReceived(String var1x, String var2x, String var3x, byte[] var4) {
               Exception var10000;
               label50: {
                  <undefinedtype> var12;
                  <undefinedtype> var13;
                  boolean var10001;
                  String var10002;
                  try {
                     var12 = this;
                     var13 = this;
                     PreferenceUtilKt.putStr(FBlueSDK.this.mContext, "connected_mac", var1x);
                     var10002 = var3x.replace(" ", "");
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label50;
                  }

                  var1x = var10002;

                  int var15;
                  try {
                     var15 = PreferenceUtilKt.getInt(FBlueSDK.this.mContext, "session_key", -1);
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label50;
                  }

                  int var11 = var15;

                  byte[] var16;
                  try {
                     var16 = CryptoUtils.Companion.decrypt(ExpandKt.hexStringToByteArray(var1x), var11);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label50;
                  }

                  byte[] var10 = var16;

                  DataReceivedCallback var14;
                  try {
                     var14 = var2;
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label50;
                  }

                  if (var14 == null) {
                     return;
                  }

                  var12 = this;
                  String var17 = "FBlueSDK";

                  try {
                     MsgLogger.e(var17, "cjg packetReceived DataReceivedCallback decryptData => " + ExpandKt.bytesToHex(var10));
                     var2.onReceiveGattData(var10);
                     return;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }

               var10000.printStackTrace();
            }
         });
         this.mFscBleCentralApi.startScan();
      }

   }

   public void stopScanning() {
      if (isTablet(this.mContext) && this.isReceiverRegistered) {
         this.mContext.unregisterReceiver(this.bondStateReceiver);
         this.isReceiverRegistered = false;
      }

      FscBleCentralApi var1;
      if ((var1 = this.mFscBleCentralApi) != null) {
         var1.stopScan();
      }

   }

   public String currentBoundDevice() {
      return PreferenceUtilKt.getStr(this.mContext, "bind_address", "");
   }

   public String firmwareVersion() {
      return PreferenceUtilKt.getStr(this.mContext, "firmware_version", "");
   }

   public void bindDevice(List var1, FscDevice var2, BindCallback var3) {
      this.mHandler.postDelayed(this.bindTimeout, 30000L);
      this.mBindCallback = var3;
      final String var12;
      String var4 = (var12 = var2.getAddress()).replace(":", "");
      PreferenceUtilKt.putStr(this.mContext, "big_endian_mac", var4);
      String var5;
      String var10000 = var5 = var2.getBindFrames();
      String var9 = var2.getFirmwareVersion();
      if (var10000 == null) {
         this.bindDeviceFailed();
         this.mBindCallback.onBindResult(false);
      } else {
         PreferenceUtilKt.putStr(this.mContext, "binding_frames", var5);
         PreferenceUtilKt.putStr(this.mContext, "firmware_version", var9);
         CryptoUtils.Companion var10;
         int[] var10003 = (var10 = CryptoUtils.Companion).convertMacAddressToIntArray(var4);
         var10.tea(var10003, 16L);
         int var11 = var10003[0];
         PreferenceUtilKt.putInt(this.mContext, "session_key", var11);
         PreferenceUtilKt.putStr(this.mContext, "bind_address", "");
         if (isTablet(this.mContext)) {
            for(var11 = 0; var11 < var1.size(); ++var11) {
               if (((BluetoothDevice)var1.get(var11)).getAddress().equals(var12)) {
                  this.bluetoothDevice = (BluetoothDevice)var1.get(var11);
               }
            }

            if (!this.isReceiverRegistered) {
               IntentFilter var7;
               var7 = new IntentFilter.<init>("android.bluetooth.device.action.BOND_STATE_CHANGED");
               this.mContext.registerReceiver(this.bondStateReceiver, var7);
               this.isReceiverRegistered = true;
            }

            BluetoothDevice var6;
            if (this.mFscBleCentralApi != null && (var6 = this.bluetoothDevice) != null) {
               MsgLogger.e("FBlueSDK", "bindDevice boundState => " + var6.createBond());
            }
         } else {
            FscBleCentralApi var8;
            if ((var8 = this.mFscBleCentralApi) != null) {
               var8.connect(var12, Boolean.FALSE);
               this.mFscBleCentralApi.setCallbacks(new FscBleCentralCallbacksImp() {
                  public void blePeripheralConnected(BluetoothGatt var1, String var2, ConnectType var3) {
                     String var4 = FileUtil.bytesToHex(new byte[]{-4, -70, 0, 2}, 4);
                     int var6 = PreferenceUtilKt.getInt(FBlueSDK.this.mContext, "session_key", -1);
                     byte[] var5 = CryptoUtils.Companion.encrypt(ExpandKt.toByteArray(var4), var6);
                     if (FBlueSDK.this.mFscBleCentralApi != null) {
                        FBlueSDK.this.mHandler.postDelayed(() -> {
                           FBlueSDK.this.mFscBleCentralApi.send(var5);
                        }, 3000L);
                     }

                  }

                  public void blePeripheralDisconnected(BluetoothGatt var1, String var2, int var3) {
                     FBlueSDK.this.bindDeviceFailed();
                     FBlueSDK.this.mBindCallback.onBindResult(false);
                     FBlueSDK.this.mHandler.removeCallbacks(FBlueSDK.this.bindTimeout);
                  }

                  public void writeResponse(byte[] var1) {
                     FBlueSDK.this.bindDeviceSuccess(var12);
                     FBlueSDK.this.mBindCallback.onBindResult(true);
                     FBlueSDK.this.mHandler.removeCallbacks(FBlueSDK.this.bindTimeout);
                     if (FBlueSDK.this.mFscBleCentralApi != null) {
                        FBlueSDK.this.mFscBleCentralApi.disconnect(var12);
                     }

                  }
               });
            }
         }

      }
   }

   public boolean unpairDevice(BluetoothDevice param1) {
      // $FF: Couldn't be decompiled
   }

   public String byteArrayToMacAddress(byte[] var1) {
      if (var1 != null && var1.length == 6) {
         StringBuilder var4;
         var4 = new StringBuilder.<init>();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Object[] var3;
            (var3 = new Object[1])[0] = var1[var2];
            var4.append(String.format("%02X", var3));
            if (var2 < var1.length - 1) {
               var4.append(":");
            }
         }

         return var4.toString();
      } else {
         throw new IllegalArgumentException("Invalid MAC address byte array");
      }
   }

   public boolean sendData(byte[] var1) {
      String var3 = FileUtil.bytesToHex(var1, var1.length);
      int var2 = PreferenceUtilKt.getInt(this.mContext, "session_key", -1);
      var1 = CryptoUtils.Companion.encrypt(ExpandKt.toByteArray(var3), var2);
      FscBleCentralApi var4;
      if ((var4 = this.mFscBleCentralApi) != null && var4.isConnected()) {
         if (this.mFscBleCentralApi.send(var1)) {
            MsgLogger.e("FBlueSDK", "BLE_VVV sendData 数据发送成功 isSend => true");
            return true;
         } else {
            MsgLogger.e("FBlueSDK", "BLE_VVV sendData 数据发送失败 isSend => false");
            return false;
         }
      } else {
         MsgLogger.e("FBlueSDK", "BLE_VVV sendData mFscBleCentralApi == null 或者 设备断开连接");
         return false;
      }
   }

   public void disconnect() {
      FscBleCentralApi var1;
      if ((var1 = this.mFscBleCentralApi) != null && var1.isConnected()) {
         this.mFscBleCentralApi.disconnect();
      }

   }

   public boolean isBlank(String var1) {
      return var1 == null || var1.trim().isEmpty();
   }
}
