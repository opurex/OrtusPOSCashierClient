package a.a.b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;

public final class b {
   public static boolean a(CharSequence var0) {
      return var0 == null || var0.length() == 0;
   }

   public static boolean a(CharSequence var0, CharSequence var1) {
      if (var0 == var1) {
         return true;
      } else {
         int var2;
         if (var0 != null && var1 != null && (var2 = var0.length()) == var1.length()) {
            if (var0 instanceof String && var1 instanceof String) {
               return var0.equals(var1);
            } else {
               for(int var3 = 0; var3 < var2; ++var3) {
                  if (var0.charAt(var3) != var1.charAt(var3)) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   public static String a(Throwable var0) {
      if (var0 == null) {
         return "";
      } else {
         for(Throwable var1 = var0; var1 != null; var1 = var1.getCause()) {
            if (var1 instanceof UnknownHostException) {
               return "";
            }
         }

         StringWriter var2;
         StringWriter var10000 = var2 = new StringWriter;
         var10000.<init>();
         PrintWriter var10001 = new PrintWriter(var2);
         var0.printStackTrace(var10001);
         var10001.flush();
         return var10000.toString();
      }
   }

   public static String a(int var0) {
      switch (var0) {
         case 2:
            return "VERBOSE";
         case 3:
            return "DEBUG";
         case 4:
            return "INFO";
         case 5:
            return "WARN";
         case 6:
            return "ERROR";
         case 7:
            return "ASSERT";
         default:
            return "UNKNOWN";
      }
   }

   public static String b(Object var0) {
      if (var0 == null) {
         return "null";
      } else if (!var0.getClass().isArray()) {
         return var0.toString();
      } else if (var0 instanceof boolean[]) {
         return Arrays.toString((boolean[])var0);
      } else if (var0 instanceof byte[]) {
         return Arrays.toString((byte[])var0);
      } else if (var0 instanceof char[]) {
         return Arrays.toString((char[])var0);
      } else if (var0 instanceof short[]) {
         return Arrays.toString((short[])var0);
      } else if (var0 instanceof int[]) {
         return Arrays.toString((int[])var0);
      } else if (var0 instanceof long[]) {
         return Arrays.toString((long[])var0);
      } else if (var0 instanceof float[]) {
         return Arrays.toString((float[])var0);
      } else if (var0 instanceof double[]) {
         return Arrays.toString((double[])var0);
      } else {
         return var0 instanceof Object[] ? Arrays.deepToString((Object[])var0) : "Couldn't find a correct type for the object";
      }
   }

   @NonNull
   public static Object a(@Nullable Object var0) {
      var0.getClass();
      return var0;
   }
}
