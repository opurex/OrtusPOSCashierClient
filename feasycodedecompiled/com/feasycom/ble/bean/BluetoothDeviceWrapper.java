package com.feasycom.ble.bean;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Build.VERSION;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import com.feasycom.ble.controler.FscBleCentralCallbacks;
import com.feasycom.ble.error.GattError;
import com.feasycom.ble.request.Request;
import com.feasycom.ble.service.OTAService;
import com.feasycom.common.bean.ConnectType;
import com.feasycom.common.bean.Type;
import com.feasycom.common.utils.ExpandKt;
import com.feasycom.common.utils.FileUtilsKt;
import com.feasycom.common.utils.MsgLogger;
import com.feasycom.encrypted.controler.FscEncryptApi;
import com.feasycom.encrypted.utils.FACP;
import com.feasycom.logger.Logger;
import com.feasycom.ota.bean.DfuFileInfo;
import com.feasycom.ota.utils.FileUtil;
import com.feasycom.ota.utils.OtaUtils;
import com.feasycom.ota.utils.TeaCode;
import com.feasycom.ota.utils.XmodemUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.Job.DefaultImpls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000å\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\"\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r*\u0001&\b\u0086\b\u0018\u0000 È\u00012\u00020\u0001:\u0004È\u0001É\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010z\u001a\u00020{H\u0002J\b\u0010|\u001a\u00020\fH\u0002J\u001a\u0010}\u001a\u00020~2\u0006\u0010\u007f\u001a\u0002082\u0007\u0010\u0080\u0001\u001a\u00020\u0016H\u0086 J\t\u0010\u0081\u0001\u001a\u00020{H\u0002J\n\u0010\u0082\u0001\u001a\u00020\u0003HÆ\u0003J\n\u0010\u0083\u0001\u001a\u00020\u0005HÆ\u0003J\n\u0010\u0084\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0085\u0001\u001a\u00020\tHÆ\u0003J\u0019\u0010\u0086\u0001\u001a\u00020\f2\u0007\u0010\u0087\u0001\u001a\u00020?2\u0007\u0010\u0088\u0001\u001a\u00020\fJ\u0010\u0010\u0089\u0001\u001a\u00020\f2\u0007\u0010\u0087\u0001\u001a\u00020?J+\u0010\u008a\u0001\u001a\u00020\f2\u0007\u0010\u0087\u0001\u001a\u00020?2\u0007\u0010\u008b\u0001\u001a\u0002082\u0007\u0010\u008c\u0001\u001a\u00020\f2\u0007\u0010\u008d\u0001\u001a\u00020\u0016J\u0007\u0010\u008e\u0001\u001a\u00020\fJ2\u0010\u008f\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\t\u0010\u0090\u0001\u001a\u00020{H\u0007J\u0007\u0010\u0091\u0001\u001a\u00020{J\t\u0010\u0092\u0001\u001a\u00020{H\u0002J\u0015\u0010\u0093\u0001\u001a\u00020\f2\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\r\u0010\u0095\u0001\u001a\b\u0012\u0004\u0012\u00020*0)J\u0017\u0010\u0096\u0001\u001a\u0005\u0018\u00010\u0097\u00012\t\u0010\u0098\u0001\u001a\u0004\u0018\u00010:H\u0002J\u0007\u0010\u0099\u0001\u001a\u00020\u0016J\r\u0010\u009a\u0001\u001a\b\u0012\u0004\u0012\u00020:0)J\u0012\u0010\u009b\u0001\u001a\u00020{2\u0007\u0010\u009c\u0001\u001a\u00020$H\u0003J\t\u0010\u009d\u0001\u001a\u0004\u0018\u00010:J\n\u0010\u009e\u0001\u001a\u00020\u0016HÖ\u0001J\t\u0010\u009f\u0001\u001a\u00020{H\u0002J\t\u0010 \u0001\u001a\u00020{H\u0003J\t\u0010¡\u0001\u001a\u00020{H\u0003J\t\u0010¢\u0001\u001a\u00020{H\u0003J\t\u0010£\u0001\u001a\u00020{H\u0003J\t\u0010¤\u0001\u001a\u00020{H\u0003J\u0012\u0010¥\u0001\u001a\u00020{2\u0007\u0010¦\u0001\u001a\u00020\u0016H\u0003J\t\u0010§\u0001\u001a\u00020{H\u0003J\t\u0010¨\u0001\u001a\u00020{H\u0003J\t\u0010©\u0001\u001a\u00020{H\u0003J\t\u0010ª\u0001\u001a\u00020{H\u0003J\u001c\u0010«\u0001\u001a\u00020{2\u0007\u0010\u009c\u0001\u001a\u00020$2\b\u0010¬\u0001\u001a\u00030\u0097\u0001H\u0003J\t\u0010\u00ad\u0001\u001a\u00020{H\u0003J\u0015\u0010®\u0001\u001a\u00020\f2\n\u0010¬\u0001\u001a\u0005\u0018\u00010\u0097\u0001H\u0002J\u0012\u0010¯\u0001\u001a\u00020{2\u0007\u0010°\u0001\u001a\u00020\fH\u0003J\u0012\u0010±\u0001\u001a\u00020\u00032\u0007\u0010²\u0001\u001a\u000208H\u0002J\u0007\u0010³\u0001\u001a\u00020\fJ\u0012\u0010´\u0001\u001a\u00020{2\u0007\u0010µ\u0001\u001a\u00020:H\u0007J\u0012\u0010¶\u0001\u001a\u00020{2\u0007\u0010¦\u0001\u001a\u00020\u0016H\u0007J\u0011\u0010·\u0001\u001a\u00020\f2\u0006\u0010\u007f\u001a\u000208H\u0007J\u0017\u0010¸\u0001\u001a\u00020{2\u000e\u0010¹\u0001\u001a\t\u0012\u0004\u0012\u00020\u00030º\u0001J\u0013\u0010»\u0001\u001a\u00020\f2\b\u0010¼\u0001\u001a\u00030½\u0001H\u0007J\u000f\u0010»\u0001\u001a\u00020\f2\u0006\u0010\u007f\u001a\u000208J\u0010\u0010»\u0001\u001a\u00020\f2\u0007\u0010¾\u0001\u001a\u00020\u0016J\u0019\u0010¿\u0001\u001a\u00020\f2\u0007\u0010µ\u0001\u001a\u00020:2\u0007\u0010À\u0001\u001a\u00020\u0016J\u0010\u0010¿\u0001\u001a\u00020{2\u0007\u0010\u0087\u0001\u001a\u00020?J\u0010\u0010Á\u0001\u001a\u00020{2\u0007\u0010Â\u0001\u001a\u00020\u000eJ\u0007\u0010Ã\u0001\u001a\u00020{J\n\u0010Ä\u0001\u001a\u00020\u0003HÖ\u0001J$\u0010Å\u0001\u001a\u00020\f2\u0007\u0010µ\u0001\u001a\u00020:2\u0007\u0010Æ\u0001\u001a\u00020\f2\u0007\u0010Ç\u0001\u001a\u000208H\u0003R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082D¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR!\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u001e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b\u001f\u0010 R\u000e\u0010#\u001a\u00020$X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0004\n\u0002\u0010'R\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020*0)X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0015\u0010,\u001a\u00060-j\u0002`.¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u000e\u00101\u001a\u000202X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u000e\u00105\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u000208X\u0082.¢\u0006\u0002\n\u0000R\u0010\u00109\u001a\u0004\u0018\u00010:X\u0082\u000e¢\u0006\u0002\n\u0000R!\u0010;\u001a\b\u0012\u0004\u0012\u0002080\u001e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b=\u0010\"\u001a\u0004\b<\u0010 R\u001a\u0010>\u001a\u00020?X\u0080.¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR\u0010\u0010D\u001a\u0004\u0018\u00010EX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010I\u001a\u00020\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010K\"\u0004\bL\u0010MR\u0014\u0010N\u001a\b\u0012\u0004\u0012\u00020:0)X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010O\u001a\u0004\u0018\u00010?X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010A\"\u0004\bQ\u0010CR\u0010\u0010R\u001a\u0004\u0018\u00010SX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R!\u0010U\u001a\b\u0012\u0004\u0012\u00020:0\u001e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bW\u0010\"\u001a\u0004\bV\u0010 R\u001a\u0010X\u001a\u00020\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bY\u0010K\"\u0004\bZ\u0010MR\u0010\u0010[\u001a\u0004\u0018\u000108X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\\\u001a\u0004\u0018\u00010]X\u0082\u000e¢\u0006\u0002\n\u0000R!\u0010^\u001a\b\u0012\u0004\u0012\u00020]0\u001e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b`\u0010\"\u001a\u0004\b_\u0010 R\u000e\u0010a\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010b\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bc\u0010d\"\u0004\be\u0010fR\u000e\u0010g\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010h\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010i\u001a\u0004\u0018\u000108X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010j\u001a\u00020kX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010l\u001a\u0012\u0012\u0004\u0012\u00020\u00030mj\b\u0012\u0004\u0012\u00020\u0003`nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010o\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bp\u0010d\"\u0004\bq\u0010fR\u000e\u0010r\u001a\u00020\u000eX\u0082D¢\u0006\u0002\n\u0000R\u0010\u0010s\u001a\u0004\u0018\u00010:X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010t\u001a\u00060-j\u0002`.8\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u001a\u0010u\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bv\u0010w\"\u0004\bx\u0010y¨\u0006Ê\u0001"},
   d2 = {"Lcom/feasycom/ble/bean/BluetoothDeviceWrapper;", "", "mAddress", "", "device", "Landroid/bluetooth/BluetoothDevice;", "mContext", "Landroid/content/Context;", "connectCallback", "Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$ConnectCallback;", "(Ljava/lang/String;Landroid/bluetooth/BluetoothDevice;Landroid/content/Context;Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$ConnectCallback;)V", "FACP2", "", "RSSI_INTERVAL", "", "getConnectCallback", "()Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$ConnectCallback;", "setConnectCallback", "(Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$ConnectCallback;)V", "connectJob", "Lkotlinx/coroutines/Job;", "connectNumber", "", "getDevice", "()Landroid/bluetooth/BluetoothDevice;", "handler", "Landroid/os/Handler;", "getMAddress", "()Ljava/lang/String;", "mAtQueue", "Ljava/util/Queue;", "getMAtQueue", "()Ljava/util/Queue;", "mAtQueue$delegate", "Lkotlin/Lazy;", "mBluetoothGatt", "Landroid/bluetooth/BluetoothGatt;", "mBluetoothGattCallback", "com/feasycom/ble/bean/BluetoothDeviceWrapper$mBluetoothGattCallback$1", "Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$mBluetoothGattCallback$1;", "mBluetoothGattServices", "", "Landroid/bluetooth/BluetoothGattService;", "mCommand", "mConnectRunnable", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "getMConnectRunnable", "()Ljava/lang/Runnable;", "mConnectType", "Lcom/feasycom/common/bean/ConnectType;", "getMContext", "()Landroid/content/Context;", "mDeviceBusy", "mDeviceBusyLock", "mDfuFile", "", "mFACPCharacteristic", "Landroid/bluetooth/BluetoothGattCharacteristic;", "mFacpQueue", "getMFacpQueue", "mFacpQueue$delegate", "mFscBleCentralCallbacks", "Lcom/feasycom/ble/controler/FscBleCentralCallbacks;", "getMFscBleCentralCallbacks$FeasyBlueLibrary_release", "()Lcom/feasycom/ble/controler/FscBleCentralCallbacks;", "setMFscBleCentralCallbacks$FeasyBlueLibrary_release", "(Lcom/feasycom/ble/controler/FscBleCentralCallbacks;)V", "mFscEncryptApiImp", "Lcom/feasycom/encrypted/controler/FscEncryptApi;", "mInitMtu", "mIsConnect", "mMaximumPacketByte", "mModuleVersion", "getMModuleVersion", "()I", "setMModuleVersion", "(I)V", "mNotifyCharacteristicList", "mOTACallbacks", "getMOTACallbacks$FeasyBlueLibrary_release", "setMOTACallbacks$FeasyBlueLibrary_release", "mOtaService", "Lcom/feasycom/ble/service/OTAService;", "mPauseSend", "mReadQueue", "getMReadQueue", "mReadQueue$delegate", "mRealMtu", "getMRealMtu", "setMRealMtu", "mReceivePasswordInfo", "mRequest", "Lcom/feasycom/ble/request/Request;", "mRequests", "getMRequests", "mRequests$delegate", "mReset", "mSendInterval", "getMSendInterval", "()J", "setMSendInterval", "(J)V", "mSendJob", "mSendPacket", "mSendPasswordInfo", "mServiceConnection", "Landroid/content/ServiceConnection;", "mServiceFilterList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "mStartTimer", "getMStartTimer", "setMStartTimer", "mTimeOut", "mWriteCharacteristic", "rssiRunnable", "writeSuccess", "getWriteSuccess", "()Z", "setWriteSuccess", "(Z)V", "bindService", "", "bluetoothGattRefresh", "cacChecksum", "", "bytes", "len", "clearQueue", "component1", "component2", "component3", "component4", "connect", "fscBleCentralCallbacks", "facpSwitchOpen", "connectToModify", "connectToOTAWithFactory", "dfuFile", "reset", "moduleVersion", "continueSend", "copy", "createBond", "disconnect", "enqueue", "equals", "other", "getBluetoothGattServices", "getCccd", "Landroid/bluetooth/BluetoothGattDescriptor;", "characteristic", "getMaximumPacketByte", "getNotifyCharacteristicList", "getSupportedServices", "gatt", "getWriteCharacteristic", "hashCode", "initQueue", "internalAt", "internalConnect", "internalDisconnect", "internalEnableFACPNotifications", "internalEnableNotifications", "internalMtu", "mtu", "internalOTA", "internalReadCharacteristic", "internalSendATCommand", "internalSendEncrypt", "internalWriteDescriptorWorkaround", "descriptor", "internalWriteFACPShake", "isCCCD", "nextRequest", "boolean", "parseMacAddress", "data", "pauseSend", "read", "ch", "requestMtu", "send", "sendATCommand", "command", "", "sendFile", "inputStream", "Ljava/io/InputStream;", "size", "setCharacteristic", "properties", "setSendInterval", "interval", "stopSend", "toString", "updateGattDescriptor", "enable", "value", "Companion", "ConnectCallback", "FeasyBlueLibrary_release"}
)
public final class BluetoothDeviceWrapper {
   @NotNull
   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
   private static final boolean HAVE_AUTH = false;
   @NotNull
   private static final String TAG = "BLEBluetoothDevice";
   private static final UUID SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
   private static final UUID NOTIFY_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
   private static final UUID WRITE_UUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
   private static final UUID FACP_UUID = UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb");
   private static final UUID MAC_UUID = UUID.fromString("0000fff5-0000-1000-8000-00805f9b34fb");
   private static final UUID GATT_DEVICE_INFO_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
   private static final UUID GAP_SERVICE_UUID = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
   private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
   @NotNull
   private final String mAddress;
   @NotNull
   private final BluetoothDevice device;
   @NotNull
   private final Context mContext;
   @NotNull
   private ConnectCallback connectCallback;
   private Job connectJob;
   private Job mSendJob;
   @NotNull
   private final Handler handler;
   private final long RSSI_INTERVAL;
   private long mSendInterval;
   @NotNull
   private final Lazy mRequests$delegate;
   @NotNull
   private final Lazy mReadQueue$delegate;
   @NotNull
   private final Lazy mFacpQueue$delegate;
   @NotNull
   private final Lazy mAtQueue$delegate;
   @Nullable
   private String mCommand;
   @NotNull
   private List mBluetoothGattServices;
   private boolean FACP2;
   @Nullable
   private Request mRequest;
   private int mInitMtu;
   private int mMaximumPacketByte;
   private int mRealMtu;
   private int mSendPacket;
   private int connectNumber;
   private boolean mIsConnect;
   @NotNull
   private ConnectType mConnectType;
   private BluetoothGatt mBluetoothGatt;
   private int mModuleVersion;
   @NotNull
   private List mNotifyCharacteristicList;
   @Nullable
   private BluetoothGattCharacteristic mWriteCharacteristic;
   @Nullable
   private BluetoothGattCharacteristic mFACPCharacteristic;
   public FscBleCentralCallbacks mFscBleCentralCallbacks;
   @Nullable
   private FscEncryptApi mFscEncryptApiImp;
   @Nullable
   private byte[] mSendPasswordInfo;
   @Nullable
   private byte[] mReceivePasswordInfo;
   private long mStartTimer;
   @NotNull
   private final Runnable mConnectRunnable;
   @NotNull
   private final Object mDeviceBusyLock;
   private boolean mDeviceBusy;
   private final long mTimeOut;
   private boolean mReset;
   private boolean mPauseSend;
   private byte[] mDfuFile;
   @Nullable
   private OTAService mOtaService;
   @Nullable
   private FscBleCentralCallbacks mOTACallbacks;
   @NotNull
   private final ServiceConnection mServiceConnection;
   @SuppressLint({"MissingPermission"})
   @NotNull
   private final Runnable rssiRunnable;
   @NotNull
   private final <undefinedtype> mBluetoothGattCallback;
   @NotNull
   private final ArrayList mServiceFilterList;
   private boolean writeSuccess;

   public BluetoothDeviceWrapper(@NotNull String var1, @NotNull BluetoothDevice var2, @NotNull Context var3, @NotNull ConnectCallback var4) {
      Intrinsics.checkNotNullParameter(var1, "mAddress");
      Intrinsics.checkNotNullParameter(var2, "device");
      Intrinsics.checkNotNullParameter(var3, "mContext");
      Intrinsics.checkNotNullParameter(var4, "connectCallback");
      super();
      this.mAddress = var1;
      this.device = var2;
      this.mContext = var3;
      this.connectCallback = var4;
      this.handler = new Handler(Looper.getMainLooper());
      this.RSSI_INTERVAL = 1000L;
      this.mRequests$delegate = LazyKt.lazy(null.a);
      this.mReadQueue$delegate = LazyKt.lazy(null.a);
      this.mFacpQueue$delegate = LazyKt.lazy(null.a);
      this.mAtQueue$delegate = LazyKt.lazy(null.a);
      this.mBluetoothGattServices = new ArrayList();
      this.mInitMtu = 517;
      this.mMaximumPacketByte = 20;
      this.mRealMtu = 23;
      this.mConnectType = ConnectType.CONNECT;
      this.mNotifyCharacteristicList = new ArrayList();
      this.mConnectRunnable = new BluetoothDeviceWrapper$special$$inlined$Runnable$1(this);
      this.mDeviceBusyLock = new Object();
      this.mTimeOut = 6000L;
      this.mServiceConnection = new ServiceConnection() {
         public void onServiceConnected(@NotNull ComponentName var1, @NotNull IBinder var2) {
            Intrinsics.checkNotNullParameter(var1, "name");
            Intrinsics.checkNotNullParameter(var2, "service");
            MsgLogger.e("=================BLE 升级服务启动成功==============");
            BluetoothDeviceWrapper.this.mOtaService = ((OTAService.LocalBinder)var2).getMService();
            OTAService var3;
            if ((var3 = BluetoothDeviceWrapper.this.mOtaService) != null) {
               var3.startOta(BluetoothDeviceWrapper.this);
            }

         }

         public void onServiceDisconnected(@NotNull ComponentName var1) {
            Intrinsics.checkNotNullParameter(var1, "name");
            BluetoothDeviceWrapper.this.mOtaService = null;
         }
      };
      this.rssiRunnable = new BluetoothDeviceWrapper$special$$inlined$Runnable$2(this);
      this.mBluetoothGattCallback = new BluetoothGattCallback() {
         public void onMtuChanged(@Nullable BluetoothGatt var1, int var2, int var3) {
            super.onMtuChanged(var1, var2, var3);
            MsgLogger.e("BLE mBluetoothGattCallback status => " + var3 + " \r\n mtu => " + var2);
            if (var3 == 0) {
               BluetoothDeviceWrapper.this.setMRealMtu(var2);
               BluetoothDeviceWrapper var5 = BluetoothDeviceWrapper.this;
               int var4;
               if (var2 == 247) {
                  var4 = var2 - 65;
               } else if ((var4 = var2 - 3) > 512) {
                  var4 = var2 - 5;
               }

               var5.mMaximumPacketByte = var4;
               MsgLogger.e("BLE mBluetoothGattCallback mMaximumPacketByte => " + BluetoothDeviceWrapper.this.mMaximumPacketByte);
               Request var6;
               if (BluetoothDeviceWrapper.this.mRequest != null && ((var6 = BluetoothDeviceWrapper.this.mRequest) != null ? var6.getType() : null) == Type.REQUEST_MTU) {
                  BluetoothDeviceWrapper.this.nextRequest(true);
               }

               BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().bleMtuChanged(var2, var3);
            }

         }

         @SuppressLint({"MissingPermission"})
         public void onConnectionStateChange(@NotNull BluetoothGatt var1, int var2, int var3) {
            Intrinsics.checkNotNullParameter(var1, "gatt");
            super.onConnectionStateChange(var1, var2, var3);
            MsgLogger.e("BLE onConnectionStateChange addr => " + BluetoothDeviceWrapper.this.getMAddress() + "  deviceAddr => " + var1.getDevice().getAddress() + "  status => " + var2 + "  newState => " + var3);
            Object var4;
            Object var10001 = var4 = BluetoothDeviceWrapper.this.mDeviceBusyLock;
            BluetoothDeviceWrapper var10002 = BluetoothDeviceWrapper.this;
            synchronized(var4){}

            try {
               var10002.mDeviceBusy = false;
            } catch (Throwable var8) {
               throw var8;
            }

            if (var2 == 0 && var3 == 2) {
               var1.discoverServices();
            } else {
               BluetoothDeviceWrapper.this.mIsConnect = false;
               BluetoothDeviceWrapper.this.stopSend();
               var1.close();
               BluetoothDeviceWrapper.this.bluetoothGattRefresh();
               BluetoothDeviceWrapper var13;
               BluetoothDeviceWrapper var16;
               Handler var19;
               if (var2 != 62 && var2 != 133) {
                  MsgLogger.e("BLE onConnectionStateChange: 断开连接清除缓存");
                  BluetoothDeviceWrapper.this.internalDisconnect();
                  if (BluetoothDeviceWrapper.this.mOtaService != null) {
                     label141: {
                        BluetoothDeviceWrapper var10000 = BluetoothDeviceWrapper.this;
                        var16 = var10000;
                        var10002 = var13 = var10000;

                        IllegalArgumentException var14;
                        label140: {
                           FscBleCentralCallbacks var15;
                           boolean var17;
                           String var18;
                           try {
                              MsgLogger.e("BLE onConnectionStateChange: 升级服务解绑");
                              var10002.getMContext().getApplicationContext().unbindService(var13.mServiceConnection);
                              var16.mOtaService = null;
                              var15 = var10000.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
                              var18 = var13.getMAddress();
                           } catch (IllegalArgumentException var10) {
                              var14 = var10;
                              var17 = false;
                              break label140;
                           }

                           float var20 = -1.0F;

                           try {
                              var15.otaProgressUpdate(var18, var20, XmodemUtils.OTA_STATUS_FAILED);
                              break label141;
                           } catch (IllegalArgumentException var9) {
                              var14 = var9;
                              var17 = false;
                           }
                        }

                        var14.printStackTrace();
                     }
                  }

                  var19 = BluetoothDeviceWrapper.this.handler;
                  var13 = BluetoothDeviceWrapper.this;
                  var19.postDelayed(new BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$2(var13, var1, var2), 0L);
                  BluetoothDeviceWrapper.this.handler.removeCallbacks(BluetoothDeviceWrapper.this.rssiRunnable);
                  BluetoothDeviceWrapper.this.connectNumber = 0;
               } else {
                  var16 = BluetoothDeviceWrapper.this;
                  var16.connectNumber = var16.connectNumber + 1;
                  if (BluetoothDeviceWrapper.this.connectNumber <= 10) {
                     BluetoothDeviceWrapper var12 = BluetoothDeviceWrapper.this;
                     BluetoothGatt var11;
                     if (VERSION.SDK_INT >= 23) {
                        Intrinsics.checkNotNullExpressionValue(var11 = var12.getDevice().connectGatt(BluetoothDeviceWrapper.this.getMContext(), false, this, 2), "{\n                      …                        }");
                     } else {
                        Intrinsics.checkNotNullExpressionValue(var11 = var12.getDevice().connectGatt(BluetoothDeviceWrapper.this.getMContext(), false, this), "{\n                      …                        }");
                     }

                     var12.mBluetoothGatt = var11;
                  } else {
                     if (var3 == 0) {
                        if (var2 != 0) {
                           MsgLogger.e("BLE onConnectionStateChange", "Error: (0x" + Integer.toHexString(var2) + "): " + GattError.parseConnectionError(var2));
                        }
                     } else if (var2 != 0) {
                        MsgLogger.e("BLE onConnectionStateChange", "Error (0x" + Integer.toHexString(var2) + "): " + GattError.parseConnectionError(var2));
                     }

                     var19 = BluetoothDeviceWrapper.this.handler;
                     var13 = BluetoothDeviceWrapper.this;
                     var19.postDelayed(new BluetoothDeviceWrapper$mBluetoothGattCallback$1$onConnectionStateChange$$inlined$Runnable$1(var13, var1, var2), 0L);
                     BluetoothDeviceWrapper.this.handler.removeCallbacks(BluetoothDeviceWrapper.this.rssiRunnable);
                     BluetoothDeviceWrapper.this.connectNumber = 0;
                  }
               }
            }

         }

         public void onServicesDiscovered(@NotNull BluetoothGatt var1, int var2) {
            Intrinsics.checkNotNullParameter(var1, "gatt");
            super.onServicesDiscovered(var1, var2);
            if (var2 == 0) {
               BluetoothDeviceWrapper.this.getSupportedServices(var1);
               BluetoothDeviceWrapper.this.nextRequest(true);
            } else {
               BluetoothDeviceWrapper.this.internalDisconnect();
            }

         }

         public void onCharacteristicChanged(@NotNull BluetoothGatt var1, @NotNull BluetoothGattCharacteristic var2) {
            Intrinsics.checkNotNullParameter(var1, "gatt");
            Intrinsics.checkNotNullParameter(var2, "characteristic");
            super.onCharacteristicChanged(var1, var2);
            byte[] var5 = var2.getValue();
            if (BluetoothDeviceWrapper.this.mNotifyCharacteristicList.contains(var2)) {
               String var7;
               String var10002 = var7 = new String;
               Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
               var10002.<init>(var5, Charsets.UTF_8);
               FscBleCentralCallbacks var10001 = BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
               String var3 = BluetoothDeviceWrapper.this.getMAddress();
               String var4 = ExpandKt.toHexString(var5);
               var10001.packetReceived(var3, var7, var4, var5);
               FscBleCentralCallbacks var10;
               if ((var10 = BluetoothDeviceWrapper.this.getMOTACallbacks$FeasyBlueLibrary_release()) != null) {
                  FscBleCentralCallbacks var10000 = var10;
                  var3 = BluetoothDeviceWrapper.this.getMAddress();
                  var4 = ExpandKt.toHexString(var5);
                  var10000.packetReceived(var3, var7, var4, var5);
               }

               if (Intrinsics.areEqual(var7, "$OK,Opened$")) {
                  if (!BluetoothDeviceWrapper.this.mIsConnect) {
                     BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().startATCommand();
                     BluetoothDeviceWrapper.this.nextRequest(true);
                  }
               } else {
                  String var6;
                  if ((var6 = BluetoothDeviceWrapper.this.mCommand) != null) {
                     BluetoothDeviceWrapper var12 = BluetoothDeviceWrapper.this;
                     MsgLogger.e("strValue => " + var7);
                     if (StringsKt.contains$default(var7, "ERROR", false, 2, (Object)null)) {
                        if (StringsKt.contains$default(var6, "=", false, 2, (Object)null)) {
                           var12.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().atCommandCallBack(var6, ExpandKt.getParam(var6), 6, 3);
                        } else {
                           var7 = ExpandKt.getParam(var7);
                           var12.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().atCommandCallBack(var6, var7, 5, 3);
                        }
                     } else if (StringsKt.contains$default(var6, "=", false, 2, (Object)null)) {
                        var12.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().atCommandCallBack(var6, ExpandKt.getParam(var6), 6, 2);
                     } else {
                        var7 = ExpandKt.getParam(var7);
                        MsgLogger.e("mCommandStrValue it => " + var6 + "    receiveTemp => " + var7);
                        var12.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().atCommandCallBack(var6, var7, 5, 2);
                     }
                  }

                  BluetoothDeviceWrapper.this.internalSendATCommand();
               }
            } else {
               UUID var9 = var2.getUuid();
               BluetoothGattCharacteristic var13;
               UUID var14;
               if ((var13 = BluetoothDeviceWrapper.this.mFACPCharacteristic) != null) {
                  var14 = var13.getUuid();
               } else {
                  var14 = null;
               }

               if (Intrinsics.areEqual(var9, var14)) {
                  StringBuilder var15;
                  if (FACP.cacChecksum(var5, var5.length) == var5[var5.length - 1]) {
                     if (var5[0] == -2 && var5[1] == -22) {
                        int var11;
                        if ((var11 = var5[4]) == 1) {
                           StringBuilder var10004 = (new StringBuilder()).append("BLE onCharacteristicChanged => 收到第一次握手信息 ");
                           Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
                           MsgLogger.i(var10004.append(ExpandKt.toHexString(var5)).toString());
                           BluetoothDeviceWrapper.this.getMFacpQueue().clear();
                           byte[] var10003 = var5 = new byte[5];
                           var10003[0] = -2;
                           var10003[1] = -22;
                           var10003[2] = 0;
                           var10003[3] = 2;
                           var10003[4] = 32;
                           BluetoothDeviceWrapper.this.getMFacpQueue().add(ArraysKt.plus(var5, BluetoothDeviceWrapper.this.cacChecksum(var5, 5)));
                           byte[] var17 = var5 = new byte[5];
                           var17[0] = -2;
                           var17[1] = -22;
                           var17[2] = 0;
                           var17[3] = 2;
                           var17[4] = 33;
                           BluetoothDeviceWrapper.this.getMFacpQueue().add(ArraysKt.plus(var5, BluetoothDeviceWrapper.this.cacChecksum(var5, 5)));
                           BluetoothDeviceWrapper.this.internalWriteFACPShake();
                        } else {
                           int var8;
                           if (var11 == 32) {
                              StringBuilder var10005 = (new StringBuilder()).append("BLE onCharacteristicChanged => 收到第二次握手信息 ");
                              Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
                              MsgLogger.i(var10005.append(ExpandKt.toHexString(var5)).toString());
                              var11 = ((var5[6] & 255) << 8) + (var5[7] & 255);
                              var8 = ((var5[8] & 255) << 8) + (var5[9] & 255);
                              BluetoothDeviceWrapper.this.mMaximumPacketByte = var8;
                              BluetoothDeviceWrapper.this.mSendPacket = var11;
                              MsgLogger.i("BLE FACP2.0 =>  mtu  " + var8 + "   可以发送   " + BluetoothDeviceWrapper.this.mSendPacket + " 包");
                              BluetoothDeviceWrapper.this.internalWriteFACPShake();
                           } else if (var11 == 33) {
                              BluetoothDeviceWrapper.this.FACP2 = true;
                              var15 = (new StringBuilder()).append("BLE 收到第三次握手信息 => ");
                              Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
                              MsgLogger.i(var15.append(ExpandKt.toHexString(var5)).toString());
                           } else if (var11 == 34) {
                              var11 = (var5[6] << 8) + var5[7];
                              BluetoothDeviceWrapper var16 = BluetoothDeviceWrapper.this;
                              var16.mSendPacket = var16.mSendPacket + var11;
                              var15 = (new StringBuilder()).append("BLE FACP2.0: 收到可发送包数 => ").append(var11).append("   共可发送包数 => ").append(BluetoothDeviceWrapper.this.mSendPacket).append("   原始数据为 => ");
                              Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
                              MsgLogger.i(var15.append(ExpandKt.toHexString(var5)).toString());
                           } else if (var11 == 35) {
                              var8 = (var5[6] << 8) + var5[7];
                              BluetoothDeviceWrapper.this.mSendPacket = var8;
                              MsgLogger.i("BLE onCharacteristicChanged => " + BluetoothDeviceWrapper.this.mSendPacket);
                           } else {
                              MsgLogger.i("BLE 收到其他的信息");
                           }
                        }
                     }
                  } else {
                     var15 = (new StringBuilder()).append("BLE 收到错误的FACP2.0 信息 => ");
                     Intrinsics.checkNotNullExpressionValue(var5, "characteristicValue");
                     MsgLogger.i(var15.append(ExpandKt.toHexString(var5)).toString());
                  }
               }
            }

         }

         public void onCharacteristicWrite(@Nullable BluetoothGatt var1, @NotNull BluetoothGattCharacteristic var2, int var3) {
            <undefinedtype> var10000 = this;
            Intrinsics.checkNotNullParameter(var2, "characteristic");
            super.onCharacteristicWrite(var1, var2, var3);
            Object var8;
            Object var10002 = var8 = BluetoothDeviceWrapper.this.mDeviceBusyLock;
            BluetoothDeviceWrapper var10003 = BluetoothDeviceWrapper.this;
            synchronized(var8){}

            try {
               var10003.mDeviceBusy = false;
            } catch (Throwable var5) {
               throw var5;
            }

            byte[] var6 = var2.getValue();
            BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().writeResponse(var6);
            StringBuilder var7 = (new StringBuilder()).append("BLE onCharacteristicWrite 发送数据  ");
            Intrinsics.checkNotNullExpressionValue(var6, "value");
            MsgLogger.e(var7.append(new String(var6, Charsets.UTF_8)).append("   ").append(ExpandKt.toHexString(var6)).append("  status => ").append(var3).toString());
         }

         public void onDescriptorWrite(@Nullable BluetoothGatt var1, @Nullable BluetoothGattDescriptor var2, int var3) {
            super.onDescriptorWrite(var1, var2, var3);
            Object var6;
            Object var10001 = var6 = BluetoothDeviceWrapper.this.mDeviceBusyLock;
            BluetoothDeviceWrapper var10002 = BluetoothDeviceWrapper.this;
            synchronized(var6){}

            try {
               var10002.mDeviceBusy = false;
            } catch (Throwable var5) {
               throw var5;
            }

            if (var3 == 0) {
               if (BluetoothDeviceWrapper.this.isCCCD(var2)) {
                  if (BluetoothDeviceWrapper.this.mRequest != null) {
                     BluetoothDeviceWrapper.this.nextRequest(true);
                  }

               }
            }
         }

         public void onCharacteristicRead(@Nullable BluetoothGatt var1, @NotNull BluetoothGattCharacteristic var2, int var3) {
            Intrinsics.checkNotNullParameter(var2, "characteristic");
            super.onCharacteristicRead(var1, var2, var3);
            byte[] var11;
            byte[] var10000 = var11 = var2.getValue();
            Object var13;
            Object var10001 = var13 = BluetoothDeviceWrapper.this.mDeviceBusyLock;
            BluetoothDeviceWrapper var10002 = BluetoothDeviceWrapper.this;
            synchronized(var13){}

            try {
               var10002.mDeviceBusy = false;
            } catch (Throwable var8) {
               throw var8;
            }

            FscBleCentralCallbacks var15;
            if (var10000 != null && Intrinsics.areEqual(var2.getUuid(), UUID.fromString("0000fff5-0000-1000-8000-00805f9b34fb"))) {
               var15 = BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
               String var14 = BluetoothDeviceWrapper.this.getMAddress();
               String var4;
               var4 = new String.<init>(var11, Charsets.UTF_8);
               String var5 = ExpandKt.toHexString(var11);
               var15.readResponse(var14, var2, var4, var5, var11);
            }

            if (BluetoothDeviceWrapper.this.mRequest != null) {
               BluetoothDeviceWrapper.this.nextRequest(false);
            }

            if (Intrinsics.areEqual(var2.getUuid().toString(), "00002a24-0000-1000-8000-00805f9b34fb")) {
               var15 = BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release();
               Intrinsics.checkNotNullExpressionValue(var11, "characteristicValue");
               var15.readModuleModel(ExpandKt.parsingModel(new String(var11, Charsets.UTF_8)));
            }

            if (Intrinsics.areEqual(var2.getUuid().toString(), "00002a28-0000-1000-8000-00805f9b34fb")) {
               Intrinsics.checkNotNullExpressionValue(var11, "characteristicValue");
               String var16 = ExpandKt.parsingVersion(new String(var11, Charsets.UTF_8));
               MsgLogger.e("BLE onCharacteristicRead module  =>  " + var11);
               BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().readModuleVersion(ExpandKt.parsingVersion(new String(var11, Charsets.UTF_8)));
               BluetoothDeviceWrapper var10 = BluetoothDeviceWrapper.this;

               int var12;
               label71: {
                  int var17;
                  try {
                     var17 = Integer.parseInt(var16);
                  } catch (Exception var9) {
                     var12 = 0;
                     break label71;
                  }

                  var12 = var17;
               }

               var10.setMModuleVersion(var12);
            }

         }

         @RequiresApi(
            api = 26
         )
         @Keep
         public final void onConnectionUpdated(@Nullable BluetoothGatt var1, int var2, int var3, int var4, int var5) {
            if (var5 != 0) {
               if (var5 != 59) {
                  MsgLogger.e("BLE ly: else");
               } else {
                  MsgLogger.e("BLE ly: 0x3b");
               }
            } else {
               MsgLogger.e("BLE Connection parameters updated (interval: " + (double)var2 * 1.25 + "ms, latency: " + var3 + ", timeout: " + var4 * 10 + "ms)");
            }

         }

         public void onPhyRead(@Nullable BluetoothGatt var1, int var2, int var3, int var4) {
            super.onPhyRead(var1, var2, var3, var4);
         }

         public void onPhyUpdate(@Nullable BluetoothGatt var1, int var2, int var3, int var4) {
            super.onPhyUpdate(var1, var2, var3, var4);
         }

         public void onReadRemoteRssi(@Nullable BluetoothGatt var1, int var2, int var3) {
            super.onReadRemoteRssi(var1, var2, var3);
         }
      };
      this.mServiceFilterList = CollectionsKt.arrayListOf(new String[]{"1800", "1801", "2a00", "2a01", "2a05", "2a29", "2a24", "2a25", "2a27", "2a26", "2a28", "2a23", "2a2a"});
      System.loadLibrary("util");
      System.loadLibrary("facp");
   }

   private final Queue getMRequests() {
      return (Queue)this.mRequests$delegate.getValue();
   }

   private final Queue getMReadQueue() {
      return (Queue)this.mReadQueue$delegate.getValue();
   }

   private final Queue getMFacpQueue() {
      return (Queue)this.mFacpQueue$delegate.getValue();
   }

   private final Queue getMAtQueue() {
      return (Queue)this.mAtQueue$delegate.getValue();
   }

   @SuppressLint({"MissingPermission"})
   private final boolean updateGattDescriptor(BluetoothGattCharacteristic var1, boolean var2, byte[] var3) {
      BluetoothDeviceWrapper var10000 = this;
      boolean var4 = false;

      BluetoothGatt var5;
      label67: {
         Exception var13;
         label71: {
            BluetoothGatt var14;
            boolean var10001;
            try {
               var14 = var10000.mBluetoothGatt;
            } catch (Exception var9) {
               var13 = var9;
               var10001 = false;
               break label71;
            }

            var5 = var14;
            if (var14 == null) {
               try {
                  Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
               } catch (Exception var8) {
                  var13 = var8;
                  var10001 = false;
                  break label71;
               }

               var5 = null;
            }

            boolean var15;
            try {
               var15 = var5.setCharacteristicNotification(var1, var2);
            } catch (Exception var7) {
               var13 = var7;
               var10001 = false;
               break label71;
            }

            var4 = var15;

            try {
               MsgLogger.e("updateGattDescriptor setCharacteristicNotification ch => " + var1 + "    enable => " + var2);
               break label67;
            } catch (Exception var6) {
               var13 = var6;
               var10001 = false;
            }
         }

         var13.printStackTrace();
         this.disconnect();
      }

      if (!var4) {
         return false;
      } else {
         List var10;
         if ((var10 = var1.getDescriptors()) != null && var10.size() > 0) {
            BluetoothGattDescriptor var12;
            for(Iterator var11 = var10.iterator(); var11.hasNext(); var5.writeDescriptor(var12)) {
               (var12 = (BluetoothGattDescriptor)var11.next()).setValue(var3);
               if ((var5 = this.mBluetoothGatt) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                  var5 = null;
               }
            }

            if (Arrays.equals(var3, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)) {
               this.mNotifyCharacteristicList.remove(var1);
            } else if (Arrays.equals(var3, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
               this.mNotifyCharacteristicList.add(var1);
            }

            return true;
         } else {
            return true;
         }
      }
   }

   private final void initQueue() {
      this.getMRequests().add(new Request(Type.CONNECT));
      this.getMRequests().add(new Request(Type.ENABLE_NOTIFICATIONS));
      this.getMRequests().add(new Request(Type.REQUEST_MTU));
   }

   @SuppressLint({"MissingPermission"})
   private final void internalConnect() {
      Exception var10000;
      label92: {
         boolean var10001;
         int var19;
         try {
            var19 = VERSION.SDK_INT;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label92;
         }

         int var1 = var19;
         boolean var2;
         <undefinedtype> var3;
         Context var17;
         Context var10002;
         BluetoothGatt var18;
         BluetoothDevice var20;
         BluetoothDeviceWrapper var21;
         BluetoothGatt var22;
         <undefinedtype> var23;
         if (var19 >= 23) {
            if (var1 >= 26) {
               try {
                  MsgLogger.d("internalConnect BLE PHY_LE_2M_MASK...");
                  var20 = this.device;
                  var21 = this;
                  var10002 = this.mContext;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label92;
               }

               var17 = var10002;
               var2 = false;

               try {
                  var23 = var21.mBluetoothGattCallback;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label92;
               }

               var3 = var23;

               try {
                  var22 = var20.connectGatt(var17, var2, var3, 2, 2);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label92;
               }
            } else {
               try {
                  MsgLogger.d("internalConnect BLE PHY_LE_1M_MASK");
                  var20 = this.device;
                  var21 = this;
                  var10002 = this.mContext;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label92;
               }

               var17 = var10002;
               var2 = false;

               try {
                  var23 = var21.mBluetoothGattCallback;
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label92;
               }

               var3 = var23;

               try {
                  var22 = var20.connectGatt(var17, var2, var3, 2);
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label92;
               }
            }

            var18 = var22;

            try {
               Intrinsics.checkNotNullExpressionValue(var18, "{\n                if (Bu…          }\n            }");
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label92;
            }
         } else {
            try {
               MsgLogger.e("internalConnect 版本号小于23");
               var20 = this.device;
               var21 = this;
               var10002 = this.mContext;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label92;
            }

            var17 = var10002;
            var2 = false;

            try {
               var23 = var21.mBluetoothGattCallback;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label92;
            }

            var3 = var23;

            try {
               var22 = var20.connectGatt(var17, var2, var3);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label92;
            }

            var18 = var22;

            try {
               Intrinsics.checkNotNullExpressionValue(var22, "{\n                MsgLog…ttCallback)\n            }");
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label92;
            }
         }

         try {
            this.mBluetoothGatt = var18;
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      var10000.printStackTrace();
   }

   private final boolean bluetoothGattRefresh() {
      Exception var10000;
      label61: {
         BluetoothGatt var10;
         boolean var10001;
         try {
            var10 = this.mBluetoothGatt;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label61;
         }

         BluetoothGatt var1 = var10;
         if (var10 == null) {
            try {
               Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label61;
            }

            var1 = null;
         }

         BluetoothDeviceWrapper var11;
         Class var12;
         try {
            var11 = this;
            var12 = var1.getClass();
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label61;
         }

         String var10002 = "refresh";

         Method var13;
         try {
            var13 = var12.getMethod(var10002);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label61;
         }

         Method var9 = var13;

         try {
            var10 = var11.mBluetoothGatt;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label61;
         }

         var1 = var10;
         if (var10 == null) {
            try {
               Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label61;
            }

            var1 = null;
         }

         try {
            Object var14 = var9.invoke(var1);
            Intrinsics.checkNotNull(var14, "null cannot be cast to non-null type kotlin.Boolean");
            return (Boolean)var14;
         } catch (Exception var2) {
            var10000 = var2;
            var10001 = false;
         }
      }

      Logger.e(var10000.toString());
      var10000.printStackTrace();
      return false;
   }

   @SuppressLint({"MissingPermission"})
   private final void internalDisconnect() {
      if (this.mOtaService != null) {
         try {
            MsgLogger.e("internalDisconnect BLE 解绑升级服务.");
            this.mContext.getApplicationContext().unbindService(this.mServiceConnection);
            this.mOtaService = null;
         } catch (IllegalArgumentException var2) {
            var2.printStackTrace();
         }
      }

      Job var1;
      if ((var1 = this.mSendJob) != null) {
         if (var1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
            var1 = null;
         }

         if (var1.isActive()) {
            MsgLogger.e("BLEBluetoothDevice", "模块主动发起断开连接，取消SendJob任务。");
            if ((var1 = this.mSendJob) == null) {
               Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
               var1 = null;
            }

            DefaultImpls.cancel$default(var1, (CancellationException)null, 1, (Object)null);
         }
      }

      this.clearQueue();
      BluetoothGatt var3;
      if ((var3 = this.mBluetoothGatt) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var3 = null;
      }

      var3.disconnect();
      if ((var3 = this.mBluetoothGatt) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var3 = null;
      }

      var3.close();
      this.handler.removeCallbacks(this.rssiRunnable);
   }

   private final void clearQueue() {
      this.mRequest = null;
      this.getMAtQueue().clear();
      this.getMReadQueue().clear();
      this.getMFacpQueue().clear();
      this.getMRequests().clear();
   }

   @SuppressLint({"MissingPermission"})
   private final void internalEnableNotifications() {
      if (this.mNotifyCharacteristicList.isEmpty()) {
         this.disconnect();
      } else {
         Iterator var1 = this.mNotifyCharacteristicList.iterator();

         while(var1.hasNext()) {
            BluetoothGattCharacteristic var2;
            BluetoothGattDescriptor var3;
            if ((var3 = this.getCccd(var2 = (BluetoothGattCharacteristic)var1.next())) != null) {
               BluetoothGatt var4;
               if ((var4 = this.mBluetoothGatt) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                  var4 = null;
               }

               var4.setCharacteristicNotification(var2, true);
               var3.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
               BluetoothGatt var5;
               if ((var5 = this.mBluetoothGatt) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                  var5 = null;
               }

               this.internalWriteDescriptorWorkaround(var5, var3);
            }
         }

      }
   }

   @SuppressLint({"MissingPermission"})
   private final void internalEnableFACPNotifications() {
      BluetoothGattCharacteristic var1;
      BluetoothGattDescriptor var2;
      Unit var4;
      if ((var1 = this.mFACPCharacteristic) != null && (var2 = this.getCccd(var1)) != null) {
         BluetoothGatt var3;
         if ((var3 = this.mBluetoothGatt) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            var3 = null;
         }

         var3.setCharacteristicNotification(var1, true);
         MsgLogger.e("BLE internalEnable setCharacteristicNotification =>  " + var1);
         var2.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
         BluetoothGatt var5;
         if ((var5 = this.mBluetoothGatt) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            var5 = null;
         }

         this.internalWriteDescriptorWorkaround(var5, var2);
         var4 = Unit.INSTANCE;
      } else {
         var4 = null;
      }

      if (var4 == null) {
         Logger.i("没有FACP2.0的特征值");
         this.nextRequest(true);
      }

   }

   @SuppressLint({"MissingPermission"})
   private final void internalMtu(int var1) {
      if (VERSION.SDK_INT >= 21) {
         BluetoothGatt var2;
         if ((var2 = this.mBluetoothGatt) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            var2 = null;
         }

         var2.requestMtu(var1);
      } else {
         this.nextRequest(true);
      }

   }

   @SuppressLint({"MissingPermission"})
   private final void internalWriteFACPShake() {
      NoSuchElementException var10000;
      label43: {
         boolean var10001;
         BluetoothGattCharacteristic var6;
         try {
            var6 = this.mWriteCharacteristic;
         } catch (NoSuchElementException var5) {
            var10000 = var5;
            var10001 = false;
            break label43;
         }

         final BluetoothGattCharacteristic var1 = var6;
         if (var6 == null) {
            return;
         }

         try {
            var1.setValue((byte[])this.getMFacpQueue().remove());
         } catch (NoSuchElementException var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         GlobalScope var7;
         CoroutineDispatcher var8;
         try {
            var7 = GlobalScope.INSTANCE;
            var8 = Dispatchers.getIO();
         } catch (NoSuchElementException var3) {
            var10000 = var3;
            var10001 = false;
            break label43;
         }

         Object var10002 = null;

         try {
            BuildersKt.launch$default(var7, var8, (CoroutineStart)var10002, new Function2((Continuation)null) {
               public int a;

               @Nullable
               public final Object invokeSuspend(@NotNull Object var1x) {
                  Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  int var3;
                  if ((var3 = this.a) != 0 && var3 != 1) {
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  } else {
                     ResultKt.throwOnFailure(var1x);

                     do {
                        if (!BluetoothDeviceWrapper.this.mDeviceBusy) {
                           BluetoothGatt var4;
                           if ((var4 = BluetoothDeviceWrapper.this.mBluetoothGatt) == null) {
                              Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                              var4 = null;
                           }

                           var4.writeCharacteristic(var1);
                           return Unit.INSTANCE;
                        }

                        this.a = 1;
                     } while(DelayKt.delay(0L, this) != var2);

                     return var2;
                  }
               }

               @NotNull
               public final Continuation create(@Nullable Object var1x, @NotNull Continuation var2) {
                  <undefinedtype> var10002 = this;
                  BluetoothDeviceWrapper var3 = BluetoothDeviceWrapper.this;
                  return new <anonymous constructor>(var2);
               }

               @Nullable
               public final Object a(@NotNull CoroutineScope var1x, @Nullable Continuation var2) {
                  return ((<undefinedtype>)this.create(var1x, var2)).invokeSuspend(Unit.INSTANCE);
               }
            }, 2, (Object)null);
            return;
         } catch (NoSuchElementException var2) {
            var10000 = var2;
            var10001 = false;
         }
      }

      var10000.printStackTrace();
      this.nextRequest(true);
   }

   @SuppressLint({"MissingPermission"})
   private final void internalReadCharacteristic() {
      label71: {
         BluetoothGattCharacteristic var10000;
         boolean var10001;
         try {
            var10000 = (BluetoothGattCharacteristic)this.getMReadQueue().remove();
         } catch (NoSuchElementException var11) {
            var10001 = false;
            break label71;
         }

         BluetoothGattCharacteristic var1 = var10000;

         int var12;
         try {
            var12 = var10000.getProperties();
         } catch (NoSuchElementException var10) {
            var10001 = false;
            break label71;
         }

         if ((var12 & 2) != 0) {
            label72: {
               BluetoothGatt var13;
               try {
                  var13 = this.mBluetoothGatt;
               } catch (NoSuchElementException var6) {
                  var10001 = false;
                  break label72;
               }

               BluetoothGatt var2 = var13;
               if (var13 == null) {
                  try {
                     Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                  } catch (NoSuchElementException var5) {
                     var10001 = false;
                     break label72;
                  }

                  var2 = null;
               }

               BluetoothDeviceWrapper var14;
               try {
                  var14 = this;
                  var2.readCharacteristic(var1);
               } catch (NoSuchElementException var4) {
                  var10001 = false;
                  break label72;
               }

               try {
                  var14.nextRequest(true);
                  return;
               } catch (NoSuchElementException var3) {
                  var10001 = false;
               }
            }
         } else {
            label60: {
               Request var15;
               try {
                  var15 = this.mRequest;
               } catch (NoSuchElementException var9) {
                  var10001 = false;
                  break label60;
               }

               if (var15 != null) {
                  try {
                     this.nextRequest(false);
                     return;
                  } catch (NoSuchElementException var7) {
                     var10001 = false;
                  }
               } else {
                  try {
                     this.nextRequest(true);
                     return;
                  } catch (NoSuchElementException var8) {
                     var10001 = false;
                  }
               }
            }
         }
      }

      this.nextRequest(true);
   }

   @SuppressLint({"MissingPermission"})
   private final void internalSendEncrypt() {
      BluetoothGattCharacteristic var1;
      Unit var3;
      if ((var1 = this.mWriteCharacteristic) != null) {
         BluetoothDeviceWrapper var10000 = this;
         var1.setValue(this.mSendPasswordInfo);
         StringBuilder var4 = (new StringBuilder()).append("BLE internalSendEncrypt => ");
         BluetoothGatt var2;
         if ((var2 = var10000.mBluetoothGatt) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            var2 = null;
         }

         MsgLogger.e(var4.append(var2.writeCharacteristic(var1)).toString());
         var3 = Unit.INSTANCE;
      } else {
         var3 = null;
      }

      if (var3 == null) {
         MsgLogger.i("BLE internalSendEncrypt writeCharacteristic is null");
      }

   }

   @SuppressLint({"MissingPermission"})
   private final void internalAt() {
      BluetoothGattCharacteristic var1;
      Unit var3;
      if ((var1 = this.mWriteCharacteristic) != null) {
         BluetoothDeviceWrapper var10000 = this;
         var1.setValue("$OpenFscAtEngine$");
         StringBuilder var4 = (new StringBuilder()).append("BLE internalAt => ");
         BluetoothGatt var2;
         if ((var2 = var10000.mBluetoothGatt) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
            var2 = null;
         }

         MsgLogger.e(var4.append(var2.writeCharacteristic(var1)).toString());
         var3 = Unit.INSTANCE;
      } else {
         var3 = null;
      }

      if (var3 == null) {
         MsgLogger.i("BLE internalAt writeCharacteristic is null");
      }

   }

   @SuppressLint({"MissingPermission"})
   private final void internalOTA() {
      OtaUtils.Companion var1 = OtaUtils.Companion;
      byte[] var2;
      if ((var2 = this.mDfuFile) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mDfuFile");
         var2 = null;
      }

      DfuFileInfo var4;
      if ((var4 = var1.checkDfuFile(var2)) != null) {
         final Integer var5 = var4.versionStart;
         final int var3 = var4.type_model;
         MsgLogger.e("BLE internalOTA mModuleVersion => " + this.mModuleVersion + "   versionStart => " + var4.versionStart);
         if (this.mModuleVersion < var4.versionStart) {
            this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().otaProgressUpdate(this.mAddress, 0.0F, XmodemUtils.OTA_STATUS_VERIFY_FAILED);
         } else {
            MsgLogger.e("BLE internalOTA: EnterDFU" + (this.mReset ^ 1));
            this.mOTACallbacks = new FscBleCentralCallbacks() {
               @SuppressLint({"MissingPermission"})
               public void packetReceived(@Nullable String var1, @NotNull String var2, @Nullable String var3x, @Nullable byte[] var4) {
                  Intrinsics.checkNotNullParameter(var2, "strValue");
                  MsgLogger.e("BLE internalOTA packetReceived strValue =>  " + var2);
                  if (var2.length() >= 15) {
                     String var10002 = var2.substring(3, 7);
                     Intrinsics.checkNotNullExpressionValue(var10002, "this as java.lang.String…ing(startIndex, endIndex)");
                     int var5x = FileUtil.stringToInt(var10002);
                     String var10001 = var2.substring(7, 11);
                     Intrinsics.checkNotNullExpressionValue(var10001, "this as java.lang.String…ing(startIndex, endIndex)");
                     FileUtil.stringToInt(var10001);
                     String var10000 = var2.substring(11, 15);
                     Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String…ing(startIndex, endIndex)");
                     int var9 = FileUtil.stringToInt(var10000);
                     StringBuilder var12 = (new StringBuilder()).append("BLE internalOTA  模块型号 => ");
                     String var10003 = var2.substring(11, 15);
                     Intrinsics.checkNotNullExpressionValue(var10003, "this as java.lang.String…ing(startIndex, endIndex)");
                     MsgLogger.e(var12.append(FileUtil.stringToInt(var10003)).toString());
                     if (var9 != var3) {
                        MsgLogger.e("BLE internalOTA => 模块型号不符");
                        BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().otaProgressUpdate(BluetoothDeviceWrapper.this.getMAddress(), 0.0F, XmodemUtils.OTA_STATUS_VERIFY_MODEL_FAILED);
                        BluetoothDeviceWrapper.this.disconnect();
                        BluetoothDeviceWrapper.this.setMOTACallbacks$FeasyBlueLibrary_release((FscBleCentralCallbacks)null);
                        return;
                     }

                     Integer var11 = var5;
                     Intrinsics.checkNotNullExpressionValue(var11, "dfuVersion");
                     if (var5x < var11.intValue()) {
                        MsgLogger.e("BLE internalOTA => App版本过低");
                        BluetoothDeviceWrapper.this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().otaProgressUpdate(BluetoothDeviceWrapper.this.getMAddress(), 0.0F, XmodemUtils.OTA_STATUS_VERIFY_APP_VERSION_FAILED);
                        BluetoothDeviceWrapper.this.disconnect();
                        BluetoothDeviceWrapper.this.setMOTACallbacks$FeasyBlueLibrary_release((FscBleCentralCallbacks)null);
                        return;
                     }

                     OtaUtils.Companion var6 = OtaUtils.Companion;
                     byte[] var8;
                     if ((var8 = BluetoothDeviceWrapper.this.mDfuFile) == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mDfuFile");
                        var8 = null;
                     }

                     if (var6.needsReconnect(var8)) {
                        GlobalScope var10 = GlobalScope.INSTANCE;
                        Function2 var7;
                        var7 = new Function2() {
                           public int a;

                           @Nullable
                           public final Object invokeSuspend(@NotNull Object var1) {
                              label30: {
                                 Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                 int var3x;
                                 if ((var3x = this.a) != 0) {
                                    if (var3x != 1) {
                                       if (var3x != 2) {
                                          throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                       }

                                       ResultKt.throwOnFailure(var1);
                                       break label30;
                                    }

                                    ResultKt.throwOnFailure(var1);
                                 } else {
                                    ResultKt.throwOnFailure(var1);
                                    this.a = 1;
                                    if (DelayKt.delay(3000L, this) == var2) {
                                       return var2;
                                    }
                                 }

                                 if (!null.super.mIsConnect) {
                                    BluetoothGatt var4;
                                    if ((var4 = null.super.mBluetoothGatt) == null) {
                                       Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                                       var4 = null;
                                    }

                                    var4.connect();
                                    this.a = 2;
                                    if (DelayKt.delay(2000L, this) == var2) {
                                       return var2;
                                    }
                                 }
                              }

                              null.super.bindService();
                              return Unit.INSTANCE;
                           }

                           @NotNull
                           public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
                              return new <anonymous constructor>(var2);
                           }

                           @Nullable
                           public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
                              return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                           }
                        }.<init>((Continuation)null);
                        BuildersKt.launch$default(var10, (CoroutineContext)null, (CoroutineStart)null, var7, 3, (Object)null);
                        return;
                     }
                  }

                  BluetoothDeviceWrapper.this.bindService();
               }
            };
            byte[] var10001 = ("EnterDFU" + (this.mReset ^ 1)).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(var10001, "this as java.lang.String).getBytes(charset)");
            this.send(var10001);
         }
      }
   }

   private final void bindService() {
      TeaCode var1;
      var1 = new TeaCode.<init>();
      byte[] var2;
      if ((var2 = this.mDfuFile) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mDfuFile");
         var2 = null;
      }

      byte[] var4;
      byte[] var10001 = var4 = var1.feasycom_decryption(var2);
      var2 = new byte[var10001.length - 1024];
      int var3 = var10001.length - 1024;
      System.arraycopy(var10001, 1024, var2, 0, var3);
      Intent var5;
      Intent var6 = var5 = new Intent;
      var5.<init>();
      var5.putExtra("fileByte", var4);
      var5.putExtra("fileByteNoCheck", var2);
      var6.setClass(this.mContext, OTAService.class);
      this.mContext.getApplicationContext().bindService(var5, this.mServiceConnection, 1);
   }

   private final BluetoothGattDescriptor getCccd(BluetoothGattCharacteristic var1) {
      if (var1 == null) {
         return null;
      } else {
         return (var1.getProperties() & 16) == 0 ? null : var1.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
      }
   }

   @SuppressLint({"MissingPermission"})
   private final void internalWriteDescriptorWorkaround(BluetoothGatt var1, BluetoothGattDescriptor var2) {
      BluetoothGattCharacteristic var3;
      BluetoothGattCharacteristic var10000 = var3 = var2.getCharacteristic();
      int var4 = var10000.getWriteType();
      var10000.setWriteType(2);
      GlobalScope var6 = GlobalScope.INSTANCE;
      Function2 var5;
      var5 = new Function2() {
         public int a;
         // $FF: synthetic field
         public final BluetoothGatt c;
         // $FF: synthetic field
         public final BluetoothGattDescriptor d;
         // $FF: synthetic field
         public final BluetoothGattCharacteristic e;
         // $FF: synthetic field
         public final int f;

         public {
            this.c = var2;
            this.d = var3;
            this.e = var4;
            this.f = var5;
         }

         @Nullable
         public final Object invokeSuspend(@NotNull Object var1) {
            Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int var3;
            if ((var3 = this.a) != 0 && var3 != 1) {
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            } else {
               ResultKt.throwOnFailure(var1);

               do {
                  if (!BluetoothDeviceWrapper.this.mDeviceBusy) {
                     this.c.writeDescriptor(this.d);
                     this.e.setWriteType(this.f);
                     return Unit.INSTANCE;
                  }

                  this.a = 1;
               } while(DelayKt.delay(0L, this) != var2);

               return var2;
            }
         }

         @NotNull
         public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
            <undefinedtype> var10002 = this;
            <undefinedtype> var10003 = this;
            <undefinedtype> var10004 = this;
            <undefinedtype> var10005 = this;
            BluetoothDeviceWrapper var6 = BluetoothDeviceWrapper.this;
            BluetoothGatt var7 = var10005.c;
            BluetoothGattDescriptor var3 = var10004.d;
            BluetoothGattCharacteristic var4 = var10003.e;
            int var5 = var10002.f;
            return new <anonymous constructor>(var7, var3, var4, var5, var2);
         }

         @Nullable
         public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
            return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
         }
      }.<init>(var1, var2, var3, var4, (Continuation)null);
      BuildersKt.launch$default(var6, (CoroutineContext)null, (CoroutineStart)null, var5, 3, (Object)null);
   }

   @SuppressLint({"MissingPermission"})
   private final void internalSendATCommand() {
      String var1;
      label30: {
         String var10000;
         try {
            var10000 = (String)this.getMAtQueue().remove();
         } catch (NoSuchElementException var4) {
            var1 = null;
            break label30;
         }

         var1 = var10000;
      }

      this.mCommand = var1;
      BluetoothGattCharacteristic var2;
      Unit var5;
      if ((var2 = this.mWriteCharacteristic) != null) {
         if (var1 != null) {
            GlobalScope var10001 = GlobalScope.INSTANCE;
            Function2 var3;
            var3 = new Function2() {
               public int a;
               // $FF: synthetic field
               public final BluetoothGattCharacteristic c;
               // $FF: synthetic field
               public final String d;

               public {
                  this.c = var2;
                  this.d = var3;
               }

               @Nullable
               public final Object invokeSuspend(@NotNull Object var1) {
                  Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  int var3;
                  if ((var3 = this.a) != 0 && var3 != 1) {
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  } else {
                     ResultKt.throwOnFailure(var1);

                     do {
                        if (!BluetoothDeviceWrapper.this.mDeviceBusy) {
                           BluetoothGattCharacteristic var10001 = this.c;
                           byte[] var10002 = (this.d + "\r\n").getBytes(Charsets.UTF_8);
                           Intrinsics.checkNotNullExpressionValue(var10002, "this as java.lang.String).getBytes(charset)");
                           var10001.setValue(var10002);
                           BluetoothGatt var4;
                           if ((var4 = BluetoothDeviceWrapper.this.mBluetoothGatt) == null) {
                              Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                              var4 = null;
                           }

                           var4.writeCharacteristic(this.c);
                           return Unit.INSTANCE;
                        }

                        this.a = 1;
                     } while(DelayKt.delay(0L, this) != var2);

                     return var2;
                  }
               }

               @NotNull
               public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
                  <undefinedtype> var10002 = this;
                  <undefinedtype> var10003 = this;
                  BluetoothDeviceWrapper var4 = BluetoothDeviceWrapper.this;
                  BluetoothGattCharacteristic var5 = var10003.c;
                  String var3 = var10002.d;
                  return new <anonymous constructor>(var5, var3, var2);
               }

               @Nullable
               public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
                  return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
               }
            }.<init>(var2, var1, (Continuation)null);
            this.mSendJob = BuildersKt.launch$default(var10001, (CoroutineContext)null, (CoroutineStart)null, var3, 3, (Object)null);
            var5 = Unit.INSTANCE;
         } else {
            var5 = null;
         }

         if (var5 == null) {
            this.getMFscBleCentralCallbacks$FeasyBlueLibrary_release().endATCommand();
         }

         var5 = Unit.INSTANCE;
      } else {
         var5 = null;
      }

      if (var5 == null) {
         MsgLogger.i("BLE internalSendATCommand writeCharacteristic is null");
      }

   }

   private final void enqueue() {
      Request var1;
      label16: {
         Request var10000;
         try {
            var10000 = (Request)this.getMRequests().remove();
         } catch (NoSuchElementException var2) {
            var1 = null;
            break label16;
         }

         var1 = var10000;
      }

      this.mRequest = var1;
      if (var1 != null) {
         MsgLogger.i("BLE enqueue request type => " + var1.getType());
      }

      this.nextRequest(false);
   }

   @SuppressLint({"MissingPermission"})
   private final void nextRequest(boolean var1) {
      Request var2;
      if ((var2 = this.mRequest) != null) {
         if (var1) {
            this.enqueue();
         } else {
            switch (BluetoothDeviceWrapper.WhenMappings.$EnumSwitchMapping$0[var2.getType().ordinal()]) {
               case 1:
                  this.internalConnect();
                  break;
               case 2:
                  this.internalEnableNotifications();
                  break;
               case 3:
                  this.internalMtu(this.mInitMtu);
                  break;
               case 4:
                  this.internalReadCharacteristic();
                  break;
               case 5:
                  BluetoothDeviceWrapper var10000 = this;
                  Handler var10001 = this.handler;
                  BluetoothDeviceWrapper var10002 = this;
                  Runnable var4 = this.mConnectRunnable;
                  long var5 = var10002.mTimeOut;
                  var10001.postDelayed(var4, var5);
                  var10000.internalSendEncrypt();
                  break;
               case 6:
                  this.internalAt();
                  break;
               case 7:
                  this.internalEnableFACPNotifications();
                  break;
               case 8:
                  this.internalWriteFACPShake();
                  break;
               case 9:
                  this.internalDisconnect();
                  break;
               case 10:
                  this.mIsConnect = true;
                  this.internalOTA();
            }
         }
      } else {
         this.mIsConnect = true;
         MsgLogger.e("BLE 连接时间为 => " + (System.currentTimeMillis() - this.mStartTimer));
         this.handler.post(this.rssiRunnable);
         this.handler.postDelayed(new BluetoothDeviceWrapper$nextRequest$lambda-21$$inlined$Runnable$1(this), 0L);
      }

   }

   private final String parseMacAddress(byte[] var1) {
      if (var1.length >= 6) {
         <undefinedtype> var2 = null.a;
         return ArraysKt.joinToString$default(var1, ":", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, var2, 30, (Object)null);
      } else {
         return "Invalid MAC Address";
      }
   }

   @SuppressLint({"MissingPermission"})
   private final void getSupportedServices(BluetoothGatt var1) {
      if (this.mBluetoothGattServices.size() > 0) {
         this.mBluetoothGattServices.clear();
      }

      BluetoothGatt var2;
      if ((var2 = this.mBluetoothGatt) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var2 = null;
      }

      List var10000 = var2.getServices();
      Intrinsics.checkNotNullExpressionValue(var10000, "mBluetoothGatt.services");
      ArrayList var9;
      var9 = new ArrayList.<init>();
      Iterator var3 = var10000.iterator();

      while(var3.hasNext()) {
         Object var4;
         String var10001 = ((BluetoothGattService)(var4 = var3.next())).getUuid().toString();
         Intrinsics.checkNotNullExpressionValue(var10001, "it.uuid.toString()");
         String var5;
         Intrinsics.checkNotNullExpressionValue(var5 = var10001.substring(4, 8), "this as java.lang.String…ing(startIndex, endIndex)");
         if (this.mServiceFilterList.contains(var5) ^ true) {
            var9.add(var4);
         }
      }

      this.mBluetoothGattServices.addAll(var9);
      BluetoothGattService var10 = var1.getService(SERVICE_UUID);
      BluetoothGattService var11;
      Queue var18;
      List var19;
      if ((var11 = var1.getService(GATT_DEVICE_INFO_UUID)) != null) {
         Intrinsics.checkNotNullExpressionValue(var11, "getService(GATT_DEVICE_INFO_UUID)");
         var18 = this.getMReadQueue();
         var19 = var11.getCharacteristics();
         Intrinsics.checkNotNullExpressionValue(var19, "it.characteristics");
         var18.addAll(var19);
      }

      if ((var11 = var1.getService(GAP_SERVICE_UUID)) != null) {
         Intrinsics.checkNotNullExpressionValue(var11, "getService(GAP_SERVICE_UUID)");
         var18 = this.getMReadQueue();
         var19 = var11.getCharacteristics();
         Intrinsics.checkNotNullExpressionValue(var19, "it.characteristics");
         var18.addAll(var19);
      }

      if (var10 == null) {
         MsgLogger.d("BLE getSupportedServices => 不包含 FF0");
         var10000 = var1.getServices();
         Intrinsics.checkNotNullExpressionValue(var10000, "gatt.services");
         Iterator var8 = var10000.iterator();

         while(var8.hasNext()) {
            if (Intrinsics.areEqual(BleNamesResolver.resolveServiceName((var10 = (BluetoothGattService)var8.next()).getUuid().toString()), "Unknown")) {
               var10000 = var10.getCharacteristics();
               Intrinsics.checkNotNullExpressionValue(var10000, "service.characteristics");
               ArrayList var12;
               var12 = new ArrayList.<init>();
               Iterator var14 = var10000.iterator();

               Object var17;
               while(var14.hasNext()) {
                  if ((((BluetoothGattCharacteristic)(var17 = var14.next())).getProperties() & 4) != 0) {
                     var12.add(var17);
                  }
               }

               if (var12.isEmpty() ^ true) {
                  MsgLogger.d("BLE getSupportedServices 有写的特征值 => " + ((BluetoothGattCharacteristic)var12.get(0)).getUuid() + "   " + var10.getUuid());
                  this.mWriteCharacteristic = (BluetoothGattCharacteristic)var12.get(0);
               }

               var10000 = var10.getCharacteristics();
               Intrinsics.checkNotNullExpressionValue(var10000, "service.characteristics");
               var12 = new ArrayList.<init>();
               var14 = var10000.iterator();

               while(var14.hasNext()) {
                  if ((((BluetoothGattCharacteristic)(var17 = var14.next())).getProperties() & 16) != 0) {
                     var12.add(var17);
                  }
               }

               if (var12.isEmpty() ^ true) {
                  MsgLogger.d("BLE getSupportedServices 有读的特征值 => " + ((BluetoothGattCharacteristic)var12.get(0)).getUuid() + "   " + var10.getUuid());
                  var10000 = this.mNotifyCharacteristicList;
                  Object var20 = var12.get(0);
                  Intrinsics.checkNotNullExpressionValue(var20, "it[0]");
                  var10000.add(var20);
               }
            }

            if (this.mWriteCharacteristic != null && this.mNotifyCharacteristicList.isEmpty() ^ true) {
               return;
            }
         }

         if (this.mNotifyCharacteristicList.isEmpty()) {
            this.mWriteCharacteristic = null;
         }
      } else {
         MsgLogger.e("BLE getSupportedServices 包含 FFF0");
         UUID var15;
         BluetoothGattCharacteristic var16;
         if ((var16 = var10.getCharacteristic(var15 = WRITE_UUID)) != null) {
            MsgLogger.e("BLE getSupportedServices 找到写UUID => " + var15);
            this.mWriteCharacteristic = var16;
         }

         if ((var16 = var10.getCharacteristic(var15 = NOTIFY_UUID)) != null) {
            MsgLogger.e("BLE getSupportedServices 找到读UUID => " + var15);
            this.mNotifyCharacteristicList.add(var16);
         }

         if ((var16 = var10.getCharacteristic(var15 = FACP_UUID)) != null) {
            MsgLogger.e("BLE getSupportedServices 找到FACP_UUID => " + var15);
            this.mFACPCharacteristic = var16;
         }

         UUID var7;
         BluetoothGattCharacteristic var13;
         if ((var13 = var10.getCharacteristic(var7 = MAC_UUID)) != null) {
            MsgLogger.e("BLE getSupportedServices 找到读MAC UUID => " + var7);
            var1.readCharacteristic(var13);
         }
      }

   }

   private final boolean isCCCD(BluetoothGattDescriptor var1) {
      return var1 != null && Intrinsics.areEqual(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID, var1.getUuid());
   }

   // $FF: synthetic method
   public static BluetoothDeviceWrapper copy$default(BluetoothDeviceWrapper var0, String var1, BluetoothDevice var2, Context var3, ConnectCallback var4, int var5, Object var6) {
      if ((var5 & 1) != 0) {
         var1 = var0.mAddress;
      }

      if ((var5 & 2) != 0) {
         var2 = var0.device;
      }

      if ((var5 & 4) != 0) {
         var3 = var0.mContext;
      }

      if ((var5 & 8) != 0) {
         var4 = var0.connectCallback;
      }

      return var0.copy(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public static final ConnectType access$getMConnectType$p(BluetoothDeviceWrapper var0) {
      return var0.mConnectType;
   }

   // $FF: synthetic method
   public static final boolean access$getMPauseSend$p(BluetoothDeviceWrapper var0) {
      return var0.mPauseSend;
   }

   // $FF: synthetic method
   public static final BluetoothGattCharacteristic access$getMWriteCharacteristic$p(BluetoothDeviceWrapper var0) {
      return var0.mWriteCharacteristic;
   }

   // $FF: synthetic method
   public static final boolean access$getFACP2$p(BluetoothDeviceWrapper var0) {
      return var0.FACP2;
   }

   // $FF: synthetic method
   public static final FscEncryptApi access$getMFscEncryptApiImp$p(BluetoothDeviceWrapper var0) {
      return var0.mFscEncryptApiImp;
   }

   // $FF: synthetic method
   public static final byte[] access$getMSendPasswordInfo$p(BluetoothDeviceWrapper var0) {
      return var0.mSendPasswordInfo;
   }

   // $FF: synthetic method
   public static final byte[] access$getMReceivePasswordInfo$p(BluetoothDeviceWrapper var0) {
      return var0.mReceivePasswordInfo;
   }

   // $FF: synthetic method
   public static final void access$setMReceivePasswordInfo$p(BluetoothDeviceWrapper var0, byte[] var1) {
      var0.mReceivePasswordInfo = var1;
   }

   @NotNull
   public final String getMAddress() {
      return this.mAddress;
   }

   @NotNull
   public final BluetoothDevice getDevice() {
      return this.device;
   }

   @NotNull
   public final Context getMContext() {
      return this.mContext;
   }

   @NotNull
   public final ConnectCallback getConnectCallback() {
      return this.connectCallback;
   }

   public final void setConnectCallback(@NotNull ConnectCallback var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.connectCallback = var1;
   }

   public final long getMSendInterval() {
      return this.mSendInterval;
   }

   public final void setMSendInterval(long var1) {
      this.mSendInterval = var1;
   }

   public final int getMRealMtu() {
      return this.mRealMtu;
   }

   public final void setMRealMtu(int var1) {
      this.mRealMtu = var1;
   }

   public final int getMModuleVersion() {
      return this.mModuleVersion;
   }

   public final void setMModuleVersion(int var1) {
      this.mModuleVersion = var1;
   }

   @NotNull
   public final FscBleCentralCallbacks getMFscBleCentralCallbacks$FeasyBlueLibrary_release() {
      FscBleCentralCallbacks var1;
      if ((var1 = this.mFscBleCentralCallbacks) != null) {
         return var1;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("mFscBleCentralCallbacks");
         return null;
      }
   }

   public final void setMFscBleCentralCallbacks$FeasyBlueLibrary_release(@NotNull FscBleCentralCallbacks var1) {
      Intrinsics.checkNotNullParameter(var1, "<set-?>");
      this.mFscBleCentralCallbacks = var1;
   }

   public final long getMStartTimer() {
      return this.mStartTimer;
   }

   public final void setMStartTimer(long var1) {
      this.mStartTimer = var1;
   }

   @NotNull
   public final Runnable getMConnectRunnable() {
      return this.mConnectRunnable;
   }

   @Nullable
   public final FscBleCentralCallbacks getMOTACallbacks$FeasyBlueLibrary_release() {
      return this.mOTACallbacks;
   }

   public final void setMOTACallbacks$FeasyBlueLibrary_release(@Nullable FscBleCentralCallbacks var1) {
      this.mOTACallbacks = var1;
   }

   public final boolean connect(@NotNull FscBleCentralCallbacks var1, boolean var2) {
      Intrinsics.checkNotNullParameter(var1, "fscBleCentralCallbacks");
      this.mConnectType = ConnectType.CONNECT;
      this.setMFscBleCentralCallbacks$FeasyBlueLibrary_release(var1);
      this.mStartTimer = System.currentTimeMillis();
      this.initQueue();
      this.getMRequests().add(new Request(Type.READ));
      if (var2) {
         this.getMRequests().add(new Request(Type.FACP));
      }

      this.enqueue();
      return true;
   }

   public final boolean connectToModify(@NotNull FscBleCentralCallbacks var1) {
      Intrinsics.checkNotNullParameter(var1, "fscBleCentralCallbacks");
      this.mConnectType = ConnectType.MODIFY;
      MsgLogger.e("============BLE 参数修改连接=============");
      this.mStartTimer = System.currentTimeMillis();
      this.setMFscBleCentralCallbacks$FeasyBlueLibrary_release(var1);
      this.initQueue();
      this.getMRequests().add(new Request(Type.READ));
      this.getMRequests().add(new Request(Type.AT));
      this.enqueue();
      return true;
   }

   public final boolean connectToOTAWithFactory(@NotNull FscBleCentralCallbacks var1, @NotNull byte[] var2, boolean var3, int var4) {
      BluetoothDeviceWrapper var10000 = this;
      BluetoothDeviceWrapper var10001 = this;
      BluetoothDeviceWrapper var10002 = this;
      BluetoothDeviceWrapper var10003 = this;
      BluetoothDeviceWrapper var10004 = this;
      FscBleCentralCallbacks var10005 = var1;
      BluetoothDeviceWrapper var10006 = this;
      byte[] var10007 = var2;
      BluetoothDeviceWrapper var10008 = this;
      boolean var10009 = var3;
      BluetoothDeviceWrapper var10010 = this;
      int var10011 = var4;
      BluetoothDeviceWrapper var10012 = this;
      BluetoothDeviceWrapper var10013 = this;
      Intrinsics.checkNotNullParameter(var1, "fscBleCentralCallbacks");
      Intrinsics.checkNotNullParameter(var2, "dfuFile");
      MsgLogger.e("============BLE 空中升级连接=============");

      NullPointerException var8;
      boolean var9;
      try {
         var10013.mConnectType = ConnectType.OTA;
         OtaUtils.Companion.needsReconnect(var2);
      } catch (NullPointerException var7) {
         var8 = var7;
         var9 = false;
         throw var8;
      }

      try {
         var10012.mStartTimer = System.currentTimeMillis();
         var10010.mModuleVersion = var10011;
         var10008.mReset = var10009;
         var10006.mDfuFile = var10007;
         var10004.setMFscBleCentralCallbacks$FeasyBlueLibrary_release(var10005);
         var10003.initQueue();
         var10002.getMRequests().add(new Request(Type.OTA));
      } catch (NullPointerException var6) {
         var8 = var6;
         var9 = false;
         throw var8;
      }

      try {
         var10001.mInitMtu = 100;
         var10000.enqueue();
         return true;
      } catch (NullPointerException var5) {
         var8 = var5;
         var9 = false;
         throw var8;
      }
   }

   public final void disconnect() {
      if (this.getMRequests().size() != 0) {
         this.getMRequests().clear();
         this.getMRequests().add(new Request(Type.DISCONNECT));
         this.nextRequest(true);
      } else {
         this.internalDisconnect();
      }

   }

   @SuppressLint({"MissingPermission", "ObsoleteSdkInt", "SupportAnnotationUsage"})
   @RequiresApi(21)
   public final void requestMtu(int var1) {
      BluetoothGatt var2;
      if ((var2 = this.mBluetoothGatt) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var2 = null;
      }

      var2.requestMtu(var1);
   }

   @NotNull
   public final List getBluetoothGattServices() {
      return this.mBluetoothGattServices;
   }

   @Nullable
   public final BluetoothGattCharacteristic getWriteCharacteristic() {
      return this.mWriteCharacteristic;
   }

   @NotNull
   public final List getNotifyCharacteristicList() {
      return this.mNotifyCharacteristicList;
   }

   public final int getMaximumPacketByte() {
      return this.mMaximumPacketByte;
   }

   public final void setCharacteristic(@NotNull FscBleCentralCallbacks var1) {
      Intrinsics.checkNotNullParameter(var1, "fscBleCentralCallbacks");
      this.setMFscBleCentralCallbacks$FeasyBlueLibrary_release(var1);
   }

   public final boolean setCharacteristic(@NotNull BluetoothGattCharacteristic var1, int var2) {
      Intrinsics.checkNotNullParameter(var1, "ch");
      BluetoothDeviceWrapper var10000;
      byte[] var3;
      boolean var4;
      switch (var2) {
         case 1:
            if ((var1.getProperties() & 4) != 0) {
               var1.setWriteType(1);
               this.mWriteCharacteristic = var1;
               var4 = true;
               return var4;
            }
            break;
         case 2:
            if ((var1.getProperties() & 8) != 0) {
               var1.setWriteType(2);
               Logger.e(String.valueOf(var1.getUuid()));
               this.mWriteCharacteristic = var1;
               var4 = true;
               return var4;
            }
            break;
         case 3:
            if ((var1.getProperties() & 16) != 0) {
               var10000 = this;
               Intrinsics.checkNotNullExpressionValue(var3 = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, "ENABLE_NOTIFICATION_VALUE");
               var4 = var10000.updateGattDescriptor(var1, true, var3);
               return var4;
            }
            break;
         case 4:
            if ((var1.getProperties() & 16) == 0 && this.mNotifyCharacteristicList.size() <= 1) {
               break;
            }

            var10000 = this;
            Intrinsics.checkNotNullExpressionValue(var3 = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, "DISABLE_NOTIFICATION_VALUE");
            var4 = var10000.updateGattDescriptor(var1, false, var3);
            return var4;
         case 5:
            if ((var1.getProperties() & 32) != 0) {
               var10000 = this;
               Intrinsics.checkNotNullExpressionValue(var3 = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE, "ENABLE_INDICATION_VALUE");
               var4 = var10000.updateGattDescriptor(var1, true, var3);
               return var4;
            }
            break;
         case 6:
            var10000 = this;
            Intrinsics.checkNotNullExpressionValue(var3 = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, "DISABLE_NOTIFICATION_VALUE");
            var4 = var10000.updateGattDescriptor(var1, false, var3);
            return var4;
      }

      var4 = false;
      return var4;
   }

   @SuppressLint({"MissingPermission"})
   public final void read(@NotNull BluetoothGattCharacteristic var1) {
      Intrinsics.checkNotNullParameter(var1, "ch");
      BluetoothGatt var2;
      if ((var2 = this.mBluetoothGatt) == null) {
         Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
         var2 = null;
      }

      var2.readCharacteristic(var1);
      Object var5;
      Object var10000 = var5 = this.mDeviceBusyLock;
      BluetoothDeviceWrapper var10001 = this;
      synchronized(var5){}

      try {
         var10001.mDeviceBusy = true;
      } catch (Throwable var4) {
         throw var4;
      }

   }

   public final void setSendInterval(long var1) {
      this.mSendInterval = var1;
   }

   public final boolean sendFile(int var1) {
      GlobalScope var10000 = GlobalScope.INSTANCE;
      Function2 var2;
      var2 = new Function2() {
         public int a;
         // $FF: synthetic field
         public final BluetoothDeviceWrapper c;

         public {
            this.c = var2;
         }

         @Nullable
         public final Object invokeSuspend(@NotNull Object var1) {
            Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int var3;
            if ((var3 = this.a) != 0) {
               if (var3 != 1) {
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               ResultKt.throwOnFailure(var1);
            } else {
               ResultKt.throwOnFailure(var1);
               int var10000 = BluetoothDeviceWrapper.this;
               this.a = 1;
               if ((var1 = FileUtilsKt.createTestData(var10000, this)) == var2) {
                  return var2;
               }
            }

            <undefinedtype> var4 = this;
            ByteArrayInputStream var5 = (ByteArrayInputStream)var1;
            var4.c.sendFile((InputStream)var5);
            return Unit.INSTANCE;
         }

         @NotNull
         public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
            <undefinedtype> var10002 = this;
            int var3 = BluetoothDeviceWrapper.this;
            return new <anonymous constructor>(var10002.c, var2);
         }

         @Nullable
         public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
            return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
         }
      }.<init>(this, (Continuation)null);
      BuildersKt.launch$default(var10000, (CoroutineContext)null, (CoroutineStart)null, var2, 3, (Object)null);
      return true;
   }

   public final boolean sendFile(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "bytes");
      if (this.mWriteCharacteristic != null) {
         BluetoothDeviceWrapper var10000 = this;
         ByteArrayInputStream var2;
         var2 = new ByteArrayInputStream.<init>(var1);
         var10000.sendFile((InputStream)var2);
         return true;
      } else {
         MsgLogger.i("BLE sendFile writeCharacteristic is null");
         return false;
      }
   }

   public final boolean getWriteSuccess() {
      return this.writeSuccess;
   }

   public final void setWriteSuccess(boolean var1) {
      this.writeSuccess = var1;
   }

   @SuppressLint({"MissingPermission", "NewApi"})
   public final boolean sendFile(@NotNull InputStream var1) {
      Intrinsics.checkNotNullParameter(var1, "inputStream");
      BluetoothGattCharacteristic var2;
      if ((var2 = this.mWriteCharacteristic) != null) {
         Job var3;
         if ((var3 = this.mSendJob) != null) {
            if (var3 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
               var3 = null;
            }

            if (!var3.isCompleted()) {
               if ((var3 = this.mSendJob) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
                  var3 = null;
               }

               if (!var3.isCancelled()) {
                  Job var4;
                  if ((var4 = this.mSendJob) == null) {
                     Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
                     var4 = null;
                  }

                  DefaultImpls.cancel$default(var4, (CancellationException)null, 1, (Object)null);
                  return false;
               }
            }
         }

         GlobalScope var10002 = GlobalScope.INSTANCE;
         CoroutineDispatcher var10003 = Dispatchers.getIO();
         Function2 var5;
         var5 = new Function2() {
            public Object a;
            public Object b;
            public Object c;
            public Object d;
            public Object e;
            public Object f;
            public int g;
            public int h;
            public int i;
            // $FF: synthetic field
            public Object j;
            // $FF: synthetic field
            public final BluetoothDeviceWrapper l;
            // $FF: synthetic field
            public final BluetoothGattCharacteristic m;

            public {
               this.l = var2;
               this.m = var3;
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object param1) {
               // $FF: Couldn't be decompiled
            }

            @NotNull
            public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
               <undefinedtype> var10003 = this;
               <undefinedtype> var10004 = this;
               InputStream var5 = BluetoothDeviceWrapper.this;
               BluetoothDeviceWrapper var3 = var10004.l;
               BluetoothGattCharacteristic var4 = var10003.m;
               Function2 var10000 = new <anonymous constructor>(var3, var4, var2);
               var10000.j = var1;
               return var10000;
            }

            @Nullable
            public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
               return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }.<init>(this, var2, (Continuation)null);
         this.mSendJob = BuildersKt.launch$default(var10002, var10003, (CoroutineStart)null, var5, 2, (Object)null);
         return this.writeSuccess;
      } else {
         return false;
      }
   }

   @SuppressLint({"MissingPermission"})
   public final boolean send(@NotNull byte[] var1) {
      Intrinsics.checkNotNullParameter(var1, "bytes");
      BluetoothGattCharacteristic var2;
      if ((var2 = this.mWriteCharacteristic) != null) {
         var2.setValue(var1);
         GlobalScope var10000 = GlobalScope.INSTANCE;
         CoroutineDispatcher var10001 = Dispatchers.getIO();
         Function2 var3;
         var3 = new Function2() {
            public int a;
            // $FF: synthetic field
            public final BluetoothGattCharacteristic c;

            public {
               this.c = var2;
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object var1) {
               Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               int var3;
               if ((var3 = this.a) != 0) {
                  if (var3 != 1) {
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  ResultKt.throwOnFailure(var1);
               } else {
                  ResultKt.throwOnFailure(var1);
                  this.a = 1;
                  if (DelayKt.delay(100L, this) == var2) {
                     return var2;
                  }
               }

               BluetoothGatt var4;
               if ((var4 = BluetoothDeviceWrapper.this.mBluetoothGatt) == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("mBluetoothGatt");
                  var4 = null;
               }

               var4.writeCharacteristic(this.c);
               return Unit.INSTANCE;
            }

            @NotNull
            public final Continuation create(@Nullable Object var1, @NotNull Continuation var2) {
               <undefinedtype> var10002 = this;
               BluetoothDeviceWrapper var3 = BluetoothDeviceWrapper.this;
               return new <anonymous constructor>(var10002.c, var2);
            }

            @Nullable
            public final Object a(@NotNull CoroutineScope var1, @Nullable Continuation var2) {
               return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
            }
         }.<init>(var2, (Continuation)null);
         BuildersKt.launch$default(var10000, var10001, (CoroutineStart)null, var3, 2, (Object)null);
         return true;
      } else {
         MsgLogger.e("BLE_VVV internalAt writeCharacteristic is null");
         return false;
      }
   }

   public final void sendATCommand(@NotNull Set var1) {
      Intrinsics.checkNotNullParameter(var1, "command");
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         this.getMAtQueue().add(var2);
      }

      this.internalSendATCommand();
   }

   public final boolean pauseSend() {
      this.mPauseSend = true;
      return true;
   }

   public final boolean continueSend() {
      this.mPauseSend = false;
      return true;
   }

   public final void stopSend() {
      Job var1;
      if ((var1 = this.mSendJob) != null) {
         if (var1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSendJob");
            var1 = null;
         }

         DefaultImpls.cancel$default(var1, (CancellationException)null, 1, (Object)null);
         this.mPauseSend = false;
      }

   }

   @SuppressLint({"MissingPermission"})
   public final void createBond() {
      MsgLogger.e("BLE createBond => " + this.device.createBond());
   }

   public final native byte cacChecksum(@NotNull byte[] var1, int var2);

   @NotNull
   public final String component1() {
      return this.mAddress;
   }

   @NotNull
   public final BluetoothDevice component2() {
      return this.device;
   }

   @NotNull
   public final Context component3() {
      return this.mContext;
   }

   @NotNull
   public final ConnectCallback component4() {
      return this.connectCallback;
   }

   @NotNull
   public final BluetoothDeviceWrapper copy(@NotNull String var1, @NotNull BluetoothDevice var2, @NotNull Context var3, @NotNull ConnectCallback var4) {
      Intrinsics.checkNotNullParameter(var1, "mAddress");
      Intrinsics.checkNotNullParameter(var2, "device");
      Intrinsics.checkNotNullParameter(var3, "mContext");
      Intrinsics.checkNotNullParameter(var4, "connectCallback");
      return new BluetoothDeviceWrapper(var1, var2, var3, var4);
   }

   @NotNull
   public String toString() {
      return "BluetoothDeviceWrapper(mAddress=" + this.mAddress + ", device=" + this.device + ", mContext=" + this.mContext + ", connectCallback=" + this.connectCallback + ')';
   }

   public int hashCode() {
      return ((this.mAddress.hashCode() * 31 + this.device.hashCode()) * 31 + this.mContext.hashCode()) * 31 + this.connectCallback.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof BluetoothDeviceWrapper)) {
         return false;
      } else {
         BluetoothDeviceWrapper var2 = (BluetoothDeviceWrapper)var1;
         if (!Intrinsics.areEqual(this.mAddress, var2.mAddress)) {
            return false;
         } else if (!Intrinsics.areEqual(this.device, var2.device)) {
            return false;
         } else if (!Intrinsics.areEqual(this.mContext, var2.mContext)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.connectCallback, var2.connectCallback);
         }
      }
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"},
      d2 = {"Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$Companion;", "", "()V", "CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID", "Ljava/util/UUID;", "kotlin.jvm.PlatformType", "FACP_UUID", "GAP_SERVICE_UUID", "GATT_DEVICE_INFO_UUID", "HAVE_AUTH", "", "MAC_UUID", "NOTIFY_UUID", "SERVICE_UUID", "TAG", "", "WRITE_UUID", "FeasyBlueLibrary_release"}
   )
   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }

   @Metadata(
      mv = {1, 7, 1},
      k = 1,
      xi = 48,
      d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0007"},
      d2 = {"Lcom/feasycom/ble/bean/BluetoothDeviceWrapper$ConnectCallback;", "", "failure", "", "deviceWrapper", "Lcom/feasycom/ble/bean/BluetoothDeviceWrapper;", "success", "FeasyBlueLibrary_release"}
   )
   public interface ConnectCallback {
      void success(@NotNull BluetoothDeviceWrapper var1);

      void failure(@NotNull BluetoothDeviceWrapper var1);
   }

   // $FF: synthetic class
   @Metadata(
      mv = {1, 7, 1},
      k = 3,
      xi = 48
   )
   public class WhenMappings {
      // $FF: synthetic field
      public static final int[] $EnumSwitchMapping$0;

      static {
         int[] var10000 = new int[Type.values().length];
         var10000[Type.CONNECT.ordinal()] = 1;
         var10000[Type.ENABLE_NOTIFICATIONS.ordinal()] = 2;
         var10000[Type.REQUEST_MTU.ordinal()] = 3;
         var10000[Type.READ.ordinal()] = 4;
         var10000[Type.ENCRYPT.ordinal()] = 5;
         var10000[Type.AT.ordinal()] = 6;
         var10000[Type.FACP.ordinal()] = 7;
         var10000[Type.FACP_SHAKE.ordinal()] = 8;
         var10000[Type.DISCONNECT.ordinal()] = 9;
         var10000[Type.OTA.ordinal()] = 10;
         $EnumSwitchMapping$0 = var10000;
      }
   }
}
