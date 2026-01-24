package com.feasycom.feasyblue.activity

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.*
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import kotlinx.android.synthetic.main.activity_advanced_settings_list.*

class AdvancedSettingsListActivity : BaseActivity(), View.OnClickListener {

    private val sFscSppCentralApi: FscSppCentralApi by lazy {
        FscSppCentralApiImp.getInstance(this)
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false);
            it.setDisplayHomeAsUpEnabled(true)
        }
        toolbarTitle.text = getString(R.string.advanced_setting_text)

        toolbar.setNavigationOnClickListener {
            MainActivity.activityStart(this, 1)
            finish()
        }

        sdp_service_switch_view.isOpened = getBoolean("sdpServiceSwitchOpen", false)
        if (sdp_service_switch_view.isOpened) {
            custom_uuid8_et.isEnabled = false
            custom_uuid4_et1.isEnabled = false
            custom_uuid4_et2.isEnabled = false
            custom_uuid4_et3.isEnabled = false
            custom_uuid12_et.isEnabled = false
        }
        val customUuid = getStr("customUuid", "")
        if (customUuid.isNotEmpty()) {
            val hexArray = customUuid.split("-")
            custom_uuid8_et.setText(hexArray[0])
            custom_uuid4_et1.setText(hexArray[1])
            custom_uuid4_et2.setText(hexArray[2])
            custom_uuid4_et3.setText(hexArray[3])
            custom_uuid12_et.setText(hexArray[4])
        }

        initEvent()
    }



    private fun initEvent() {
        sppAutoConnectTestButton.setOnClickListener(this)
        bleAutoConnectTestButton.setOnClickListener(this)
        sdp_service_switch_view.setOnClickListener(this)
        restore_uuid_acb.setOnClickListener(this)

        setOnLongClickListener(custom_uuid8_et)
        setOnLongClickListener(custom_uuid4_et1)
        setOnLongClickListener(custom_uuid4_et2)
        setOnLongClickListener(custom_uuid4_et3)
        setOnLongClickListener(custom_uuid12_et)

    }

    private fun setOnLongClickListener(editText: EditText) {
        editText.setOnLongClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val copiedText = clipData.getItemAt(0).text
                Log.e("TAG", "copiedText => $copiedText")
                // 这里处理复制的文本
                if (copiedText.length == 32) {
                    val editTexts = listOf(
                        custom_uuid8_et,
                        custom_uuid4_et1,
                        custom_uuid4_et2,
                        custom_uuid4_et3,
                        custom_uuid12_et
                    )
                    formatAndFillEditText(copiedText.toString(), editTexts)
                }
            }
            true
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_advanced_settings_list
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            MainActivity.activityStart(this, 1)
            finish()
            false
        } else {
            super.onKeyDown(keyCode, event);
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.sppAutoConnectTestButton -> {
                val isAllGranted = getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    SppAutoConnectTestActivity.activityStart(this)
                }
            }
            R.id.bleAutoConnectTestButton -> {
                val isAllGranted = getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    BleAutoConnectTestActivity.activityStart(this)
                }
            }
            R.id.sdp_service_switch_view -> {

                if (sdp_service_switch_view.isOpened) {
                    val customUuid =
                        "${custom_uuid8_et.text}-${custom_uuid4_et1.text}-${custom_uuid4_et2.text}-${custom_uuid4_et3.text}-${custom_uuid12_et.text}"
                    if (custom_uuid8_et.text.isNotEmpty() &&
                        custom_uuid8_et.text.length == 8 &&
                        custom_uuid8_et.text.matches(hexRegex.toRegex()) &&
                        custom_uuid4_et1.text.isNotEmpty() &&
                        custom_uuid4_et1.text.length == 4 &&
                        custom_uuid4_et1.text.matches(hexRegex.toRegex()) &&
                        custom_uuid4_et2.text.isNotEmpty() &&
                        custom_uuid4_et2.text.length == 4 &&
                        custom_uuid4_et2.text.matches(hexRegex.toRegex()) &&
                        custom_uuid4_et3.text.isNotEmpty() &&
                        custom_uuid4_et3.text.length == 4 &&
                        custom_uuid4_et3.text.matches(hexRegex.toRegex()) &&
                        custom_uuid12_et.text.isNotEmpty() &&
                        custom_uuid12_et.text.length == 12 &&
                        custom_uuid12_et.text.matches(hexRegex.toRegex())
                    ) {
                        putStr("customUuid", customUuid)
                        sFscSppCentralApi.openSdpService(customUuid)
                        putBoolean("sdpServiceSwitchOpen", true)
                        custom_uuid8_et.isEnabled = false
                        custom_uuid4_et1.isEnabled = false
                        custom_uuid4_et2.isEnabled = false
                        custom_uuid4_et3.isEnabled = false
                        custom_uuid12_et.isEnabled = false
                    } else {
                        putBoolean("sdpServiceSwitchOpen", false)
                        sdp_service_switch_view.isOpened = false
                        ToastUtil.show(this, getString(R.string.none))
                    }
                } else {
                    putBoolean("sdpServiceSwitchOpen", false)
                    sdp_service_switch_view.isOpened = false
                    sFscSppCentralApi.closeSdpService()
                    custom_uuid8_et.isEnabled = true
                    custom_uuid4_et1.isEnabled = true
                    custom_uuid4_et2.isEnabled = true
                    custom_uuid4_et3.isEnabled = true
                    custom_uuid12_et.isEnabled = true
                }
            }
            R.id.restore_uuid_acb -> {
                custom_uuid8_et.text.clear()
                custom_uuid4_et1.text.clear()
                custom_uuid4_et2.text.clear()
                custom_uuid4_et3.text.clear()
                custom_uuid12_et.text.clear()
            }
        }
    }

    companion object {
        const val hexRegex = "^[0-9A-Fa-f]+$"
        fun activityStart(context: Context) {
            val intent = Intent(context, AdvancedSettingsListActivity::class.java)
            context.startActivity(intent)
        }
    }

}