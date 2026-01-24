package com.feasycom.ble.controler;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.controler.FscCentralCallbacks;
import java.util.List;

public interface FscBleCentralCallbacks extends FscCentralCallbacks {
   default void blePeripheralFound(FscDevice var1, int var2, byte[] var3) {
   }

   default void blePeripheralFound(BluetoothDevice var1, FscDevice var2, int var3, byte[] var4) {
   }

   default void blePeripheralConnected(BluetoothGatt var1, String var2, ConnectType var3) {
   }

   default void blePeripheralDisconnected(BluetoothGatt var1, String var2, int var3) {
   }

   default void servicesFound(BluetoothGatt var1, String var2, List var3) {
   }

   default void characteristicForService(BluetoothGatt var1, String var2, BluetoothGattService var3, BluetoothGattCharacteristic var4) {
   }

   default void readResponse(String var1, BluetoothGattCharacteristic var2, String var3, String var4, byte[] var5) {
   }

   default void writeResponse(byte[] var1) {
   }

   default void bleMtuChanged(int var1, int var2) {
   }

   default void readModuleModel(String var1) {
   }

   default void readModuleVersion(String var1) {
   }

   default void onReadRemoteRssi(int var1) {
   }
}
