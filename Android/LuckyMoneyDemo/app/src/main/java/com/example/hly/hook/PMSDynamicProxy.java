package com.example.hly.hook;

import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hly on 3/1/17.
 */

public class PMSDynamicProxy implements InvocationHandler {
    private static final String TAG = "PMS";
    private Object real;

    public PMSDynamicProxy(Object o) {
        real = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i(TAG, "invoke: " + method.getName());
        if (method.getName().equals("checkUidPermission")) {
            Log.i(TAG, "invoke: return " + PackageManager.PERMISSION_GRANTED);
            return PackageManager.PERMISSION_GRANTED;
        }
        return method.invoke(real,args);
    }
}
