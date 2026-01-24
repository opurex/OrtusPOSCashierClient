package com.feasycom.ble.controler;

import a.a.a.a;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.FscDevice;
import java.util.List;

public class FscBleCentralCallbacksImp implements FscBleCentralCallbacks {
   public void blePeripheralConnected(BluetoothGatt var1, String var2, ConnectType var3) {
      a.a().a("FscBleCentralCallbacksImp_blePeripheralConnected");
   }

   public void startScan() {
      a.a().a("FscBleCentralCallbacksImp_startScan");
   }

   public void stopScan() {
      a.a().a("FscBleCentralCallbacksImp_stopScan");
   }

   public void blePeripheralFound(FscDevice var1, int var2, byte[] var3) {
   }

   public void blePeripheralDisconnected(BluetoothGatt var1, String var2, int var3) {
      a.a().a("FscBleCentralCallbacksImp_blePeripheralDisconnected");
   }

   public void servicesFound(BluetoothGatt var1, String var2, List var3) {
      a.a().a("FscBleCentralCallbacksImp_servicesFound");
   }

   public void characteristicForService(BluetoothGatt var1, String var2, BluetoothGattService var3, BluetoothGattCharacteristic var4) {
      a.a().a("FscBleCentralCallbacksImp_characteristicForService");
   }

   public void packetReceived(String var1, String var2, String var3, byte[] var4) {
   }

   public void packetSend(String var1, String var2, byte[] var3) {
   }

   public void sendPacketProgress(String var1, int var2, byte[] var3) {
   }

   public void readResponse(String var1, BluetoothGattCharacteristic var2, String var3, String var4, byte[] var5) {
   }

   public void bleMtuChanged(int var1, int var2) {
      a.a().a("FscBleCentralCallbacksImp_bleMtuChanged");
   }

   public void readModuleModel(String var1) {
      a.a().a("FscBleCentralCallbacksImp_readModuleModel");
   }

   public void readModuleVersion(String var1) {
      a.a().a("FscBleCentralCallbacksImp_readModuleVersion");
   }

   public void atCommandCallBack(String var1, String var2, int var3, int var4) {
      a.a().a("FscBleCentralCallbacksImp_atCommandCallBack");
   }

   public void endATCommand() {
      a.a().a("FscBleCentralCallbacksImp_endATCommand");
   }

   public void startATCommand() {
      a.a().a("FscBleCentralCallbacksImp_startATCommand");
   }

   public void onReadRemoteRssi(int var1) {
      a.a().a("FscBleCentralCallbacksImp_onReadRemoteRssi");
   }
}
