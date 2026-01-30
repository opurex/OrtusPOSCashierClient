package com.opurex.ortus.client.utils.scale;

import android.content.Context;
import android.content.SharedPreferences;

import com.opurex.ortus.client.OrtusPOS;


public class SharePrefenceUtil {

    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;

    private static final String KEY_FLAG             = "KEY_FLAG";//设置标志位
    private static final String KEY_ADDRESS             = "KEY_ADDRESS";//设置标志位
    private static final String KEY_NAME             = "KEY_NAME";//设置标志位
    private static final String KEY_AUTO             = "KEY_AUTO";//飞易通自动连接

    private static SharedPreferences getSp() {
        if (sharedPreferences == null) {
            OrtusPOS appInstance = OrtusPOS.getInstance();
            if (appInstance != null) {
                sharedPreferences = appInstance.getSharedPreferences("OrtusPOSPreference", Context.MODE_PRIVATE);
            } else {
                // Fallback to using application context if available
                Context applicationContext = OrtusPOS.getAppContext();
                if (applicationContext != null) {
                    sharedPreferences = applicationContext.getSharedPreferences("OrtusPOSPreference", Context.MODE_PRIVATE);
                } else {
                    throw new IllegalStateException("Unable to access application context for SharedPreferences");
                }
            }
        }
        return sharedPreferences;
    }

    private static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = getSp().edit();
        }
        return editor;
    }

    private static void setStr(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    private static String getStr(String key){
        return getSp().getString(key,"");
    }

    private static void setBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    private static boolean getBoolean(String key){
        return getSp().getBoolean(key,false);
    }
    private static boolean getBoolean(String key,boolean bDef){
        return getSp().getBoolean(key,bDef);
    }

    private static void setInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    private static int getInt(String key){
        return getSp().getInt(key,-1);
    }

    public static boolean getFlag() {
        return getBoolean(KEY_FLAG);
    }

    public static void setFlag(boolean bFlag) {
        setBoolean(KEY_FLAG,bFlag);
    }

    public static void setAddress(String str){
        setStr(KEY_ADDRESS,str);
    }
    public static String getName(){
        return getStr(KEY_NAME);
    }
    public static void setName(String str){
        setStr(KEY_NAME,str);
    }
    public static String getAddress(){
        return getStr(KEY_ADDRESS);
    }

    public static void setAuto(boolean bFlag){
        setBoolean(KEY_AUTO,bFlag);
    }

    public static boolean getAuto(){
        return getBoolean(KEY_AUTO,true);
    }
}



