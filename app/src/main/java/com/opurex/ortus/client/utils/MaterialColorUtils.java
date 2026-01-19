package com.opurex.ortus.client.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.color.utilities.ColorUtils;
import com.google.android.material.color.utilities.Hct;
import com.google.android.material.color.utilities.DynamicScheme;
import com.google.android.material.color.utilities.SchemeTonalSpot;
import com.opurex.ortus.client.R;

/**
 * Utility class for Material Design 3 color theming
 * Provides methods to generate dynamic color schemes based on brand colors
 */
public class MaterialColorUtils {
    
    /**
     * Generates a Material 3 color scheme based on a source color
     * @param sourceColor The source color to generate the scheme from (e.g., brand primary color)
     * @param isDark Whether to generate a dark theme
     * @return A color scheme with all Material 3 color roles
     */
    public static DynamicScheme generateColorScheme(@ColorInt int sourceColor, boolean isDark) {
        Hct hct = Hct.fromInt(sourceColor);
        return new SchemeTonalSpot(hct, isDark, 0.0);
    }
    
    /**
     * Extracts the primary color from an image bitmap
     * @param bitmap The source bitmap to extract color from
     * @return The primary color extracted from the bitmap
     */
    public static int getColorFromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return Color.TRANSPARENT;
        }

        // Get the center pixel as a representative color
        int centerX = bitmap.getWidth() / 2;
        int centerY = bitmap.getHeight() / 2;
        return bitmap.getPixel(centerX, centerY);
    }
    
    /**
     * Gets the OrtusPOS brand primary color from resources
     * @param context The context to access resources
     * @return The brand primary color
     */
    public static int getOrtusBrandPrimary(Context context) {
        try {
            return context.getResources().getColor(R.color.colorPrimary, context.getTheme());
        } catch (Resources.NotFoundException e) {
            // Fallback to a default color if resource not found
            return Color.parseColor("#9E36A4"); // OrtusPOS primary purple
        }
    }

    /**
     * Gets the OrtusPOS brand secondary color from resources
     * @param context The context to access resources
     * @return The brand secondary color
     */
    public static int getOrtusBrandSecondary(Context context) {
        try {
            return context.getResources().getColor(R.color.colorSecondary, context.getTheme());
        } catch (Resources.NotFoundException e) {
            // Fallback to a default color if resource not found
            return Color.parseColor("#303DB9"); // OrtusPOS secondary blue
        }
    }

    /**
     * Gets the OrtusPOS brand tertiary color from resources
     * @param context The context to access resources
     * @return The brand tertiary color
     */
    public static int getOrtusBrandTertiary(Context context) {
        try {
            return context.getResources().getColor(R.color.md_theme_tertiary, context.getTheme());
        } catch (Resources.NotFoundException e) {
            // Fallback to a default color if resource not found
            return Color.parseColor("#FEB01C"); // OrtusPOS tertiary yellow
        }
    }
    
    /**
     * Calculates a harmonized color based on the primary color
     * @param sourceColor The source color to harmonize with
     * @param targetColor The color to harmonize
     * @return The harmonized color
     */
    public static int harmonizeWithPrimary(@ColorInt int sourceColor, @ColorInt int targetColor) {
        Hct sourceHct = Hct.fromInt(sourceColor);
        Hct targetHct = Hct.fromInt(targetColor);
        
        // Adjust the hue of the target color to be closer to the source color
        double harmonizedHue = sourceHct.getHue();
        double harmonizedChroma = Math.min(sourceHct.getChroma(), targetHct.getChroma());
        
        return Hct.from(harmonizedHue, harmonizedChroma, targetHct.getTone()).toInt();
    }
    
    /**
     * Checks if dynamic colors are supported on the current device
     * @param context The context to check device capabilities
     * @return True if dynamic colors are supported, false otherwise
     */
    public static boolean areDynamicColorsSupported(Context context) {
        return DynamicColors.isDynamicColorAvailable();
    }
    
    /**
     * Applies a color tone adjustment to create lighter or darker variants
     * @param color The base color to adjust
     * @param toneAdjustment The tone adjustment (-20 for darker, +20 for lighter)
     * @return The adjusted color
     */
    public static int adjustColorTone(@ColorInt int color, double toneAdjustment) {
        Hct hct = Hct.fromInt(color);
        double newTone = Math.max(0, Math.min(100, hct.getTone() + toneAdjustment));
        return Hct.from(hct.getHue(), hct.getChroma(), newTone).toInt();
    }
    
    /**
     * Converts an RGB color to HCT color space
     * @param color The RGB color to convert
     * @return The HCT representation of the color
     */
    public static Hct rgbToHct(@ColorInt int color) {
        return Hct.fromInt(color);
    }
    
    /**
     * Creates a color palette based on the OrtusPOS brand colors
     * @param context The context to access resources
     * @return An array of colors representing the brand palette
     */
    public static int[] getOrtusColorPalette(Context context) {
        try {
            return new int[] {
                getOrtusBrandPrimary(context),
                getOrtusBrandSecondary(context),
                getOrtusBrandTertiary(context),
                context.getResources().getColor(R.color.md_theme_surface, context.getTheme()),
                context.getResources().getColor(R.color.md_theme_primaryContainer, context.getTheme()),
                context.getResources().getColor(R.color.md_theme_secondaryContainer, context.getTheme())
            };
        } catch (Resources.NotFoundException e) {
            // Fallback to default brand colors if resources not found
            return new int[] {
                Color.parseColor("#9E36A4"), // Primary
                Color.parseColor("#303DB9"), // Secondary
                Color.parseColor("#FEB01C"), // Tertiary
                Color.parseColor("#FFFFFF"), // Surface
                Color.parseColor("#EADDFF"), // Primary container
                Color.parseColor("#A8E6CF")  // Secondary container
            };
        }
    }
}