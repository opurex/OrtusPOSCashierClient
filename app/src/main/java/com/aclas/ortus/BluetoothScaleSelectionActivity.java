package com.aclas.ortus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.data.St_PSData;
import com.example.scaler.AclasScaler;
import com.opurex.ortus.client.R;
import com.opurex.ortus.client.utils.scale.BatteryUtil;
import com.opurex.ortus.client.utils.scale.LogUtil;
import com.opurex.ortus.client.utils.scale.SharePrefenceUtil;
import com.opurex.ortus.client.utils.scale.UtilPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  1 init();初始化界面
 *  2 InitDevice(AclasScaler.Type_FSC);初始化秤
 *  3 searchDev()搜索蓝牙秤
 *  4 OpenScale 连接蓝牙秤
 *    AclasConnect 新配对的传入mac参数;已配对的重联传入""空字符 成功返回0,负值失败
 *    在成功获取秤数据后,接收到onDisConnected()信号是秤解绑了.
 *  5 CloseScale() 断开连接
 *   解绑只能由秤端发起(长按数字键9直到听到长滴声),解绑后才能被搜索到(秤没变化大概30S左右进入睡眠，进入睡眠前都能被搜索到,
 *   唤醒后需要再次长按数字键9才能被搜索到).
 *
 */
public class BluetoothScaleSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView    m_lvList;

    private SimpleAdapter m_listAdapter;
    private List<HashMap<String,String>> m_listData;
    private List<String> m_listMac;

    private LinearLayout    m_btnBack;
    private LinearLayout    m_ltMain;
    private LinearLayout    m_ltPair;
    private TextView    m_tvTitle;
    private TextView m_tvName;
    private TextView m_tvMac;

    private TextView    m_tvWeight;
    private TextView    m_tvUnit;
    private TextView    m_tvPrice;
    private TextView    m_tvTotal;
    private TextView    m_tvTare;
    private TextView    m_tvKey;
    private TextView    m_tvTareUnit;

    private AclasScaler m_scaler = null;
    private AclasScaler.WeightInfoNew m_weight = null;

    private boolean m_bInitFinish = false;

    private String[]    permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        InitDevice(AclasScaler.Type_FSC);
        int iRet = UtilPermission.getPermission(permissions,this.getApplicationContext(), BluetoothScaleSelectionActivity.this);//动态申请权限

        if(iRet==permissions.length){
            showLog("onCreate getPermission ret:"+iRet);
            checkPower();
        }
        showAppInfo();
    }

    @Override
    protected void onPause(){
//        if(m_bInitFinish&&m_iPage==PAGE_MAIN) {
//            showLog("----onPause----");
//            CloseScale();
//        }else{
//            showLog("----onPause----m_bInitFinish："+m_bInitFinish+" Page:"+m_iPage);
//        }
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(m_bInitFinish&&m_iPage==PAGE_MAIN){
            showLog("----onResume----");
//            InitDevice(AclasScaler.Type_FSC);//250613
            checkPower();
        }else{
            showLog("----onResume----m_bInitFinish："+m_bInitFinish+" Page:"+m_iPage);
        }
    }
    /**
     * 初始化界面
     */
    private void init(){
        m_lvList    = findViewById(R.id.lv_Devicelist);
        m_listData  = new ArrayList<>();
        m_listMac   = new ArrayList<>();
        m_listAdapter   = new SimpleAdapter(this,m_listData,
                R.layout.device_layout,
                new String[]{"NAME","MAC","SIG"},
                new int[]{R.id.tv_pair_name,R.id.tv_pair_mac,R.id.tv_pair_sig});
        m_lvList.setAdapter(m_listAdapter);
        m_lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = m_listData.get(position);
                String mac = map.get("MAC");
                String name = map.get("NAME");
                showLog("POS:"+position+" name:"+name+" mac:"+mac);
                checkPower(mac,name);
                handleWaitDlg();
            }
        });
        m_btnBack   = findViewById(R.id.btn_back);
        m_ltMain    = findViewById(R.id.lt_main);
        m_ltPair    = findViewById(R.id.lt_pair);
        m_tvTitle   = findViewById(R.id.tv_title);
        m_tvName    = findViewById(R.id.tv_dev_name);
        m_tvMac     = findViewById(R.id.tv_dev_mac);
        m_tvWeight  = findViewById(R.id.tv_weight);
        m_tvUnit  = findViewById(R.id.tv_unit);
        m_tvPrice  = findViewById(R.id.tv_price);
        m_tvTotal  = findViewById(R.id.tv_total);
        m_tvTare  = findViewById(R.id.tv_tare);
        m_tvTareUnit  = findViewById(R.id.tv_unit_tare);
        m_tvKey  = findViewById(R.id.tv_key);
    }

    private String m_strName = "";
    private String m_strMac = "";
    private final int PAGE_MAIN = 0;
    private final int PAGE_PAIR = 1;

    private int m_iPage = -1;

    /**
     * 显示界面
     * @param index  PAGE_MAIN 秤数据界面;  PAGE_PAIR 秤搜索和连接界面
     */
    private void showPage(int index){
        switch (index){
            case PAGE_MAIN:
                showLog("showPage showWait false");
                showWait(false);
                m_iPage = PAGE_MAIN;
                m_tvTitle.setText(R.string.title_main);
                m_btnBack.setVisibility(View.INVISIBLE);
                m_ltMain.setVisibility(View.VISIBLE);
                m_ltPair.setVisibility(View.GONE);
                break;
            case PAGE_PAIR:
                m_iPage = PAGE_PAIR;
                m_tvTitle.setText(R.string.title_pair);
                m_ltPair.setVisibility(View.VISIBLE);
                m_btnBack.setVisibility(View.VISIBLE);
                m_ltMain.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 添加蓝牙秤
     * @param name  蓝牙秤名称
     * @param mac   蓝牙秤mac
     * @param value 信号值
     */
    private void addDev(String name,String mac,String value){
        HashMap<String,String> map = new HashMap<>();
        map.put("NAME",name);
        map.put("MAC",mac);
        map.put("SIG",value);
        int index = m_listMac.indexOf(mac);
        if(index>=0){
            m_listData.remove(index);
            m_listData.add(index,map);
        }else{
            m_listData.add(map);
            m_listMac.add(mac);
        }
        m_listAdapter.notifyDataSetChanged();
    }

    /**
     * 添加蓝牙秤
     * @param info  蓝牙搜索回调信息    "name,mac"
     */
    private void addDev(String info){
        if(info.contains(",")){
            String[] list = info.split(",");
            addDev(list[0],list[1],list[2]+"db");
        }
    }

    /**
     *  界面显示已连接蓝牙信息
     * @param mac
     * @param name
     */
    private void showDevInfo(String mac,String name){
        m_tvName.setText(getString(R.string.dev_name)+name);
        m_tvMac.setText(getString(R.string.dev_mac)+mac);
    }

    public void onDestroy(){
        super.onDestroy();
        showLog("---onDestroy---");
        CloseScale();
    }
    public void onClick(View v){
        int id = v.getId();
        boolean bFlag = false;
        switch (id){
            case R.id.btn_back:
            case R.id.tv_back:
                showPage(PAGE_MAIN);
                checkPower();
                showLog("---back btn click---");
                break;
            case R.id.btn_pair:
                CloseScale();
                InitDevice(AclasScaler.Type_FSC);//250613
                searchDev();
                showPage(PAGE_PAIR);
                break;
        }
    }

    private void showLog(String info){
        LogUtil.info(info);
    }

    private void showErr(String info){
        LogUtil.error(info);
    }

    /**
     *
     * @return 获取APP版本信息
     */
    private String getVersionName() {
        String strName = "";
        try {
            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.aclas.ortus", 0);
            strName = info.versionName;
            LogUtil.info("Version:" + strName);
        } catch (Exception e) {
            LogUtil.error(e.toString());
        }
        return strName;
    }

    /**
     * 显示APP版本信息
     */
    private void showAppInfo(){
        final TextView tv = findViewById(R.id.tv_ver_app);
        tv.setText(getString(R.string.app_info)+getVersionName());
    }

    /**
     * 显示蓝牙模组版本
     */
    private void showBtVersion(){
        if(m_scaler!=null){
           String strVer = m_scaler.AclasFirmwareVersion();
            final TextView tv = findViewById(R.id.tv_ver_bt);
            tv.setText(getString(R.string.bt_info)+strVer);
        }
    }

    /**
     * 蓝牙秤搜索回调
     */
    private AclasScaler.AclasBluetoothListener m_listenerBt = new AclasScaler.AclasBluetoothListener() {
        @Override
        public void onSearchBluetooth(String s) {
            showLog("onSearchBluetooth:"+s);
            if(m_iPage == PAGE_PAIR){
                addDev(s);
            }
        }

        @Override
        public void onSearchFinish() {
            showLog("onSearchFinish");
            handleMsg("onSearchFinish");
        }
    };
    private AclasScaler.AclasScalerListener m_listener = new AclasScaler.AclasScalerListener() {

        @Override
        public void onError(int errornum, String str) {
            // TODO Auto-generated method stub
            String info = "onError: "+errornum+" str:"+str;

            handleMsg(info);
        }

        @Override
        public void onDisConnected() {
            // TODO Auto-generated method stub
            handler.sendEmptyMessage(MSG_Disconn);
            handleMsg(getString(R.string.unbind));
            showLog("onDisConnected ");
        }

        @Override
        public void onConnected() {
            // TODO Auto-generated method stub
           showLog( "onConnected");
//            handleConnect(0);
            //handleMsg("onConnected");
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
        public void onUpdateProcess(int iIndex,int iTotal){
        }
    };

    /**
     * 显示Toast
     * @param info 信息内容
     */
    private void handleMsg(String info){
        Message msg	= handler.obtainMessage(MSG_Msg, info);
        handler.sendMessage(msg);
    }
    private final int MSG_Connect 		= 0;
    private final int MSG_Disconn 		= 1;
    private final int MSG_Msg		 	= 6;
    private final int MSG_WAIT 		= 7;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_Connect:
                    if(PAGE_MAIN!=m_iPage){
                        showDevInfo(m_strMac,m_strName);
                        showPage(PAGE_MAIN);
                        showBtVersion();
                    }
                    break;
                case MSG_Disconn:
//                    秤解绑了。需要重新绑定AclasConnect传入mac地址;
                    showLog(getString(R.string.unbind));
                    showDevInfo("","");
                    saveAddress("");
                    break;
                case MSG_Msg:
                    Toast.makeText(
                            BluetoothScaleSelectionActivity.this,
                            (String)msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_WAIT:
                    showLog("-------------MSG_WAIT----------------arg1:"+msg.arg1+" m_iWaitVal:"+m_iWaitVal+" arg2:"+msg.arg2);
                    if(msg.arg2==1){
                        showWait(true);
                    }else{
                        if(m_Dialog!=null){
                            if(msg.arg1==m_iWaitVal){
                                showWait(false);
//                            InitSpin();
                            }
                        }
                    }
                    break;
            }
        }
    };

    //按键值 代表的 按键信息
    private	final 		String[]	stKeys			= {	"KEY_Num0","KEY_Num1","KEY_Num2","KEY_Num3","KEY_Num4",
            "KEY_Num5","KEY_Num6","KEY_Num7","KEY_Num8","KEY_Num9",
            "KEY_Num00","KEY_Dot","KEY_Add","KEY_PriceLock","KEY_Change",
            "KEY_MC","KEY_PLU","KEY_OnOff","KEY_PLUSET","KEY_SUM",
            "KEY_SUB","KEY_Tare","KEY_Zero","KEY_Clear","KEY_BATV",
            "KEY_M0_To_M5","KEY_MC","KEY_---","KEY_---","KEY_---",
            "KEY_---","KEY_---","KEY_---","KEY_---","KEY_---",
            "KEY_PLU01","KEY_PLU02","KEY_PLU03","KEY_PLU04","KEY_PLU05",
            "KEY_PLU06","KEY_PLU07","KEY_PLU08","KEY_PLU09","KEY_PLU10",
            "KEY_PLU11","KEY_PLU12","KEY_PLU13","KEY_PLU14","KEY_PLU15",
            "KEY_PLU16","KEY_PLU17","KEY_PLU18","KEY_PLU19","KEY_PLU20",
            "KEY_PLU21","KEY_PLU22","KEY_PLU23","KEY_PLU24","KEY_PLU25",
            "KEY_PLU26","KEY_PLU27","KEY_PLU28","KEY_PLU29","KEY_PLU30",
            "KEY_PLU31","KEY_PLU32","KEY_PLU33","KEY_PLU34","KEY_PLU35",
            "KEY_PLU36","KEY_PLU37","KEY_PLU38","KEY_PLU39","KEY_PLU40",
            "KEY_PLU41","KEY_PLU42","KEY_PLU43","KEY_PLU44","KEY_PLU45",
            "KEY_PLU46","KEY_PLU47","KEY_PLU48","KEY_PLU49","KEY_PLU50",
            "KEY_PLU51","KEY_PLU52","KEY_PLU53","KEY_PLU54","KEY_PLU55",
            "KEY_PLU56","KEY_PLU57","KEY_PLU58","KEY_PLU59","KEY_PLU60",
            "KEY_PLU61","KEY_PLU62","KEY_PLU63","KEY_PLU64","KEY_PLU65",//20line
            "KEY_M0","KEY_M1","KEY_M2","KEY_M3","KEY_M4",
            "KEY_M5","KEY_---","KEY_---","KEY_---","KEY_---",
            "KEY_PLU66","KEY_PLU67","KEY_PLU68","KEY_PLU69","KEY_PLU70"

    };
    private int iKeyCnt = 0;//按键计数

    /**
     * 界面显示单价总价按键
     */
    private void showPSData(){
        boolean bNegative =  getWeightInfo().netWeight<0;
        m_tvPrice.setText(getDoubleString(m_psdata.dPrice,m_psdata.iDotPrice));
        m_tvTotal.setText((bNegative?"----":getDoubleString(m_psdata.dAmount,m_psdata.iDotAmount)));
        if(m_psdata.iKeyVal>=0&&m_psdata.iKeyVal<stKeys.length){
            String strKey = String.valueOf(m_psdata.iKeyVal);
            strKey += " ";
            strKey += stKeys[m_psdata.iKeyVal];
            strKey += " :";
            strKey += String.valueOf(iKeyCnt++);
            if(iKeyCnt>998){
                iKeyCnt = 0;
            }
            m_tvKey.setText(strKey);
        }
    }

    /**
     * 清除秤数据
     */
    private void clearData(){
        m_tvKey.setText("");
        m_tvTare.setText("");
        m_tvTotal.setText("");
        m_tvPrice.setText("");
        m_tvWeight.setText("");
    }
    /**
     *
     * @param dVal  数值
     * @param iDot  小数位数
     * @return  字符串
     */
    private String getDoubleString(double dVal,int iDot){
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
        return m_weight.setData(info);
    }

    private synchronized AclasScaler.WeightInfoNew getWeightInfo(){
        return m_weight;
    }
    private St_PSData m_psdata =  new St_PSData();
    private AclasScaler.AclasScalerPSXListener m_listenerPS = new AclasScaler.AclasScalerPSXListener() {
        @Override
        public void onRcvData(St_PSData st_psData) {
            m_psdata.setData(st_psData);
            //String strInfo = String.format("price:%.3f amt:%.3f key:%d",st_psData.dPrice,st_psData.dAmount,st_psData.iKeyVal);
            //showLog(strInfo);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPSData();
                }
            });
        }
    };


    /**
     * 显示 重量值 状态 皮重值
     */
    private void showWeight(final AclasScaler.WeightInfoNew info){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(info.isOverWeight){
                    //	m_tvStable.setText("Error");
                    m_tvWeight.setText("----");
                }else{
                    showLog("rec:"+info.toString()+" decimal:"+info.iDecimal);
                    String strNet = info.toString();
                    m_tvWeight.setText((info.isStable?"S ":"U ")+strNet);
                    switch (info.iDecimal){
                        case 0:
                            m_tvTare.setText(String.format("%d ",(int)info.tareWeight)+info.unit );
                            break;
                        case 1:
                            m_tvTare.setText(String.format("%.1f ",info.tareWeight)+info.unit );
                            break;
                        case 2:
                            m_tvTare.setText(String.format("%.2f ",info.tareWeight)+info.unit );
                            break;
                        default:
                            m_tvTare.setText(String.format("%.3f ",info.tareWeight)+info.unit );
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permiss, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permiss,grantResults);
        showLog("onRequestPermissionsResult requestCode:"+requestCode+" permissions size:"+permiss.length+" "+permiss[0]+" grantResults zize:"+grantResults.length+" "+grantResults[0]);
        if(requestCode==(permissions.length-1)){
            showLog("getPermission len-1 requestCode:"+requestCode);
            checkPower();
        }else {
            int iRet = UtilPermission.getPermission(permissions,this.getApplicationContext(), BluetoothScaleSelectionActivity.this);
            showLog("onRequestPermissionsResult requestCode:"+requestCode+" ret:"+iRet);
            if(iRet==permissions.length){
                //m_rbBLE.performClick();
                showLog("getPermission len requestCode:"+requestCode);
                checkPower();
            }
        }
    }

    /**
     * 初始化秤  设置回调
     * @param iType  AclasScaler.Type_FSC
     */
    private void InitDevice(int iType) {
        showLog("InitDevice:"+iType);
        if(m_scaler==null){
            m_scaler = new AclasScaler(iType, this,m_listener);
            m_scaler.setAclasPSXListener(m_listenerPS);//设置 单价总价按键 的回调
            m_scaler.setBluetoothListener(m_listenerBt);//搜索蓝牙回调
            m_weight = m_scaler.new WeightInfoNew();
            m_scaler.setLog(true);
        }
        showBtVersion();
    }

    private void searchDev(){
        if(m_scaler!=null){
            m_listMac.clear();
            m_listData.clear();
            m_listAdapter.notifyDataSetChanged();
            m_scaler.startScanBluetooth(true);
        }
    }

    private void checkPower(final String mac,final String name){
        showLog("-----checkPower-------");
        if(BatteryUtil.Companion.checkOptimization(this,getPackageName())){
            OpenScale(mac,name);
            m_bInitFinish   = true;
        }
    }

    private void checkPower(){
        showLog("-----checkPower-------");
        if(BatteryUtil.Companion.checkOptimization(this,getPackageName())){
            OpenScale();
            m_bInitFinish   = true;
        }
    }

    /**
     * 打开秤
     */
    private void OpenScale(){
        if (m_scaler != null) {
            String mac = getAddress();
            String name = getName();

            m_strName   = name;
            m_strMac    = mac;
            if(mac!=null&&mac.contains(":")){
                OpenScale("",name);
            }
        }
    }

    private void CloseScale(){
        if(m_scaler!=null){
            m_scaler.AclasDisconnect();
            m_scaler    = null;//250613

        }
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

    /**
     *打开秤
     * @param mac
     * @param name
     */
    private void OpenScale(final String mac,final String name){
        if(!mac.isEmpty()){
            m_strName   = name;
            m_strMac    = mac;
        }
        m_weight.init();//初始化重量数据
        iKeyCnt = 0;
        clearData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int iRet = -1;

                //已经配对的 mac传入"",未配对的传入mac地址
                iRet = m_scaler.AclasConnect(mac);
                if(iRet==0){//连接成功 存储非空 蓝牙信息
                    if(!mac.isEmpty()){
                        saveAddress(mac);
                        setName(name);
                    }
                }else{
                    saveAddress("");
                }
                showLog("OpenScale in thread closeWait ret:"+iRet);
                closeWaitDlg(m_iWaitVal,0);
                handleMsg((iRet==0?getString(R.string.connect):getString(R.string.openfailed))+":"+mac);
            }
        }).start();

    }

    private int m_iWaitVal  = 0;
    private void closeWaitDlg(int iVal,int iDelay){
        showLog("closeWaitDlg---------------------val:"+iVal+" delay:"+iDelay);
        Message msg = handler.obtainMessage(MSG_WAIT,iVal,0);
        if(iDelay>0){
            handler.sendMessageDelayed(msg,iDelay);
        }else {
            handler.sendMessage(msg);
        }
    }
    private void handleWaitDlg(){
        showLog("handleWaitDlg---------------------:");
        Message msg = handler.obtainMessage(MSG_WAIT,0,1);
        handler.sendMessageDelayed(msg,50);
    }

    private ProgressDialog m_Dialog = null;

    /**
     * 等待界面 的显示与隐藏
     * @param bShow  true 显示;  false 隐藏;
     */
    private void showWait(boolean bShow){
        if(bShow){
            if(m_Dialog==null){
                m_iWaitVal++;
                m_Dialog = ProgressDialog.show(BluetoothScaleSelectionActivity.this, "",
                        getString(R.string.wait_connect), false, false,
                        null);

                closeWaitDlg(m_iWaitVal,30000);
            }
        }else{
            if(m_Dialog!=null){
                m_Dialog.dismiss();
                m_Dialog    = null;
            }
        }
    }
}