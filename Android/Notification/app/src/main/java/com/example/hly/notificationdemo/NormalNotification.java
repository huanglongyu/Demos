package com.example.hly.notificationdemo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hly on 11/23/16.
 */

public class NormalNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {

    public NormalNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        builder.setTicker("NormalNotification");
        builder.setContentTitle("NormalNotification");
        builder.setContentText("NormalNotification ContentText");

        Intent intent = new Intent(mContext, ActivityA.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
