package com.example.hly.luckymoneydemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.example.hly.common.Utils;

/**
 * Created by hly on 2/20/17.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MonitorNotificationService extends NotificationListenerService {
    private static final String TAG = "MonitorNotification";
    private SharedPreferences sp;
    private String fileName = "MonitorNotificationService";
    private String KEY_HAD_DESTROIED = "had_destroied";


    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String QQ_NOTIFICATION_TIP = "[QQ红包]";

    private static final String QQ_PACKAGE= "com.tencent.mobileqq";
    private static final String WX_PACKAGE= "com.tencent.mm";

    private NotificationImpl mNotificationImpl;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        mNotificationImpl = new NotificationImpl(this);

        boolean had_Destroied = sp.getBoolean(KEY_HAD_DESTROIED, true);
        if (had_Destroied) {
            sp.edit().putBoolean(KEY_HAD_DESTROIED, false).commit();
        }
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
        Log.i(TAG, "onListenerHintsChanged : " + hints);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sp.edit().putBoolean(KEY_HAD_DESTROIED, true).commit();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted:" + Process.myUid() + " " + Process.myPid());
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();


        if (notification == null || TextUtils.isEmpty(packageName)) {
            Log.e(TAG, "notification:" + notification + " return");
            return;
        }

        CharSequence ticker = notification.tickerText;

        if (ticker == null) {
            Log.e(TAG, "ticker:" + ticker + " return");
            return;
        }

        boolean isTrigger = false;
        String tikerString = ticker.toString();
        if ((packageName.equals(QQ_PACKAGE) && tikerString.contains(QQ_NOTIFICATION_TIP)) ||
                (packageName.equals(WX_PACKAGE) && tikerString.contains(WECHAT_NOTIFICATION_TIP))) {
            Log.i(TAG, "going to doGrab : " + isTrigger + " " + mNotificationImpl);
            //andorid 7.0 not support
            if (Build.VERSION.SDK_INT >= 24) {
                isTrigger = true;
            } else {
                if (Utils.isLuckMoney(notification)) {
                    isTrigger = true;
                } else {
                    isTrigger = false;
                }
            }
        }

        if (mNotificationImpl != null && isTrigger) {
            Log.i(TAG, "doGrab");
            mNotificationImpl.doGrab(notification);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationRemoved");
    }
}