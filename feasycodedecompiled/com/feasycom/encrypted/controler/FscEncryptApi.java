package com.feasycom.encrypted.controler;

public interface FscEncryptApi {
   byte[] getEncrypted();

   byte[] getBeaconEncrypted(String var1, String var2);

   boolean verifyEncrypted(byte[] var1);
}
