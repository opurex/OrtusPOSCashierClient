package com.feasycom.feasyblue.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.feasycom.ble.controler.FscBleCentralApi
import com.feasycom.ble.controler.FscBleCentralApiImp
import com.feasycom.ble.controler.FscBleCentralCallbacksImp
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.Constant
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dao.DeviceDatabase
import com.feasycom.feasyblue.dialog.DfuNameDialogFragment
import com.feasycom.feasyblue.dialog.ProgressBarDialogFragment
import com.feasycom.feasyblue.utils.*
import com.feasycom.network.DeviceNetwork
import com.feasycom.ota.utils.FileUtil
import com.feasycom.ota.utils.OtaUtils
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_BEGIN
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_FAILED
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_FINISH
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_PROCESSING
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_VERIFY_APP_VERSION_FAILED
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_VERIFY_FAILED
import com.feasycom.ota.utils.XmodemUtils.Companion.OTA_STATUS_VERIFY_MODEL_FAILED
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import com.feasycom.spp.controler.FscSppCentralCallbacksImp
import kotlinx.android.synthetic.main.activity_ota.*
import kotlinx.android.synthetic.main.activity_ota.toolbar
import kotlinx.android.synthetic.main.activity_ota.toolbarTitle
import kotlinx.android.synthetic.main.activity_ota.view.*
import kotlinx.android.synthetic.main.activity_throughput.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.*
import java.net.UnknownHostException
import java.text.DecimalFormat

class OtaActivity : BaseActivity(), View.OnClickListener {

    lateinit var mFscDevice: FscDevice
    lateinit var mSppCentralApi: FscSppCentralApi
    lateinit var mBleCentralApi: FscBleCentralApi

    private val mFileNameLiveData = MutableLiveData<String>()
    private val mFileUriLiveData = MutableLiveData<Uri>()
    private val mFileByteArrayLiveData = MutableLiveData<ByteArray>()

    private var mProgress = ProgressBarDialogFragment()

    private var dialog: androidx.appcompat.app.AlertDialog? = null

    private var isUpgrading = false

    private val mHandler = Handler(Looper.getMainLooper()) { msg ->

        /**
         * @param msg A [Message][android.os.Message] object
         * @return True if no further handling is desired
         */
        when (msg.what) {
            START_DOWNLOAD -> {
                mProgress.show(supportFragmentManager, "ota")
            }
            DOWNLOAD_FINISH -> {
                // 下载完成
                mProgress.dismiss()
            }
            SHOW_DIALOG -> {
                mProgress.dismiss()
                // 提示
                showDialog(msg.data.getString("message", ""))
            }
            UPDATE_FAILED -> {
                otaState.text = getString(R.string.updateFailed)
                timeCount.stop()
            }
        }
        false
    }

    private val deviceDao by lazy {
        DeviceDatabase.getDataBase(this).deviceDao()
    }

    @SuppressLint("StringFormatInvalid")
    override fun initView() {
        intent?.getParcelableExtra<FscDevice>("FscDevice")?.let {
            mFscDevice = it
        } ?: finish()

        if (!::mFscDevice.isInitialized) {
            finish()
        }
        initToolbar()
        initSDK()
        setEvent()
        setObserve()
    }

    private fun initSDK() {
        if (mFscDevice.mode == Constant.BLE_MODE) {
            mBleCentralApi = FscBleCentralApiImp.getInstance()
            mBleCentralApi.setCallbacks(object : FscBleCentralCallbacksImp() {
                override fun otaProgressUpdate(
                    address: String,
                    percentage: Float,
                    status: Int
                ) { // 升级进度
                    uiOTAProgressUpdate(percentage, status)
                }

                override fun packetReceived(
                    address: String,
                    strValue: String,
                    dataHexString: String,
                    data: ByteArray
                ) {
                    uiReceived(strValue)
                }
            })
            clear.visibility = View.GONE
        } else {
            mSppCentralApi = FscSppCentralApiImp.getInstance()
            mSppCentralApi.setCallbacks(object : FscSppCentralCallbacksImp() {
                override fun otaProgressUpdate(address: String?, percentage: Float, status: Int) {
//                    MsgLogger.e("SPP升级进度 mSppCentralApi address => $address \r\n percentage => $percentage \r\n status => $status")
                    uiOTAProgressUpdate(percentage, status)
                }

                override fun packetReceived(
                    address: String,
                    strValue: String,
                    hexString: String,
                    data: ByteArray
                ) {
                    uiReceived(strValue)
                }

                override fun sppPeripheralDisconnected(address: String?) {
                }

                override fun bondIng() {
                    startOtaButton.isEnabled = false
                }
            })
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbarTitle.setText(R.string.OTAUpgrade)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            disconnect()
            finish()
        }
    }

    private fun setEvent() {
        selectFileButton.setOnClickListener(this)
        downloadButton.setOnClickListener(this)
        startOtaButton.setOnClickListener(this)
    }

    private fun getFileUri() {
        getStr("fileUri", "").let {
            if (it.isNotEmpty()) {
                mFileUriLiveData.value = Uri.parse(it)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setObserve() {
        mFileUriLiveData.observe(this) {
            putStr("fileUri", it.toString())
            getUriInfo(it)
        }
        mFileNameLiveData.observe(this) {
            fileName.text = it
        }
        mFileByteArrayLiveData.observe(this) {
            file_size_tv.text = getString(R.string.file_size, it.size)
                getDfuFileInformation(it)?.let { dfuFileInfo ->
                    dfuBootloader.text = Integer.valueOf(dfuFileInfo.bootloader).toString()
                    dfuVersion.text = "${Integer.valueOf(dfuFileInfo.versionStart)}-${Integer.valueOf(dfuFileInfo.version_soft_end)}"
                    MsgLogger.e("setObserve => " + dfuFileInfo.type_model)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val deviceInfo = deviceDao.queryDeviceByNumber(dfuFileInfo.type_model)
                        withContext(Dispatchers.Main) {
                            dfuModelName.text = deviceInfo?.name ?: "unknown"
                            if (!isUpgrading) {
                                startOtaButton.isEnabled = true
                                otaState.text = getString(R.string.please_start_the_upgrade)
                            }
                        }
                    }
                } ?: let {
                    otaState.text = getString(R.string.dfu_error)
                    startOtaButton.isEnabled = false
                    dfuModelName.text = "unknown"
                    dfuVersion.text = "unknown"
                    dfuBootloader.text = "unknown"
                }
        }
    }

    @SuppressLint("StringFormatInvalid")
    fun uiOTAProgressUpdate(percentage: Float, status: Int) {
        runOnUiThread {
            if (percentage > 0) {
                otaProgress.progress = percentage.toInt()
                val df = if (percentage<1){
                    DecimalFormat("0.00")
                }else {
                    DecimalFormat("#.00")
                }
                progressCount.text = "${df.format(percentage)}"
            }
            when (status) {
                OTA_STATUS_FINISH -> {// todo 10086
                    runOnUiThread {
                        otaState.text = getString(R.string.updateSuccess)
                        if (clear.isChecked) {
                            // 清除配对记录
                            mSppCentralApi.clearDevice(mFscDevice.address)
                        }
                        timeCount.stop()
                        disconnect()
                        updateClickStatus()
                    }
                    isUpgrading = false
                }
                OTA_STATUS_FAILED -> {// todo 120
                    otaState.text = getString(R.string.updateFailed)
                    timeCount.stop()
                    updateClickStatus()
                    isUpgrading = false
                }
                OTA_STATUS_BEGIN -> {// todo 110
                    otaState.text = getString(R.string.waitingForUpdate)
                    MsgLogger.e("uiOTAProgressUpdate => 开始升级")
                    isUpgrading = true
                }
                OTA_STATUS_PROCESSING -> {// todo 121
                    otaState.text = getString(R.string.updating)
                }
                OTA_STATUS_VERIFY_MODEL_FAILED -> {// todo 124
                    otaState.text = getString(R.string.nameNotMatch)
                    timeCount.stop()
                    updateClickStatus()
                }
                OTA_STATUS_VERIFY_APP_VERSION_FAILED -> {// todo 125
                    otaState.text = getString(R.string.versionIsLow)
                    timeCount.stop()
                    updateClickStatus()
                }
                OTA_STATUS_VERIFY_FAILED -> {// todo 122
                    otaState.text = getString(R.string.versionIsHigh)
                    timeCount.stop()
                    updateClickStatus()
                }
            }
        }
    }

    private fun updateClickStatus(){
        startOtaButton.isEnabled = true
        selectFileButton.isEnabled = true
        downloadButton.isEnabled = true
    }

    fun uiReceived(strValue: String) {
        if (strValue.contains("OK")) {
            runOnUiThread {
                if (strValue.length >= 15) {
                    val moduleVersionInt =
                        Integer.valueOf(FileUtil.stringToInt(strValue.substring(3, 7)))
                    val moduleBootloaderInt =
                        Integer.valueOf(FileUtil.stringToInt(strValue.substring(7, 11)))
                    moduleVersion.text =
                        moduleVersionInt.toString()
                    moduleBootloader.text =
                        moduleBootloaderInt.toString()

                    lifecycleScope.launch(Dispatchers.IO) {
                        val deviceInfo = deviceDao.queryDeviceByNumber(
                            stringToInt(strValue.substring(11, 15))
                        )
                        withContext(Dispatchers.Main) {
                            moduleModelName.text = deviceInfo?.name ?: "unknown"
                            /*// TODO("判读条件是否要升级")
                            if (moduleModelName.text != "-" && moduleModelName.text.toString() != dfuModelName.text.toString()){
                                // 模块型号不符
                            }else if ((moduleVersionInt < Integer.valueOf(dfuVersion.text.toString())) || moduleBootloaderInt < Integer.valueOf(dfuBootloader.text.toString())){
                            }*/
                        }
                    }
                } else {
                    moduleVersion.text = "unknown"
                    moduleBootloader.text = "unknown"
                    moduleModelName.text = "unknown"
                }
            }
        }
    }


    private fun showDialog(message: String) {
        val alertDialog =
            AlertDialog.Builder(this@OtaActivity)
                .setTitle(R.string.error)
                .setInverseBackgroundForced(false)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton(R.string.ok, null)
                .create()
        alertDialog.show()
    }

    private fun getUriInfo(uri: Uri) {
        when (uri.scheme) {
            "content" -> {
                try {
                    contentResolver.query(uri, null, null, null, null)?.use {
                        if (it.moveToFirst()) {
                            contentResolver.openInputStream(uri)?.use {
                                mFileByteArrayLiveData.postValue(it.readBytes())
                            }
                            val fileName = it.getString(
                                it.getColumnIndexOrThrow(
                                    MediaStore.Downloads.DISPLAY_NAME
                                )
                            )
//                            MsgLogger.d(TAG,"SPP getUriInfo fileName => $fileName")
                            mFileNameLiveData.postValue(
                                fileName
                            )
                        }
                    }
                } catch (e: Exception) {
                    MsgLogger.e("getUriInfo => ${e.message}")
                }
            }
            "file" -> {
                try {
                    uri.toFile().let {
                        mFileNameLiveData.postValue(it.name)
                        mFileByteArrayLiveData.postValue(it.readBytes())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getDfuFileInformation(dfuFile: ByteArray) = OtaUtils.checkDfuFile(dfuFile)


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
            intent,
            READ_FILE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != READ_FILE_REQUEST_CODE || resultCode != RESULT_OK) {
            return
        }
        data?.data?.let {
            if (it.path != null) {
                mFileUriLiveData.postValue(it)
            }
        }
    }

    override fun getLayout() = R.layout.activity_ota

    companion object {
        private const val TAG = "OtaActivity"
        private const val START_DOWNLOAD = 0x0001
        private const val DOWNLOAD_FINISH = 0x0002
        private const val SHOW_DIALOG = 0x0003
        private const val UPDATE_FAILED = 0x0004
        private const val READ_FILE_REQUEST_CODE = 42

        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun activityStart(context: Context, fscDevice: FscDevice) {
            Intent(context, OtaActivity::class.java).apply {
                putExtra("FscDevice", fscDevice)
                context.startActivity(this)
            }
        }
    }

    private suspend fun downloadDFU(parameter: String) {
        coroutineScope {
            deleteExistsFile(parameter)
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
                            MediaStore.Downloads.DISPLAY_NAME,
                            "${parameter}.dfu"
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
                                mFileUriLiveData.postValue(it)
                            }
                            runOnUiThread {
                                Toast.makeText(
                                    this@OtaActivity,
                                    getString(R.string.down_file_finish),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH)
                        }
                        return@coroutineScope
                    } else {
                        val directoryPath =
                            "${File.separator}sdcard${File.separator}Download${File.separator}dfu${File.separator}"
                        MsgLogger.d(TAG,"SPP downloadDFU directoryPath => $directoryPath")
                        val directory = File(directoryPath)
                        if (!directory.exists()) {
                            directory.mkdirs()
                        }
                        val replaceParameter = parameter.replace("/","_")
                        val path = "${directoryPath}${replaceParameter}.dfu"
                        MsgLogger.d(TAG,"SPP downloadDFU path => $path")
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
                                    this@OtaActivity,
                                    getString(R.string.down_file_finish),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH)
                            mFileUriLiveData.postValue(file.toUri())
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
                mHandler.sendMessage(message)
                MsgLogger.d(TAG,"SPP downloadDFU HttpException Exception e => ${e.message}")
            } catch (e: IOException) {
                val message = Message()
                message.what = SHOW_DIALOG
                message.data.putString("message", getString(R.string.io_error))
                mHandler.sendMessage(message)
                MsgLogger.d(TAG,"SPP downloadDFU IOException Exception e => ${e.message}")
            } catch (e: UnknownHostException) {
                val message = Message()
                message.what = SHOW_DIALOG
                message.data.putString("message", getString(R.string.network_error))
                mHandler.sendMessage(message)
                MsgLogger.d(TAG,"SPP downloadDFU UnknownHostException Exception e => ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                MsgLogger.d(TAG,"SPP downloadDFU Exception e => ${e.message}")
            }
        }
    }


    private fun deleteExistsFile(parameter: String) {
//        MsgLogger.d(TAG,"SPP deleteExistsFile parameter => $parameter")
        val replaceParameter = parameter.replace("/","_")
//        MsgLogger.d(TAG,"SPP deleteExistsFile replaceParameter => $replaceParameter")
        if (isExternalStorageLegacy()) {
            val externalContentUri = MediaStore.Downloads.getContentUri("external")
            val selection = MediaStore.Downloads.DISPLAY_NAME + "=?"
            val arg = arrayOf("$replaceParameter.dfu")
//            MsgLogger.e(TAG,"externalContentUri => $externalContentUri  selection=> $selection")
//            for (i in arg.indices) {
//                MsgLogger.e(TAG,"arg[$i] => ${arg[i]}")
//            }
            val cursor = contentResolver.query(
                externalContentUri,
                null,
                selection,
                arg,
                null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndexOrThrow = it.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                    val queryId = it.getLong(columnIndexOrThrow)
                    val uri = ContentUris.withAppendedId(externalContentUri, queryId)
                    contentResolver.delete(uri, null, null)
                    MsgLogger.e("SPP deleteExistsFile => 删除是分区存储的固件")
                }
            }
        } else {
            // 不是分区存储
            val path = "${File.separator}sdcard${File.separator}Download${File.separator}dfu${File.separator}${replaceParameter}"
            MsgLogger.d(TAG,"SPP deleteExistsFile path => $path")
            val file = File(path)
            val exists = file.exists()
            MsgLogger.d(TAG,"SPP deleteExistsFile exists => $exists")
            if (exists) {
                file.delete()
                MsgLogger.e("SPP deleteExistsFile => 删除不是分区存储的固件")
            }
        }
    }

    private fun checkPermission() {
        // 检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            if (dialog != null) {
                dialog!!.dismiss()
                dialog = null
            }
            dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.tips)) //设置标题
                .setMessage(resources.getString(R.string.file_access_permissions))
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                    dialog.dismiss()
                    ActivityCompat.requestPermissions(
                        this@OtaActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                    )
                }.create()
            dialog!!.show()
        }else{
            createDfuDialog()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createDfuDialog()
                }
            }
        }
    }

    private fun createDfuDialog(){
        val dfuNameDialogFragment = DfuNameDialogFragment()
        dfuNameDialogFragment.onClickComplete = { dialog, parameter ->
            dialog.dismiss()
            if (parameter.isNotEmpty() && parameter.isNotBlank()) {
                mHandler.sendEmptyMessage(START_DOWNLOAD)
                lifecycleScope.launch(Dispatchers.IO) {
                    downloadDFU(parameter)
                }
            }
        }
        dfuNameDialogFragment.show(supportFragmentManager, "dfu")
    }

    private fun startOta() {
        mFileByteArrayLiveData.value?.let {
            try {
                if (mFscDevice.mode == Constant.BLE_MODE) {
                    mBleCentralApi.connectToOTAWithFactory(
                        mFscDevice.address,
                        it,
                        resetFlag.isChecked
                    )
                } else {
                    mSppCentralApi.connectToOTAWithFactory(
                        mFscDevice.address,
                        it,
                        resetFlag.isChecked
                    )
                }
            } catch (e: NullPointerException) {
                MsgLogger.e(TAG,"SPP startOta NullPointerException 升级文件有误 => ${e.message}")
                mHandler.sendEmptyMessage(UPDATE_FAILED)
            }
        }
    }

    private fun disconnect() {
        if (mFscDevice.mode == Constant.BLE_MODE) {
            mBleCentralApi.disconnect(mFscDevice.address)
        } else {
            mSppCentralApi.disconnect(mFscDevice.address)
        }
        mFileUriLiveData.value?.let {
            putStr("fileUri", it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgLogger.e("SPP onDestroy => 界面销毁当前蓝牙断开连接...")
        disconnect()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selectFileButton -> {
                performFileSearch()
                group.visibility = View.INVISIBLE
            }
            R.id.downloadButton -> {// 获取固件
                if (isExternalStorageLegacy()) {
                    createDfuDialog()
                    group.visibility = View.INVISIBLE
                } else {
                    checkPermission()
                    group.visibility = View.INVISIBLE
                }
            }
            R.id.startOtaButton -> {// 开始升级
                startOtaButton.isEnabled = false
                selectFileButton.isEnabled = false
                downloadButton.isEnabled = false

                startOta()
                group.visibility = View.VISIBLE

                timeCount.base = SystemClock.elapsedRealtime()//计时器清零
                val hour = ((SystemClock.elapsedRealtime() - timeCount.base) / 1000 / 60)
                timeCount.format = "0${hour}:%s"
                timeCount.start()
                otaState.text = getString(R.string.waitingForUpdate)
                otaProgress.progress = 0
                progressCount.text = getString(R.string.upgrade_percentage, 0)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            disconnect()
            startActivity(Intent(this, OtaSearchDeviceActivity::class.java))
            finish()
            false
        } else {
            super.onKeyDown(keyCode, event)
        }
    }


    override fun onResume() {
        super.onResume()
        getFileUri()
    }
}
