package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.LoadDialogFragment
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.interfaces.FeasyWiFiCallbacks
import com.feasycom.feasyblue.logic.BluetoothRepository
import com.feasycom.feasyblue.utils.getInt
import com.feasycom.feasyblue.utils.getStr
import com.feasycom.feasyblue.utils.putInt
import com.feasycom.feasyblue.utils.putStr
import kotlinx.android.synthetic.main.activity_gateway.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class GatewayActivity : BaseActivity(), FeasyWiFiCallbacks {

    private val mProtocolList: MutableList<String> by lazy {
        resources.getStringArray(R.array.gateway).toMutableList()
    }

    private val requests: Deque<ByteArray> = LinkedList()

    private lateinit var mWaitDialog: LoadDialogFragment

    private lateinit var mMessageDialog: WFBaseDialog

    private var isSuccessFlag = false

    override fun initView() {
        BluetoothRepository.registerViewCallback(this)
        requests.clear()
        header.titleText.text = getString(R.string.title_gateway)
        header.back_group.visibility = View.VISIBLE

        //声明一个下拉列表的数组适配器
        val starAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_select, mProtocolList)
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_dropdown)
        //设置下拉框的数组适配器
        spinner.adapter = starAdapter
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                http_group.visibility = View.GONE
                tcp_group.visibility = View.GONE
                mqtt_group.visibility = View.GONE
                putInt("selection", position)
                when (position) {
                    0 -> {
                        connect_btn.visibility = View.GONE
                    }
                    1 -> {
                        // MQTT
                        mqtt_group.visibility = View.VISIBLE
                        connect_btn.visibility = View.VISIBLE
//                        BluetoothRepository.send("AT+PROFILE=1\r\n".toByteArray())
                    }
                    2 -> {
                        // TCP
                        tcp_group.visibility = View.VISIBLE
                        connect_btn.visibility = View.VISIBLE
//                        BluetoothRepository.send("AT+PROFILE=2\r\n".toByteArray())
                    }
                    3 -> {
                        // HTTP
                        http_group.visibility = View.VISIBLE
                        connect_btn.visibility = View.VISIBLE
//                        BluetoothRepository.send("AT+PROFILE=3\r\n".toByteArray())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        val selection = getInt("selection", 0)
        spinner.setSelection(selection)
        when(selection) {
            0 -> {
                http_group.visibility = View.GONE
                tcp_group.visibility = View.GONE
                mqtt_group.visibility = View.GONE
                connect_btn.visibility = View.GONE
            }
            1 -> {
                http_group.visibility = View.GONE
                tcp_group.visibility = View.GONE
                mqtt_group.visibility = View.VISIBLE
                connect_btn.visibility = View.VISIBLE
            }
            2 -> {
                http_group.visibility = View.GONE
                mqtt_group.visibility = View.GONE
                tcp_group.visibility = View.VISIBLE
                connect_btn.visibility = View.VISIBLE
                val tcpServer = getStr("tcpServer", "")
                val tcpPort = getStr("tcpPort", "")
                tcp_server_et.setText(tcpServer)
                tcp_server_et.setSelection(tcpServer.length)
                tcp_port_et.setText(tcpPort)
                tcp_port_et.setSelection(tcpPort.length)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_gateway
    }

    private fun initEvent() {
        header.backImg.setOnClickListener {
            finish()
        }
        header.back_tv.setOnClickListener {
            finish()
        }
        connect_btn.setOnClickListener {
            when (spinner.selectedItemPosition) {
                1 -> {
                    if (mqtt_broker_et.text.isNotBlank() && mqtt_client_et.text.isNotBlank()) {
                        MsgLogger.e(TAG, "initEvent: AT+BROKER => ${mqtt_broker_et.text}\r\n")
                        requests.add("AT+BROKER=${mqtt_broker_et.text}\r\n".toByteArray())
                        if (mqtt_port_et.text.isNotBlank()) {
                            val port = try {
                                mqtt_port_et.text.toString().toInt()
                            } catch (e: Exception) {
                                1883
                            }
                            MsgLogger.e(TAG, "initEvent: AT+PORT=${port}")
                            requests.add("AT+PORT=${port}\r\n".toByteArray())
                        }
                        MsgLogger.e(TAG, "initEvent: AT+CLIENTID => ${mqtt_client_et.text}")
                        requests.add("AT+CLIENTID=${mqtt_client_et.text}\r\n".toByteArray())
                        if (mqtt_user_name_et.text.isNotBlank()) {
                            MsgLogger.e(TAG, "initEvent: AT+USERNAME=${mqtt_user_name_et.text}\r\n")
                            requests.add("AT+USERNAME=${mqtt_user_name_et.text}\r\n".toByteArray())
                        }
                        if (mqtt_password_et.text.isNotBlank()) {
                            MsgLogger.e(TAG, "initEvent: AT+MQTTPWD => ${mqtt_password_et.text}")
                            requests.add("AT+MQTTPWD=${mqtt_password_et.text}\r\n".toByteArray())
                        }
                        if (mqtt_topic_et.text.isNotBlank()) {
                            //requests.add("AT+PUBTPC=${mqtt_topic_et.text}\r\n".toByteArray()) // 支持旧版的BW固件
                            requests.add("AT+SUBTPC=${mqtt_topic_et.text}\r\n".toByteArray()) // 支持新的BW固件
                        }
                        requests.add("AT+REBOOT\r\n".toByteArray())
                    }
                }

                2 -> {
                    if (tcp_server_et.text.toString().isNotBlank() && tcp_port_et.text.toString()
                            .isNotBlank()
                    ) {
                        MsgLogger.e(TAG, "initEvent: AT+TCPCFG => ${tcp_server_et.text},${tcp_port_et.text}\n")
                        val tcpServer = tcp_server_et.text.toString()
                        val tcpPort = tcp_port_et.text.toString()
                        putStr("tcpServer", tcpServer)
                        putStr("tcpPort", tcpPort)
                        requests.add("AT+TCPCFG=$tcpServer,$tcpPort\r\n".toByteArray())
//                        requests.add("AT+REBOOT\r\n".toByteArray())
                    }
                }
                3 -> {
                    if (http_et.text.toString().isNotBlank()) {
                        MsgLogger.e(TAG, "initEvent: AT+HTTPCFG => ${http_et.text}")
                        requests.add("AT+HTTPCFG=${http_et.text}\r\n".toByteArray())
                        requests.add("AT+REBOOT\r\n".toByteArray())
                    }
                }
            }
            if (requests.isNotEmpty()) {
                mWaitDialog = LoadDialogFragment(getString(R.string.send_config))
                mWaitDialog.show(supportFragmentManager, "config")
                sendAt()
            }
        }
    }

    private fun sendAt() {
        if (requests.isNotEmpty()) {
            val byteArray = requests.remove()
            BluetoothRepository.send(byteArray)
        } else {
            when (getInt("selection", -1)) {
                2 -> {
                    if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
                        mWaitDialog.dismiss()
                        runOnUiThread {
                            TCPCommunicationActivity.activityStart(this, getStr("tcpServer",""), getStr("tcpPort", ""))
                        }
                    }
                }
            }

        }
    }

    override fun blePeripheralDisconnected() {
        super.blePeripheralDisconnected()
        // TODO: 断开连接
        BluetoothRepository.isDisconnected = true
        lifecycleScope.launch(Dispatchers.Main) {
            if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
                mWaitDialog.dismiss()
                val message =
                    if (isSuccessFlag) getString(R.string.config_failed) else getString(R.string.config_success)
                mMessageDialog = MessageDialog.Builder(this@GatewayActivity)
                    .setMessage(message)
                    .setConfirm(getString(R.string.ok))
                    .setCancel(null)
                    .setListener {
                        mMessageDialog.dismiss()
                        finish()
                    }
                    .show()
            } else {
                // 返回上一层
                finish()
            }
        }
    }

    override fun packetReceived(strValue: String?) {
        super.packetReceived(strValue)
        MsgLogger.e(TAG, "gateway packetReceived 收到数据 => $strValue")
        if (strValue!!.contains("ERR001")) {
            MsgLogger.d(TAG, "因为发送了相同的topic，所以收到了ERR001")
            isSuccessFlag = true
        }
        sendAt()
    }

    override fun packetSend(strValue: String?) {
        super.packetSend(strValue)
        MsgLogger.e(TAG, "packetSend 发送数据 => $strValue")
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothRepository.unRegisterViewCallback(this)
    }

    companion object {
        const val TAG: String = "GatewayActivity"
        fun activityStart(context: Context) {
            Intent(context, GatewayActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

}