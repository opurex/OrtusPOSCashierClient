# Testing Guide for OrtusPOS Scale Integration

This document outlines a comprehensive testing strategy for the OrtusPOS scale integration, covering `Product`, `BluetoothScaleHelper`, `ScaleManager`, `ProductScaleDialog`, and `TicketFragment`. The goal is to ensure the correct functionality of each component and their interactions.

## 1. Test Setup and Frameworks

We will primarily use:
*   **JUnit 4/5**: For unit tests of pure Java/Kotlin logic.
*   **Mockito**: For mocking dependencies and verifying interactions.
*   **AndroidX Test Library**:
    *   **AndroidJUnit4/5**: Test runner for instrumented tests.
    *   **Espresso**: For UI interaction and assertion in instrumented tests.
    *   **FragmentScenario**: For testing `Fragment` lifecycles in isolation.
    *   **Robolectric**: (Optional, for local instrumented tests) Allows running Android tests on a JVM without an emulator, speeding up UI-related tests that don't require actual device interaction.

**Dependencies (example for `app/build.gradle`):**

```groovy
// Unit tests
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:3.12.4' // Or latest version
testImplementation 'org.mockito:mockito-inline:3.12.4' // For mocking final classes/methods

// Instrumented tests
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
androidTestImplementation 'androidx.test:runner:1.4.0'
androidTestImplementation 'androidx.test:rules:1.4.0'
androidTestImplementation 'androidx.fragment:fragment-testing:1.3.6' // For FragmentScenario
androidTestImplementation 'org.mockito:mockito-android:3.12.4' // For mocking in instrumented tests
// Optional: Robolectric
// testImplementation 'org.robolectric:robolectric:4.8.1'
```

## 2. Test Cases by Component

### 2.1. `Product` Model (Unit Tests)

**Location:** `app/src/test/java/com/opurex/ortus/client/models/ProductTest.java`

**Focus:** Basic data integrity and `isScaled()` property.

*   **`testIsScaled()`**:
    *   Verify `isScaled()` returns `true` when set to `true`.
    *   Verify `isScaled()` returns `false` when set to `false`.
    *   Verify default value if not explicitly set.
*   **`testProductSerializationDeserialization()`** (if applicable):
    *   Test that a `Product` object can be correctly serialized and deserialized (e.g., to/from JSON or Parcelable).

### 2.2. `BluetoothScaleHelper` (Instrumented Tests)

**Location:** `app/src/androidTest/java/com/opurex/ortus/client/utils/BluetoothScaleHelperTest.java`

**Focus:** Interaction with `AclasScaler` and Bluetooth hardware. Mock `AclasScaler` to control its behavior.

*   **`testInitialization()`**:
    *   Verify `BluetoothScaleHelper` constructor initializes `AclasScaler` without exceptions.
    *   Verify `AclasScaler` listeners (`AclasScalerListener`, `AclasBluetoothListener`, `AclasScalerPSXListener`) are set.
*   **`testBluetoothStateChecks()`**:
    *   Verify `isBluetoothSupported()` returns correct state (mock `BluetoothAdapter`).
    *   Verify `isBluetoothEnabled()` returns correct state (mock `BluetoothAdapter`).
*   **`testStartStopScan()`**:
    *   Mock `AclasScaler.startScanBluetooth(true/false)`.
    *   Verify `startScan()` calls `aclasScaler.startScanBluetooth(true)`.
    *   Verify `stopScan()` calls `aclasScaler.startScanBluetooth(false)`.
    *   Verify `onSearchBluetooth()` callback from `AclasScaler` triggers `ScanListener.onDeviceFound()`.
    *   Verify `onSearchFinish()` callback from `AclasScaler` triggers `ScanListener.onScanFinished()`.
*   **`testConnectToScale_success()`**:
    *   Mock `AclasScaler.AclasConnect()` to return `0`.
    *   Verify `connectToScale()` returns `true`.
    *   Verify `ConnectionStateListener.onConnected()` is called.
    *   Verify `isConnected()` returns `true`.
*   **`testConnectToScale_failure()`**:
    *   Mock `AclasScaler.AclasConnect()` to return a non-zero value.
    *   Verify `connectToScale()` returns `false`.
    *   Verify `ConnectionStateListener.onError()` is called.
    *   Verify `isConnected()` returns `false`.
*   **`testDisconnect()`**:
    *   Mock `AclasScaler.AclasDisconnect()`.
    *   Verify `disconnect()` calls `aclasScaler.AclasDisconnect()`.
    *   Verify `ConnectionStateListener.onDisconnected()` is called.
    *   Verify `isConnected()` returns `false`.
*   **`testWeightDataReception()`**:
    *   Simulate `AclasScalerListener.onRcvData(WeightInfoNew)` being called.
    *   Verify `ScaleDataListener.onWeightReceived()` is triggered with correct data.
*   **`testPriceDataReception()`** (if `AclasScalerPSXListener` is used):
    *   Simulate `AclasScalerPSXListener.onRcvData(St_PSData)` being called.
    *   Verify `ScaleDataListener.onPriceDataReceived()` is triggered with correct data.
*   **`testZeroScale()`**:
    *   Mock `AclasScaler.AclasZero()`.
    *   Verify `zeroScale()` calls `aclasScaler.AclasZero()`.
    *   Test behavior when not connected (should call `onError`).
*   **`testTareScale()`**:
    *   Mock `AclasScaler.AclasTare()`.
    *   Verify `tareScale()` calls `aclasScaler.AclasTare()`.
    *   Test behavior when not connected (should call `onError`).

### 2.3. `ScaleManager` (Unit Tests)

**Location:** `app/src/test/java/com/opurex/ortus/client/utils/ScaleManagerTest.java`

**Focus:** Correct delegation and listener propagation. Mock `BluetoothScaleHelper`.

*   **`testInitialization()`**:
    *   Verify `ScaleManager` constructor creates a `BluetoothScaleHelper` instance.
    *   Verify `BluetoothScaleHelper`'s `ScaleDataListener`, `ConnectionStateListener`, and `ScanListener` are set to internal anonymous implementations.
*   **`testMethodDelegation()`**:
    *   Verify `startScan()`, `stopScan()`, `connectToScale()`, `disconnect()`, `zeroScale()`, `tareScale()` all correctly call the corresponding methods on the mocked `BluetoothScaleHelper`.
*   **`testScaleWeightListenerPropagation()`**:
    *   Set a `ScaleManager.ScaleWeightListener`.
    *   Simulate `BluetoothScaleHelper.ScaleDataListener.onWeightReceived()` being called.
    *   Verify `ScaleManager.ScaleWeightListener.onWeightReceived()` is called.
*   **`testConnectionStateListenerPropagation()`**:
    *   Set a `ScaleManager.ConnectionStateListener`.
    *   Simulate `BluetoothScaleHelper.ConnectionStateListener.onConnected()`, `onDisconnected()`, `onError()` being called.
    *   Verify `ScaleManager.ConnectionStateListener` methods are called.
*   **`testScanListenerPropagation()`**:
    *   Set a `ScaleManager.ScanListener`.
    *   Simulate `BluetoothScaleHelper.ScanListener.onDeviceFound()`, `onScanFinished()` being called.
    *   Verify `ScaleManager.ScanListener` methods are called.

### 2.4. `ProductScaleDialog` (Instrumented Tests)

**Location:** `app/src/androidTest/java/com/opurex/ortus/client/fragments/ProductScaleDialogTest.java`

**Focus:** UI display, user interaction, and interaction with `ScaleManager`. Use `FragmentScenario`.

*   **`testNewInstance()`**:
    *   Verify `newInstance()` correctly bundles `Product` and `isProductReturn` arguments.
*   **`testLifecycle_listenerAttachment()`**:
    *   Use `FragmentScenario` to launch the dialog.
    *   Verify `onAttach()` correctly casts and assigns the `Listener`.
    *   Verify `onResume()` registers `ScaleWeightListener` and `ConnectionStateListener` with `ScaleManager`.
    *   Verify `onPause()` unregisters listeners.
*   **`testUI_initialState()`**:
    *   Verify initial display of product label, empty weight, and default status.
    *   Verify "Zero" and "Tare" buttons are disabled if scale is not connected.
*   **`testUI_onWeightReceived()`**:
    *   Mock `ScaleManager` to trigger `onWeightReceived()`.
    *   Verify `weightInput`, `weightDisplay`, and `priceDisplay` update correctly with simulated weight.
*   **`testUI_onScaleConnected()`**:
    *   Mock `ScaleManager` to trigger `onScaleConnected()`.
    *   Verify `statusDisplay` shows "Connected" and buttons are enabled.
*   **`testUI_onScaleDisconnected()`**:
    *   Mock `ScaleManager` to trigger `onScaleDisconnected()`.
    *   Verify `statusDisplay` shows "Disconnected" and buttons are disabled.
*   **`testUI_onScaleError()`**:
    *   Mock `ScaleManager` to trigger `onScaleError()`.
    *   Verify a `Toast` is shown.
*   **`testUI_manualWeightInput()`**:
    *   Type a weight into `weightInput`.
    *   Verify `priceDisplay` updates correctly based on `Product` price.
*   **`testUI_zeroButtonInteraction()`**:
    *   Ensure scale is connected (mock `ScaleManager.isConnected()` to return `true`).
    *   Click "Zero" button.
    *   Verify `scaleManager.zeroScale()` is called.
*   **`testUI_tareButtonInteraction()`**:
    *   Ensure scale is connected.
    *   Click "Tare" button.
    *   Verify `scaleManager.tareScale()` is called.
*   **`testUI_confirmButton()`**:
    *   Enter a weight (manual or simulated from scale).
    *   Click "Ok" button.
    *   Verify `ProductScaleDialog.Listener.onPsdPositiveClick()` is called with the correct `Product`, weight, and `isProductReturned` value.
    *   Verify dialog is dismissed.

### 2.5. `TicketFragment` (Instrumented Tests)

**Location:** `app/src/androidTest/java/com/opurex/ortus/client/fragments/TicketFragmentTest.java`

**Focus:** Product selection logic and interaction with `ProductScaleDialog`. Use `FragmentScenario`.

*   **`testProductSelection_nonScaled()`**:
    *   Simulate clicking a non-scaled `Product`.
    *   Verify `mListener.addProduct()` is called.
    *   Verify `ProductScaleDialog` is NOT shown.
*   **`testProductSelection_scaled()`**:
    *   Simulate clicking a scaled `Product`.
    *   Verify `ProductScaleDialog` is shown.
    *   Verify `ProductScaleDialog.newInstance()` is called with correct arguments.
    *   Verify `ProductScaleDialog.setTargetFragment()` is called with `TicketFragment`.
*   **`testOnPsdPositiveClick_addProduct()`**:
    *   Simulate `ProductScaleDialog.Listener.onPsdPositiveClick()` being called with a weight and `isProductReturned = false`.
    *   Verify `mListener.addAScaledProductToTicket()` is called with the correct `Product` and weight.
*   **`testOnPsdPositiveClick_addReturnProduct()`**:
    *   Simulate `ProductScaleDialog.Listener.onPsdPositiveClick()` being called with a weight and `isProductReturned = true`.
    *   Verify `mListener.addAScaledProductReturnToTicket()` is called with the correct `Product` and weight.
*   **`testMdfyQty_scaledProduct()`**:
    *   Simulate `mdfyQty()` being called for a scaled `TicketLine`.
    *   Verify `ProductScaleDialog` is shown.
    *   Simulate `ProductScaleDialog.Listener.onPsdPositiveClick()` being called.
    *   Verify `mTicketData.adjustScale()` is called.

### 2.6. `Transaction` Activity (Instrumented Tests)

**Location:** `app/src/androidTest/java/com/opurex/ortus/client/TransactionTest.java` (if `Transaction` is the hosting activity)

**Focus:** Overall activity flow and fragment hosting.

*   **`testTicketFragmentHosting()`**:
    *   Verify `TicketFragment` is correctly added to the `Transaction` activity.
    *   Verify `Transaction` activity correctly implements `TicketFragment.Listener` and handles its callbacks.
*   **`testScaleConnectionMenuOption()`** (if applicable):
    *   If there's a menu option in the activity to manage scale connections, test its interaction with `ScaleManager`.

## 3. General Testing Best Practices

*   **Isolation**: Use mocking extensively to test components in isolation.
*   **Clear Assertions**: Ensure each test has clear assertions about expected behavior.
*   **Edge Cases**: Test null inputs, empty lists, error conditions, and boundary values.
*   **Asynchronous Operations**: Use `IdlingResource` for Espresso tests or `CountDownLatch` for other instrumented tests to handle asynchronous operations (like Bluetooth callbacks).
*   **Permissions**: Ensure your `androidTest` manifest includes necessary permissions for Bluetooth.
*   **Test Data**: Use consistent and representative test data.

This testing guide provides a structured approach to verifying the scale integration. Implementing these tests will significantly improve the reliability and maintainability of your OrtusPOS application.
