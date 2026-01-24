package com.feasycom.common.bean;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"},
   d2 = {"Lcom/feasycom/common/bean/ScannerFilter;", "", "()V", "deviceName", "", "getDeviceName", "()Ljava/lang/String;", "setDeviceName", "(Ljava/lang/String;)V", "rssi", "", "getRssi", "()I", "setRssi", "(I)V", "FeasyBlueLibrary_release"}
)
public final class ScannerFilter {
   private int rssi = -127;
   @NotNull
   private String deviceName = "";

   public final int getRssi() {
      return this.rssi;
   }

   public final void setRssi(int var1) {
      this.rssi = var1;
   }

   @NotNull
   public final String getDeviceName() {
      return this.deviceName;
   }

   public final void setDeviceName(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.deviceName = var1;
   }
}
