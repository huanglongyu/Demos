package com.example.hly.simpledroidplugdemo.hook.AMS;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.hly.simpledroidplugdemo.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by hly on 9/20/16.
 */
public class AMSHookHelper {
    public static void hookAMS(Context c) {
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

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
                    new Class<?>[] { iActivityManagerInterface }, new AMSDynamicProxy(c, rawIActivityManager));
            mInstanceField.set(gDefault, proxy);
            Log.i(MainActivity.TAG, "hook ActivityManagerNative success");
        }catch (IllegalAccessException e) {
            Log.i(MainActivity.TAG, "AMSHookHelper", e);
        }catch (NoSuchFieldException e) {
            Log.i(MainActivity.TAG, "AMSHookHelper", e);
        }catch (ClassNotFoundException e) {
            Log.i(MainActivity.TAG, "AMSHookHelper", e);
        }

        //hook handler
        try {
            Class activityThread = ActivityThread.class;
            Field field = activityThread.getDeclaredField("mH");
            field.setAccessible(true);
            Handler mH = (Handler) field.get(ActivityThread.currentActivityThread());


            Field mCallBackField = Handler.class.getDeclaredField("mCallback");
            mCallBackField.setAccessible(true);
            mCallBackField.set(mH, new ActivityThreadHandlerCallback(mH));
        }catch (NoSuchFieldException e) {
            Log.i(MainActivity.TAG, "AMSHookHelper", e);
        }catch (IllegalAccessException e) {
            Log.i(MainActivity.TAG, "AMSHookHelper", e);
        }

    }
}
