package com.feasycom.ota.bean;

import androidx.annotation.Keep;

@Keep
public class DfuFileInfo {
   @Keep
   public int bootloader;
   @Keep
   public int versionStart;
   @Keep
   public int version_soft_end;
   @Keep
   public int type_model;
}
