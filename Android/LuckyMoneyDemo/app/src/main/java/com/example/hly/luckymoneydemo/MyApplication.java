package com.example.hly.luckymoneydemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.hly.hook.HookHelper;

/**
 * Created by hly on 3/5/17.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG, "attachBaseContext");
        HookHelper.hook(base);
    }
}
