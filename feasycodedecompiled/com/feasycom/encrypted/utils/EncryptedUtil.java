package com.feasycom.encrypted.utils;

import androidx.annotation.Keep;

public class EncryptedUtil {
   @Keep
   public static byte[] covAssicAndByte(String var0, String var1) {
      byte[] var3 = var0.getBytes();
      byte[] var2;
      byte[] var5;
      byte[] var10000 = var2 = new byte[(var5 = hexToByte(var1)).length + 4];
      System.arraycopy(var3, 0, var2, 0, 4);
      int var4 = var5.length;
      System.arraycopy(var5, 0, var2, 4, var4);
      return var10000;
   }

   @Keep
   public static byte[] hexToByte(String var0) {
      if (var0.length() <= 0) {
         return new byte[0];
      } else {
         if ((var0 = var0.replace(" ", "")).length() % 2 == 1) {
            StringBuffer var10000 = new StringBuffer(var0);
            var10000.insert(var0.length() - 1, '0');
            var0 = var10000.toString();
         }

         byte[] var1 = new byte[var0.length() / 2];

         int var3;
         for(int var2 = 0; var2 < var0.length(); var2 = var3) {
            var3 = var2 + 2;
            String var4 = var0.substring(var2, var3);
            if (var2 == 0) {
               var1[var2] = (byte)Integer.parseInt(var4, 16);
            } else {
               var2 /= 2;
               var1[var2] = (byte)Integer.parseInt(var4, 16);
            }
         }

         return var1;
      }
   }
}
