package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.widget.addTextChangedListener
import com.feasycom.ble.controler.FscBleCentralApi
import com.feasycom.ble.controler.FscBleCentralApiImp
import com.feasycom.ble.controler.FscBleCentralCallbacksImp
import com.feasycom.common.bean.ConnectType
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.*
import kotlinx.android.synthetic.main.setting_ble_connect_test.*
import com.swallowsonny.convertextlibrary.hex2ByteArray
import java.util.*
import kotlin.system.measureTimeMillis

class BleConnectTestActivity : BaseActivity(), OnClickListener {

    private lateinit var mFscDevice: FscDevice
    private lateinit var mBleApi: FscBleCentralApi
    private val testStatus = StringBuffer()

    private var handler: Handler? = null

    private var mByteArray: ByteArray = byteArrayOf()

    private var cStart: Calendar = Calendar.getInstance()
    private var cEnd: Calendar = Calendar.getInstance()
    private var okCount = 0
    private var bleConnectFailCount = 0
    private var testIsFinish = false
    private var connecting = false
    private val connectTimeout = 10000

    private val hexKeyListener: HexKeyListener by lazy {
        HexKeyListener()
    }

    var hexClickRunnable = Runnable {
        ble_hex_check.isEnabled = true
    }

    override fun getLayout(): Int {
        return R.layout.setting_ble_connect_test
    }

    override fun initView() {
        initToolbar()
        intent?.getParcelableExtra<FscDevice>("FscDevice")?.let {
            mFscDevice = it
            ble_test_toolbarTitle.text = it.name
        }
        if (!::mFscDevice.isInitialized) {
            return
        }
        mBleApi = FscBleCentralApiImp.getInstance()
        mBleApi.setCallbacks(object : FscBleCentralCallbacksImp() {
            override fun blePeripheralConnected(
                gatt: BluetoothGatt?, address: String?, tye: ConnectType?
            ) {
                MsgLogger.e(TAG, "blePeripheralConnected: BLE连接成功.")
                connecting = false
                connectedToDo()
                handler?.removeMessages(4) // 移除连接超时计时器
                handler?.sendEmptyMessageDelayed(1, 500)
            }

            override fun sendPacketProgress(
                address: String?, percentage: Int, sendByte: ByteArray?
            ) {
                MsgLogger.e(TAG, "sendPacketProgress percentage => $percentage")
                if (percentage == 100) {
                    //数据发送成功
                    addState("${resources.getString(R.string.btnSends)}${mByteArray.size}bytes\r\n****************************")

                    okCount++
                    val bleTestSuccessCount = String.format("%d", okCount)
                    runOnUiThread {
                        ble_test_success_count_tv.text = bleTestSuccessCount
                    }

                    var disconnectTime = "1000"
                    if (ble_disconnect_time_et.text.toString().isNotEmpty()) {
                        disconnectTime = ble_disconnect_time_et.text.toString() + "000"
                    } else {
                        runOnUiThread {
                            ble_disconnect_time_et.setText("1")
                        }
                    }
                    handler?.sendEmptyMessageDelayed(2, disconnectTime.toLong()) // 设置断开连接间隔时间
                }
            }

            override fun blePeripheralDisconnected(
                gatt: BluetoothGatt?, address: String, status: Int
            ) {
                MsgLogger.e(TAG, "blePeripheralDisconnected: BLE断开连接.")
                connecting = false
                handler?.removeMessages(4) // 移除连接超时计时器
                var connectTime = "1000"
                if (ble_reconnect_time_et.text.toString().isNotEmpty()) {
                    connectTime = ble_reconnect_time_et.text.toString() + "000"
                } else {
                    runOnUiThread {
                        ble_reconnect_time_et.setText("1")
                    }
                }
                handler?.sendEmptyMessageDelayed(3, connectTime.toLong()) // 设置重新连接间隔时间
            }

        })

        handler = @SuppressLint("HandlerLeak") object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 -> {
                        // 连接成功，发送数据
                        sendData()
                    }
                    2 -> {
                        // 数据发送成功，断开连接
                        disconnect()
                    }
                    3 -> {
                        // 断开连接成功，重新连接
                        connect(mFscDevice.address)
                    }
                    4 -> {
                        // ble连接失败，主动断开连接并重新连接
                        val bleConnectFailed =
                            resources.getString(R.string.connect) + resources.getString(R.string.failure) + "\n" + "****************************"
                        addState(bleConnectFailed)
                        bleConnectFailCount++
                        val bleConnectFailCount = String.format("%d", bleConnectFailCount)
                        runOnUiThread {
                            ble_test_fail_tv.text = bleConnectFailCount
                        }
                        if (connecting) {
                            // 取消连接
                            disconnect()
                        }
                    }
                }
            }
        }

        ble_hex_check.isOpened = getBoolean("bleHexCheck")
        mByteArray = getByteArray("bleSendEdit")
        if (ble_hex_check.isOpened) {
            with(ble_send_edit_et) {
                keyListener = hexKeyListener
                setText(mByteArray.toHexString())
            }
        } else {
            with(ble_send_edit_et) {
                keyListener = TextKeyListener.getInstance()
                setText(String(mByteArray))
            }
            ble_send_edit_et.keyListener = TextKeyListener.getInstance()
        }

        initEvent()
    }

    private fun initToolbar() {
        setSupportActionBar(ble_toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        ble_toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initEvent() {
        ble_start_test_btn.setOnClickListener(this)
        ble_clear_data_btn.setOnClickListener(this)
        ble_hex_check.setOnClickListener(this)

        ble_send_edit_et.addTextChangedListener(onTextChanged = { s, start, before, count ->
            mByteArray = if (s!!.isEmpty()) {
                byteArrayOf()
            } else {
                if (ble_hex_check.isOpened) {
                    s.toString().hex2ByteArray()
                } else {
                    s.toString().toByteArray()
                }
            }

            putByteArray("bleSendEdit", mByteArray)
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ble_start_test_btn -> {
                ble_start_test_btn.isEnabled = false
                connect(mFscDevice.address)
            }
            R.id.ble_clear_data_btn -> {
                testStatus.setLength(0)
                ble_receive_et.setText("")
            }
            R.id.ble_hex_check -> {
                ble_hex_check.isEnabled = false
                handler?.postDelayed(hexClickRunnable, 500)
                measureTimeMillis {
                    putBoolean("bleHexCheck", ble_hex_check.isOpened)
                    if (ble_hex_check.isOpened) {
                        val hexString = mByteArray.toHexString()
                        with(ble_send_edit_et) {
                            keyListener = hexKeyListener
                            setText(hexString)
                            try {
                                setSelection(hexString.length)
                            } catch (e: java.lang.IndexOutOfBoundsException) {
                                setSelection(hexString.length - 1)
                            }
                        }
                    } else {
                        if (mByteArray.isNotEmpty()) {
                            val string = mByteArray.getEncoding()
                            runOnUiThread {
                                with(ble_send_edit_et) {
                                    keyListener = TextKeyListener.getInstance()
                                    setText(string)
                                    try {
                                        setSelection(string.length)
                                    } catch (e: java.lang.IndexOutOfBoundsException) {
                                        setSelection(string.length - 1)
                                    }
                                }
                            }
                        } else {
                            runOnUiThread {
                                with(ble_send_edit_et) {
                                    keyListener = TextKeyListener.getInstance()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addState(text: CharSequence) {
        if (testStatus.length > 10000) {
            testStatus.delete(0, 3000)
        }
        cStart = cEnd
        val mCalendar = Calendar.getInstance()
        cEnd = mCalendar
        val hour = mCalendar[Calendar.HOUR_OF_DAY]
        val minute = mCalendar[Calendar.MINUTE]
        val second = mCalendar[Calendar.SECOND]
        val strTime = String.format("【%02d:%02d:%02d】", hour, minute, second)
        testStatus.append("$strTime$text\r\n")
        runOnUiThread {
            ble_receive_et.setText(testStatus)
            if (ble_receive_et.text.length > 100) {
                ble_receive_et.setSelection(ble_receive_et.text.length - 1)
            }
        }
    }

    private fun appendInterval() {
        val i: Long = cEnd.time.time - cStart.time.time
        val string = """ - $i ms
"""
        testStatus.replace(testStatus.length - 2, testStatus.length, string)
        runOnUiThread {
            if (ble_receive_et.text.length > 100) {
                ble_receive_et.setSelection(ble_receive_et.text.length - 1)
            }
        }
    }

    private fun connectedToDo() {
        var string =
            resources.getString(R.string.connect) + "[" + mFscDevice.name + "]" + resources.getString(
                R.string.success
            )
        addState(string)
        appendInterval()
    }

    @SuppressLint("MissingPermission")
    private fun connect(address: String) {
        if (testIsFinish) {
            return
        }
        val strCount: String = (okCount + 1).toString()
        addState(getText(R.string.No_).toString() + strCount + getText(R.string.No_2) + getText(R.string.startTest))
        addState(resources.getString(R.string.start) + resources.getString(R.string.connect) + "[" + mFscDevice.name + "]")
        appendInterval()

        mBleApi.connect(address, getBoolean("facpSwitchOpen", false))
        connecting = true
        // 设置10秒连接超时计时器
        handler?.sendEmptyMessageDelayed(4, connectTimeout.toLong())

    }

    @SuppressLint("MissingPermission")
    private fun sendData() {
        mBleApi.send(mByteArray)
    }

    @SuppressLint("MissingPermission")
    private fun disconnect() {
        mBleApi.disconnect(mFscDevice.address)
    }

    override fun onDestroy() {
        super.onDestroy()
        testIsFinish = true
        disconnect()
    }

    companion object {
        private const val TAG = "BleBleConnect"

        fun activityStart(context: Context, fscDevice: FscDevice) {
            val intent = Intent(context, BleConnectTestActivity::class.java)
            intent.putExtra("FscDevice", fscDevice)
            context.startActivity(intent)
        }
    }

}