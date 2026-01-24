package com.feasycom.ble.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
   d2 = {"Lcom/feasycom/ble/utils/CryptoUtils;", "", "()V", "Companion", "FeasyBlueLibrary_release"}
)
public final class CryptoUtils {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   private static final int DELTA = -1640531527;
   @NotNull
   private static final int[] k = new int[]{1233145762, 117691475, 1510088720, 1464937610};
   @NotNull
   private static final int[] v = new int[]{-603115520, 494272512};

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\tJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0004J\u0016\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\tJ\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\tJ\u000e\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\tJ\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\tJ\u0016\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u001cR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"},
      d2 = {"Lcom/feasycom/ble/utils/CryptoUtils$Companion;", "", "()V", "DELTA", "", "k", "", "v", "convertMacAddressLittleToBigEndian", "", "littleEndianMac", "convertMacAddressToIntArray", "macAddress", "decrypt", "", "encryptedData", "key", "encrypt", "data", "formatMacAddress", "hexStringToByteArray", "hexString", "hexStringToInt", "hex", "parseCompleteLocalName", "", "tea", "N", "", "FeasyBlueLibrary_release"}
   )
   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      @NotNull
      public final int[] convertMacAddressToIntArray(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "macAddress");
         if (var1.length() == 12) {
            String var2;
            Intrinsics.checkNotNullExpressionValue(var2 = var1.substring(0, 8), "this as java.lang.String…ing(startIndex, endIndex)");
            String var10000 = var1.substring(8);
            Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).substring(startIndex)");
            var10000 = StringsKt.padEnd(var10000, 8, '0');
            int var3 = (int)Long.parseLong(var2, CharsKt.checkRadix(16));
            int var4 = (int)Long.parseLong(var10000, CharsKt.checkRadix(16));
            return new int[]{var3, var4};
         } else {
            throw new IllegalArgumentException("MAC 地址长度应为 12 位".toString());
         }
      }

      public final void tea(@NotNull int[] var1, long var2) {
         Intrinsics.checkNotNullParameter(var1, "v");
         int var6 = var1[0];
         int var4 = var1[1];
         int var5 = 0;
         int var10001;
         int var7;
         if (var2 > 0L) {
            for(var7 = (int)var2 * -1640531527; var5 != var7; var4 += (var10001 << 4 ^ var6 >>> 5) + (var6 ^ var5) + CryptoUtils.k[var5 >>> 11 & 3]) {
               var10001 = var6 += (var4 << 4 ^ var4 >>> 5) + (var4 ^ var5) + CryptoUtils.k[var5 & 3];
               var5 += -1640531527;
            }
         } else {
            for(var7 = (int)(-var2) * -1640531527; var7 != 0; var6 -= (var10001 << 4 ^ var4 >>> 5) + (var4 ^ var7) + CryptoUtils.k[var7 & 3]) {
               var10001 = var4 -= (var6 << 4 ^ var6 >>> 5) + (var6 ^ var7) + CryptoUtils.k[var7 >>> 11 & 3];
               var7 -= -1640531527;
            }
         }

         var1[0] = var6;
         var1[1] = var4;
      }

      @NotNull
      public final byte[] encrypt(@NotNull byte[] var1, int var2) {
         Intrinsics.checkNotNullParameter(var1, "data");
         byte[] var10 = new byte[var1.length];
         int var3 = var1.length / 4;

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            int var5;
            int var6;
            int var7;
            int var8;
            int var9 = (var1[var5 = var4 * 4] & 255 | (var1[var6 = var5 + 1] & 255) << 8 | (var1[var7 = var5 + 2] & 255) << 16 | (var1[var8 = var5 + 3] & 255) << 24) ^ var2;
            var10[var5] = (byte)(var9 & 255);
            var10[var6] = (byte)(var9 >> 8 & 255);
            var10[var7] = (byte)(var9 >> 16 & 255);
            var10[var8] = (byte)(var9 >> 24 & 255);
         }

         var3 *= 4;

         for(var4 = var1.length; var3 < var4; ++var3) {
            var10[var3] = (byte)(var1[var3] ^ var2 >> var3 % 4 * 8);
         }

         return var10;
      }

      @NotNull
      public final byte[] decrypt(@NotNull byte[] var1, int var2) {
         Intrinsics.checkNotNullParameter(var1, "encryptedData");
         byte[] var10 = new byte[var1.length];
         int var3 = var1.length / 4;

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            int var5;
            int var6;
            int var7;
            int var8;
            int var9 = (var1[var5 = var4 * 4] & 255 | (var1[var6 = var5 + 1] & 255) << 8 | (var1[var7 = var5 + 2] & 255) << 16 | (var1[var8 = var5 + 3] & 255) << 24) ^ var2;
            var10[var5] = (byte)(var9 & 255);
            var10[var6] = (byte)(var9 >> 8 & 255);
            var10[var7] = (byte)(var9 >> 16 & 255);
            var10[var8] = (byte)(var9 >> 24 & 255);
         }

         var3 *= 4;

         for(var4 = var1.length; var3 < var4; ++var3) {
            var10[var3] = (byte)(var1[var3] ^ var2 >> var3 % 4 * 8);
         }

         return var10;
      }

      @NotNull
      public final byte[] hexStringToByteArray(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "hexString");
         int var6;
         byte[] var2 = new byte[(var6 = var1.length()) / 2];
         IntProgression var10000 = RangesKt.step(RangesKt.until(0, var6), 2);
         var6 = var10000.getFirst();
         int var3 = var10000.getLast();
         int var4;
         if ((var4 = var10000.getStep()) > 0 && var6 <= var3 || var4 < 0 && var3 <= var6) {
            while(true) {
               int var5 = var6 / 2;
               var2[var5] = (byte)((CharsKt.digitToInt(var1.charAt(var6), 16) << 4) + CharsKt.digitToInt(var1.charAt(var6 + 1), 16));
               if (var6 == var3) {
                  break;
               }

               var6 += var4;
            }
         }

         return var2;
      }

      public final int hexStringToInt(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "hex");
         return Integer.parseInt(var1, CharsKt.checkRadix(16));
      }

      public final void parseCompleteLocalName(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "data");
         byte[] var10000 = this.hexStringToByteArray(var1);
         Charset var2;
         Intrinsics.checkNotNullExpressionValue(var2 = StandardCharsets.UTF_8, "UTF_8");
         new String(var10000, var2);
      }

      @NotNull
      public final String convertMacAddressLittleToBigEndian(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "littleEndianMac");
         StringBuilder var5;
         var5 = new StringBuilder.<init>();
         int var2;
         int var3;
         if ((var3 = ProgressionUtilKt.getProgressionLastElement(var2 = var1.length() - 2, 0, -2)) <= var2) {
            while(true) {
               int var4 = var2 + 2;
               var5.append(var1, var2, var4);
               if (var2 == var3) {
                  break;
               }

               var2 -= 2;
            }
         }

         String var10000 = var5.toString();
         Intrinsics.checkNotNullExpressionValue(var10000, "bigEndianMac.toString()");
         return var10000;
      }

      @NotNull
      public final String formatMacAddress(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "macAddress");
         if (var1.length() == 12) {
            List var10000 = StringsKt.chunked(var1, 2);
            <undefinedtype> var2 = null.a;
            return CollectionsKt.joinToString$default(var10000, ":", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, var2, 30, (Object)null);
         } else {
            throw new IllegalArgumentException("MAC address must be 12 characters long".toString());
         }
      }
   }
}
