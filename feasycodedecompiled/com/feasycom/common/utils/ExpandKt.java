package com.feasycom.common.utils;

import androidx.annotation.Keep;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 2,
   xi = 48,
   d1 = {"\u00000\n\u0000\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\t\u001a\u000e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003\u001a\u000e\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003\u001a\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003\u001a\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003\u001a\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0003\u001a\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00112\u0006\u0010\u0012\u001a\u00020\u0003\u001a\u000e\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0003\u001a\f\u0010\u0014\u001a\u00020\u0003*\u00020\bH\u0007\u001a\u000e\u0010\u0015\u001a\u0004\u0018\u00010\u0003*\u00020\u0003H\u0007\u001a\f\u0010\u0016\u001a\u00020\u0003*\u00020\u0003H\u0007\u001a\f\u0010\u0017\u001a\u00020\u0003*\u00020\u0003H\u0007\u001a\f\u0010\u0018\u001a\u00020\u0003*\u00020\u0003H\u0007\u001a\f\u0010\u0019\u001a\u00020\u0003*\u00020\bH\u0007\u001a\f\u0010\u0019\u001a\u00020\u0003*\u00020\u0003H\u0007\"\u0010\u0010\u0000\u001a\u00020\u00018\u0002X\u0083\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"},
   d2 = {"HEX_ARRAY", "", "convertMacAddressLittleToBigEndian", "", "littleEndianMac", "formatMacAddress", "macAddress", "hexStringToByteArray", "", "s", "hexStringToInt", "", "hex", "parseCompleteLocalName", "", "data", "splitHexStringToInt", "", "hexString", "toByteArray", "bytesToHex", "getParam", "hexToByte", "parsingModel", "parsingVersion", "toHexString", "FeasyBlueLibrary_release"}
)
public final class ExpandKt {
   @Keep
   @NotNull
   private static final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   @Keep
   @NotNull
   public static final String bytesToHex(@NotNull byte[] var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      if (var0.length > 0 && var0.length > 0) {
         int var1 = RangesKt.coerceAtMost(var0.length, var0.length - 0);
         StringBuilder var2;
         var2 = new StringBuilder.<init>();

         for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = var0[var3] & 255;
            char[] var5;
            var2.append((var5 = HEX_ARRAY)[var4 >>> 4]).append(var5[var4 & 15]).append(" ");
         }

         String var10000 = var2.toString();
         Intrinsics.checkNotNullExpressionValue(var10000, "hexChars.toString()");
         return var10000;
      } else {
         return "";
      }
   }

   @Keep
   @NotNull
   public static final String toHexString(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      byte[] var10000 = var0.getBytes(Charsets.UTF_8);
      Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).getBytes(charset)");
      return bytesToHex(var10000);
   }

   @Keep
   @NotNull
   public static final String hexToByte(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      if (var0.length() <= 0) {
         return " ";
      } else {
         if ((var0 = StringsKt.replace$default(var0, " ", "", false, 4, (Object)null)).length() % 2 == 1) {
            StringBuffer var10000 = new StringBuffer(var0);
            var10000.insert(var0.length() - 1, '0');
            Intrinsics.checkNotNullExpressionValue(var0 = var10000.toString(), "strBuf.toString()");
         }

         byte[] var1 = new byte[var0.length() / 2];

         int var3;
         for(int var2 = 0; var2 < var0.length(); var2 = var3) {
            var3 = var2 + 2;
            String var4;
            Intrinsics.checkNotNullExpressionValue(var4 = var0.substring(var2, var3), "this as java.lang.String…ing(startIndex, endIndex)");
            if (var2 == 0) {
               var1[var2] = (byte)Integer.parseInt(var4, CharsKt.checkRadix(16));
            } else {
               var2 /= 2;
               var1[var2] = (byte)Integer.parseInt(var4, CharsKt.checkRadix(16));
            }
         }

         return new String(var1, Charsets.UTF_8);
      }
   }

   @Keep
   @Nullable
   public static final String getParam(@NotNull String var0) {
      String var10000 = var0;
      Intrinsics.checkNotNullParameter(var0, "<this>");

      Exception var8;
      label51: {
         boolean var10001;
         try {
            MsgLogger.e("getParam => " + var0);
         } catch (Exception var5) {
            var8 = var5;
            var10001 = false;
            break label51;
         }

         String var10 = "=";

         boolean var9;
         try {
            var9 = StringsKt.contains$default(var10000, var10, false, 2, (Object)null);
         } catch (Exception var4) {
            var8 = var4;
            var10001 = false;
            break label51;
         }

         if (!var9) {
            return var0;
         }

         int var10002;
         try {
            var10000 = var0;
            var10 = var0;
            var10002 = StringsKt.indexOf$default(var0, "=", 0, false, 6, (Object)null);
         } catch (Exception var3) {
            var8 = var3;
            var10001 = false;
            break label51;
         }

         int var6 = var10002;

         try {
            var10000 = var10000.substring(var6, var10.length());
         } catch (Exception var2) {
            var8 = var2;
            var10001 = false;
            break label51;
         }

         var0 = var10000;

         try {
            Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String…ing(startIndex, endIndex)");
            return var0;
         } catch (Exception var1) {
            var8 = var1;
            var10001 = false;
         }
      }

      Exception var7 = var8;
      var7.printStackTrace();
      MsgLogger.e("getParam Exception => " + var7.getMessage());
      var0 = null;
      return var0;
   }

   @Keep
   @NotNull
   public static final String parsingVersion(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      List var10000 = StringsKt.split$default(var0, new String[]{","}, false, 0, 6, (Object)null);
      ArrayList var3;
      var3 = new ArrayList.<init>();
      Iterator var1 = var10000.iterator();

      while(var1.hasNext()) {
         Object var2;
         if (StringsKt.contains$default((String)(var2 = var1.next()), ".", false, 2, (Object)null)) {
            var3.add(var2);
         }
      }

      CharSequence var4 = (CharSequence)var3.get(0);
      return (new Regex("[a-zA-Z.]")).replace(var4, "");
   }

   @Keep
   @NotNull
   public static final String parsingModel(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      List var10000 = StringsKt.split$default(var0, new String[]{","}, false, 0, 6, (Object)null);
      ArrayList var4;
      var4 = new ArrayList.<init>();
      Iterator var1 = var10000.iterator();

      while(var1.hasNext()) {
         Object var2;
         String var3 = (String)(var2 = var1.next());
         if ((new Regex("(FSC|-|BP|BT)")).containsMatchIn(var3)) {
            var4.add(var2);
         }
      }

      return StringsKt.replace$default((String)var4.get(0), "FSC-", "", false, 4, (Object)null);
   }

   @Keep
   @NotNull
   public static final String toHexString(@NotNull byte[] var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      StringBuilder var1;
      var1 = new StringBuilder.<init>(var0.length);
      int var2 = 0;

      for(int var3 = var0.length; var2 < var3; ++var2) {
         byte var4 = var0[var2];
         String var10001 = String.format("%02x ", Arrays.copyOf(new Object[]{var4}, 1));
         Intrinsics.checkNotNullExpressionValue(var10001, "format(format, *args)");
         Locale var5;
         Intrinsics.checkNotNullExpressionValue(var5 = Locale.ROOT, "ROOT");
         String var6;
         Intrinsics.checkNotNullExpressionValue(var6 = var10001.toUpperCase(var5), "this as java.lang.String).toUpperCase(locale)");
         var1.append(var6);
      }

      String var10000 = var1.toString();
      Intrinsics.checkNotNullExpressionValue(var10000, "sb.toString()");
      return var10000;
   }

   @NotNull
   public static final byte[] toByteArray(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "hexString");
      int var1;
      byte[] var2 = new byte[(var1 = var0.length()) / 2];
      IntProgression var10000 = RangesKt.step(RangesKt.until(0, var1), 2);
      var1 = var10000.getFirst();
      int var3 = var10000.getLast();
      int var4;
      if ((var4 = var10000.getStep()) > 0 && var1 <= var3 || var4 < 0 && var3 <= var1) {
         while(true) {
            int var5 = var1 / 2;
            var2[var5] = (byte)((Character.digit(var0.charAt(var1), 16) << 4) + Character.digit(var0.charAt(var1 + 1), 16));
            if (var1 == var3) {
               break;
            }

            var1 += var4;
         }
      }

      return var2;
   }

   public static final void parseCompleteLocalName(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "data");
      byte[] var10000 = hexStringToByteArray(var0);
      Charset var1;
      Intrinsics.checkNotNullExpressionValue(var1 = StandardCharsets.UTF_8, "UTF_8");
      new String(var10000, var1);
   }

   @NotNull
   public static final byte[] hexStringToByteArray(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "s");
      int var1;
      byte[] var2 = new byte[(var1 = var0.length()) / 2];
      IntProgression var10000 = RangesKt.step(RangesKt.until(0, var1), 2);
      var1 = var10000.getFirst();
      int var3 = var10000.getLast();
      int var4;
      if ((var4 = var10000.getStep()) > 0 && var1 <= var3 || var4 < 0 && var3 <= var1) {
         while(true) {
            int var5 = var1 / 2;
            var2[var5] = (byte)((Character.digit(var0.charAt(var1), 16) << 4) + Character.digit(var0.charAt(var1 + 1), 16));
            if (var1 == var3) {
               break;
            }

            var1 += var4;
         }
      }

      return var2;
   }

   public static final int hexStringToInt(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "hex");
      return Integer.parseInt(var0, CharsKt.checkRadix(16));
   }

   @NotNull
   public static final String convertMacAddressLittleToBigEndian(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "littleEndianMac");
      StringBuilder var1;
      var1 = new StringBuilder.<init>();
      int var2;
      int var3;
      if ((var3 = ProgressionUtilKt.getProgressionLastElement(var2 = var0.length() - 2, 0, -2)) <= var2) {
         while(true) {
            int var4 = var2 + 2;
            var1.append(var0, var2, var4);
            if (var2 == var3) {
               break;
            }

            var2 -= 2;
         }
      }

      String var10000 = var1.toString();
      Intrinsics.checkNotNullExpressionValue(var10000, "bigEndianMacs.toString()");
      return var10000;
   }

   @NotNull
   public static final List splitHexStringToInt(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "hexString");
      ArrayList var1;
      var1 = new ArrayList.<init>(var0.length());

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         var1.add(Integer.parseInt(String.valueOf(var0.charAt(var2)), 16));
      }

      return var1;
   }

   @NotNull
   public static final String formatMacAddress(@NotNull String var0) {
      Intrinsics.checkNotNullParameter(var0, "macAddress");
      if (var0.length() == 12) {
         List var10000 = StringsKt.chunked(var0, 2);
         <undefinedtype> var2 = null.a;
         return CollectionsKt.joinToString$default(var10000, ":", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, var2, 30, (Object)null);
      } else {
         throw new IllegalArgumentException("MAC address must be 12 characters long".toString());
      }
   }
}
