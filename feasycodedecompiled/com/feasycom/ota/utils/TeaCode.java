package com.feasycom.ota.utils;

import androidx.annotation.Keep;
import com.feasycom.ota.bean.DfuFileInfo;

@Keep
public class TeaCode {
   static {
      System.loadLibrary("ota");
   }

   public native byte[] encrypt_bitstream(byte[] var1);

   public native byte[] decrypt_bitstream(byte[] var1);

   public native byte[] feasycom_decryption(byte[] var1);

   public native DfuFileInfo getDfuFileInformation(byte[] var1);
}
