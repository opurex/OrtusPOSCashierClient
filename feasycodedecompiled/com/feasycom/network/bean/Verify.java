package com.feasycom.network.bean;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004¨\u0006\u0010"},
   d2 = {"Lcom/feasycom/network/bean/Verify;", "", "email", "", "(Ljava/lang/String;)V", "getEmail", "()Ljava/lang/String;", "setEmail", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "FeasyBlueLibrary_release"}
)
public final class Verify {
   @NotNull
   private String email;

   public Verify(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "email");
      super();
      this.email = var1;
   }

   // $FF: synthetic method
   public static Verify copy$default(Verify var0, String var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.email;
      }

      return var0.copy(var1);
   }

   @NotNull
   public final String getEmail() {
      return this.email;
   }

   public final void setEmail(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.email = var1;
   }

   @NotNull
   public final String component1() {
      return this.email;
   }

   @NotNull
   public final Verify copy(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "email");
      return new Verify(var1);
   }

   @NotNull
   public String toString() {
      return "Verify(email=" + this.email + ')';
   }

   public int hashCode() {
      return this.email.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Verify)) {
         return false;
      } else {
         Verify var10000 = this;
         Verify var2 = (Verify)var1;
         return Intrinsics.areEqual(var10000.email, var2.email);
      }
   }
}
