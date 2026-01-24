package com.feasycom.feasyblue.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.bean.ConnectType
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.DfuNameDialogFragment
import com.feasycom.feasyblue.dialog.ProgressBarDialogFragment
import com.feasycom.feasyblue.utils.*
import com.feasycom.network.DeviceNetwork
import com.feasycom.ota.utils.XmodemUtils
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_AT_FAILED
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_BEGIN
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_FAILED
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_FINISH
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_PROCESSING
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_VERIFY_FAILED
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import com.feasycom.spp.controler.FscSppCentralCallbacksImp
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_batch_update.*
import kotlinx.android.synthetic.main.activity_batch_update.clear
import kotlinx.android.synthetic.main.activity_batch_update.downloadButton
import kotlinx.android.synthetic.main.activity_batch_update.fileName
import kotlinx.android.synthetic.main.activity_batch_update.group
import kotlinx.android.synthetic.main.activity_batch_update.otaProgress
import kotlinx.android.synthetic.main.activity_batch_update.otaState
import kotlinx.android.synthetic.main.activity_batch_update.progressCount
import kotlinx.android.synthetic.main.activity_batch_update.resetFlag
import kotlinx.android.synthetic.main.activity_batch_update.selectFileButton
import kotlinx.android.synthetic.main.activity_batch_update.startOtaButton
import kotlinx.android.synthetic.main.activity_batch_update.timeCount
import kotlinx.android.synthetic.main.activity_batch_update.toolbar
import kotlinx.android.synthetic.main.activity_batch_update.toolbarSubtitle
import kotlinx.android.synthetic.main.activity_batch_update.toolbarTitle
import kotlinx.android.synthetic.main.activity_ota.*
import kotlinx.android.synthetic.main.fragment_communication.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.*
import java.net.UnknownHostException
import java.text.DecimalFormat

class BatchOtaActivity : BaseActivity(), View.OnClickListener {

    lateinit var mSppCentralApi: FscSppCentralApi

    private var filterRssi: Int = -50
    private var filterNameSwitch: Boolean = false
    private var filterName: String = ""

    private val fileNameLiveData = MutableLiveData<String>()
    private val fileUriLiveData = MutableLiveData<Uri>()
    private val fileByteArrayLiveData = MutableLiveData<ByteArray>()

    private val mFailedDevices: HashSet<String> = HashSet<String>()

    private val mSuccessDevices: HashSet<String> = HashSet()

    var progress = ProgressBarDialogFragment()

    var mFscDevice: FscDevice? = null

    var mMessage: SpannableStringBuilder = SpannableStringBuilder()

    var isStop = true

    var isUpdate = false

    var isAllowUpdate = false

    // var updateNumber = 0;

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        /**
         * @param msg A [Message][android.os.Message] object
         * @return True if no further handling is desired
         */
        when (msg.what) {
            START_DOWNLOAD -> {
                // 开始下载
                progress.show(supportFragmentManager, "login")
            }
//            DOWNLOAD_FULFIL -> {
//                // 下载完成
//                progress.dismiss()
//            }
            SHOW_DIALOG -> {
                progress.dismiss()
                // 显示下载失败的提示
                showDialog(msg.data.getString("message", ""))
            }
            DOWNLOAD_FINISH -> {
                // 下载完成
                progress.dismiss()
            }
        }
        false
    }

    val mStartScanner = Runnable { startSppScan() }

    private fun showDialog(message: String) {
        val alertDialog =
            AlertDialog.Builder(this).setTitle(R.string.error).setInverseBackgroundForced(false)
                .setMessage(message).setIcon(R.mipmap.ic_launcher)
                .setNegativeButton(R.string.ok, null).create()
        alertDialog.show()
    }

    private var mUpdateSuccessTotal = 0

    private var mUpdateSuccessNumber = 0


    override fun initView() {
        initToolbar()
        isAllowUpdate = getBoolean("isAllowUpdate", false)
        val str = getStrSet("devices", HashSet())
        mSuccessDevices.addAll(str)
        mMessage.clear()
        val log = getStr("log", "")
        if (log.isNullOrBlank()) {
            mMessage.append(log)
        }

        upgrade_log_tv.text = mMessage
        initSDK()
        initEvent()
        initObserve()
        fileUriLiveData.value = Uri.parse(getStr("fileUri", ""))


        // updateNumber = getInt("update_number")
    }

    private fun initObserve() {
        fileUriLiveData.observe(this) {
            otaState.text = getString(R.string.waitingForUpdate)
            putStr("fileUri", it.toString())
            PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                    scope.showRequestReasonDialog(
                        deniedList, getString(R.string.permission_write_prompt), getString(
                            R.string.ok
                        ), getString(R.string.close)
                    )
                }.onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList, getString(R.string.to_setting_open_permission), getString(
                            R.string.ok
                        ), getString(R.string.close)
                    )
                }.request { allGranted, _, _ ->
                    if (allGranted) {
                        getUriInfo(it)
                    }
                }
        }

        fileNameLiveData.observe(this) {
            fileName.text = it
        }

        fileByteArrayLiveData.observe(this) {
            startOtaButton.isEnabled = true
        }
    }

    private fun getUriInfo(uri: Uri) {
        when (uri.scheme) {
            "content" -> {
                try {
                    contentResolver.query(uri, null, null, null, null)?.use {
                        MsgLogger.e("getUriInfo: " + it.moveToFirst())
                        if (it.moveToFirst()) {
                            fileNameLiveData.postValue(
                                it.getString(
                                    it.getColumnIndexOrThrow(
                                        MediaStore.Downloads.DISPLAY_NAME
                                    )
                                )
                            )
                            contentResolver.openInputStream(uri)?.use {
                                fileByteArrayLiveData.value = it.readBytes()
                            } ?: MsgLogger.e("空")
                        }
                    }
                } catch (e: Exception) {
                }
            }
            "file" -> {
                try {
                    uri.toFile().let {
                        fileNameLiveData.postValue(it.name)
                        fileByteArrayLiveData.postValue(it.readBytes())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getLayout() = R.layout.activity_batch_update

    private fun initSDK() {
        mSppCentralApi = FscSppCentralApiImp.getInstance()
        mSppCentralApi.initialize()
        mSppCentralApi.stopScan()
        mSppCentralApi.setCallbacks(object : FscSppCentralCallbacksImp() {
            @SuppressLint("SetTextI18n", "MissingPermission")
            override fun otaProgressUpdate(address: String?, percentage: Float, status: Int) {
                runOnUiThread {
                    if (percentage > 0) {
                        otaProgress.progress = percentage.toInt()
                        val df = if (percentage < 1) {
                            DecimalFormat("0.00")
                        } else {
                            DecimalFormat("#.00")
                        }
                        progressCount.text = "${df.format(percentage)}"
                    }
                    when (status) {
                        OTA_STATUS_FINISH -> {
                            isUpdate = false
                            mUpdateSuccessTotal++
                            mUpdateSuccessNumber++
                            putInt("update_success_number", mUpdateSuccessTotal)
                            toolbarSubtitle.text =
                                getString(R.string.number_upgrades, mUpdateSuccessTotal)
                            otaState.text = getString(R.string.updateSuccess)
                            timeCount.stop()
                            if (clear.isChecked) {
                                // 清除配对记录
                                mSppCentralApi.clearDevice(mFscDevice?.address)
                            }
                            disconnect()
                            // TODO:
                            mMessage.append(SpannableString("${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n升级成功\n").apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#000000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                upgrade_log_tv.text = this
                            }
                            group.visibility = View.INVISIBLE
                            mFscDevice?.address?.let {
                                mSuccessDevices.add(it)
                                mFscDevice = null
                            }
                            isStop = false
                            startSppScan()
                        }
                        OTA_STATUS_FAILED -> {
                            MsgLogger.e("otaProgressUpdate => 批量升级失败.")
                            isUpdate = false
                            otaState.text = getString(R.string.updateFailed)
                            timeCount.stop()
                            if (mFscDevice == null) {
                                startSppScan()
                            }
                            // TODO:
                            mMessage.append(SpannableString("${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n升级失败\n").apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                upgrade_log_tv.text = this
                            }
                            group.visibility = View.INVISIBLE
                            mFscDevice?.address?.let {
                                mFailedDevices.add(it)
                                mFscDevice = null
                            }
                            isStop = false
                            startSppScan()
                        }
                        OTA_STATUS_AT_FAILED -> {
                            isUpdate = false
                            otaState.text = getString(R.string.updateFailed)
                            timeCount.stop()

                            // TODO:
                            mMessage.append(SpannableString("${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n无法查询到模块信息\n").apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                upgrade_log_tv.text = this
                            }
                            group.visibility = View.INVISIBLE
                            isStop = false
                            startSppScan()
                        }
                        OTA_STATUS_VERIFY_FAILED -> {
                            isUpdate = false
                            otaState.text = getString(R.string.updateFailed)
                            timeCount.stop()
                            // TODO:
                            mMessage.append(SpannableString("${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n模块版本不符\n").apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                upgrade_log_tv.text = this
                            }
                            group.visibility = View.INVISIBLE
                            isStop = false
                            startSppScan()
                        }
                        OTA_STATUS_BEGIN -> {
                            isUpdate = true
                            otaState.text = getString(R.string.waitingForUpdate)
                            otaProgress.progress = 0
                            group.visibility = View.VISIBLE
                            startTimer()
                        }
                        OTA_STATUS_PROCESSING -> {
                            otaState.text = getString(R.string.updating)
                        }
                        0x0FFF -> {
                            mMessage.append(SpannableString("${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n升级失败\n模组需要重新上电\n").apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                mFscDevice?.address?.let {
                                    mFailedDevices.add(it)
                                    mFscDevice = null
                                }
                                upgrade_log_tv.text = this
                                group.visibility = View.INVISIBLE
                                isStop = false
                                startSppScan()
                            }
                        }
                        XmodemUtils.Companion.OTA_STATUS_PROCESSING -> {
                            otaState.text = getString(R.string.updating)
                        }
                        XmodemUtils.Companion.OTA_STATUS_VERIFY_MODEL_FAILED -> {

                            mMessage.append(SpannableString(
                                "${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n升级失败\n${
                                    getString(
                                        R.string.nameNotMatch
                                    )
                                }\n"
                            ).apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                mFscDevice?.address?.let {
                                    mFailedDevices.add(it)
                                    mFscDevice = null
                                }
                                upgrade_log_tv.text = this
                                group.visibility = View.INVISIBLE
                                isStop = false
                                startSppScan()
                            }
                        }
                        XmodemUtils.Companion.OTA_STATUS_VERIFY_APP_VERSION_FAILED -> {
                            mMessage.append(SpannableString(
                                "${mFscDevice?.device?.name}  -  ${mFscDevice?.address}\n升级失败\n${
                                    getString(
                                        R.string.versionIsLow
                                    )
                                }\n"
                            ).apply {
                                setSpan(
                                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                                    0,
                                    length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }).apply {
                                mFscDevice?.address?.let {
                                    mFailedDevices.add(it)
                                    mFscDevice = null
                                }
                                upgrade_log_tv.text = this
                                group.visibility = View.INVISIBLE
                                isStop = false
                                startSppScan()
                            }
                        }
                    }
                    scrollView.post { scrollView.scrollTo(0, 1000) }
                }
            }

            override fun packetReceived(
                address: String, strValue: String, hexString: String, data: ByteArray
            ) {

            }

            override fun sppPeripheralDisconnected(address: String?) {
                MsgLogger.d("sppPeripheralDisconnected => 断开连接.")
                isStop = false
            }

            override fun sppPeripheralConnected(device: BluetoothDevice?, type: ConnectType?) {
                super.sppPeripheralConnected(device, type)
                MsgLogger.d("sppPeripheralDisconnected => 连接成功.")
            }

            @SuppressLint("MissingPermission")
            override fun sppPeripheralFound(sppDevice: FscDevice, rssi: Int) {
                super.sppPeripheralFound(sppDevice, rssi)
                if (isStop) {
                    return
                }
                MsgLogger.d("sppPeripheralFound => ${sppDevice.address}")
                if (sppDevice.device == null) {
                    MsgLogger.d("sppPeripheralFound => device is null")
                    return
                }
                if (sppDevice.device.name == null) {
                    MsgLogger.d("sppPeripheralFound => device name is null")
                    return
                }
                if (sppDevice.device.name.lowercase().contains("Feasycom".lowercase())) {
                    MsgLogger.d("sppPeripheralFound: name => ${sppDevice.device.name}  address  =>  ${sppDevice.device.address}   rssi  => $rssi")
                }

                if (!((rssi >= filterRssi) && (rssi <= -30))) {
                    MsgLogger.d("sppPeripheralFound =>  信号值不符   rssi  =>  $rssi    filterRssi  =>   $filterRssi")
                    return
                }

                mFailedDevices.filter {
                    it == sppDevice.address
                }.let {
                    if (it.isNotEmpty()) {
                        runOnUiThread {
                            ToastUtil.show(
                                this@BatchOtaActivity, "扫描到升级失败的设备  ${sppDevice.address}"
                            )
                        }
                        return
                    }
                }
                mSuccessDevices.filter {
                    it == sppDevice.address
                }.let {
                    if (it.isNotEmpty() && !isAllowUpdate) {
                        runOnUiThread {
                            ToastUtil.show(
                                this@BatchOtaActivity, "扫描到已升级过的设备  ${sppDevice.address}"
                            )
                        }
                        return
                    }
                }

                if (filterName.isBlank()) {
                    filterName = "Feasycom"
                }

                if (sppDevice.device.name.lowercase().contains(filterName.lowercase())) {
                    MsgLogger.d("过滤条件  name => $filterName   rssi  => $filterRssi     扫描到符合条件的设备 => ${sppDevice.device.name}   ${sppDevice.device.address}  $rssi")
                    mFscDevice = sppDevice
                    fileByteArrayLiveData.value?.let {
                        otaState.text = getString(R.string.connecting)
                        handler.removeCallbacks(mStartScanner)
                        startTimer()
                        MsgLogger.e("sppPeripheralFound => 开始连接")
                        isStop = true
                        isAllowUpdate = false
                        mSppCentralApi.connectToVerifyOTAWithFactory(
                            sppDevice.address, it, resetFlag.isChecked, checkModuleVersion.isChecked
                        )
                    }
                }
            }

            override fun startScan() {
                super.startScan()
                otaState.text = getString(R.string.searching)
            }

            override fun stopScan() {
                super.stopScan()
                if (!isStop) {
                    startSppScan()
                }
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbarTitle.setText(R.string.OTABatchUpgrade)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            disconnect()
            handler.removeCallbacks(mStartScanner)
            mSppCentralApi.stopScan()
            finish()
        }
        mUpdateSuccessTotal = getInt("update_success_number")
        toolbarSubtitle.text = getString(R.string.number_upgrades, mUpdateSuccessTotal)
    }

    private fun initEvent() {
        selectFileButton.setOnClickListener(this)
        downloadButton.setOnClickListener(this)
        startOtaButton.setOnClickListener(this)

        copy_log_btn.setOnClickListener {
            ToastUtil.show(this, getString(R.string.copy_all))
            copyText(upgrade_log_tv.text.toString())
        }

        batch_filter_tv.setOnClickListener {
            BatchFilterActivity.activityStart(this)
        }

        clear_cache_btn.setOnClickListener {
            DataCleanManagerUtil.clearIntExtCache(this)
            ToastUtil.show(this, getString(R.string.clear_cache))
            mUpdateSuccessTotal = 0
            putInt("update_success_number", mUpdateSuccessTotal)
            toolbarSubtitle.text = getString(R.string.number_upgrades, mUpdateSuccessTotal)
            isAllowUpdate = true
            putBoolean("isAllowUpdate", true)
            mMessage.clear()
            upgrade_log_tv.text = mMessage
        }

        delete_history_btn.setOnClickListener {
            /*val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TITLE, "FeasyBlue")
            startActivityForResult(intent, 2011)*/
            ToastUtil.show(this, getString(R.string.clear_device_record))
            putStrSet("devices", HashSet<String>())
            mSuccessDevices.clear()
            mFailedDevices.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        filterRssi = getInt("batchRssiValue", -60)
        filterNameSwitch = getBoolean("batchNameSwitch", false)
        filterName = if (filterNameSwitch) {
            getStr("batchNameValue", "")
        } else {
            ""
        }

        if (filterRssi > -60 || filterNameSwitch) {
            batch_filter_iv.setImageResource(R.drawable.icon_filtter_ing)
        } else {
            batch_filter_iv.setImageResource(R.drawable.icon_filtter_none)
        }
    }

    private fun disconnect() {
        mSppCentralApi.disconnect()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selectFileButton -> {
                PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                        scope.showRequestReasonDialog(
                            deniedList, getString(R.string.permission_write_prompt), getString(
                                R.string.ok
                            ), getString(R.string.close)
                        )
                    }.onForwardToSettings { scope, deniedList ->
                        scope.showForwardToSettingsDialog(
                            deniedList, getString(R.string.to_setting_open_permission), getString(
                                R.string.ok
                            ), getString(R.string.close)
                        )
                    }.request { allGranted, _, _ ->
                        if (allGranted) {
                            performFileSearch()
                        }
                    }
            }
            R.id.downloadButton -> {
                initPermission()
            }
            R.id.startOtaButton -> {
                if (startOtaButton.text == getString(R.string.start_batch_upgrade)) {
                    selectFileButton.isEnabled = false
                    downloadButton.isEnabled = false
                    isStop = false
                    startOtaButton.text = getString(R.string.stop_batch_upgrade)
                    if (!isUpdate) {
                        startSppScan()
                        otaState.text = getString(R.string.waitingForUpdate)
                        otaProgress.progress = 0
                        progressCount.text = getString(R.string.upgrade_percentage, 0)
                    }
                } else {
                    isStop = true
                    mSppCentralApi.stopScan()
                    selectFileButton.isEnabled = true
                    downloadButton.isEnabled = true
                    startOtaButton.text = getString(R.string.start_batch_upgrade)
                }
            }
        }
    }

    private fun startTimer() {
        timeCount.base = SystemClock.elapsedRealtime()   //计时器清零
        val hour = ((SystemClock.elapsedRealtime() - timeCount.base) / 1000 / 60)
        timeCount.format = "0${hour}:%s"
        timeCount.start()
    }


    private fun startSppScan() {
        /*if (!isStop){
            handler.postDelayed(mStartScanner, 15000)
            mSppCentralApi.startScan()
        }*/
        if (!isStop) {
            mSppCentralApi.startScan()
        }
    }


    /**
     * Fires an intent to spin up the "file chooser" UI to select a file
     */
    private fun performFileSearch() {
        val intent: Intent = if (isKitkatOrAbove()) {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/octet-stream"

        startActivityForResult(
            intent, READ_FILE_REQUEST_CODE
        )
    }


    private fun initPermission() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(
                    deniedList, getString(R.string.permission_write_prompt), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList, getString(R.string.to_setting_open_permission), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.request { allGranted, _, _ ->
                if (allGranted) {
                    val dfuNameDialogFragment = DfuNameDialogFragment()
                    dfuNameDialogFragment.onClickComplete = { dialog, parameter ->
                        dialog.dismiss()
                        if (parameter.isNotEmpty() && parameter.isNotBlank()) {
                            handler.sendEmptyMessage(START_DOWNLOAD)
                            lifecycleScope.launch(Dispatchers.IO) {
                                downloadDFU(parameter)
                            }
                        }
                    }
                    dfuNameDialogFragment.show(supportFragmentManager, "duf")
                }
            }
    }

    private suspend fun downloadDFU(parameter: String) {
        coroutineScope {
            val split = parameter.split("/")
            val fileName = split[split.size - 1]
            deleteExistsFile(fileName)
            try {
                DeviceNetwork.downloadDFU(parameter).apply {
                    if (isExternalStorageLegacy()) {
                        // 分区存储
                        val contentUri = MediaStore.Downloads.getContentUri("external")
                        val contentValues = ContentValues()
                        contentValues.put(
                            MediaStore.Downloads.RELATIVE_PATH,
                            Environment.DIRECTORY_DOWNLOADS + "/dfu"
                        )
                        contentValues.put(
                            MediaStore.Downloads.DISPLAY_NAME, "${fileName}.dfu"
                        )
                        launch(Dispatchers.IO) {
                            // 创建文件
                            contentResolver.insert(contentUri, contentValues)?.let {
                                contentResolver.openOutputStream(it)?.use { out ->
                                    val byteArray = ByteArray(1024)
                                    var len: Int
                                    while (byteStream().read(byteArray).also { len = it } != -1) {
                                        out.write(byteArray, 0, len)
                                    }
                                }
                                fileUriLiveData.postValue(it)
                            }
                            runOnUiThread {
                                Toast.makeText(
                                    this@BatchOtaActivity,
                                    getString(R.string.down_file_finish),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            handler.sendEmptyMessage(DOWNLOAD_FINISH)
                        }
                        return@coroutineScope
                    } else {
                        val directoryPath =
                            "${File.separator}sdcard${File.separator}Download${File.separator}dfu${File.separator}"
                        val directory = File(directoryPath)
                        if (!directory.exists()) {
                            directory.mkdirs()
                        }
                        val path = "${directoryPath}${fileName}.dfu"
                        val file = File(path)
                        launch(Dispatchers.IO) {
                            BufferedInputStream(byteStream()).use { input ->
                                BufferedOutputStream(FileOutputStream(file)).use { out ->
                                    var data: Int
                                    while (input.read().also { data = it } != -1) {
                                        out.write(data)
                                    }
                                }
                            }
                            runOnUiThread {
                                Toast.makeText(
                                    this@BatchOtaActivity,
                                    getString(R.string.down_file_finish),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            handler.sendEmptyMessage(DOWNLOAD_FINISH)
                            fileUriLiveData.postValue(file.toUri())
                        }
                    }
                }
            } catch (e: HttpException) {
                val message = Message()
                message.what = SHOW_DIALOG
                message.data.putString(
                    "message", when (e.code()) {
                        404 -> {
                            getString(R.string.down_file_error)
                        }
                        else -> {
                            getString(R.string.network_error)
                        }
                    }
                )
                handler.sendMessage(message)
            } catch (e: IOException) {
                val message = Message()
                message.what = SHOW_DIALOG
                message.data.putString("message", getString(R.string.io_error))
                handler.sendMessage(message)
            } catch (e: UnknownHostException) {
                val message = Message()
                message.what = SHOW_DIALOG
                message.data.putString("message", getString(R.string.network_error))
                handler.sendMessage(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteExistsFile(fileName: String) {
        if (isExternalStorageLegacy()) {
            val externalContentUri = MediaStore.Downloads.getContentUri("external")
            val selection = MediaStore.Downloads.DISPLAY_NAME + "=?"
            val arg = arrayOf("$fileName.dfu")
            val cursor = contentResolver.query(
                externalContentUri, null, selection, arg, null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndexOrThrow = it.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                    val queryId = it.getLong(columnIndexOrThrow)
                    val uri = ContentUris.withAppendedId(externalContentUri, queryId)
                    contentResolver.delete(uri, null, null)
                }
            }
            MsgLogger.e("deleteExistsFile => 删除现有文件")
        } else {
            // 不是分区存储
            val path =
                "${File.separator}sdcard${File.separator}Download${File.separator}dfu${File.separator}${fileName}"
            val file = File(path)
            val exists = file.exists()
            if (exists) {
                file.delete()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        MsgLogger.e("requestCode => $requestCode   resultCode => $resultCode")
        if (resultCode != RESULT_OK) {
            return
        } else if (requestCode == 2011) {
            if (data != null) {
                val uri = data.data
                uri?.let {
                    try {
                        contentResolver.openOutputStream(it)?.use {
                            it.write(mMessage.toString().toByteArray())
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (requestCode == READ_FILE_REQUEST_CODE) {
            data?.data?.let {
                if (it.path != null) {
                    fileUriLiveData.postValue(it)
                    // lastDevices.clear()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isStop = true
        putStrSet("devices", mSuccessDevices)
        putStr("log", upgrade_log_tv.text.toString())
    }

    companion object {
        private const val TAG = "BatchOtaActivity"
        private const val START_DOWNLOAD = 0X0001

        //        private const val DOWNLOAD_FULFIL = 0X0002
        private const val SHOW_DIALOG = 0x0003
        private const val DOWNLOAD_FINISH = 0x000444
        private const val READ_FILE_REQUEST_CODE = 42

        fun activityStart(context: Context) {
            val intent = Intent(context, BatchOtaActivity::class.java)
            context.startActivity(intent)
        }
    }

}