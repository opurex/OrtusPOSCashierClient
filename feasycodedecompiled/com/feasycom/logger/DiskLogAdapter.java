package com.feasycom.logger;

import a.a.b.b;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiskLogAdapter implements LogAdapter {
   @NonNull
   private final FormatStrategy formatStrategy;

   public DiskLogAdapter() {
      this.formatStrategy = CsvFormatStrategy.newBuilder().build();
   }

   public DiskLogAdapter(@NonNull FormatStrategy var1) {
      this.formatStrategy = (FormatStrategy)b.a((Object)var1);
   }

   public boolean isLoggable(int var1, @Nullable String var2) {
      return true;
   }

   public void log(int var1, @Nullable String var2, @NonNull String var3) {
      this.formatStrategy.log(var1, var2, var3);
   }
}
