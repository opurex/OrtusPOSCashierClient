package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.widget.addTextChangedListener
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.*
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_filter.toolbar
import kotlinx.android.synthetic.main.activity_filter.toolbarTitle
import kotlinx.android.synthetic.main.activity_throughput.*

class FilterActivity : BaseActivity(), View.OnClickListener {

    var handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    var nameFilterClickRunnable = Runnable {
        name_filter_switch.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.filter)
        toolbar.setNavigationOnClickListener { finish() }

        name_filter_switch.setOnClickListener(this)

        rssiSeekBar.progress = getInt("rssiValue", -100) + 100

        rssi_filter_threshold_atv.text = " ${getInt("rssiValue", -100)} dB"

        var nameSwitch = getBoolean("nameSwitch", false)
        name_filter_switch.isOpened = nameSwitch

        name_filter_aet.setText(getStr("nameValue", ""))

        name_filter_aet.addTextChangedListener {
            putStr("nameValue", it.toString())
        }

        rssiSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            //监听进度条
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rssi_filter_threshold_atv.text = " ${progress - 100} dB"
                putInt("rssiValue", progress - 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                val start = seekBar.progress
                rssi_filter_threshold_atv.text = "${start - 100} dB"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val end = seekBar.progress
                rssi_filter_threshold_atv.text = "${end - 100} dB"
            }
        })

    }

    override fun getLayout() = R.layout.activity_filter

    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, FilterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.name_filter_switch -> {
                // 解决重复快速点击导致hexCheck时 发送框内容转换出现错误的bug
                // 不使用定时器，在方法最底部加上hexCheck.isEnabled = true 无效果
                name_filter_switch.isEnabled = false
                handler.postDelayed(nameFilterClickRunnable, 300)
                putBoolean("nameSwitch", name_filter_switch.isOpened)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(nameFilterClickRunnable)
    }

}