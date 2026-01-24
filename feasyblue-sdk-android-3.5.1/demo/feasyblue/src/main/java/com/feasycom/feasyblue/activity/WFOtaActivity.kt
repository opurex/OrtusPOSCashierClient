package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.LoadDialogFragment
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.interfaces.FeasyWiFiCallbacks
import com.feasycom.feasyblue.logic.BluetoothRepository
import kotlinx.android.synthetic.main.activity_wifi_ota.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WFOtaActivity : BaseActivity(), FeasyWiFiCallbacks {

    private lateinit var mWaitDialog: LoadDialogFragment

    private lateinit var mMessageDialog: WFBaseDialog

    override fun initView() {
        BluetoothRepository.registerViewCallback(this)
        header.titleText.text = getString(R.string.title_ota)
        header.back_group.visibility = View.VISIBLE
        initEvent()
    }

    override fun getLayout(): Int {
        return R.layout.activity_wifi_ota
    }

    private fun initEvent(){
        header.backImg.setOnClickListener {
            finish()
        }
        header.back_tv.setOnClickListener{
            finish()
        }
        query_ip.setOnClickListener{ // 查询IP
            clearFocus()
            BluetoothRepository.getIp()
        }
        query_ver.setOnClickListener { // 查询版本
            clearFocus()
            BluetoothRepository.getVersion()
        }
        upgrade.setOnClickListener { // 升级
            if (ota.text.toString().isNotBlank()){
                clearFocus()
                BluetoothRepository.startOTA(ota.text.toString())
            }
        }
        reset.setOnClickListener { // 恢复出厂设置
            clearFocus()
            mMessageDialog = MessageDialog.Builder(this)
                .setMessage(getString(R.string.reset_tips)) // 确定按钮文本
                .setConfirm(getString(R.string.pin_cancel)) // 设置null表示不显示取消按钮
                .setCancel(getString(R.string.reset)) // 设置点击按钮后不关闭对话框
                .setListener(
                    object : MessageDialog.OnListener{
                        override fun onConfirm(dialog: WFBaseDialog?) {

                        }

                        override fun onCancel(dialog: WFBaseDialog?) {
                            super.onCancel(dialog)
                            mWaitDialog = LoadDialogFragment(getString(R.string.reset_wait))
                            mWaitDialog.show(this@WFOtaActivity.supportFragmentManager, "reset")
                            BluetoothRepository.reset()
                        }
                    }
                )
                .show()
        }
    }

    private fun clearFocus(){
        ota.clearFocus()
        ip.clearFocus()
        ver.clearFocus()
        hideKeyboard(ota)
        hideKeyboard(ip)
        hideKeyboard(ver)
    }

    private fun hideKeyboard(v: View){
        val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive){
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }
    }

    override fun blePeripheralDisconnected() {
        lifecycleScope.launch(Dispatchers.Main){
            BluetoothRepository.isDisconnected = true
            if(::mMessageDialog.isInitialized &&  mMessageDialog.isShowing ){
                mMessageDialog.dismiss()
            }
            if (::mWaitDialog.isInitialized && mWaitDialog.isVisible){
                mWaitDialog.dismiss()
                if (mWaitDialog.tag !="reset"){
                    /*mMessageDialog = MessageDialog.Builder(requireContext())
                        .setMessage(R.string.update_fail)
                        .setConfirm(R.string.ok)
                        .setCancel(null)
                        .setListener(
                            object : MessageDialog.OnListener{
                                override fun onConfirm(dialog: BaseDialog?) {
                                    findNavController().navigate(OtaFragmentDirections.actionOtaFragmentToSettingFragment(true))
                                }
                                override fun onCancel(dialog: BaseDialog?) {
                                }
                            })
                        .show()*/
                    // 返回到上一层
                    finish()
                    return@launch
                }
            }
            // 返回到上一层
            finish()
        }
    }

    override fun ipInformation(ips: String?) {
        super.ipInformation(ips)
        lifecycleScope.launch(Dispatchers.Main){
            ip.text = ips
        }
    }

    override fun versionInformation(version: String?) {
        super.versionInformation(version)
        try {
            lifecycleScope.launch(Dispatchers.Main){
                ver.text = version
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onOtaSuccess() {
        super.onOtaSuccess()
        MsgLogger.d(TAG,"onOtaSuccess => 升级成功")
        lifecycleScope.launch(Dispatchers.Main){
            mWaitDialog.dismiss()
            mMessageDialog = MessageDialog.Builder(this@WFOtaActivity)
                // 内容必须要填写
                .setMessage(getString(R.string.update_success)) // 确定按钮文本
                .setConfirm(getString(R.string.ok)) // 设置null表示不显示取消按钮
                .setCancel(null) // 设置点击按钮后不关闭对话框
                .show()
        }
    }

    override fun onOtaFailure() {
        super.onOtaFailure()
        MsgLogger.e(TAG,"onOtaFailure => 升级失败")
        lifecycleScope.launch(Dispatchers.Main){
            mWaitDialog.dismiss()
            mMessageDialog = MessageDialog.Builder(this@WFOtaActivity)
                // 内容必须要填写
                .setMessage(getString(R.string.update_fail)) // 确定按钮文本
                .setConfirm(getString(R.string.ok)) // 设置null表示不显示取消按钮
                .setCancel(null) // 设置点击按钮后不关闭对话框
                .show()
        }
    }

    override fun onOtaProgress(progress: String?) {
        super.onOtaProgress(progress)
        MsgLogger.e(TAG,"onOtaProgress => $progress")
        lifecycleScope.launch(Dispatchers.Main){
            mWaitDialog.mMessage = "${getString(R.string.upgrade_message)} ( $progress%)"
        }
    }

    override fun onOtaNetworkIsNotConfigured() {
        super.onOtaNetworkIsNotConfigured()
        lifecycleScope.launch(Dispatchers.Main) {
            mMessageDialog = MessageDialog.Builder(this@WFOtaActivity)
                .setMessage(getString(R.string.ota_tips_dialog))
                .setConfirm(android.R.string.ok) // 设置 null 表示不显示取消按钮
                .setCancel(null) // 设置点击按钮后不关闭对话框
                .show()
            mMessageDialog.setCanceledOnTouchOutside(false)
        }
    }

    override fun onOtaStartConfig() {
        super.onOtaStartConfig()
        lifecycleScope.launch(Dispatchers.Main) {
            mWaitDialog = LoadDialogFragment(getString(R.string.upgrade_message))
            mWaitDialog.show(this@WFOtaActivity.supportFragmentManager, "ota")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothRepository.unRegisterViewCallback(this)
    }

    companion object{
        const val TAG: String = "WFOtaActivity"
        fun activityStart(context: Context) {
            Intent(context, WFOtaActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

}