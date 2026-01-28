package com.opurex.ortus.client.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.data.St_PSData;
import com.example.scaler.AclasScaler;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.utils.scale.BatteryUtil;
import com.opurex.ortus.client.utils.scale.BatteryUtilProductScaleDialog;
import com.opurex.ortus.client.utils.scale.LogUtil;
import com.opurex.ortus.client.utils.scale.SharePrefenceUtil;
import com.opurex.ortus.client.utils.scale.UtilPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ProductScaleDialog - A DialogFragment that handles scale connection, pairing, and weight measurement
 * for scaled products. It takes a Product as input, gets weight from the scale, calculates the total
 * price, and returns the result to the calling activity/fragment via a Listener interface.
 */
public class ProductScaleDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ProductScaleDialog";
    
    // Arguments keys
    private static final String ARG_PRODUCT = "arg_product";
    private static final String ARG_IS_RETURN = "arg_is_return";
    
    // Listener interface for callback
    public interface Listener {
        void onPsdPositiveClick(Product product, double weight, boolean isProductReturned);
        
        /**
         * Optional method for handling negative/cancel actions
         * Default implementation does nothing for backward compatibility
         */
        default void onPsdNegativeClick() {
            // Default empty implementation for backward compatibility
        }
    }
    
    private Listener mListener;
    private Product mProduct;
    private boolean mIsProductReturned;
    
    // UI Components
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
    
    // Scale components
    private AclasScaler m_scaler = null;
    private AclasScaler.WeightInfoNew m_weight = null;
    private boolean m_bInitFinish = false;
    
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
    
    // Page constants
    private final int PAGE_MAIN = 0;
    private final int PAGE_PAIR = 1;
    private int m_iPage = -1;
    
    // State tracking
    private String m_strName = "";
    private String m_strMac = "";
    
    // Weight and calculation tracking
    private double m_currentWeight = 0.0;
    private St_PSData m_psdata = new St_PSData();
    
    /**
     * Factory method to create a new instance of ProductScaleDialog
     * @param product The product to weigh
     * @param isReturnProduct Whether this is a product return
     * @return A new ProductScaleDialog instance
     */
    public static ProductScaleDialog newInstance(Product product, boolean isReturnProduct) {
        ProductScaleDialog dialog = new ProductScaleDialog();
        
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putBoolean(ARG_IS_RETURN, isReturnProduct);
        dialog.setArguments(args);
        
        return dialog;
    }
    
    /**
     * Set the listener for dialog callbacks
     * @param listener The listener to receive callbacks
     */
    public void setDialogListener(Listener listener) {
        mListener = listener;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set dialog style for full screen scale interface
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        
        // Get arguments
        if (getArguments() != null) {
            mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
            mIsProductReturned = getArguments().getBoolean(ARG_IS_RETURN, false);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        View view = inflater.inflate(R.layout.scale_activity_main, container, false);
        
        // Initialize UI components
        initUI(view);
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize scale device
        InitDevice(AclasScaler.Type_FSC);
        
        // Request permissions
        int iRet = UtilPermission.getPermission(permissions, requireContext(), requireActivity());
        if(iRet == permissions.length){
            showLog("onViewCreated getPermission ret:" + iRet);
            checkPower();
        }
        
        // Set product information in UI
        updateProductInfo();
        
        // Show app info
        showAppInfo();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if(m_bInitFinish && m_iPage == PAGE_MAIN){
            showLog("----onResume----");
            checkPower();
        } else {
            showLog("----onResume----m_bInitFinish：" + m_bInitFinish + " Page:" + m_iPage);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onDestroy() {
        showLog("---onDestroy---");
        CloseScale();
        super.onDestroy();
    }
    
    /**
     * Initialize UI components
     */
    private void initUI(View view) {
        m_lvList = view.findViewById(R.id.lv_Devicelist);
        m_listData = new ArrayList<>();
        m_listMac = new ArrayList<>();
        m_listAdapter = new SimpleAdapter(requireContext(), m_listData,
                R.layout.device_layout,
                new String[]{"NAME","MAC","SIG"},
                new int[]{R.id.tv_pair_name, R.id.tv_pair_mac, R.id.tv_pair_sig});
        m_lvList.setAdapter(m_listAdapter);
        m_lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = m_listData.get(position);
                String mac = map.get("MAC");
                String name = map.get("NAME");
                showLog("POS:" + position + " name:" + name + " mac:" + mac);
                checkPower(mac, name);
                handleWaitDlg();
            }
        });
        
        m_btnBack = view.findViewById(R.id.btn_back);
        m_ltMain = view.findViewById(R.id.lt_main);
        m_ltPair = view.findViewById(R.id.lt_pair);
        m_tvTitle = view.findViewById(R.id.tv_title);
        m_tvName = view.findViewById(R.id.tv_dev_name);
        m_tvMac = view.findViewById(R.id.tv_dev_mac);
        m_tvWeight = view.findViewById(R.id.tv_weight);
        m_tvUnit = view.findViewById(R.id.tv_unit);
        m_tvPrice = view.findViewById(R.id.tv_price);
        m_tvTotal = view.findViewById(R.id.tv_total);
        m_tvTare = view.findViewById(R.id.tv_tare);
        m_tvTareUnit = view.findViewById(R.id.tv_unit_tare);
        m_tvKey = view.findViewById(R.id.tv_key);
        
        // Set click listeners
        m_btnBack.setOnClickListener(this);
        view.findViewById(R.id.btn_pair).setOnClickListener(this);
        view.findViewById(R.id.tv_back).setOnClickListener(this);
        view.findViewById(R.id.scale_ok_button).setOnClickListener(this);
        view.findViewById(R.id.scale_cancel_button).setOnClickListener(this);
    }
    
    /**
     * Update product information in UI
     */
    private void updateProductInfo() {
        if (mProduct != null) {
            // Set product price in the price display
            m_tvPrice.setText(String.format("%.3f", mProduct.getPrice()));
        }
    }
    
    /**
     * Show the appropriate page (main scale view or pairing view)
     */
    private void showPage(int index) {
        switch (index) {
            case PAGE_MAIN:
                showLog("showPage showWait false");
                showWait(false);
                m_iPage = PAGE_MAIN;
                m_tvTitle.setText(R.string.title_main);
                m_btnBack.setVisibility(View.INVISIBLE);
                if (m_ltMain != null) m_ltMain.setVisibility(View.VISIBLE);
                if (m_ltPair != null) m_ltPair.setVisibility(View.GONE);
                break;
            case PAGE_PAIR:
                m_iPage = PAGE_PAIR;
                m_tvTitle.setText(R.string.title_pair);
                if (m_ltPair != null) m_ltPair.setVisibility(View.VISIBLE);
                m_btnBack.setVisibility(View.VISIBLE);
                if (m_ltMain != null) m_ltMain.setVisibility(View.GONE);
                break;
        }
    }
    
    /**
     * Add a Bluetooth device to the list
     */
    private void addDev(String name, String mac, String value) {
        HashMap<String,String> map = new HashMap<>();
        map.put("NAME", name);
        map.put("MAC", mac);
        map.put("SIG", value);
        int index = m_listMac.indexOf(mac);
        if(index >= 0) {
            m_listData.remove(index);
            m_listData.add(index, map);
        } else {
            m_listData.add(map);
            m_listMac.add(mac);
        }
        m_listAdapter.notifyDataSetChanged();
    }
    
    /**
     * Add a Bluetooth device from search info
     */
    private void addDev(String info) {
        if(info.contains(",")) {
            String[] list = info.split(",");
            if (list.length >= 3) {
                addDev(list[0], list[1], list[2] + "db");
            }
        }
    }
    
    /**
     * Show device information
     */
    private void showDevInfo(String mac, String name) {
        m_tvName.setText(getString(R.string.dev_name) + name);
        m_tvMac.setText(getString(R.string.dev_mac) + mac);
    }
    
    /**
     * Handle click events
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
            case R.id.tv_back:
                showPage(PAGE_MAIN);
                checkPower();
                showLog("---back btn click---");
                break;
            case R.id.btn_pair:
                CloseScale();
                InitDevice(AclasScaler.Type_FSC);
                searchDev();
                showPage(PAGE_PAIR);
                break;
            case R.id.scale_ok_button:
                onPositiveClick();
                break;
            case R.id.scale_cancel_button:
                onNegativeClick();
                break;
        }
    }
    
    /**
     * Initialize the scale device
     */
    private void InitDevice(int iType) {
        showLog("InitDevice:" + iType);
        if(m_scaler == null) {
            m_scaler = new AclasScaler(iType, requireContext(), m_listener);
            m_scaler.setAclasPSXListener(m_listenerPS);
            m_scaler.setBluetoothListener(m_listenerBt);
            m_weight = m_scaler.new WeightInfoNew();
            m_scaler.setLog(true);
        }
        showBtVersion();
    }
    
    /**
     * Search for Bluetooth devices
     */
    private void searchDev() {
        if(m_scaler != null) {
            m_listMac.clear();
            m_listData.clear();
            m_listAdapter.notifyDataSetChanged();
            m_scaler.startScanBluetooth(true);
        }
    }
    
    /**
     * Check battery optimization and open scale if allowed
     */
    private void checkPower() {
        showLog("-----checkPower-------");
        if(BatteryUtilProductScaleDialog.Companion.checkOptimization(requireActivity(), requireContext().getPackageName())) {
            OpenScale();
            m_bInitFinish = true;
        }
    }
    
    /**
     * Check battery optimization and open scale with specific device
     */
    private void checkPower(final String mac, final String name) {
        showLog("-----checkPower-------");
        if(BatteryUtilProductScaleDialog.Companion.checkOptimization(requireActivity(), requireContext().getPackageName())) {
            OpenScale(mac, name);
            m_bInitFinish = true;
        }
    }
    
    /**
     * Open scale connection
     */
    private void OpenScale() {
        if (m_scaler != null) {
            String mac = getAddress();
            String name = getName();
            
            m_strName = name;
            m_strMac = mac;
            if(mac != null && mac.contains(":")) {
                OpenScale("", name);
            }
        }
    }
    
    /**
     * Open scale connection with specific device
     */
    private void OpenScale(final String mac, final String name) {
        if(!mac.isEmpty()) {
            m_strName = name;
            m_strMac = mac;
        }
        m_weight.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int iRet = -1;
                iRet = m_scaler.AclasConnect(mac);
                if(iRet == 0) {
                    if(!mac.isEmpty()) {
                        saveAddress(mac);
                        setName(name);
                    }
                } else {
                    saveAddress("");
                }
                showLog("OpenScale in thread closeWait ret:" + iRet);
                closeWaitDlg(m_iWaitVal, 0);
                handleMsg((iRet == 0 ? getString(R.string.connect) : getString(R.string.openfailed)) + ":" + mac);
            }
        }).start();
    }
    
    /**
     * Close scale connection
     */
    private void CloseScale() {
        if(m_scaler != null) {
            m_scaler.AclasDisconnect();
            m_scaler = null;
        }
    }
    
    /**
     * Bluetooth listener for device search
     */
    private AclasScaler.AclasBluetoothListener m_listenerBt = new AclasScaler.AclasBluetoothListener() {
        @Override
        public void onSearchBluetooth(String s) {
            showLog("onSearchBluetooth:" + s);
            if(m_iPage == PAGE_PAIR) {
                addDev(s);
            }
        }
        
        @Override
        public void onSearchFinish() {
            showLog("onSearchFinish");
            handleMsg("onSearchFinish");
        }
    };
    
    /**
     * Scale listener for weight data and connection events
     */
    private AclasScaler.AclasScalerListener m_listener = new AclasScaler.AclasScalerListener() {
        @Override
        public void onError(int errornum, String str) {
            String info = "onError: " + errornum + " str:" + str;
            handleMsg(info);
        }
        
        @Override
        public void onDisConnected() {
            handler.sendEmptyMessage(MSG_Disconn);
            handleMsg(getString(R.string.unbind));
            showLog("onDisConnected ");
        }
        
        @Override
        public void onConnected() {
            showLog("onConnected");
            handler.sendEmptyMessage(MSG_Connect);
        }
        
        @Override
        public void onRcvData(AclasScaler.WeightInfoNew info) {
            if(setWeightInfo(info)) {
                showWeight(getWeightInfo());
                // Calculate total price when weight is received
                calculateTotalPrice(info.netWeight);
            }
        }
        
        public void onUpdateProcess(int iIndex, int iTotal) {
            // Ignore
        }
    };
    
    /**
     * PSX listener for price/amount data
     */
    private AclasScaler.AclasScalerPSXListener m_listenerPS = new AclasScaler.AclasScalerPSXListener() {
        @Override
        public void onRcvData(St_PSData st_psData) {
            m_psdata.setData(st_psData);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPSData();
                }
            });
        }
    };
    
    /**
     * Calculate total price based on weight and product price
     */
    private void calculateTotalPrice(double weight) {
        if (mProduct != null && weight > 0) {
            double totalPrice = weight * mProduct.getPrice();
            
            // Update both the current weight and UI on the UI thread
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_currentWeight = weight;
                    m_tvTotal.setText(String.format("%.3f", totalPrice));
                    showLog("Weight updated: " + weight + ", Total: " + totalPrice);
                }
            });
        }
    }
    
    /**
     * Handle positive button click - send result back to listener
     */
    public void onPositiveClick() {
        showLog("onPositiveClick called - Product: " + (mProduct != null ? mProduct.getLabel() : "null") + 
                ", Weight: " + m_currentWeight + ", IsReturn: " + mIsProductReturned);
        
        if (mListener != null && mProduct != null && m_currentWeight > 0) {
            showLog("Calling listener.onPsdPositiveClick with weight: " + m_currentWeight);
            mListener.onPsdPositiveClick(mProduct, m_currentWeight, mIsProductReturned);
        } else {
            showLog("ERROR: Cannot call listener - Product: " + (mProduct != null) + 
                    ", Weight: " + m_currentWeight + ", Listener: " + (mListener != null));
        }
        dismiss();
    }
    
    /**
     * Handle negative button click
     */
    public void onNegativeClick() {
        if (mListener != null) {
            mListener.onPsdNegativeClick();
        }
        dismiss();
    }
    
    /**
     * Show weight information in UI
     */
    private void showWeight(final AclasScaler.WeightInfoNew info) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(info.isOverWeight) {
                    m_tvWeight.setText("----");
                } else {
                    showLog("rec:" + info.toString() + " decimal:" + info.iDecimal);
                    String strNet = info.toString();
                    m_tvWeight.setText((info.isStable ? "S " : "U ") + strNet);
                    
                    switch (info.iDecimal) {
                        case 0:
                            m_tvTare.setText(String.format("%d ", (int)info.tareWeight) + info.unit);
                            break;
                        case 1:
                            m_tvTare.setText(String.format("%.1f ", info.tareWeight) + info.unit);
                            break;
                        case 2:
                            m_tvTare.setText(String.format("%.2f ", info.tareWeight) + info.unit);
                            break;
                        default:
                            m_tvTare.setText(String.format("%.3f ", info.tareWeight) + info.unit);
                            break;
                    }
                }
            }
        });
    }
    
    /**
     * Show price and total data
     */
    private void showPSData() {
        boolean bNegative = getWeightInfo().netWeight < 0;
        m_tvPrice.setText(getDoubleString(m_psdata.dPrice, m_psdata.iDotPrice));
        m_tvTotal.setText((bNegative ? "----" : getDoubleString(m_psdata.dAmount, m_psdata.iDotAmount)));
    }
    
    /**
     * Format double value as string
     */
    private String getDoubleString(double dVal, int iDot) {
        String str;
        switch (iDot) {
            case 0:
                str = String.format("%d ", (int)dVal);
                break;
            case 1:
                str = String.format("%.1f ", dVal);
                break;
            case 2:
                str = String.format("%.2f ", dVal);
                break;
            default:
                str = String.format("%.3f ", dVal);
                break;
        }
        return str;
    }
    
    /**
     * Set weight info (thread-safe)
     */
    private synchronized boolean setWeightInfo(AclasScaler.WeightInfoNew info) {
        return m_weight.setData(info);
    }
    
    /**
     * Get weight info (thread-safe)
     */
    private synchronized AclasScaler.WeightInfoNew getWeightInfo() {
        return m_weight;
    }
    
    // Message handling constants and methods
    private final int MSG_Connect = 0;
    private final int MSG_Disconn = 1;
    private final int MSG_Msg = 6;
    private final int MSG_WAIT = 7;
    
    private int m_iWaitVal = 0;
    private ProgressDialog m_Dialog = null;
    
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_Connect:
                    if(PAGE_MAIN != m_iPage) {
                        showDevInfo(m_strMac, m_strName);
                        showPage(PAGE_MAIN);
                        showBtVersion();
                    }
                    break;
                case MSG_Disconn:
                    showLog(getString(R.string.unbind));
                    showDevInfo("", "");
                    saveAddress("");
                    break;
                case MSG_Msg:
                    Toast.makeText(
                            requireContext(),
                            (String)msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_WAIT:
                    showLog("-------------MSG_WAIT----------------arg1:" + msg.arg1 + " m_iWaitVal:" + m_iWaitVal + " arg2:" + msg.arg2);
                    if(msg.arg2 == 1) {
                        showWait(true);
                    } else {
                        if(m_Dialog != null) {
                            if(msg.arg1 == m_iWaitVal) {
                                showWait(false);
                            }
                        }
                    }
                    break;
            }
        }
    };
    
    /**
     * Show/hide wait dialog
     */
    private void showWait(boolean bShow) {
        if(bShow) {
            if(m_Dialog == null) {
                m_iWaitVal++;
                m_Dialog = ProgressDialog.show(requireContext(), "",
                        getString(R.string.wait_connect), false, false,
                        null);
                closeWaitDlg(m_iWaitVal, 30000);
            }
        } else {
            if(m_Dialog != null) {
                m_Dialog.dismiss();
                m_Dialog = null;
            }
        }
    }
    
    private void closeWaitDlg(int iVal, int iDelay) {
        showLog("closeWaitDlg---------------------val:" + iVal + " delay:" + iDelay);
        Message msg = handler.obtainMessage(MSG_WAIT, iVal, 0);
        if(iDelay > 0) {
            handler.sendMessageDelayed(msg, iDelay);
        } else {
            handler.sendMessage(msg);
        }
    }
    
    private void handleWaitDlg() {
        showLog("handleWaitDlg---------------------:");
        Message msg = handler.obtainMessage(MSG_WAIT, 0, 1);
        handler.sendMessageDelayed(msg, 50);
    }
    
    private void handleMsg(String info) {
        Message msg = handler.obtainMessage(MSG_Msg, info);
        handler.sendMessage(msg);
    }
    
    /**
     * Show Bluetooth version
     */
    private void showBtVersion() {
        if(m_scaler != null) {
            String strVer = m_scaler.AclasFirmwareVersion();
            TextView tv = requireView().findViewById(R.id.tv_ver_bt);
            tv.setText(getString(R.string.bt_info) + strVer);
        }
    }
    
    /**
     * Show app version
     */
    private void showAppInfo() {
        TextView tv = requireView().findViewById(R.id.tv_ver_app);
        tv.setText(getString(R.string.app_info) + getVersionName());
    }
    
    /**
     * Get app version name
     */
    private String getVersionName() {
        String strName = "";
        try {
            PackageManager manager = requireContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.aclas.ortus", 0);
            strName = info.versionName;
            LogUtil.info("Version:" + strName);
        } catch (Exception e) {
            LogUtil.error(e.toString());
        }
        return strName;
    }
    
    /**
     * Handle permission request result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permiss, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permiss, grantResults);
        showLog("onRequestPermissionsResult requestCode:" + requestCode + " permissions size:" + permiss.length + " " + permiss[0] + " grantResults zize:" + grantResults.length + " " + grantResults[0]);
        if(requestCode == (permissions.length - 1)) {
            showLog("getPermission len-1 requestCode:" + requestCode);
            checkPower();
        } else {
            int iRet = UtilPermission.getPermission(permissions, requireContext(), requireActivity());
            showLog("onRequestPermissionsResult requestCode:" + requestCode + " ret:" + iRet);
            if(iRet == permissions.length) {
                showLog("getPermission len requestCode:" + requestCode);
                checkPower();
            }
        }
    }
    
    // Shared preferences helpers
    private void saveAddress(String strMac) {
        SharePrefenceUtil.setAddress(strMac);
    }
    
    private String getAddress() {
        return SharePrefenceUtil.getAddress();
    }
    
    private void setName(String name) {
        SharePrefenceUtil.setName(name);
    }
    
    private String getName() {
        return SharePrefenceUtil.getName();
    }
    
    // Logging helpers
    private void showLog(String info) {
        LogUtil.info(info);
    }
    
    private void showErr(String info) {
        LogUtil.error(info);
    }
}