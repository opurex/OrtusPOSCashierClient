package com.example.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 6, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0003¢\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J;\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÖ\u0001J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b¨\u0006\u001c"},
   d2 = {"Lcom/example/util/Packet;", "", "frameType", "", "randomNum", "totalPackets", "", "currentPacket", "decryptedData", "(Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;)V", "getCurrentPacket", "()I", "getDecryptedData", "()Ljava/lang/String;", "getFrameType", "getRandomNum", "getTotalPackets", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "ndklib_release"}
)
public final class Packet {
   private final int h;
   @NotNull
   private final String i;
   @NotNull
   private final String f;
   private final int d;
   @NotNull
   private final String ALLATORIxDEMO;

   @NotNull
   public final String getRandomNum() {
      return this.f;
   }

   public final int component3() {
      return this.d;
   }

   @NotNull
   public final String getFrameType() {
      return this.ALLATORIxDEMO;
   }

   public final int component4() {
      return this.h;
   }

   public Packet(@NotNull String arg0, @NotNull String arg1, int arg2, int arg3, @NotNull String arg4) {
      Intrinsics.checkNotNullParameter(arg0, FutureThreadPool.ALLATORIxDEMO("xh\u007fw{Ngj{"));
      Intrinsics.checkNotNullParameter(arg1, PacketHandler.ALLATORIxDEMO("HZT_UVtNW"));
      Intrinsics.checkNotNullParameter(arg4, FutureThreadPool.ALLATORIxDEMO("z\u007f}hgjj\u007fz^\u007fn\u007f"));
      super();
      this.ALLATORIxDEMO = arg0;
      this.f = arg1;
      this.d = arg2;
      this.h = arg3;
      this.i = arg4;
   }

   @NotNull
   public final String component5() {
      return this.i;
   }

   public static String ALLATORIxDEMO(String arg0) {
      int var10000 = (2 ^ 5) << 4 ^ 5;
      int var10001 = (3 ^ 5) << 3 ^ 2;
      int var10002 = 2 << 3 ^ 3 ^ 5;
      int var10003 = arg0.length();
      char[] var10004 = new char[var10003];
      boolean var10006 = true;
      int var5 = var10003 - 1;
      var10003 = var10002;
      int var3;
      var10002 = var3 = var5;
      char[] var1 = var10004;
      int var4 = var10003;
      var10001 = var10000;
      var10000 = var10002;

      for(int var2 = var10001; var10000 >= 0; var10000 = var3) {
         var10001 = var3;
         char var6 = arg0.charAt(var3);
         --var3;
         var1[var10001] = (char)(var6 ^ var2);
         if (var3 < 0) {
            break;
         }

         var10002 = var3--;
         var1[var10002] = (char)(arg0.charAt(var10002) ^ var4);
      }

      return new String(var1);
   }

   @NotNull
   public final String component1() {
      return this.ALLATORIxDEMO;
   }

   // $FF: synthetic method
   public static Packet copy$default(Packet arg0, String arg1, String arg2, int arg3, int arg4, String arg5, int arg6, Object arg7) {
      if ((arg6 & 1) != 0) {
         arg1 = arg0.ALLATORIxDEMO;
      }

      if ((arg6 & 2) != 0) {
         arg2 = arg0.f;
      }

      if ((arg6 & 4) != 0) {
         arg3 = arg0.d;
      }

      if ((arg6 & 8) != 0) {
         arg4 = arg0.h;
      }

      if ((arg6 & 16) != 0) {
         arg5 = arg0.i;
      }

      return arg0.copy(arg1, arg2, arg3, arg4, arg5);
   }

   @NotNull
   public final String component2() {
      return this.f;
   }

   @NotNull
   public final String getDecryptedData() {
      return this.i;
   }

   @NotNull
   public final Packet copy(@NotNull String arg0, @NotNull String arg1, int arg2, int arg3, @NotNull String arg4) {
      Intrinsics.checkNotNullParameter(arg0, PacketHandler.ALLATORIxDEMO("\\I[V_oCK_"));
      Intrinsics.checkNotNullParameter(arg1, FutureThreadPool.ALLATORIxDEMO("l{p~qwPos"));
      Intrinsics.checkNotNullParameter(arg4, PacketHandler.ALLATORIxDEMO("^^YICKN^^\u007f[O["));
      return new Packet(arg0, arg1, arg2, arg3, arg4);
   }

   public final int getTotalPackets() {
      return this.d;
   }

   public boolean equals(@Nullable Object arg0) {
      if (this == arg0) {
         return true;
      } else if (!(arg0 instanceof Packet)) {
         return false;
      } else {
         Packet var2 = (Packet)arg0;
         if (!Intrinsics.areEqual(this.ALLATORIxDEMO, var2.ALLATORIxDEMO)) {
            return false;
         } else if (!Intrinsics.areEqual(this.f, var2.f)) {
            return false;
         } else if (this.d != var2.d) {
            return false;
         } else if (this.h != var2.h) {
            return false;
         } else {
            return Intrinsics.areEqual(this.i, var2.i);
         }
      }
   }

   @NotNull
   public String toString() {
      return (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("N{}q{n6|l{s\u007fJcn\u007f#")).append(this.ALLATORIxDEMO).append(PacketHandler.ALLATORIxDEMO("\u0017\u001aI[U^TWuOV\u0007")).append(this.f).append(FutureThreadPool.ALLATORIxDEMO("2:juj{rJ\u007fyu\u007fji#")).append(this.d).append(PacketHandler.ALLATORIxDEMO("\u0017\u001aXOIH^TOjZYP_O\u0007")).append(this.h).append(FutureThreadPool.ALLATORIxDEMO("6>~{ylcnn{~Z{j{#")).append(this.i).append(')').toString();
   }

   public int hashCode() {
      return (((this.ALLATORIxDEMO.hashCode() * 31 + this.f.hashCode()) * 31 + Integer.hashCode(this.d)) * 31 + Integer.hashCode(this.h)) * 31 + this.i.hashCode();
   }

   public final int getCurrentPacket() {
      return this.h;
   }
}
