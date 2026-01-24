package com.feasycom.ble.controler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import com.feasycom.ble.bean.BluetoothDeviceWrapper;
import com.feasycom.ble.utils.FeasycomUtil;
import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.bean.ScannerFilter;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.ota.utils.FileUtil;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FscBleCentralApiImp implements FscBleCentralApi, BluetoothDeviceWrapper.ConnectCallback {
   private static final String TAG = "FscBleCentralApiImp";
   private static Context sContext;
   private static FscBleCentralApi instance;
   public static boolean isDebug;
   private BluetoothManager mBluetoothManager;
   private BluetoothAdapter mBluetoothAdapter;
   private FscBleCentralCallbacks mFscBleCentralCallbacks;
   private final Handler mHandler = new Handler(Looper.getMainLooper());
   private ScanSettings mScanSettings;
   private Boolean SCAN_FINISH;
   private boolean scanSuccess;
   private BluetoothLeScanner bluetoothLeScanner;
   private final int MAX_CONNECT_NUMBER;
   private final Map deviceMap;
   private final ArrayList addressList;
   private final BluetoothAdapter.LeScanCallback scanCallbacks;
   private final ScanCallback mScanCallback;
   private long lastRefreshTime;

   public static String getLastTwoBytes(String var0) {
      if (var0.length() % 2 == 0) {
         return var0.substring(var0.length() - 4);
      } else {
         throw new IllegalArgumentException("Hex string must have an even number of characters.");
      }
   }

   private FscBleCentralApiImp() {
      this.SCAN_FINISH = Boolean.TRUE;
      this.scanSuccess = false;
      this.MAX_CONNECT_NUMBER = 6;
      this.deviceMap = new HashMap();
      this.addressList = new ArrayList();
      this.scanCallbacks = new BluetoothAdapter.LeScanCallback() {
         public void onLeScan(BluetoothDevice var1, int var2, byte[] var3) {
            <undefinedtype> var10000 = this;
            FscBleCentralApiImp.this.scanSuccess = true;
            FscDevice var5;
            FscDevice var10001 = var5 = new FscDevice;
            String var4 = var1.getName();
            var5.<init>(var4, var1.getAddress(), var1, var2, "BLE", "", "");
            var5.setScanRecord(var3);
            FeasycomUtil.parseAdvData(var10001, var3);
            MsgLogger.e(FscBleCentralApiImp.TAG, "scanCallbacks rssi => " + var2);
            FscBleCentralApiImp.this.mFscBleCentralCallbacks.blePeripheralFound(var5, var2, var3);
         }
      };
      this.mScanCallback = new ScanCallback() {
         @SuppressLint({"MissingPermission"})
         public void onScanResult(int var1, ScanResult var2) {
            ScanResult var10000 = var2;
            ScanResult var10001 = var2;
            super.onScanResult(var1, var2);
            FscBleCentralApiImp.this.scanSuccess = true;
            BluetoothDevice var9;
            String var10 = (var9 = var2.getDevice()).getAddress();
            int var3 = var10001.getRssi();
            byte[] var4;
            byte[] var11 = var4 = var10000.getScanRecord().getBytes();
            String var5;
            if ((var5 = FileUtil.bytesToHex(var11, var11.length)).length() >= 14) {
               String var6 = null;
               String var7;
               FscDevice var8;
               if (var5.contains("FFA055")) {
                  var6 = var5.substring(14, 16);
                  var7 = FscBleCentralApiImp.getLastTwoBytes(var5);
                  var8 = new FscDevice.<init>(var9.getName(), var10, var9, var3, "BLE", var6, var7);
                  MsgLogger.d(FscBleCentralApiImp.TAG, "Detected binding frame device: " + var5);
               } else {
                  String var12 = var5;
                  var5 = var5.substring(8, 10);
                  var7 = var12.substring(12, 14);
                  if ("FF".equals(var5) && "A1".equals(var7)) {
                     var8 = new FscDevice.<init>(var9.getName(), var10, var9, var3, "BLE");
                  } else {
                     var8 = var6;
                  }
               }

               if (var8 != null) {
                  var8.setScanRecord(var4);
                  FscBleCentralApiImp.this.mFscBleCentralCallbacks.blePeripheralFound(var9, var8, var3, var4);
               }
            }

         }

         public void onBatchScanResults(List var1) {
            super.onBatchScanResults(var1);
         }

         @SuppressLint({"MissingPermission"})
         public void onScanFailed(int var1) {
            MsgLogger.e("=======================onScanFailed 错误码 => " + var1 + " =================");
            if (var1 == 2 && FscBleCentralApiImp.this.mBluetoothAdapter != null) {
               FscBleCentralApiImp.this.mBluetoothAdapter.disable();
               FscBleCentralApiImp.this.mHandler.postDelayed(() -> {
                  FscBleCentralApiImp.this.mBluetoothAdapter.enable();
                  FscBleCentralApiImp.this.startScan();
               }, 1000L);
            }

         }
      };
      this.lastRefreshTime = 0L;
      MsgLogger.e(TAG, "BLE_VVV FeasyBlue SDK Version: 3.5.1.2");
   }

   @Keep
   public static FscBleCentralApi getInstance(Context var0) {
      sContext = var0;
      if (instance == null) {
         instance = new FscBleCentralApiImp();
      }

      return instance;
   }

   @Keep
   public static FscBleCentralApi getInstance() {
      if (instance == null) {
         instance = new FscBleCentralApiImp();
      }

      return instance;
   }

   private void initializeScanner() {
      BluetoothAdapter var1;
      BluetoothLeScanner var2;
      if ((var1 = this.mBluetoothAdapter) != null) {
         var2 = var1.getBluetoothLeScanner();
      } else {
         var2 = null;
      }

      this.bluetoothLeScanner = var2;
   }

   @SuppressLint({"NewApi"})
   private ScanSettings createOreoAndAboveScanSettings() {
      return (new ScanSettings.Builder()).setScanMode(2).setNumOfMatches(3).setMatchMode(2).setCallbackType(1).setReportDelay(0L).setLegacy(false).setPhy(255).build();
   }

   private ScanSettings createPreOreoScanSettings() {
      return (new ScanSettings.Builder()).setScanMode(2).setReportDelay(0L).build();
   }

   private void retryScan() {
      MsgLogger.e(TAG, "cjg retryScan......");
      this.stopScan();
      this.startScan((ScannerFilter)null);
   }

   public void isShowLog(boolean var1) {
      isDebug = var1;
   }

   public boolean initialize() {
      if (this.mBluetoothManager == null && (this.mBluetoothManager = (BluetoothManager)sContext.getSystemService("bluetooth")) == null) {
         return false;
      } else if (this.mBluetoothAdapter == null && (this.mBluetoothAdapter = this.mBluetoothManager.getAdapter()) == null) {
         return false;
      } else {
         ScanSettings var1;
         if (VERSION.SDK_INT >= 26) {
            var1 = this.createOreoAndAboveScanSettings();
         } else {
            var1 = this.createPreOreoScanSettings();
         }

         this.mScanSettings = var1;
         this.initializeScanner();
         return true;
      }
   }

   public boolean isEnabled() {
      BluetoothAdapter var1;
      return (var1 = this.mBluetoothAdapter) == null ? false : var1.isEnabled();
   }

   @Keep
   public void setCallbacks(FscBleCentralCallbacks var1) {
      this.mFscBleCentralCallbacks = var1;
      Iterator var2 = this.deviceMap.values().iterator();

      while(var2.hasNext()) {
         ((BluetoothDeviceWrapper)var2.next()).setCharacteristic(var1);
      }

   }

   public void connect(String var1, Boolean var2) {
      BluetoothDevice var3;
      if (this.deviceMap.size() < 6 && (var3 = this.mBluetoothAdapter.getRemoteDevice(var1)) != null) {
         FscBleCentralApiImp var10000 = this;
         FscBleCentralApiImp var10001 = this;
         BluetoothDeviceWrapper var4;
         BluetoothDeviceWrapper var10002 = var4 = new BluetoothDeviceWrapper;
         var4.<init>(var3.getAddress(), var3, sContext, this);
         FscBleCentralCallbacks var5 = this.mFscBleCentralCallbacks;
         var10002.connect(var5, var2);
         var10001.deviceMap.put(var1, var4);
         var10000.addressList.add(var1);
      }

   }

   public void connectToModify(String var1) {
      BluetoothDevice var2;
      if (this.deviceMap.size() < 6 && (var2 = this.mBluetoothAdapter.getRemoteDevice(var1)) != null) {
         BluetoothDeviceWrapper var3;
         BluetoothDeviceWrapper var10002 = var3 = new BluetoothDeviceWrapper;
         var3.<init>(var2.getAddress(), var2, sContext, this);
         var10002.connectToModify(this.mFscBleCentralCallbacks);
         this.deviceMap.put(var1, var3);
         this.addressList.add(var1);
      }

   }

   public void connectToOTAWithFactory(@Nullable String var1, @NotNull byte[] var2, boolean var3) {
      if (this.deviceMap.size() < 6) {
         BluetoothDevice var4 = this.mBluetoothAdapter.getRemoteDevice(var1);
         BluetoothDeviceWrapper var5;
         BluetoothDeviceWrapper var10002 = var5 = new BluetoothDeviceWrapper;
         var5.<init>(var4.getAddress(), var4, sContext, this);
         var10002.connectToOTAWithFactory(this.mFscBleCentralCallbacks, var2, var3, Integer.MAX_VALUE);
         this.deviceMap.put(var1, var5);
         this.addressList.add(var1);
      }

   }

   public void connectToVerifyOTAWithFactory(String var1, byte[] var2, boolean var3, boolean var4) {
   }

   public void startOTA(String var1, byte[] var2, boolean var3) {
   }

   @SuppressLint({"MissingPermission"})
   public boolean clearDevice(String var1) {
      Iterator var12 = this.mBluetoothAdapter.getBondedDevices().iterator();

      while(true) {
         BluetoothDevice var2;
         do {
            if (!var12.hasNext()) {
               return false;
            }
         } while(!(var2 = (BluetoothDevice)var12.next()).getAddress().equals(var1));

         Object var10000;
         label64: {
            Class var13;
            boolean var10001;
            try {
               var13 = var2.getClass();
            } catch (NoSuchMethodException var9) {
               var10000 = var9;
               var10001 = false;
               break label64;
            } catch (IllegalAccessException var10) {
               var10000 = var10;
               var10001 = false;
               break label64;
            } catch (InvocationTargetException var11) {
               var10000 = var11;
               var10001 = false;
               break label64;
            }

            String var14 = "removeBond";
            Object var10002 = null;

            Method var15;
            try {
               var15 = var13.getMethod(var14, (Class[])var10002);
            } catch (NoSuchMethodException var6) {
               var10000 = var6;
               var10001 = false;
               break label64;
            } catch (IllegalAccessException var7) {
               var10000 = var7;
               var10001 = false;
               break label64;
            } catch (InvocationTargetException var8) {
               var10000 = var8;
               var10001 = false;
               break label64;
            }

            BluetoothDevice var16 = var2;
            var10002 = null;

            try {
               return (Boolean)var15.invoke(var16, (Object[])var10002);
            } catch (NoSuchMethodException var3) {
               var10000 = var3;
               var10001 = false;
            } catch (IllegalAccessException var4) {
               var10000 = var4;
               var10001 = false;
            } catch (InvocationTargetException var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         ((ReflectiveOperationException)var10000).printStackTrace();
      }
   }

   public void disconnect() {
      if (!this.addressList.isEmpty()) {
         ArrayList var10001 = this.addressList;
         this.disconnect((String)var10001.get(var10001.size() - 1));
      }
   }

   public void disconnect(String var1) {
      BluetoothDeviceWrapper var2;
      if ((var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var2.disconnect();
         this.deviceMap.remove(var1);
         this.addressList.remove(var1);
      }

   }

   @SuppressLint({"MissingPermission"})
   public List getBondDevices() {
      FscBleCentralApiImp var10000 = this;
      ArrayList var4;
      var4 = new ArrayList.<init>();
      Iterator var1 = var10000.mBluetoothAdapter.getBondedDevices().iterator();

      while(var1.hasNext()) {
         BluetoothDevice var2 = (BluetoothDevice)var1.next();
         String var3 = var2.getName();
         var4.add(new FscDevice(var3, var2.getAddress(), var2, 0, "BLE", "", ""));
      }

      return var4;
   }

   public void startScan() {
      this.startScan((ScannerFilter)null);
   }

   @SuppressLint({"MissingPermission", "ObsoleteSdkInt"})
   public void startScan(ScannerFilter var1) {
      long var2;
      if ((var2 = System.currentTimeMillis()) - this.lastRefreshTime < 20000L) {
         MsgLogger.e(TAG, "刷新间隔太短，忽略此次刷新请求");
      } else {
         this.lastRefreshTime = var2;
         if (this.mBluetoothAdapter == null || !this.SCAN_FINISH) {
            this.scanSuccess = false;
            this.mHandler.postDelayed(() -> {
               if (!this.scanSuccess) {
                  this.stopScan();
                  this.startScan();
               }

            }, 3000L);
         }

         FscBleCentralApiImp var10000 = this;
         this.stopScan();
         MsgLogger.e(TAG, "cjg startScan......");

         Exception var18;
         label92: {
            boolean var10001;
            BluetoothLeScanner var19;
            try {
               var19 = var10000.bluetoothLeScanner;
            } catch (Exception var12) {
               var18 = var12;
               var10001 = false;
               break label92;
            }

            BluetoothLeScanner var14 = var19;
            if (var19 != null) {
               var19 = var14;
               FscBleCentralApiImp var20 = this;
               FscBleCentralApiImp var10002 = this;
               var1 = null;

               ScanSettings var21;
               try {
                  var21 = var10002.mScanSettings;
               } catch (Exception var11) {
                  var18 = var11;
                  var10001 = false;
                  break label92;
               }

               ScanSettings var16 = var21;

               ScanCallback var22;
               try {
                  var22 = var20.mScanCallback;
               } catch (Exception var10) {
                  var18 = var10;
                  var10001 = false;
                  break label92;
               }

               ScanCallback var3 = var22;

               try {
                  var19.startScan(var1, var16, var3);
               } catch (Exception var9) {
                  var18 = var9;
                  var10001 = false;
                  break label92;
               }
            } else {
               try {
                  this.mBluetoothAdapter.startLeScan(this.scanCallbacks);
               } catch (Exception var8) {
                  var18 = var8;
                  var10001 = false;
                  break label92;
               }
            }

            FscBleCentralCallbacks var24;
            try {
               this.SCAN_FINISH = Boolean.FALSE;
               var24 = this.mFscBleCentralCallbacks;
            } catch (Exception var7) {
               var18 = var7;
               var10001 = false;
               break label92;
            }

            FscBleCentralCallbacks var15 = var24;
            if (var24 != null) {
               try {
                  var15.startScan();
               } catch (Exception var6) {
                  var18 = var6;
                  var10001 = false;
                  break label92;
               }
            }

            var10000 = this;
            int var17 = 240000;

            Runnable var23;
            Handler var25;
            int var26;
            try {
               var25 = var10000.mHandler;
               var26 = var17;
               var23 = this::retryScan;
            } catch (Exception var5) {
               var18 = var5;
               var10001 = false;
               break label92;
            }

            Runnable var13 = var23;
            var2 = (long)var26;

            try {
               var25.postDelayed(var13, var2);
               return;
            } catch (Exception var4) {
               var18 = var4;
               var10001 = false;
            }
         }

         var18.printStackTrace();
      }
   }

   @SuppressLint({"MissingPermission"})
   public void stopScan() {
      // $FF: Couldn't be decompiled
   }

   public boolean pauseSend(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.pauseSend() : false;
   }

   public boolean continueSend(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.continueSend() : false;
   }

   public boolean isConnected() {
      return this.addressList.isEmpty() ^ true;
   }

   public boolean isConnected(String var1) {
      return this.deviceMap.containsKey(var1);
   }

   public void setSendInterval(String var1, Long var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var3.setSendInterval(var2);
      }

   }

   public boolean send(String var1) {
      if (this.addressList.isEmpty()) {
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.send((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   public boolean send(String var1, String var2) {
      return this.send(var1, var2.getBytes());
   }

   public boolean send(byte[] var1) {
      if (this.addressList.isEmpty()) {
         MsgLogger.e(TAG, "send addressList isEmpty...");
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.send((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   public boolean send(String var1, byte[] var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) == null) {
         MsgLogger.e(TAG, "BLE_VVV send bluetoothDeviceWrapper == null...");
         return false;
      } else {
         boolean var4;
         boolean var10000 = var4 = var3.send(var2);
         MsgLogger.e(TAG, "BLE_VVV bluetoothDeviceWrapper sendData => " + var4);
         return var10000;
      }
   }

   public boolean sendFile(int var1) {
      if (this.addressList.isEmpty()) {
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.sendFile((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   public boolean sendFile(String var1, int var2) {
      BluetoothDeviceWrapper var3;
      return (var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var3.sendFile(var2) : false;
   }

   public void sendATCommand(Set var1) {
      if (!this.addressList.isEmpty()) {
         ArrayList var10001 = this.addressList;
         this.sendATCommand((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   public void sendATCommand(String var1, Set var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var3.sendATCommand(var2);
      }

   }

   public int getMtu() {
      if (this.addressList.isEmpty()) {
         return 0;
      } else {
         ArrayList var10001 = this.addressList;
         return this.getMtu((String)var10001.get(var10001.size() - 1));
      }
   }

   public int getMtu(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.getMRealMtu() : 0;
   }

   public int getMaximumPacketByte() {
      if (this.addressList.isEmpty()) {
         return 0;
      } else {
         ArrayList var10001 = this.addressList;
         return this.getMaximumPacketByte((String)var10001.get(var10001.size() - 1));
      }
   }

   public int getMaximumPacketByte(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.getMaximumPacketByte() : 0;
   }

   public void stopSend() {
      if (!this.addressList.isEmpty()) {
         ArrayList var10001 = this.addressList;
         this.stopSend((String)var10001.get(var10001.size() - 1));
      }
   }

   public void stopSend(String var1) {
      BluetoothDeviceWrapper var2;
      if ((var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var2.stopSend();
      }

   }

   public boolean setCharacteristic(BluetoothGattCharacteristic var1, int var2) {
      if (this.addressList.isEmpty()) {
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.setCharacteristic((String)var10001.get(var10001.size() - 1), var1, var2);
      }
   }

   public boolean setCharacteristic(String var1, BluetoothGattCharacteristic var2, int var3) {
      BluetoothDeviceWrapper var4;
      return (var4 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var4.setCharacteristic(var2, var3) : false;
   }

   public boolean read(String var1, BluetoothGattCharacteristic var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var3.read(var2);
      }

      return false;
   }

   public List getBluetoothGattServices(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.getBluetoothGattServices() : null;
   }

   public BluetoothGattCharacteristic getWriteCharacteristic(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.getWriteCharacteristic() : null;
   }

   public List getNotifyCharacteristicList(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.getNotifyCharacteristicList() : null;
   }

   @RequiresApi(
      api = 21
   )
   public void requestMtu(int var1) {
      if (!this.addressList.isEmpty()) {
         ArrayList var10001 = this.addressList;
         this.requestMtu((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   @RequiresApi(
      api = 21
   )
   public void requestMtu(String var1, int var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var3.requestMtu(var2);
      }

   }

   public void createBond() {
      Iterator var1 = this.deviceMap.values().iterator();

      while(var1.hasNext()) {
         ((BluetoothDeviceWrapper)var1.next()).createBond();
      }

   }

   public void success(@NotNull BluetoothDeviceWrapper var1) {
   }

   public void failure(BluetoothDeviceWrapper var1) {
      if ((BluetoothDeviceWrapper)this.deviceMap.get(var1.getMAddress()) != null) {
         this.deviceMap.remove(var1.getMAddress());
         this.addressList.remove(var1.getMAddress());
      }

   }

   public boolean sendFile(@NotNull InputStream var1) {
      ArrayList var10001 = this.addressList;
      return this.sendFile((String)var10001.get(var10001.size() - 1), var1);
   }

   public boolean sendFile(@NotNull byte[] var1) {
      ArrayList var10001 = this.addressList;
      return this.sendFile((String)var10001.get(var10001.size() - 1), var1);
   }

   public boolean sendFile(@Nullable String var1, @NotNull InputStream var2) {
      BluetoothDeviceWrapper var3;
      return (var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var3.sendFile(var2) : false;
   }

   public boolean sendFile(@Nullable String var1, @NotNull byte[] var2) {
      BluetoothDeviceWrapper var3;
      return (var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var3.sendFile(var2) : false;
   }
}
