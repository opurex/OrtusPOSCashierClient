package com.feasycom.feasyblue.fragment

import android.view.View
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.activity.*
import com.feasycom.feasyblue.utils.getBoolean
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment(), View.OnClickListener {

    override fun getLayoutId() = R.layout.fragment_setting

    private val sFscSppCentralApi: FscSppCentralApi by lazy {
        FscSppCentralApiImp.getInstance(requireContext())
    }

    override fun initView() {
        toolbarTitle.text = getString(R.string.setting)
        parameterDefining.setOnClickListener(this)
        otaButton.setOnClickListener(this)
        otaBatchButton.setOnClickListener(this)
        wifiSettingButton.setOnClickListener(this)
        advancedSettingButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.parameterDefining -> {
                val isAllGranted = requireContext().getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    PasswordActivity.activityStart(requireContext(),0)
                    requireActivity().finish()
                }
            }
            R.id.otaButton -> {
                val isAllGranted = requireContext().getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    OtaSearchDeviceActivity.activityStart(requireContext())
                }
            }
            R.id.otaBatchButton -> {
                val isAllGranted = requireContext().getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    BatchOtaActivity.activityStart(requireContext())
                }
            }
            R.id.wifiSettingButton -> {
                val isAllGranted = requireContext().getBoolean("isAllGranted", false)
                if (isAllGranted){
                    WiFiSearchDeviceActivity.activityStart(requireContext())
                }

            }
            R.id.advancedSettingButton -> {
                val isAllGranted = requireContext().getBoolean("isAllGranted", false)
                if (isAllGranted) {
                    PasswordActivity.activityStart(requireContext(),1)
                    requireActivity().finish()
                }
            }
        }
    }

    companion object {

        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}