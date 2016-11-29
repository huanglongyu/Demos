package com.example.hly.notificationdemo;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hly on 11/24/16.
 */

public class BigTextNotification extends BaseNotification implements MainActivity.NotificationCompatImpl{

    public BigTextNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        String content = "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "BigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotificationBigTextNotification" +
                "12312312312";
        builder.setTicker("BigTextNotification");
        builder.setContentTitle("BigTextNotification");
        builder.setContentText(content);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));

        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
