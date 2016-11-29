package com.example.hly.notificationdemo;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by hly on 11/27/16.
 */

public class TaskStackNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public static final String TAG = TaskStackNotification.class.getSimpleName();

    public TaskStackNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        builder.setTicker("TaskStackNotification");
        builder.setContentTitle("TaskStackNotification");
        builder.setContentText("TaskStackNotification ContentText");

        try {
            ActivityInfo info = mContext.getPackageManager().
                    getActivityInfo(new ComponentName(mContext, ActivityA.class), 0);
            String parentActivity = info.parentActivityName;
            Log.i(TAG, "parentActivity:" + parentActivity);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        stackBuilder.addParentStack(ActivityA.class);

        stackBuilder.addNextIntent(new Intent(mContext, ActivityA.class));
        stackBuilder.addNextIntent(new Intent(mContext, ActivityB.class));

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);


        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
