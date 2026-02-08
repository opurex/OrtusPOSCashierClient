package com.opurex.ortus.client.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScaleLogger {

    private static final String TAG = "ScaleLogger";
    private static File logFile;

    // Initialize log file in Documents folder
    public static void init(Context context) {
        File docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!docs.exists()) docs.mkdirs();
        logFile = new File(docs, "scale_log.txt");
    }

    // Write message with timestamp
    public static void log(Context context, String msg) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String fullMsg = timestamp + " | " + msg + "\n";

        // Log to Logcat
        Log.i(TAG, msg);

        // Append to file
        try {
            if (logFile != null) {
                FileWriter writer = new FileWriter(logFile, true);
                writer.append(fullMsg);
                writer.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to write log: " + e.getMessage());
        }
    }
}
