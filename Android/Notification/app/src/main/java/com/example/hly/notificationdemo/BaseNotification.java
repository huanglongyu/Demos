package com.example.hly.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hly on 11/23/16.
 */

public abstract class BaseNotification {
    protected NotificationCompat.Builder builder;
    protected NotificationManager manager;
    protected Context mContext;

    public BaseNotification(Context context) {
        mContext = context;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
    }

    protected Notification getNotification() {
        return builder.build();
    }
}
