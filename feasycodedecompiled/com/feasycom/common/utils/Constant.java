package com.feasycom.common.utils;

import androidx.annotation.Keep;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b*\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u00020\u00068\u0006X\u0087T¢\u0006\u0002\n\u0000¨\u00060"},
   d2 = {"Lcom/feasycom/common/utils/Constant;", "", "()V", "BLE_MODE", "", "CHARACTERISTIC_WRITE", "", "CHARACTERISTIC_WRITE_NO_RESPONSE", "COMMAND_ADVIN", "COMMAND_BADVDATA", "COMMAND_BEGIN", "COMMAND_BUZ", "COMMAND_BWMODE", "COMMAND_BWMODE_CLOSE", "COMMAND_BWMODE_OPEN", "COMMAND_END", "COMMAND_EXTEND", "COMMAND_FAILED", "COMMAND_GSCFG", "COMMAND_HEADER", "COMMAND_KEYCFG", "COMMAND_LED", "COMMAND_LENAME", "COMMAND_MODEL", "COMMAND_NAME", "COMMAND_OTA", "COMMAND_PIN", "COMMAND_RAP", "COMMAND_STATE_QUERY", "COMMAND_STATE_SET", "COMMAND_SUCCESSFUL", "COMMAND_TIME_OUT", "COMMAND_TX_POWER", "COMMAND_VERSION", "COMPLETE_LOCAL_NAME", "DISABLE_CHARACTERISTIC_INDICATE", "DISABLE_CHARACTERISTIC_NOTIFICATION", "ENABLE_CHARACTERISTIC_INDICATE", "ENABLE_CHARACTERISTIC_NOTIFICATION", "FIFO_SEND_FINISH", "FLAG", "INCOMPLETE_SERVICE_UUIDS_128BIT", "INCOMPLETE_SERVICE_UUIDS_16BIT", "MANUFACTURER_SPECIFIC_DATA", "PACKAGE_SEND_FINISH", "SERVICE_DATA", "SPP_MODE", "TX_POWER_LEVEL", "FeasyBlueLibrary_release"}
)
@Keep
public final class Constant {
   @NotNull
   public static final Constant INSTANCE = new Constant();
   @Keep
   public static final int PACKAGE_SEND_FINISH = 100;
   @Keep
   public static final int FIFO_SEND_FINISH = 101;
   @Keep
   public static final int CHARACTERISTIC_WRITE_NO_RESPONSE = 1;
   @Keep
   public static final int CHARACTERISTIC_WRITE = 2;
   @Keep
   public static final int ENABLE_CHARACTERISTIC_NOTIFICATION = 3;
   @Keep
   public static final int DISABLE_CHARACTERISTIC_NOTIFICATION = 4;
   @Keep
   public static final int ENABLE_CHARACTERISTIC_INDICATE = 5;
   @Keep
   public static final int DISABLE_CHARACTERISTIC_INDICATE = 6;
   @Keep
   @NotNull
   public static final String COMMAND_HEADER = "AT+";
   @Keep
   @NotNull
   public static final String COMMAND_BEGIN = "Opened";
   @Keep
   @NotNull
   public static final String COMMAND_MODEL = "MODEL";
   @Keep
   @NotNull
   public static final String COMMAND_VERSION = "VERSION";
   @Keep
   @NotNull
   public static final String COMMAND_NAME = "NAME";
   @Keep
   @NotNull
   public static final String COMMAND_LENAME = "LENAME";
   @Keep
   @NotNull
   public static final String COMMAND_ADVIN = "ADVIN";
   @Keep
   @NotNull
   public static final String COMMAND_GSCFG = "GSCFG";
   @Keep
   @NotNull
   public static final String COMMAND_KEYCFG = "KEYCFG";
   @Keep
   @NotNull
   public static final String COMMAND_LED = "LED";
   @Keep
   @NotNull
   public static final String COMMAND_BUZ = "BUZ";
   @Keep
   @NotNull
   public static final String COMMAND_RAP = "RAP";
   @Keep
   @NotNull
   public static final String COMMAND_OTA = "OTA";
   @Keep
   @NotNull
   public static final String COMMAND_BWMODE = "BWMODE";
   @Keep
   @NotNull
   public static final String COMMAND_BWMODE_OPEN = "0";
   @Keep
   @NotNull
   public static final String COMMAND_BWMODE_CLOSE = "1";
   @Keep
   @NotNull
   public static final String COMMAND_PIN = "PIN";
   @Keep
   @NotNull
   public static final String COMMAND_TX_POWER = "TXPOWER";
   @Keep
   @NotNull
   public static final String COMMAND_EXTEND = "EXTEND";
   @Keep
   @NotNull
   public static final String COMMAND_BADVDATA = "BADVDATA";
   @Keep
   @NotNull
   public static final String COMMAND_END = "END";
   @Keep
   public static final int COMMAND_SUCCESSFUL = 2;
   @Keep
   public static final int COMMAND_FAILED = 3;
   @Keep
   public static final int COMMAND_TIME_OUT = 4;
   @Keep
   public static final int COMMAND_STATE_QUERY = 5;
   @Keep
   public static final int COMMAND_STATE_SET = 6;
   @Keep
   public static final int FLAG = 1;
   @Keep
   public static final int INCOMPLETE_SERVICE_UUIDS_16BIT = 2;
   @Keep
   public static final int INCOMPLETE_SERVICE_UUIDS_128BIT = 6;
   @Keep
   public static final int COMPLETE_LOCAL_NAME = 9;
   @Keep
   public static final int TX_POWER_LEVEL = 10;
   @Keep
   public static final int SERVICE_DATA = 22;
   @Keep
   public static final int MANUFACTURER_SPECIFIC_DATA = 255;
   @Keep
   @NotNull
   public static final String SPP_MODE = "SPP";
   @Keep
   @NotNull
   public static final String BLE_MODE = "BLE";

   private Constant() {
   }
}
