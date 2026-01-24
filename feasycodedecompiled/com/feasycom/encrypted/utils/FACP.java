package com.feasycom.encrypted.utils;

public class FACP {
   public static native byte cacChecksum(byte[] var0, int var1);

   static {
      System.loadLibrary("util");
   }
}
