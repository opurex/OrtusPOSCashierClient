package com.opurex.ortus.client.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for exporting the SQLite database to external storage.
 */
public class DatabaseExportUtil {
    
    private static final String TAG = "DatabaseExportUtil";
    private static final String DATABASE_NAME = "ortuspos.db";
    
    /**
     * Checks if storage permissions are granted
     *
     * @param context The application context
     * @return true if permissions are granted, false otherwise
     */
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+, check for manage external storage permission
            return Environment.isExternalStorageManager();
        } else {
            // For older Android versions, check for write permission
            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * Requests storage permissions
     *
     * @param activity The activity requesting permissions
     * @param requestCode The request code to identify the permission request
     */
    public static void requestStoragePermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+, request manage external storage permission
            ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                requestCode);
        } else {
            // For older Android versions, request write permission
            ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
        }
    }

    /**
     * Exports the SQLite database to external storage under the app's package directory.
     *
     * @param context The application context
     * @return true if export was successful, false otherwise
     */
    public static boolean exportDatabase(Context context) {
        try {
            // Check if we have the required permissions
            if (!hasStoragePermission(context)) {
                // Show a message if permission is not granted
                Toast.makeText(context, 
                    "Storage permission required to export database", 
                    Toast.LENGTH_LONG).show();
                return false;
            }
            
            // Get the database path from the context
            File dbFile = context.getDatabasePath(DATABASE_NAME);
            
            if (!dbFile.exists()) {
                Log.e(TAG, "Database file does not exist: " + dbFile.getAbsolutePath());
                return false;
            }
            
            // Create directory path for export - under app's package name
            String packageName = context.getPackageName();
            
            File exportDir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+, use external files directory to avoid scoped storage issues
                exportDir = new File(context.getExternalFilesDir(null), "database_exports");
            } else {
                // For older Android versions, use external storage
                exportDir = new File(Environment.getExternalStorageDirectory(), packageName);
            }
            
            // Create the directory if it doesn't exist
            if (!exportDir.exists()) {
                if (!exportDir.mkdirs()) {
                    Log.e(TAG, "Failed to create export directory: " + exportDir.getAbsolutePath());
                    return false;
                }
            }
            
            // Generate export filename with timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String timestamp = sdf.format(new Date());
            String exportFileName = "ortuspos_db_" + timestamp + ".db";
            File exportFile = new File(exportDir, exportFileName);
            
            // Copy the database file to the export location
            copyFile(dbFile, exportFile);
            
            Log.i(TAG, "Database exported successfully to: " + exportFile.getAbsolutePath());
            
            // Show a toast notification to the user
            Toast.makeText(context, 
                "Database exported to: " + exportFile.getAbsolutePath(), 
                Toast.LENGTH_LONG).show();
                
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error exporting database", e);
            Toast.makeText(context, 
                "Error exporting database: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
            return false;
        }
    }
    
    /**
     * Copies a file from source to destination.
     *
     * @param source The source file
     * @param destination The destination file
     * @throws IOException if copying fails
     */
    private static void copyFile(File source, File destination) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(destination);
            
            byte[] buffer = new byte[1024];
            int length;
            
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}