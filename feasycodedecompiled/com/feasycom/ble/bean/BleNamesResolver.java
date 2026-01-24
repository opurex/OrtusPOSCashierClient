package com.feasycom.ble.bean;

import android.util.SparseArray;
import androidx.annotation.Keep;
import java.util.HashMap;

@Keep
public class BleNamesResolver {
   private static final HashMap mServices;
   private static final HashMap mCharacteristics;
   private static final SparseArray mValueFormats;
   private static final SparseArray mAppearance;
   private static final SparseArray mHeartRateSensorLocation;

   public static String resolveServiceName(String var0) {
      if ((var0 = (String)mServices.get(var0)) == null) {
         var0 = "Unknown";
      }

      return var0;
   }

   public static String resolveValueTypeDescription(int var0) {
      return (String)mValueFormats.get(Integer.valueOf(var0), "Unknown Format");
   }

   public static String resolveCharacteristicName(String var0) {
      if ((var0 = (String)mCharacteristics.get(var0)) == null) {
         var0 = "Unknown";
      }

      return var0;
   }

   public static String resolveUuid(String var0) {
      String var1;
      if ((var1 = (String)mServices.get(var0)) != null) {
         return "Service: " + var1;
      } else {
         return (var0 = (String)mCharacteristics.get(var0)) != null ? "Characteristic: " + var0 : "Unknown UUID";
      }
   }

   public static String resolveAppearance(int var0) {
      return (String)mAppearance.get(Integer.valueOf(var0), "Unknown Appearance");
   }

   public static String resolveHeartRateSensorLocation(int var0) {
      return (String)mHeartRateSensorLocation.get(Integer.valueOf(var0), "Other");
   }

   public static boolean isService(String var0) {
      return mServices.containsKey(var0);
   }

   public static boolean isCharacteristic(String var0) {
      return mCharacteristics.containsKey(var0);
   }

   static {
      HashMap var0;
      HashMap var10000 = var0 = new HashMap;
      var10000.<init>();
      mServices = var10000;
      HashMap var1;
      var10000 = var1 = new HashMap;
      var10000.<init>();
      mCharacteristics = var10000;
      SparseArray var2;
      SparseArray var5 = var2 = new SparseArray;
      var5.<init>();
      mValueFormats = var5;
      SparseArray var3;
      var5 = var3 = new SparseArray;
      var5.<init>();
      mAppearance = var5;
      SparseArray var4;
      var5 = var4 = new SparseArray;
      var4.<init>();
      mHeartRateSensorLocation = var4;
      var0.put("00001811-0000-1000-8000-00805f9b34fb", "Alert Notification Service");
      var0.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
      var0.put("00001810-0000-1000-8000-00805f9b34fb", "Blood Pressure");
      var0.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
      var0.put("00001818-0000-1000-8000-00805f9b34fb", "Cycling Power");
      var0.put("00001816-0000-1000-8000-00805f9b34fb", "Cycling Speed and Cadence");
      var0.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information");
      var0.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");
      var0.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
      var0.put("00001808-0000-1000-8000-00805f9b34fb", "Glucose");
      var0.put("00001809-0000-1000-8000-00805f9b34fb", "Health Thermometer");
      var0.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate");
      var0.put("00001812-0000-1000-8000-00805f9b34fb", "Human Interface Device");
      var0.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert");
      var0.put("00001803-0000-1000-8000-00805f9b34fb", "Link Loss");
      var0.put("00001819-0000-1000-8000-00805f9b34fb", "Location and Navigation");
      var0.put("00001807-0000-1000-8000-00805f9b34fb", "Next DST Change Service");
      var0.put("0000180e-0000-1000-8000-00805f9b34fb", "Phone Alert Status Service");
      var0.put("00001806-0000-1000-8000-00805f9b34fb", "Reference Time Update Service");
      var0.put("00001814-0000-1000-8000-00805f9b34fb", "Running Speed and Cadence");
      var0.put("00001813-0000-1000-8000-00805f9b34fb", "Scan Parameters");
      var0.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");
      var1.put("00002a43-0000-1000-8000-00805f9b34fb", "Alert Category ID");
      var1.put("00002a42-0000-1000-8000-00805f9b34fb", "Alert Category ID Bit Mask");
      var1.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level");
      var1.put("00002a44-0000-1000-8000-00805f9b34fb", "Alert Notification Control Point");
      var1.put("00002a3f-0000-1000-8000-00805f9b34fb", "Alert Status");
      var1.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
      var1.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");
      var1.put("00002a49-0000-1000-8000-00805f9b34fb", "Blood Pressure Feature");
      var1.put("00002a35-0000-1000-8000-00805f9b34fb", "Blood Pressure Measurement");
      var1.put("00002a38-0000-1000-8000-00805f9b34fb", "Body Sensor Location");
      var1.put("00002a22-0000-1000-8000-00805f9b34fb", "Boot Keyboard Input Report");
      var1.put("00002a32-0000-1000-8000-00805f9b34fb", "Boot Keyboard Output Report");
      var1.put("00002a33-0000-1000-8000-00805f9b34fb", "Boot Mouse Input Report");
      var1.put("00002a5c-0000-1000-8000-00805f9b34fb", "CSC Feature");
      var1.put("00002a5b-0000-1000-8000-00805f9b34fb", "CSC Measurement");
      var1.put("00002a2b-0000-1000-8000-00805f9b34fb", "Current Time");
      var1.put("00002a66-0000-1000-8000-00805f9b34fb", "Cycling Power Control Point");
      var1.put("00002a65-0000-1000-8000-00805f9b34fb", "Cycling Power Feature");
      var1.put("00002a63-0000-1000-8000-00805f9b34fb", "Cycling Power Measurement");
      var1.put("00002a64-0000-1000-8000-00805f9b34fb", "Cycling Power Vector");
      var1.put("00002a08-0000-1000-8000-00805f9b34fb", "Date Time");
      var1.put("00002a0a-0000-1000-8000-00805f9b34fb", "Day Date Time");
      var1.put("00002a09-0000-1000-8000-00805f9b34fb", "Day of Week");
      var1.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
      var1.put("00002a0d-0000-1000-8000-00805f9b34fb", "DST Offset");
      var1.put("00002a0c-0000-1000-8000-00805f9b34fb", "Exact Time 256");
      var1.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision String");
      var1.put("00002a51-0000-1000-8000-00805f9b34fb", "Glucose Feature");
      var1.put("00002a18-0000-1000-8000-00805f9b34fb", "Glucose Measurement");
      var1.put("00002a34-0000-1000-8000-00805f9b34fb", "Glucose Measurement Context");
      var1.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision String");
      var1.put("00002a39-0000-1000-8000-00805f9b34fb", "Heart Rate Control Point");
      var1.put("00002a37-0000-1000-8000-00805f9b34fb", "Heart Rate Measurement");
      var1.put("00002a4c-0000-1000-8000-00805f9b34fb", "HID Control Point");
      var1.put("00002a4a-0000-1000-8000-00805f9b34fb", "HID Information");
      var1.put("00002a2a-0000-1000-8000-00805f9b34fb", "IEEE 11073-20601 Regulatory Certification Data List");
      var1.put("00002a36-0000-1000-8000-00805f9b34fb", "Intermediate Cuff Pressure");
      var1.put("00002a1e-0000-1000-8000-00805f9b34fb", "Intermediate Temperature");
      var1.put("00002a6b-0000-1000-8000-00805f9b34fb", "LN Control Point");
      var1.put("00002a6a-0000-1000-8000-00805f9b34fb", "LN Feature");
      var1.put("00002a0f-0000-1000-8000-00805f9b34fb", "Local Time Information");
      var1.put("00002a67-0000-1000-8000-00805f9b34fb", "Location and Speed");
      var1.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
      var1.put("00002a21-0000-1000-8000-00805f9b34fb", "Measurement Interval");
      var1.put("00002a24-0000-1000-8000-00805f9b34fb", "Model Number String");
      var1.put("00002a68-0000-1000-8000-00805f9b34fb", "Navigation");
      var1.put("00002a46-0000-1000-8000-00805f9b34fb", "New Alert");
      var1.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters");
      var1.put("00002a02-0000-1000-8000-00805f9b34fb", "Peripheral Privacy Flag");
      var1.put("00002a50-0000-1000-8000-00805f9b34fb", "PnP ID");
      var1.put("00002a69-0000-1000-8000-00805f9b34fb", "Position Quality");
      var1.put("00002a4e-0000-1000-8000-00805f9b34fb", "Protocol Mode");
      var1.put("00002a03-0000-1000-8000-00805f9b34fb", "Reconnection Address");
      var1.put("00002a52-0000-1000-8000-00805f9b34fb", "Record Access Control Point");
      var1.put("00002a14-0000-1000-8000-00805f9b34fb", "Reference Time Information");
      var1.put("00002a4d-0000-1000-8000-00805f9b34fb", "Report");
      var1.put("00002a4b-0000-1000-8000-00805f9b34fb", "Report Map");
      var1.put("00002a40-0000-1000-8000-00805f9b34fb", "Ringer Control Point");
      var1.put("00002a41-0000-1000-8000-00805f9b34fb", "Ringer Setting");
      var1.put("00002a54-0000-1000-8000-00805f9b34fb", "RSC Feature");
      var1.put("00002a53-0000-1000-8000-00805f9b34fb", "RSC Measurement");
      var1.put("00002a55-0000-1000-8000-00805f9b34fb", "SC Control Point");
      var1.put("00002a4f-0000-1000-8000-00805f9b34fb", "Scan Interval Window");
      var1.put("00002a31-0000-1000-8000-00805f9b34fb", "Scan Refresh");
      var1.put("00002a5d-0000-1000-8000-00805f9b34fb", "Sensor Location");
      var1.put("00002a25-0000-1000-8000-00805f9b34fb", "Serial Number String");
      var1.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
      var1.put("00002a28-0000-1000-8000-00805f9b34fb", "Software Revision String");
      var1.put("00002a47-0000-1000-8000-00805f9b34fb", "Supported New Alert Category");
      var1.put("00002a48-0000-1000-8000-00805f9b34fb", "Supported Unread Alert Category");
      var1.put("00002a23-0000-1000-8000-00805f9b34fb", "System ID");
      var1.put("00002a1c-0000-1000-8000-00805f9b34fb", "Temperature Measurement");
      var1.put("00002a1d-0000-1000-8000-00805f9b34fb", "Temperature Type");
      var1.put("00002a12-0000-1000-8000-00805f9b34fb", "Time Accuracy");
      var1.put("00002a13-0000-1000-8000-00805f9b34fb", "Time Source");
      var1.put("00002a16-0000-1000-8000-00805f9b34fb", "Time Update Control Point");
      var1.put("00002a17-0000-1000-8000-00805f9b34fb", "Time Update State");
      var1.put("00002a11-0000-1000-8000-00805f9b34fb", "Time with DST");
      var1.put("00002a0e-0000-1000-8000-00805f9b34fb", "Time Zone");
      var1.put("00002a07-0000-1000-8000-00805f9b34fb", "Tx Power Level");
      var1.put("00002a45-0000-1000-8000-00805f9b34fb", "Unread Alert Status");
      var2.put(Integer.valueOf(52), "32bit float");
      var2.put(Integer.valueOf(50), "16bit float");
      var2.put(Integer.valueOf(34), "16bit signed int");
      var2.put(Integer.valueOf(36), "32bit signed int");
      var2.put(Integer.valueOf(33), "8bit signed int");
      var2.put(Integer.valueOf(18), "16bit unsigned int");
      var2.put(Integer.valueOf(20), "32bit unsigned int");
      var2.put(Integer.valueOf(17), "8bit unsigned int");
      var3.put(Integer.valueOf(833), "Heart Rate Sensor: Belt");
      var3.put(Integer.valueOf(832), "Generic Heart Rate Sensor");
      var3.put(Integer.valueOf(0), "Unknown");
      var3.put(Integer.valueOf(64), "Generic Phone");
      var3.put(Integer.valueOf(1157), "Cycling: Speed and Cadence Sensor");
      var3.put(Integer.valueOf(1152), "General Cycling");
      var3.put(Integer.valueOf(1153), "Cycling Computer");
      var3.put(Integer.valueOf(1154), "Cycling: Speed Sensor");
      var3.put(Integer.valueOf(1155), "Cycling: Cadence Sensor");
      var3.put(Integer.valueOf(1156), "Cycling: Speed and Cadence Sensor");
      var3.put(Integer.valueOf(1157), "Cycling: Power Sensor");
      var5.put(Integer.valueOf(0), "Other");
      var5.put(Integer.valueOf(1), "Chest");
      var5.put(Integer.valueOf(2), "Wrist");
      var5.put(Integer.valueOf(3), "Finger");
      var5.put(Integer.valueOf(4), "Hand");
      var5.put(Integer.valueOf(5), "Ear Lobe");
      var5.put(Integer.valueOf(6), "Foot");
   }
}
