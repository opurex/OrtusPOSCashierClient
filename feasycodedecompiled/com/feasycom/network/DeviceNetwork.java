package com.feasycom.network;

import com.feasycom.network.api.ApiService;
import com.feasycom.network.api.DeviceService;
import com.feasycom.network.api.DfuService;
import com.feasycom.network.api.ImageService;
import com.feasycom.network.bean.DeviceParameter;
import com.feasycom.network.bean.Parameter;
import com.feasycom.network.bean.ProtocolParams;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u000eH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u001dJ#\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020\u000eH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010#R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006$"},
   d2 = {"Lcom/feasycom/network/DeviceNetwork;", "", "()V", "apiService", "Lcom/feasycom/network/api/ApiService;", "deviceService", "Lcom/feasycom/network/api/DeviceService;", "dfuService", "Lcom/feasycom/network/api/DfuService;", "imageService", "Lcom/feasycom/network/api/ImageService;", "downImg", "Lokhttp3/ResponseBody;", "path", "", "fileName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "downloadDFU", "name", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllDevice", "Lcom/feasycom/network/bean/Devices;", "deviceInfo", "Lcom/feasycom/network/bean/DeviceParameter;", "(Lcom/feasycom/network/bean/DeviceParameter;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLanch", "Lcom/feasycom/network/bean/Splash;", "parameter", "Lcom/feasycom/network/bean/Parameter;", "(Lcom/feasycom/network/bean/Parameter;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProtocol", "Lcom/feasycom/network/bean/ProtocolResponse;", "protocolParams", "Lcom/feasycom/network/bean/ProtocolParams;", "local", "(Lcom/feasycom/network/bean/ProtocolParams;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "FeasyBlueLibrary_release"}
)
public final class DeviceNetwork {
   @NotNull
   public static final DeviceNetwork INSTANCE = new DeviceNetwork();
   @NotNull
   private static DfuService dfuService;
   @NotNull
   private static ImageService imageService;
   @NotNull
   private static ApiService apiService;
   @NotNull
   private static DeviceService deviceService;

   private DeviceNetwork() {
   }

   // $FF: synthetic method
   public static Object downImg$default(DeviceNetwork var0, String var1, String var2, Continuation var3, int var4, Object var5) {
      if ((var4 & 1) != 0) {
         var1 = "beacon";
      }

      if ((var4 & 2) != 0) {
         var2 = "lanch.png";
      }

      return var0.downImg(var1, var2, var3);
   }

   // $FF: synthetic method
   public static Object getProtocol$default(DeviceNetwork var0, ProtocolParams var1, String var2, Continuation var3, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = "en";
      }

      return var0.getProtocol(var1, var2, var3);
   }

   static {
      RetrofitClient var10000 = RetrofitClient.INSTANCE;
      dfuService = (DfuService)var10000.create(DfuService.class);
      imageService = (ImageService)var10000.create(ImageService.class);
      apiService = (ApiService)var10000.create(ApiService.class);
      deviceService = (DeviceService)var10000.create(DeviceService.class);
   }

   @Nullable
   public final Object downloadDFU(@NotNull String var1, @NotNull Continuation var2) {
      return dfuService.downloadDFU(var1, var2);
   }

   @Nullable
   public final Object downImg(@NotNull String var1, @NotNull String var2, @NotNull Continuation var3) {
      return imageService.downImg(var1, var2, var3);
   }

   @Nullable
   public final Object getLanch(@NotNull Parameter var1, @NotNull Continuation var2) {
      return apiService.getLanch(var1, var2);
   }

   @Nullable
   public final Object getProtocol(@NotNull ProtocolParams var1, @NotNull String var2, @NotNull Continuation var3) {
      return apiService.getProtocol(var1, var2, var3);
   }

   @Nullable
   public final Object getAllDevice(@NotNull DeviceParameter var1, @NotNull Continuation var2) {
      return deviceService.getAllDevice(var1, var2);
   }
}
