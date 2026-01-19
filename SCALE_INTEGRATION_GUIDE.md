# Aclas Bluetooth Scale Integration Guide for OrtusPOS

## 1. Overview

This guide details the steps to integrate the Aclas Bluetooth Scale SDK (`PSXAndroid_BT_SDK_En_V1.006`) into the OrtusPOS Android application. The goal is to enable the app to connect to a Bluetooth scale, receive weight data in real-time, and use that data within the `ProductScaleDialog` to process transactions for scaled products.

This guide assumes the existing architecture described in `SCALE_FUNCTIONALITY_IMPLEMENTATION.md`, which includes a `ScaleManager` (or a similar helper class) responsible for handling the scale logic and a `ProductScaleDialog` for the user interface.

## 2. Prerequisites

### A. Add SDK Library
Ensure the `AclasScaleSdk.jar` file from the SDK is included in your OrtusPOS app's `app/libs` directory and added as a dependency in your `app/build.gradle` file.

```groovy
// app/build.gradle
dependencies {
    // ... other dependencies
    implementation files('libs/AclasScaleSdk.jar')
}
```

### B. Add Android Manifest Permissions
Add the necessary Bluetooth and location permissions from the SDK's `AndroidManifest.xml` to your app's `AndroidManifest.xml`.

```xml
<!-- /app/src/main/AndroidManifest.xml -->
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

## 3. Integration Steps

The core of the integration involves centralizing all interaction with the scale hardware in your `ScaleManager` class and then using a listener pattern to communicate with the UI, specifically the `ProductScaleDialog`.

### Step 1: Initialize the ScaleManager

Your `ScaleManager` will be the single source of truth for the scale's state and data. It should initialize the `AclasScaler` and manage its lifecycle.

```java
// In your ScaleManager.java
import com.example.scaler.AclasScaler;
import android.content.Context;

public class ScaleManager implements AclasScaler.AclasScalerListener, AclasScaler.AclasBluetoothListener {

    private AclasScaler mScaler;
    private Context mContext;
    private WeightListener mWeightListener;
    private ScanListener mScanListener;
    private ConnectionListener mConnectionListener;

    // Listener interfaces for UI communication
    public interface WeightListener {
        void onWeightChanged(AclasScaler.WeightInfoNew weightInfo);
    }
    public interface ScanListener {
        void onDeviceFound(String name, String mac, String signal);
        void onScanFinished();
    }
    public interface ConnectionListener {
        void onScaleConnected();
        void onScaleDisconnected();
        void onScaleError(String message);
    }

    public ScaleManager(Context context) {
        this.mContext = context;
    }

    public void initialize() {
        if (mScaler == null) {
            mScaler = new AclasScaler(AclasScaler.Type_FSC, mContext, this);
            mScaler.setBluetoothListener(this);
            mScaler.setLog(true); // Enable logging for debugging
        }
    }

    // ... listener setters, connection methods, etc.
}
```

### Step 2: Implement Device Discovery

The `ScaleManager` should handle scanning for available Bluetooth scales.

```java
// In your ScaleManager.java

public void startScan() {
    if (mScaler != null) {
        mScaler.startScanBluetooth(true);
    }
}

public void stopScan() {
    if (mScaler != null) {
        mScaler.startScanBluetooth(false);
    }
}

// AclasScaler.AclasBluetoothListener implementation
@Override
public void onSearchBluetooth(String deviceInfo) {
    // deviceInfo format is "name,mac,signal"
    if (deviceInfo != null && deviceInfo.contains(",")) {
        String[] parts = deviceInfo.split(",");
        if (mScanListener != null) {
            mScanListener.onDeviceFound(parts[0], parts[1], parts[2]);
        }
    }
}

@Override
public void onSearchFinish() {
    if (mScanListener != null) {
        mScanListener.onScanFinished();
    }
}
```

### Step 3: Manage Connection

Add methods to connect to a selected scale and handle connection state changes.

```java
// In your ScaleManager.java

public void connect(String mac) {
    if (mScaler != null) {
        new Thread(() -> {
            // AclasConnect is a blocking call, so run it in a background thread.
            // Pass the MAC address for the initial connection.
            int result = mScaler.AclasConnect(mac);
            if (result != 0) {
                if (mConnectionListener != null) {
                    mConnectionListener.onScaleError("Connection failed");
                }
            }
        }).start();
    }
}

public void disconnect() {
    if (mScaler != null) {
        mScaler.AclasDisconnect();
    }
}

// AclasScaler.AclasScalerListener implementation
@Override
public void onConnected() {
    if (mConnectionListener != null) {
        mConnectionListener.onScaleConnected();
    }
}

@Override
public void onDisConnected() {
    if (mConnectionListener != null) {
        mConnectionListener.onScaleDisconnected();
    }
}

@Override
public void onError(int errorNum, String errorMessage) {
    if (mConnectionListener != null) {
        mConnectionListener.onScaleError("Error " + errorNum + ": " + errorMessage);
    }
}
```

### Step 4: Receive and Broadcast Weight Data

This is the most critical step. The `ScaleManager` receives data from the SDK and forwards it to the active UI component.

```java
// In your ScaleManager.java

// AclasScaler.AclasScalerListener implementation
@Override
public void onRcvData(AclasScaler.WeightInfoNew info) {
    if (mWeightListener != null) {
        // Forward the new weight information to the registered listener (ProductScaleDialog)
        mWeightListener.onWeightChanged(info);
    }
}

// Required empty implementation
@Override
public void onUpdateProcess(int iIndex, int iTotal) {}

// Method for the UI to register a listener
public void setWeightListener(WeightListener listener) {
    this.mWeightListener = listener;
}
```

### Step 5: Integrate with `ProductScaleDialog`

Modify your `ProductScaleDialog` to use the `ScaleManager` and update its UI in real-time.

```java
// In ProductScaleDialog.java

public class ProductScaleDialog extends DialogFragment implements ScaleManager.WeightListener {

    private ScaleManager mScaleManager;
    private TextView weightTextView;
    private TextView priceTextView;
    private Product mProduct;

    @Override
    public void onResume() {
        super.onResume();
        // Get the singleton instance of your ScaleManager
        mScaleManager = // ... get your ScaleManager instance
        // Register this dialog to receive weight updates
        mScaleManager.setWeightListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister to prevent memory leaks and unwanted updates
        if (mScaleManager != null) {
            mScaleManager.setWeightListener(null);
        }
    }

    @Override
    public void onWeightChanged(AclasScaler.WeightInfoNew weightInfo) {
        // This is called by the ScaleManager on a background thread.
        // Ensure UI updates are done on the main thread.
        getActivity().runOnUiThread(() -> {
            if (weightInfo.isOverWeight) {
                weightTextView.setText("----");
            } else {
                // The SDK provides the weight as a formatted string
                String weightString = weightInfo.toString();
                double weightValue = weightInfo.netWeight;

                // Update UI
                weightTextView.setText(weightString);

                // Real-time price calculation
                double totalPrice = mProduct.getPrice() * weightValue;
                priceTextView.setText(String.format("%.2f", totalPrice));

                // You can also display stability status
                boolean isStable = weightInfo.isStable;
                // (e.g., change color of weight text based on stability)
            }
        });
    }

    // ... other dialog logic
}
```

### Step 6: Tare and Zero Operations

The SDK example (`MainActivity.java`) does not show explicit method calls for "Zero" or "Tare". The `stKeys` array suggests these are key presses sent from the scale itself.

1.  **Check the SDK:** First, inspect the `AclasScaler` class from `AclasScaleSdk.jar` for public methods like `zero()` or `tare()`. If they exist, you can add corresponding methods to your `ScaleManager` and call them from your `ProductScaleDialog`'s buttons.

2.  **Assume Physical Buttons:** If no such methods exist, the user is expected to press the "Zero" and "Tare" buttons on the physical scale. Your application does not need to send these commands. The `WeightInfoNew` object contains the `tareWeight`, so your UI can accurately display the current tare value.

## 4. Full Workflow

1.  **App Start:** Your application initializes the `ScaleManager` singleton.
2.  **Connect to Scale:** The user navigates to a "Settings" or "Connect Scale" screen. This screen uses `ScaleManager.startScan()` to find devices and `ScaleManager.connect(mac)` to connect.
3.  **Select Scaled Product:** The user adds a product marked with `scaled = true` to the ticket.
4.  **Open Dialog:** The `ProductScaleDialog` opens. In its `onResume`, it registers itself as the `WeightListener` for the `ScaleManager`.
5.  **Receive Weight:** The user places the item on the scale. The `ScaleManager` receives `onRcvData` callbacks and passes the `WeightInfoNew` object to the `ProductScaleDialog`.
6.  **Update UI:** The dialog's `onWeightChanged` method is triggered, updating the weight and total price in real-time.
7.  **Confirm Weight:** Once the weight is stable, the user clicks "OK". The dialog returns the final weight (`weightInfo.netWeight`) to the calling activity/fragment.
8.  **Add to Ticket:** The `addAScaledProductToTicket()` method is called with the product and the final weight.
9.  **Dismiss Dialog:** The dialog's `onPause` is called, and it unregisters itself as a listener.

This structured approach ensures a clean separation of concerns, making your scale integration robust, maintainable, and easy to debug.
