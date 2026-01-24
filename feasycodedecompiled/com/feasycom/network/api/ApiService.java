package com.feasycom.network.api;

import com.feasycom.network.bean.Parameter;
import com.feasycom.network.bean.ProtocolParams;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001b\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0006J%\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0003\u0010\u000b\u001a\u00020\fH§@ø\u0001\u0000¢\u0006\u0002\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"},
   d2 = {"Lcom/feasycom/network/api/ApiService;", "", "getLanch", "Lcom/feasycom/network/bean/Splash;", "parameter", "Lcom/feasycom/network/bean/Parameter;", "(Lcom/feasycom/network/bean/Parameter;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProtocol", "Lcom/feasycom/network/bean/ProtocolResponse;", "protocolParams", "Lcom/feasycom/network/bean/ProtocolParams;", "local", "", "(Lcom/feasycom/network/bean/ProtocolParams;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "FeasyBlueLibrary_release"}
)
public interface ApiService {
   @POST("agreement")
   @Nullable
   Object getProtocol(@Body @NotNull ProtocolParams var1, @Header("local") @NotNull String var2, @NotNull Continuation var3);

   @Streaming
   @POST("lanch")
   @Nullable
   Object getLanch(@Body @NotNull Parameter var1, @NotNull Continuation var2);

   @Metadata(
      mv = {1, 7, 1},
      k = 3,
      xi = 48
   )
   public static final class DefaultImpls {
      // $FF: synthetic method
      public static Object getProtocol$default(ApiService var0, ProtocolParams var1, String var2, Continuation var3, int var4, Object var5) {
         if (var5 == null) {
            if ((var4 & 2) != 0) {
               var2 = "en";
            }

            return var0.getProtocol(var1, var2, var3);
         } else {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getProtocol");
         }
      }
   }
}
