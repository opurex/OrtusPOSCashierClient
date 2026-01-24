package com.feasycom.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface FormatStrategy {
   void log(int var1, @Nullable String var2, @NonNull String var3);
}
