package com.feasycom.ota.bean;

public class ReceivePacketData {
   private byte status;
   private int reqSize;
   private int offset;

   public byte getStatus() {
      return this.status;
   }

   public void setStatus(byte var1) {
      this.status = var1;
   }

   public int getReqSize() {
      return this.reqSize;
   }

   public void setReqSize(int var1) {
      this.reqSize = var1;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }
}
