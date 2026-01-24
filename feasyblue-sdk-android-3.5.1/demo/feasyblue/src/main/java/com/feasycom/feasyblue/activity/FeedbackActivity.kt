package com.feasycom.feasyblue.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.InfoDialog
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.utils.FeasyCallbackSDK
import com.feasycom.feasyblue.utils.FeasyCallbackSDK.Companion.FEEDBACK_ADVICE
import com.feasycom.feasyblue.utils.FeasyCallbackSDK.Companion.FEEDBACK_BUG
import com.feasycom.feasyblue.utils.FeasyCallbackSDK.Companion.FEEDBACK_COOPERATION
import com.feasycom.feasyblue.utils.FeasyCallbackSDK.Companion.FEEDBACK_UI
import com.feasycom.feasyblue.utils.FileShareHelper
import com.feasycom.feasyblue.utils.ToastUtil
import com.feasycom.feasyblue.utils.getStr
import com.feasycom.feasyblue.utils.putStr
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class FeedbackActivity: BaseActivity(){

    var connectDialog: InfoDialog? = null

    var handler: Handler = Handler(Looper.getMainLooper())

    private var mPreferenceKey = "FEEDBACK_ADVICE"

    private lateinit var mMessageDialog: WFBaseDialog

    private val mNetworkIntentFilter: IntentFilter by lazy {
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    private val mNetworkReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                val wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                //如果无网络连接activeInfo为null
                val activeInfo = manager.activeNetworkInfo

                when {
                    wifiInfo!!.isConnected -> {
                        // wifi 网络
                    }
                    mobileInfo!!.isConnected -> {
                        // 手机网络
                    }
                    null == activeInfo -> {
                        // 没有网络
                        mResultDialog = AlertDialog.Builder(this@FeedbackActivity)
                            .setMessage(getString(R.string.no_network))
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                        mResultDialog.setCanceledOnTouchOutside(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initView() {
        registerReceiver(mNetworkReceiver, mNetworkIntentFilter)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.feedback_title)
        deviceData.setText(getStr(mPreferenceKey))
        deviceData.setSelection(deviceData.text.toString().length)
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

        submit.setOnClickListener {
            if (deviceData.text.toString().isNotBlank()) {
                connectDialog = InfoDialog(this, getString(R.string.feedback_status))
                connectDialog?.show()

                handler.postDelayed({
                    connectDialog?.let {
                        if (it.isShowing) {
                            it.dismiss()
                        }
                    }
                }, 10000)

                FeasyCallbackSDK.complete = {
                    runOnUiThread {
                        MainScope().launch {
                            connectDialog?.info =
                                getString(if (it) R.string.feedback_success else R.string.feedback_failure)
                            delay(1000)
                            connectDialog?.dismiss()
                            putStr(mPreferenceKey,"")
                            deviceData.setText(getStr(mPreferenceKey))
                        }
                    }
                }

                FeasyCallbackSDK.feedback(
                    this, "${deviceData.text}", when (radio_group.checkedRadioButtonId) {
                        R.id.proposal -> FEEDBACK_ADVICE
                        R.id.abnormal_function -> FEEDBACK_BUG
                        R.id.interface_abnormality -> FEEDBACK_UI
                        R.id.cooperation -> FEEDBACK_COOPERATION
                        else -> FEEDBACK_ADVICE
                    }
                )
            } else {
                MainScope().launch {
                    connectDialog?.info = getString(R.string.feedback_data_error)
                    delay(1000)
                    connectDialog?.dismiss()
                }
            }
        }

        radio_group.setOnCheckedChangeListener { _, p1 ->
            mPreferenceKey = when (p1) {
                R.id.proposal -> "FEEDBACK_ADVICE"
                R.id.abnormal_function -> "FEEDBACK_BUG"
                R.id.interface_abnormality -> "FEEDBACK_UI"
                R.id.cooperation -> "FEEDBACK_COOPERATION"
                else -> "FEEDBACK_ADVICE"
            }

            deviceData.setText(getStr(mPreferenceKey))
            deviceData.setSelection(deviceData.text.toString().length)
        }

        toolbarLog.setOnClickListener {
            //导出日志
            val directory: File = getExternalFilesDir("Logcat")!!
            if (!directory.isDirectory) {
                directory.delete()
            }
            if (!directory.exists()){
                ToastUtil.show(this,getString(R.string.no_logcat_file))
                return@setOnClickListener
            }
            val logcatFile = File(directory,"Logcat.txt")
            if (!logcatFile.exists()) {
                ToastUtil.show(this,getString(R.string.no_log))
                return@setOnClickListener
            }
            val logcatFileUri = FileShareHelper.getFileUri(this, logcatFile)
            FileShareHelper.shareFile(this, logcatFileUri, "application/pdf")
        }

        toolbarLog.setOnLongClickListener {
            //弹框提示是否删除当前所有日志记录
            mMessageDialog = MessageDialog.Builder(this@FeedbackActivity)
                .setMessage(getString(R.string.is_delete_all_log))
                .setConfirm(getString(R.string.ok))
                .setCancel(getString(R.string.cancel))
                .setListener(object : MessageDialog.OnListener {
                    override fun onConfirm(dialog: WFBaseDialog?) {
                        val directory: File = getExternalFilesDir("Logcat")!!
                        if (!directory.isDirectory) {
                            directory.delete()
                        }
                        if (directory.exists()) {
                            deleteFolder(directory)
                        }
                        if (!directory.exists()){
                            directory.mkdirs()
                        }
                    }

                    override fun onCancel(dialog: WFBaseDialog?) {

                    }
                }).show()
            true
        }

        deviceData.addTextChangedListener {
            MsgLogger.e("initView => ${it.toString()}")
            putStr(mPreferenceKey, it.toString())
        }
    }

    private fun deleteFolder(folder: File): Boolean {
        // 如果传入的是文件而不是文件夹，则直接删除
        if (folder.isFile) {
            return folder.delete()
        }

        // 如果传入的是文件夹，则递归删除其所有内容
        val files = folder.listFiles() ?: return false
        for (file in files) {
            if (file.isDirectory) {
                // 递归删除子文件夹
                deleteFolder(file)
            } else {
                // 删除文件
                file.delete()
            }
        }

        // 删除空文件夹
        return folder.delete()
    }

    override fun getLayout() = R.layout.activity_feedback

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNetworkReceiver)
    }


    companion object {
        const val TAG: String = "FeedbackActivity"
        private lateinit var mResultDialog: AlertDialog

        fun activityStart(context: Context){
            val intent = Intent(context, FeedbackActivity::class.java)
            context.startActivity(intent)
        }
    }



}