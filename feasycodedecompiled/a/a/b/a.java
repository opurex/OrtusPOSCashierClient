package a.a.b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.feasycom.logger.LogAdapter;
import com.feasycom.logger.Printer;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a implements Printer {
   public final ThreadLocal a;
   public final List b;

   public a() {
      a var10000 = this;
      a var10001 = this;
      super();
      ThreadLocal var1;
      var1 = new ThreadLocal.<init>();
      var10001.a = var1;
      ArrayList var2;
      var2 = new ArrayList.<init>();
      var10000.b = var2;
   }

   public Printer t(String var1) {
      if (var1 != null) {
         this.a.set(var1);
      }

      return this;
   }

   public void d(@NonNull String var1, @Nullable Object... var2) {
      this.a(3, (Throwable)null, var1, var2);
   }

   public void d(@Nullable Object var1) {
      a var10000 = this;
      String var2 = a.a.b.b.b(var1);
      Object[] var3 = new Object[0];
      var10000.a(3, (Throwable)null, var2, var3);
   }

   public void e(@NonNull String var1, @Nullable Object... var2) {
      this.e((Throwable)null, var1, var2);
   }

   public void e(@Nullable Throwable var1, @NonNull String var2, @Nullable Object... var3) {
      this.a(6, var1, var2, var3);
   }

   public void w(@NonNull String var1, @Nullable Object... var2) {
      this.a(5, (Throwable)null, var1, var2);
   }

   public void i(@NonNull String var1, @Nullable Object... var2) {
      this.a(4, (Throwable)null, var1, var2);
   }

   public void v(@NonNull String var1, @Nullable Object... var2) {
      this.a(2, (Throwable)null, var1, var2);
   }

   public void wtf(@NonNull String var1, @Nullable Object... var2) {
      this.a(7, (Throwable)null, var1, var2);
   }

   public void json(@Nullable String var1) {
      if (a.a.b.b.a((CharSequence)var1)) {
         this.d("Empty/Null json content");
      } else {
         label59: {
            String var10000;
            boolean var10001;
            try {
               var10000 = var1.trim();
            } catch (JSONException var7) {
               var10001 = false;
               break label59;
            }

            var1 = var10000;

            boolean var8;
            try {
               var8 = var10000.startsWith("{");
            } catch (JSONException var6) {
               var10001 = false;
               break label59;
            }

            if (var8) {
               try {
                  this.d((new JSONObject(var1)).toString(2));
                  return;
               } catch (JSONException var3) {
                  var10001 = false;
               }
            } else {
               label45: {
                  try {
                     var8 = var1.startsWith("[");
                  } catch (JSONException var5) {
                     var10001 = false;
                     break label45;
                  }

                  if (var8) {
                     try {
                        this.d((new JSONArray(var1)).toString(2));
                        return;
                     } catch (JSONException var2) {
                        var10001 = false;
                     }
                  } else {
                     a var9 = this;
                     String var10 = "Invalid Json";

                     try {
                        var9.e(var10);
                        return;
                     } catch (JSONException var4) {
                        var10001 = false;
                     }
                  }
               }
            }
         }

         this.e("Invalid Json");
      }
   }

   public void xml(@Nullable String var1) {
      if (a.a.b.b.a((CharSequence)var1)) {
         this.d("Empty/Null xml content");
      } else {
         label44: {
            a var10000;
            boolean var10001;
            StreamSource var9;
            try {
               var10000 = this;
               var9 = new StreamSource;
            } catch (TransformerException var7) {
               var10001 = false;
               break label44;
            }

            StreamSource var2 = var9;

            StreamResult var10;
            try {
               var9.<init>(new StringReader(var1));
               var10 = new StreamResult;
            } catch (TransformerException var6) {
               var10001 = false;
               break label44;
            }

            StreamResult var8;
            StreamResult var10002 = var8 = var10;

            Transformer var11;
            try {
               var10002.<init>(new StringWriter());
               var11 = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerException var5) {
               var10001 = false;
               break label44;
            }

            Transformer var3 = var11;

            try {
               var3.setOutputProperty("indent", "yes");
               var3.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
               var11.transform(var2, var8);
               var10000.d(var10.getWriter().toString().replaceFirst(">", ">\n"));
               return;
            } catch (TransformerException var4) {
               var10001 = false;
            }
         }

         this.e("Invalid xml");
      }
   }

   public synchronized void log(int var1, @Nullable String var2, @Nullable String var3, @Nullable Throwable var4) {
      if (var4 != null && var3 != null) {
         var3 = var3 + " : " + a.a.b.b.a(var4);
      }

      if (var4 != null && var3 == null) {
         var3 = a.a.b.b.a(var4);
      }

      if (a.a.b.b.a((CharSequence)var3)) {
         var3 = "Empty/NULL log message";
      }

      Iterator var5 = this.b.iterator();

      while(var5.hasNext()) {
         LogAdapter var6;
         if ((var6 = (LogAdapter)var5.next()).isLoggable(var1, var2)) {
            var6.log(var1, var2, var3);
         }
      }

   }

   public void clearLogAdapters() {
      this.b.clear();
   }

   public void addAdapter(@NonNull LogAdapter var1) {
      this.b.add((LogAdapter)a.a.b.b.a((Object)var1));
   }

   public final synchronized void a(int var1, @Nullable Throwable var2, @NonNull String var3, @Nullable Object... var4) {
      a var10000 = this;
      a var10001 = this;
      a.a.b.b.a((Object)var3);
      String var5 = this.a();
      var3 = var10001.a(var3, var4);
      var10000.log(var1, var5, var3, var2);
   }

   @Nullable
   public final String a() {
      String var1;
      if ((var1 = (String)this.a.get()) != null) {
         this.a.remove();
         return var1;
      } else {
         return null;
      }
   }

   @NonNull
   public final String a(@NonNull String var1, @Nullable Object... var2) {
      if (var2 != null && var2.length != 0) {
         var1 = String.format(var1, var2);
      }

      return var1;
   }
}
