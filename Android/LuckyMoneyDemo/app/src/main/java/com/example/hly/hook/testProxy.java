package com.example.hly.hook;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hly on 3/5/17.
 */

public class testProxy implements InvocationHandler {
    private static final String TAG = "testProxy";
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i(TAG, "invoke: " + method.getName());
        return null;
    }
}
