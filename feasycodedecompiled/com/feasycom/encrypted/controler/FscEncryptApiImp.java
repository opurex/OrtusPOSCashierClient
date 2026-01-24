package com.feasycom.encrypted.controler;

import androidx.annotation.Keep;
import com.feasycom.encrypted.bean.EncryptAlgorithm;
import com.feasycom.encrypted.bean.EncryptInfo;
import com.feasycom.encrypted.utils.EncryptedUtil;

public class FscEncryptApiImp implements FscEncryptApi {
   private static final String TAG = "FeasycomApiImp";
   private static FscEncryptApi instance;
   private EncryptInfo mEncryptInfo;

   @Keep
   public static synchronized FscEncryptApi getInstance() {
      if (instance == null) {
         instance = new FscEncryptApiImp();
      }

      return instance;
   }

   private static String toHexString(byte[] var0) {
      StringBuilder var1;
      var1 = new StringBuilder.<init>(var0.length);
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         byte var4 = var0[var3];
         Object[] var5;
         (var5 = new Object[1])[0] = var4;
         var1.append(String.format("%02x ", var5).toUpperCase());
      }

      return var1.toString();
   }

   private FscEncryptApiImp() {
   }

   static {
      System.loadLibrary("util");
   }

   public byte[] getEncrypted() {
      return EncryptedUtil.covAssicAndByte("AUTH", (this.mEncryptInfo = EncryptInfo.createRandom("Universal")).getPassword());
   }

   public byte[] getBeaconEncrypted(String var1, String var2) {
      return EncryptedUtil.covAssicAndByte("AUTH", (this.mEncryptInfo = EncryptInfo.create(var1, var2, "Beacon")).getPassword());
   }

   public boolean verifyEncrypted(byte[] var1) {
      String var2 = toHexString(var1).replace(" ", "").toUpperCase();
      if (this.mEncryptInfo.isEncryptAlgorithmUniversal()) {
         return EncryptAlgorithm.Universal.randomNumberMatches(this.mEncryptInfo, var2);
      } else {
         return this.mEncryptInfo.isEncryptAlgorithmBeacon() ? EncryptAlgorithm.Beacon.randomNumberMatches(this.mEncryptInfo, var2) : false;
      }
   }
}
