package com.feasycom.encrypted.bean;

import androidx.annotation.Keep;

@Keep
public class EncryptInfo {
   private static final String TAG = "EncryptInfo";
   public static final int ENCRYPT_WAY = 2;
   public static final String ENCRYPT_BEACON = "Beacon";
   public static final String ENCRYPT_UNIVERSAL = "Universal";
   private String mPassword = "";
   private String mRandomNumber = "";
   private String mEncryptAlgorithm = "";

   private EncryptInfo(String[] var1, String var2) {
      this.mPassword = var1[0];
      this.mRandomNumber = var1[1];
      this.mEncryptAlgorithm = var2;
   }

   private static native String[] gen(String var0, String var1);

   public static EncryptInfo create(String var0, String var1, String var2) {
      String[] var10000;
      String[] var4;
      try {
         var10000 = gen(var0, var1);
      } catch (Exception var3) {
         var3.printStackTrace();
         var10000 = var4 = new String[3];
         var10000[0] = " ";
         var10000[1] = " ";
         var10000[2] = " ";
         return new EncryptInfo(var4, var2);
      }

      var4 = var10000;
      return new EncryptInfo(var4, var2);
   }

   public static native EncryptInfo createRandom(String var0);

   static {
      System.loadLibrary("util");
   }

   public boolean isEncryptAlgorithmUniversal() {
      String var1;
      return (var1 = this.mEncryptAlgorithm) != null && var1.equals("Universal");
   }

   public boolean isEncryptAlgorithmBeacon() {
      String var1;
      return (var1 = this.mEncryptAlgorithm) != null && var1.equals("Beacon");
   }

   public String getEncryptAlgorithm() {
      return this.mEncryptAlgorithm;
   }

   public String getPassword() {
      return this.mPassword;
   }

   public String getRandomNumber() {
      return this.mRandomNumber.toUpperCase();
   }
}
