package com.opurex.ortus.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.color.utilities.DynamicScheme;
import com.google.android.material.color.utilities.Hct;
import com.opurex.ortus.client.R;

/**
 * Theme manager for OrtusPOS application
 * Handles dynamic theming based on Material Design 3 principles
 */
public class ThemeManager {
    
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";
    private static final String KEY_CUSTOM_PRIMARY_COLOR = "custom_primary_color";
    private static final String KEY_CUSTOM_SECONDARY_COLOR = "custom_secondary_color";
    private static final String KEY_CUSTOM_TERTIARY_COLOR = "custom_tertiary_color";
    
    public static final int THEME_MODE_LIGHT = 0;
    public static final int THEME_MODE_DARK = 1;
    public static final int THEME_MODE_SYSTEM = 2;

    private Context context;
    private SharedPreferences prefs;

    public ThemeManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Apply the current theme to an activity
     * @param activity The activity to apply the theme to
     */
    public void applyTheme(Activity activity) {
        // Apply dynamic colors if supported and enabled
        if (DynamicColors.isDynamicColorAvailable()) {
            DynamicColors.applyToActivityIfAvailable(activity);
        }

        // Apply custom theme based on user preferences
        applyCustomTheme(activity);
    }

    /**
     * Apply a custom theme based on user preferences
     * @param activity The activity to apply the theme to
     */
    private void applyCustomTheme(Activity activity) {
        int themeMode = getThemeMode();
        switch (themeMode) {
            case THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Generate a theme based on a custom primary color
     * @param primaryColor The primary color to base the theme on
     * @param isDark Whether to generate a dark theme
     * @return A color scheme based on the primary color
     */
    public DynamicScheme generateCustomTheme(@ColorInt int primaryColor, boolean isDark) {
        return MaterialColorUtils.generateColorScheme(primaryColor, isDark);
    }
    
    /**
     * Apply a theme based on an image bitmap
     * @param bitmap The bitmap to extract colors from
     * @param activity The activity to apply the theme to
     */
    public void applyThemeFromBitmap(Bitmap bitmap, Activity activity) {
        if (bitmap == null) return;

        int primaryColor = MaterialColorUtils.getColorFromBitmap(bitmap);
        DynamicScheme scheme = generateCustomTheme(primaryColor, isDarkTheme());

        // Save the custom color as the primary color preference
        setCustomPrimaryColor(primaryColor);

        // Apply the theme
        applyCustomTheme(activity);
    }
    
    /**
     * Apply the OrtusPOS brand theme
     * @param activity The activity to apply the theme to
     */
    public void applyOrtusBrandTheme(Activity activity) {
        int primaryColor = MaterialColorUtils.getOrtusBrandPrimary(activity);
        boolean isDark = isDarkTheme();

        // Generate and apply the theme based on the brand color
        DynamicScheme scheme = MaterialColorUtils.generateColorScheme(primaryColor, isDark);

        // Apply the custom theme
        applyCustomTheme(activity);
    }
    
    /**
     * Get the current theme mode
     * @return The current theme mode (THEME_MODE_LIGHT, THEME_MODE_DARK, or THEME_MODE_SYSTEM)
     */
    public int getThemeMode() {
        return prefs.getInt(KEY_THEME_MODE, THEME_MODE_SYSTEM);
    }
    
    /**
     * Set the theme mode
     * @param themeMode The theme mode to set
     */
    public void setThemeMode(int themeMode) {
        prefs.edit().putInt(KEY_THEME_MODE, themeMode).apply();
    }
    
    /**
     * Get the custom primary color
     * @return The custom primary color, or the default brand color if none is set
     */
    public int getCustomPrimaryColor() {
        int defaultColor = MaterialColorUtils.getOrtusBrandPrimary(context);
        return prefs.getInt(KEY_CUSTOM_PRIMARY_COLOR, defaultColor);
    }
    
    /**
     * Set the custom primary color
     * @param color The color to set as primary
     */
    public void setCustomPrimaryColor(@ColorInt int color) {
        prefs.edit().putInt(KEY_CUSTOM_PRIMARY_COLOR, color).apply();
    }
    
    /**
     * Get the custom secondary color
     * @return The custom secondary color, or the default brand color if none is set
     */
    public int getCustomSecondaryColor() {
        int defaultColor = MaterialColorUtils.getOrtusBrandSecondary(context);
        return prefs.getInt(KEY_CUSTOM_SECONDARY_COLOR, defaultColor);
    }
    
    /**
     * Set the custom secondary color
     * @param color The color to set as secondary
     */
    public void setCustomSecondaryColor(@ColorInt int color) {
        prefs.edit().putInt(KEY_CUSTOM_SECONDARY_COLOR, color).apply();
    }
    
    /**
     * Get the custom tertiary color
     * @return The custom tertiary color, or the default brand color if none is set
     */
    public int getCustomTertiaryColor() {
        int defaultColor = MaterialColorUtils.getOrtusBrandTertiary(context);
        return prefs.getInt(KEY_CUSTOM_TERTIARY_COLOR, defaultColor);
    }
    
    /**
     * Set the custom tertiary color
     * @param color The color to set as tertiary
     */
    public void setCustomTertiaryColor(@ColorInt int color) {
        prefs.edit().putInt(KEY_CUSTOM_TERTIARY_COLOR, color).apply();
    }
    
    /**
     * Reset all custom colors to defaults
     */
    public void resetCustomColors() {
        prefs.edit()
            .remove(KEY_CUSTOM_PRIMARY_COLOR)
            .remove(KEY_CUSTOM_SECONDARY_COLOR)
            .remove(KEY_CUSTOM_TERTIARY_COLOR)
            .apply();
    }
    
    /**
     * Check if the current theme is dark
     * @return True if the current theme is dark, false otherwise
     */
    public boolean isDarkTheme() {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * Check if the current theme mode is dark
     * @return True if the current theme mode is dark, false otherwise
     */
    private boolean isDarkThemeMode() {
        int themeMode = getThemeMode();
        if (themeMode == THEME_MODE_DARK) {
            return true;
        } else if (themeMode == THEME_MODE_LIGHT) {
            return false;
        } else {
            // For system mode, check the current system setting
            int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        }
    }
    
    /**
     * Check if the device supports Material You dynamic colors
     * @return True if dynamic colors are supported, false otherwise
     */
    public boolean supportsDynamicColors() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && DynamicColors.isDynamicColorAvailable();
    }
    
    /**
     * Apply a harmonized color scheme based on the primary color
     * @param primaryColor The primary color to harmonize with
     * @param activity The activity to apply the theme to
     */
    public void applyHarmonizedTheme(@ColorInt int primaryColor, Activity activity) {
        // Generate a scheme based on the primary color
        DynamicScheme scheme = generateCustomTheme(primaryColor, isDarkTheme());

        // Save the custom primary color
        setCustomPrimaryColor(primaryColor);

        // Apply the theme
        applyCustomTheme(activity);
    }
}