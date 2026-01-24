package com.feasycom.feasyblue.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.graphics.Rect
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feasycom.ble.controler.FscBleCentralApi
import com.feasycom.ble.controler.FscBleCentralApiImp
import com.feasycom.ble.controler.FscBleCentralCallbacksImp
import com.feasycom.common.bean.ConnectType
import com.feasycom.common.bean.FscDevice
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.activity.*
import com.feasycom.feasyblue.adapter.DeviceAdapter
import com.feasycom.feasyblue.dialog.LoadDialogFragment
import com.feasycom.feasyblue.dialog.MessageDialog
import com.feasycom.feasyblue.dialog.WFBaseDialog
import com.feasycom.feasyblue.interfaces.FeasyWiFiCallbacks
import com.feasycom.feasyblue.logic.BluetoothRepository
import com.feasycom.feasyblue.utils.*
import com.feasycom.spp.controler.FscSppCentralApi
import com.feasycom.spp.controler.FscSppCentralApiImp
import com.feasycom.spp.controler.FscSppCentralCallbacksImp
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.fragment_communication.*
import kotlinx.android.synthetic.main.fragment_communication.toolbarTitle
import kotlinx.android.synthetic.main.setting_spp_connect_test.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CommunicationFragment : BaseFragment(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener, FeasyWiFiCallbacks {

    private lateinit var mWaitDialog: LoadDialogFragment
    private lateinit var mMessageDialog: WFBaseDialog
    private var filterRssi: Int = -100
    private var filterNameSwitch: Boolean = false
    private var filterName: String = ""

    private val mHandler = Handler(Looper.getMainLooper())

    private val devices = mutableListOf<FscDevice>()
    private var mFscDevice: FscDevice? = null

    private var isWiFiParamSetting = false
    private var isSppTest = false
    private var isBleTest = false

    private val sFscSppCentralApi: FscSppCentralApi by lazy {
        FscSppCentralApiImp.getInstance(requireContext())
    }

    private val mFscBleCentralApi: FscBleCentralApi by lazy {
        FscBleCentralApiImp.getInstance(requireContext())
    }

    override fun getLayoutId() = R.layout.fragment_communication

    private var mDeviceAdapter: DeviceAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        activity?.let {
            when (it) {
                is MainActivity -> {
                    toolbarTitle.text = getString(R.string.communication)
                }
                is ParameterModificationDeviceListActivity -> {
                    toolbarTitle.text = getString(R.string.parameterDefining)
                }
                is OtaSearchDeviceActivity -> {
                    toolbarTitle.text = getString(R.string.OTAUpgrade)
                }
                is WiFiSearchDeviceActivity -> {
                    toolbarTitle.text = getString(R.string.wifi_param_setting)
                    isWiFiParamSetting = true
                }
                is SppAutoConnectTestActivity -> {
                    toolbarTitle.text = getString(R.string.spp_auto_connect_test)
                    isSppTest = true
                }
                is BleAutoConnectTestActivity -> {
                    toolbarTitle.text = getString(R.string.ble_auto_connect_test)
                    isBleTest = true
                }
                else -> toolbarTitle.text = getString(R.string.communication)
            }

            if (it is MainActivity) {
                toolbarImageButton.visibility = View.GONE
            } else {
                toolbarImageButton.visibility = View.VISIBLE
            }
        }
        MsgLogger.e("initView: 设置为true")
        BluetoothRepository.registerViewCallback(this)
        mWaitDialog = LoadDialogFragment(getString(R.string.connectings))
        if (isWiFiParamSetting) {
            bleCheck.isChecked = true
            sppCheck.visibility = View.GONE
        } else if (isSppTest) {
            sppCheck.isChecked = true
            bleCheck.visibility = View.GONE
        } else if (isBleTest) {
            bleCheck.isChecked = true
            sppCheck.visibility = View.GONE
        } else {
            sppCheck.isChecked = true
        }

        mDeviceAdapter = DeviceAdapter(devices)
        mDeviceAdapter?.mOnClickListener = { position ->
            stopScan()
            activity?.let {
                when (it) {
                    is MainActivity -> {
                        if (devices.isNotEmpty()) {
                            ThroughputActivity.activityStart(requireContext(), devices[position])
                        }
                    }
                    is ParameterModificationDeviceListActivity -> {
                        ParameterModifyInformationActivity.activityStart(
                            requireContext(), devices[position]
                        )
                    }
                    is OtaSearchDeviceActivity -> {
                        OtaActivity.activityStart(requireContext(), devices[position])
                    }
                    is WiFiSearchDeviceActivity -> {
                        if (BluetoothRepository.isBtEnabled()) {
                            mFscDevice = devices[position]
                            MsgLogger.e("BLE WiFiSearchDeviceActivity...mFscDevice name => ${mFscDevice?.name}")
                            if (mFscDevice!!.name != null /*&& mFscDevice!!.name.contains("BW")*/) {
                                mWaitDialog.show(
                                    requireActivity().supportFragmentManager,
                                    "connect"
                                )
                                BluetoothRepository.connect(devices[position].address)
                            } else {
                                mMessageDialog = MessageDialog.Builder(requireContext())
                                    .setMessage(getString(R.string.not_wifi_device))
                                    .setConfirm(getString(R.string.ok))
                                    .show()
                            }
                        } else {
                            mMessageDialog = MessageDialog.Builder(requireContext())
                                .setMessage(getString(R.string.open_bluetooth_tips)) // 确定按钮文本
                                .setConfirm(getString(R.string.ok)) // 设置点击按钮后不关闭对话框
                                .show()
                        }

                    }
                    is SppAutoConnectTestActivity -> {
                        SppConnectTestActivity.activityStart(requireContext(), devices[position])
                    }
                    is BleAutoConnectTestActivity -> {
                        BleConnectTestActivity.activityStart(requireContext(), devices[position])
                    }
                }
            }
        }

        with(deviceRecyclerView) {
            // 解决局部刷新导致页面闪烁的问题
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mDeviceAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    with(UIUtil.dip2px(context, 1.0)) {
                        outRect.top = this
                        outRect.bottom = this
                        outRect.left = this
                        outRect.right = this
                    }
                }
            })
        }

        smartRefreshLayout.setOnRefreshListener {
            devices.clear()
            mDeviceAdapter?.notifyDataSetChanged()
            val isAllGranted = requireContext().getBoolean("isAllGranted", false)
            if (isAllGranted) {
                startScan()
            }
            it.closeHeaderOrFooter()
        }

        bluetooth.visibility = if (mFscBleCentralApi.isEnabled) View.GONE else View.VISIBLE
        val enabled: Boolean = isLocationEnabled(requireContext())
        gps.visibility = if (enabled) View.GONE else View.VISIBLE
        initEvent()
        // openSdpService()
        registerBroadcastReceivers()

    }

    override fun blePeripheralConnected() {
        BluetoothRepository.isDisconnected = false
        lifecycleScope.launch(Dispatchers.Main) {
            if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
                mWaitDialog.dismiss()
                WiFiSettingActivity.activityStart(requireContext(), mFscDevice!!)
            }
        }
    }

    override fun blePeripheralDisconnected() {
        super.blePeripheralDisconnected()
        BluetoothRepository.isDisconnected = true
        lifecycleScope.launch(Dispatchers.Main) {
            if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
                mWaitDialog.dismiss()
                mMessageDialog = MessageDialog.Builder(requireContext())
                    .setMessage(getString(R.string.connection_failed)) // 确定按钮文本
                    .setConfirm(getString(R.string.ok)) // 设置点击按钮后不关闭对话框
                    .show()
            }
        }
    }

    override fun onConnectTimeout() {// 30秒钟连接超时的处理
        super.onConnectTimeout()
        if (::mWaitDialog.isInitialized && mWaitDialog.isVisible) {
            mWaitDialog.dismiss()
            mMessageDialog = MessageDialog.Builder(requireContext())
                .setMessage(getString(R.string.connection_time_out)) // 确定按钮文本
                .setConfirm(getString(R.string.ok)) // 设置点击按钮后不关闭对话框
                .show()
        }
    }

    private fun initEvent() {
        sortLinearLayout.setOnClickListener(this)
        filterLinearLayout.setOnClickListener(this)
        header_right_LL.setOnCheckedChangeListener(this)
        toolbarImageButton.setOnClickListener(this)
        bluetooth_enable.setOnClickListener(this)
        gps_enable.setOnClickListener(this)
        gps_more.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        devices.clear()
        mDeviceAdapter?.notifyDataSetChanged()

        with(requireContext()) {
            filterRssi = getInt("rssiValue", -100)
            filterNameSwitch = getBoolean("nameSwitch", false)
            filterName = if (filterNameSwitch) {
                getStr("nameValue", "")
            } else {
                ""
            }

            if (filterRssi > -100 || filterNameSwitch) {
                filter_iv.setImageResource(R.drawable.icon_filtter_ing)
            } else {
                filter_iv.setImageResource(R.drawable.icon_filtter_none)
            }

        }

        mFscBleCentralApi.setCallbacks(object : FscBleCentralCallbacksImp() {
            // 扫描发现设备的回调
            override fun blePeripheralFound(bleDevice: FscDevice, rssi: Int, record: ByteArray?) {
                super.blePeripheralFound(bleDevice, rssi, record)
//                MsgLogger.e("blePeripheralFound: 发现设备")
                if (bleCheck?.isChecked == true) {
                    uiFoundDevice(bleDevice)
                }
            }

            override fun startScan() {
                super.startScan()
                MsgLogger.e("blePeripheralFound: 开始扫描")
                if (bleCheck?.isChecked == true) {
                    uiStartScan()
                }
            }

            override fun stopScan() {
                super.stopScan()
                MsgLogger.e("blePeripheralFound: 停止扫描")
                if (bleCheck?.isChecked == true) {
                    uiStopScan()
                }
            }
        })

        sFscSppCentralApi.setCallbacks(object : FscSppCentralCallbacksImp() {
            override fun sppPeripheralFound(sppDevice: FscDevice, rssi: Int) {
//                MsgLogger.e("sppPeripheralConnected: 发现设备  ${sppDevice.address}")
                if (sppCheck?.isChecked == true) {
                    uiFoundDevice(sppDevice)
                }
            }

            override fun startScan() {
                MsgLogger.e("sppPeripheralConnected: 开始扫描")
                if (sppCheck?.isChecked == true) {
                    uiStartScan()
                }

            }

            override fun stopScan() {
                MsgLogger.e("sppPeripheralConnected: 停止扫描")
                if (sppCheck?.isChecked == true) {
                    uiStopScan()
                }
            }

            override fun sppPeripheralConnected(device: BluetoothDevice, connectType: ConnectType) {
                MsgLogger.e("sppPeripheralConnected: 连接成功")
                stopScan()
                ThroughputActivity.activityStart(requireContext(), device)
            }
        })

        val firstPermission = requireContext().getBoolean("firstPermission", false)
        if (!firstPermission) {
            initPermission()
        } else {
            val isAllGranted = requireContext().getBoolean("isAllGranted", false)
            if (isAllGranted){
                startScan()
            }
        }

    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionX.init(this).permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            ).explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(
                    deniedList, getString(R.string.permission_location_prompt), getString(
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
                    requireContext().putBoolean("isAllGranted", true)
                    startScan()
                } else {
                    requireContext().putBoolean("firstPermission", true)
                }
            }
        } else {
            PermissionX.init(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                    scope.showRequestReasonDialog(
                        deniedList, getString(R.string.permission_location_prompt), getString(
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
                        requireContext().putBoolean("isAllGranted", true)
                        startScan()
                    } else {
                        requireContext().putBoolean("firstPermission", true)
                    }

                }
        }
    }

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sortLinearLayout -> {

                if (!ClickUtil.isFastClick()) {
                    with(devices) {
                        val pairedDevices = filter {
                            it.device.bondState == BluetoothDevice.BOND_BONDED
                        }
                        val unpairedDevice = filterNot {
                            it.device.bondState == BluetoothDevice.BOND_BONDED
                        }.sortedByDescending {
                            it.rssi
                        }
                        clear()
                        addAll(pairedDevices)
//
//                        for (i in pairedDevices.indices){
//                            MsgLogger.e("rssi1 => ${pairedDevices[i].rssi}    name => ${pairedDevices[i].name}  address => ${pairedDevices[i].address}  i => $i")
//                        }

                        addAll(unpairedDevice)
//
//                        for (i in unpairedDevice.indices){
//                            MsgLogger.e("rssi2 => ${unpairedDevice[i].rssi}    name => ${unpairedDevice[i].name}  address => ${unpairedDevice[i].address}  i => $i")
//                        }
                    }
                    mDeviceAdapter?.notifyDataSetChanged()
                }

            }
            R.id.filterLinearLayout -> {
                FilterActivity.activityStart(requireContext())
            }
            R.id.toolbarImageButton -> {
                MainActivity.activityStart(requireContext(), 1)
                requireActivity().finish()
            }
            R.id.bluetooth_enable -> {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, 1)
            }
            R.id.gps_enable -> {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 2)
            }
            R.id.gps_more -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.gps_more_title)) //设置对话框标题
                    .setMessage(getString(R.string.gps_more_text_bluetooth)) //设置显示的内容
                    .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    .show() //显示此对话框
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        devices.clear()
        mDeviceAdapter?.notifyDataSetChanged()

        val isAllGranted = requireContext().getBoolean("isAllGranted", false)
        if (isAllGranted) {
            startScan()
        }
    }

    private fun startScan() {
        try {
            if (bluetooth.visibility == View.VISIBLE || gps.visibility == View.VISIBLE) {
                return
            }
            if (header_right_LL.checkedRadioButtonId == R.id.bleCheck) {
                MsgLogger.d("startScan ble扫描")
                sFscSppCentralApi.stopScan()
                mHandler.removeCallbacks(mScanSppRunnable)
                mHandler.postDelayed(mScanBleRunnable, 50)
                uiStartScan()
            } else {
                MsgLogger.d("startScan spp扫描")
                mHandler.removeCallbacks(mScanBleRunnable)
                mHandler.postDelayed(mScanSppRunnable, 50)
                uiStartScan()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val mScanBleRunnable = Runnable {
        mFscBleCentralApi.startScan()
    }

    private val mScanSppRunnable = Runnable {
        sFscSppCentralApi.startScan()
    }

    private fun stopScan() {
        try {
            if (bluetooth.visibility == View.VISIBLE || gps.visibility == View.VISIBLE) {
                return
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } finally {
            try {
                sFscSppCentralApi.stopScan()
                mFscBleCentralApi.stopScan()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun uiStartScan() {
        if (isAdded) {
            toolbarSubtitle.text = resources.getString(R.string.searching)
        }
    }

    private fun uiStopScan() {
        if (isAdded) {
            toolbarSubtitle?.text = resources.getString(R.string.searched)
        }
    }

    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    private fun uiFoundDevice(fscDevice: FscDevice) {
        if (isAdded) {
            if (fscDevice.rssi == 127) return
            if (filterNameSwitch) {
                if (filterName.isNotEmpty()) {
                    try {
                        if (fscDevice.device.name == null) return
                        if (!fscDevice.device.name.uppercase()
                                .contains(filterName.uppercase())
                        ) return
                    }catch (e: NullPointerException){
                        e.printStackTrace()
                    }
                }
            }
            if (fscDevice.rssi > filterRssi) {
                val index = devices.indexOf(fscDevice)
                if (index == -1) {
                    devices.add(fscDevice)
                    deviceRecyclerView?.adapter?.notifyItemChanged(devices.size - 1)
                } else {
                    devices[index] = fscDevice
                    deviceRecyclerView?.adapter?.notifyItemChanged(index)
                }
            }
        }
    }

    /**
     * Broadcast receiver to monitor the changes in the location provider
     */
    private val mLocationProviderChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val enabled: Boolean = isLocationEnabled(requireContext())
            if (enabled) {
                stopScan()
            }
            gps.visibility = if (enabled) View.GONE else View.VISIBLE
        }
    }

    /**
     * Broadcast receiver to monitor the changes in the bluetooth adapter
     */
    private val mBluetoothStateBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
            val previousState = intent.getIntExtra(
                BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_OFF
            )
            when (state) {
                BluetoothAdapter.STATE_ON -> {
                    // 记录一下，这里添加了stopScan()代码是因为关闭了蓝牙刷新界面后，再重新打开蓝牙没有数据
                    stopScan()
                    MsgLogger.e("==============蓝牙已开启=============")
                    bluetooth.visibility = View.GONE
                }
                BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_OFF -> if (previousState != BluetoothAdapter.STATE_TURNING_OFF && previousState != BluetoothAdapter.STATE_OFF) {
                    stopScan()
                    MsgLogger.e("==============蓝牙已关闭=============")
                    bluetooth.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Register for required broadcast receivers.
     */
    private fun registerBroadcastReceivers() {
        requireContext().registerReceiver(
            mBluetoothStateBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
        if (isMarshmallowOrAbove()) {
            requireContext().registerReceiver(
                mLocationProviderChangedReceiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION)
            )
        }
    }

    /**
     * Unregister for required broadcast receivers.
     */
    private fun unregisterBroadcastReceivers() {
        requireContext().unregisterReceiver(mBluetoothStateBroadcastReceiver)
        if (isMarshmallowOrAbove()) {
            requireContext().unregisterReceiver(mLocationProviderChangedReceiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceivers()
        BluetoothRepository.unRegisterViewCallback(this)
    }


    companion object {
        fun newInstance(): CommunicationFragment {
            return CommunicationFragment()
        }
    }

}