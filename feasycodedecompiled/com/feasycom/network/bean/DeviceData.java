package com.feasycom.network.bean;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR \u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\t\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0014\"\u0004\b\u0018\u0010\u0016¨\u0006\u0019"},
   d2 = {"Lcom/feasycom/network/bean/DeviceData;", "", "hash", "", "list", "", "Lcom/feasycom/network/bean/DeviceInfo;", "page", "", "size", "(Ljava/lang/String;Ljava/util/List;II)V", "getHash", "()Ljava/lang/String;", "setHash", "(Ljava/lang/String;)V", "getList", "()Ljava/util/List;", "setList", "(Ljava/util/List;)V", "getPage", "()I", "setPage", "(I)V", "getSize", "setSize", "FeasyBlueLibrary_release"}
)
public final class DeviceData {
   @NotNull
   private String hash;
   @NotNull
   private List list;
   private int page;
   private int size;

   public DeviceData(@NotNull String var1, @NotNull List var2, int var3, int var4) {
      Intrinsics.checkNotNullParameter(var1, "hash");
      Intrinsics.checkNotNullParameter(var2, "list");
      super();
      this.hash = var1;
      this.list = var2;
      this.page = var3;
      this.size = var4;
   }

   // $FF: synthetic method
   public DeviceData(String var1, List var2, int var3, int var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 4) != 0) {
         var3 = 0;
      }

      if ((var5 & 8) != 0) {
         var4 = 0;
      }

      this(var1, var2, var3, var4);
   }

   @NotNull
   public final String getHash() {
      return this.hash;
   }

   public final void setHash(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.hash = var1;
   }

   @NotNull
   public final List getList() {
      return this.list;
   }

   public final void setList(@NotNull List var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.list = var1;
   }

   public final int getPage() {
      return this.page;
   }

   public final void setPage(int var1) {
      this.page = var1;
   }

   public final int getSize() {
      return this.size;
   }

   public final void setSize(int var1) {
      this.size = var1;
   }
}
