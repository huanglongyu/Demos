package com.example.hly.luckymoneydemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.example.hly.common.PowerUtils;

/**
 * Created by hly on 2/20/17.
 */

public class NotificationImpl implements GrabImpl.Notification {
    private Context mContext;

    public NotificationImpl(Context c) {
        mContext = c;
    }

    @Override
    public void doGrab(Notification notification) {
        try {

            if (!PowerUtils.isScreenOn(mContext)) {
                Log.i("longyu", "lightScreen");
                PowerUtils.lightScreen(mContext.getApplicationContext());
            } else {
                Log.i("longyu", "no need to lightScreen");
            }

            notification.contentIntent.send();
            Log.i("longyu", "contentIntent.send");

            if (PowerUtils.isScreenLocked(mContext)) {
                Log.i("longyu", "unlockScreen");
                PowerUtils.unlockScreen(mContext.getApplicationContext());
            } else {
                Log.i("longyu", "no need to unlockScreen");
            }

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            Log.e("longyu", "doGrab: ", e);
        }
    }
}