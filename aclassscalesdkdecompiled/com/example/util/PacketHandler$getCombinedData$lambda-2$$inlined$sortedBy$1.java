package com.example.util;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;

@Metadata(
   mv = {1, 6, 0},
   k = 3,
   xi = 48,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"},
   d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"}
)
public final class PacketHandler$getCombinedData$lambda-2$$inlined$sortedBy$1 implements Comparator {
   public final int compare(Object arg0, Object arg1) {
      Packet var3 = (Packet)arg0;
      boolean var4 = false;
      Comparable var10000 = (Comparable)var3.getCurrentPacket();
      var3 = (Packet)arg1;
      Comparable var5 = var10000;
      var4 = false;
      return ComparisonsKt.compareValues(var5, (Comparable)var3.getCurrentPacket());
   }
}
