package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.method.TextKeyListener
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.widget.addTextChangedListener
import com.feasycom.common.bean.FscDevice
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.*
import com.swallowsonny.convertextlibrary.hex2ByteArray
import kotlinx.android.synthetic.main.setting_spp_connect_test.*
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class SppConnectTestActivity : BaseActivity(), OnClickListener {

    private val share = FEShare.getInstance()
    private val strState = StringBuffer()
    private var cStart: Calendar = Calendar.getInstance()
    private var cEnd: Calendar = Calendar.getInstance()

    private var okCount = 0
    private var failCount = 0

    private var mByteArray: ByteArray = byteArrayOf()

    private var isFinish = false

    private lateinit var mFscDevice: FscDevice

    private val hexKeyListener: HexKeyListener by lazy {
        HexKeyListener()
    }

    var handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    var hexClickRunnable = Runnable {
        spp_hex_check.isEnabled = true
    }

    override fun getLayout(): Int {
        return R.layout.setting_spp_connect_test
    }

    override fun initView() {
        initToolbar()
        intent?.getParcelableExtra<FscDevice>("FscDevice")?.let {
            mFscDevice = it
            spp_test_toolbarTitle.text = it.name
        }
        if (!::mFscDevice.isInitialized) {
            return
        }

        spp_hex_check.isOpened = getBoolean("sppHexCheck")
        mByteArray = getByteArray("sppSendEdit")
        if (spp_hex_check.isOpened) {
            with(spp_send_edit_et) {
                keyListener = hexKeyListener
                setText(mByteArray.toHexString())
            }
        } else {
            with(spp_send_edit_et) {
                keyListener = TextKeyListener.getInstance()
                setText(String(mByteArray))
            }
            spp_send_edit_et.keyListener = TextKeyListener.getInstance()
        }

        initEvent()

    }

    private fun initToolbar() {
        setSupportActionBar(spp_toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        spp_toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initEvent() {
        spp_start_test_btn.setOnClickListener(this)
        spp_clear_data_btn.setOnClickListener(this)
        spp_hex_check.setOnClickListener(this)

        spp_send_edit_et.addTextChangedListener(onTextChanged = { s, start, before, count ->
            mByteArray = if (s!!.isEmpty()) {
                byteArrayOf()
            } else {
                if (spp_hex_check.isOpened) {
                    s.toString().hex2ByteArray()
                } else {
                    s.toString().toByteArray()
                }
            }

            putByteArray("sppSendEdit", mByteArray)
        })

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.spp_start_test_btn -> {
                spp_start_test_btn.isEnabled = false
                connect()
            }
            R.id.spp_clear_data_btn -> {
                strState.setLength(0)
                spp_receive_et.setText("")
            }
            R.id.spp_hex_check -> {
                spp_hex_check.isEnabled = false
                handler?.postDelayed(hexClickRunnable, 500)
                measureTimeMillis {
                    putBoolean("sppHexCheck", spp_hex_check.isOpened)
                    if (spp_hex_check.isOpened) {
                        val hexString = mByteArray.toHexString()
                        with(spp_send_edit_et) {
                            keyListener = hexKeyListener
                            setText(hexString)
                            try {
                                setSelection(hexString.length)
                            } catch (e: IndexOutOfBoundsException) {
                                setSelection(hexString.length - 1)
                            }
                        }
                    } else {
                        if (mByteArray.isNotEmpty()) {
                            val string = mByteArray.getEncoding()
                            runOnUiThread {
                                with(spp_send_edit_et) {
                                    keyListener = TextKeyListener.getInstance()
                                    setText(string)
                                    try {
                                        setSelection(string.length)
                                    } catch (e: IndexOutOfBoundsException) {
                                        setSelection(string.length - 1)
                                    }
                                }
                            }
                        } else {
                            runOnUiThread {
                                with(spp_send_edit_et) {
                                    keyListener = TextKeyListener.getInstance()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connect() {
        if (isFinish) {
            return
        }
        val remoteDevice =
            FEShare.getInstance().bluetoothAdapter.getRemoteDevice(mFscDevice.address.toString())
        val strCount: String = (okCount + 1).toString()
        addState(
            getText(R.string.No_).toString() + strCount + getText(R.string.No_2) + getText(
                R.string.startTest
            )
        )
        addState(resources.getString(R.string.start) + resources.getString(R.string.connect) + "[" + remoteDevice.name + "]")
        appendInterval()
        thread {
            if (share.connect(remoteDevice)) {
                Log.i(TAG, "连接成功")
                connectedToDo()
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val length = share.writes(mByteArray)
                if (length > 0) {
                    addState("${resources.getString(R.string.btnSends)}${mByteArray.size}bytes\r\n****************************")
                    successful()
                } else {
                    addState(resources.getString(R.string.btnSends) + resources.getString(R.string.failure) + "\n" + "****************************")
                    failure()
                }
            } else {
                //连接失败
                val string =
                    resources.getString(R.string.connect) + resources.getString(R.string.failure) + "\n" + "****************************"
                addState(string)
                failure()
            }
        }
    }

    private fun addState(text: CharSequence) {
        if (strState.length > 10000) {
            strState.delete(0, 3000)
        }
        cStart = cEnd
        val c = Calendar.getInstance()
        cEnd = c
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]
        val second = c[Calendar.SECOND]
        val strTime = String.format("【%02d:%02d:%02d】", hour, minute, second)
        strState.append("${strTime}${text}\r\n")
        runOnUiThread {
            spp_receive_et.setText(strState)
            if (spp_receive_et.text.length > 100) {
                spp_receive_et.setSelection(spp_receive_et.text.length - 1)
            }
        }
    }

    private fun appendInterval() {
        val i: Long = cEnd.time.time - cStart.time.time
        val string = """ - $i ms
"""
        strState.replace(strState.length - 2, strState.length, string)
        runOnUiThread {
            spp_receive_et.setText(strState)
            if (spp_receive_et.text.length > 100) {
                spp_receive_et.setSelection(spp_receive_et.text.length - 1)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectedToDo() {
        var string =
            resources.getString(R.string.connect) + "[" + share.device.name + "]" + resources.getString(
                R.string.success
            )
        addState(string)
        appendInterval()
    }

    private fun successful() {
        okCount++
        val string = String.format("%d", okCount)
        runOnUiThread { spp_test_success_count_tv.setText(string) }
        Thread {
            var strTime = "1000"
            if (spp_disconnect_time_et.text.toString().isNotEmpty()) {
                strTime = spp_disconnect_time_et.text.toString() + "000"
            } else {
                runOnUiThread { spp_disconnect_time_et.setText("1") }
            }
            val time = java.lang.Long.valueOf(strTime)
            try {
                Thread.sleep(time)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            share.disConnect()

            var connectTime = "1000"
            if (spp_reconnect_time_et.text.toString().isNotEmpty()) {
                connectTime = spp_reconnect_time_et.text.toString() + "000"
            } else {
                runOnUiThread { spp_reconnect_time_et.setText("1") }
            }
            val time1 = java.lang.Long.valueOf(connectTime)
            try {
                Thread.sleep(time1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            connect()
        }.start()

    }

    private fun failure() {
        failCount++
        val string = String.format("%d", failCount)
        runOnUiThread {
            spp_test_fail_count_tv.text = string
        }
        thread {
            var connectTime = "1000"
            if (spp_reconnect_time_et.text.toString().isNotEmpty()) {
                connectTime = spp_reconnect_time_et.text.toString() + "000"
            } else {
                runOnUiThread { spp_reconnect_time_et.setText("1") }
            }
            val time1 = java.lang.Long.valueOf(connectTime)
            try {
                Thread.sleep(time1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            connect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isFinish = true
        share.disConnect()
    }

    companion object {
        private const val TAG = "SppConnectTestActivity"

        fun activityStart(context: Context, fscDevice: FscDevice) {
            val intent = Intent(context, SppConnectTestActivity::class.java)
            intent.putExtra("FscDevice", fscDevice)
            context.startActivity(intent)
        }
    }

}
