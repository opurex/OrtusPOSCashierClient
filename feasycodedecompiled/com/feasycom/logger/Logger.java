package com.feasycom.logger;

import a.a.b.a;
import a.a.b.b;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Logger {
   public static final int VERBOSE = 2;
   public static final int DEBUG = 3;
   public static final int INFO = 4;
   public static final int WARN = 5;
   public static final int ERROR = 6;
   public static final int ASSERT = 7;
   @NonNull
   private static Printer printer = new a();

   private Logger() {
   }

   public static void printer(@NonNull Printer var0) {
      printer = (Printer)b.a((Object)var0);
   }

   public static void addLogAdapter(@NonNull LogAdapter var0) {
      printer.addAdapter((LogAdapter)b.a((Object)var0));
   }

   public static void clearLogAdapters() {
      printer.clearLogAdapters();
   }

   public static Printer t(@Nullable String var0) {
      return printer.t(var0);
   }

   public static void log(int var0, @Nullable String var1, @Nullable String var2, @Nullable Throwable var3) {
      printer.log(var0, var1, var2, var3);
   }

   public static void d(@NonNull String var0, @Nullable Object... var1) {
      printer.d(var0, var1);
   }

   public static void d(@Nullable Object var0) {
      printer.d(var0);
   }

   public static void e(@NonNull String var0, @Nullable Object... var1) {
      printer.e((Throwable)null, var0, var1);
   }

   public static void e(@Nullable Throwable var0, @NonNull String var1, @Nullable Object... var2) {
      printer.e(var0, var1, var2);
   }

   public static void i(@NonNull String var0, @Nullable Object... var1) {
      printer.i(var0, var1);
   }

   public static void v(@NonNull String var0, @Nullable Object... var1) {
      printer.v(var0, var1);
   }

   public static void w(@NonNull String var0, @Nullable Object... var1) {
      printer.w(var0, var1);
   }

   public static void wtf(@NonNull String var0, @Nullable Object... var1) {
      printer.wtf(var0, var1);
   }

   public static void json(@Nullable String var0) {
      printer.json(var0);
   }

   public static void xml(@Nullable String var0) {
      printer.xml(var0);
   }
}
