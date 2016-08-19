package com.example.hly.graphicdemo;

import android.app.ActivityThread;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hly on 8/19/16.
 */
public class Dump {
    private static final String TAG = "DumpDemo";

    public void doDump(Context c) {
        try {
            enableProfile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(c, "dump gfxinfo without \"Draw Process Execute\", cause not enable profile GPU in developer options", Toast.LENGTH_SHORT).show();
        }

        FileDescriptor fd = null;
        File gfxFile = new File("/sdcard/gfx.txt");
        try {
            if (!gfxFile.exists()) {
                gfxFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(gfxFile);
            fos.write(("").getBytes());
            fos.flush();
            fd = fos.getFD();

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            ActivityThread currentActivityThread = (ActivityThread)currentActivityThreadMethod.invoke(null);

            Class<?> application = Class.forName("android.app.ActivityThread$ApplicationThread");
            Method method = application.getDeclaredMethod("dumpGfxInfo",new Class[]{FileDescriptor.class, String[].class});
            method.setAccessible(true);
            method.invoke(currentActivityThread.getApplicationThread() , fd , new String[]{"com.example.hly.graphicdemo"});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void enableProfile() {
        String oldValue = SystemProperties.get("debug.hwui.profile");
        if (oldValue == null || TextUtils.isEmpty(oldValue)) {
            SystemProperties.set("debug.hwui.profile", "true");
        } else if (oldValue.equals("visual_bars") || oldValue.equals("true")) {
            Log.i(TAG, "profile gpu rendeing is enabled");
        } else if (oldValue.equals("false")) {
            Log.i(TAG, "profile gpu rendeing is disabled");
            SystemProperties.set("debug.hwui.profile", "true");
        }
        Log.i(TAG, "old value:" + oldValue);
        Log.i(TAG, "new value:" + SystemProperties.get("debug.hwui.profile"));
    }

}
