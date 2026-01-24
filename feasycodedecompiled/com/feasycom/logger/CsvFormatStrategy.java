package com.feasycom.logger;

import a.a.b.b;
import android.os.Environment;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvFormatStrategy implements FormatStrategy {
   private static final String NEW_LINE = System.getProperty("line.separator");
   private static final String NEW_LINE_REPLACEMENT = " <br> ";
   private static final String SEPARATOR = ",";
   @NonNull
   private final Date date;
   @NonNull
   private final SimpleDateFormat dateFormat;
   @NonNull
   private final LogStrategy logStrategy;
   @Nullable
   private final String tag;

   private CsvFormatStrategy(@NonNull Builder var1) {
      b.a((Object)var1);
      this.date = var1.date;
      this.dateFormat = var1.dateFormat;
      this.logStrategy = var1.logStrategy;
      this.tag = var1.tag;
   }

   @NonNull
   public static Builder newBuilder() {
      return new Builder();
   }

   @Nullable
   private String formatTag(@Nullable String var1) {
      return !b.a((CharSequence)var1) && !b.a(this.tag, var1) ? this.tag + "-" + var1 : this.tag;
   }

   // $FF: synthetic method
   public CsvFormatStrategy(Builder var1, Object var2) {
      this(var1);
   }

   public void log(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      var2 = this.formatTag(var2);
      this.date.setTime(System.currentTimeMillis());
      StringBuilder var4;
      StringBuilder var10001 = var4 = new StringBuilder;
      var4.<init>();
      var4.append(Long.toString(this.date.getTime()));
      var4.append(",");
      var4.append(this.dateFormat.format(this.date));
      var4.append(",");
      var4.append(b.a(var1));
      var4.append(",");
      var10001.append(var2);
      String var5;
      if (var3.contains(var5 = NEW_LINE)) {
         var3 = var3.replaceAll(var5, " <br> ");
      }

      var4.append(",");
      var4.append(var3);
      var4.append(var5);
      this.logStrategy.log(var1, var2, var4.toString());
   }

   public static final class Builder {
      private static final int MAX_BYTES = 512000;
      public Date date;
      public SimpleDateFormat dateFormat;
      public LogStrategy logStrategy;
      public String tag;

      private Builder() {
         this.tag = "PRETTY_LOGGER";
      }

      // $FF: synthetic method
      public Builder(Object var1) {
         this();
      }

      @NonNull
      public Builder date(@Nullable Date var1) {
         this.date = var1;
         return this;
      }

      @NonNull
      public Builder dateFormat(@Nullable SimpleDateFormat var1) {
         this.dateFormat = var1;
         return this;
      }

      @NonNull
      public Builder logStrategy(@Nullable LogStrategy var1) {
         this.logStrategy = var1;
         return this;
      }

      @NonNull
      public Builder tag(@Nullable String var1) {
         this.tag = var1;
         return this;
      }

      @NonNull
      public CsvFormatStrategy build() {
         if (this.date == null) {
            Date var1;
            var1 = new Date.<init>();
            this.date = var1;
         }

         if (this.dateFormat == null) {
            SimpleDateFormat var4;
            var4 = new SimpleDateFormat.<init>("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
            this.dateFormat = var4;
         }

         if (this.logStrategy == null) {
            String var5 = Environment.getExternalStorageDirectory().getAbsolutePath();
            var5 = var5 + File.separatorChar + "logger";
            HandlerThread var2;
            HandlerThread var10001 = var2 = new HandlerThread;
            var10001.<init>("AndroidFileLogger." + var5);
            var10001.start();
            DiskLogStrategy.a var3;
            var3 = new DiskLogStrategy.a.<init>(var2.getLooper(), var5, 512000);
            this.logStrategy = new DiskLogStrategy(var3);
         }

         return new CsvFormatStrategy(this);
      }
   }
}
