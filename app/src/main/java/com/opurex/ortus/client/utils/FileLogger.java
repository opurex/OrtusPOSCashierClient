package com.opurex.ortus.client.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLogger {
    private static final String TAG = "FileLogger";
    private static final String LOG_FILE_PATH = "OrtusPOS/dcm/debug.log";
    private static FileLogger instance;
    private String basePath;

    private FileLogger(String basePath) {
        this.basePath = basePath;
    }

    public static synchronized FileLogger getInstance(Context context) {
        if (instance == null) {
            // Use external files directory for the app-specific external storage
            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir != null) {
                instance = new FileLogger(externalFilesDir.getAbsolutePath());
            } else {
                // Fallback to internal storage if external is not available
                instance = new FileLogger(context.getFilesDir().getAbsolutePath());
            }
        }
        return instance;
    }

    public void log(String tag, String message) {
        log(tag, message, "INFO");
    }

    public void log(String tag, String message, String level) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
        String logEntry = String.format("[%s] [%s] %s: %s%n", timestamp, level, tag, message);

        // Also log to Android logcat
        switch (level) {
            case "ERROR":
                Log.e(tag, message);
                break;
            case "WARN":
                Log.w(tag, message);
                break;
            case "DEBUG":
                Log.d(tag, message);
                break;
            default:
                Log.i(tag, message);
                break;
        }

        // Write to file
        writeToFile(logEntry);
    }

    public void d(String tag, String message) {
        log(tag, message, "DEBUG");
    }

    public void i(String tag, String message) {
        log(tag, message, "INFO");
    }

    public void w(String tag, String message) {
        log(tag, message, "WARN");
    }

    public void e(String tag, String message) {
        log(tag, message, "ERROR");
    }

    public void e(String tag, String message, Throwable throwable) {
        log(tag, message + " - Exception: " + throwable.getMessage(), "ERROR");
        // Log the stack trace as well
        for (StackTraceElement element : throwable.getStackTrace()) {
            log(tag, "  at " + element.toString(), "ERROR");
        }
    }

    private synchronized void writeToFile(String logEntry) {
        try {
            String fullPath = basePath + "/" + LOG_FILE_PATH;
            File logFile = new File(fullPath);

            // Create parent directories if they don't exist
            File parentDir = logFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Create the file if it doesn't exist
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(logEntry);
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to log file", e);
        }
    }

    public void clearLog() {
        try {
            String fullPath = basePath + "/" + LOG_FILE_PATH;
            File logFile = new File(fullPath);
            if (logFile.exists()) {
                logFile.delete();
                logFile.createNewFile();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error clearing log file", e);
        }
    }
}