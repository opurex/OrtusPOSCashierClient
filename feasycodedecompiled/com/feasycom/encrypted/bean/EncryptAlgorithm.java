package com.feasycom.encrypted.bean;

public class EncryptAlgorithm {
   public static class Beacon {
      public static final String NAME = "Beacon";

      public static native String parseRandomNumber(String var0);

      public static native boolean randomNumberMatches(EncryptInfo var0, String var1);
   }

   public static class Universal {
      public static final String NAME = "Universal";

      public static native String parseRandomNumber(String var0);

      public static native boolean randomNumberMatches(EncryptInfo var0, String var1);
   }
}
