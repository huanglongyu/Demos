package com.example.hly.luckymoneydemo;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
//import android.view.IWindowManager;
//import android.view.WindowManagerPolicy;

import com.example.hly.common.PowerUtils;
import com.example.hly.common.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hly on 2/20/17.
 */

public class NotificationImpl implements GrabImpl.Notification {
    private static final String TAG = "longyu";
    private Context mContext;
    private KeyguardManager km;

    public NotificationImpl(Context c) {
        mContext = c;
        km = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void doGrab(Notification notification) {
        try {
            Log.i(TAG, "doGrab init:  " + km.isKeyguardLocked());

            if (!PowerUtils.isScreenOn(mContext)) {
                Log.i("longyu", "lightScreen");
                PowerUtils.lightScreen(mContext.getApplicationContext());
            } else {
                Log.i("longyu", "no need to lightScreen");
            }

            notification.contentIntent.send();
            Log.i("longyu", "contentIntent.send");

            if (PowerUtils.isScreenLocked(mContext)) {
                Utils.unlock();
//                PowerUtils.unlockScreen(mContext);
            }

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            Log.e("longyu", "CanceledException: ", e);
        } catch (NoSuchMethodException e) {
            Log.i("longyu", "NoSuchMethodException", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.i("longyu", "IllegalAccessException", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.i("longyu", "InvocationTargetException", e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.i("longyu", "ClassNotFoundException", e);
            e.printStackTrace();
        }
    }
}