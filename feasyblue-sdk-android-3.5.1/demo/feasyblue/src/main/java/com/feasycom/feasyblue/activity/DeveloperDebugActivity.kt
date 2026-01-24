package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.logcat.*
import com.feasycom.feasyblue.utils.*
import kotlinx.android.synthetic.main.activity_developer_debug.*

class DeveloperDebugActivity :  BaseActivity(), View.OnClickListener{

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.developer_debug)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        facp_switch_view.isOpened = getBoolean("facpSwitchOpen", false)
        initEvent()
    }

    private fun initEvent() {
        facp_switch_view.setOnClickListener(this)
        debug_log_switch_view.setOnClickListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_developer_debug
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.facp_switch_view -> {
                if (facp_switch_view.isOpened) {
                    putBoolean("facpSwitchOpen", true)
                } else {
                    putBoolean("facpSwitchOpen", false)
                }
            }
            R.id.debug_log_switch_view -> {
                if (debug_log_switch_view.isOpened) {
                    openLog()
                } else {
                    closeLog()
                }
            }
        }
    }

    private fun openLog(){
        startService(Intent(this,LogcatService::class.java))
    }

    private fun closeLog() {
        stopService(Intent(this, LogcatService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        closeLog()
    }

    companion object {
        const val TAG: String = "DeveloperDebugActivity"
        fun activityStart(context: Context){
            val intent = Intent(context, DeveloperDebugActivity::class.java)
            context.startActivity(intent)
        }
    }

}