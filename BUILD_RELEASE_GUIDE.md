# OrtusPOS App - Building and Release Guide

## Overview
OrtusPOS is a Point of Sale (POS) system with support for Bluetooth scales and other peripherals. The app has two product flavors:
- **wcr**: Web Cash Register flavor (package: com.opurex.ortus.client.wcr)
- **vanilla**: Standard flavor (package: com.opurex.ortus.client.vanilla, app name: "OrtusPOSScale")

## Prerequisites
- Android Studio or command-line Android SDK tools
- Java 11 JDK
- Gradle 7.5 or higher

## Building the App

### 1. Clone the Repository
```bash
git clone <repository-url>
cd OrtusPOSScaleApp
```

### 2. Build Specific Flavors

#### Debug Builds:
```bash
# Build wcr debug APK
./gradlew assembleWcrDebug

# Build vanilla debug APK  
./gradlew assembleVanillaDebug
```

#### Release Builds:
```bash
# Build wcr release APK
./gradlew assembleWcrRelease

# Build vanilla release APK
./gradlew assembleVanillaRelease
```

### 3. Build All Variants
```bash
# Build all debug variants
./gradlew assembleDebug

# Build all release variants
./gradlew assembleRelease

# Build everything
./gradlew build
```

## APK Output Locations
Built APKs are located in:
- `/app/build/outputs/apk/wcr/debug/` (wcr debug APK)
- `/app/build/outputs/apk/vanilla/debug/` (vanilla debug APK)
- `/app/build/outputs/apk/wcr/release/` (wcr release APK)
- `/app/build/outputs/apk/vanilla/release/` (vanilla release APK)

## Release Process

### 1. Prepare for Release
- Update version codes and names in `app/build.gradle`
- Test thoroughly on target devices
- Verify all features work properly
- Update changelog/documentation

### 2. Generate Signed APK
The app is configured with signing configurations. To generate a signed release APK:

```bash
# For wcr flavor
./gradlew assembleWcrRelease

# For vanilla flavor
./gradlew assembleVanillaRelease
```

### 3. Find Generated APKs
The signed APKs will be in:
- `app/build/outputs/apk/wcr/release/app-wcr-release.apk`
- `app/build/outputs/apk/vanilla/release/app-vanilla-release.apk`

## Android 15 and Redmi Device Compatibility

The app includes special handling for Android 15 and Redmi devices:
- Bluetooth foreground service for background operations
- Enhanced permission handling
- Location services requirement checks
- Battery optimization request handling

## Debug Logging

The app now supports file-based logging at:
`Android/data/com.opurex.ortus.client/files/OrtusPOS/dcm/debug.log`

On the device, this translates to:
`/storage/emulated/0/Android/data/com.opurex.ortus.client/files/OrtusPOS/dcm/debug.log`

This log captures all application events and errors for debugging purposes.

Note: This location is in the app's private external storage, which means:
- The file is only accessible when the app has appropriate permissions
- The file gets deleted when the app is uninstalled
- The file is specific to the app installation

## Testing Checklist

Before releasing, ensure:
- [ ] Both flavors build successfully
- [ ] Bluetooth scale functionality works
- [ ] All permissions are properly handled
- [ ] App works on target Android versions (especially Android 15)
- [ ] Redmi device compatibility features work
- [ ] Logging works correctly
- [ ] All UI elements display properly
- [ ] Payment processing works
- [ ] Receipt printing works

## Troubleshooting

### Common Build Issues:
- **Missing keystore**: Ensure `keystore.properties` file exists with proper configuration
- **Java version**: Ensure Java 11 is used
- **Gradle version**: Use the wrapper Gradle version (7.5)

### Common Runtime Issues:
- **Bluetooth permissions**: Ensure location services are enabled
- **Background operations**: Check battery optimization settings
- **Scale connectivity**: Verify foreground service is running

## Version Information
Current version code: 8002006
Current version name: 8.2.6

## Support
For support issues, contact the development team or check the project documentation.