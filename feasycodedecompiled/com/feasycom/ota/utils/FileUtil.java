package com.feasycom.ota.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import com.feasycom.ota.bean.DfuFileInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

@Keep
public class FileUtil {
   public static final String[] moduleNameInfo = new String[]{"BT401", "BT405", "BT426N", "BT501", "BT502", "BT522", "BT616", "BT625", "BT626", "BT803", "BT813D", "BT816S", "BT821", "BT822", "BT826", "BT826N", "BT836", "BT836N", "BT906", "BT909", "BP102", "BT816S3", "BT926", "BT901", "BP109", "BP103", "BP104", "BP201", "BP106", "BP101", "BP671", "BT826H", "BT826NH", "BT826E", "BT826EH"};
   public static final String[] moules = new String[]{"BT401", "BT405", "BT426N", "BT501", "BT502", "BT522", "BT616", "BT625", "BT626", "BT803", "BT813D", "BT816S", "BT821", "BT822", "BT826", "BT826N", "BT836", "BT836N", "BT906", "BT909", "BP102", "BT816S3", "BT926", "BT901", "BP109", "BP103", "BP104", "BP201", "BP106", "BP101", "BP671", "BT826H", "BT826NH", "BT826E", "BT826EH", "BT836B", "BT826B", "BT736", "BT926B", "BT966", "BT721B", "BT886", "BT104B", "BP110", "BP102B", "BP104D", "BT826A", "BT826BA", "BT986"};
   public static final String[] reconnectModule = new String[]{"BT901", "BT906", "BT909", "BT826", "BT926", "BT966"};
   public static char[] charTable = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static final byte[] ALPHANUMERIC = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
   private static final String Hex = "0123456789ABCDEF";

   public static String getMoules(int var0) {
      return moules[var0 - 1];
   }

   public static String getFileAbsolutePath(Context var0, Uri var1) {
      if (var0 != null && var1 != null) {
         if (VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(var0, var1)) {
            String[] var4;
            if (isExternalStorageDocument(var1)) {
               if ("primary".equalsIgnoreCase((var4 = DocumentsContract.getDocumentId(var1).split(":"))[0])) {
                  return Environment.getExternalStorageDirectory() + "/" + var4[1];
               }
            } else {
               if (isDownloadsDocument(var1)) {
                  return getDataColumn(var0, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(var1))), (String)null, (String[])null);
               }

               if (isMediaDocument(var1)) {
                  String[] var5;
                  String var2 = (var5 = DocumentsContract.getDocumentId(var1).split(":"))[0];
                  Uri var3 = null;
                  if ("image".equals(var2)) {
                     var3 = Media.EXTERNAL_CONTENT_URI;
                  } else if ("video".equals(var2)) {
                     var3 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                  } else if ("audio".equals(var2)) {
                     var3 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                  }

                  Context var10000 = var0;
                  (var4 = new String[1])[0] = var5[1];
                  return getDataColumn(var10000, var3, "_id=?", var4);
               }
            }
         } else {
            if ("content".equalsIgnoreCase(var1.getScheme())) {
               if (isGooglePhotosUri(var1)) {
                  return var1.getLastPathSegment();
               }

               return getDataColumn(var0, var1, (String)null, (String[])null);
            }

            if ("file".equalsIgnoreCase(var1.getScheme())) {
               return var1.getPath();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static String getModelName(String var0) {
      return var0 != null && !"".equals(var0) && var0.split("_").length == 8 ? var0.split("_")[0] : null;
   }

   public static String getAppVersion(String var0) {
      return var0 != null && !"".equals(var0) && var0.split("_").length == 8 ? var0.split("_")[6] : null;
   }

   public static String getBootLoaderVersion(String var0) {
      return var0 != null && !"".equals(var0) && var0.split("_").length == 8 ? var0.split("_")[4] : null;
   }

   public static byte[] readFile(String var0) {
      if (var0 != null && var0.length() >= 1) {
         var0 = URLDecoder.decode(var0, "UTF-8").replace("file:", "");
         File var1;
         File var10000 = var1 = new File;
         var10000.<init>(var0, "");
         FileInputStream var5;
         var5 = new FileInputStream.<init>(var1);

         Exception var6;
         label41: {
            long var7;
            boolean var10001;
            try {
               var7 = var10000.length();
            } catch (Exception var4) {
               var6 = var4;
               var10001 = false;
               break label41;
            }

            int var8 = (int)var7;

            byte[] var9;
            try {
               var9 = new byte[var8];
            } catch (Exception var3) {
               var6 = var3;
               var10001 = false;
               break label41;
            }

            byte[] var10 = var9;

            try {
               var5.read(var10);
               return var9;
            } catch (Exception var2) {
               var6 = var2;
               var10001 = false;
            }
         }

         var6.printStackTrace();
         return null;
      } else {
         return null;
      }
   }

   public static String getDataColumn(Context var0, Uri var1, String var2, String[] var3) {
      Context var10000 = var0;
      Cursor var17 = null;
      String[] var4;
      (var4 = new String[1])[0] = "_data";

      label164: {
         Throwable var19;
         label165: {
            boolean var10001;
            Cursor var20;
            try {
               var20 = var10000.getContentResolver().query(var1, var4, var2, var3, (String)null);
            } catch (Throwable var16) {
               var19 = var16;
               var10001 = false;
               break label165;
            }

            var17 = var20;
            if (var20 == null) {
               break label164;
            }

            boolean var21;
            try {
               var21 = var17.moveToFirst();
            } catch (Throwable var15) {
               var19 = var15;
               var10001 = false;
               break label165;
            }

            if (!var21) {
               break label164;
            }

            String var22;
            try {
               var22 = var17.getString(var17.getColumnIndexOrThrow("_data"));
            } catch (Throwable var14) {
               var19 = var14;
               var10001 = false;
               break label165;
            }

            var17.close();
            return var22;
         }

         Throwable var18 = var19;
         if (var17 != null) {
            var17.close();
         }

         throw var18;
      }

      if (var17 != null) {
         var17.close();
      }

      return "";
   }

   public static boolean isExternalStorageDocument(Uri var0) {
      return "com.android.externalstorage.documents".equals(var0.getAuthority());
   }

   public static boolean isDownloadsDocument(Uri var0) {
      return "com.android.providers.downloads.documents".equals(var0.getAuthority());
   }

   public static boolean isMediaDocument(Uri var0) {
      return "com.android.providers.media.documents".equals(var0.getAuthority());
   }

   public static boolean isGooglePhotosUri(Uri var0) {
      return "com.google.android.apps.photos.content".equals(var0.getAuthority());
   }

   public static String bytesToHex(byte[] var0, int var1) {
      return var0 == null ? "" : bytesToHex(var0, 0, var0.length, false);
   }

   public static String bytesToHex(byte[] var0, int var1, int var2, boolean var3) {
      if (var0 != null && var0.length > var1 && var2 > 0) {
         char[] var4 = new char[(var2 = Math.min(var2, var0.length - var1)) * 2];

         for(int var5 = 0; var5 < var2; ++var5) {
            int var6 = var0[var1 + var5] & 255;
            int var7 = var5 * 2;
            char[] var8;
            char[] var10001 = var8 = HEX_ARRAY;
            int var10002 = var6;
            var4[var7] = var8[var6 >>> 4];
            var6 = var7 + 1;
            var4[var6] = var10001[var10002 & 15];
         }

         return !var3 ? new String(var4) : "0x" + new String(var4);
      } else {
         return "";
      }
   }

   public static byte[] hexToByte(String var0) {
      if (var0.length() <= 0) {
         return new byte[0];
      } else {
         if ((var0 = var0.replace(" ", "")).length() % 2 == 1) {
            StringBuffer var10000 = new StringBuffer(var0);
            var10000.insert(var0.length() - 1, '0');
            var0 = var10000.toString();
         }

         byte[] var1 = new byte[var0.length() / 2];

         int var3;
         for(int var2 = 0; var2 < var0.length(); var2 = var3) {
            var3 = var2 + 2;
            String var4 = var0.substring(var2, var3);
            if (var2 == 0) {
               var1[var2] = (byte)Integer.parseInt(var4, 16);
            } else {
               var2 /= 2;
               var1[var2] = (byte)Integer.parseInt(var4, 16);
            }
         }

         return var1;
      }
   }

   public static String formattingOneIntToStrings(int var0) {
      int var1 = (var0 &= 15) + 1;
      return "0123456789ABCDEF".substring(var0, var1);
   }

   public static int formattingOneHexToInt(String var0) {
      return var0 != null && var0.length() == 1 ? "0123456789ABCDEF".indexOf(var0.toUpperCase()) : 0;
   }

   public static int formattingHexToInt(String var0) {
      String var10000 = var0;
      var0 = var0.substring(0, 1);
      return formattingOneHexToInt(var0) * 16 + formattingOneHexToInt(var10000.substring(1, 2));
   }

   public static int add(int var0, int var1) {
      int var10000 = var0;
      var0 &= var1;

      for(var1 ^= var10000; var0 != 0; var0 = var10000) {
         var10000 = var1;
         var1 &= var0 <<= 1;
         var0 ^= var10000;
         var10000 = var1;
         var1 = var0;
      }

      return var1;
   }

   public static int minus(int var0, int var1) {
      return add(var0, add(~var1, 1));
   }

   private static long transform(byte var0) {
      long var1;
      if ((var1 = (long)var0) < 0L) {
         var1 += 256L;
      }

      return var1;
   }

   public static int byteToInt(byte var0) {
      return var0 & 255;
   }

   public static int byteToInt(byte var0, byte var1) {
      byte var10000 = (byte)var0;
      var0 = var1 & 255;
      return (var10000 & 255) + (var0 << 8);
   }

   public static int byteToInt(byte var0, byte var1, byte var2, byte var3) {
      byte var10000 = (byte)var0;
      byte var10001 = (byte)var1;
      var0 = var3 & 255;
      var1 = var2 & 255;
      var2 = var10001 & 255;
      return (var10000 & 255) + (var2 << 8) + (var1 << 16) + (var0 << 24);
   }

   public static int stringToInt(String var0) {
      String var10000 = var0;
      var0 = var0.substring(0, 2);
      var10000 = var10000.substring(2, 4);
      int var1 = formattingHexToInt(var0);
      return byteToInt((byte)var1, (byte)formattingHexToInt(var10000));
   }

   public static int stringToInt1(String var0) {
      String var10000 = var0.replace(" ", "");
      var0 = var10000.substring(0, 2);
      var10000 = var10000.substring(2, 4);
      int var10002 = formattingHexToInt(var0);
      int var1 = formattingHexToInt(var10000);
      return byteToInt((byte)var1, (byte)var10002);
   }

   public static short byteToShort_2(byte var0, byte var1) {
      return (short)add(var0 & 255, var1 << 8);
   }

   public static int[] byteToInt(byte[] var0) {
      int[] var1 = new int[var0.length / 4];
      int var2 = 0;

      for(int var3 = 0; var3 < var0.length; var3 += 4) {
         var1[var2] = (int)(transform(var0[var3]) | transform(var0[var3 + 1]) << 8 | transform(var0[var3 + 2]) << 16 | transform(var0[var3 + 3]) << 24);
         ++var2;
      }

      return var1;
   }

   public static byte[] intToByte(int[] var0) {
      int var1;
      byte[] var2 = new byte[var1 = var0.length * 4];
      int var3 = 0;

      for(int var4 = 0; var4 < var1; var4 += 4) {
         var2[var4] = (byte)(var0[var3] & 255);
         int var5 = var4 + 1;
         var2[var5] = (byte)(var0[var3] >> 8 & 255);
         var5 = var4 + 2;
         var2[var5] = (byte)(var0[var3] >> 16 & 255);
         var5 = var4 + 3;
         var2[var5] = (byte)(var0[var3] >> 24 & 255);
         ++var3;
      }

      return var2;
   }

   public static long intToLong(int var0) {
      return (long)var0 & 4294967295L;
   }

   @NonNull
   public static String getFileName(String var0) {
      int var1;
      int var10000 = var1 = var0.lastIndexOf("/");
      int var2 = var0.lastIndexOf(".");
      return var10000 != -1 && var2 != -1 ? var0.substring(var1 + 1, var2) : "";
   }

   public static long readFileSize(String var0) {
      if (var0 != null && var0.length() >= 1) {
         var0 = URLDecoder.decode(var0, "UTF-8").replace("file:", "");
         return (new File(var0, "")).length();
      } else {
         return 0L;
      }
   }

   public static byte[] readFileToByte(String var0) {
      if (var0 != null && var0.length() >= 1) {
         var0 = URLDecoder.decode(var0, "UTF-8").replace("file:", "");
         File var10000 = new File(var0, "");
         FileInputStream var1;
         var1 = new FileInputStream.<init>(var0);
         byte[] var2 = new byte[(int)var10000.length()];
         var1.read(var2);
         return var2;
      } else {
         return null;
      }
   }

   public static InputStream getFileStream(String var0) {
      if (var0 != null && var0.length() >= 1) {
         var0 = URLDecoder.decode(var0, "UTF-8").replace("file:", "");
         return new FileInputStream(var0);
      } else {
         return null;
      }
   }

   public static String getFileByInputString(InputStream var0) {
      InputStreamReader var1;
      var1 = new InputStreamReader.<init>(var0);
      BufferedReader var6;
      var6 = new BufferedReader.<init>(var1);
      StringBuffer var7;
      var7 = new StringBuffer.<init>("");

      while(true) {
         IOException var10000;
         label39: {
            boolean var10001;
            String var8;
            try {
               var8 = var6.readLine();
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label39;
            }

            String var2 = var8;
            if (var8 == null) {
               break;
            }

            StringBuffer var9;
            try {
               var9 = var7;
               var7.append(var2);
            } catch (IOException var4) {
               var10000 = var4;
               var10001 = false;
               break label39;
            }

            try {
               var9.append("\n");
               continue;
            } catch (IOException var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         var10000.printStackTrace();
         break;
      }

      return new String(var7);
   }

   public static boolean isSdCardExist() {
      return Environment.getExternalStorageState().equals("mounted");
   }

   public static DfuFileInfo getDfuFileInformation(byte[] var0) {
      return (new TeaCode()).getDfuFileInformation(var0);
   }

   private static boolean isReconnect(String var0) {
      String[] var1;
      int var2 = (var1 = reconnectModule).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1[var3].equals(var0)) {
            return false;
         }
      }

      return true;
   }

   public static String getModelName(int var0) {
      String[] var1;
      return var0 > (var1 = moduleNameInfo).length ? "Unknown" : var1[var0 - 1];
   }

   public static boolean needsReconnect(byte[] var0) {
      int var1;
      return isReconnect((var1 = getDfuFileInformation(var0).type_model) == 40 ? "BT966" : getModelName(var1));
   }

   public static String hexToString(String var0) {
      StringBuilder var1;
      var1 = new StringBuilder.<init>();
      int var2 = 0;

      while(var2 < var0.length()) {
         var1.append((char)Integer.parseInt(var0.substring(var2, var2 += 2), 16));
      }

      return var1.toString();
   }

   public static byte[] changeBytesEndian(byte[] var0) {
      int var1;
      byte[] var2 = new byte[var1 = var0.length];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = var0[var1 - var3 - 1];
      }

      return var2;
   }
}
