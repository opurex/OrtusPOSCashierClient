package com.feasycom.spp.controler;

import androidx.annotation.Keep;
import com.feasycom.common.controler.FscApi;

@Keep
public interface FscSppCentralApi extends FscApi {
   boolean isEnabledSDP();

   void openSdpService(String var1);

   void closeSdpService();

   void connect(String var1);
}
