package com.feasycom.ble.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swallowsonny.convertextlibrary.ByteArrayExtKt;
import com.swallowsonny.convertextlibrary.StringExtKt;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 2,
   xi = 48,
   d1 = {"\u0000T\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001a\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0004j\b\u0012\u0004\u0012\u00020\u0001`\u00052\u0006\u0010\u0006\u001a\u00020\u0007\u001a&\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00072\u0016\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0004j\b\u0012\u0004\u0012\u00020\u0001`\u0005\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u000e\u001a\u00020\f\u001a\u001c\u0010\u000f\u001a\u00020\u0010*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u001a\u001c\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u000e\u001a\u00020\u0013\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u000e\u001a\u00020\u0015\u001a\u001c\u0010\u0016\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u000e\u001a\u00020\u0001\u001a:\u0010\u0017\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0018j\b\u0012\u0004\u0012\u00020\u0001`\u0019*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0016\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0018j\b\u0012\u0004\u0012\u00020\u0001`\u0019\u001a\u0014\u0010\u001b\u001a\n \u001d*\u0004\u0018\u00010\u001c0\u001c*\u00020\u0007H\u0002\u001a\u001a\u0010\u001e\u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\f\u001a\u001a\u0010\u001f\u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0010\u001a\u001a\u0010 \u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0013\u001a\u001a\u0010!\u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0015\u001a\u001a\u0010\"\u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0001\u001a*\u0010#\u001a\u00020\t*\u00020\u00072\u0006\u0010\r\u001a\u00020\u00012\u0016\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0018j\b\u0012\u0004\u0012\u00020\u0001`\u0019\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006$"},
   d2 = {"FILE_NAME", "", "KEY_ORDERED_STRING_SET", "getOrderedStringSet", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "context", "Landroid/content/Context;", "saveOrderedStringSet", "", "orderedStringSet", "getBoolean", "", "key", "default", "getByteArray", "", "value", "getInt", "", "getLong", "", "getStr", "getStrSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "data", "preference", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "putBoolean", "putByteArray", "putInt", "putLong", "putStr", "putStrSet", "FeasyBlueLibrary_release"}
)
public final class PreferenceUtilKt {
   @NotNull
   private static final String FILE_NAME = "feasycom";
   @NotNull
   private static final String KEY_ORDERED_STRING_SET = "OrderedStringSet";

   private static final SharedPreferences preference(Context var0) {
      return var0.getSharedPreferences("feasycom", 0);
   }

   public static final boolean getBoolean(@NotNull Context var0, @NotNull String var1, boolean var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      return preference(var0).getBoolean(var1, var2);
   }

   // $FF: synthetic method
   public static boolean getBoolean$default(Context var0, String var1, boolean var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = false;
      }

      return getBoolean(var0, var1, var2);
   }

   @NotNull
   public static final String getStr(@NotNull Context var0, @NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "default");
      String var3;
      if ((var3 = preference(var0).getString(var1, var2)) == null) {
         var3 = var2;
      }

      return var3;
   }

   // $FF: synthetic method
   public static String getStr$default(Context var0, String var1, String var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = "";
      }

      return getStr(var0, var1, var2);
   }

   public static final int getInt(@NotNull Context var0, @NotNull String var1, int var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      return preference(var0).getInt(var1, var2);
   }

   // $FF: synthetic method
   public static int getInt$default(Context var0, String var1, int var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = 0;
      }

      return getInt(var0, var1, var2);
   }

   public static final long getLong(@NotNull Context var0, @NotNull String var1, long var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      return preference(var0).getLong(var1, var2);
   }

   // $FF: synthetic method
   public static long getLong$default(Context var0, String var1, long var2, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = 0L;
      }

      return getLong(var0, var1, var2);
   }

   @NotNull
   public static final HashSet getStrSet(@NotNull Context var0, @NotNull String var1, @NotNull HashSet var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "data");
      Set var10000 = preference(var0).getStringSet(var1, var2);
      Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type java.util.HashSet<kotlin.String>{ kotlin.collections.TypeAliasesKt.HashSet<kotlin.String> }");
      return (HashSet)var10000;
   }

   public static final void putBoolean(@NotNull Context var0, @NotNull String var1, boolean var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      var3.putBoolean(var1, var2);
      var3.apply();
   }

   public static final void putStr(@NotNull Context var0, @NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "value");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      var3.putString(var1, var2);
      var3.apply();
   }

   public static final void putInt(@NotNull Context var0, @NotNull String var1, int var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      var3.putInt(var1, var2);
      var3.apply();
   }

   public static final void putLong(@NotNull Context var0, @NotNull String var1, long var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var4 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var4, "editor");
      var4.putLong(var1, var2);
      var4.apply();
   }

   public static final void putStrSet(@NotNull Context var0, @NotNull String var1, @NotNull HashSet var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "value");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      var3.putStringSet(var1, var2);
      var3.apply();
   }

   public static final void putByteArray(@NotNull Context var0, @NotNull String var1, @NotNull byte[] var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "value");
      SharedPreferences var10000 = preference(var0);
      Intrinsics.checkNotNullExpressionValue(var10000, "preference()");
      SharedPreferences.Editor var3;
      SharedPreferences.Editor var4 = var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      putStr(var0, var1, ByteArrayExtKt.toHexString(var2, true));
      var4.apply();
   }

   public static final void saveOrderedStringSet(@NotNull Context var0, @NotNull LinkedHashSet var1) {
      Intrinsics.checkNotNullParameter(var0, "context");
      Intrinsics.checkNotNullParameter(var1, "orderedStringSet");
      SharedPreferences var10000 = var0.getSharedPreferences("feasycom", 0);
      Intrinsics.checkNotNullExpressionValue(var10000, "context.getSharedPrefere…ME, Context.MODE_PRIVATE)");
      String var2 = (new Gson()).toJson(var1);
      SharedPreferences.Editor var3 = var10000.edit();
      Intrinsics.checkExpressionValueIsNotNull(var3, "editor");
      var3.putString("OrderedStringSet", var2);
      var3.apply();
   }

   @NotNull
   public static final LinkedHashSet getOrderedStringSet(@NotNull Context var0) {
      Intrinsics.checkNotNullParameter(var0, "context");
      SharedPreferences var10000 = var0.getSharedPreferences("feasycom", 0);
      Intrinsics.checkNotNullExpressionValue(var10000, "context.getSharedPrefere…ME, Context.MODE_PRIVATE)");
      String var2;
      LinkedHashSet var4;
      if ((var2 = var10000.getString("OrderedStringSet", (String)null)) != null) {
         Type var1 = (new TypeToken() {
         }).getType();
         Object var3 = (new Gson()).fromJson(var2, var1);
         Intrinsics.checkNotNullExpressionValue(var3, "{\n        val type = obj…romJson(json, type)\n    }");
         var4 = (LinkedHashSet)var3;
      } else {
         var4 = new LinkedHashSet.<init>();
      }

      return var4;
   }

   @NotNull
   public static final byte[] getByteArray(@NotNull Context var0, @NotNull String var1, @NotNull byte[] var2) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "key");
      Intrinsics.checkNotNullParameter(var2, "value");
      String var4;
      if ((var4 = preference(var0).getString(var1, "")) != null && var4.length() != 0) {
         byte[] var10000;
         try {
            Intrinsics.checkNotNull(var4);
            var10000 = StringExtKt.hex2ByteArray(var4);
         } catch (Exception var3) {
            return var2;
         }

         var2 = var10000;
      }

      return var2;
   }

   // $FF: synthetic method
   public static byte[] getByteArray$default(Context var0, String var1, byte[] var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = new byte[0];
      }

      return getByteArray(var0, var1, var2);
   }
}
