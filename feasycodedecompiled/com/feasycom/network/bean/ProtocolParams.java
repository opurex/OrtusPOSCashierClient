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
   d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"},
   d2 = {"Lcom/feasycom/network/bean/ProtocolParams;", "", "app", "", "type", "", "(Ljava/lang/String;I)V", "getApp", "()Ljava/lang/String;", "getType", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "FeasyBlueLibrary_release"}
)
@Keep
public final class ProtocolParams {
   @NotNull
   private final String app;
   private final int type;

   public ProtocolParams(@NotNull String var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "app");
      super();
      this.app = var1;
      this.type = var2;
   }

   // $FF: synthetic method
   public static ProtocolParams copy$default(ProtocolParams var0, String var1, int var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = var0.app;
      }

      if ((var3 & 2) != 0) {
         var2 = var0.type;
      }

      return var0.copy(var1, var2);
   }

   @NotNull
   public final String getApp() {
      return this.app;
   }

   public final int getType() {
      return this.type;
   }

   @NotNull
   public final String component1() {
      return this.app;
   }

   public final int component2() {
      return this.type;
   }

   @NotNull
   public final ProtocolParams copy(@NotNull String var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "app");
      return new ProtocolParams(var1, var2);
   }

   @NotNull
   public String toString() {
      return "ProtocolParams(app=" + this.app + ", type=" + this.type + ')';
   }

   public int hashCode() {
      return this.app.hashCode() * 31 + Integer.hashCode(this.type);
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ProtocolParams)) {
         return false;
      } else {
         ProtocolParams var2 = (ProtocolParams)var1;
         if (!Intrinsics.areEqual(this.app, var2.app)) {
            return false;
         } else {
            return this.type == var2.type;
         }
      }
   }
}
