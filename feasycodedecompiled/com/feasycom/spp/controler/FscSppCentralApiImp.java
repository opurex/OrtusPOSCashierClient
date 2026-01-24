package com.feasycom.spp.controler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.utils.FileUtilsKt;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.network.utils.NetworkUtil;
import com.feasycom.spp.bean.BluetoothDeviceWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FscSppCentralApiImp implements FscSppCentralApi, BluetoothDeviceWrapper.ConnectCallback {
   private static final String TAG = "FscSppApiImp";
   private static FscSppCentralApi instance;
   @SuppressLint({"StaticFieldLeak"})
   private static Context mContext;
   public static String CUSTOM_SPP_UUID = "";
   // $FF: synthetic field
   public static final boolean $assertionsDisabled = FscSppCentralApiImp.class.desiredAssertionStatus() ^ true;
   private FscSppCentralCallbacks mUiCallback;
   private BluetoothManager mBluetoothManager;
   private BluetoothAdapter mBluetoothAdapter;
   private IntentFilter intentFilter = null;
   private BluetoothSocket mSocket;
   private Thread acceptThread;
   public String mDeviceAddress = null;
   private final Map deviceMap = new HashMap();
   private final ArrayList addressList = new ArrayList();
   private final BroadcastReceiver receiver = new BroadcastReceiver() {
      @SuppressLint({"MissingPermission"})
      public void onReceive(Context var1, Intent var2) {
         if (FscSppCentralApiImp.this.mUiCallback != null) {
            String var5;
            String var10000 = var5 = var2.getAction();
            var10000.hashCode();
            byte var3 = -1;
            switch (var10000.hashCode()) {
               case -1780914469:
                  if (var5.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
                     var3 = 0;
                  }
                  break;
               case -1481831396:
                  if (var5.equals("android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE")) {
                     var3 = 1;
                  }
                  break;
               case 6759640:
                  if (var5.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                     var3 = 2;
                  }
                  break;
               case 1123270207:
                  if (var5.equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
                     var3 = 3;
                  }
                  break;
               case 1167529923:
                  if (var5.equals("android.bluetooth.device.action.FOUND")) {
                     var3 = 4;
                  }
                  break;
               case 2116862345:
                  if (var5.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                     var3 = 5;
                  }
            }

            switch (var3) {
               case 0:
                  FscSppCentralApiImp.this.mUiCallback.stopScan();
                  break;
               case 1:
                  MsgLogger.e("SPP onReceive: EXTRA_PREVIOUS_CONNECTION_STATE");
                  break;
               case 2:
                  FscSppCentralApiImp.this.mUiCallback.startScan();
                  break;
               case 3:
                  if (var2.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", 0) != 2) {
                     boolean var10 = true;
                  }
                  break;
               case 4:
                  BluetoothDevice var6;
                  if ((var6 = (BluetoothDevice)var2.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getType() != 1 && var6.getType() != 3) {
                     return;
                  }

                  <undefinedtype> var9 = this;
                  short var4 = var2.getExtras().getShort("android.bluetooth.device.extra.RSSI");
                  FscDevice var7;
                  FscDevice var10001 = var7 = new FscDevice;
                  String var8 = var6.getName();
                  var10001.<init>(var8, var6.getAddress(), var6, var4, "SPP", "", "");
                  FscSppCentralApiImp.this.mUiCallback.sppPeripheralFound(var7, var4);
                  break;
               case 5:
                  switch (var2.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", 0)) {
                     case 10:
                        MsgLogger.e("SPP onReceive => BOND_NONE");
                        break;
                     case 11:
                        MsgLogger.e("SPP onReceive => 绑定中");
                        FscSppCentralApiImp.this.mUiCallback.bondIng();
                        break;
                     case 12:
                        MsgLogger.e("SPP onReceive => 绑定完成");
                  }
            }

         }
      }
   };

   private FscSppCentralApiImp() {
      String var2 = FileUtilsKt.getAppName(mContext);
      String var1 = FileUtilsKt.getAppVersion(mContext);
      NetworkUtil.feedback(mContext, "FeasyBlue<SPP-SDK启动记录>" + "\n1.App Name = " + var2 + "\n2.App Version = " + var1 + "\n3.Package Name = " + FileUtilsKt.getPackageName(mContext) + "\n4.SDK Version = " + "3.5.1.1", false);
   }

   @Keep
   public static FscSppCentralApi getInstance(Context var0) {
      mContext = var0;
      if (instance == null) {
         instance = new FscSppCentralApiImp();
      }

      return instance;
   }

   @Keep
   public static FscSppCentralApi getInstance() {
      FscSppCentralApi var0;
      if ((var0 = instance) != null) {
         return var0;
      } else {
         throw new RuntimeException("Context is null");
      }
   }

   private String formatUUIDString(String var1) {
      Object[] var2;
      Object[] var10000 = var2 = new Object[5];
      var2[0] = var1.substring(0, 8);
      var2[1] = var1.substring(8, 12);
      var2[2] = var1.substring(12, 16);
      var2[3] = var1.substring(16, 20);
      var10000[4] = var1.substring(20, 32);
      return String.format("%s-%s-%s-%s-%s", var2);
   }

   private IntentFilter getIntentFilter() {
      if (this.intentFilter == null) {
         (this.intentFilter = new IntentFilter()).addAction("android.bluetooth.device.action.FOUND");
         this.intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
         this.intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
         this.intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
         this.intentFilter.addAction("android.bluetooth.adapter.extra.CONNECTION_STATE");
         this.intentFilter.addAction("android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE");
         this.intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
      }

      return this.intentFilter;
   }

   public void isShowLog(boolean var1) {
   }

   public boolean isEnabled() {
      BluetoothAdapter var1;
      return (var1 = this.mBluetoothAdapter) == null ? false : var1.isEnabled();
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

   public boolean initialize() {
      if (this.mBluetoothManager == null && (this.mBluetoothManager = (BluetoothManager)mContext.getSystemService("bluetooth")) == null) {
         return false;
      } else {
         if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
         }

         boolean var4 = false;

         label99: {
            Context var10000;
            BroadcastReceiver var1;
            Intent var9;
            label98: {
               try {
                  var4 = true;
                  mContext.getApplicationContext().unregisterReceiver(this.receiver);
                  var4 = false;
                  break label98;
               } catch (RuntimeException var5) {
                  System.out.println(var5);
                  var4 = false;
               } finally {
                  if (var4) {
                     var10000 = mContext.getApplicationContext();
                     FscSppCentralApiImp var10001 = this;
                     BroadcastReceiver var7 = this.receiver;
                     Intent var8;
                     if ((var8 = var10000.registerReceiver(var7, var10001.getIntentFilter(), 2)) == null) {
                        MsgLogger.e("SPP intent is null");
                     } else {
                        MsgLogger.e("SPP => " + var8.toString());
                     }

                  }
               }

               var10000 = mContext.getApplicationContext();
               var1 = this.receiver;
               if ((var9 = var10000.registerReceiver(var1, this.getIntentFilter(), 2)) != null) {
                  MsgLogger.e("SPP => " + var9.toString());
                  return this.mBluetoothAdapter != null;
               }
               break label99;
            }

            var10000 = mContext.getApplicationContext();
            var1 = this.receiver;
            if ((var9 = var10000.registerReceiver(var1, this.getIntentFilter(), 2)) != null) {
               MsgLogger.e("SPP => " + var9.toString());
               return this.mBluetoothAdapter != null;
            }
         }

         MsgLogger.e("SPP intent is null");
         return this.mBluetoothAdapter != null;
      }
   }

   @Keep
   public void setCallbacks(FscSppCentralCallbacks var1) {
      this.mUiCallback = var1;
      Iterator var2 = this.deviceMap.values().iterator();

      while(var2.hasNext()) {
         ((BluetoothDeviceWrapper)var2.next()).setCharacteristic(var1);
      }

   }

   public boolean isEnabledSDP() {
      return this.acceptThread != null;
   }

   @SuppressLint({"MissingPermission"})
   public void openSdpService(String var1) {
      (this.acceptThread = new Thread(() -> {
         IOException var10000;
         label70: {
            BluetoothAdapter var14;
            boolean var10001;
            try {
               var14 = this.mBluetoothAdapter;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label70;
            }

            BluetoothAdapter var2 = var14;
            if (var14 == null) {
               return;
            }

            FscSppCentralApiImp var15 = this;
            BluetoothAdapter var16 = var2;
            String var10002 = var1;
            String var12 = "Feasycom";

            BluetoothSocket var19;
            try {
               BluetoothServerSocket var17 = var16.listenUsingRfcommWithServiceRecord(var12, UUID.fromString(var10002));
               CUSTOM_SPP_UUID = var1;
               MsgLogger.e("SPP openSdpService => 开启SDP服务.");
               var19 = var17.accept();
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label70;
            }

            BluetoothSocket var20 = var19;

            BluetoothDevice var22;
            try {
               this.mSocket = var20;
               var22 = var19.getRemoteDevice();
            } catch (IOException var7) {
               var10000 = var7;
               var10001 = false;
               break label70;
            }

            BluetoothDevice var11 = var22;

            boolean var18;
            try {
               MsgLogger.e("SPP 接受客户连接 , 远端设备名字 => " + var11.getName() + " , 远端设备地址 => " + var11.getAddress());
               var18 = var15.mSocket.isConnected();
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break label70;
            }

            if (!var18) {
               return;
            }

            BluetoothDeviceWrapper var21;
            try {
               var21 = new BluetoothDeviceWrapper;
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label70;
            }

            BluetoothDeviceWrapper var13 = var21;

            FscSppCentralApiImp var23;
            try {
               var23 = this;
               var13.<init>(var11.getAddress(), var11, mContext, this);
               var20 = this.mSocket;
            } catch (IOException var4) {
               var10000 = var4;
               var10001 = false;
               break label70;
            }

            BluetoothSocket var10 = var20;

            try {
               var21.setSocket(var10, var23.mUiCallback);
               return;
            } catch (IOException var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         var10000.printStackTrace();
      })).start();
   }

   public void closeSdpService() {
      Object var10000;
      label60: {
         BluetoothSocket var13;
         boolean var10001;
         try {
            MsgLogger.e("SPP closeSdpService => 关闭SDP服务");
            var13 = this.mSocket;
         } catch (NullPointerException var10) {
            var10000 = var10;
            var10001 = false;
            break label60;
         } catch (IOException var11) {
            var10000 = var11;
            var10001 = false;
            break label60;
         }

         BluetoothSocket var1 = var13;
         if (var13 != null) {
            try {
               var1.close();
            } catch (NullPointerException var8) {
               var10000 = var8;
               var10001 = false;
               break label60;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label60;
            }
         }

         Thread var14;
         try {
            var14 = this.acceptThread;
         } catch (NullPointerException var6) {
            var10000 = var6;
            var10001 = false;
            break label60;
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label60;
         }

         Thread var12 = var14;
         if (var14 == null) {
            return;
         }

         boolean var15;
         try {
            var15 = var12.isAlive();
         } catch (NullPointerException var4) {
            var10000 = var4;
            var10001 = false;
            break label60;
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
            break label60;
         }

         if (!var15) {
            return;
         }

         try {
            this.acceptThread.interrupt();
            this.acceptThread = null;
            return;
         } catch (NullPointerException var2) {
            var10000 = var2;
            var10001 = false;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      ((Exception)var10000).printStackTrace();
   }

   @SuppressLint({"MissingPermission"})
   public void connect(String var1) {
      this.mBluetoothAdapter.cancelDiscovery();
      (new BluetoothDeviceWrapper(var1, this.mBluetoothAdapter.getRemoteDevice(var1), mContext, this)).connect(this.mUiCallback);
      this.mDeviceAddress = var1;
   }

   @SuppressLint({"MissingPermission"})
   public void connectToModify(String var1) {
      this.mBluetoothAdapter.cancelDiscovery();
      (new BluetoothDeviceWrapper(var1, this.mBluetoothAdapter.getRemoteDevice(var1), mContext, this)).connectToModify(this.mUiCallback);
   }

   @SuppressLint({"MissingPermission"})
   public void connectToOTAWithFactory(@Nullable String var1, @NotNull byte[] var2, boolean var3) {
      this.mBluetoothAdapter.cancelDiscovery();
      if (!$assertionsDisabled && var1 == null) {
         throw new AssertionError();
      } else {
         (new BluetoothDeviceWrapper(var1, this.mBluetoothAdapter.getRemoteDevice(var1), mContext, this)).connectToOTAWithFactory(this.mUiCallback, var2, var3, Integer.MAX_VALUE);
      }
   }

   @SuppressLint({"MissingPermission"})
   public void connectToVerifyOTAWithFactory(String var1, byte[] var2, boolean var3, boolean var4) {
      this.mBluetoothAdapter.cancelDiscovery();
      (new BluetoothDeviceWrapper(var1, this.mBluetoothAdapter.getRemoteDevice(var1), mContext, this)).connectToVerifyOTAWithFactory(this.mUiCallback, var2, var3, var4);
   }

   public void disconnect() {
      if (!this.addressList.isEmpty()) {
         ArrayList var10001 = this.addressList;
         this.disconnect((String)var10001.get(var10001.size() - 1));
      }
   }

   public void disconnect(String var1) {
      BluetoothDeviceWrapper var3;
      BluetoothDeviceWrapper var10000 = var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1);
      StringBuilder var4 = (new StringBuilder()).append("SPP disconnect: ");
      boolean var2;
      if (var10000 == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      MsgLogger.e(var4.append(var2).toString());
      if (var3 != null) {
         MsgLogger.e("Spp手动断开连接");
         var3.disconnect();
      }

   }

   public List getBondDevices() {
      FscSppCentralApiImp var10000 = this;
      ArrayList var4;
      var4 = new ArrayList.<init>();
      Iterator var1 = var10000.mBluetoothAdapter.getBondedDevices().iterator();

      while(var1.hasNext()) {
         BluetoothDevice var2 = (BluetoothDevice)var1.next();
         String var3 = var2.getName();
         var4.add(new FscDevice(var3, var2.getAddress(), var2, 0, "SPP", "", ""));
      }

      return var4;
   }

   @SuppressLint({"MissingPermission"})
   public void startScan() {
      if (this.mBluetoothAdapter.isDiscovering()) {
         this.stopScan();
      }

      BluetoothAdapter var1;
      if ((var1 = this.mBluetoothAdapter) != null) {
         if (var1.isEnabled()) {
            Iterator var5 = this.mBluetoothAdapter.getBondedDevices().iterator();

            while(var5.hasNext()) {
               BluetoothDevice var2 = (BluetoothDevice)var5.next();
               FscDevice var3;
               FscDevice var10001 = var3 = new FscDevice;
               String var4 = var2.getName();
               var10001.<init>(var4, var2.getAddress(), var2, 0, "SPP", "", "");
               this.mUiCallback.sppPeripheralFound(var3, 0);
            }

            this.mBluetoothAdapter.startDiscovery();
         }
      }
   }

   @SuppressLint({"MissingPermission"})
   public void stopScan() {
      if (this.mBluetoothAdapter.isDiscovering()) {
         this.mBluetoothAdapter.cancelDiscovery();
      }
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
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.send((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   public boolean send(String var1, byte[] var2) {
      BluetoothDeviceWrapper var3;
      if ((var3 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null) {
         var3.send(var2);
      }

      return false;
   }

   @RequiresApi(
      api = 23
   )
   public boolean sendFile(int var1) {
      if (this.addressList.isEmpty()) {
         return false;
      } else {
         ArrayList var10001 = this.addressList;
         return this.sendFile((String)var10001.get(var10001.size() - 1), var1);
      }
   }

   @RequiresApi(
      api = 23
   )
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

   public boolean pauseSend(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.pauseSend() : false;
   }

   public boolean continueSend(String var1) {
      BluetoothDeviceWrapper var2;
      return (var2 = (BluetoothDeviceWrapper)this.deviceMap.get(var1)) != null ? var2.continueSend() : false;
   }

   public void success(@NotNull BluetoothDeviceWrapper var1) {
      this.deviceMap.put(var1.getMAddress(), var1);
      this.addressList.add(var1.getMAddress());
   }

   public void failure(@NotNull BluetoothDeviceWrapper var1) {
      this.deviceMap.remove(var1.getMAddress());
      this.addressList.remove(var1.getMAddress());
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
