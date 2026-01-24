package com.feasycom.common.controler;

public interface FscCentralCallbacks {
   default void startScan() {
   }

   default void stopScan() {
   }

   default void packetReceived(String var1, String var2, String var3, byte[] var4) {
   }

   default void packetSend(String var1, String var2, byte[] var3) {
   }

   default void sendDataStatus(int var1) {
   }

   default void sendPacketProgress(String var1, int var2, byte[] var3) {
   }

   default void otaProgressUpdate(String var1, float var2, int var3) {
   }

   default void atCommandCallBack(String var1, String var2, int var3, int var4) {
   }

   default void endATCommand() {
   }

   default void startATCommand() {
   }
}
