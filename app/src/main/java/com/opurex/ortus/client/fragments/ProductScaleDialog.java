package com.opurex.ortus.client.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.preference.PreferenceManager;

import com.example.data.St_PSData;
import com.example.scaler.AclasScaler;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.utils.ScaleManager;
import com.opurex.ortus.client.utils.scale.BatteryUtil;
import com.opurex.ortus.client.utils.scale.LogUtil;
import com.opurex.ortus.client.utils.scale.SharePrefenceUtil;
import com.opurex.ortus.client.utils.scale.UtilPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductScaleDialog extends DialogFragment implements ScaleManager.ScaleWeightListener, ScaleManager.ConnectionStateListener {

    public static final String TAG = "ProductScaleDialog";

    private static final String ARG_PRODUCT = "arg_product";
    private static final String ARG_IS_RETURN = "arg_is_return";

    private Listener mListener;
    private Product mProd;
    private boolean mIsProductReturn;

    private EditText weightInput;
    private TextView weightDisplay;
    private TextView priceDisplay;
    private TextView statusDisplay;
    private Button zeroButton;
    private Button tareButton;

    private ScaleManager scaleManager;
    private ScaleManager externalScaleManager; // External ScaleManager passed from caller
    private boolean isScaleConnected = false;

    // Fields for AclasScaler functionality
    private AclasScaler m_scaler = null;
    private AclasScaler.WeightInfoNew m_weight = null;
    private String m_strName = "";
    private String m_strMac = "";
    private final int PAGE_MAIN = 0;
    private final int PAGE_PAIR = 1;
    private int m_iPage = -1;
    private ProgressDialog m_Dialog = null;
    private int m_iWaitVal = 0;

    // UI elements for device selection
    private ListView m_lvList;
    private SimpleAdapter m_listAdapter;
    private List<HashMap<String,String>> m_listData;
    private List<String> m_listMac;
    private LinearLayout m_btnBack;
    private LinearLayout m_ltMain;
    private LinearLayout m_ltPair;
    private TextView m_tvTitle;
    private TextView m_tvName;
    private TextView m_tvMac;
    private TextView m_tvWeight;
    private TextView m_tvUnit;
    private TextView m_tvPrice;
    private TextView m_tvTotal;
    private TextView m_tvTare;
    private TextView m_tvKey;
    private TextView m_tvTareUnit;

    // Permissions
    private String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    };

    public interface Listener {
        void onPsdPositiveClick(Product p, double weight, boolean isProductReturned);
    }

    public static ProductScaleDialog newInstance(Product p, boolean isProductReturn) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, p);
        args.putBoolean(ARG_IS_RETURN, isProductReturn);
        ProductScaleDialog dialog = new ProductScaleDialog();
        dialog.setArguments(args);
        return dialog;
    }

    // Overloaded method to accept external ScaleManager
    public static ProductScaleDialog newInstance(Product p, boolean isProductReturn, ScaleManager scaleManager) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, p);
        args.putBoolean(ARG_IS_RETURN, isProductReturn);
        ProductScaleDialog dialog = new ProductScaleDialog();
        dialog.setArguments(args);
        dialog.externalScaleManager = scaleManager; // Store the external ScaleManager
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Set the listener from the parent fragment only if no external listener was set beforehand
        if (mListener == null) { // Only set target fragment as listener if no external listener was set via setDialogListener
            if (getTargetFragment() instanceof Listener) {
                mListener = (Listener) getTargetFragment();
            } else if (getActivity() instanceof Listener) {
                // Check if the calling activity implements the listener interface
                mListener = (Listener) getActivity();
            } else {
                throw new ClassCastException("Calling fragment or activity must implement ProductScaleDialog.Listener");
            }
        }
    }

    // Method to set an external listener (used when called from Transaction)
    public void setDialogListener(Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProd = (Product) getArguments().getSerializable(ARG_PRODUCT);
            mIsProductReturn = getArguments().getBoolean(ARG_IS_RETURN);
        }
        // Use external ScaleManager if provided, otherwise create a new one
        if (externalScaleManager != null) {
            scaleManager = externalScaleManager;
        } else {
            // The dialog is responsible for creating its own ScaleManager
            scaleManager = new ScaleManager(requireContext());
        }

        // Initialize AclasScaler
        InitDevice(AclasScaler.Type_FSC);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_product_scale, null);
        weightInput = view.findViewById(R.id.scale_input);
        weightDisplay = view.findViewById(R.id.scale_weight_display);
        priceDisplay = view.findViewById(R.id.scale_price_display);
        statusDisplay = view.findViewById(R.id.scale_status_display);
        zeroButton = view.findViewById(R.id.scale_zero_button);
        tareButton = view.findViewById(R.id.scale_tare_button);
        
        weightInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                updatePriceDisplayFromInput();
            }
        });
        
        zeroButton.setOnClickListener(v -> zeroScale());
        tareButton.setOnClickListener(v -> tareScale());
        
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(mProd.getLabel());
        alertDialogBuilder
                .setIcon(R.drawable.scale)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    String weightString = weightInput.getText().toString();
                    if (mListener != null && !TextUtils.isEmpty(weightString)) {
                        double weight = Double.parseDouble(weightString);
                        if (weight > 0) {
                            mListener.onPsdPositiveClick(mProd, weight, mIsProductReturn);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
                
        // Find the pair button and set click listener
        Button pairButton = view.findViewById(R.id.btn_pair);
        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPairButtonClick();
            }
        });

        return alertDialogBuilder.create();
    }

    // Handle the pair button click similar to BluetoothScaleSelectionActivity
    private void onPairButtonClick() {
        CloseScale();
        InitDevice(AclasScaler.Type_FSC); //250613
        searchDev();
        showPage(PAGE_PAIR);
    }

    @Override
    public void onResume() {
        super.onResume();
        scaleManager.setScaleWeightListener(this);
        scaleManager.setConnectionStateListener(this);
//        isScaleConnected = scaleManager.isConnected();
        updateScaleStatus();
    }

    /**
     * 初始化秤  设置回调
     * @param iType  AclasScaler.Type_FSC
     */
    private void InitDevice(int iType) {
        LogUtil.info("ProductScaleDialog InitDevice:" + iType);
        if(m_scaler == null){
            m_scaler = new AclasScaler(iType, requireContext(), m_listener);
            m_scaler.setAclasPSXListener(m_listenerPS); //设置 单价总价按键 的回调
            m_scaler.setBluetoothListener(m_listenerBt); //搜索蓝牙回调
            m_weight = m_scaler.new WeightInfoNew();
            m_scaler.setLog(true);
        }
    }

    private void searchDev(){
        if(m_scaler != null){
            if (m_listMac != null) m_listMac.clear();
            if (m_listData != null) m_listData.clear();
            if (m_listAdapter != null) m_listAdapter.notifyDataSetChanged();
            m_scaler.startScanBluetooth(true);
        }
    }

    /**
     * 显示界面
     * @param index  PAGE_MAIN 秤数据界面;  PAGE_PAIR 秤搜索和连接界面
     */
    private void showPage(int index){
        // For now, just log the page change since we're in a dialog
        LogUtil.info("ProductScaleDialog showPage: " + index);
        switch (index){
            case PAGE_MAIN:
                LogUtil.info("ProductScaleDialog showPage showWait false");
                showWait(false);
                m_iPage = PAGE_MAIN;
                // Update UI elements if they exist
                if (m_tvTitle != null) m_tvTitle.setText("Scale Data");
                if (m_btnBack != null) m_btnBack.setVisibility(View.INVISIBLE);
                if (m_ltMain != null) m_ltMain.setVisibility(View.VISIBLE);
                if (m_ltPair != null) m_ltPair.setVisibility(View.GONE);
                break;
            case PAGE_PAIR:
                m_iPage = PAGE_PAIR;
                // Update UI elements if they exist
                if (m_tvTitle != null) m_tvTitle.setText("Pair Scale");
                if (m_ltPair != null) m_ltPair.setVisibility(View.VISIBLE);
                if (m_btnBack != null) m_btnBack.setVisibility(View.VISIBLE);
                if (m_ltMain != null) m_ltMain.setVisibility(View.GONE);
                break;
        }
    }

    private void CloseScale(){
        if(m_scaler != null){
            m_scaler.AclasDisconnect();
            m_scaler = null; //250613
        }
    }

    private void checkPower(final String mac, final String name){
        LogUtil.info("ProductScaleDialog checkPower with mac: " + mac + ", name: " + name);
        if(BatteryUtilProductScaleDialog.INSTANCE.checkOptimization(requireActivity(), requireActivity().getPackageName())){
            OpenScale(mac, name);
        }
    }

    private void checkPower(){
        LogUtil.info("ProductScaleDialog checkPower");
        if(BatteryUtilProductScaleDialog.INSTANCE.checkOptimization(requireActivity(), requireActivity().getPackageName())){
            OpenScale();
        }
    }

    /**
     * 打开秤
     */
    private void OpenScale(){
        if (m_scaler != null) {
            String mac = getAddress();
            String name = getName();

            m_strName = name;
            m_strMac = mac;
            if(mac != null && mac.contains(":")){
                OpenScale("", name);
            }
        }
    }

    /**
     *打开秤
     * @param mac
     * @param name
     */
    private void OpenScale(final String mac, final String name){
        if(!mac.isEmpty()){
            m_strName = name;
            m_strMac = mac;
        }
        if (m_weight != null) {
            m_weight.init(); //初始化重量数据
        }
        // Clear data
        clearData();

        // Run in background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int iRet = -1;

                //已经配对的 mac传入"",未配对的传入mac地址
                if (m_scaler != null) {
                    iRet = m_scaler.AclasConnect(mac);
                    if(iRet == 0){ //连接成功 存储非空 蓝牙信息
                        if(!mac.isEmpty()){
                            saveAddress(mac);
                            setName(name);
                        }
                    }else{
                        saveAddress("");
                    }
                }
                LogUtil.info("ProductScaleDialog OpenScale in thread closeWait ret:" + iRet);
                closeWaitDlg(m_iWaitVal, 0);

                // Send message to UI thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = (iRet == 0 ? "Connected" : "Connection failed") + ":" + mac;
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void clearData(){
        if (m_tvKey != null) m_tvKey.setText("");
        if (m_tvTare != null) m_tvTare.setText("");
        if (m_tvTotal != null) m_tvTotal.setText("");
        if (m_tvPrice != null) m_tvPrice.setText("");
        if (m_tvWeight != null) m_tvWeight.setText("");
    }

    /**
     * 存储 mac
     * @param strMac
     */
    private void saveAddress(String strMac){
        SharePrefenceUtil.setAddress(strMac);
    }

    /**
     * 读取mac
     * @return mac 或者 ""
     */
    private String getAddress(){
        return SharePrefenceUtil.getAddress();
    }

    /**
     * 读取 蓝牙秤名称
     * @return 蓝牙秤名称或者 ""
     */
    private String getName(){
        return SharePrefenceUtil.getName();
    }

    /**
     * 存储蓝牙秤名称
     * @param name
     */
    private void setName(String name){
        SharePrefenceUtil.setName(name);
    }

    private void closeWaitDlg(int iVal, int iDelay){
        LogUtil.info("ProductScaleDialog closeWaitDlg val:" + iVal + " delay:" + iDelay);
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_WAIT, iVal, 0);
            if(iDelay > 0){
                handler.sendMessageDelayed(msg, iDelay);
            }else {
                handler.sendMessage(msg);
            }
        }
    }

    private void handleWaitDlg(){
        LogUtil.info("ProductScaleDialog handleWaitDlg");
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_WAIT, 0, 1);
            handler.sendMessageDelayed(msg, 50);
        }
    }

    /**
     * 等待界面 的显示与隐藏
     * @param bShow  true 显示;  false 隐藏;
     */
    private void showWait(boolean bShow){
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(bShow){
                    if(m_Dialog == null){
                        m_iWaitVal++;
                        m_Dialog = ProgressDialog.show(getActivity(), "",
                                "Connecting...", false, false, null);

                        closeWaitDlg(m_iWaitVal, 30000);
                    }
                }else{
                    if(m_Dialog != null){
                        m_Dialog.dismiss();
                        m_Dialog = null;
                    }
                }
            }
        });
    }

    // Message constants
    private final int MSG_Connect = 0;
    private final int MSG_Disconn = 1;
    private final int MSG_Msg = 6;
    private final int MSG_WAIT = 7;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_Connect:
                    if(PAGE_MAIN != m_iPage){
                        if (m_tvName != null && m_tvMac != null) {
                            showDevInfo(m_strMac, m_strName);
                        }
                        showPage(PAGE_MAIN);
                        showBtVersion();
                    }
                    break;
                case MSG_Disconn:
                    //秤解绑了。需要重新绑定AclasConnect传入mac地址;
                    LogUtil.info("ProductScaleDialog unbind");
                    if (m_tvName != null && m_tvMac != null) {
                        showDevInfo("", "");
                    }
                    saveAddress("");
                    break;
                case MSG_Msg:
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        getContext(),
                                        (String)msg.obj,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case MSG_WAIT:
                    LogUtil.info("ProductScaleDialog MSG_WAIT arg1:" + msg.arg1 + " m_iWaitVal:" + m_iWaitVal + " arg2:" + msg.arg2);
                    if(msg.arg2 == 1){
                        showWait(true);
                    }else{
                        if(m_Dialog != null){
                            if(msg.arg1 == m_iWaitVal){
                                showWait(false);
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     *  界面显示已连接蓝牙信息
     * @param mac
     * @param name
     */
    private void showDevInfo(String mac, String name){
        if (m_tvName != null) m_tvName.setText("Device Name: " + name);
        if (m_tvMac != null) m_tvMac.setText("Device MAC: " + mac);
    }

    /**
     * 显示蓝牙模组版本
     */
    private void showBtVersion(){
        if(m_scaler != null){
            String strVer = m_scaler.AclasFirmwareVersion();
            LogUtil.info("ProductScaleDialog BT Version: " + strVer);
        }
    }

    /**
     * 蓝牙秤搜索回调
     */
    private AclasScaler.AclasBluetoothListener m_listenerBt = new AclasScaler.AclasBluetoothListener() {
        @Override
        public void onSearchBluetooth(String s) {
            LogUtil.info("ProductScaleDialog onSearchBluetooth:" + s);
            if(m_iPage == PAGE_PAIR){
                addDev(s);
            }
        }

        @Override
        public void onSearchFinish() {
            LogUtil.info("ProductScaleDialog onSearchFinish");
            handleMsg("onSearchFinish");
        }
    };

    private AclasScaler.AclasScalerListener m_listener = new AclasScaler.AclasScalerListener() {
        @Override
        public void onError(int errornum, String str) {
            String info = "onError: " + errornum + " str:" + str;
            handleMsg(info);
        }

        @Override
        public void onDisConnected() {
            handler.sendEmptyMessage(MSG_Disconn);
            handleMsg("Scale disconnected");
            LogUtil.info("ProductScaleDialog onDisConnected ");
        }

        @Override
        public void onConnected() {
            LogUtil.info("ProductScaleDialog onConnected");
            handler.sendEmptyMessage(MSG_Connect);
        }

        public void onRcvData(AclasScaler.WeightInfoNew info) {
            if(setWeightInfo(info)){
                showWeight(getWeightInfo());
            }
        }

        /**
         * 忽略
         */
        public void onUpdateProcess(int iIndex, int iTotal){
        }
    };

    private St_PSData m_psdata = new St_PSData();
    private AclasScaler.AclasScalerPSXListener m_listenerPS = new AclasScaler.AclasScalerPSXListener() {
        @Override
        public void onRcvData(St_PSData st_psData) {
            m_psdata.setData(st_psData);
            //String strInfo = String.format("price:%.3f amt:%.3f key:%d",st_psData.dPrice,st_psData.dAmount,st_psData.iKeyVal);
            //showLog(strInfo);

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPSData();
                    }
                });
            }
        }
    };

    /**
     * 界面显示单价总价按键
     */
    private void showPSData(){
        if (m_weight == null) return;

        boolean bNegative = m_weight.netWeight < 0;
        if (m_tvPrice != null) m_tvPrice.setText(getDoubleString(m_psdata.dPrice, m_psdata.iDotPrice));
        if (m_tvTotal != null) m_tvTotal.setText((bNegative ? "----" : getDoubleString(m_psdata.dAmount, m_psdata.iDotAmount)));
    }

    /**
     *
     * @param dVal  数值
     * @param iDot  小数位数
     * @return  字符串
     */
    private String getDoubleString(double dVal, int iDot){
        String str = null;

        switch (iDot){
            case 0:
                str = String.format("%d ",(int)dVal);
                break;
            case 1:
                str = String.format("%.1f ",dVal);
                break;
            case 2:
                str = String.format("%.2f ",dVal);
                break;
            default:
                str = String.format("%.3f ",dVal);
                break;
        }
        return str;
    }

    private synchronized boolean setWeightInfo(AclasScaler.WeightInfoNew info){
        if (m_weight != null) {
            return m_weight.setData(info);
        }
        return false;
    }

    private synchronized AclasScaler.WeightInfoNew getWeightInfo(){
        return m_weight;
    }

    /**
     * 显示 重量值 状态 皮重值
     */
    private void showWeight(final AclasScaler.WeightInfoNew info){
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(info.isOverWeight){
                    if (m_tvWeight != null) m_tvWeight.setText("----");
                }else{
                    LogUtil.info("ProductScaleDialog rec:" + info.toString() + " decimal:" + info.iDecimal);
                    String strNet = info.toString();
                    if (m_tvWeight != null) m_tvWeight.setText((info.isStable ? "S " : "U ") + strNet);

                    // Update the weight input field with the current weight
                    if (weightInput != null && info != null) {
                        String weightValue = String.format("%.3f", info.netWeight);
                        weightInput.setText(weightValue);
                        updatePriceDisplayFromInput(); // Update price based on new weight
                    }

                    if (m_tvTare != null) {
                        switch (info.iDecimal){
                            case 0:
                                m_tvTare.setText(String.format("%d ",(int)info.tareWeight) + info.unit);
                                break;
                            case 1:
                                m_tvTare.setText(String.format("%.1f ",info.tareWeight) + info.unit);
                                break;
                            case 2:
                                m_tvTare.setText(String.format("%.2f ",info.tareWeight) + info.unit);
                                break;
                            default:
                                m_tvTare.setText(String.format("%.3f ",info.tareWeight) + info.unit);
                                break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 添加蓝牙秤
     * @param info  蓝牙搜索回调信息    "name,mac"
     */
    private void addDev(String info){
        if(info.contains(",")){
            String[] list = info.split(",");
            addDev(list[0], list[1], list[2] + "db");
        }
    }

    /**
     * 添加蓝牙秤
     * @param name  蓝牙秤名称
     * @param mac   蓝牙秤mac
     * @param value 信号值
     */
    private void addDev(String name, String mac, String value){
        if (m_listData == null) m_listData = new ArrayList<>();
        if (m_listMac == null) m_listMac = new ArrayList<>();

        HashMap<String,String> map = new HashMap<>();
        map.put("NAME", name);
        map.put("MAC", mac);
        map.put("SIG", value);
        int index = m_listMac.indexOf(mac);
        if(index >= 0){
            m_listData.remove(index);
            m_listData.add(index, map);
        }else{
            m_listData.add(map);
            m_listMac.add(mac);
        }
        if (m_listAdapter != null) m_listAdapter.notifyDataSetChanged();
    }

    /**
     * 显示Toast
     * @param info 信息内容
     */
    private void handleMsg(String info){
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_Msg, info);
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scaleManager.setScaleWeightListener(null);
        scaleManager.setConnectionStateListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.info("ProductScaleDialog onDestroy()");
        CloseScale();
    }
    
    private void updateScaleStatus() {
        if (isScaleConnected) {
            statusDisplay.setText("Scale connected");
            statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            zeroButton.setEnabled(true);
            tareButton.setEnabled(true);
            weightInput.setEnabled(true); // Enable input when scale is connected
        } else {
            // Check if manual weight entry is enabled in settings
            boolean manualEntryEnabled = isManualWeightEntryEnabled();
            statusDisplay.setText("Scale not connected" + (manualEntryEnabled ? " - enter weight manually" : ""));

            // Use different colors based on manual entry availability
            if (manualEntryEnabled) {
                // Orange color when scale is not connected but manual entry is available
                statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                // Red color when scale is not connected and manual entry is not available
                statusDisplay.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            // Enable/disable UI elements based on manual entry availability
            zeroButton.setEnabled(manualEntryEnabled);
            tareButton.setEnabled(manualEntryEnabled);
            weightInput.setEnabled(manualEntryEnabled); // Also control the input field based on manual entry setting
        }
    }

    /**
     * Check if manual weight entry is enabled in settings
     */
    private boolean isManualWeightEntryEnabled() {
        if (getContext() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            return prefs.getBoolean("enable_manual_weight_entry", true); // Default to true as requested
        }
        return true; // Default to true if context is not available
    }
    
    private void zeroScale() {
        if (scaleManager != null && isScaleConnected) {
            // Connected mode: use actual scale
         //   scaleManager.zeroScale();
            Toast.makeText(getContext(), "Scale zeroed", Toast.LENGTH_SHORT).show();
        } else {
            // Manual mode: clear the manual input field
            if (isManualWeightEntryEnabled()) {
                weightInput.setText("0.0");
                Toast.makeText(getContext(), "Manual input cleared", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scale not connected and manual entry disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tareScale() {
        if (scaleManager != null && isScaleConnected) {
            // Connected mode: use actual scale
          //  scaleManager.tareScale();
            Toast.makeText(getContext(), "Scale tared", Toast.LENGTH_SHORT).show();
        } else {
            // Manual mode: clear the manual input field (simulate tare)
            if (isManualWeightEntryEnabled()) {
                weightInput.setText("0.0");
                Toast.makeText(getContext(), "Manual input cleared (tare)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scale not connected and manual entry disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void updatePriceDisplayFromInput() {
        String weightStr = weightInput.getText().toString();
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                double weight = Double.parseDouble(weightStr);
                double price = mProd.getPrice() * weight;
                priceDisplay.setText(String.format("Price: %.2f", price));
            } catch (NumberFormatException e) {
                priceDisplay.setText("Invalid weight");
            }
        } else {
            priceDisplay.setText("Enter weight to calculate price");
        }
    }
    
    // --- Callbacks from ScaleManager ---
    
    @Override
    public void onWeightReceived(double weight, String unit) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            String weightText = String.format("%.3f %s", weight, unit);
            weightDisplay.setText("Weight: " + weightText);
            // Auto-fill the input field with the weight from the scale
            weightInput.setText(String.format("%.3f", weight));

            // Also update the price display based on the new weight
            updatePriceDisplayFromInput();
        });
    }

    /**
     * Update weight display when scale is not connected
     */
    private void updateWeightDisplayForDisconnectedState() {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            boolean manualEntryEnabled = isManualWeightEntryEnabled();
            if (manualEntryEnabled) {
                weightDisplay.setText("Enter weight manually in the field below");
                // Keep the input field as is - it will be controlled by updateScaleStatus()
            } else {
                weightDisplay.setText("Scale not connected - manual entry disabled");
            }
        });
    }
    
    @Override
    public void onScaleConnected() {
        isScaleConnected = true;
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                updateScaleStatus();
                // When connected, show waiting message until we get actual weight
                weightDisplay.setText("Waiting for weight data...");
            });
        }
    }

    @Override
    public void onScaleDisconnected() {
        isScaleConnected = false;
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                updateScaleStatus();
                updateWeightDisplayForDisconnectedState(); // Update weight display for disconnected state
            });
        }
    }

    @Override
    public void onScaleError(String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Scale Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
