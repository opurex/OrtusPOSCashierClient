package com.feasycom.ota.utils;

import androidx.annotation.Keep;
import com.feasycom.ota.error.CException;
import com.feasycom.ota.error.SendingCanceledException;
import com.feasycom.ota.error.SendingEndedException;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
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
   d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0005\n\u0002\b\u0003\u0018\u0000 +2\u00020\u0001:\u0001+B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020)H\u0007J\b\u0010*\u001a\u00020\u0003H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R6\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u000e\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t8\u0006@GX\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0004R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0003X\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0003X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0013\"\u0004\b\u0019\u0010\u0004R\u000e\u0010\u001a\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R`\u0010!\u001a\u001f\u0012\u0013\u0012\u00110\u001c¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0004\u0012\u00020\n\u0018\u00010\u001d2#\u0010\b\u001a\u001f\u0012\u0013\u0012\u00110\u001c¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0004\u0012\u00020\n\u0018\u00010\u001d8\u0006@GX\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u000e\u0010&\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006,"},
   d2 = {"Lcom/feasycom/ota/utils/XmodemUtils;", "", "fileByte", "", "([B)V", "begin", "", "byteCount", "value", "Lkotlin/Function0;", "", "completeCallback", "getCompleteCallback", "()Lkotlin/jvm/functions/Function0;", "setCompleteCallback", "(Lkotlin/jvm/functions/Function0;)V", "crc16", "Lcom/feasycom/ota/utils/CRC16;", "getFileByte", "()[B", "setFileByte", "fileByteLen", "fileByteSrc", "mCmd", "getMCmd", "setMCmd", "packageNo", "percentage", "", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "progress", "progressUpdateCallback", "getProgressUpdateCallback", "()Lkotlin/jvm/functions/Function1;", "setProgressUpdateCallback", "(Lkotlin/jvm/functions/Function1;)V", "xModemBuffer", "getData", "data", "", "startTrans", "Companion", "FeasyBlueLibrary_release"}
)
public final class XmodemUtils {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private static final String TAG = "Xmodem";
   private static final byte SOH = 1;
   private static final byte STX = 2;
   private static final byte C = 67;
   private static final byte S = 83;
   private static final byte EOT = 4;
   private static final byte ACK = 6;
   private static final byte NAK = 21;
   private static final byte CAN = 24;
   private static final byte CTRLZ = 26;
   @Keep
   @JvmField
   public static final int OTA_STATUS_BEGIN = 110;
   @Keep
   @JvmField
   public static final int OTA_STATUS_PROCESSING = 121;
   @Keep
   @JvmField
   public static final int OTA_STATUS_FINISH = 10086;
   @Keep
   @JvmField
   public static final int OTA_STATUS_FAILED = 120;
   @Keep
   @JvmField
   public static final int OTA_STATUS_VERIFY_FAILED = 122;
   @Keep
   @JvmField
   public static final int OTA_STATUS_VERIFY_MODEL_FAILED = 124;
   @Keep
   @JvmField
   public static final int OTA_STATUS_VERIFY_APP_VERSION_FAILED = 125;
   @Keep
   @JvmField
   public static final int OTA_STATUS_AT_FAILED = 123;
   @NotNull
   private byte[] fileByte;
   private byte[] fileByteSrc;
   private int fileByteLen;
   @NotNull
   private byte[] xModemBuffer;
   private int packageNo;
   private int begin;
   private float percentage;
   private int byteCount;
   @NotNull
   private final CRC16 crc16;
   @Keep
   @Nullable
   private Function0 completeCallback;
   @Keep
   @Nullable
   private Function1 progressUpdateCallback;
   public byte[] mCmd;

   public XmodemUtils(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "fileByte");
      super();
      this.fileByte = var1;
      this.xModemBuffer = new byte[1029];
      this.packageNo = 1;
      this.crc16 = new CRC16();
   }

   private final byte[] startTrans() {
      byte[] var1;
      byte[] var10002 = var1 = this.xModemBuffer;
      var1[0] = STX;
      int var10003 = this.packageNo;
      var1[1] = (byte)var10003;
      var10002[2] = (byte)(~var10003);
      int var8 = this.fileByteLen;
      int var2;
      int var10000 = var2 = this.begin;
      this.byteCount = var8 - var2;
      float var9;
      float var13 = var9 = (float)(var10000 * 100 / var8);
      this.percentage = var9;
      if (var13 >= 100.0F) {
         this.percentage = 100.0F;
         Function1 var11;
         if ((var11 = this.progressUpdateCallback) != null) {
            var11.invoke(100.0F);
         }
      } else {
         Function1 var12;
         if ((var12 = this.progressUpdateCallback) != null) {
            var12.invoke(var9);
         }
      }

      if (this.byteCount > 1024) {
         this.byteCount = 1024;
      }

      if (this.mCmd != null && this.getMCmd()[0] == 4) {
         Function0 var10;
         if ((var10 = this.completeCallback) != null) {
            var10.invoke();
         }

         throw new SendingEndedException("发送结束");
      } else {
         byte[] var7;
         if (this.byteCount > 0) {
            for(var8 = 3; var8 < 1027; ++var8) {
               this.xModemBuffer[var8] = 0;
            }

            byte[] var3;
            int var4;
            byte[] var5;
            if ((var8 = this.byteCount) == 0) {
               this.xModemBuffer[3] = 26;
            } else {
               for(var2 = 0; var2 < var8; ++var2) {
                  var3 = this.xModemBuffer;
                  var4 = var2 + 3;
                  if ((var5 = this.fileByteSrc) == null) {
                     Intrinsics.throwUninitializedPropertyAccessException("fileByteSrc");
                     var5 = null;
                  }

                  var3[var4] = var5[this.begin + var2];
               }

               if ((var8 = this.byteCount) < 1024) {
                  this.xModemBuffer[var8 + 3] = 26;
               }
            }

            var8 = 0;

            for(var2 = this.byteCount; var8 < var2; ++var8) {
               var3 = this.xModemBuffer;
               var4 = var8 + 3;
               if ((var5 = this.fileByteSrc) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("fileByteSrc");
                  var5 = null;
               }

               var3[var4] = var5[this.begin + var8];
            }

            if ((var8 = this.byteCount) < 1024) {
               this.xModemBuffer[var8 + 3] = 26;
            }

            var1 = new byte[1024];

            for(var2 = 0; var2 < 1024; ++var2) {
               var1[var2] = this.xModemBuffer[var2 + 3];
            }

            short var6 = this.crc16.crc16_ccitt(var1, 1024);
            var10002 = this.xModemBuffer;
            var10002[1027] = (byte)(var6 >> 8 & 255);
            var10002[1028] = (byte)(var6 & 255);
            this.setMCmd(var10002);
            var7 = this.xModemBuffer;
         } else {
            (var7 = new byte[1])[0] = 4;
            this.setMCmd(var7);
         }

         return var7;
      }
   }

   @NotNull
   public final byte[] getFileByte() {
      return this.fileByte;
   }

   public final void setFileByte(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.fileByte = var1;
   }

   @Nullable
   public final Function0 getCompleteCallback() {
      return this.completeCallback;
   }

   @Keep
   public final void setCompleteCallback(@Nullable Function0 var1) {
      this.completeCallback = var1;
   }

   @Nullable
   public final Function1 getProgressUpdateCallback() {
      return this.progressUpdateCallback;
   }

   @Keep
   public final void setProgressUpdateCallback(@Nullable Function1 var1) {
      this.progressUpdateCallback = var1;
   }

   @NotNull
   public final byte[] getMCmd() {
      byte[] var1;
      if ((var1 = this.mCmd) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("mCmd");
         return null;
      }
   }

   public final void setMCmd(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.mCmd = var1;
   }

   @Keep
   @NotNull
   public final byte[] getData(byte var1) {
      XmodemUtils var10000;
      XmodemUtils var10001;
      byte[] var6;
      if (var1 == 67) {
         var10000 = this;
         var10001 = this;
         XmodemUtils var10002 = this;
         byte[] var10003 = this.fileByte;
         int var3;
         byte[] var5 = new byte[var3 = var10003.length - 1024];
         int var2 = var10003.length - 1024;
         System.arraycopy(var10003, 1024, var5, 0, var2);
         var10002.fileByteSrc = var5;
         var10001.fileByteLen = var3;
         var6 = var10000.startTrans();
      } else if (var1 == 83) {
         var10000 = this;
         var10001 = this;
         byte[] var4;
         this.fileByteSrc = var4 = this.fileByte;
         var10001.fileByteLen = var4.length;
         var6 = var10000.startTrans();
      } else if (var1 == 6) {
         this.begin += 1024;
         ++this.packageNo;
         var6 = this.startTrans();
      } else {
         if (var1 == 4) {
            throw new SendingEndedException("发送结束");
         }

         if (var1 == 24) {
            throw new SendingCanceledException("取消发送");
         }

         if (var1 != 21) {
            throw new CException("发送结束");
         }

         var6 = this.xModemBuffer;
      }

      return var6;
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0000\b\u0087\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u000b8\u0006X\u0087D¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0018"},
      d2 = {"Lcom/feasycom/ota/utils/XmodemUtils$Companion;", "", "()V", "ACK", "", "C", "CAN", "CTRLZ", "EOT", "NAK", "OTA_STATUS_AT_FAILED", "", "OTA_STATUS_BEGIN", "OTA_STATUS_FAILED", "OTA_STATUS_FINISH", "OTA_STATUS_PROCESSING", "OTA_STATUS_VERIFY_APP_VERSION_FAILED", "OTA_STATUS_VERIFY_FAILED", "OTA_STATUS_VERIFY_MODEL_FAILED", "S", "SOH", "STX", "TAG", "", "FeasyBlueLibrary_release"}
   )
   @Keep
   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
