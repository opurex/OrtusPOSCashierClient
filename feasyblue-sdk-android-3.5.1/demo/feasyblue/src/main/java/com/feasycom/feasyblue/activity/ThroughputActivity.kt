package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.MediaStore
import android.text.method.TextKeyListener
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Chronometer
import androidx.core.net.toFile
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.feasycom.ble.controler.FscBleCentralApi
import com.feasycom.ble.controler.FscBleCentralApiImp
import com.feasycom.ble.controler.FscBleCentralCallbacksImp
import com.feasycom.common.bean.ConnectType
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.Constant
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.utils.*
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import com.feasycom.spp.controler.FscSppCentralCallbacksImp
import com.swallowsonny.convertextlibrary.hex2ByteArray
import com.swallowsonny.convertextlibrary.toHexString
import kotlinx.android.synthetic.main.activity_ota.*
import kotlinx.android.synthetic.main.activity_throughput.*
import kotlinx.android.synthetic.main.activity_throughput.toolbar
import kotlinx.android.synthetic.main.activity_throughput.toolbarTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.CRC32
import kotlin.system.measureTimeMillis

class ThroughputActivity : BaseActivity(), View.OnClickListener {

    lateinit var mFscDevice: FscDevice
    lateinit var mDevice: BluetoothDevice
    lateinit var mBleApi: FscBleCentralApi
    lateinit var mSppApi: FscSppCentralApi
    private var mBytesToBeSent = 0
    private var mReceiveBufferHex = StringBuffer() // 接收数据缓存buffer hex string 类型
    private var mReceiveBuffer = StringBuffer() // 接收数据缓存buffer string类型
    private var mReceiveByteCount = 0
    private var mReceivePackageCount = 0
    private var mSendByteCountSend = 0
    private var mSendPackageCountSend = 0
    private var mDeviceMode = "BLE"
    private var mSendCRC = CRC32() // 发送crc
    private var mReceiveCRC = CRC32() // 接收crc

    private var mSendFileByte = 0

    private var mIsPause = false

    private var mIsSending = false

    private var mResultCode: Int = 0
    private var mData: Intent? = null
    private var mLastStopTime: Long = 0L

    private var mByteArray: ByteArray = byteArrayOf()

    private var address: String = ""
    private lateinit var mMessageDialog: WFBaseDialog

    private var receiveData: MutableList<String> = mutableListOf()
    private var bleSendData: MutableList<String> = mutableListOf()
    private var sppSendData: MutableList<String> = mutableListOf()

    private val hexKeyListener: HexKeyListener by lazy {
        HexKeyListener()
    }


    var handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    var hexClickRunnable = Runnable {
        hexCheck.isEnabled = true
    }

    @SuppressLint("ClickableViewAccessibility", "MissingPermission")
    override fun initView() {
        initToolbar()
        intent?.getParcelableExtra<FscDevice>("FscDevice")?.let {
            MsgLogger.d(" FscDevice   =>   $it   name   =>   ${it.name}")
            mFscDevice = it
            toolbarTitle.text = it.name
            mDevice = it.device
            mDeviceMode = it.mode
            toolbarSubtitle.text = resources.getString(R.string.connecting)
            address = mFscDevice.address
            sendButton.isEnabled = false
            sendFileButton.isEnabled = false
        }
        intent?.getParcelableExtra<BluetoothDevice>("BluetoothDevice")?.let {
            MsgLogger.d(" BluetoothDevice   =>   $it   name   =>   ${it.name}")
            mDevice = it
            toolbarTitle.text = it.name
            mDeviceMode = Constant.SPP_MODE
            address = mDevice.address
            toolbarSubtitle.text = resources.getString(R.string.through_connected)
        }
        if (!::mFscDevice.isInitialized && !::mDevice.isInitialized) {
            return
        }
        if (mDeviceMode == Constant.BLE_MODE) {
            with(switchServiceButton) {
                visibility = View.VISIBLE
                isEnabled = false
            }
            mBleApi = FscBleCentralApiImp.getInstance()
            mBleApi.setCallbacks(object : FscBleCentralCallbacksImp() {
                override fun blePeripheralConnected(
                    gatt: BluetoothGatt?,
                    address: String,
                    connectType: ConnectType
                ) {
                    MsgLogger.e("blePeripheralConnected: BLE连接成功.")
                    uiDeviceConnected()
                }

                override fun blePeripheralDisconnected(
                    gatt: BluetoothGatt?,
                    address: String,
                    status: Int
                ) {
                    MsgLogger.e("blePeripheralConnected: BLE断开连接.")
                    uiDeviceDisconnected()
                }

                override fun packetReceived(
                    address: String,
                    strValue: String,
                    hexString: String,
                    data: ByteArray
                ) {
                    MsgLogger.e("packetReceived BLE收包回调: address => $address \r\n strValue => $strValue \r\n hexString => $hexString \r\n data => ${data.bytesToHex()}")
                    uiReceiveData(strValue, hexString, data)
                }


                override fun sendPacketProgress(
                    address: String,
                    percentage: Int,
                    sendByte: ByteArray
                ) {
                    MsgLogger.e("sendPacketProgress BLE发包进度回调: address => $address \r\n percentage => $percentage \r\n sendByte => ${sendByte.bytesToHex()}")
                    uiSendDataProgress(percentage, sendByte.size)
                }

                override fun packetSend(
                    address: String,
                    strValue: String,
                    data: ByteArray
                ) {//数据发送完成的回调
                    MsgLogger.e("packetSend BLE发包回调: address => $address \r\n strValue => $strValue \r\n data => ${data.bytesToHex()}")
                    uiSendData(data)
                    bleSendData.add((if (hexCheck.isOpened) data.bytesToHex() else strValue))
                }

                override fun onReadRemoteRssi(rssi: Int) {
//                    MsgLogger.e("onReadRemoteRssi rssi => $rssi")
                    updateRssiView(rssi)
                }
            })
            if (::mFscDevice.isInitialized) {
                MsgLogger.e(" BLE开始连接.")
                mBleApi.connect(address, getBoolean("facpSwitchOpen", false))
            }
            rssi_value_icon_rl.visibility = View.VISIBLE
        } else {
            with(switchServiceButton) {
                visibility = View.GONE
                isEnabled = false
            }
            mSppApi = FscSppCentralApiImp.getInstance()
            mSppApi.setCallbacks(object : FscSppCentralCallbacksImp() {
                override fun sppPeripheralConnected(
                    device: BluetoothDevice,
                    connectType: ConnectType
                ) {
                    MsgLogger.e("sppPeripheralConnected: 连接成功")
                    uiDeviceConnected()
                }

                override fun sppPeripheralDisconnected(address: String) {
                    MsgLogger.e("sppPeripheralConnected: 断开连接")
                    uiDeviceDisconnected()
                }

                override fun packetReceived(
                    address: String,
                    strValue: String,
                    hexString: String,
                    data: ByteArray
                ) {
                    super.packetReceived(address, strValue, hexString, data)
                    MsgLogger.e("packetReceived SPP收包回调: address => $address \r\n strValue => $strValue \r\n hexString => $hexString \r\n data => ${data.bytesToHex()}")
                    uiReceiveData(strValue, hexString, data)
                }

                override fun sendPacketProgress(
                    address: String,
                    percentage: Int,
                    sendByte: ByteArray
                ) {
                    MsgLogger.e("sendPacketProgress SPP发包进度回调: address => $address \r\n percentage => $percentage \r\n sendByte => ${sendByte.bytesToHex()}")
                    uiSendDataProgress(percentage, sendByte.size)
                }

                override fun packetSend(
                    address: String?,
                    strValue: String,
                    hexString: String,
                    data: ByteArray
                ) {
                    MsgLogger.e("packetSend SPP发包回调: address => $address \r\n strValue => $strValue \r\n data => ${data.bytesToHex()}")
                    uiSendData(data)
                    sppSendData.add((if (hexCheck.isOpened) data.bytesToHex() else strValue))
                }
            })
            if (::mFscDevice.isInitialized) {
                MsgLogger.e(" SPP开始连接.")
                mSppApi.connect(address)
            }
            rssi_value_icon_rl.visibility = View.GONE
        }
        rateText.text = getString(R.string.rate, 0)
        hexCheck.isOpened = getBoolean("hexCheck")
        mByteArray = getByteArray("sendEdit")
        editByteCount.text = getString(R.string.byteString, mByteArray.size)
        if (hexCheck.isOpened) {
            with(sendEdit) {
                keyListener = hexKeyListener
                setText(mByteArray.toHexString())
            }
        } else {
            with(sendEdit) {
                keyListener = TextKeyListener.getInstance()
                setText(String(mByteArray))
            }
            sendEdit.keyListener = TextKeyListener.getInstance()
        }
        initEvent()
        initUi()
    }

    private fun updateRssiView(rssiValue: Int) {
        runOnUiThread {
            when {
                rssiValue < -100 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi0)
                    rssi_value_atv.text = rssiValue.toString()
                }
                rssiValue < -85 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi1)
                    rssi_value_atv.text = rssiValue.toString()
                }
                rssiValue < -70 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi2)
                    rssi_value_atv.text = rssiValue.toString()
                }
                rssiValue < -65 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi3)
                    rssi_value_atv.text = rssiValue.toString()
                }
                rssiValue < -50 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi4)
                    rssi_value_atv.text = rssiValue.toString()
                }
                rssiValue < -35 -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi5)
                    rssi_value_atv.text = rssiValue.toString()
                }
                else -> {
                    rssi_icon_aiv.setImageResource(R.drawable.rssi5)
                    rssi_value_atv.text = rssiValue.toString()
                }
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initEvent() {
        sendButton.setOnClickListener(this)
        sendFileButton.setOnClickListener(this)
        send_status_btn.setOnClickListener(this)
        switchServiceButton.setOnClickListener(this)
        export.setOnClickListener(this)
        clearButton.setOnClickListener(this)
        intervalSendCheck.setOnClickListener(this)
        hexCheck.setOnClickListener(this)
        send_new_line.setOnClickListener(this)

        intervalSendTime.addTextChangedListener {
            if (it?.isBlank() == true) {
                setSendInterval(0L)
            } else {
                setSendInterval(it.toString().toLong())
            }
        }


        sendEdit.addTextChangedListener(onTextChanged = { s, start, before, count ->
            MsgLogger.e("initEvent: s -> $s start -> $start  before -> $before  count -> $count")
            mByteArray = if (s!!.isEmpty()) {
                byteArrayOf()
            } else {
                if (hexCheck.isOpened) {
                    s.toString().hex2ByteArray()
                } else {
                    s.toString().toByteArray()
                }
            }

            putByteArray("sendEdit", mByteArray)
            mBytesToBeSent = mByteArray.size
            editByteCount.text = getString(R.string.byteString, mBytesToBeSent)
        })



        stop_btn.setOnClickListener {
            mIsPause = false
            mIsSending = false
            send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.start))
            stopSend()
            group2.visibility = View.VISIBLE
            group3.visibility = View.GONE
        }
        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            runOnUiThread {
                try {
                    if ((SystemClock.elapsedRealtime() - chronometer.base) != 0L) {
                        rateText.text = getString(
                            R.string.rate,
                            mSendFileByte / ((SystemClock.elapsedRealtime() - chronometer.base) / 1000)
                        )
                    }
                } catch (e: ArithmeticException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setSendInterval(interval: Long) {
        if (mDeviceMode == Constant.BLE_MODE) {
            mBleApi.setSendInterval(address, interval)
        } else {
            mSppApi.setSendInterval(address, interval)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        MsgLogger.e("onActivityResult: $requestCode    $resultCode")
        if (requestCode != SELECT_FILE) {
            return
        }
        if (resultCode == FILE_PATH || resultCode == FILE_SIZE) {
            mResultCode = resultCode
            mData = data
            sendFile()
        }
    }

    private fun sendFile() {
        rateText.text = getString(R.string.rate, 0)
        when (mResultCode) {
            FILE_SIZE -> {
                val size = mData?.getIntExtra("size", 0)!!
                val sizeStr = mData?.getStringExtra("sizeStr")!!
                selected_file_tv.text =
                    getString(R.string.selectedFile, "${sizeStr} ${getString(R.string.testFile)}")
                if (mIsSending) {
                    stopSend()
                }
                sendFile {
                    if (mDeviceMode == Constant.BLE_MODE) {
                        mBleApi.sendFile(size)
                    } else {
                        mSppApi.sendFile(size)
                    }
                }
            }
            FILE_PATH -> {
                mData?.data?.let {
                    selected_file_tv.text = getString(R.string.selectedFile, getFileNameByUri(it))
                    contentResolver.openInputStream(it)?.let { inputStream ->
                        if (mIsSending) {
                            stopSend()
                        }
                        sendFile {
                            if (mDeviceMode == Constant.BLE_MODE) {
                                mBleApi.sendFile(inputStream)
                            } else {
                                mSppApi.sendFile(inputStream)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getFileNameByUri(uri: Uri): String {
        return when (uri.scheme) {
            "content" -> {
                try {
                    contentResolver.query(uri, null, null, null, null)?.use {
                        if (it.moveToFirst()) {
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    MediaStore.Downloads.DISPLAY_NAME
                                )
                            )
                        } else {
                            " "
                        }
                    } ?: " "
                } catch (e: Exception) {
                    " "
                }
            }
            "file" -> {
                uri.toFile().name
            }
            else -> " "
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        intervalSendCheck.isOpened = false
        disconnect()
        handler.removeCallbacks(hexClickRunnable)
    }

    private fun disconnect() {
        if (mDeviceMode == Constant.BLE_MODE) {
            mBleApi.disconnect()
        } else {
            mSppApi.disconnect()
        }
    }

    fun uiDeviceConnected() {
        runOnUiThread {
            toolbarSubtitle.text = resources.getString(R.string.through_connected)
            sendButton.isEnabled = true
            sendFileButton.isEnabled = true
            send_status_btn.isEnabled = true
            if (mDeviceMode == Constant.BLE_MODE) {
                switchServiceButton.isEnabled = true
                sendFileButton.isEnabled = true
            }

        }
    }

    fun uiDeviceDisconnected() {
        runOnUiThread {
            toolbarSubtitle.text = resources.getString(R.string.through_disconnected)
            sendButton.isEnabled = false
            sendFileButton.isEnabled = false
            send_status_btn.isEnabled = false
            send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.start))
            MsgLogger.e("uiDeviceDisconnected: false 1")
            mIsSending = false
            if (mDeviceMode == Constant.BLE_MODE) {
                switchServiceButton.isEnabled = false
            }
            chronometer.stop()
        }
    }

    fun uiReceiveData(strValue: String, hexString: String, data: ByteArray) {
        if (mReceiveBuffer.length > 1024) {
            mReceiveBuffer.delete(0, mReceiveBuffer.length)
            mReceiveBuffer.setLength(0)
            mReceiveBufferHex.delete(
                0,
                mReceiveBufferHex.length
            )
            mReceiveBufferHex.setLength(0)
        }

        mReceiveBuffer.append(strValue)
        mReceiveBufferHex.append(hexString)
        mReceiveCRC.update(data)
        runOnUiThread {
            receiveEdit.setText(String(if (hexCheck.isOpened) mReceiveBufferHex else mReceiveBuffer))
            receiveData.add(String(if (hexCheck.isOpened) mReceiveBufferHex else mReceiveBuffer))
            receiveEdit.setSelection(receiveEdit.text.length, receiveEdit.text.length)
            crcReceive.text =
                getString(R.string.CRC, mReceiveCRC.value.toHexString().uppercase(Locale.ROOT))
            mReceiveByteCount += data.size
            mReceivePackageCount += 1
            receiveTextView.text =
                getString(R.string.receive, mReceiveByteCount, mReceivePackageCount)
        }
    }

    @SuppressLint("SetTextI18n")
    fun uiSendDataProgress(progress: Int, byteSize: Int) {
        runOnUiThread {
            numberProgressBar.progress = progress
            mSendFileByte += byteSize
            if (progress == 100) {
                mIsSending = false
                mIsPause = false
                switchServiceButton.isEnabled = true
                sendFileButton.isEnabled = true
                send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.start))
                rateText.text = getString(
                    R.string.rate, try {
                        mSendFileByte / ((SystemClock.elapsedRealtime() - chronometer.base) / 1000)
                    } catch (e: ArithmeticException) {
                        0
                    }
                )
                chronometer.stop()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun uiSendData(data: ByteArray) {
        mSendCRC.update(data)
        mSendByteCountSend += data.size
        runOnUiThread {
            crcSend.text =
                getString(R.string.CRC, mSendCRC.value.toHexString().uppercase(Locale.ROOT))
            mSendPackageCountSend += 1
            sendTextView.text = getString(R.string.send, mSendByteCountSend, mSendPackageCountSend)
        }
    }


    private fun send() {
        try {
            if (sendEdit.text.isNotBlank()) {
                val isSendNewLine = getBoolean("sendNewLine", false)
                val byteArrayString = String(mByteArray)

                val apiToSend = if (mDeviceMode == Constant.BLE_MODE) mBleApi else mSppApi

                if (isSendNewLine) {
                    if (byteArrayString == "\$OpenFscAtEngine\$") {
                        ToastUtil.show(this, getString(R.string.error_open_fsc))
                        return
                    }
                    val addCRLFByteArray = addCRLF(mByteArray)
                    apiToSend.send(addCRLFByteArray)
                } else {
                    if (byteArrayString.contains("AT")) {
                        ToastUtil.show(this, getString(R.string.error_send_at))
                        return
                    }
                    apiToSend.send(mByteArray)
                }
            }
        } catch (e: Exception) {
            MsgLogger.e("SEND => ${e.printStackTrace()}")
        }
    }

    private fun addCRLF(data: ByteArray): ByteArray {
        val crlf: ByteArray = byteArrayOf('\r'.code.toByte(), '\n'.code.toByte())
        val newData = ByteArray(data.size + crlf.size)
        System.arraycopy(data, 0, newData, 0, data.size)
        System.arraycopy(crlf, 0, newData, data.size, crlf.size)
        return newData
    }

    private fun sendFile(sendFileFunction: () -> Unit) {
        mIsPause = false
        mIsSending = true
        sendFileFunction()
        numberProgressBar.progress = 0
        group2.visibility = View.GONE
        group3.visibility = View.VISIBLE
        switchServiceButton.isEnabled = false
        sendFileButton.isEnabled = false
        send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.pause))
        mSendFileByte = 0
        chronometer.base = SystemClock.elapsedRealtime()
        val hour = ((SystemClock.elapsedRealtime() - chronometer.base) / 1000 / 60)
        chronometer.format = "0${hour}:%s"
        chronometer.start()
    }

    override fun getLayout() = R.layout.activity_throughput

    override fun onClick(v: View?) {
        intervalSendTime.clearFocus()
        when (v?.id) {
            R.id.sendButton -> {
                send()
            }
            R.id.sendFileButton -> {
                val intent = Intent(this@ThroughputActivity, SelectFileActivity::class.java)
                startActivityForResult(intent, SELECT_FILE)
            }
            R.id.send_status_btn -> {
                if (mIsPause) {
                    continueSend()
                    send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.pause))
                    mIsPause = false
                    mIsSending = true
                } else {
                    if (mIsSending) {
                        pauseSend()
                        send_status_btn.setImageDrawable(resources.getDrawable(R.drawable.start))
                        mIsPause = true
                        mIsSending = true
                    } else {
                        sendFile()
                        MsgLogger.e("onClick: 设置为没有暂停，正在发送文件")
                        mIsPause = false
                        mIsSending = true
                    }
                }
            }
            R.id.switchServiceButton -> {
                ServiceSelectActivity.activityStart(this, mFscDevice)
            }
            R.id.export -> {
                val directory = File(getExternalFilesDir(null), "ExportedData")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val suffix = if (mDeviceMode == Constant.BLE_MODE) "BLE" else "SPP"

                val receiveDataFileName =
                    "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}_${suffix}_ReceiveData"
                val receiveDataFile = File(directory, "$receiveDataFileName.txt")
                Log.e("tag", "receiveDataFile => ${receiveDataFile.path}")
                val isReceiveDataSaved = FileUtils().exportDataToTxt(receiveData, receiveDataFile)

                val sendDataFileName =
                    "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}_${suffix}_SendData"
                val sendDataFile = File(directory, "$sendDataFileName.txt")
                Log.e("tag", "sendDataFile => ${sendDataFile.path}")
                val isSendDataSaved = FileUtils().exportDataToTxt(
                    if (mDeviceMode == Constant.BLE_MODE) bleSendData else sppSendData,
                    sendDataFile
                )

                if (isReceiveDataSaved) {
                    val fileUri = FileShareHelper.getFileUri(this, receiveDataFile)
                    FileShareHelper.shareFile(this, fileUri, "application/pdf")
                }

                if (isSendDataSaved) {
                    val fileUri = FileShareHelper.getFileUri(this, sendDataFile)
                    FileShareHelper.shareFile(this, fileUri, "application/pdf")
                }

            }
            R.id.clearButton -> {
                initUi()
            }
            R.id.intervalSendCheck -> {
                if (sendEdit.text.isBlank()) return
                if (!intervalSendCheck.isOpened) return
                lifecycleScope.launch(Dispatchers.IO) {
                    while (intervalSendCheck.isOpened) {
//                        MsgLogger.e("intervalSendCheck is Opened2 => ${intervalSendCheck.isOpened}")
                        send()
//                        delay(try {
//                            intervalSendTime.text.toString().toLong()
//                        } catch (e: NumberFormatException) {
//                            intervalSendTime.hint.toString().toLong()
//                        })
                    }
                }
            }
            R.id.hexCheck -> {
                // 解决重复快速点击导致hexCheck时 发送框内容转换出现错误的bug
                // 不使用定时器，在方法最底部加上hexCheck.isEnabled = true 无效果
                hexCheck.isEnabled = false
                handler.postDelayed(hexClickRunnable, 300)
                val measureTimeMillis = measureTimeMillis {
                    putBoolean("hexCheck", hexCheck.isOpened)
                    if (hexCheck.isOpened) {
                        receiveEdit.setText(mReceiveBufferHex)

                        val hexString = mByteArray.toHexString()
                        with(sendEdit) {
                            keyListener = hexKeyListener
                            setText(hexString)
                            try {
                                setSelection(hexString.length)
                            } catch (e: IndexOutOfBoundsException) {
                                setSelection(hexString.length - 1)
                            }
                        }
                    } else {
                        receiveEdit.setText(mReceiveBuffer)
                        // val string = String(mByteArray)
                        if (mByteArray.isNotEmpty()) {
                            val string = mByteArray.getEncoding()
                            runOnUiThread {
                                with(sendEdit) {
                                    MsgLogger.e("onClick: 切换成普通的")
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
                                with(sendEdit) {
                                    MsgLogger.e("onClick: 切换成普通的")
                                    keyListener = TextKeyListener.getInstance()
                                }
                            }
                        }
                    }
                    receiveEdit.setSelection(receiveEdit.text.length, receiveEdit.text.length)
                }
                MsgLogger.e("耗时 => $measureTimeMillis")
            }
            R.id.send_new_line -> {
                putBoolean("sendNewLine", send_new_line.isOpened)
                if (send_new_line.isOpened) {
                    MsgLogger.e("send_new_line", "TRUE")
                } else {
                    MsgLogger.e("send_new_line", "FALSE")
                }
            }
        }
    }

    private fun initUi() {
        mReceiveBuffer.delete(0, mReceiveBuffer.length)
        mReceiveBufferHex.delete(0, mReceiveBufferHex.length)
        receiveEdit.text.clear()
        mReceiveByteCount = 0
        mSendFileByte = 0
        mReceivePackageCount = 0
        mSendByteCountSend = 0
        mSendPackageCountSend = 0

        receiveTextView.text = getString(R.string.receive, mReceiveByteCount, mReceivePackageCount)
        sendTextView.text = getString(R.string.send, mSendByteCountSend, mSendPackageCountSend)
        crcSend.text = getString(R.string.CRC, "00000000")
        crcReceive.text = getString(R.string.CRC, "00000000")
        // sendByteText.text = getString(R.string.sendByte, mSendFileByte)
        mSendCRC.reset()
        mReceiveCRC.reset()

        getBoolean("sendNewLine", false).let {
            send_new_line.isOpened = it
        }
    }

    private fun stopSend() {
        switchServiceButton.isEnabled = true
        sendFileButton.isEnabled = true
        if (mDeviceMode == Constant.BLE_MODE) {
            mBleApi.stopSend()
        } else {
            mSppApi.stopSend()
        }
        chronometer.stop()
    }


    private fun pauseSend() {
        switchServiceButton.isEnabled = true
        sendFileButton.isEnabled = true
        if (mDeviceMode == Constant.BLE_MODE) {
            mBleApi.pauseSend(address)
        } else {
            mSppApi.pauseSend(address)
        }
        chronometer.stop()
        mLastStopTime = SystemClock.elapsedRealtime()
    }

    private fun continueSend() {
        switchServiceButton.isEnabled = false
        sendFileButton.isEnabled = false
        if (mDeviceMode == Constant.BLE_MODE) {
            mBleApi.continueSend(address)
        } else {
            mSppApi.continueSend(address)
        }
        val intervalOnPause = SystemClock.elapsedRealtime() - mLastStopTime
        chronometer.base = chronometer.base + intervalOnPause
        chronometer.start()
    }


    companion object {
        fun activityStart(context: Context, fscDevice: FscDevice) {
            val intent = Intent(context, ThroughputActivity::class.java)
            intent.putExtra("FscDevice", fscDevice)
            context.startActivity(intent)
        }

        fun activityStart(context: Context, device: BluetoothDevice) {
            val intent = Intent(context, ThroughputActivity::class.java)
            intent.putExtra("BluetoothDevice", device)
            context.startActivity(intent)
        }

        const val SELECT_FILE = 0x0001
    }
}