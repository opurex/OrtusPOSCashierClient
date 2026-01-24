package com.feasycom.network.bean;

import androidx.annotation.Keep;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\f\u001a\u00020\rHÖ\u0001J\t\u0010\u000e\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000f"},
   d2 = {"Lcom/feasycom/network/bean/Data;", "", "url", "", "(Ljava/lang/String;)V", "getUrl", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "FeasyBlueLibrary_release"}
)
@Keep
public final class Data {
   @NotNull
   private final String url;

   public Data(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "url");
      super();
      this.url = var1;
   }

   // $FF: synthetic method
   public static Data copy$default(Data var0, String var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.url;
      }

      return var0.copy(var1);
   }

   @NotNull
   public final String getUrl() {
      return this.url;
   }

   @NotNull
   public final String component1() {
      return this.url;
   }

   @NotNull
   public final Data copy(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "url");
      return new Data(var1);
   }

   @NotNull
   public String toString() {
      return "Data(url=" + this.url + ')';
   }

   public int hashCode() {
      return this.url.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Data)) {
         return false;
      } else {
         Data var10000 = this;
         Data var2 = (Data)var1;
         return Intrinsics.areEqual(var10000.url, var2.url);
      }
   }
}
