# Material Design 3 Integration for OrtusPOS

This document explains how the Material Design 3 color theming system has been integrated into the OrtusPOS application using Google's Material Color Utilities.

## Overview

The OrtusPOS application has been updated to use Material Design 3 principles with dynamic color theming capabilities. This includes:

- Full Material 3 color system with proper color roles
- Integration of OrtusPOS brand colors (#9E36A4 primary, #303DB9 secondary, #FEB01C tertiary)
- Dynamic color generation based on source colors
- Support for both light and dark themes
- Compatibility with Android 12+ dynamic colors (Material You)

## Key Components

### 1. MaterialColorUtils.java
Utility class for generating Material 3 color schemes based on source colors.

Key features:
- `generateColorScheme(int sourceColor, boolean isDark)` - Creates a complete Material 3 color scheme
- `getOrtusBrandPrimary(Context)` - Gets the OrtusPOS primary brand color
- `getOrtusColorPalette(Context)` - Gets the complete OrtusPOS brand color palette
- `harmonizeWithPrimary(int, int)` - Harmonizes colors with the primary color
- `adjustColorTone(int, double)` - Adjusts color lightness/darkness

### 2. ThemeManager.java
Manages application-wide theme settings and preferences.

Key features:
- Theme mode management (light/dark/system)
- Custom color preferences storage
- Dynamic theme application
- Support for image-based theming

### 3. MaterialThemeHelper.java
Helper class to easily apply Material 3 theming to activities.

Key methods:
- `initializeMaterialTheme(Context)` - Initialize the Material 3 theme system
- `applyMaterial3Theme(Activity)` - Apply Material 3 theming to an activity
- `applyCustomTheme(Activity, int, boolean)` - Apply a custom theme based on a color

## Usage Examples

### Applying Material 3 Theming to an Activity

```java
public class MyActivity extends TrackedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply Material 3 theming before calling super.onCreate
        MaterialThemeHelper.applyMaterial3Theme(this);
        
        setContentView(R.layout.my_activity_layout);
    }
}
```

### Initializing Material 3 Theming for the Application

In your Application class (OrtusPOS.java):

```java
public class OrtusPOS extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OrtusPOS.context = getApplicationContext();
        
        // Initialize Material 3 theming
        MaterialThemeHelper.initializeMaterialTheme(this);
    }
}
```

### Creating a Custom Theme Based on a Color

```java
// Apply a custom theme based on a specific color
int customColor = Color.parseColor("#FF6B35"); // Orange
MaterialThemeHelper.applyCustomTheme(this, customColor, false); // Light theme
```

### Switching Between Theme Modes

```java
// Switch to dark mode
MaterialThemeHelper.setThemeMode(this, ThemeManager.THEME_MODE_DARK);

// Switch to light mode
MaterialThemeHelper.setThemeMode(this, ThemeManager.THEME_MODE_LIGHT);

// Follow system theme
MaterialThemeHelper.setThemeMode(this, ThemeManager.THEME_MODE_SYSTEM);
```

## Color Resources

The following color resources have been defined in `colors.xml`:

- `ortus_primary` - Primary brand color (#9E36A4)
- `ortus_secondary` - Secondary brand color (#303DB9)
- `ortus_tertiary` - Tertiary brand color (#FEB01C)
- `ortus_neutral` - Neutral color (#DDC2DE)
- `ortus_primary_variant` - Primary variant (#702DB2)
- `ortus_secondary_variant` - Secondary variant (#E24778)

## Material 3 Color Roles

The theme properly implements all Material 3 color roles:

- Primary, Secondary, Tertiary colors and their containers
- Surface, Background colors
- Error colors
- Outline colors
- On colors for all the above

## Dynamic Color Support

On Android 12+ devices, the application will automatically use Material You dynamic colors if available. The application also provides custom color theming capabilities that work on all supported Android versions.

## Best Practices

1. Always call `MaterialThemeHelper.applyMaterial3Theme()` early in your activity's `onCreate()` method
2. Use the MaterialColorUtils to generate harmonious color schemes
3. Respect user's theme preferences (light/dark/system)
4. Maintain accessibility by ensuring proper contrast ratios
5. Use the OrtusPOS brand colors as the foundation for custom themes

## Migration Notes

The application has been migrated from the deprecated Holo theme to Material 3 design system while maintaining full functionality. All UI components now follow Material Design 3 guidelines with proper color roles and accessibility considerations.