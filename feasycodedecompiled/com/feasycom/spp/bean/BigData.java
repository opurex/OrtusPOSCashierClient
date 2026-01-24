package com.feasycom.spp.bean;

import android.os.Binder;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"},
   d2 = {"Lcom/feasycom/spp/bean/BigData;", "Landroid/os/Binder;", "data", "", "([B)V", "getData", "()[B", "FeasyBlueLibrary_release"}
)
public final class BigData extends Binder {
   @NotNull
   private final byte[] data;

   public BigData(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "data");
      super();
      this.data = var1;
   }

   @NotNull
   public final byte[] getData() {
      return this.data;
   }
}
