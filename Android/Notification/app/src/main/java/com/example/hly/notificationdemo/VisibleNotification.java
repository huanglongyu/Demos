package com.example.hly.notificationdemo;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hly on 11/28/16.
 */

public class VisibleNotification extends NormalNotification implements MainActivity.NotificationCompatImpl {
    public VisibleNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        //if u want this work , u must have to do below setting
        //1. set lockscreen as a PIN or pattern lock
        //2. change "Show all notification content " to "Hide sensitive notification content"
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        }

        // VISIBILITY_SECRET do not show any notification
        // VISIBILITY_PRIVATE only show icon and title
        super.showNotification();
    }
}
