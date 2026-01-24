package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import com.feasycom.feasyblue.R

class WiFiSearchDeviceActivity: BaseActivity() {

    override fun initView() {

    }

    override fun getLayout() = R.layout.activity_wifi_search_device

    companion object{
        private const val TAG = "WiFiSearchDeviceActivity"

        fun activityStart(context: Context){
            val intent = Intent(context, WiFiSearchDeviceActivity::class.java)
            context.startActivity(intent)
        }
    }
}