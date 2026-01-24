package com.feasycom.network.utils;

import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLUtil {
   public static SSLSocketFactory getSSLSocketFactory() {
      // $FF: Couldn't be decompiled
   }

   private static TrustManager[] getTrustManager() {
      TrustManager[] var10000 = new TrustManager[1];
      X509TrustManager var0;
      var0 = new X509TrustManager() {
         public void checkClientTrusted(X509Certificate[] var1, String var2) {
         }

         public void checkServerTrusted(X509Certificate[] var1, String var2) {
         }

         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      }.<init>();
      var10000[0] = var0;
      return var10000;
   }

   public static HostnameVerifier getHostnameVerifier() {
      return new HostnameVerifier() {
         public boolean verify(String var1, SSLSession var2) {
            return true;
         }
      };
   }
}
