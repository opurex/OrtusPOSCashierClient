package com.feasycom.ble.controler;

import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import com.feasycom.common.bean.ScannerFilter;
import com.feasycom.common.controler.FscApi;
import java.util.List;

@Keep
public interface FscBleCentralApi extends FscApi {
   void startScan(ScannerFilter var1);

   void connect(String var1, Boolean var2);

   int getMtu();

   int getMtu(String var1);

   int getMaximumPacketByte();

   int getMaximumPacketByte(String var1);

   boolean setCharacteristic(BluetoothGattCharacteristic var1, int var2);

   boolean setCharacteristic(String var1, BluetoothGattCharacteristic var2, int var3);

   boolean read(String var1, BluetoothGattCharacteristic var2);

   void startOTA(String var1, byte[] var2, boolean var3);

   List getBluetoothGattServices(String var1);

   BluetoothGattCharacteristic getWriteCharacteristic(String var1);

   List getNotifyCharacteristicList(String var1);

   @RequiresApi(21)
   void requestMtu(int var1);

   @RequiresApi(21)
   void requestMtu(String var1, int var2);

   void createBond();
}
