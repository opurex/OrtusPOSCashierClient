package com.opurex.ortus.client.utils.scale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class UtilPermission {
    static private final int MY_REQUEST_CODE   = 1000;
    private static int i = 0;
    static public int getPermission(String[]    permissions, Context context, Activity activity){
        PackageManager packageManager  = context.getPackageManager();
        PermissionInfo permissionInfo  = null;

        for(;i<permissions.length;i++){
            try {
                String strPer = permissions[i];

                // Skip getting permission info for newer permissions on older Android versions
                // BLUETOOTH_SCAN and BLUETOOTH_CONNECT were added in Android 12 (API 31)
                if ((strPer.equals(Manifest.permission.BLUETOOTH_SCAN) ||
                     strPer.equals(Manifest.permission.BLUETOOTH_CONNECT)) &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    // On older Android versions, just check if permission is granted without getting info
                    int iPer = ContextCompat.checkSelfPermission(context, strPer);
                    if(iPer != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, strPer)){
                            LogUtil.info("i:"+i+" 不再提示【"+strPer+"】权限申请:"+String.valueOf(iPer));
                        }else{
                            String[] tmp = {strPer};
                            LogUtil.info("i:"+i+" 申请获取权限【"+strPer+"】 :"+String.valueOf(iPer));
                            ActivityCompat.requestPermissions(activity,tmp,i);
                            break;
                        }
                    }else{
                        LogUtil.info("i:"+i+" 已经获取权限【"+strPer+"】");
                    }
                } else {
                    // For other permissions or on Android 12+, get permission info normally
                    permissionInfo = packageManager.getPermissionInfo(strPer, 0);
                    CharSequence permissionName = permissionInfo.loadLabel(packageManager);
                    int iPer = ContextCompat.checkSelfPermission(context, strPer);
                    if(iPer != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, strPer)){
                            LogUtil.info("i:"+i+" 不再提示【"+permissionName+"】权限申请:"+String.valueOf(iPer));
                        }else{
                            String[] tmp = {strPer};
                            LogUtil.info("i:"+i+" 申请获取权限【"+permissionName+"】 :"+String.valueOf(iPer));
                            ActivityCompat.requestPermissions(activity,tmp,i);
                            break;
                        }
                    }else{
                        LogUtil.info("i:"+i+" 已经获取权限【"+permissionName+"】");
                    }
                }
            }catch (PackageManager.NameNotFoundException e) {
                // Handle the case where permission doesn't exist on this Android version
                LogUtil.info("Permission not found on this Android version: " + permissions[i]);
                // Still check if permission is granted (will return PERMISSION_DENIED for non-existent permissions)
                int iPer = ContextCompat.checkSelfPermission(context, permissions[i]);
                if(iPer != PackageManager.PERMISSION_GRANTED){
                    LogUtil.info("i:"+i+" 权限不存在或未获取【"+permissions[i]+"】 :"+String.valueOf(iPer));
                }else{
                    LogUtil.info("i:"+i+" 已经获取权限【"+permissions[i]+"】");
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.error("Exception:"+e.toString());
            }
        }
//        LogUtil.info("getPermission return:"+i);
        return i++;
    }
}
