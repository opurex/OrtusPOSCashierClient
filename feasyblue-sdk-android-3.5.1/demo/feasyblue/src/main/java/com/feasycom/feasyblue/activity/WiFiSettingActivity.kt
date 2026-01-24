package com.feasycom.feasyblue.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.feasycom.common.bean.FscDevice
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.LoadDialogFragment
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.interfaces.FeasyWiFiCallbacks
import com.feasycom.feasyblue.logic.BluetoothRepository
import kotlinx.android.synthetic.main.activity_wifi_setting.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WiFiSettingActivity : BaseActivity(), View.OnClickListener, FeasyWiFiCallbacks {

    private lateinit var mFscDevice: FscDevice

    private lateinit var mWaitDialog: LoadDialogFragment
    private lateinit var mMessageDialog: WFBaseDialog

    private val mBluetoothCrv: CardView? by lazy { findViewById(R.id.bluetooth_crv) }
    private val mOtaCrv: CardView? by lazy { findViewById(R.id.ota_crv) }
    private val mGatewayCrv: CardView? by lazy { findViewById(R.id.gateway_crv) }
    private val mBackImg: ImageView? by lazy { findViewById(R.id.backImg) }
    private val mBackTv: TextView? by lazy { findViewById(R.id.back_tv) }

    override fun initView() {
        BluetoothRepository.registerViewCallback(this)// 注册FeasyWiFiCallbacks的回调
        if (BluetoothRepository.isDisconnected) {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    mMessageDialog = MessageDialog.Builder(this@WiFiSettingActivity)
                        .setMessage(getString(R.string.disconnet))
                        .setConfirm(getString(R.string.ok))
                        .setCancel(null)
                        .setListener(
                            object : MessageDialog.OnListener {
                                override fun onConfirm(dialog: WFBaseDialog?) {
                                    finish()
                                }

                                override fun onCancel(dialog: WFBaseDialog?) {
                                }
                            })
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        intent?.getParcelableExtra<FscDevice>("FscDevice")?.let {
            mFscDevice = it
        } ?: finish()

        if (!::mFscDevice.isInitialized) {
            finish()
        }
        val mFscDeviceName = mFscDevice.name
        if (mFscDeviceName.length > 10) {
            header.titleText.text = "${mFscDeviceName.substring(0, 10)}..."
        } else {
            header.titleText.text = mFscDeviceName
        }
        header.back_group.visibility = View.VISIBLE
        initEvent()
        BluetoothRepository.getDNS()
    }

    override fun blePeripheralDisconnected() {
        super.blePeripheralDisconnected()
        BluetoothRepository.isDisconnected = true
        lifecycleScope.launch(Dispatchers.Main) {
            mMessageDialog = MessageDialog.Builder(this@WiFiSettingActivity)
                .setMessage(getString(R.string.disconnet))
                .setConfirm(getString(R.string.ok))
                .setCancel(null)
                .setListener(
                    object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: WFBaseDialog?) {
                            finish()
                        }

                        override fun onCancel(dialog: WFBaseDialog?) {
                        }
                    })
                .show()

        }
    }

    private fun initEvent() {
        mBluetoothCrv?.setOnClickListener(this)
        mOtaCrv?.setOnClickListener(this)
        mGatewayCrv?.setOnClickListener(this)
        mBackImg?.setOnClickListener(this)
        mBackTv?.setOnClickListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_wifi_setting
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bluetooth_crv -> {
                BluetoothDmsActivity.activityStart(this)
            }
            R.id.ota_crv -> {
                WFOtaActivity.activityStart(this)
            }
            R.id.gateway_crv -> {
                GatewayActivity.activityStart(this)
            }
            R.id.backImg -> {
                finish()
            }
            R.id.back_tv -> {
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "WiFiSettingActivity"

        fun activityStart(context: Context, fscDevice: FscDevice) {
            Intent(context, WiFiSettingActivity::class.java).apply {
                putExtra("FscDevice", fscDevice)
                context.startActivity(this)
            }
        }
    }

    override fun packetReceived(strValue: String?) {
        Log.e(TAG,"packetReceived strValue -> $strValue")
        BluetoothRepository.mGoneDNS = strValue?.contains("ERR") != true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BluetoothRepository.isConnect()) {
            BluetoothRepository.disconnect()
        }
        BluetoothRepository.unRegisterViewCallback(this)
    }

}