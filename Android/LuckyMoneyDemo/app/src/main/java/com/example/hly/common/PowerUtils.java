package com.example.hly.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by hly on 2/20/17.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PowerUtils {
    private static final String TAG = "PowerUtils";

    public static void unlockScreen(Context context) {
        try {
            KeyguardManager km = (KeyguardManager) context
                    .getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock(TAG);
            kl.disableKeyguard();
            Log.i(TAG, "unlockScreen: ");
        } catch (Exception e) {
            Log.e(TAG, "unlockScreen: ", e);
        }
    }

    public static void lockScreen(Activity activity) {
        DevicePolicyManager dpm = (DevicePolicyManager) activity
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName cn = new ComponentName(activity, LockReceiver.class);
        if (dpm.isAdminActive(cn)) {// 判断是否有权限(激活了设备管理器)
            dpm.lockNow();// 直接锁屏
        } else {
            activeManager(activity, cn);// 激活设备管理器获取权限
        }
    }

    public static boolean isScreenLocked(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return pm.isInteractive();
        } else {
            return pm.isScreenOn();
        }
    }

    private static void activeManager(Activity activity, ComponentName cn) {
        // 使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        activity.startActivity(intent);
    }

    public static void lightScreen(Context context) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
            wl.acquire();
            wl.release();
            Log.i(TAG, "lightScreen");
        } catch (Exception e) {
            Log.w(TAG, "lightScreen", e);
        }
    }

    public static class LockReceiver extends DeviceAdminReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive");
            super.onReceive(context, intent);
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            Log.i(TAG, "激活使用");
            super.onEnabled(context, intent);
        }

        @Override
        public void onDisabled(Context context, Intent intent) {
            Log.i(TAG, "取消激活");
            super.onDisabled(context, intent);
        }

    }
}
