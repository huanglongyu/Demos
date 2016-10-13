package com.example.hly.camerademo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by hly on 10/12/16.
 */

public abstract class CameraBase {

    protected abstract void doCapture();
    protected abstract void doPreView();
    protected abstract void doPaused();
    protected abstract View getPreView();
    protected abstract void doReleaseCamera();
    protected abstract void setActionCallback(ICaptureCallback c);
    protected abstract void setCaputreInfo(int type, String name);

    /** Check if this device has a camera */
    protected boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    //retrun width / height
    protected int[] getScreen(Context c) {
        if (c instanceof Activity) {
            Activity activity = (Activity)c;
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            int[] data = new int[2];
            data[0] = 0;
            data[1] = 0;
            @SuppressWarnings("rawtypes")
            Class cl;
            try {
                cl = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = cl.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                data[0] = dm.widthPixels;
                data[1] = dm.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
                data[0] = 0;
                data[1] = 0;
            }

            if (data[0] <= 0 || data[1] <= 0) {
                data[0] = activity.getWindowManager().getDefaultDisplay().getWidth();
                data[1] = activity.getWindowManager().getDefaultDisplay().getHeight();
            }
            if (data[0] > data[1]) {
                data[0] = data[0] + data[1];
                data[1] = data[0] - data[1];
                data[0] = data[0] - data[1];
            }
            return data;
        }
        return  null;
    }
}
