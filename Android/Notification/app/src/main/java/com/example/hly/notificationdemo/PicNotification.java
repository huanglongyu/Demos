package com.example.hly.notificationdemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hly on 11/24/16.
 */

public class PicNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public PicNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        builder.setTicker("PicNotification");
        builder.setContentTitle("PicNotification");
        builder.setContentText("PicNotificationPicNotificationPicNotification");
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle("BigContentTitle")
                        .setSummaryText("SummaryText")
                        .bigPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bigpic))
                        .bigLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bigpic)));

        manager.notify((int) System.currentTimeMillis(), getNotification());
    }
}
