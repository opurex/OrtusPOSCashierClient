package com.opurex.ortus.client.utils.scale

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.FragmentActivity

class BatteryUtilProductScaleDialog {

    companion object {
        /**
         * Checks if battery optimization is disabled for the app
         * @param context The context (can be FragmentActivity now)
         * @param packageName The package name of the app
         * @return true if battery optimization is disabled or not applicable, false otherwise
         */
        @JvmStatic
        fun checkOptimization(context: FragmentActivity, packageName: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                if (powerManager.isIgnoringBatteryOptimizations(packageName)) {
                    true
                } else {
                    // Try to request to ignore battery optimizations
                    try {
                        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        intent.data = Uri.parse("package:$packageName")
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback: open battery optimization settings
                        try {
                            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                            context.startActivity(intent)
                        } catch (e2: Exception) {
                            // If all fails, just return true to allow the operation to continue
                            true
                        }
                    }
                    false
                }
            } else {
                true // Pre-Marshmallow, no battery optimization
            }
        }
    }
}