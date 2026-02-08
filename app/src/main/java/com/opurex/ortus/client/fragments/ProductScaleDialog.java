package com.opurex.ortus.client.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.data.St_PSData;
import com.example.scaler.AclasScaler;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.utils.ScaleLogger;
import com.opurex.ortus.client.utils.scale.BatteryUtilProductScaleDialog;
import com.opurex.ortus.client.utils.scale.LogUtil;
import com.opurex.ortus.client.utils.scale.SharePrefenceUtil;
import com.opurex.ortus.client.utils.scale.UtilPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ProductScaleDialog - DialogFragment for scale interaction and product weight measurement.
 */
public class ProductScaleDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ProductScaleDialog";
    private static final int REQ_BLUETOOTH_SCAN = 1001;

    private LinearLayout m_ltButtons;
    private ListView m_lvList;
    private SimpleAdapter m_listAdapter;
    private List<HashMap<String, String>> m_listData;
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
    private TextView m_tvTareUnit;
    private TextView m_tvKey;

    private AclasScaler m_scaler = null;
    private AclasScaler.WeightInfoNew m_weight = null;
    private boolean m_bInitFinish = false;

    private final int PAGE_MAIN = 0;
    private final int PAGE_PAIR = 1;
    private int m_iPage = -1;

    private String m_strName = "";
    private String m_strMac = "";
    private double m_currentWeight = 0.0;

    private St_PSData m_psdata = new St_PSData();

    private Product mProduct;
    private boolean mIsProductReturned;

    private Listener mListener;

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

    private ProgressDialog m_Dialog = null;
    private int m_iWaitVal = 0;

    // Message constants
    private final int MSG_Connect = 0;
    private final int MSG_Disconn = 1;
    private final int MSG_Msg = 6;
    private final int MSG_WAIT = 7;

    /** Listener interface for callback */
    public interface Listener {
        void onPsdPositiveClick(Product product, double weight, boolean isProductReturned);

        default void onPsdNegativeClick() { }
    }

    /** Factory method */
    public static ProductScaleDialog newInstance(Product product, boolean isReturnProduct) {
        ProductScaleDialog dialog = new ProductScaleDialog();
        Bundle args = new Bundle();
        args.putSerializable("arg_product", product);
        args.putBoolean("arg_is_return", isReturnProduct);
        dialog.setArguments(args);
        return dialog;
    }

    /** Set dialog listener */
    public void setDialogListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScaleLogger.init(requireContext());
        ScaleLogger.log(requireContext(), "Dialog Created");

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);

        if (getArguments() != null) {
            mProduct = (Product) getArguments().getSerializable("arg_product");
            mIsProductReturned = getArguments().getBoolean("arg_is_return", false);
            ScaleLogger.log(requireContext(), "Arguments received: product=" + mProduct + " isReturn=" + mIsProductReturned);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scale_activity_main, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitDevice(AclasScaler.Type_FSC);

        int iRet = UtilPermission.getPermission(permissions, requireContext(), requireActivity());
        ScaleLogger.log(requireContext(), "Permission check ret: " + iRet);
        if (iRet == permissions.length) {
            checkPower();
        }

        updateProductInfo();
        showAppInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (m_bInitFinish && m_iPage == PAGE_MAIN) {
            ScaleLogger.log(requireContext(), "onResume: checking power");
            checkPower();
        } else {
            ScaleLogger.log(requireContext(), "----onResume----m_bInitFinish：" + m_bInitFinish + " Page:" + m_iPage);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        ScaleLogger.log(requireContext(), "onDestroy called");
        CloseScale();
        super.onDestroy();
    }

    /** Initialize UI components */
    private void initUI(View view) {
        m_lvList = view.findViewById(R.id.lv_Devicelist);
        m_ltButtons = view.findViewById(R.id.ll_buttons);

        m_listData = new ArrayList<>();
        m_listMac = new ArrayList<>();
        m_listAdapter = new SimpleAdapter(requireContext(), m_listData,
                R.layout.device_layout,
                new String[]{"NAME", "MAC", "SIG"},
                new int[]{R.id.tv_pair_name, R.id.tv_pair_mac, R.id.tv_pair_sig});
        m_lvList.setAdapter(m_listAdapter);

        m_lvList.setOnItemClickListener((parent, v, position, id) -> {
            HashMap<String, String> map = m_listData.get(position);
            String mac = map.get("MAC");
            String name = map.get("NAME");
            ScaleLogger.log(requireContext(), "Device clicked: " + name + " - " + mac);
            checkPower(mac, name);
            handleWaitDlg();
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

        m_ltButtons.findViewById(R.id.scale_ok_button).setEnabled(false);

        m_btnBack.setOnClickListener(this);
        view.findViewById(R.id.btn_pair).setOnClickListener(this);
        view.findViewById(R.id.tv_back).setOnClickListener(this);
        view.findViewById(R.id.scale_ok_button).setOnClickListener(this);
        view.findViewById(R.id.scale_cancel_button).setOnClickListener(this);
    }

    /** Update product info */
    private void updateProductInfo() {
        if (mProduct != null) {
            m_tvPrice.setText(String.format("%.3f", mProduct.getPrice()));
            ScaleLogger.log(requireContext(), "Product price set: " + mProduct.getPrice());
        }
    }

    /** Show main/pair page */
    private void showPage(int index) {
        switch (index) {
            case PAGE_MAIN:
                showWait(false);
                m_iPage = PAGE_MAIN;
                m_tvTitle.setText(R.string.title_main);
                m_btnBack.setVisibility(View.INVISIBLE);
                m_ltMain.setVisibility(View.VISIBLE);
                m_ltPair.setVisibility(View.GONE);
                m_ltButtons.setVisibility(View.VISIBLE);
                ScaleLogger.log(requireContext(), "Switched to PAGE_MAIN");
                break;
            case PAGE_PAIR:
                m_iPage = PAGE_PAIR;
                m_tvTitle.setText(R.string.title_pair);
                m_ltPair.setVisibility(View.VISIBLE);
                m_btnBack.setVisibility(View.VISIBLE);
                m_ltMain.setVisibility(View.GONE);
                m_ltButtons.setVisibility(View.GONE);
                ScaleLogger.log(requireContext(), "Switched to PAGE_PAIR");
                break;
        }
    }

    /** Add device to list */
    private void addDev(String name, String mac, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put("NAME", name);
        map.put("MAC", mac);
        map.put("SIG", value);

        int index = m_listMac.indexOf(mac);
        if (index >= 0) {
            m_listData.remove(index);
            m_listData.add(index, map);
        } else {
            m_listData.add(map);
            m_listMac.add(mac);
        }
        m_listAdapter.notifyDataSetChanged();
        ScaleLogger.log(requireContext(), "Device added: " + name + " - " + mac);
    }

    private void addDev(String info) {
        if (info.contains(",")) {
            String[] list = info.split(",");
            if (list.length >= 3) addDev(list[0], list[1], list[2] + "db");
        }
    }

    /** Show device info */
    private void showDevInfo(String mac, String name) {
        m_tvName.setText(getString(R.string.dev_name) + name);
        m_tvMac.setText(getString(R.string.dev_mac) + mac);
    }

    /** Click handler */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
            case R.id.tv_back:
                showPage(PAGE_MAIN);
                checkPower();
                ScaleLogger.log(requireContext(), "Back button clicked");
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

    /** Init scale */
    private void InitDevice(int iType) {
        ScaleLogger.log(requireContext(), "InitDevice type=" + iType);
        if (m_scaler == null) {
            m_scaler = new AclasScaler(iType, requireContext(), m_listener);
            m_scaler.setAclasPSXListener(m_listenerPS);
            m_scaler.setBluetoothListener(m_listenerBt);
            m_weight = m_scaler.new WeightInfoNew();
            m_scaler.setLog(true);
        }
        showBtVersion();
    }

    /** Search devices */
//    private void searchDev() {
//        if (m_scaler != null) {
//            m_listMac.clear();
//            m_listData.clear();
//            m_listAdapter.notifyDataSetChanged();
//            m_scaler.startScanBluetooth(true);
//            ScaleLogger.log(requireContext(), "Started Bluetooth scan");
//        }
//    }
    private boolean hasBluetoothScanPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true;
        }
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestBluetoothScanPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT
                    },
                    REQ_BLUETOOTH_SCAN
            );
        }
    }



    private void searchDev() {
        ScaleLogger.log(requireContext(),"searchDev() called");

        if (!hasBluetoothScanPermission()) {
            ScaleLogger.log(requireContext(),"BLUETOOTH_SCAN not granted, requesting permission");
            requestBluetoothScanPermission();
            return;
        }

        ScaleLogger.log(requireContext(),"Permission granted, starting Bluetooth scan");

        try {
            if (m_scaler != null) {
                m_listMac.clear();
                m_listData.clear();
                m_listAdapter.notifyDataSetChanged();
                m_scaler.startScanBluetooth(true);
                //  aclasScaler.startScanBluetooth();
                ScaleLogger.log(requireContext(), "Bluetooth scan started successfully");
            }
        } catch (Exception e) {
            ScaleLogger.log(requireContext(),"Scan failed: " + Log.getStackTraceString(e));
        }
    }


    /** Check power / battery optimization */
    private void checkPower() {
        ScaleLogger.log(requireContext(), "checkPower");
        if (BatteryUtilProductScaleDialog.Companion.checkOptimization(requireActivity(), requireContext().getPackageName())) {
            OpenScale();
            m_bInitFinish = true;
        }
    }

    private void checkPower(final String mac, final String name) {
        ScaleLogger.log(requireContext(), "checkPower for device: " + name + " - " + mac);
        if (BatteryUtilProductScaleDialog.Companion.checkOptimization(requireActivity(), requireContext().getPackageName())) {
            OpenScale(mac, name);
            m_bInitFinish = true;
        }
    }

    /** Open scale */
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

    private void OpenScale(final String mac, final String name) {
        if (!mac.isEmpty()) {
            m_strName = name;
            m_strMac = mac;
        }
        m_weight.init();
        new Thread(() -> {
            int iRet = m_scaler.AclasConnect(mac);
            if (iRet == 0) {
                if (!mac.isEmpty()) {
                    saveAddress(mac);
                    setName(name);
                }
            } else saveAddress("");
            ScaleLogger.log(requireContext(), "OpenScale thread result: " + iRet);
            closeWaitDlg(m_iWaitVal, 0);
            handleMsg((iRet == 0 ? getString(R.string.connect) : getString(R.string.openfailed)) + ":" + mac);
        }).start();
    }

    /** Close scale */
    private void CloseScale() {
        if (m_scaler != null) {
            m_scaler.AclasDisconnect();
            m_scaler = null;
            ScaleLogger.log(requireContext(), "Scale closed");
        }
    }

    /** Bluetooth listener */
    private final AclasScaler.AclasBluetoothListener m_listenerBt = new AclasScaler.AclasBluetoothListener() {
        @Override
        public void onSearchBluetooth(String s) {
            ScaleLogger.log(requireContext(), "onSearchBluetooth: " + s);
            if (m_iPage == PAGE_PAIR) addDev(s);
        }

        @Override
        public void onSearchFinish() {
            ScaleLogger.log(requireContext(), "onSearchFinish");
            handleMsg("onSearchFinish");
        }
    };

    /** Scale listener */
    private final AclasScaler.AclasScalerListener m_listener = new AclasScaler.AclasScalerListener() {
        @Override
        public void onError(int errornum, String str) {
            String info = "onError: " + errornum + " str:" + str;
            ScaleLogger.log(requireContext(), info);
        }

        @Override
        public void onDisConnected() {
            handler.sendEmptyMessage(MSG_Disconn);
            handleMsg(getString(R.string.unbind));
            ScaleLogger.log(requireContext(), "onDisConnected");
        }

        @Override
        public void onConnected() {
            ScaleLogger.log(requireContext(), "onConnected");
            handler.sendEmptyMessage(MSG_Connect);
        }

        @Override
        public void onRcvData(AclasScaler.WeightInfoNew info) {
            if (setWeightInfo(info)) {
                showWeight(getWeightInfo());
                // Calculate total price when weight is received
                calculateTotalPrice(info.netWeight);
            }
        }

        @Override
        public void onUpdateProcess(int iIndex, int iTotal) { }
    };

    /** PSX listener */
    private final AclasScaler.AclasScalerPSXListener m_listenerPS = st_psData -> {
        m_psdata.setData(st_psData);
        requireActivity().runOnUiThread(this::showPSData);
    };

    /** Calculate total price */
    private void calculateTotalPrice(double weight) {
        if (mProduct != null) {
            double totalPrice = weight * mProduct.getPrice();
            requireActivity().runOnUiThread(() -> {
                m_currentWeight = weight;
                m_tvTotal.setText(String.format("%.3f", totalPrice));
                m_ltButtons.findViewById(R.id.scale_ok_button).setEnabled(weight > 0);
                ScaleLogger.log(requireContext(), "Weight updated: " + weight + ", Total: " + totalPrice);
            });
        }
    }

    /** Positive click */
    public void onPositiveClick() {
        ScaleLogger.log(requireContext(), "onPositiveClick: weight=" + m_currentWeight);
        if (mListener != null && mProduct != null && m_currentWeight > 0) {
            ScaleLogger.log(requireContext(), "Calling listener.onPsdPositiveClick with weight: " + m_currentWeight);
            mListener.onPsdPositiveClick(mProduct, m_currentWeight, mIsProductReturned);
        } else {
            ScaleLogger.log(requireContext(), "ERROR: Cannot call listener - Product: " + (mProduct != null) +
                    ", Weight: " + m_currentWeight + ", Listener: " + (mListener != null));
        }
        dismiss();
    }

    /** Negative click */
    public void onNegativeClick() {
        if (mListener != null) {
            mListener.onPsdNegativeClick();
        }
        dismiss();
    }

    /** Show weight */
    private void showWeight(final AclasScaler.WeightInfoNew info) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(info.isOverWeight) {
                    m_tvWeight.setText("----");
                } else {
                    ScaleLogger.log(requireContext(), "rec:" + info.toString() + " decimal:" + info.iDecimal);
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

    /** Show PS data */
    private void showPSData() {
        boolean bNegative = getWeightInfo().netWeight < 0;
        m_tvPrice.setText(String.format("%.3f", m_psdata.dPrice));
        m_tvTotal.setText(bNegative ? "----" : String.format("%.3f", m_psdata.dAmount));
        ScaleLogger.log(requireContext(), "PSData updated: Price=" + m_psdata.dPrice + " Total=" + m_psdata.dAmount);
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

    private void showWait(boolean bShow) {
        if (bShow) {
            if (m_Dialog == null) {
                m_iWaitVal++;
                m_Dialog = ProgressDialog.show(requireContext(), "",
                        getString(R.string.wait_connect), false, false, null);
                closeWaitDlg(m_iWaitVal, 30000);
                ScaleLogger.log(requireContext(), "Wait dialog shown");
            }
        } else {
            if (m_Dialog != null) {
                m_Dialog.dismiss();
                m_Dialog = null;
                ScaleLogger.log(requireContext(), "Wait dialog dismissed");
            }
        }
    }

    private void closeWaitDlg(int iVal, int iDelay) {
        ScaleLogger.log(requireContext(), "closeWaitDlg val=" + iVal + " delay=" + iDelay);
        Message msg = handler.obtainMessage(MSG_WAIT, iVal, 0);
        if (iDelay > 0) handler.sendMessageDelayed(msg, iDelay);
        else handler.sendMessage(msg);
    }

    private void handleWaitDlg() {
        ScaleLogger.log(requireContext(), "handleWaitDlg called");
        Message msg = handler.obtainMessage(MSG_WAIT, 0, 1);
        handler.sendMessageDelayed(msg, 50);
    }

    private void handleMsg(String info) {
        Message msg = handler.obtainMessage(MSG_Msg, info);
        handler.sendMessage(msg);
    }

    /** Handler for messages */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_Connect:
                    if (m_iPage != PAGE_MAIN) {
                        showDevInfo(m_strMac, m_strName);
                        showPage(PAGE_MAIN);
                        showBtVersion();
                        ScaleLogger.log(requireContext(), "MSG_Connect handled");
                    }
                    break;
                case MSG_Disconn:
                    showLog(getString(R.string.unbind));
                    showDevInfo("", "");
                    saveAddress("");
                    ScaleLogger.log(requireContext(), "MSG_Disconn handled");
                    break;
                case MSG_Msg:
                    Toast.makeText(requireContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    ScaleLogger.log(requireContext(), "MSG_Msg: " + msg.obj);
                    break;
                case MSG_WAIT:
                    ScaleLogger.log(requireContext(), "MSG_WAIT arg1:" + msg.arg1 + " arg2:" + msg.arg2 + " m_iWaitVal:" + m_iWaitVal);
                    if (msg.arg2 == 1) showWait(true);
                    else if (m_Dialog != null && msg.arg1 == m_iWaitVal) showWait(false);
                    break;
            }
        }
    };

    /** Show Bluetooth version */
    private void showBtVersion() {
        if (m_scaler != null) {
            String strVer = m_scaler.AclasFirmwareVersion();
            TextView tv = requireView().findViewById(R.id.tv_ver_bt);
            tv.setText(getString(R.string.bt_info) + strVer);
            ScaleLogger.log(requireContext(), "Bluetooth version: " + strVer);
        }
    }

    /** Show app version */
    private void showAppInfo() {
        TextView tv = requireView().findViewById(R.id.tv_ver_app);
        tv.setText(getString(R.string.app_info) + getVersionName());
        ScaleLogger.log(requireContext(), "App version shown: " + getVersionName());
    }

    /** Get app version */
    private String getVersionName() {
        String strName = "";
        try {
            PackageManager manager = requireContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(requireContext().getPackageName(), 0);
            strName = info.versionName;
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

    /** Thread-safe weight access */
    private synchronized boolean setWeightInfo(AclasScaler.WeightInfoNew info) {
        return m_weight.setData(info);
    }

    private synchronized AclasScaler.WeightInfoNew getWeightInfo() {
        return m_weight;
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