package com.feasycom.ota.error;

import androidx.annotation.Keep;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005¨\u0006\u0006"},
   d2 = {"Lcom/feasycom/ota/error/SendingCanceledException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "message", "", "(Ljava/lang/String;)V", "FeasyBlueLibrary_release"}
)
@Keep
public final class SendingCanceledException extends Exception {
   public SendingCanceledException(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "message");
      super(var1);
   }
}
