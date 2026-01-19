# Database Export Feature Implementation

## Overview
This document describes the implementation of the database export functionality for the OrtusPOS Android application.

## Implementation Details

### 1. DatabaseExportUtil Class
- Created `DatabaseExportUtil.java` in `com.opurex.ortus.client.utils` package
- Provides static method `exportDatabase(Context context)` to export the SQLite database
- Handles both older Android versions (with external storage permissions) and newer versions (Android 10+) with scoped storage

### 2. Export Features
- Exports the `ortuspos.db` SQLite database file
- For Android 10+ (API 29+): Saves to app-specific external files directory to comply with scoped storage
- For older Android versions: Saves to package-named directory under external storage
- Filename includes timestamp in format `yyyyMMdd_HHmmss`
- Shows toast notification on successful or failed export

### 3. Integration with Existing Code
- Modified `Configure.java` export method to include database export functionality
- Added necessary permissions to `AndroidManifest.xml`:
  - `WRITE_EXTERNAL_STORAGE`
  - `READ_EXTERNAL_STORAGE`
- Added import for `DatabaseExportUtil` class

### 4. Runtime Permissions Handling
- Implemented methods to check and request storage permissions
- For Android 11+: Uses `MANAGE_EXTERNAL_STORAGE` permission
- For older Android versions: Uses `WRITE_EXTERNAL_STORAGE` permission
- Properly handles permission result callbacks

### 5. Enhanced Export Options
- Added a dialog that allows users to choose what to export:
  - "Export Configuration Only": Exports JSON config and cash archive files only
  - "Export Database Only": Exports the SQLite database file only
  - "Export Both": Exports both configuration and database

## Usage
1. Navigate to the Configuration screen in the app
2. Select the "Export" option from the menu (the same menu that has import and debug options)
3. A dialog will appear allowing you to choose what to export
4. If database export is selected, the app will request storage permissions if not already granted
5. Files will be exported to the appropriate location based on the Android version

## File Locations
- **Android 10+**: `Android/data/[package_name]/files/database_exports/` (for database) and `[package_name]/` (for config)
- **Android 9 and below**: `/storage/emulated/0/[package_name]/`

## Technical Considerations
- Implements proper file copy with try-with-resources to ensure streams are closed
- Uses timestamped filenames to prevent overwriting previous exports
- Handles potential IOExceptions during file operations
- Uses Android's default SQLite database path mechanism (`context.getDatabasePath()`)
- Properly handles runtime permissions and user responses

## Security and Privacy
- Exported database files are stored in locations accessible by other apps on the device
- Users should be aware that exported databases may contain sensitive business data
- The app properly requests and handles storage permissions according to Android best practices