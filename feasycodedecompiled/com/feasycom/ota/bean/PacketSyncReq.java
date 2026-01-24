package com.feasycom.ota.bean;

public class PacketSyncReq {
   public byte[] idInProgress = new byte[4];

   public byte[] getIdInProgress() {
      return this.idInProgress;
   }

   public void setIdInProgress(byte[] var1) {
      this.idInProgress = var1;
   }
}
