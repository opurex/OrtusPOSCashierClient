package com.feasycom.ble.utils;

import com.feasycom.common.bean.FscDevice;
import com.feasycom.common.utils.ExpandKt;
import com.feasycom.common.utils.MsgLogger;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
   d2 = {"Lcom/feasycom/ble/utils/FeasycomUtil;", "", "()V", "Companion", "FeasyBlueLibrary_release"}
)
public final class FeasycomUtil {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private static final String TAG = "FeasycomUtil";

   @JvmStatic
   public static final synchronized void parseAdvData(@NotNull FscDevice var0, @NotNull byte[] var1) {
      Companion.parseAdvData(var0, var1);
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000¨\u0006\u000f"},
      d2 = {"Lcom/feasycom/ble/utils/FeasycomUtil$Companion;", "", "()V", "TAG", "", "byteToInt", "", "a", "", "parseAdvData", "", "fscDevice", "Lcom/feasycom/common/bean/FscDevice;", "scanData", "", "FeasyBlueLibrary_release"}
   )
   public static final class Companion {
      private Companion() {
      }

      private final int byteToInt(byte var1) {
         return var1 & 255;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      @JvmStatic
      public final synchronized void parseAdvData(@NotNull FscDevice var1, @NotNull byte[] var2) {
         Intrinsics.checkNotNullParameter(var1, "fscDevice");
         Intrinsics.checkNotNullParameter(var2, "scanData");

         int var4;
         for(int var3 = 0; var3 < var2.length; var3 = var3 + var4 + 1) {
            if ((var4 = this.byteToInt(var2[var3])) == 0) {
               return;
            }

            if (var4 > var2.length - var3) {
               return;
            }

            int var5;
            int var6;
            int var10000 = var6 = this.byteToInt(var2[var5 = var3 + 1]);
            byte[] var10001 = var2;
            int var10002 = var5;
            int var10003 = var5 = var4 - 1;

            byte[] var7;
            Exception var18;
            boolean var19;
            byte[] var23;
            label85: {
               label93: {
                  try {
                     var23 = new byte[var10003];
                  } catch (Exception var13) {
                     var18 = var13;
                     var19 = false;
                     break label93;
                  }

                  var7 = var23;
                  ++var10002;

                  try {
                     System.arraycopy(var10001, var10002, var7, 0, var5);
                     break label85;
                  } catch (Exception var12) {
                     var18 = var12;
                     var19 = false;
                  }
               }

               var18.printStackTrace();
               return;
            }

            MsgLogger.e("parseAdvData", "scanRecord111 => " + ExpandKt.bytesToHex(var7));
            if (var10000 != 1) {
               if (var6 != 2) {
                  if (var6 != 6) {
                     if (var6 != 22) {
                        String var14;
                        if (var6 != 255) {
                           if (var6 != 9) {
                              if (var6 == 10) {
                                 var1.setTxPowerLevel(ExpandKt.bytesToHex(var7));
                              }
                           } else {
                              var14 = new String.<init>(var7, Charsets.UTF_8);
                              var1.setCompleteLocalName(var14);
                           }
                        } else {
                           String var20 = var14 = StringsKt.replace$default(ExpandKt.bytesToHex(var7), " ", "", false, 4, (Object)null);
                           MsgLogger.e("parseAdvData", "specificData => " + var14);
                           if (StringsKt.contains$default(var20, "A0550A00", false, 2, (Object)null)) {
                              MsgLogger.e("parseAdvData", "macAddress (Big Endian) => " + CollectionsKt.joinToString$default(CollectionsKt.reversed(StringsKt.chunked(StringsKt.substringAfter(var14, "A0550A00", ""), 2)), "", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
                           }
                        }
                     } else {
                        var1.setServiceData(ExpandKt.bytesToHex(var7));
                     }
                  } else {
                     byte[] var16 = new byte[var5];

                     for(int var8 = 0; var8 < var5; ++var8) {
                        int var9 = var5 - 1 - var8;
                        var16[var9] = var7[var8];
                     }

                     var1.setIncompleteServiceUUIDs_128bit(ExpandKt.bytesToHex(var16));
                  }
               } else {
                  label95: {
                     byte[] var10004;
                     FscDevice var21;
                     byte var10005;
                     byte[] var22;
                     try {
                        var21 = var1;
                        var10001 = var7;
                        var22 = var7;
                        var23 = var7;
                        var10004 = var7;
                        var10005 = var7[0];
                     } catch (Exception var11) {
                        var18 = var11;
                        var19 = false;
                        break label95;
                     }

                     byte var15 = var10005;
                     byte var17 = 0;

                     try {
                        var23[var17] = var10004[1];
                        var22[1] = var15;
                        var21.setIncompleteServiceUUIDs_16bit(ExpandKt.bytesToHex(var10001));
                        continue;
                     } catch (Exception var10) {
                        var18 = var10;
                        var19 = false;
                     }
                  }

                  var18.printStackTrace();
               }
            } else {
               var1.setFlag(ExpandKt.bytesToHex(var7));
            }
         }

      }
   }
}
