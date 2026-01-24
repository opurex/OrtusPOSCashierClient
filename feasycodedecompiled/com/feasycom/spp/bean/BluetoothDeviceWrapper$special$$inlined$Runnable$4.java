package com.feasycom.spp.bean;

import com.feasycom.common.utils.MsgLogger;
import com.feasycom.ota.utils.XmodemUtils;
import kotlin.Metadata;

@Metadata(
   mv = {1, 7, 1},
   k = 3,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"},
   d2 = {"<anonymous>", "", "run", "kotlinx/coroutines/RunnableKt$Runnable$1"}
)
public final class BluetoothDeviceWrapper$special$$inlined$Runnable$4 implements Runnable {
   // $FF: synthetic field
   public final BluetoothDeviceWrapper this$0;

   public BluetoothDeviceWrapper$special$$inlined$Runnable$4(BluetoothDeviceWrapper var1) {
      this.this$0 = var1;
   }

   public final void run() {
      MsgLogger.e("SPP OTA 升级失败断开连接");
      this.this$0.disconnect();
      this.this$0.getMFscSppCentralCallbacks().otaProgressUpdate(this.this$0.getMAddress(), 0.0F, XmodemUtils.OTA_STATUS_AT_FAILED);
   }
}
