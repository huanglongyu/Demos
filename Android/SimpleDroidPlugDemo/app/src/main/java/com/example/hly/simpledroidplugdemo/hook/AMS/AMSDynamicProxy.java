package com.example.hly.simpledroidplugdemo.hook.AMS;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hly.simpledroidplugdemo.MainActivity;
import com.example.hly.simpledroidplugdemo.StubActivity;
import com.example.hly.simpledroidplugdemo.hook.DirHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hly on 9/20/16.
 */
public class AMSDynamicProxy implements InvocationHandler{
    private Object real;
    private Context context;

    public AMSDynamicProxy(Context c, Object o) {
        real = o;
        context = c;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Log.i(MainActivity.TAG, "AMSDynamicProxy invoke : " + method.getName());
        if ("startActivity".equals(method.getName())) {
            Intent raw;
            int index = 0;

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
//            Log.i(MainActivity.TAG, "AMSDynamicProxy raw intent index : " + index);
            raw = (Intent) args[index];

            Intent newIntent = new Intent();
            // 这里包名直接写死,如果再插件里,不同的插件有不同的包  传递插件的包名即可
//            String targetPackage = "com.example.hly.droidplugtest";

            // 这里我们把启动的Activity临时替换为 StubActivity
            ComponentName componentName = new ComponentName(context.getPackageName(), StubActivity.class.getCanonicalName());
            newIntent.setComponent(componentName);

            // 把我们原始要启动的TargetActivity先存起来
            newIntent.putExtra(DirHelper.EXTRA_TARGET_INTENT, raw);

            // 替换掉Intent, 达到欺骗AMS的目的
            args[index] = newIntent;

            Log.d(MainActivity.TAG, "hook startActivity success, hookedIntent:" + newIntent);
            return method.invoke(real, args);

        }

        return method.invoke(real,args);
    }
}
