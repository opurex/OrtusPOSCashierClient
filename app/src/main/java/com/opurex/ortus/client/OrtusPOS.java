package com.opurex.ortus.client;

import android.app.Application;
import android.content.Context;

import com.opurex.ortus.client.utils.OpurexConfiguration;

/**
 * Created by nsvir on 21/09/15.
 * n.svirchevsky@gmail.com
 */
public class OrtusPOS extends Application {

    public static final String TAG = "Ortus:";
    private static Context context;

    public static float getRestaurantMapWidth() {
        return 635f;
    }

    public static float getRestaurantMapHeight() {
        return 500f;
    }

    public void onCreate() {
        super.onCreate();
        OrtusPOS.context = getApplicationContext();

        // Initialize file logger
        com.opurex.ortus.client.utils.FileLogger.getInstance(this);
    }

    public static Context getAppContext() {
        return OrtusPOS.context;
    }

    public static String getStringResource(int resourceId) {
        return OrtusPOS.getAppContext().getString(resourceId);
    }

    public static OpurexConfiguration getConfiguration() {
        return new OpurexConfiguration(getAppContext());
    }

    //shorter function
    public static OpurexConfiguration getConf() {
        return getConfiguration();
    }

    public static class Toast {

        private static android.widget.Toast lastToast;

        private Toast() {
        }

        public static void show(String message) {
            show(message, android.widget.Toast.LENGTH_SHORT);
        }

        public static void show(int stringid) {
            show(OrtusPOS.getStringResource(stringid));
        }

        public static void show(String message, int length) {
            cancelLastToast();
            android.widget.Toast toast = android.widget.Toast.makeText(OrtusPOS.getAppContext(), message, length);
            toast.show();
            lastToast = toast;
        }

        public static void cancelLastToast() {
            if (lastToast != null) {
                lastToast.cancel();
                lastToast = null;
            }
        }
    }

    public static class Log {

        public static StackTraceElement getStackTraceElement() {
            StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
            for (int i = 1; i < stElements.length; i++) {
                StackTraceElement ste = stElements[i];
                if (!ste.getClassName().equals(OrtusPOS.class.getName())
                        && ste.getClassName().indexOf("java.lang.Thread") != 0
                        && ste.getClassName().indexOf("com.opurex.ortus.clientOpurex$Log") != 0) {
                    return ste;
                }
            }
            return null;
        }

        protected static String removePackage(String className) {
            int index = className.lastIndexOf(".") + 1;
            if (index != -1) {
                return className.substring(index);
            }
            return className;
        }

        public static String getUniversalLog() {
            StackTraceElement stackTraceElement = getStackTraceElement();
            if (stackTraceElement != null) {
                return "Opurex:" + removePackage(stackTraceElement.getClassName()) + ":" + stackTraceElement.getMethodName();
            } else {
                return "Opurex: <could not get stacktrace>";
            }
        }

        public static void w(String message) {
            String tag = getUniversalLog();
            android.util.Log.w(tag, message);
            // Also log to file
            com.opurex.ortus.client.utils.FileLogger.getInstance(OrtusPOS.getAppContext()).w(tag, message);
        }

        public static void w(String message, Throwable e) {
            String tag = getUniversalLog();
            android.util.Log.w(tag, message, e);
            // Also log to file
            com.opurex.ortus.client.utils.FileLogger.getInstance(OrtusPOS.getAppContext()).w(tag, message + " - Exception: " + e.getMessage());
        }

        public static void d(String message) {
            String tag = getUniversalLog();
            android.util.Log.d(tag, message);
            // Also log to file
            com.opurex.ortus.client.utils.FileLogger.getInstance(OrtusPOS.getAppContext()).d(tag, message);
        }

        public static void d(String message, Throwable e) {
            String tag = getUniversalLog();
            android.util.Log.d(tag, message, e);
            // Also log to file
            com.opurex.ortus.client.utils.FileLogger.getInstance(OrtusPOS.getAppContext()).d(tag, message + " - Exception: " + e.getMessage());
        }
    }


}
