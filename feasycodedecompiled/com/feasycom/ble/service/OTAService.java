package com.feasycom.ble.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.feasycom.ble.bean.BluetoothDeviceWrapper;
import com.feasycom.ble.controler.FscBleCentralCallbacks;
import com.feasycom.ble.controler.FscBleCentralCallbacksImp;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.ota.utils.XmodemUtils;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u001c2\u00020\u0001:\u0002\u001c\u001dB\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\nH\u0002J\u000e\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u001e"},
   d2 = {"Lcom/feasycom/ble/service/OTAService;", "Landroid/app/Service;", "()V", "mDeviceWrapper", "Lcom/feasycom/ble/bean/BluetoothDeviceWrapper;", "getMDeviceWrapper", "()Lcom/feasycom/ble/bean/BluetoothDeviceWrapper;", "setMDeviceWrapper", "(Lcom/feasycom/ble/bean/BluetoothDeviceWrapper;)V", "mFileByte", "", "mFscBleCallbacks", "Lcom/feasycom/ble/controler/FscBleCentralCallbacks;", "getMFscBleCallbacks", "()Lcom/feasycom/ble/controler/FscBleCentralCallbacks;", "setMFscBleCallbacks", "(Lcom/feasycom/ble/controler/FscBleCentralCallbacks;)V", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onUnbind", "", "send", "", "cmd", "startOta", "deviceWrapper", "Companion", "LocalBinder", "FeasyBlueLibrary_release"}
)
public final class OTAService extends Service {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   public static final String TAG = "OTAService";
   private byte[] mFileByte;
   public FscBleCentralCallbacks mFscBleCallbacks;
   public BluetoothDeviceWrapper mDeviceWrapper;

   private final void send(byte[] var1) {
      this.getMDeviceWrapper().send(var1);
   }

   @Nullable
   public IBinder onBind(@Nullable Intent var1) {
      byte[] var2;
      if (var1 != null && (var2 = var1.getByteArrayExtra("fileByte")) != null) {
         this.mFileByte = var2;
         return new LocalBinder();
      } else {
         return null;
      }
   }

   @NotNull
   public final FscBleCentralCallbacks getMFscBleCallbacks() {
      FscBleCentralCallbacks var1;
      if ((var1 = this.mFscBleCallbacks) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("mFscBleCallbacks");
         return null;
      }
   }

   public final void setMFscBleCallbacks(@NotNull FscBleCentralCallbacks var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.mFscBleCallbacks = var1;
   }

   @NotNull
   public final BluetoothDeviceWrapper getMDeviceWrapper() {
      BluetoothDeviceWrapper var1;
      if ((var1 = this.mDeviceWrapper) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("mDeviceWrapper");
         return null;
      }
   }

   public final void setMDeviceWrapper(@NotNull BluetoothDeviceWrapper var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.mDeviceWrapper = var1;
   }

   public final void startOta(@NotNull BluetoothDeviceWrapper var1) {
      Intrinsics.checkNotNullParameter(var1, "deviceWrapper");
      this.setMDeviceWrapper(var1);
      this.setMFscBleCallbacks(var1.getMFscBleCentralCallbacks$FeasyBlueLibrary_release());
      XmodemUtils var2 = new XmodemUtils;
      byte[] var3;
      if ((var3 = this.mFileByte) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mFileByte");
         var3 = null;
      }

      BluetoothDeviceWrapper var10000 = var1;
      var2.<init>(var3);
      Function0 var5;
      var5 = new Function0() {
         // $FF: synthetic field
         public final BluetoothDeviceWrapper b;

         public {
            this.b = var2;
         }

         public final void a() {
            OTAService.this.getMFscBleCallbacks().otaProgressUpdate(this.b.getMAddress(), 100.0F, XmodemUtils.OTA_STATUS_FINISH);
         }
      }.<init>(var1);
      var2.setCompleteCallback(var5);
      Function1 var6;
      var6 = new Function1() {
         // $FF: synthetic field
         public final BluetoothDeviceWrapper b;

         public {
            this.b = var2;
         }

         public final void a(float var1) {
            if (var1 < 100.0F) {
               OTAService.this.getMFscBleCallbacks().otaProgressUpdate(this.b.getMAddress(), var1, XmodemUtils.OTA_STATUS_PROCESSING);
            } else {
               OTAService.this.getMFscBleCallbacks().otaProgressUpdate(this.b.getMAddress(), 100.0F, XmodemUtils.OTA_STATUS_FINISH);
            }

         }
      }.<init>(var1);
      var2.setProgressUpdateCallback(var6);
      FscBleCentralCallbacksImp var4;
      var4 = new FscBleCentralCallbacksImp() {
         // $FF: synthetic field
         public final XmodemUtils $mXmodemUtils;

         public {
            this.$mXmodemUtils = var2;
         }

         public void packetReceived(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull byte[] var4) {
            <undefinedtype> var10000 = this;
            Intrinsics.checkNotNullParameter(var1, "address");
            Intrinsics.checkNotNullParameter(var2, "strValue");
            Intrinsics.checkNotNullParameter(var3, "dataHexString");
            Intrinsics.checkNotNullParameter(var4, "data");

            try {
               OTAService.this.send(this.$mXmodemUtils.getData(var4[0]));
            } catch (Exception var5) {
               MsgLogger.e("packetReceived => " + var5.getMessage());
            }

         }
      }.<init>(var2);
      var10000.setMOTACallbacks$FeasyBlueLibrary_release(var4);
   }

   public boolean onUnbind(@Nullable Intent var1) {
      return super.onUnbind(var1);
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"},
      d2 = {"Lcom/feasycom/ble/service/OTAService$LocalBinder;", "Landroid/os/Binder;", "(Lcom/feasycom/ble/service/OTAService;)V", "mService", "Lcom/feasycom/ble/service/OTAService;", "getMService", "()Lcom/feasycom/ble/service/OTAService;", "FeasyBlueLibrary_release"}
   )
   public final class LocalBinder extends Binder {
      @NotNull
      public final OTAService getMService() {
         return OTAService.this;
      }
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"},
      d2 = {"Lcom/feasycom/ble/service/OTAService$Companion;", "", "()V", "TAG", "", "FeasyBlueLibrary_release"}
   )
   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
