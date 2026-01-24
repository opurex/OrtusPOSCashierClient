package com.feasycom.ota.utils;

import androidx.annotation.Keep;
import com.feasycom.ota.bean.DfuFileInfo;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005¢\u0006\u0002\u0010\u0002J\f\u0010\u0003\u001a\u00020\u0004*\u00020\u0004H\u0007¨\u0006\u0006"},
   d2 = {"Lcom/feasycom/ota/utils/OtaUtils;", "", "()V", "hexToByte", "", "Companion", "FeasyBlueLibrary_release"}
)
@Keep
public final class OtaUtils {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   @NotNull
   private static final Map moduleNameInfo = MapsKt.mapOf(new Pair[]{TuplesKt.to(1, "BT401"), TuplesKt.to(2, "BT405"), TuplesKt.to(3, "BT426N"), TuplesKt.to(4, "BT501"), TuplesKt.to(5, "BT502"), TuplesKt.to(6, "BT522"), TuplesKt.to(7, "BT616"), TuplesKt.to(8, "BT625"), TuplesKt.to(9, "BT626"), TuplesKt.to(10, "BT803"), TuplesKt.to(11, "BT813D"), TuplesKt.to(12, "BT816S"), TuplesKt.to(13, "BT821"), TuplesKt.to(14, "BT822"), TuplesKt.to(15, "BT826"), TuplesKt.to(16, "BT826N"), TuplesKt.to(17, "BT836"), TuplesKt.to(18, "BT836N"), TuplesKt.to(19, "BT906"), TuplesKt.to(20, "BT909"), TuplesKt.to(22, "BT816S3"), TuplesKt.to(23, "BT926"), TuplesKt.to(24, "BT901"), TuplesKt.to(32, "BT826H"), TuplesKt.to(33, "BT826NH"), TuplesKt.to(34, "BT826E"), TuplesKt.to(35, "BT826EH"), TuplesKt.to(36, "BT836B"), TuplesKt.to(37, "BT826B"), TuplesKt.to(38, "BT736"), TuplesKt.to(39, "BT926B"), TuplesKt.to(40, "BT966"), TuplesKt.to(41, "BT721B"), TuplesKt.to(42, "BT886"), TuplesKt.to(47, "BT826A"), TuplesKt.to(48, "BT826BA"), TuplesKt.to(49, "BT986"), TuplesKt.to(50, "BT826F"), TuplesKt.to(51, "BT946"), TuplesKt.to(53, "BT618"), TuplesKt.to(55, "BT671B-BG21"), TuplesKt.to(57, "BT986-1"), TuplesKt.to(58, "BT826F-1"), TuplesKt.to(60, "BT946-1")});
   @NotNull
   private static final int[] reconnectModule = new int[]{12, 17, 22, 36, 38};

   @Keep
   @NotNull
   public final String hexToByte(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "<this>");
      if (var1.length() <= 0) {
         return " ";
      } else {
         String var5;
         if ((var5 = StringsKt.replace$default(var1, " ", "", false, 4, (Object)null)).length() % 2 == 1) {
            StringBuffer var10000 = new StringBuffer(var5);
            var10000.insert(var5.length() - 1, '0');
            Intrinsics.checkNotNullExpressionValue(var5 = var10000.toString(), "strBuf.toString()");
         }

         byte[] var6 = new byte[var5.length() / 2];

         int var3;
         for(int var2 = 0; var2 < var5.length(); var2 = var3) {
            var3 = var2 + 2;
            String var4;
            Intrinsics.checkNotNullExpressionValue(var4 = var5.substring(var2, var3), "this as java.lang.String…ing(startIndex, endIndex)");
            if (var2 == 0) {
               var6[var2] = (byte)Integer.parseInt(var4, CharsKt.checkRadix(16));
            } else {
               var2 /= 2;
               var6[var2] = (byte)Integer.parseInt(var4, CharsKt.checkRadix(16));
            }
         }

         return new String(var6, Charsets.ISO_8859_1);
      }
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\r\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\fH\u0007J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0005H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0005H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\fH\u0007R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"},
      d2 = {"Lcom/feasycom/ota/utils/OtaUtils$Companion;", "", "()V", "moduleNameInfo", "", "", "", "reconnectModule", "", "checkDfuFile", "Lcom/feasycom/ota/bean/DfuFileInfo;", "dfuFile", "", "getDfuFileInformation", "data", "getModelName", "moduleNumber", "isReconnect", "", "moduleInfo", "needsReconnect", "dfuData", "FeasyBlueLibrary_release"}
   )
   public static final class Companion {
      private Companion() {
      }

      private final boolean isReconnect(int var1) {
         return ArraysKt.contains(OtaUtils.reconnectModule, var1);
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      @Keep
      public final boolean needsReconnect(@Nullable byte[] var1) {
         return this.isReconnect(this.getDfuFileInformation(var1).type_model);
      }

      @Keep
      @NotNull
      public final DfuFileInfo getDfuFileInformation(@Nullable byte[] var1) {
         DfuFileInfo var10000 = (new TeaCode()).getDfuFileInformation(var1);
         Intrinsics.checkNotNullExpressionValue(var10000, "TeaCode().getDfuFileInformation(data)");
         return var10000;
      }

      @Keep
      @NotNull
      public final String getModelName(int var1) {
         String var2;
         if ((var2 = (String)OtaUtils.moduleNameInfo.get(var1)) == null) {
            var2 = "Unknown";
         }

         return var2;
      }

      @Keep
      @Nullable
      public final DfuFileInfo checkDfuFile(@NotNull byte[] var1) {
         Intrinsics.checkNotNullParameter(var1, "dfuFile");
         return (new TeaCode()).getDfuFileInformation(var1);
      }
   }
}
