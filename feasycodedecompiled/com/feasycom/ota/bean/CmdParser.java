package com.feasycom.ota.bean;

public class CmdParser {
   private CmdStateT cmdStateT;
   private byte[] buf = new byte[256];
   private int bufSize;

   public CmdStateT getCmdStateT() {
      return this.cmdStateT;
   }

   public void setCmdStateT(CmdStateT var1) {
      this.cmdStateT = var1;
   }

   public byte[] getBuf() {
      return this.buf;
   }

   public void setBuf(byte[] var1) {
      this.buf = var1;
   }

   public int getBufSize() {
      return this.bufSize;
   }

   public void setBufSize(int var1) {
      this.bufSize = var1;
   }
}
