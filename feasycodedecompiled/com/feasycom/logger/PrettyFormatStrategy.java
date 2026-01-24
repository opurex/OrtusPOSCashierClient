package com.feasycom.logger;

import a.a.b.a;
import a.a.b.b;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PrettyFormatStrategy implements FormatStrategy {
   private static final int CHUNK_SIZE = 4000;
   private static final int MIN_STACK_OFFSET = 5;
   private static final char TOP_LEFT_CORNER = '┌';
   private static final char BOTTOM_LEFT_CORNER = '└';
   private static final char MIDDLE_CORNER = '├';
   private static final char HORIZONTAL_LINE = '│';
   private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
   private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
   private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
   private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
   private static final String MIDDLE_BORDER = "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
   private final int methodCount;
   private final int methodOffset;
   private final boolean showThreadInfo;
   @NonNull
   private final LogStrategy logStrategy;
   @Nullable
   private final String tag;

   private PrettyFormatStrategy(@NonNull Builder var1) {
      b.a((Object)var1);
      this.methodCount = var1.methodCount;
      this.methodOffset = var1.methodOffset;
      this.showThreadInfo = var1.showThreadInfo;
      this.logStrategy = var1.logStrategy;
      this.tag = var1.tag;
   }

   @NonNull
   public static Builder newBuilder() {
      return new Builder();
   }

   private void logTopBorder(int var1, @Nullable String var2) {
      this.logChunk(var1, var2, "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
   }

   private void logHeaderContent(int var1, @Nullable String var2, int var3) {
      StackTraceElement[] var4 = Thread.currentThread().getStackTrace();
      if (this.showThreadInfo) {
         this.logChunk(var1, var2, "│ Thread: " + Thread.currentThread().getName());
         this.logDivider(var1, var2);
      }

      String var5 = "";
      int var6;
      if (var3 + (var6 = this.getStackOffset(var4) + this.methodOffset) > var4.length) {
         var3 = var4.length - var6 - 1;
      }

      for(; var3 > 0; --var3) {
         int var7;
         if ((var7 = var3 + var6) < var4.length) {
            StringBuilder var10002 = new StringBuilder();
            var10002.append('│').append(' ').append(var5).append(this.getSimpleClassName(var4[var7].getClassName())).append(".").append(var4[var7].getMethodName()).append(" ").append(" (").append(var4[var7].getFileName()).append(":").append(var4[var7].getLineNumber()).append(")");
            var5 = var5 + "   ";
            this.logChunk(var1, var2, var10002.toString());
         }
      }

   }

   private void logBottomBorder(int var1, @Nullable String var2) {
      this.logChunk(var1, var2, "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
   }

   private void logDivider(int var1, @Nullable String var2) {
      this.logChunk(var1, var2, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
   }

   private void logContent(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      String[] var6;
      int var4 = (var6 = var3.split(System.getProperty("line.separator"))).length;

      for(int var5 = 0; var5 < var4; ++var5) {
         this.logChunk(var1, var2, "│ " + var6[var5]);
      }

   }

   private void logChunk(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      this.logStrategy.log(var1, var2, var3);
   }

   private String getSimpleClassName(@NonNull String var1) {
      b.a((Object)var1);
      return var1.substring(var1.lastIndexOf(".") + 1);
   }

   private int getStackOffset(@NonNull StackTraceElement[] var1) {
      b.a((Object)var1);

      for(int var3 = 5; var3 < var1.length; ++var3) {
         String var2;
         if (!(var2 = var1[var3].getClassName()).equals(a.class.getName()) && !var2.equals(Logger.class.getName())) {
            return var3 + -1;
         }
      }

      return -1;
   }

   @Nullable
   private String formatTag(@Nullable String var1) {
      return !b.a((CharSequence)var1) && !b.a(this.tag, var1) ? this.tag + "-" + var1 : this.tag;
   }

   // $FF: synthetic method
   public PrettyFormatStrategy(Builder var1, Object var2) {
      this(var1);
   }

   public void log(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      var2 = this.formatTag(var2);
      this.logTopBorder(var1, var2);
      int var4 = this.methodCount;
      this.logHeaderContent(var1, var2, var4);
      int var5;
      byte[] var9;
      if ((var5 = (var9 = var3.getBytes()).length) <= 4000) {
         if (this.methodCount > 0) {
            this.logDivider(var1, var2);
         }

         this.logContent(var1, var2, var3);
         this.logBottomBorder(var1, var2);
      } else {
         if (this.methodCount > 0) {
            this.logDivider(var1, var2);
         }

         for(int var8 = 0; var8 < var5; var8 += 4000) {
            int var6 = Math.min(var5 - var8, 4000);
            String var7;
            var7 = new String.<init>(var9, var8, var6);
            this.logContent(var1, var2, var7);
         }

         this.logBottomBorder(var1, var2);
      }
   }

   public static class Builder {
      public int methodCount;
      public int methodOffset;
      public boolean showThreadInfo;
      @Nullable
      public LogStrategy logStrategy;
      @Nullable
      public String tag;

      private Builder() {
         this.methodCount = 2;
         this.methodOffset = 0;
         this.showThreadInfo = true;
         this.tag = "PRETTY_LOGGER";
      }

      // $FF: synthetic method
      public Builder(Object var1) {
         this();
      }

      @NonNull
      public Builder methodCount(int var1) {
         this.methodCount = var1;
         return this;
      }

      @NonNull
      public Builder methodOffset(int var1) {
         this.methodOffset = var1;
         return this;
      }

      @NonNull
      public Builder showThreadInfo(boolean var1) {
         this.showThreadInfo = var1;
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
      public PrettyFormatStrategy build() {
         if (this.logStrategy == null) {
            LogcatLogStrategy var1;
            var1 = new LogcatLogStrategy.<init>();
            this.logStrategy = var1;
         }

         return new PrettyFormatStrategy(this);
      }
   }
}
