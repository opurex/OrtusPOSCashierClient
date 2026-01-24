package com.feasycom.common.controler;

import androidx.annotation.Keep;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Keep
public interface FscApi {
   void isShowLog(boolean var1);

   boolean initialize();

   boolean isEnabled();

   boolean clearDevice(String var1);

   void connectToModify(String var1);

   void connectToOTAWithFactory(String var1, byte[] var2, boolean var3);

   void connectToVerifyOTAWithFactory(String var1, byte[] var2, boolean var3, boolean var4);

   void disconnect();

   void disconnect(String var1);

   List getBondDevices();

   void startScan();

   void stopScan();

   boolean isConnected();

   boolean isConnected(String var1);

   void setSendInterval(String var1, Long var2);

   boolean send(String var1);

   boolean send(String var1, String var2);

   boolean send(byte[] var1);

   boolean send(String var1, byte[] var2);

   boolean sendFile(int var1);

   boolean sendFile(String var1, int var2);

   boolean sendFile(byte[] var1);

   boolean sendFile(String var1, byte[] var2);

   boolean sendFile(InputStream var1);

   boolean sendFile(String var1, InputStream var2);

   void sendATCommand(Set var1);

   void sendATCommand(String var1, Set var2);

   void stopSend();

   boolean pauseSend(String var1);

   boolean continueSend(String var1);

   void stopSend(String var1);

   void setCallbacks(Object var1);
}
