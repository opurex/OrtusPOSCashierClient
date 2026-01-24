package com.feasycom.network.api;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J%\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u0005H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\b"},
   d2 = {"Lcom/feasycom/network/api/ImageService;", "", "downImg", "Lokhttp3/ResponseBody;", "path", "", "fileName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "FeasyBlueLibrary_release"}
)
public interface ImageService {
   @Streaming
   @GET("https://image.feasycom.com/lanchImage/{parameter}/{fileName}")
   @Nullable
   Object downImg(@Path("parameter") @NotNull String var1, @Path("fileName") @NotNull String var2, @NotNull Continuation var3);

   @Metadata(
      mv = {1, 7, 1},
      k = 3,
      xi = 48
   )
   public static final class DefaultImpls {
      // $FF: synthetic method
      public static Object downImg$default(ImageService var0, String var1, String var2, Continuation var3, int var4, Object var5) {
         if (var5 == null) {
            if ((var4 & 1) != 0) {
               var1 = "beacon";
            }

            if ((var4 & 2) != 0) {
               var2 = "lanch.png";
            }

            return var0.downImg(var1, var2, var3);
         } else {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: downImg");
         }
      }
   }
}
