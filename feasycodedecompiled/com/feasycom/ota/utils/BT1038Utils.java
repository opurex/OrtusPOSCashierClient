package com.feasycom.ota.utils;

public class BT1038Utils {
   public static int littleEndianRead16(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8;
   }

   public static int littleEndianRead24(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16;
   }

   public static long littleEndianRead32(byte[] var0, int var1) {
      return (long)var0[var1] & 255L | ((long)var0[var1 + 1] & 255L) << 8 | ((long)var0[var1 + 2] & 255L) << 16 | ((long)var0[var1 + 3] & 255L) << 24;
   }

   public static void littleEndianStore16(byte[] var0, int var1, int var2) {
      byte[] var10000 = var0;
      var0[var1] = (byte)(var2 & 255);
      int var3 = var1 + 1;
      var10000[var3] = (byte)(var2 >> 8 & 255);
   }

   public static void littleEndianStore32(byte[] var0, int var1, long var2) {
      byte[] var10000 = var0;
      byte[] var10002 = var0;
      byte[] var10004 = var0;
      var0[var1] = (byte)((int)(var2 & 255L));
      int var4 = var1 + 1;
      var10004[var4] = (byte)((int)(var2 >> 8 & 255L));
      var4 = var1 + 2;
      var10002[var4] = (byte)((int)(var2 >> 16 & 255L));
      var4 = var1 + 3;
      var10000[var4] = (byte)((int)(var2 >> 24 & 255L));
   }

   public static int bigEndianRead16(byte[] var0, int var1) {
      return (var0[var1 + 1] & 255) << 8 | var0[var1] & 255;
   }

   public static int bigEndianRead24(byte[] var0, int var1) {
      return var0[var1 + 2] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1] & 255) << 16;
   }

   public static long bigEndianRead32(byte[] var0, int var1) {
      return (long)var0[var1 + 3] & 255L | ((long)var0[var1 + 2] & 255L) << 8 | ((long)var0[var1 + 1] & 255L) << 16 | ((long)var0[var1] & 255L) << 24;
   }

   public static void bigEndianStore16(byte[] var0, int var1, int var2) {
      byte[] var10000 = var0;
      var0[var1] = (byte)(var2 >> 8 & 255);
      int var3 = var1 + 1;
      var10000[var3] = (byte)(var2 & 255);
   }

   public static void bigEndianStore24(byte[] var0, int var1, int var2) {
      byte[] var10000 = var0;
      byte[] var10002 = var0;
      var0[var1] = (byte)(var2 >> 16 & 255);
      int var3 = var1 + 1;
      var10002[var3] = (byte)(var2 >> 8 & 255);
      var3 = var1 + 2;
      var10000[var3] = (byte)(var2 & 255);
   }

   public static void bigEndianStore32(byte[] var0, int var1, long var2) {
      byte[] var10000 = var0;
      byte[] var10002 = var0;
      byte[] var10004 = var0;
      var0[var1] = (byte)((int)(var2 >> 24 & 255L));
      int var4 = var1 + 1;
      var10004[var4] = (byte)((int)(var2 >> 16 & 255L));
      var4 = var1 + 2;
      var10002[var4] = (byte)((int)(var2 >> 8 & 255L));
      var4 = var1 + 3;
      var10000[var4] = (byte)((int)(var2 & 255L));
   }

   public static void bigEndianStore32To16(short[] var0, int var1, long var2) {
      short[] var10000 = var0;
      var0[var1] = (short)((int)(var2 >> 16 & 65535L));
      int var4 = var1 + 1;
      var10000[var4] = (short)((int)(var2 & 65535L));
   }

   public static void reverseBytes(byte[] var0, byte[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var2 - 1 - var3;
         var1[var4] = var0[var3];
      }

   }

   public static void reverse24(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 3);
   }

   public static void reverse32(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 4);
   }

   public static void reverse48(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 6);
   }

   public static void reverse56(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 7);
   }

   public static void reverse64(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 8);
   }

   public static void reverse128(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 16);
   }

   public static void reverse256(byte[] var0, byte[] var1) {
      reverseBytes(var0, var1, 32);
   }

   public static char hexToChar(byte var0) {
      return (char)(var0 >= 0 && var0 <= 9 ? var0 + 48 : (var0 >= 10 && var0 <= 15 ? var0 + 65 - 10 : '\uffff'));
   }

   public static int charToHex(byte var0) {
      return var0 >= 48 && var0 <= 57 ? var0 - 48 : (var0 >= 97 && var0 <= 102 ? var0 - 97 + 10 : (var0 >= 65 && var0 <= 70 ? var0 - 65 + 10 : '\uffff'));
   }

   private static int nibbleForChar(char var0) {
      if ('0' <= var0 && var0 <= '9') {
         return var0 - 48;
      } else if ('a' <= var0 && var0 <= 'f') {
         return var0 - 97 + 10;
      } else {
         return 'A' <= var0 && var0 <= 'F' ? var0 - 65 + 10 : -1;
      }
   }

   public static boolean isDigitalString(byte[] var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         if (!isBetween('0', (char)var0[var2], '9')) {
            return false;
         }
      }

      return true;
   }

   public static boolean isHexString(byte[] var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         if (!isBetween('0', (char)var0[var2], '9') && !isBetween('a', (char)var0[var2], 'f') && !isBetween('A', (char)var0[var2], 'F')) {
            return false;
         }
      }

      return true;
   }

   public static boolean strToHex(byte[] var0, String var1, int var2) {
      int var3 = 0;
      if (var2 % 2 != 0) {
         return false;
      } else {
         while(var3 < var2) {
            int var4;
            byte var10000 = (byte)(var4 = (byte)nibbleForChar(var1.charAt(var3)));
            byte var5 = (byte)nibbleForChar(var1.charAt(var3 + 1));
            if (var10000 == -1 || var5 == -1) {
               return false;
            }

            byte var10001 = (byte)var4;
            var4 = var3 / 2;
            var0[var4] = (byte)(var10001 << 4 | var5);
            var3 += 2;
         }

         return true;
      }
   }

   public static long strToDec(String var0, int var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < var1; ++var4) {
         var2 = var2 * 10L + (long)(var0.charAt(var4) - 48);
      }

      return var2;
   }

   public static int crc16CCITFalse(byte[] var0, int var1) {
      int var2 = 65535;
      short var3 = 4129;

      while(true) {
         int var10000 = var1;
         var1 += -1;
         if (var10000 <= 0) {
            return var2;
         }

         var2 ^= var0[var0.length - var1 - 1] << 8;

         for(int var4 = 0; var4 < 8; ++var4) {
            if ((var2 & '耀') != 0) {
               var2 = var2 << 1 ^ var3;
            } else {
               var2 <<= 1;
            }
         }
      }
   }

   private static boolean isBetween(char var0, char var1, char var2) {
      return var1 >= var0 && var1 <= var2;
   }

   public static byte[] byteMerge(byte[] var0, byte[] var1) {
      byte[] var2;
      byte[] var10000 = var2 = new byte[var0.length + var1.length];
      byte[] var10001 = var1;
      byte[] var10003 = var0;
      byte[] var10004 = var0;
      int var3 = var0.length;
      System.arraycopy(var10004, 0, var2, 0, var3);
      var3 = var10003.length;
      int var4 = var1.length;
      System.arraycopy(var10001, 0, var2, var3, var4);
      return var10000;
   }

   public static byte[] intToBytes(int var0) {
      byte[] var1 = new byte[2];

      for(int var2 = 0; var2 < 4; ++var2) {
         var1[var2] = (byte)(var0 >> var2 * 8);
      }

      return var1;
   }

   public static byte[] convertIntTo24Bits(int var0) {
      byte[] var1;
      byte[] var10000 = var1 = new byte[3];
      var1[0] = (byte)(var0 >> 16 & 255);
      var1[1] = (byte)(var0 >> 8 & 255);
      var10000[2] = (byte)(var0 & 255);
      return var10000;
   }
}
