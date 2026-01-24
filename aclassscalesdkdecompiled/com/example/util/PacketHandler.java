package com.example.util;

import com.aclas.ndklib.BuildConfig;
import com.example.io.BluetoothCtrl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 6, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005J\u0006\u0010\r\u001a\u00020\u000bJ\u0006\u0010\u000e\u001a\u00020\u0005J\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0005H\u0002R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"},
   d2 = {"Lcom/example/util/PacketHandler;", "", "()V", "packetMap", "", "", "", "Lcom/example/util/Packet;", "seenPackets", "", "addPacket", "", "packetString", "clear", "getCombinedData", "parsePacket", "ndklib_release"}
)
public final class PacketHandler {
   @NotNull
   private final Map d = (Map)(new LinkedHashMap());
   @NotNull
   private final Set ALLATORIxDEMO = (Set)(new LinkedHashSet());

   // $FF: synthetic method
   private final Packet ALLATORIxDEMO(String arg0) {
      String var10003 = arg0.substring(0, 2);
      Intrinsics.checkNotNullExpressionValue(var10003, BluetoothCtrl.ALLATORIxDEMO("-l0wye*$3e/ewh8j>*\np+m7cⁿm7cqw-e+p\u0010j=a!(ya7`\u0010j=a!-"));
      String var2 = var10003;
      String var10002 = arg0.substring(2, 4);
      Intrinsics.checkNotNullExpressionValue(var10002, BuildConfig.ALLATORIxDEMO("b.\u007f56'ef|'`'8*w(qhE2d/x!‰/x!>5b'd2_(r#nj6#x\"_(r#no"));
      String var3 = var10002;
      String var10001 = arg0.substring(4, 6);
      Intrinsics.checkNotNullExpressionValue(var10001, BluetoothCtrl.ALLATORIxDEMO("-l0wye*$3e/ewh8j>*\np+m7cⁿm7cqw-e+p\u0010j=a!(ya7`\u0010j=a!-"));
      int var4;
      int var5 = (var4 = Integer.parseInt(var10001, CharsKt.checkRadix(16))) >> 4;
      int var6 = var4 & 15;
      String var10000 = arg0.substring(6);
      Intrinsics.checkNotNullExpressionValue(var10000, BuildConfig.ALLATORIxDEMO("2~/efw56,w0whz'x!8\u0015b4\u007f(qo85c$e2d/x!>5b'd2_(r#no"));
      String var7 = var10000;
      return new Packet(var2, var3, var5, var6, var7);
   }

   @NotNull
   public final String getCombinedData() {
      StringBuilder var1 = new StringBuilder();
      Iterable var2 = (Iterable)this.d.values();
      boolean var3 = false;

      Iterator var4;
      for(Iterator var10000 = var4 = var2.iterator(); var10000.hasNext(); var10000 = var4) {
         List var6 = (List)var4.next();
         boolean var7 = false;
         if (var6.size() == ((Packet)var6.get(0)).getTotalPackets()) {
            Iterable var8 = (Iterable)var6;
            boolean var9 = false;
            var8 = (Iterable)CollectionsKt.sortedWith(var8, (Comparator)(new PacketHandler$getCombinedData$lambda-2$$inlined$sortedBy$1()));
            var9 = false;
            Iterator var11;
            var10000 = var11 = var8.iterator();

            while(var10000.hasNext()) {
               Packet var13 = (Packet)var11.next();
               boolean var14 = false;
               var10000 = var11;
               var1.append(var13.getDecryptedData());
            }
         }
      }

      String var15 = var1.toString();
      Intrinsics.checkNotNullExpressionValue(var15, BluetoothCtrl.ALLATORIxDEMO("g6i;m7a=@8p8*-k\np+m7cq-"));
      return var15;
   }

   public static String ALLATORIxDEMO(String arg0) {
      int var10000 = (2 ^ 5) << 3 ^ 2;
      int var10001 = 2 ^ 5;
      int var10002 = (2 ^ 5) << 3 ^ 3;
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

   public final void clear() {
      this.d.clear();
      this.ALLATORIxDEMO.clear();
   }

   public final void addPacket(@NotNull String arg0) {
      Intrinsics.checkNotNullParameter(arg0, BuildConfig.ALLATORIxDEMO("f'u-s2E2d/x!"));
      if (!this.ALLATORIxDEMO.contains(arg0)) {
         this.ALLATORIxDEMO.add(arg0);
         Packet var2;
         String var3 = (var2 = this.ALLATORIxDEMO(arg0)).getRandomNum();
         this.d.putIfAbsent(var3, (List)(new ArrayList()));
         Object var10000 = this.d.get(var3);
         Intrinsics.checkNotNull(var10000);
         ((List)var10000).add(var2);
      }

   }
}
