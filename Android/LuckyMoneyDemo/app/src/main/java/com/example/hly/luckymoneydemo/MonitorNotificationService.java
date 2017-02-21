package com.example.hly.luckymoneydemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
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
        Log.i(TAG, "onCreate: ");
        sp = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        mNotificationImpl = new NotificationImpl(this);

        boolean had_Destroied = sp.getBoolean(KEY_HAD_DESTROIED, true);
        if (had_Destroied) {
            Log.i(TAG, "onCreate: opended");
            sp.edit().putBoolean(KEY_HAD_DESTROIED, false).commit();
        }
//        getContentResolver().registerContentObserver(Settings.Secure.CONTENT_URI, true, cob);
//        cob.onChange(true);
    }

    private ContentObserver cob = new ContentObserver(new Handler()) {
        @Override

        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            boolean isEnable = Utils.isNotifyServiceEnable(MonitorNotificationService.this);
            Log.i(TAG, "service is enabled: " + isEnable);
        }
    };

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: closed");
        super.onDestroy();
        sp.edit().putBoolean(KEY_HAD_DESTROIED, true).commit();
//        getContentResolver().unregisterContentObserver(cob);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();

        if (notification == null || TextUtils.isEmpty(packageName)) {
            return;
        }

        CharSequence ticker = notification.tickerText;

        if (ticker == null) {
            return;
        }

        String tikerString = ticker.toString();
        if ((packageName.equals(QQ_PACKAGE) && tikerString.contains(QQ_NOTIFICATION_TIP)) ||
                (packageName.equals(WX_PACKAGE) && tikerString.contains(WECHAT_NOTIFICATION_TIP))) {

            if (mNotificationImpl != null) {
                Log.i(TAG, "doGrab");
                mNotificationImpl.doGrab(notification);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

}