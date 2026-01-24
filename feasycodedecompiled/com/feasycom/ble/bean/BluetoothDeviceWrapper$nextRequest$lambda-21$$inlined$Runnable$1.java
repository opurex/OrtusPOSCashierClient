package com.feasycom.ble.bean;

import android.bluetooth.BluetoothGatt;
import com.feasycom.ble.controler.FscBleCentralCallbacks;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
   mv = {1, 7, 1},
   k = 3,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"},
   d2 = {"<anonymous>", "", "run", "kotlinx/coroutines/RunnableKt$Runnable$1"}
)
public final class BluetoothDeviceWrapper$nextRequest$lambda-21$$inlined$Runnable$1 implements Runnable {
   // $FF: synthetic field
   public final BluetoothDeviceWrapper this$0;

   public BluetoothDeviceWrapper$nextRequest$lambda_21$$inlined$Runnable$1/* $FF was: BluetoothDeviceWrapper$nextRequest$lambda-21$$inlined$Runnable$1*/(BluetoothDeviceWrapper var1) {
      this.this$0 = var1;
   }

   public final void run() {
      this.this$0.getConnectCallback().success(this.this$0);
      FscBleCentralCallbacks var1 = this.this$0.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
      BluetoothGatt var2;
      if ((var2 = BluetoothDeviceWrapper.access$getMBluetoothGatt$p(this.this$0)) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var2 = null;
      }

      BluetoothDeviceWrapper$nextRequest$lambda-21$$inlined$Runnable$1 var10002 = this;
      String var3 = this.this$0.getMAddress();
      var1.blePeripheralConnected(var2, var3, BluetoothDeviceWrapper.access$getMConnectType$p(var10002.this$0));
   }
}
