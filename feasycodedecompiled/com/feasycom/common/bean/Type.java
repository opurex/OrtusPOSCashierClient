package com.feasycom.common.bean;

import kotlin.Metadata;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0013\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013¨\u0006\u0014"},
   d2 = {"Lcom/feasycom/common/bean/Type;", "", "(Ljava/lang/String;I)V", "SET", "CONNECT", "DISCONNECT", "WRITE", "READ", "ENABLE_NOTIFICATIONS", "DISABLE_NOTIFICATIONS", "REQUEST_MTU", "SET_PREFERRED_PHY", "READ_PHY", "ENCRYPT", "OTA", "VERIFY_OTA", "AT", "RECEIVE", "FACP", "FACP_SHAKE", "FeasyBlueLibrary_release"}
)
public enum Type {
   SET,
   CONNECT,
   DISCONNECT,
   WRITE,
   READ,
   ENABLE_NOTIFICATIONS,
   DISABLE_NOTIFICATIONS,
   REQUEST_MTU,
   SET_PREFERRED_PHY,
   READ_PHY,
   ENCRYPT,
   OTA,
   VERIFY_OTA,
   AT,
   RECEIVE,
   FACP,
   FACP_SHAKE;

   // $FF: synthetic method
   private static final Type[] $values() {
      return new Type[]{SET, CONNECT, DISCONNECT, WRITE, READ, ENABLE_NOTIFICATIONS, DISABLE_NOTIFICATIONS, REQUEST_MTU, SET_PREFERRED_PHY, READ_PHY, ENCRYPT, OTA, VERIFY_OTA, AT, RECEIVE, FACP, FACP_SHAKE};
   }
}
