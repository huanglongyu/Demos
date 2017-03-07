package com.example.hly.hook;

import android.content.IIntentSender;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hly on 3/5/17.
 */

public class IActivityManagerHandler implements InvocationHandler{
    private static final String TAG = "AMS";
    private Object real;

    public IActivityManagerHandler(Object r) {
        real = r;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getIntentForIntentSender")) {
            Log.i(TAG, "invoke getIntentForIntentSender");
        }

        if (method.getName().equals("getIntentForIntentSender")) {
            Log.i(TAG, "invoke getIntentForIntentSender");
        }
        return method.invoke(real,args);
    }
}
