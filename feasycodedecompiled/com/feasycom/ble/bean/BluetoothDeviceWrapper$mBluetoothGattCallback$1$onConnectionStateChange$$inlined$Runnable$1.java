package com.feasycom.ble.bean;

import android.bluetooth.BluetoothGatt;
import com.feasycom.ble.controler.FscBleCentralCallbacks;
import kotlin.Metadata;

@Metadata(
   mv = {1, 7, 1},
   k = 3,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"},
   d2 = {"<anonymous>", "", "run", "kotlinx/coroutines/RunnableKt$Runnable$1"}
)
public final class BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$1 implements Runnable {
   // $FF: synthetic field
   public final BluetoothDeviceWrapper this$0;
   // $FF: synthetic field
   public final BluetoothGatt $gatt$inlined;
   // $FF: synthetic field
   public final int $status$inlined;

   public BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$1(BluetoothDeviceWrapper var1, BluetoothGatt var2, int var3) {
      this.this$0 = var1;
      this.$gatt$inlined = var2;
      this.$status$inlined = var3;
   }

   public final void run() {
      this.this$0.getConnectCallback().failure(this.this$0);
      FscBleCentralCallbacks var10000 = this.this$0.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
      BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$1 var10001 = this;
      BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$1 var10002 = this;
      BluetoothGatt var3 = this.$gatt$inlined;
      String var1 = var10002.this$0.getMAddress();
      int var2 = var10001.$status$inlined;
      var10000.blePeripheralDisconnected(var3, var1, var2);
   }
}
