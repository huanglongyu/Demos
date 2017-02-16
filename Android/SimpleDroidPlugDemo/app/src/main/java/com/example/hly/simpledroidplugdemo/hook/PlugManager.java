package com.example.hly.simpledroidplugdemo.hook;

import android.content.Context;

/**
 * Created by hly on 9/20/16.
 */
public class PlugManager {
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static  String getHostApk() {
        return  mContext.getApplicationInfo().sourceDir;
    }
}
