package com.feasycom.network.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b'\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u00109\u001a\u00020\nH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0015\"\u0004\b\u001a\u0010\u0017R\u001c\u0010\u001b\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\f\"\u0004\b\u001d\u0010\u000eR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\f\"\u0004\b \u0010\u000eR\u001c\u0010!\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\f\"\u0004\b#\u0010\u000eR\u001e\u0010$\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0006\"\u0004\b&\u0010\bR\u001c\u0010'\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\f\"\u0004\b)\u0010\u000eR\u001c\u0010*\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\f\"\u0004\b,\u0010\u000eR\u001a\u0010-\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0006\"\u0004\b/\u0010\bR\u001c\u00100\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\f\"\u0004\b2\u0010\u000eR\u001a\u00103\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0015\"\u0004\b5\u0010\u0017R\u001c\u00106\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\f\"\u0004\b8\u0010\u000e¨\u0006:"},
   d2 = {"Lcom/feasycom/network/bean/DeviceInfo;", "", "()V", "createTime", "", "getCreateTime", "()J", "setCreateTime", "(J)V", "defaultInterval", "", "getDefaultInterval", "()Ljava/lang/String;", "setDefaultInterval", "(Ljava/lang/String;)V", "deviceId", "getDeviceId", "setDeviceId", "deviceType", "", "getDeviceType", "()I", "setDeviceType", "(I)V", "funcType", "getFuncType", "setFuncType", "gsensorDuration", "getGsensorDuration", "setGsensorDuration", "gsensorInterval", "getGsensorInterval", "setGsensorInterval", "gsensorSensitivity", "getGsensorSensitivity", "setGsensorSensitivity", "id", "getId", "setId", "keyDuration", "getKeyDuration", "setKeyDuration", "keyInterval", "getKeyInterval", "setKeyInterval", "modifyTime", "getModifyTime", "setModifyTime", "name", "getName", "setName", "number", "getNumber", "setNumber", "txPower", "getTxPower", "setTxPower", "toString", "FeasyBlueLibrary_release"}
)
@Entity
public final class DeviceInfo {
   @PrimaryKey(
      autoGenerate = true
   )
   private long id;
   private long createTime;
   @Nullable
   private String defaultInterval;
   @Nullable
   private String deviceId;
   private int deviceType;
   private int funcType;
   @Nullable
   private String gsensorDuration;
   @Nullable
   private String gsensorInterval;
   @Nullable
   private String gsensorSensitivity;
   @Nullable
   private String keyDuration;
   @Nullable
   private String keyInterval;
   private long modifyTime;
   @Nullable
   private String name;
   private int number;
   @Nullable
   private String txPower;

   public final long getId() {
      return this.id;
   }

   public final void setId(long var1) {
      this.id = var1;
   }

   public final long getCreateTime() {
      return this.createTime;
   }

   public final void setCreateTime(long var1) {
      this.createTime = var1;
   }

   @Nullable
   public final String getDefaultInterval() {
      return this.defaultInterval;
   }

   public final void setDefaultInterval(@Nullable String var1) {
      this.defaultInterval = var1;
   }

   @Nullable
   public final String getDeviceId() {
      return this.deviceId;
   }

   public final void setDeviceId(@Nullable String var1) {
      this.deviceId = var1;
   }

   public final int getDeviceType() {
      return this.deviceType;
   }

   public final void setDeviceType(int var1) {
      this.deviceType = var1;
   }

   public final int getFuncType() {
      return this.funcType;
   }

   public final void setFuncType(int var1) {
      this.funcType = var1;
   }

   @Nullable
   public final String getGsensorDuration() {
      return this.gsensorDuration;
   }

   public final void setGsensorDuration(@Nullable String var1) {
      this.gsensorDuration = var1;
   }

   @Nullable
   public final String getGsensorInterval() {
      return this.gsensorInterval;
   }

   public final void setGsensorInterval(@Nullable String var1) {
      this.gsensorInterval = var1;
   }

   @Nullable
   public final String getGsensorSensitivity() {
      return this.gsensorSensitivity;
   }

   public final void setGsensorSensitivity(@Nullable String var1) {
      this.gsensorSensitivity = var1;
   }

   @Nullable
   public final String getKeyDuration() {
      return this.keyDuration;
   }

   public final void setKeyDuration(@Nullable String var1) {
      this.keyDuration = var1;
   }

   @Nullable
   public final String getKeyInterval() {
      return this.keyInterval;
   }

   public final void setKeyInterval(@Nullable String var1) {
      this.keyInterval = var1;
   }

   public final long getModifyTime() {
      return this.modifyTime;
   }

   public final void setModifyTime(long var1) {
      this.modifyTime = var1;
   }

   @Nullable
   public final String getName() {
      return this.name;
   }

   public final void setName(@Nullable String var1) {
      this.name = var1;
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int var1) {
      this.number = var1;
   }

   @Nullable
   public final String getTxPower() {
      return this.txPower;
   }

   public final void setTxPower(@Nullable String var1) {
      this.txPower = var1;
   }

   @NotNull
   public String toString() {
      StringBuilder var10000 = new StringBuilder();
      var10000.append("DeviceInfo(id=").append(this.id).append(", createTime=").append(this.createTime).append(", defaultInterval=").append(this.defaultInterval).append(", deviceId=").append(this.deviceId).append(", deviceType=").append(this.deviceType).append(", funcType=").append(this.funcType).append(", gsensorDuration=").append(this.gsensorDuration).append(", gsensorInterval=").append(this.gsensorInterval).append(", gsensorSensitivity=").append(this.gsensorSensitivity).append(", keyDuration=").append(this.keyDuration).append(", keyInterval=").append(this.keyInterval).append(", modifyTime=");
      var10000.append(this.modifyTime).append(", name=").append(this.name).append(", number=").append(this.number).append(", txPower=").append(this.txPower).append(')');
      return var10000.toString();
   }
}
