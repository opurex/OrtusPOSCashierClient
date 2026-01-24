package com.feasycom.spp.controler;

import android.bluetooth.BluetoothDevice;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.controler.FscCentralCallbacks;

public interface FscSppCentralCallbacks extends FscCentralCallbacks {
   default void sppPeripheralFound(FscDevice var1, int var2) {
   }

   default void sppPeripheralConnected(BluetoothDevice var1, ConnectType var2) {
   }

   default void connectToModifyFail() {
   }

   default void sppPeripheralDisconnected(String var1) {
   }

   default void packetSend(String var1, String var2, String var3, byte[] var4) {
   }

   default void bondIng() {
   }

   default void connectSuccess() {
   }

   default void connectFailed() {
   }
}
