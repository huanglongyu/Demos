package com.example.hly.notificationdemo;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hly on 11/24/16.
 */

public class CustomNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public CustomNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        Notification notification;
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewBitmap(
                R.id.notification_large_icon,
                BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        remoteViews.setTextViewText(R.id.notification_content, "" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentconten");
        remoteViews.setTextViewText(R.id.notification_title, "title");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        remoteViews.setTextViewText(R.id.notification_time, format.format(new Date()));
        builder.setContent(remoteViews);
        //we can do not use setContent in N version
        //use setCustomContentView intead is also ok

        RemoteViews remoteViewBig = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification_big);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = getNotification();
                notification.bigContentView = remoteViewBig;
            } else {
                notification = getNotification();
            }
        } else {
            builder.setCustomBigContentView(remoteViewBig);
            notification = getNotification();
        }

        builder.setContentText("context2");
        builder.setContentTitle("title2");

        manager.notify((int)System.currentTimeMillis(), notification);
    }
}
