package com.feasycom.network.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.android.parcel.Parcelize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\t\u0010\n\u001a\u00020\u000bHÖ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u000bHÖ\u0001J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000bHÖ\u0001R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004¨\u0006\u0017"},
   d2 = {"Lcom/feasycom/network/bean/Parameter;", "Landroid/os/Parcelable;", "app", "", "(Ljava/lang/String;)V", "getApp", "()Ljava/lang/String;", "setApp", "component1", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "FeasyBlueLibrary_release"}
)
@Keep
@Parcelize
public final class Parameter implements Parcelable {
   @NotNull
   public static final Parcelable.Creator CREATOR;
   @SerializedName("app")
   @NotNull
   private String app;

   public Parameter(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "app");
      super();
      this.app = var1;
   }

   // $FF: synthetic method
   public static Parameter copy$default(Parameter var0, String var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.app;
      }

      return var0.copy(var1);
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
         public final Parameter[] newArray(int var1) {
            return new Parameter[var1];
         }

         @NotNull
         public final Parameter createFromParcel(@NotNull Parcel var1) {
            Intrinsics.checkNotNullParameter(var1, "parcel");
            return new Parameter(var1.readString());
         }
      }

      CREATOR = new Creator();
   }

   @NotNull
   public final String getApp() {
      return this.app;
   }

   public final void setApp(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.app = var1;
   }

   @NotNull
   public final String component1() {
      return this.app;
   }

   @NotNull
   public final Parameter copy(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "app");
      return new Parameter(var1);
   }

   @NotNull
   public String toString() {
      return "Parameter(app=" + this.app + ')';
   }

   public int hashCode() {
      return this.app.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Parameter)) {
         return false;
      } else {
         Parameter var10000 = this;
         Parameter var2 = (Parameter)var1;
         return Intrinsics.areEqual(var10000.app, var2.app);
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(@NotNull Parcel var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "out");
      var1.writeString(this.app);
   }
}
