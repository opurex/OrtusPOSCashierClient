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
   d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0007HÆ\u0003J'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0007HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0018"},
   d2 = {"Lcom/feasycom/network/bean/Splash;", "", "code", "", "data", "Lcom/feasycom/network/bean/LauchData;", "msg", "", "(ILcom/feasycom/network/bean/LauchData;Ljava/lang/String;)V", "getCode", "()I", "getData", "()Lcom/feasycom/network/bean/LauchData;", "getMsg", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "FeasyBlueLibrary_release"}
)
@Keep
public final class Splash {
   private final int code;
   @NotNull
   private final LauchData data;
   @NotNull
   private final String msg;

   public Splash(int var1, @NotNull LauchData var2, @NotNull String var3) {
      Intrinsics.checkNotNullParameter(var2, "data");
      Intrinsics.checkNotNullParameter(var3, "msg");
      super();
      this.code = var1;
      this.data = var2;
      this.msg = var3;
   }

   // $FF: synthetic method
   public static Splash copy$default(Splash var0, int var1, LauchData var2, String var3, int var4, Object var5) {
      if ((var4 & 1) != 0) {
         var1 = var0.code;
      }

      if ((var4 & 2) != 0) {
         var2 = var0.data;
      }

      if ((var4 & 4) != 0) {
         var3 = var0.msg;
      }

      return var0.copy(var1, var2, var3);
   }

   public final int getCode() {
      return this.code;
   }

   @NotNull
   public final LauchData getData() {
      return this.data;
   }

   @NotNull
   public final String getMsg() {
      return this.msg;
   }

   public final int component1() {
      return this.code;
   }

   @NotNull
   public final LauchData component2() {
      return this.data;
   }

   @NotNull
   public final String component3() {
      return this.msg;
   }

   @NotNull
   public final Splash copy(int var1, @NotNull LauchData var2, @NotNull String var3) {
      Intrinsics.checkNotNullParameter(var2, "data");
      Intrinsics.checkNotNullParameter(var3, "msg");
      return new Splash(var1, var2, var3);
   }

   @NotNull
   public String toString() {
      return "Splash(code=" + this.code + ", data=" + this.data + ", msg=" + this.msg + ')';
   }

   public int hashCode() {
      return (Integer.hashCode(this.code) * 31 + this.data.hashCode()) * 31 + this.msg.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Splash)) {
         return false;
      } else {
         Splash var2 = (Splash)var1;
         if (this.code != var2.code) {
            return false;
         } else if (!Intrinsics.areEqual(this.data, var2.data)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.msg, var2.msg);
         }
      }
   }
}
