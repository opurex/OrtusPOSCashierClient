package com.feasycom.ble.bean;

import android.bluetooth.BluetoothGatt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
   mv = {1, 7, 1},
   k = 3,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"},
   d2 = {"<anonymous>", "", "run", "kotlinx/coroutines/RunnableKt$Runnable$1"}
)
public final class BluetoothDeviceWrapper$special$$inlined$Runnable$2 implements Runnable {
   // $FF: synthetic field
   public final BluetoothDeviceWrapper this$0;

   public BluetoothDeviceWrapper$special$$inlined$Runnable$2(BluetoothDeviceWrapper var1) {
      this.this$0 = var1;
   }

   public final void run() {
      BluetoothGatt var1;
      if ((var1 = BluetoothDeviceWrapper.access$getMBluetoothGatt$p(this.this$0)) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var1 = null;
      }

      if (var1 != null) {
         var1.readRemoteRssi();
      }

   }
}
