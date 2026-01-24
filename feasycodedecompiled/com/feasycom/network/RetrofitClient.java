package com.feasycom.network;

import com.feasycom.common.utils.MsgLogger;
import com.feasycom.network.utils.SSLUtil;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\f\u001a\u0002H\r\"\u0006\b\u0000\u0010\r\u0018\u0001H\u0086\b¢\u0006\u0002\u0010\u000eJ\u001f\u0010\f\u001a\u0002H\r\"\u0004\b\u0000\u0010\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\r0\u0010¢\u0006\u0002\u0010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"},
   d2 = {"Lcom/feasycom/network/RetrofitClient;", "", "()V", "BASE_URL", "", "httpClient", "Lokhttp3/OkHttpClient;", "loggingInterceptor", "Lokhttp3/logging/HttpLoggingInterceptor;", "retrofit", "Lretrofit2/Retrofit;", "kotlin.jvm.PlatformType", "create", "T", "()Ljava/lang/Object;", "serviceClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "FeasyBlueLibrary_release"}
)
public final class RetrofitClient {
   @NotNull
   public static final RetrofitClient INSTANCE = new RetrofitClient();
   @NotNull
   private static final String BASE_URL = "https://api.feasycom.com/";
   @NotNull
   private static final HttpLoggingInterceptor loggingInterceptor;
   @NotNull
   private static final OkHttpClient httpClient;
   private static final Retrofit retrofit;

   private RetrofitClient() {
   }

   private static final void loggingInterceptor$lambda_0/* $FF was: loggingInterceptor$lambda-0*/(String var0) {
      Intrinsics.checkNotNullParameter(var0, "message");
      MsgLogger.d("Http => " + var0);
   }

   static {
      HttpLoggingInterceptor var0;
      HttpLoggingInterceptor var10000 = var0 = new HttpLoggingInterceptor;
      var10000.<init>(RetrofitClient::loggingInterceptor$lambda-0);
      loggingInterceptor = var10000;
      OkHttpClient.Builder var2 = new OkHttpClient.Builder();
      SSLSocketFactory var10001 = SSLUtil.getSSLSocketFactory();
      Intrinsics.checkNotNullExpressionValue(var10001, "getSSLSocketFactory()");
      X509TrustManager var1;
      var1 = new X509TrustManager() {
         public void checkClientTrusted(@Nullable X509Certificate[] var1, @Nullable String var2) {
         }

         public void checkServerTrusted(@Nullable X509Certificate[] var1, @Nullable String var2) {
         }

         @Nullable
         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      }.<init>();
      var2 = var2.sslSocketFactory(var10001, var1);
      HostnameVerifier var5 = SSLUtil.getHostnameVerifier();
      Intrinsics.checkNotNullExpressionValue(var5, "getHostnameVerifier()");
      var2 = var2.hostnameVerifier(var5).addNetworkInterceptor(var0.setLevel(Level.BODY));
      TimeUnit var3 = TimeUnit.SECONDS;
      OkHttpClient var4;
      httpClient = var4 = var2.connectTimeout(30L, var3).readTimeout(30L, var3).build();
      retrofit = (new Retrofit.Builder()).baseUrl("https://api.feasycom.com/").client(var4).addConverterFactory(GsonConverterFactory.create()).build();
   }

   public final Object create(@NotNull Class var1) {
      Intrinsics.checkNotNullParameter(var1, "serviceClass");
      return retrofit.create(var1);
   }

   // $FF: synthetic method
   public final Object create() {
      Intrinsics.reifiedOperationMarker(4, "T");
      return this.create(Object.class);
   }
}
