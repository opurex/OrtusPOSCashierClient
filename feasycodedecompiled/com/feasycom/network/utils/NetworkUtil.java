package com.feasycom.network.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtil {
   public static void feedback(Context var0, String var1, boolean var2) {
      if (var2) {
         (new Thread(() -> {
            try {
               HttpURLConnection var10000 = (HttpURLConnection)(new URL(var0x)).openConnection();
               var10000.setRequestMethod("POST");
               var10000.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               var10000.setDoOutput(true);
               OutputStream var10001 = var10000.getOutputStream();
               var10001.write(params(var0, var1, var3));
               var10001.flush();
               var10001.close();
               var10000.getResponseCode();
            } catch (Exception var4) {
               var4.printStackTrace();
            }

         })).start();
      }

   }

   private static byte[] params(Context var0, String var1, String var2) {
      JSONObject var10000 = new JSONObject;
      JSONObject var3;
      JSONObject var10001 = var3 = var10000;
      var10000.<init>();
      JSONObject var4;
      JSONObject var10002 = var4 = new JSONObject;
      var4.<init>();
      PackageManager var10003 = var0.getPackageManager();

      JSONException var31;
      label120: {
         PackageManager.NameNotFoundException var30;
         label115: {
            Context var10004;
            boolean var32;
            try {
               var10004 = var0;
               var3.put("msgtype", "markdown");
            } catch (JSONException var25) {
               var31 = var25;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var26) {
               var30 = var26;
               var32 = false;
               break label115;
            }

            PackageInfo var33;
            try {
               var33 = var10003.getPackageInfo(var10004.getPackageName(), 0);
            } catch (JSONException var23) {
               var31 = var23;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var24) {
               var30 = var24;
               var32 = false;
               break label115;
            }

            PackageInfo var35 = var33;

            int var37;
            try {
               PackageInfo var10005 = var35;
               var10004 = var0;
               var37 = var10005.applicationInfo.labelRes;
            } catch (JSONException var21) {
               var31 = var21;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var22) {
               var30 = var22;
               var32 = false;
               break label115;
            }

            int var27 = var37;

            String var36;
            try {
               var36 = var10004.getResources().getString(var27);
            } catch (JSONException var19) {
               var31 = var19;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var20) {
               var30 = var20;
               var32 = false;
               break label115;
            }

            String var28 = var36;

            String var34;
            try {
               var34 = var33.versionName;
            } catch (JSONException var17) {
               var31 = var17;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var18) {
               var30 = var18;
               var32 = false;
               break label115;
            }

            String var29 = var34;

            try {
               var34 = VERSION.RELEASE;
            } catch (JSONException var15) {
               var31 = var15;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var16) {
               var30 = var16;
               var32 = false;
               break label115;
            }

            String var5 = var34;

            try {
               var34 = Build.BRAND + "(" + Build.MODEL + ")";
            } catch (JSONException var13) {
               var31 = var13;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var14) {
               var30 = var14;
               var32 = false;
               break label115;
            }

            String var6 = var34;
            var34 = "content";

            try {
               var10002.put(var34, "<font color=\"warning\">Android </font>《" + var28 + "》用户反馈\n>类型：<font color=\"comment\">" + var2 + "</font>\n>APP版本：<font color=\"comment\">" + var29 + "</font>\n>系统版本：<font color=\"comment\">" + var5 + "</font>\n>手机型号：<font color=\"comment\">" + var6 + "</font>\n>反馈内容：<font color=\"comment\">" + var1 + "</font>");
            } catch (JSONException var11) {
               var31 = var11;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var12) {
               var30 = var12;
               var32 = false;
               break label115;
            }

            try {
               var10001.put("markdown", var4);
            } catch (JSONException var9) {
               var31 = var9;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var10) {
               var30 = var10;
               var32 = false;
               break label115;
            }

            try {
               return var10000.toString().getBytes();
            } catch (JSONException var7) {
               var31 = var7;
               var32 = false;
               break label120;
            } catch (PackageManager.NameNotFoundException var8) {
               var30 = var8;
               var32 = false;
            }
         }

         var30.printStackTrace();
         return "".getBytes();
      }

      var31.printStackTrace();
      return "".getBytes();
   }
}
