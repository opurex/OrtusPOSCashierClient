package com.feasycom.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface LogAdapter {
   boolean isLoggable(int var1, @Nullable String var2);

   void log(int var1, @Nullable String var2, @NonNull String var3);
}
