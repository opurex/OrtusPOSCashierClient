package com.feasycom.spp.request;

import com.feasycom.common.bean.Type;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"},
   d2 = {"Lcom/feasycom/spp/request/Request;", "", "type", "Lcom/feasycom/common/bean/Type;", "(Lcom/feasycom/common/bean/Type;)V", "getType", "()Lcom/feasycom/common/bean/Type;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "FeasyBlueLibrary_release"}
)
public final class Request {
   @NotNull
   private final Type type;

   public Request(@NotNull Type var1) {
      Intrinsics.checkNotNullParameter(var1, "type");
      super();
      this.type = var1;
   }

   // $FF: synthetic method
   public static Request copy$default(Request var0, Type var1, int var2, Object var3) {
      if ((var2 & 1) != 0) {
         var1 = var0.type;
      }

      return var0.copy(var1);
   }

   @NotNull
   public final Type getType() {
      return this.type;
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else {
         Class var2 = Request.class;
         Class var3;
         if (var1 != null) {
            var3 = var1.getClass();
         } else {
            var3 = null;
         }

         if (!Intrinsics.areEqual(var2, var3)) {
            return false;
         } else {
            Request var10000 = this;
            Intrinsics.checkNotNull(var1, "null cannot be cast to non-null type com.feasycom.spp.request.Request");
            Request var4 = (Request)var1;
            return var10000.type == var4.type;
         }
      }
   }

   public int hashCode() {
      return this.type.hashCode();
   }

   @NotNull
   public final Type component1() {
      return this.type;
   }

   @NotNull
   public final Request copy(@NotNull Type var1) {
      Intrinsics.checkNotNullParameter(var1, "type");
      return new Request(var1);
   }

   @NotNull
   public String toString() {
      return "Request(type=" + this.type + ')';
   }
}
