package com.feasycom.spp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.feasycom.common.utils.ExpandKt;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.logger.Logger;
import com.feasycom.ota.utils.XmodemUtils;
import com.feasycom.spp.bean.BigData;
import com.feasycom.spp.bean.BluetoothDeviceWrapper;
import com.feasycom.spp.controler.FscSppCentralCallbacks;
import com.feasycom.spp.utils.ConstantUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.Job.DefaultImpls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 F2\u00020\u0001:\u0002FGB\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010>\u001a\u0004\u0018\u00010?2\b\u0010@\u001a\u0004\u0018\u00010AH\u0016J\u0012\u0010B\u001a\u00020\u00132\b\u0010@\u001a\u0004\u0018\u00010AH\u0016J\u0010\u0010C\u001a\u00020D2\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\u000e\u0010E\u001a\u00020D2\u0006\u0010\u000b\u001a\u00020\fR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u0019¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u001dX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001c\u0010\"\u001a\u0004\u0018\u00010#X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010(\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020.X\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010/\u001a\u000200X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u000e\u00105\u001a\u000206X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u00108\u001a\u000209X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=¨\u0006H"},
   d2 = {"Lcom/feasycom/spp/service/OTAService;", "Landroid/app/Service;", "()V", "cmd", "", "getCmd", "()[B", "setCmd", "([B)V", "deviceModel", "", "deviceWrapper", "Lcom/feasycom/spp/bean/BluetoothDeviceWrapper;", "getDeviceWrapper", "()Lcom/feasycom/spp/bean/BluetoothDeviceWrapper;", "setDeviceWrapper", "(Lcom/feasycom/spp/bean/BluetoothDeviceWrapper;)V", "fileByte", "finish", "", "getFinish", "()Z", "setFinish", "(Z)V", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "inputStream", "Ljava/io/InputStream;", "getInputStream", "()Ljava/io/InputStream;", "setInputStream", "(Ljava/io/InputStream;)V", "mFscSppCentralCallbacks", "Lcom/feasycom/spp/controler/FscSppCentralCallbacks;", "getMFscSppCentralCallbacks", "()Lcom/feasycom/spp/controler/FscSppCentralCallbacks;", "setMFscSppCentralCallbacks", "(Lcom/feasycom/spp/controler/FscSppCentralCallbacks;)V", "nakNumber", "getNakNumber", "()I", "setNakNumber", "(I)V", "otaJob", "Lkotlinx/coroutines/Job;", "outputStream", "Ljava/io/OutputStream;", "getOutputStream", "()Ljava/io/OutputStream;", "setOutputStream", "(Ljava/io/OutputStream;)V", "resendSendRunnable", "Ljava/lang/Runnable;", "sppStatBuffer", "xModemUtils", "Lcom/feasycom/ota/utils/XmodemUtils;", "getXModemUtils", "()Lcom/feasycom/ota/utils/XmodemUtils;", "setXModemUtils", "(Lcom/feasycom/ota/utils/XmodemUtils;)V", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onUnbind", "send", "", "startOta", "Companion", "LocalBinder", "FeasyBlueLibrary_release"}
)
public final class OTAService extends Service {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   public static final String TAG = "OTAService";
   private byte[] fileByte;
   private int deviceModel;
   private Job otaJob;
   @NotNull
   private final byte[] sppStatBuffer = new byte[20];
   public XmodemUtils xModemUtils;
   public OutputStream outputStream;
   public InputStream inputStream;
   @Nullable
   private FscSppCentralCallbacks mFscSppCentralCallbacks;
   public BluetoothDeviceWrapper deviceWrapper;
   private boolean finish;
   private int nakNumber;
   @NotNull
   private final Handler handler;
   @NotNull
   private final Runnable resendSendRunnable;
   @Nullable
   private byte[] cmd;

   public OTAService() {
      Looper var10005 = Looper.myLooper();
      Intrinsics.checkNotNull(var10005);
      this.handler = new Handler(var10005);
      this.resendSendRunnable = OTAService::resendSendRunnable$lambda-2;
   }

   private final void send(byte[] var1) {
      Logger.e("发送数据: " + ExpandKt.bytesToHex(var1) + "  " + this.deviceModel);
      this.handler.postDelayed(this.resendSendRunnable, 10000L);
      this.getOutputStream().write(var1);
      if (var1[0] == 4 && ConstantUtil.INSTANCE.isDeviceModel(this.deviceModel)) {
         FscSppCentralCallbacks var2;
         if ((var2 = this.mFscSppCentralCallbacks) != null) {
            var2.otaProgressUpdate(this.getDeviceWrapper().getMAddress(), 100.0F, XmodemUtils.OTA_STATUS_FINISH);
         }

         this.mFscSppCentralCallbacks = null;
      }

   }

   private static final void resendSendRunnable$lambda_2/* $FF was: resendSendRunnable$lambda-2*/(OTAService var0) {
      Intrinsics.checkNotNullParameter(var0, "this$0");
      MsgLogger.e("十秒钟未收到回复  重新发送");
      if (var0.cmd != null) {
         var0.getOutputStream().write(var0.cmd);
      }

   }

   // $FF: synthetic method
   public static final byte[] access$getSppStatBuffer$p(OTAService var0) {
      return var0.sppStatBuffer;
   }

   // $FF: synthetic method
   public static final Runnable access$getResendSendRunnable$p(OTAService var0) {
      return var0.resendSendRunnable;
   }

   // $FF: synthetic method
   public static final void access$send(OTAService var0, byte[] var1) {
      var0.send(var1);
   }

   @Nullable
   public IBinder onBind(@Nullable Intent var1) {
      if (var1 != null) {
         Bundle var2;
         IBinder var4;
         if ((var2 = var1.getBundleExtra("bundle")) != null) {
            var4 = var2.getBinder("bigData");
         } else {
            var4 = null;
         }

         Intrinsics.checkNotNull(var4, "null cannot be cast to non-null type com.feasycom.spp.bean.BigData");
         this.fileByte = ((BigData)var4).getData();
         StringBuilder var5 = (new StringBuilder()).append("bindService fileByte => ");
         byte[] var3;
         if ((var3 = this.fileByte) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileByte");
            var3 = null;
         }

         MsgLogger.e(var5.append(ExpandKt.bytesToHex(var3)).toString());
         this.deviceModel = var1.getIntExtra("deviceModel", 0);
         return new LocalBinder();
      } else {
         return null;
      }
   }

   @NotNull
   public final XmodemUtils getXModemUtils() {
      XmodemUtils var1;
      if ((var1 = this.xModemUtils) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("xModemUtils");
         return null;
      }
   }

   public final void setXModemUtils(@NotNull XmodemUtils var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.xModemUtils = var1;
   }

   @NotNull
   public final OutputStream getOutputStream() {
      OutputStream var1;
      if ((var1 = this.outputStream) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("outputStream");
         return null;
      }
   }

   public final void setOutputStream(@NotNull OutputStream var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.outputStream = var1;
   }

   @NotNull
   public final InputStream getInputStream() {
      InputStream var1;
      if ((var1 = this.inputStream) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("inputStream");
         return null;
      }
   }

   public final void setInputStream(@NotNull InputStream var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.inputStream = var1;
   }

   @Nullable
   public final FscSppCentralCallbacks getMFscSppCentralCallbacks() {
      return this.mFscSppCentralCallbacks;
   }

   public final void setMFscSppCentralCallbacks(@Nullable FscSppCentralCallbacks var1) {
      this.mFscSppCentralCallbacks = var1;
   }

   @NotNull
   public final BluetoothDeviceWrapper getDeviceWrapper() {
      BluetoothDeviceWrapper var1;
      if ((var1 = this.deviceWrapper) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("deviceWrapper");
         return null;
      }
   }

   public final void setDeviceWrapper(@NotNull BluetoothDeviceWrapper var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.deviceWrapper = var1;
   }

   public final boolean getFinish() {
      return this.finish;
   }

   public final void setFinish(boolean var1) {
      this.finish = var1;
   }

   public final int getNakNumber() {
      return this.nakNumber;
   }

   public final void setNakNumber(int var1) {
      this.nakNumber = var1;
   }

   public final void startOta(@NotNull final BluetoothDeviceWrapper var1) {
      Intrinsics.checkNotNullParameter(var1, "deviceWrapper");
      MsgLogger.e("startOta 正真启动了服务");
      this.setDeviceWrapper(var1);
      FscSppCentralCallbacks var2;
      FscSppCentralCallbacks var10000 = var2 = var1.getMFscSppCentralCallbacks();
      this.mFscSppCentralCallbacks = var2;
      if (var10000 != null) {
         var10000 = var2;
         String var4 = var1.getMAddress();
         int var3 = XmodemUtils.OTA_STATUS_BEGIN;
         var10000.otaProgressUpdate(var4, 0.0F, var3);
      }

      OutputStream var5;
      if ((var5 = var1.getMOutputStream()) != null) {
         this.setOutputStream(var5);
      }

      InputStream var6;
      if ((var6 = var1.getMInputStream()) != null) {
         this.setInputStream(var6);
      }

      StringBuilder var7 = (new StringBuilder()).append("升级文件大小 => ");
      byte[] var8;
      if ((var8 = this.fileByte) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("fileByte");
         var8 = null;
      }

      MsgLogger.d(var7.append(var8.length).toString());
      var7 = (new StringBuilder()).append("发送数据 => ");
      if ((var8 = this.fileByte) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("fileByte");
         var8 = null;
      }

      MsgLogger.d(var7.append(ExpandKt.bytesToHex(var8)).toString());
      XmodemUtils var9 = new XmodemUtils;
      if ((var8 = this.fileByte) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("fileByte");
         var8 = null;
      }

      var9.<init>(var8);
      this.setXModemUtils(var9);
      this.getXModemUtils().setCompleteCallback(new Function0() {
         public final void a() {
            MsgLogger.d("startOta: 升级完成");
            FscSppCentralCallbacks var1x;
            if ((var1x = OTAService.this.getMFscSppCentralCallbacks()) != null) {
               var1x.otaProgressUpdate(var1.getMAddress(), 100.0F, XmodemUtils.OTA_STATUS_FINISH);
            }

            OTAService.this.setMFscSppCentralCallbacks((FscSppCentralCallbacks)null);
         }
      });
      this.getXModemUtils().setProgressUpdateCallback(new Function1() {
         public final void a(float var1x) {
            FscSppCentralCallbacks var2;
            if ((var2 = OTAService.this.getMFscSppCentralCallbacks()) != null) {
               var2.otaProgressUpdate(var1.getMAddress(), var1x, XmodemUtils.OTA_STATUS_PROCESSING);
            }

         }
      });
      GlobalScope var10001 = GlobalScope.INSTANCE;
      CoroutineDispatcher var10002 = Dispatchers.getIO();
      Function2 var10;
      var10 = new Function2() {
         public int a;
         // $FF: synthetic field
         public Object b;
         // $FF: synthetic field
         public final BluetoothDeviceWrapper d;

         public {
            this.d = var2;
         }

         @Nullable
         public final Object invokeSuspend(@NotNull Object param1) {
            // $FF: Couldn't be decompiled
         }

         @NotNull
         public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
            <undefinedtype> var10003 = this;
            OTAService var3 = OTAService.this;
            Function2 var10000 = new <anonymous constructor>(var10003.d, var2);
            var10000.b = var1;
            return var10000;
         }

         @Nullable
         public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
            return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
         }
      }.<init>(var1, (Continuation)null);
      this.otaJob = BuildersKt.launch$default(var10001, var10002, (CoroutineStart)null, var10, 2, (Object)null);
   }

   @NotNull
   public final Handler getHandler() {
      return this.handler;
   }

   @Nullable
   public final byte[] getCmd() {
      return this.cmd;
   }

   public final void setCmd(@Nullable byte[] var1) {
      this.cmd = var1;
   }

   public boolean onUnbind(@Nullable Intent var1) {
      Job var2;
      if ((var2 = this.otaJob) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("otaJob");
         var2 = null;
      }

      DefaultImpls.cancel$default(var2, (CancellationException)null, 1, (Object)null);
      return super.onUnbind(var1);
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"},
      d2 = {"Lcom/feasycom/spp/service/OTAService$LocalBinder;", "Landroid/os/Binder;", "(Lcom/feasycom/spp/service/OTAService;)V", "service", "Lcom/feasycom/spp/service/OTAService;", "getService", "()Lcom/feasycom/spp/service/OTAService;", "FeasyBlueLibrary_release"}
   )
   public final class LocalBinder extends Binder {
      @NotNull
      public final OTAService getService() {
         return OTAService.this;
      }
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"},
      d2 = {"Lcom/feasycom/spp/service/OTAService$Companion;", "", "()V", "TAG", "", "FeasyBlueLibrary_release"}
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
