package com.opurex.ortus.client.drivers.utils;

import com.opurex.ortus.client.drivers.POSDeviceManager;

/**
 * Created by svirch_n on 22/01/16.
 */
public interface DeviceManagerEventListener {

    public void onDeviceManagerEvent(POSDeviceManager manager, DeviceManagerEvent event);

}
