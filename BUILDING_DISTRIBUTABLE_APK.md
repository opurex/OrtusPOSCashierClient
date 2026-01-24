# Building a Distributable APK for OrtusPOS

## Prerequisites

Before building a distributable APK, ensure you have:

1. **Java Development Kit (JDK)** - Version 8 or higher
2. **Android Studio** - Latest stable version
3. **Gradle** - Usually bundled with Android Studio
4. **Android SDK** - With required API levels

## Building a Release APK

### Method 1: Using Android Studio

1. **Open the Project**
   - Launch Android Studio
   - Open the OrtusPOS project directory

2. **Clean and Build**
   - Go to `Build` → `Clean Project`
   - Wait for clean to complete
   - Go to `Build` → `Rebuild Project`

3. **Generate Signed APK**
   - Go to `Build` → `Generate Signed Bundle / APK`
   - Select `APK` and click `Next`
   - Click `Create new...` to create a new keystore (if you don't have one):
     - Key store path: Choose a secure location
     - Password: Create a strong password
     - Key alias: Give it a meaningful name
     - Key password: Create another strong password
     - Fill in certificate details (optional)
   - If you have an existing keystore, browse and select it
   - Enter keystore password and key alias
   - Enter key password
   - Select `release` as Build Variant
   - Click `Finish`

4. **Locate the Generated APK**
   - The APK will be saved in `app/release/` directory
   - Look for a file named `app-release.apk` or `app-release-signed.apk`

### Method 2: Using Command Line

1. **Navigate to Project Directory**
   ```bash
   cd /path/to/OrtusPOSScaleApp
   ```

2. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```
   
   On Windows:
   ```cmd
   gradlew.bat assembleRelease
   ```

3. **Locate the APK**
   - Find the APK in `app/build/outputs/apk/release/`
   - Look for `app-release.apk`

### Method 3: Using Gradle Wrapper with Specific Build Variants

1. **Check Available Build Variants**
   ```bash
   ./gradlew tasks | grep assemble
   ```

2. **Build Specific Variant**
   ```bash
   ./gradlew assembleRelease
   ```

## Important Configuration Files

### 1. build.gradle (Module: app)
Check these important settings:

```gradle
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.opurex.ortus.client"  // Unique identifier
        minSdk 21                              // Minimum Android version
        targetSdk 34                           // Target Android version
        versionCode 1                          // Increment for updates
        versionName "1.0"                      // Human-readable version
    }
    
    signingConfigs {
        release {
            storeFile file('path/to/keystore.jks')
            storePassword 'password'
            keyAlias 'key_alias'
            keyPassword 'key_password'
        }
    }
    
    buildTypes {
        release {
            minifyEnabled true          // Enable code shrinking
            shrinkResources true       // Shrink resources
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
}
```

### 2. AndroidManifest.xml
Ensure the manifest is properly configured:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- Required permissions -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        
        <!-- Activities -->
        <activity android:name=".activities.MainActivity"
                  android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
```

## Preparing for Distribution

### 1. Version Management
- Update `versionCode` and `versionName` in `build.gradle`
- `versionCode` should be incremented for each release
- `versionName` should reflect the human-readable version

### 2. Testing the APK
Before distribution, test the APK:
- Install on different devices with various screen sizes
- Test all major functionalities
- Verify Bluetooth and network connectivity
- Check receipt printing functionality

### 3. Optimize the APK
- Enable code shrinking and resource shrinking
- Use ProGuard/R8 to reduce APK size
- Remove unused resources and dependencies

## Distribution Methods

### 1. Direct APK Distribution
- Share the generated APK file directly
- Users install by opening the APK file
- May need to enable "Install from Unknown Sources" in settings

### 2. Google Play Store
- Upload the APK to Google Play Console
- Follow Google's guidelines and policies
- Requires developer account ($25 one-time fee)

### 3. Alternative App Stores
- Samsung Galaxy Store
- Huawei AppGallery
- Amazon Appstore
- Third-party stores

## Security Considerations

### 1. Keystore Management
- Keep your keystore file secure
- Remember the passwords
- Back up the keystore securely
- Never share the keystore publicly

### 2. Code Protection
- Enable minification and obfuscation
- Use ProGuard rules to protect sensitive code
- Consider using Android App Bundle for better security

### 3. Permissions
- Only request necessary permissions
- Explain why permissions are needed
- Follow privacy regulations

## Troubleshooting Common Issues

### 1. Build Errors
- Ensure all dependencies are properly configured
- Check for conflicting library versions
- Verify SDK and build tools versions

### 2. Installation Issues
- Check minimum SDK requirements
- Verify signature compatibility
- Ensure sufficient storage space

### 3. Runtime Issues
- Test on various Android versions
- Check for device-specific compatibility
- Verify hardware requirements (Bluetooth, etc.)

## Final Checklist

Before distributing your APK:

- [ ] Successfully built in release mode
- [ ] Tested on multiple devices
- [ ] Verified all core functionalities work
- [ ] Checked Bluetooth and network features
- [ ] Confirmed receipt printing works
- [ ] Updated version numbers
- [ ] Optimized for size
- [ ] Secured keystore information
- [ ] Prepared app store listings (if applicable)

The generated APK will be a standalone installation file that others can install on their Android devices to use the OrtusPOS application.