package com.opurex.ortus.client.utils.scale;

import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.opurex.ortus.client.activities.BluetoothScaleSelectionActivity

class BatteryUtil {
    companion object {
        public fun checkOptimization(activity: BluetoothScaleSelectionActivity, name:String):Boolean{
            var bRet = false
            val powerManager = activity.getSystemService(POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(name)) {
                LogUtil.info( "checkOptimization  应用未被允许忽略电池优化，启动请求")
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$name")
                activity.startActivityForResult(intent, 0)
            }else{
                bRet = true;
                LogUtil.info(  "checkOptimization  应用允许忽略电池优化")
            }
            return bRet;
        }
    }
}