package com.example.hly.notificationdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by hly on 11/28/16.
 */

public class FloatNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public FloatNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(mContext, "ur device is not support float style, use stand instead", Toast.LENGTH_SHORT).show();
        }

        builder.setTicker("FloatNotification");
        builder.setContentTitle("FloatNotification title");
        builder.setContentText("FloatNotification ContentText");

        //must set vibrate or ringtong with high/max priority
        builder.setVibrate(new long[0]);
        builder.setPriority(Notification.PRIORITY_HIGH);
        //or set full screen intent
        //builder.setFullScreenIntent(pendingIntentA, true);


        Intent intentA = new Intent(mContext, ActivityA.class);
        PendingIntent pendingIntentA = PendingIntent.getActivity(mContext, 0, intentA,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentB = new Intent(mContext, ActivityB.class);
        PendingIntent pendingIntentB = PendingIntent.getActivity(mContext, 0, intentB,
                PendingIntent.FLAG_UPDATE_CURRENT);



        builder.addAction(R.mipmap.ic_launcher, "ActivityA", pendingIntentA);
        builder.addAction(R.mipmap.ic_launcher, "ActivityB", pendingIntentB);

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
