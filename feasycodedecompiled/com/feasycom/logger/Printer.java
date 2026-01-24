package com.feasycom.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Printer {
   void addAdapter(@NonNull LogAdapter var1);

   Printer t(@Nullable String var1);

   void d(@NonNull String var1, @Nullable Object... var2);

   void d(@Nullable Object var1);

   void e(@NonNull String var1, @Nullable Object... var2);

   void e(@Nullable Throwable var1, @NonNull String var2, @Nullable Object... var3);

   void w(@NonNull String var1, @Nullable Object... var2);

   void i(@NonNull String var1, @Nullable Object... var2);

   void v(@NonNull String var1, @Nullable Object... var2);

   void wtf(@NonNull String var1, @Nullable Object... var2);

   void json(@Nullable String var1);

   void xml(@Nullable String var1);

   void log(int var1, @Nullable String var2, @Nullable String var3, @Nullable Throwable var4);

   void clearLogAdapters();
}
