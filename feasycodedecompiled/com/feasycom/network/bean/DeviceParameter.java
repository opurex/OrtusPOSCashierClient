package com.feasycom.network.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.android.parcel.Parcelize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003¢\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0006HÆ\u0003J\t\u0010\u0018\u001a\u00020\bHÆ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003JG\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001J\u0013\u0010\u001d\u001a\u00020\b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fHÖ\u0003J\t\u0010 \u001a\u00020\u0003HÖ\u0001J\t\u0010!\u001a\u00020\u0006HÖ\u0001J\u0019\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\rR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\r¨\u0006'"},
   d2 = {"Lcom/feasycom/network/bean/DeviceParameter;", "Landroid/os/Parcelable;", "deviceType", "", "funcType", "hash", "", "haveDetail", "", "page", "size", "(IILjava/lang/String;ZII)V", "getDeviceType", "()I", "getFuncType", "getHash", "()Ljava/lang/String;", "getHaveDetail", "()Z", "getPage", "getSize", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "describeContents", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "FeasyBlueLibrary_release"}
)
@Keep
@Parcelize
public final class DeviceParameter implements Parcelable {
   @NotNull
   public static final Parcelable.Creator CREATOR;
   private final int deviceType;
   private final int funcType;
   @Nullable
   private final String hash;
   private final boolean haveDetail;
   private final int page;
   private final int size;

   public DeviceParameter(int var1, int var2, @Nullable String var3, boolean var4, int var5, int var6) {
      this.deviceType = var1;
      this.funcType = var2;
      this.hash = var3;
      this.haveDetail = var4;
      this.page = var5;
      this.size = var6;
   }

   // $FF: synthetic method
   public static DeviceParameter copy$default(DeviceParameter var0, int var1, int var2, String var3, boolean var4, int var5, int var6, int var7, Object var8) {
      if ((var7 & 1) != 0) {
         var1 = var0.deviceType;
      }

      if ((var7 & 2) != 0) {
         var2 = var0.funcType;
      }

      if ((var7 & 4) != 0) {
         var3 = var0.hash;
      }

      if ((var7 & 8) != 0) {
         var4 = var0.haveDetail;
      }

      if ((var7 & 16) != 0) {
         var5 = var0.page;
      }

      if ((var7 & 32) != 0) {
         var6 = var0.size;
      }

      return var0.copy(var1, var2, var3, var4, var5, var6);
   }

   static {
      @Metadata(
         mv = {1, 7, 1},
         k = 3,
         xi = 48
      )
      final class Creator implements Parcelable.Creator {
         public Creator() {
         }

         @NotNull
         public final DeviceParameter[] newArray(int var1) {
            return new DeviceParameter[var1];
         }

         @NotNull
         public final DeviceParameter createFromParcel(@NotNull Parcel var1) {
            Intrinsics.checkNotNullParameter(var1, "parcel");
            DeviceParameter var6 = new DeviceParameter;
            int var2 = var1.readInt();
            int var3 = var1.readInt();
            String var4 = var1.readString();
            boolean var5;
            if (var1.readInt() != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            DeviceParameter var10000 = var6;
            DeviceParameter var10001 = var6;
            int var7 = var1.readInt();
            int var8 = var1.readInt();
            var10001.<init>(var2, var3, var4, var5, var7, var8);
            return var10000;
         }
      }

      CREATOR = new Creator();
   }

   public final int getDeviceType() {
      return this.deviceType;
   }

   public final int getFuncType() {
      return this.funcType;
   }

   @Nullable
   public final String getHash() {
      return this.hash;
   }

   public final boolean getHaveDetail() {
      return this.haveDetail;
   }

   public final int getPage() {
      return this.page;
   }

   public final int getSize() {
      return this.size;
   }

   public final int component1() {
      return this.deviceType;
   }

   public final int component2() {
      return this.funcType;
   }

   @Nullable
   public final String component3() {
      return this.hash;
   }

   public final boolean component4() {
      return this.haveDetail;
   }

   public final int component5() {
      return this.page;
   }

   public final int component6() {
      return this.size;
   }

   @NotNull
   public final DeviceParameter copy(int var1, int var2, @Nullable String var3, boolean var4, int var5, int var6) {
      return new DeviceParameter(var1, var2, var3, var4, var5, var6);
   }

   @NotNull
   public String toString() {
      return "DeviceParameter(deviceType=" + this.deviceType + ", funcType=" + this.funcType + ", hash=" + this.hash + ", haveDetail=" + this.haveDetail + ", page=" + this.page + ", size=" + this.size + ')';
   }

   public int hashCode() {
      int var1 = (Integer.hashCode(this.deviceType) * 31 + Integer.hashCode(this.funcType)) * 31;
      String var2;
      int var3;
      if ((var2 = this.hash) == null) {
         var3 = 0;
      } else {
         var3 = var2.hashCode();
      }

      var1 = (var1 + var3) * 31;
      byte var4;
      if ((var4 = this.haveDetail) != 0) {
         var4 = 1;
      }

      return ((var1 + var4) * 31 + Integer.hashCode(this.page)) * 31 + Integer.hashCode(this.size);
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DeviceParameter)) {
         return false;
      } else {
         DeviceParameter var2 = (DeviceParameter)var1;
         if (this.deviceType != var2.deviceType) {
            return false;
         } else if (this.funcType != var2.funcType) {
            return false;
         } else if (!Intrinsics.areEqual(this.hash, var2.hash)) {
            return false;
         } else if (this.haveDetail != var2.haveDetail) {
            return false;
         } else if (this.page != var2.page) {
            return false;
         } else {
            return this.size == var2.size;
         }
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(@NotNull Parcel var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "out");
      var1.writeInt(this.deviceType);
      var1.writeInt(this.funcType);
      var1.writeString(this.hash);
      var1.writeInt(this.haveDetail);
      var1.writeInt(this.page);
      var1.writeInt(this.size);
   }
}
