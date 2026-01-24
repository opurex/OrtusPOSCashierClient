package com.example.io;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.example.util.FutureThreadPool;
import com.example.util.Packet;

public class BluetoothCtrl {
   public static final String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

   public static boolean cancelBondProcess(BluetoothDevice arg0) throws Exception {
      Boolean var1 = false;

      Boolean var10000;
      try {
         var1 = (Boolean)arg0.getClass().getMethod(FutureThreadPool.ALLATORIxDEMO("}{py{v\\up~Nhqy{im")).invoke(arg0);
      } catch (SecurityException var3) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("$ y\u007ft}\u007frXqtzJlu}\u007fmi$")).append(var3.getMessage()).toString());
         var3.printStackTrace();
         var10000 = var1;
         return var10000;
      } catch (IllegalArgumentException var4) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("$ y\u007ft}\u007frXqtzJlu}\u007fmi$")).append(var4.getMessage()).toString());
         var4.printStackTrace();
         var10000 = var1;
         return var10000;
      } catch (Exception var5) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO("$ y\u007ft}\u007frXqtzJlu}\u007fmi$")).append(var5.getMessage()).toString());
         var5.printStackTrace();
         var10000 = var1;
         return var10000;
      }

      var10000 = var1;
      return var10000;
   }

   public static boolean createBond(BluetoothDevice arg0) throws Exception {
      return (Boolean)arg0.getClass().getMethod(Packet.ALLATORIxDEMO("u\u0007s\u0014b\u0010T\u001ax\u0011")).invoke(arg0);
   }

   public static String ALLATORIxDEMO(String arg0) {
      int var10001 = (2 ^ 5) << 3 ^ 5;
      int var10002 = 5 << 4 ^ 2 << 2 ^ 1;
      int var10003 = arg0.length();
      char[] var10004 = new char[var10003];
      boolean var10006 = true;
      int var5 = var10003 - 1;
      var10003 = var10002;
      int var3;
      var10002 = var3 = var5;
      char[] var1 = var10004;
      int var4 = var10003;
      int var10000 = var10002;

      for(byte var2 = 4; var10000 >= 0; var10000 = var3) {
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

   public static boolean setPin(BluetoothDevice arg0, String arg1) throws Exception {
      Boolean var2 = false;

      Boolean var10000;
      try {
         var2 = (Boolean)arg0.getClass().getDeclaredMethod(FutureThreadPool.ALLATORIxDEMO("i{nNsp"), byte[].class).invoke(arg0, arg1.getBytes());
         Log.d(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO(" $m\u007fjJwt$")).append(var2.toString()).toString());
      } catch (SecurityException var4) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO(" $m\u007fjJwt$")).append(var4.getMessage()).toString());
         var4.printStackTrace();
         var10000 = var2;
         return var10000;
      } catch (IllegalArgumentException var5) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO(" $m\u007fjJwt$")).append(var5.getMessage()).toString());
         var5.printStackTrace();
         var10000 = var2;
         return var10000;
      } catch (Exception var6) {
         Log.e(Packet.ALLATORIxDEMO("\u0007s\u0001c\u0007x#w\u0019c\u0010"), (new StringBuilder()).insert(0, FutureThreadPool.ALLATORIxDEMO(" $m\u007fjJwt$")).append(var6.getMessage()).toString());
         var6.printStackTrace();
         var10000 = var2;
         return var10000;
      }

      var10000 = var2;
      return var10000;
   }

   public static boolean removeBond(BluetoothDevice arg0) throws Exception {
      return (Boolean)arg0.getClass().getMethod(Packet.ALLATORIxDEMO("d\u0010{\u001a`\u0010T\u001ax\u0011")).invoke(arg0);
   }

   public static boolean cancelPairingUserInput(BluetoothDevice arg0) throws Exception {
      return (Boolean)arg0.getClass().getMethod(FutureThreadPool.ALLATORIxDEMO("y\u007ft}\u007frJ\u007fslsp}Ki{hWtnoj")).invoke(arg0);
   }
}
