package com.opurex.ortus.client.utils.scale;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "AclasScaleDemo";
    private static final boolean PRINT = true;

    public static void info(String msg){
        if (PRINT)
            Log.i(TAG,"----------"+msg);
    }

    public static void error(String msg){
        if (PRINT)
            Log.e(TAG,"----------"+msg);
    }

    public static void error(int msg){
        error(msg+"");
    }
}
