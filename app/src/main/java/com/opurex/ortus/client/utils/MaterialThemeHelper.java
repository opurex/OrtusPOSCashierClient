package com.opurex.ortus.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;
import com.opurex.ortus.client.R;

/**
 * Helper class to apply Material 3 theming to OrtusPOS activities
 * This class provides methods to initialize and apply Material 3 themes
 */
public class MaterialThemeHelper {
    
    /**
     * Initializes the Material 3 theme for the application
     * This should be called early in the application lifecycle
     * @param context The application context
     */
    public static void initializeMaterialTheme(Context context) {
        // Set up theme preferences based on user settings
        ThemeManager themeManager = new ThemeManager(context);
        
        // Apply the theme mode (light/dark/system)
        int themeMode = themeManager.getThemeMode();
        switch (themeMode) {
            case ThemeManager.THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case ThemeManager.THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case ThemeManager.THEME_MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Applies Material 3 theming to an activity
     * @param activity The activity to apply theming to
     */
    public static void applyMaterial3Theme(Activity activity) {
        // Apply dynamic colors if supported by the device
        if (DynamicColors.isDynamicColorAvailable()) {
            DynamicColors.applyToActivityIfAvailable(activity);
        }
        
        // Apply custom OrtusPOS theme
        ThemeManager themeManager = new ThemeManager(activity);
        themeManager.applyOrtusBrandTheme(activity);
    }
    
    /**
     * Checks if the current system theme is dark
     * @param context The context to check the theme
     * @return true if the system is in dark mode, false otherwise
     */
    public static boolean isSystemInDarkMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    
    /**
     * Sets the theme mode for the application
     * @param context The context to access shared preferences
     * @param themeMode The theme mode to set (light, dark, or system)
     */
    public static void setThemeMode(Context context, int themeMode) {
        ThemeManager themeManager = new ThemeManager(context);
        themeManager.setThemeMode(themeMode);
        
        // Apply the new theme mode immediately
        switch (themeMode) {
            case ThemeManager.THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case ThemeManager.THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case ThemeManager.THEME_MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Gets the current theme mode
     * @param context The context to access shared preferences
     * @return The current theme mode
     */
    public static int getCurrentThemeMode(Context context) {
        ThemeManager themeManager = new ThemeManager(context);
        return themeManager.getThemeMode();
    }
    
    /**
     * Applies a custom theme based on a specific color
     * @param activity The activity to apply the theme to
     * @param primaryColor The primary color to base the theme on
     * @param isDark Whether to generate a dark theme
     */
    public static void applyCustomTheme(Activity activity, int primaryColor, boolean isDark) {
        ThemeManager themeManager = new ThemeManager(activity);
        themeManager.applyHarmonizedTheme(primaryColor, activity);
    }
    
    /**
     * Resets the theme to the default OrtusPOS brand theme
     * @param activity The activity to reset the theme for
     */
    public static void resetToDefaultTheme(Activity activity) {
        ThemeManager themeManager = new ThemeManager(activity);
        themeManager.applyOrtusBrandTheme(activity);
    }
}