package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import com.feasycom.feasyblue.R
import com.feasycom.wifi.tcp.FscClientApiImp
import com.feasycom.wifi.tcp.FscClientCallback
import java.lang.ref.WeakReference
import kotlinx.android.synthetic.main.activity_tcp_communication.*
import kotlinx.android.synthetic.main.activity_tcp_communication.header
import kotlinx.android.synthetic.main.header.view.*

class TCPCommunicationActivity : BaseActivity(), FscClientCallback {

    private lateinit var mFscClientBwBwApiImp : FscClientApiImp

    private lateinit var mHandler : Handler

    private var sb = StringBuilder()

    class MyHandler(activity : TCPCommunicationActivity) : Handler(Looper.getMainLooper()) {
        private val mWeakReference : WeakReference<TCPCommunicationActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message) {
            mWeakReference.get()?.run {
                when (msg.what) {
                    1 -> {
                        header.titleText.text = "TCP客户端已连接"
                        send_data_acb.isEnabled = true
                        send_data_et.isEnabled = true
                    }
                    2 -> {
                        header.titleText.text = "TCP客户端已断开"
                        send_data_acb.isEnabled = false
                        send_data_et.isEnabled = false
                    }
                    3 -> {
                        val strValue = msg.data.getString("strValue")
                        sb.append(strValue)
                        receive_data_et.setText(sb.toString())
                    }
                }
            }
        }
    }

    override fun initView() {
        val host = intent?.getStringExtra("tcpServer")
        val port = intent?.getStringExtra("tcpPort")
        val intPort = port?.toInt()
        header.back_group.visibility = View.VISIBLE
        header.titleText.text = "TCP客户端连接中..."
        receive_data_et.isEnabled = false
        send_data_et.isEnabled = false
        send_data_acb.isEnabled = false
        mHandler = MyHandler(this)
        mFscClientBwBwApiImp = FscClientApiImp.getInstance()
        mFscClientBwBwApiImp.setCallback(this)
        Log.e(TAG,"host => $host  intPort => $intPort")
        mFscClientBwBwApiImp.connect(host!!, intPort!!, intPort)

        initEvent()
    }

    override fun getLayout(): Int {
        return R.layout.activity_tcp_communication
    }

    private fun initEvent() {
        header.backImg.setOnClickListener {
            finish()
        }
        header.back_tv.setOnClickListener {
            finish()
        }

        clear_text_acb.setOnClickListener {
            receive_data_et.setText("")
            send_data_et.setText("")
            sb.clear()
        }

        send_data_acb.setOnClickListener {
            val msg = send_data_et.text.toString()
            if (msg.isNotBlank()){
                mFscClientBwBwApiImp.send(msg.toByteArray())
            }
        }
    }

    override fun connected() {
        mHandler.sendEmptyMessage(1)
    }

    override fun disconnected() {
        mHandler.sendEmptyMessage(2)
    }

    override fun packetReceived(
        strValue: String?,
        hexString: String?,
        rawValue: ByteArray?,
        timestamp: String?
    ) {
        Log.e(TAG,"strValue => $strValue  hexString => $hexString  timestamp => $timestamp")
        val message = Message()
        message.what = 3
        val bundle = Bundle()
        bundle.putString("strValue",strValue)
        message.data = bundle
        mHandler.sendMessage(message)
    }

    override fun readResponse(
        strValue: String?,
        hexString: String?,
        rawValue: ByteArray?,
        timestamp: String?
    ) {
        super.readResponse(strValue, hexString, rawValue, timestamp)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFscClientBwBwApiImp.disconnect()
        mHandler.removeCallbacksAndMessages(null)
        mFscClientBwBwApiImp.disconnect()
    }

    companion object {
        const val TAG = "TCPCommunication"
        fun activityStart(context: Context, tcpServer: String, tcpPort: String){
            val intent = Intent(context, TCPCommunicationActivity::class.java)
            intent.putExtra("tcpServer", tcpServer)
            intent.putExtra("tcpPort", tcpPort)
            context.startActivity(intent)
        }
    }
}