package com.opurex.ortus.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.opurex.ortus.client.R;

/**
 * Utility class for managing dynamic theming across the application
 */
public class ThemeUtils {
    
    private static final String PREF_THEME = "app_theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;
    public static final int THEME_SYSTEM = 0;
    
    /**
     * Apply the saved theme to the application
     */
    public static void applyTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
        int themeMode = prefs.getInt(KEY_THEME_MODE, THEME_SYSTEM);
        
        setThemeMode(context, themeMode, false); // Don't save since we're just applying
    }
    
    /**
     * Set the theme mode for the application
     */
    public static void setThemeMode(Context context, int themeMode) {
        setThemeMode(context, themeMode, true);
    }
    
    private static void setThemeMode(Context context, int themeMode, boolean save) {
        if (save) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
            prefs.edit().putInt(KEY_THEME_MODE, themeMode).apply();
        }
        
        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Get the current theme mode
     */
    public static int getCurrentThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME_MODE, THEME_SYSTEM);
    }
    
    /**
     * Check if the current theme is dark
     */
    public static boolean isDarkTheme(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    
    /**
     * Get a themed color resource
     */
    public static int getThemedColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId != 0 ? 
            ContextCompat.getColor(context, typedValue.resourceId) : 
            ContextCompat.getColor(context, android.R.color.black);
    }
    
    /**
     * Get a themed color resource ID
     */
    public static int getThemedColorResourceId(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId != 0 ? typedValue.resourceId : android.R.color.black;
    }
    
    /**
     * Refresh the theme for an activity
     */
    public static void refreshTheme(Activity activity) {
        activity.recreate();
    }
}