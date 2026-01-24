package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.LoadDialogFragment
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.interfaces.FeasyWiFiCallbacks
import com.feasycom.feasyblue.logic.AtCommand
import com.feasycom.feasyblue.logic.BluetoothRepository
import com.feasycom.feasyblue.viewmodel.BluetoothViewModel
import kotlinx.android.synthetic.main.activity_bluetooth_distribution_network.*
import kotlinx.android.synthetic.main.activity_wifi_setting.header
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BluetoothDmsActivity : BaseActivity(), FeasyWiFiCallbacks {

    private val mViewModel by lazy {
        ViewModelProviders.of(this)[BluetoothViewModel::class.java]
    }

    private lateinit var mWaitDialog: LoadDialogFragment

    private lateinit var mMessageDialog: WFBaseDialog

    private var ip: String = ""

    override fun initView() {
        BluetoothRepository.registerViewCallback(this)
        header.titleText.text = getString(R.string.title_bluetooth)
        header.back_group.visibility = View.VISIBLE
        initEvent()
    }

    override fun getLayout(): Int {
        return R.layout.activity_bluetooth_distribution_network
    }

    private fun initEvent() {
        mViewModel.registerReceiver() // 注册网络发生改变的广播
        header.backImg.setOnClickListener { // 图片返回点击事件
            finish()
        }
        header.back_tv.setOnClickListener { // 文件返回点击事件
            finish()
        }

        dhcpSwitch.setOnCheckedChangeListener { _, isChecked ->
            BluetoothRepository.setDhcp(isChecked)
            MsgLogger.d(TAG, "initEvent isChecked => $isChecked")
            if (isChecked) {
                dhcp_group.visibility = View.GONE
                dns_group.visibility = View.GONE
            } else {
                dhcp_group.visibility = View.VISIBLE
                MsgLogger.d(TAG, "initEvent mGoneDNS => ${BluetoothRepository.mGoneDNS}")
                if (BluetoothRepository.mGoneDNS) {
                    dns_group.visibility = View.VISIBLE
                }
            }
        }

        configNetwork.setOnClickListener {
            if (ssid_ev.text.isBlank()){
                showMessageDialog(getString(R.string.ssid_empty))
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                if (!dhcpSwitch.isChecked) {
                    val ip = ip_ev.text.toString()
                    val gw = gw_ev.text.toString()
                    val mask = mask_ev.text.toString()
                    val dns = dns_ev.text.toString()

                    if (!ip.contains(IP_GW_MASK_REGEX)) showErrorDialog(R.string.ip_error)
                    else if (!gw.contains(IP_GW_MASK_REGEX)) showErrorDialog(R.string.gw_error)
                    else if (!mask.contains(IP_GW_MASK_REGEX)) showErrorDialog(R.string.mask_error)
//                    else if (!dns.contains(DNS_REGEX)) showErrorDialog(R.string.dns_error)
                    else {
                        showWaitDialog(R.string.configure_network)
                        BluetoothRepository.setIp(ip)
                        delay(200)
                        BluetoothRepository.setGw(gw)
                        delay(200)
                        BluetoothRepository.setMask(mask)
                        delay(200)
                        BluetoothRepository.setDns(dns)
                        delay(200)
                        BluetoothRepository.setNetwork(ssid_ev.text.toString(), password_ev.text.toString())
                    }
                } else {
                    showWaitDialog(R.string.configure_network)
                    BluetoothRepository.setNetwork(ssid_ev.text.toString(), password_ev.text.toString())
                }
            }
        }

        BluetoothRepository.getDhcp()
        mViewModel.mBroadcastData.observe(this as LifecycleOwner) {
            ssid_ev.setText(it)
        }

    }

    private fun showMessageDialog(message: String) {
        mMessageDialog = MessageDialog.Builder(this@BluetoothDmsActivity)
                .setMessage(message)
                .setConfirm(getString(R.string.ok))
                .setCancel(null)
                .show()
    }

    private suspend fun showErrorDialog(messageId: Int){
        withContext(Dispatchers.Main) {
            mMessageDialog = MessageDialog.Builder(this@BluetoothDmsActivity)
                .setMessage(getString(messageId))
                .setConfirm(getString(R.string.ok))
                .setCancel(null)
                .show()
        }
    }

    private suspend fun showWaitDialog (messageId: Int){
        withContext(Dispatchers.Main) {
            mWaitDialog = LoadDialogFragment(getString(messageId))
            mWaitDialog.show(supportFragmentManager, "config")
        }
    }

    override fun success(ip: String) {
        MsgLogger.d(TAG, "success 配网成功 ip => $ip")
        this.ip = ip
        showDialog(true)
    }

    override fun failure() {
        MsgLogger.e(TAG, "failure 配网失败...")
        showDialog(false)
    }

    private fun showDialog(isSuccess: Boolean) {
        lifecycleScope.launch {
            if (::mWaitDialog.isInitialized) {
                mWaitDialog.dismiss()
            }

            mMessageDialog = (if (isSuccess) {
                MessageDialog.Builder(this@BluetoothDmsActivity)
                    // 标题可以不用填写
                    .setTitle(getString(R.string.configure_result_success))
                    // 内容必须要填写
                    .setMessage(getString(R.string.configure_result_success_item, ip)) // 确定按钮文本
                    .setConfirm(getString(R.string.ok)) // 设置null表示不显示取消按钮
                    .setCancel(null) // 设置点击按钮后不关闭对话框
            } else {
                MessageDialog.Builder(this@BluetoothDmsActivity)
                    // 内容必须要填写
                    .setMessage(getString(R.string.configure_result_failed)) // 确定按钮文本
                    .setConfirm(getString(R.string.ok)) // 设置null表示不显示取消按钮
                    .setCancel(null) // 设置点击按钮后不关闭对话框
            }).show()
        }
    }

    override fun blePeripheralDisconnected() {
        // TODO: 断开连接
        BluetoothRepository.isDisconnected = true
        lifecycleScope.launch(Dispatchers.Main) {
            if (::mMessageDialog.isInitialized && mMessageDialog.isShowing) {
                mMessageDialog.dismiss()
            }
            if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
                mWaitDialog.dismiss()
                mMessageDialog = MessageDialog.Builder(this@BluetoothDmsActivity)
                    // 内容必须要填写
                    .setMessage(getString(R.string.configure_result_failed)) // 确定按钮文本
                    .setConfirm(getString(R.string.ok)) // 设置 null 表示不显示取消按钮
                    .setCancel(null) // 设置点击按钮后不关闭对话框
                    .setListener(
                        object : MessageDialog.OnListener {
                            override fun onConfirm(dialog: WFBaseDialog?) {
                                // 断开连接，返回到上一层
                                finish()
                            }

                            override fun onCancel(dialog: WFBaseDialog?) {
                            }
                        })
                    .show()
                return@launch
            }
            finish()
        }
    }

    override fun packetReceived(strValue: String?) {
        MsgLogger.d(TAG, "packetReceived strValue => $strValue")
        strValue?.let {
            when {
                it.contains("+DHCP") -> {
                    val s = it.split("=").toTypedArray()[1].split("\r\n")
                        .toTypedArray()[0]

                    lifecycleScope.launch(Dispatchers.Main) {
                        dhcpSwitch.isChecked = s == "1"
                        if (s == "0") {
                            dhcp_group.visibility = View.VISIBLE
                        } else {
                            dhcp_group.visibility = View.GONE
                        }
                    }
                }
                it.contains("ERR") -> {
                    when (BluetoothRepository.lostAtCommand) {
                        AtCommand.GET_DHCP -> {
                            Log.e(TAG, "packetReceived: 获取DHCP 失败")
                        }
                        AtCommand.SET_DHCP -> {
                            Log.e(TAG, "packetReceived: 设置DHCP 失败")
                        }
                        AtCommand.GET_DNS -> {
                            Log.e(TAG, "packetReceived: 获取DNS 失败")
                        }
                        AtCommand.SET_DNS -> {
                            Log.e(TAG, "packetReceived: 设置DNS 失败")
                        }
                        AtCommand.GET_IP -> {
                            Log.e(TAG, "packetReceived: 获取IP 失败")
                        }
                        AtCommand.SET_IP -> {
                            Log.e(TAG, "packetReceived: 设置IP 失败")
                        }
                        AtCommand.SET_GW -> {
                            Log.e(TAG, "packetReceived: 设置GW 失败")
                        }
                        AtCommand.SET_MASK -> {
                            Log.e(TAG, "packetReceived: 设置MASK 失败")
                        }
                        AtCommand.SET_NETWORK -> {
                            Log.e(TAG, "packetReceived: 设置网络 失败")
                        }
                        else -> {

                        }
                    }
                }
                it.contains("OK") -> {
                    when (BluetoothRepository.lostAtCommand) {
                        AtCommand.GET_DHCP -> {
                            Log.e(TAG, "packetReceived: 获取DHCP 成功")
                        }
                        AtCommand.SET_DHCP -> {
                            Log.e(TAG, "packetReceived: 设置DHCP 成功")
                        }
                        AtCommand.GET_DNS -> {
                            Log.e(TAG, "packetReceived: 获取DNS 成功")
                        }
                        AtCommand.SET_DNS -> {
                            Log.e(TAG, "packetReceived: 设置DNS 成功")
                        }
                        AtCommand.GET_IP -> {
                            Log.e(TAG, "packetReceived: 获取IP 成功")
                        }
                        AtCommand.SET_IP -> {
                            Log.e(TAG, "packetReceived: 设置IP 成功")
                        }
                        AtCommand.SET_GW -> {
                            Log.e(TAG, "packetReceived: 设置GW 成功")
                        }
                        AtCommand.SET_MASK -> {
                            Log.e(TAG, "packetReceived: 设置MASK 成功")
                        }
                        AtCommand.SET_NETWORK -> {
                            Log.e(TAG, "packetReceived: 设置网络 成功")
                        }
                        else -> {

                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.unregisterReceiver()
        BluetoothRepository.unRegisterViewCallback(this)
    }

    companion object {
        private const val TAG = "BluetoothDmsActivity"

        //        val IP_REGEX = "^([1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])(\\.([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}\$".toRegex()
        val IP_GW_MASK_REGEX = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b".toRegex()
        val DNS_REGEX =
            "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}(?:,\\s*(?:[0-9]{1,3}\\.){3}[0-9]{1,3})*\\b".toRegex()

        fun activityStart(context: Context) {
            Intent(context, BluetoothDmsActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

}