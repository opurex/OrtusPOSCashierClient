package com.feasycom.spp.bean;

import kotlin.Metadata;

@Metadata(
   mv = {1, 7, 1},
   k = 3,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"},
   d2 = {"<anonymous>", "", "run", "kotlinx/coroutines/RunnableKt$Runnable$1"}
)
public final class BluetoothDeviceWrapper$nextRequest$lambda-11$$inlined$Runnable$1 implements Runnable {
   // $FF: synthetic field
   public final BluetoothDeviceWrapper this$0;

   public BluetoothDeviceWrapper$nextRequest$lambda_11$$inlined$Runnable$1/* $FF was: BluetoothDeviceWrapper$nextRequest$lambda-11$$inlined$Runnable$1*/(BluetoothDeviceWrapper var1) {
      this.this$0 = var1;
   }

   public final void run() {
      this.this$0.getMConnectCallback().success(this.this$0);
      this.this$0.getMFscSppCentralCallbacks().sppPeripheralConnected(this.this$0.getMDevice(), BluetoothDeviceWrapper.access$getMConnectType$p(this.this$0));
   }
}
