package com.example.data;

import android.util.Log;
import com.example.io.BluetoothCtrl;
import com.example.util.PacketHandler;

public class St_Dialog06 {
   public String m_strWeight = "";
   public int m_iType = -1;
   public String m_strPrice = "";
   public String m_strText = "";
   private byte[] ALLATORIxDEMO = new byte[24];
   public String m_strAmount = "";
   public String m_strTare = "";

   // $FF: synthetic method
   private void ALLATORIxDEMO(String arg0) {
      Log.d(BluetoothCtrl.ALLATORIxDEMO("\u001dm8h6ci2"), arg0);
   }

   // $FF: synthetic method
   private int ALLATORIxDEMO(byte[] arg0, int arg1, int arg2, byte[] arg3) {
      byte var5 = -1;
      int var6 = -1;
      int var7 = 0;

      int var8;
      for(int var10000 = var8 = arg2; var10000 < arg1; var10000 = var8) {
         if (var6 == -1) {
            if (arg0[var8] == 27) {
               var6 = var8 + 1;
            }
         } else {
            if (arg0[var8] == 27 || arg0[var8] == 3) {
               if (var7 > 0 && var7 < arg3.length) {
                  System.arraycopy(arg0, var6, arg3, 0, var7);
                  Log.d(PacketHandler.ALLATORIxDEMO("\u007fSZVT]\u000b\f"), (new StringBuilder()).insert(0, BluetoothCtrl.ALLATORIxDEMO(">a-W-v0j>$\np8v->")).append(var6).append(PacketHandler.ALLATORIxDEMO("\u001aw_U\u0000")).append(var7).toString());
                  return var7;
               }
               break;
            }

            ++var7;
         }

         ++var8;
      }

      return var5;
   }

   public boolean parseData(byte[] arg0, int arg1) {
      int var3 = 0;
      if (arg0 != null && arg1 > 27) {
         this.ALLATORIxDEMO();
         boolean var4 = true;
         byte var5 = 13;
         int var6 = 4 + var5;
         int var10000;
         int var8;
         if (arg0[0 + var5] == 4 && arg0[1 + var5] == 2 && arg0[2 + var5] == 48) {
            if (arg0[3 + var5] == 49) {
               if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                  var10000 = this.m_iType = 1;
                  this.m_strPrice = new String(this.ALLATORIxDEMO, 0, var8);
                  var3 = var10000;
               }
            } else if (arg0[3 + var5] == 51) {
               if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                  this.m_iType = 3;
                  this.m_strPrice = new String(this.ALLATORIxDEMO, 0, var8);
                  var6 += var8;
                  if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                     this.m_strTare = new String(this.ALLATORIxDEMO, 0, var8);
                  }

                  var3 = 1;
               }
            } else if (arg0[3 + var5] == 52) {
               if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                  this.m_strPrice = new String(this.ALLATORIxDEMO, 0, var8);
                  var6 += var8;
                  this.m_iType = 4;
                  if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                     this.m_strText = new String(this.ALLATORIxDEMO, 0, var8);
                  }

                  var3 = 1;
               }
            } else if (arg0[3 + var5] == 53 && (var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
               this.m_strPrice = new String(this.ALLATORIxDEMO, 0, var8);
               var6 += var8;
               if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                  this.m_strTare = new String(this.ALLATORIxDEMO, 0, var8);
                  this.m_iType = 5;
                  if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var8, this.ALLATORIxDEMO)) > 0) {
                     this.m_strText = new String(this.ALLATORIxDEMO, 0, var8);
                  }
               }

               var3 = 1;
            }
         } else if (arg0[0 + var5] == 2 && arg0[1 + var5] == 48 && arg0[2 + var5] == 50) {
            var6 = 5 + var5;
            this.m_iType = 2;
            if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
               this.m_strWeight = new String(this.ALLATORIxDEMO, 0, var8);
               var6 += var8;
               if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                  var6 += var8;
                  this.m_strPrice = new String(this.ALLATORIxDEMO, 0, var8);
                  if ((var8 = this.ALLATORIxDEMO(arg0, arg1, var6, this.ALLATORIxDEMO)) > 0) {
                     var10000 = var6 + var8;
                     this.m_strAmount = new String(this.ALLATORIxDEMO, 0, var8);
                  }

                  var3 = 1;
               }
            }
         } else {
            this.ALLATORIxDEMO(arg0, arg1);
         }
      } else {
         if (arg0 != null && arg1 == 18 && arg0[13] == 4) {
            this.ALLATORIxDEMO();
            this.m_iType = -4;
            boolean var7 = true;
            return true;
         }

         this.ALLATORIxDEMO(arg0, arg1);
      }

      return (boolean)var3;
   }

   // $FF: synthetic method
   private void ALLATORIxDEMO(byte[] arg0, int arg1) {
      byte var3;
      if ((var3 = arg0[11]) > 0 && var3 <= arg1 - 15) {
         String var4 = new String(arg0, 13, var3);
         this.ALLATORIxDEMO(var4);
      }

   }

   // $FF: synthetic method
   private void ALLATORIxDEMO() {
      this.m_iType = -1;
      this.m_strPrice = "";
      this.m_strTare = "";
      this.m_strText = "";
      this.m_strAmount = "";
      this.m_strWeight = "";
   }
}
