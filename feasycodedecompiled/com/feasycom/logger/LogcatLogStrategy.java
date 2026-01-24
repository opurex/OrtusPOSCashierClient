package com.feasycom.logger;

import a.a.b.b;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LogcatLogStrategy implements LogStrategy {
   public static final String DEFAULT_TAG = "NO_TAG";

   public void log(int var1, @Nullable String var2, @NonNull String var3) {
      b.a((Object)var3);
      if (var2 == null) {
         var2 = "NO_TAG";
      }

      Log.println(var1, var2, var3);
   }
}
