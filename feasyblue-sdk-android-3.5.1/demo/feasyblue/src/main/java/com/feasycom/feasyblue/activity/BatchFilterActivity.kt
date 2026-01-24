package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.*
import kotlinx.android.synthetic.main.activity_batch_filter.*

class BatchFilterActivity : BaseActivity(), View.OnClickListener {

    var handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    var nameFilterClickRunnable = Runnable {
        batch_name_filter_switch.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        setSupportActionBar(batch_toolbar)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        batch_toolbarTitle.text = getString(R.string.filter)
        batch_toolbar.setNavigationOnClickListener { finish() }

        batch_name_filter_switch.setOnClickListener(this)

        batch_rssiSeekBar.progress = getInt("batchRssiValue", -70) + 70

        batch_rssi_filter_threshold_atv.text = " ${getInt("batchRssiValue", -70)} dB"

        Log.e("tag","batchNameSwitch => ${getBoolean("batchNameSwitch", false)}")

        getBoolean("batchNameSwitch", false).let {
            batch_name_filter_switch.isOpened = it
        }

        batch_name_filter_aet.setText(getStr("batchNameValue", ""))

        batch_name_filter_aet.addTextChangedListener {
            putStr("batchNameValue", it.toString())
        }

        batch_rssiSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //监听进度条
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                batch_rssi_filter_threshold_atv.text = " ${progress - 70} dB"
                putInt("batchRssiValue", progress - 70)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                val start = seekBar.progress
                batch_rssi_filter_threshold_atv.text = "${start - 70} dB"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val end = seekBar.progress
                batch_rssi_filter_threshold_atv.text = "${end - 70} dB"
            }
        })

    }

    override fun getLayout() = R.layout.activity_batch_filter

    companion object {
        fun activityStart(context: Context) {
            val intent = Intent(context, BatchFilterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.batch_name_filter_switch -> {
                // 解决重复快速点击导致hexCheck时 发送框内容转换出现错误的bug
                // 不使用定时器，在方法最底部加上hexCheck.isEnabled = true 无效果
                batch_name_filter_switch.isEnabled = false
                handler.postDelayed(nameFilterClickRunnable, 300)
                putBoolean("batchNameSwitch", batch_name_filter_switch.isOpened)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(nameFilterClickRunnable)
    }

}