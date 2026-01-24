package com.feasycom.spp.controler;

import a.a.a.a;
import android.bluetooth.BluetoothDevice;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.FscDevice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FscSppCentralCallbacksImp implements FscSppCentralCallbacks {
   public void sppPeripheralFound(FscDevice var1, int var2) {
      a.a().a("FscSppCentralCallbacksImp_sppPeripheralFound");
   }

   public void sppPeripheralConnected(BluetoothDevice var1, ConnectType var2) {
      a.a().a("FscSppCentralCallbacksImp_sppPeripheralConnected");
   }

   public void connectToModifyFail() {
      a.a().a("FscSppCentralCallbacksImp_connectToModifyFail");
   }

   public void sppPeripheralDisconnected(String var1) {
      a.a().a("FscSppCentralCallbacksImp_sppPeripheralDisconnected");
   }

   public void packetSend(String var1, String var2, String var3, byte[] var4) {
   }

   public void startScan() {
      a.a().a("FscSppCentralCallbacksImp_startScan");
   }

   public void stopScan() {
      a.a().a("FscSppCentralCallbacksImp_stopScan");
   }

   public void packetReceived(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull byte[] var4) {
   }

   public void packetSend(@NotNull String var1, @NotNull String var2, @NotNull byte[] var3) {
   }

   public void sendPacketProgress(@NotNull String var1, int var2, @NotNull byte[] var3) {
   }

   public void atCommandCallBack(@Nullable String var1, @Nullable String var2, int var3, int var4) {
      a.a().a("FscSppCentralCallbacksImp_atCommandCallBack");
   }

   public void endATCommand() {
   }

   public void startATCommand() {
      a.a().a("FscSppCentralCallbacksImp_startATCommand");
   }

   public void bondIng() {
      a.a().a("FscSppCentralCallbacksImp_bondIng");
   }
}
