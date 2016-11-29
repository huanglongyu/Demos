package com.example.hly.notificationdemo;

import android.content.Context;
import android.graphics.BitmapFactory;
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
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewBitmap(
                R.id.notification_large_icon,
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo));
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


        RemoteViews remoteViewBig = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification_big);
        builder.setCustomBigContentView(remoteViewBig);


        builder.setContentText("context2");
        builder.setContentTitle("title2");
        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
