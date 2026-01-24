package com.feasycom.network.bean;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\u0018\u00002\u00020\u0001B'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0015"},
   d2 = {"Lcom/feasycom/network/bean/Devices;", "", "code", "", "data", "Lcom/feasycom/network/bean/DeviceData;", "msg", "", "(ILcom/feasycom/network/bean/DeviceData;Ljava/lang/String;)V", "getCode", "()I", "setCode", "(I)V", "getData", "()Lcom/feasycom/network/bean/DeviceData;", "setData", "(Lcom/feasycom/network/bean/DeviceData;)V", "getMsg", "()Ljava/lang/String;", "setMsg", "(Ljava/lang/String;)V", "FeasyBlueLibrary_release"}
)
public final class Devices {
   private int code;
   @Nullable
   private DeviceData data;
   @Nullable
   private String msg;

   public Devices(int var1, @Nullable DeviceData var2, @Nullable String var3) {
      this.code = var1;
      this.data = var2;
      this.msg = var3;
   }

   // $FF: synthetic method
   public Devices(int var1, DeviceData var2, String var3, int var4, DefaultConstructorMarker var5) {
      if ((var4 & 1) != 0) {
         var1 = 0;
      }

      if ((var4 & 2) != 0) {
         var2 = null;
      }

      if ((var4 & 4) != 0) {
         var3 = null;
      }

      this(var1, var2, var3);
   }

   public Devices() {
      this(0, (DeviceData)null, (String)null, 7, (DefaultConstructorMarker)null);
   }

   public final int getCode() {
      return this.code;
   }

   public final void setCode(int var1) {
      this.code = var1;
   }

   @Nullable
   public final DeviceData getData() {
      return this.data;
   }

   public final void setData(@Nullable DeviceData var1) {
      this.data = var1;
   }

   @Nullable
   public final String getMsg() {
      return this.msg;
   }

   public final void setMsg(@Nullable String var1) {
      this.msg = var1;
   }
}
