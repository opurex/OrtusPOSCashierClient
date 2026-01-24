package com.feasycom.common.bean;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class FscDevice implements Parcelable {
   public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
      public FscDevice a(Parcel var1) {
         return new FscDevice(var1);
      }

      public FscDevice[] a(int var1) {
         return new FscDevice[var1];
      }
   };
   public String name;
   public String address;
   public BluetoothDevice device;
   public int rssi;
   public String mode;
   public byte[] scanRecord;
   public String bindFrames;
   public String firmwareVersion;
   public String flag;
   public String incompleteServiceUUIDs_16bit;
   public String incompleteServiceUUIDs_128bit;
   public String completeLocalName;
   public String serviceData;
   public String txPowerLevel;
   public String manufacturerSpecificData;
   public String userData;

   public FscDevice(String var1, String var2, BluetoothDevice var3, int var4, String var5) {
      this.name = var1;
      this.address = var2;
      this.device = var3;
      this.rssi = var4;
      this.mode = var5;
   }

   public FscDevice(String var1, String var2, BluetoothDevice var3, int var4, String var5, String var6, String var7) {
      this.name = var1;
      this.address = var2;
      this.device = var3;
      this.rssi = var4;
      this.mode = var5;
      this.bindFrames = var6;
      this.firmwareVersion = var7;
   }

   public FscDevice(Parcel var1) {
      this.name = var1.readString();
      this.address = var1.readString();
      this.device = (BluetoothDevice)var1.readParcelable(BluetoothDevice.class.getClassLoader());
      this.rssi = var1.readInt();
      this.mode = var1.readString();
      this.scanRecord = var1.createByteArray();
      this.flag = var1.readString();
      this.incompleteServiceUUIDs_16bit = var1.readString();
      this.incompleteServiceUUIDs_128bit = var1.readString();
      this.completeLocalName = var1.readString();
      this.serviceData = var1.readString();
      this.txPowerLevel = var1.readString();
      this.manufacturerSpecificData = var1.readString();
      this.userData = var1.readString();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getAddress() {
      return this.address;
   }

   public void setAddress(String var1) {
      this.address = var1;
   }

   public BluetoothDevice getDevice() {
      return this.device;
   }

   public void setDevice(BluetoothDevice var1) {
      this.device = var1;
   }

   public int getRssi() {
      return this.rssi;
   }

   public void setRssi(int var1) {
      this.rssi = var1;
   }

   public String getMode() {
      return this.mode;
   }

   public void setMode(String var1) {
      this.mode = var1;
   }

   public String getBindFrames() {
      return this.bindFrames;
   }

   public void setBindFrames(String var1) {
      this.bindFrames = var1;
   }

   public String getFirmwareVersion() {
      return this.firmwareVersion;
   }

   public void setFirmwareVersion(String var1) {
      this.firmwareVersion = var1;
   }

   public byte[] getScanRecord() {
      return this.scanRecord;
   }

   public void setScanRecord(byte[] var1) {
      this.scanRecord = var1;
   }

   public String getFlag() {
      return this.flag;
   }

   public void setFlag(String var1) {
      this.flag = var1;
   }

   public String getIncompleteServiceUUIDs_16bit() {
      return this.incompleteServiceUUIDs_16bit;
   }

   public void setIncompleteServiceUUIDs_16bit(String var1) {
      this.incompleteServiceUUIDs_16bit = var1;
   }

   public String getIncompleteServiceUUIDs_128bit() {
      return this.incompleteServiceUUIDs_128bit;
   }

   public void setIncompleteServiceUUIDs_128bit(String var1) {
      this.incompleteServiceUUIDs_128bit = var1;
   }

   public String getCompleteLocalName() {
      return this.completeLocalName;
   }

   public void setCompleteLocalName(String var1) {
      this.completeLocalName = var1;
   }

   public String getServiceData() {
      return this.serviceData;
   }

   public void setServiceData(String var1) {
      this.serviceData = var1;
   }

   public String getTxPowerLevel() {
      return this.txPowerLevel;
   }

   public void setTxPowerLevel(String var1) {
      this.txPowerLevel = var1;
   }

   public String getManufacturerSpecificData() {
      return this.manufacturerSpecificData;
   }

   public void setManufacturerSpecificData(String var1) {
      this.manufacturerSpecificData = var1;
   }

   public String getUserData() {
      return this.userData;
   }

   public void setUserData(String var1) {
      this.userData = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof FscDevice)) {
         return false;
      } else {
         FscDevice var10000 = this;
         FscDevice var2 = (FscDevice)var1;
         return var10000.address.equals(var2.address);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.address});
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.name);
      var1.writeString(this.address);
      var1.writeParcelable(this.device, var2);
      var1.writeInt(this.rssi);
      var1.writeString(this.mode);
      var1.writeByteArray(this.scanRecord);
      var1.writeString(this.flag);
      var1.writeString(this.incompleteServiceUUIDs_16bit);
      var1.writeString(this.incompleteServiceUUIDs_128bit);
      var1.writeString(this.completeLocalName);
      var1.writeString(this.serviceData);
      var1.writeString(this.txPowerLevel);
      var1.writeString(this.manufacturerSpecificData);
      var1.writeString(this.userData);
   }
}
