package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import com.feasycom.feasyblue.R

class SppAutoConnectTestActivity: BaseActivity() {

    override fun initView() {

    }

    override fun getLayout(): Int {
        return R.layout.activity_search_device
    }

    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, SppAutoConnectTestActivity::class.java)
            context.startActivity(intent)
        }
    }
}