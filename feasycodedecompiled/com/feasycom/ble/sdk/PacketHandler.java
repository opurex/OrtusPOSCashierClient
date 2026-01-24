package com.feasycom.ble.sdk;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketHandler {
   private static final String TAG = "PacketHandler";
   private final Map packetMap = new ConcurrentHashMap();
   private final Set seenPackets = Collections.newSetFromMap(new ConcurrentHashMap());

   private Packet parsePacket(String var1) {
      if (var1 != null && var1.length() >= 6) {
         Exception var10000;
         label61: {
            String var14;
            String var15;
            boolean var10001;
            String var10002;
            String var10003;
            try {
               var14 = var1;
               var15 = var1;
               var10002 = var1;
               var10003 = var1.substring(0, 2);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label61;
            }

            String var12 = var10003;

            try {
               var10002 = var10002.substring(2, 4);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label61;
            }

            String var2 = var10002;

            int var16;
            try {
               var16 = Integer.parseInt(var15.substring(4, 6), 16);
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label61;
            }

            int var3 = var16 >> 4;
            int var4 = var16 & 15;
            var15 = "PacketHandler";

            try {
               Log.e(var15, "parsePacket totalPackets: " + var3 + ", currentPacket: " + var4);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label61;
            }

            try {
               var14 = var14.substring(6);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label61;
            }

            String var5 = var14;

            try {
               return new Packet(var12, var2, var3, var4, var5);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var13 = var10000;
         Log.e("PacketHandler", "parsePacket Error parsing packet: " + var1, var13);
         return null;
      } else {
         Log.e("PacketHandler", "parsePacket Invalid packet string: " + var1);
         return null;
      }
   }

   public String addPacket(String var1) {
      Log.e("PacketHandler", "addPacket packetString111: " + var1);
      if (!this.seenPackets.contains(var1)) {
         this.seenPackets.add(var1);
         Log.e("PacketHandler", "addPacket packetString222: " + var1);
         Packet var2;
         if ((var2 = this.parsePacket(var1)) != null) {
            var1 = var2.getRandomNum();
            Map var10002 = this.packetMap;
            ArrayList var3;
            var3 = new ArrayList.<init>();
            var10002.putIfAbsent(var1, var3);
            ((List)this.packetMap.get(var1)).add(var2);
            if (((List)this.packetMap.get(var1)).size() == var2.getTotalPackets() && (var1 = this.getCombinedData(var1)) != null) {
               this.clear();
               return var1;
            }
         } else {
            Log.e("PacketHandler", "addPacket Failed to parse packet: " + var1);
         }
      } else {
         Log.e("PacketHandler", "addPacket packetString333: " + var1);
      }

      return "";
   }

   public String getCombinedData(String var1) {
      PacketHandler var10000 = this;
      StringBuilder var2;
      var2 = new StringBuilder.<init>();
      List var3;
      if ((var3 = (List)var10000.packetMap.get(var1)) != null && !var3.isEmpty()) {
         var3.sort(Comparator.comparingInt(Packet::getCurrentPacket));
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            var2.append(((Packet)var4.next()).getDecryptedData());
         }

         return var2.toString();
      } else {
         return null;
      }
   }

   public void clear() {
      this.packetMap.clear();
      this.seenPackets.clear();
   }

   public static class Packet {
      private final String frameType;
      private final String randomNum;
      private final int totalPackets;
      private final int currentPacket;
      private final String decryptedData;

      public Packet(String var1, String var2, int var3, int var4, String var5) {
         this.frameType = var1;
         this.randomNum = var2;
         this.totalPackets = var3;
         this.currentPacket = var4;
         this.decryptedData = var5;
      }

      public String getFrameType() {
         return this.frameType;
      }

      public String getRandomNum() {
         return this.randomNum;
      }

      public int getTotalPackets() {
         return this.totalPackets;
      }

      public int getCurrentPacket() {
         return this.currentPacket;
      }

      public String getDecryptedData() {
         return this.decryptedData;
      }
   }
}
