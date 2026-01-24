package com.feasycom.ota.bean;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u0018"},
   d2 = {"Lcom/feasycom/ota/bean/PacketTransport;", "", "moredata", "", "data", "", "(I[B)V", "getData", "()[B", "setData", "([B)V", "getMoredata", "()I", "setMoredata", "(I)V", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "FeasyBlueLibrary_release"}
)
public final class PacketTransport {
   private int moredata;
   @NotNull
   private byte[] data;

   public PacketTransport(int var1, @NotNull byte[] var2) {
      Intrinsics.checkNotNullParameter(var2, "data");
      super();
      this.moredata = var1;
      this.data = var2;
   }

   // $FF: synthetic method
   public PacketTransport(int var1, byte[] var2, int var3, DefaultConstructorMarker var4) {
      if ((var3 & 1) != 0) {
         var1 = 0;
      }

      if ((var3 & 2) != 0) {
         var2 = new byte[1024];
      }

      this(var1, var2);
   }

   // $FF: synthetic method
   public static PacketTransport copy$default(PacketTransport var0, int var1, byte[] var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = var0.moredata;
      }

      if ((var3 & 2) != 0) {
         var2 = var0.data;
      }

      return var0.copy(var1, var2);
   }

   public PacketTransport() {
      this(0, (byte[])null, 3, (DefaultConstructorMarker)null);
   }

   public final int getMoredata() {
      return this.moredata;
   }

   public final void setMoredata(int var1) {
      this.moredata = var1;
   }

   @NotNull
   public final byte[] getData() {
      return this.data;
   }

   public final void setData(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.data = var1;
   }

   public final int component1() {
      return this.moredata;
   }

   @NotNull
   public final byte[] component2() {
      return this.data;
   }

   @NotNull
   public final PacketTransport copy(int var1, @NotNull byte[] var2) {
      Intrinsics.checkNotNullParameter(var2, "data");
      return new PacketTransport(var1, var2);
   }

   @NotNull
   public String toString() {
      return "PacketTransport(moredata=" + this.moredata + ", data=" + Arrays.toString(this.data) + ')';
   }

   public int hashCode() {
      return Integer.hashCode(this.moredata) * 31 + Arrays.hashCode(this.data);
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PacketTransport)) {
         return false;
      } else {
         PacketTransport var2 = (PacketTransport)var1;
         if (this.moredata != var2.moredata) {
            return false;
         } else {
            return Intrinsics.areEqual(this.data, var2.data);
         }
      }
   }
}
