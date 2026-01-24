package com.feasycom.ble.sdk;

import android.bluetooth.BluetoothDevice;
import com.feasycom.common.bean.FscDevice;

public interface ScanResultCallback {
   void scannerResult(BluetoothDevice var1, FscDevice var2, byte[] var3);
}
