package com.example.hly.hook;

import android.annotation.TargetApi;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
//import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by hly on 3/1/17.
 */

public class HookHelper {
    private static final String TAG = "HookHelper";

    public static void hookAMS(Context c) {
        try {
            Class<?> activityManagerNativeClass = null;
            activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            // 获取 gDefault 这个字段, 想办法替换它
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
            Object rawIActivityManager = mInstanceField.get(gDefault);

            // 创建一个这个对象的代理对象, 然后替换这个字段, 让我们的代理对象帮忙干活
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManagerInterface}, new IActivityManagerHandler(rawIActivityManager));
            mInstanceField.set(gDefault, proxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void hookUserHande(Context c) {
        //        Log.i(TAG, "Process.myUid:" + Process.myUid());
//
//        try {
//            Class userClass = UserHandle.class;
//            Method[] m =userClass.getDeclaredMethods();
////            for (Method method : m) {
////                Log.i(TAG, "hook: " + method.getName());
////            }
//            Method getAppIdMethod = userClass.getDeclaredMethod("getAppId");
//            getAppIdMethod.setAccessible(true);
//            int i = (int)getAppIdMethod.invoke(null, Process.myUid());
//            Log.i(TAG, "hook: getAppId:" + i);
//        } catch (NoSuchMethodException e) {
//            Log.i(TAG, "hook userClass: NoSuchMethodException", e);
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            Log.i(TAG, "hook userClass: InvocationTargetException", e);
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            Log.i(TAG, "hook userClass: IllegalAccessException", e);
//            e.printStackTrace();
//        }


        int uid = Process.myUid();
        int id = uid % 100000;
        Log.i(TAG, "hookUserHande: " + id);
    }

    @TargetApi(23)
    public static void hookSystemConfig(Context c) {
//        try {
//            Class scClass = Class.forName("com.android.server.SystemConfig");
//            Method getInstance = scClass.getDeclaredMethod("getInstance");
//            getInstance.setAccessible(true);
//            Object sc = getInstance.invoke(null);
//
//            Method getSp = scClass.getDeclaredMethod("getSystemPermissions");
//            getSp.setAccessible(true);
//            SparseArray<ArraySet<String>> set = (SparseArray<ArraySet<String>>)getSp.invoke(sc);
//
//            int size = set.size();
//            for (int i = 0; i < size; i++) {
//                int key = set.keyAt(i);
//                ArraySet<String> list = set.get(key);
//                for (int i1 = 0; i1 < list.size(); i1++) {
//                    Log.i(TAG, "hook: " + key + " " + list.valueAt(i1));
//                }
//                boolean added = list.add("android.permission.GET_INTENT_SENDER_INTENT");
//
//                Log.i(TAG, "hook: after:" + list.size() + " added:" + added);
//            }
//        } catch (ClassNotFoundException e) {
//            Log.i(TAG, "hook: cc", e);
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            Log.i(TAG, "hook: cc", e);
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            Log.i(TAG, "hook: cc", e);
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            Log.i(TAG, "hook: cc", e);
//            e.printStackTrace();
//        }
    }


    public static void hookPMS(Context c) {
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        IPackageManager ipm = ActivityThread.getPackageManager();

        try {
            Class thread = ActivityThread.class;
            Field spmField = thread.getDeclaredField("sPackageManager");
            spmField.setAccessible(true);
            Object realIPM = spmField.get(activityThread);

            Log.i(TAG, "hookPMS: realIPM:" + realIPM + " ipm:" + ipm);

            Class<?> iIPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iIPackageManagerInterface.getClassLoader(),
                    new Class<?>[] { iIPackageManagerInterface }, new PMSDynamicProxy(realIPM));

            spmField.set(activityThread, proxy);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException", e);
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException", e);
            e.printStackTrace();
        }
    }

    public static void hookAppGlobals(Context c) {
        Class globals = AppGlobals.class;

    }

    public static void hook(Context c) {
//        hookUserHande(c);
//        hookAMS(c);
//        hookSystemConfig(c);
        hookPMS(c);
    }

}
