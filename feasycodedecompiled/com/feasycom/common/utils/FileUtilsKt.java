package com.feasycom.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.Keep;
import java.io.ByteArrayInputStream;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 2,
   xi = 48,
   d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0019\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a\u0010\u0010\t\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a\u0010\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"},
   d2 = {"createTestData", "Ljava/io/ByteArrayInputStream;", "bufferSize", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAppName", "", "context", "Landroid/content/Context;", "getAppVersion", "getPackageName", "FeasyBlueLibrary_release"}
)
public final class FileUtilsKt {
   @Keep
   @Nullable
   public static final Object createTestData(int var0, @NotNull Continuation var1) {
      Object var2;
      int var3;
      if (var1 instanceof <undefinedtype> && ((var3 = ((<undefinedtype>)(var2 = (<undefinedtype>)var1)).c) & Integer.MIN_VALUE) != 0) {
         ((<undefinedtype>)var2).c = var3 - Integer.MIN_VALUE;
      } else {
         var2 = new ContinuationImpl() {
            public Object a;
            // $FF: synthetic field
            public Object b;
            public int c;

            @Nullable
            public final Object invokeSuspend(@NotNull Object var1) {
               this.b = var1;
               this.c |= Integer.MIN_VALUE;
               return FileUtilsKt.createTestData(0, this);
            }
         }.<init>(var1);
      }

      Object var6 = ((<undefinedtype>)var2).b;
      Object var8 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      int var4;
      byte[] var7;
      if ((var4 = ((<undefinedtype>)var2).c) != 0) {
         if (var4 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         byte[] var5 = (byte[])((<undefinedtype>)var2).a;
         ResultKt.throwOnFailure(var6);
         var7 = var5;
      } else {
         ResultKt.throwOnFailure(var6);
         if (var0 % 10 != 0 || var0 == 0) {
            return new ByteArrayInputStream(new byte[0]);
         }

         var7 = new byte[var0];
         CoroutineDispatcher var10000 = Dispatchers.getDefault();
         Function2 var9;
         var9 = new Function2() {
            public int a;
            // $FF: synthetic field
            public final byte[] c;

            public {
               this.c = var2;
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object var1) {
               IntrinsicsKt.getCOROUTINE_SUSPENDED();
               if (this.a != 0) {
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               } else {
                  ResultKt.throwOnFailure(var1);
                  int var5 = 0;
                  String var2 = "0123456789ABCDEF";

                  for(int var3 = 0; var3 < FileUtilsKt.this; var3 += 10) {
                     byte[] var4;
                     byte[] var10008 = var4 = this.c;
                     var4[var3] = (byte)91;
                     int var6 = var3 + 8;
                     var10008[var6] = (byte)var2.charAt(var5 >> 0 & 15);
                     var6 = var3 + 7;
                     this.c[var6] = (byte)var2.charAt(var5 >> 4 & 15);
                     var6 = var3 + 6;
                     this.c[var6] = (byte)var2.charAt(var5 >> 8 & 15);
                     var6 = var3 + 5;
                     this.c[var6] = (byte)var2.charAt(var5 >> 12 & 15);
                     var6 = var3 + 4;
                     this.c[var6] = (byte)var2.charAt(var5 >> 16 & 15);
                     var6 = var3 + 3;
                     this.c[var6] = (byte)var2.charAt(var5 >> 20 & 15);
                     var6 = var3 + 2;
                     this.c[var6] = (byte)var2.charAt(var5 >> 24 & 15);
                     var6 = var3 + 1;
                     this.c[var6] = (byte)var2.charAt(var5 >> 28 & 15);
                     this.c[var3 + 9] = (byte)93;
                     ++var5;
                  }

                  return Unit.INSTANCE;
               }
            }

            @NotNull
            public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
               <undefinedtype> var10002 = this;
               int var3 = FileUtilsKt.this;
               return new <anonymous constructor>(var10002.c, var2);
            }

            @Nullable
            public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
               return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }.<init>(var7, (Continuation)null);
         ((<undefinedtype>)var2).a = var7;
         ((<undefinedtype>)var2).c = 1;
         if (BuildersKt.withContext(var10000, var9, (Continuation)var2) == var8) {
            return var8;
         }
      }

      return new ByteArrayInputStream(var7);
   }

   @Nullable
   public static final String getAppName(@NotNull Context var0) {
      Context var10000 = var0;
      Intrinsics.checkNotNullParameter(var0, "context");
      PackageManager var1 = var0.getPackageManager();
      return var10000.getApplicationInfo().loadLabel(var1).toString();
   }

   @Nullable
   public static final String getAppVersion(@NotNull Context var0) {
      Intrinsics.checkNotNullParameter(var0, "context");
      PackageManager var10000 = var0.getPackageManager();
      Context var10001 = var0;
      String var4 = "";

      String var1;
      PackageManager.NameNotFoundException var5;
      label26: {
         String var6;
         boolean var7;
         try {
            var6 = var10000.getPackageInfo(var10001.getPackageName(), 0).versionName;
         } catch (PackageManager.NameNotFoundException var3) {
            var5 = var3;
            var7 = false;
            break label26;
         }

         var1 = var6;

         try {
            Intrinsics.checkNotNullExpressionValue(var6, "packageInfo.versionName");
            return var1;
         } catch (PackageManager.NameNotFoundException var2) {
            var5 = var2;
            var7 = false;
         }
      }

      var5.printStackTrace();
      var1 = var4;
      return var1;
   }

   @Nullable
   public static final String getPackageName(@NotNull Context var0) {
      Intrinsics.checkNotNullParameter(var0, "context");
      return var0.getPackageName();
   }
}
