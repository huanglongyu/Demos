package com.example.myapplication;

import android.app.Notification;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

/**
 * Created by hly on 1/13/17.
 */

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        LinearLayout pareten = new LinearLayout(this);
        View view = notification.contentView.apply(this, pareten);

        Drawable drawable = view.getBackground();
        Log.i(TAG, "drawable is " + (drawable==null?"null":"" + ((ColorDrawable)drawable).getColor()));
//        int color = ((ColorDrawable)view.getBackground()).getColor();
//        Log.i(TAG, "onNotificationPosted: " + notification + " " + notification.extras + " !!!!!!!!!:" + color);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Log.i(TAG, "rankingMap: " + rankingMap);

    }
}
