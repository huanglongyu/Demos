package com.example.hly.luckymoneydemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Process;

import com.example.hly.common.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private boolean isAccessibilityService = false;
    private boolean isNotifyEnable = false;

    private Button bt, bt2;

    private TestBroad mTestBroad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: " + Process.myUid() + " " + Process.myPid());


        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.switchButton);
        bt.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.notify_switchButton);
        bt2.setOnClickListener(this);


        mTestBroad = new TestBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mTestBroad, filter);

        checkAndRebind();
    }

    private void checkAndRebind() {
        isNotifyEnable = Utils.isNotifyServiceEnable(this);

        final ComponentName monitorComponent = new ComponentName(this,  MonitorNotificationService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean isRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null ) {
            Log.e(TAG, "runningServices is null, return");
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(monitorComponent)) {
                if (service.pid == Process.myPid()) {
                    isRunning = true;
                }
            }
        }
        if (isRunning) {
            Log.i(TAG, "runningServices is running, return");
            return;
        }

        if (isNotifyEnable && !isRunning) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PackageManager packageManager = getPackageManager();
                    packageManager.setComponentEnabledSetting(monitorComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    packageManager.setComponentEnabledSetting(monitorComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Log.i(TAG, "do rebind");
                }
            }, 2000);
        } else {
            Log.i(TAG, "isNotifyEnable:" + isNotifyEnable + " isRunning:" + isRunning);
        }
    }

    private class TestBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Uri uri = intent.getData();
            if (uri == null) {
                Log.e(TAG, "onReceive: uri is null");
                return;
            }

            String pkgName = uri.getSchemeSpecificPart();
            if (pkgName == null) {
                Log.e(TAG, "onReceive: pkgName is null");
                return;
            }

            final int enabled = context.getPackageManager().getApplicationEnabledSetting(pkgName);
            String name = getPackageName();
            if (name.equals(pkgName)) {
                Log.e(TAG, pkgName + " (enabled:" + enabled + ")     uri: " + uri);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTestBroad);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtonState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchButton:
                if (!isAccessibilityService) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
                break;
            case R.id.notify_switchButton:
                if (!isNotifyEnable) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    startActivity(intent);
                }
                break;
        }
    }

    private void updateButtonState() {
        isAccessibilityService = Utils.isAccessibilityServiceEnabled(this);
        if (isAccessibilityService) {
            bt.setText("AccessibilityService opened");
        } else {
            bt.setText("click to open AccessibilityService");
        }
        isNotifyEnable = Utils.isNotifyServiceEnable(this);
        if (isNotifyEnable) {
            bt2.setText("notify opened");
        } else {
            bt2.setText("click to open notify");
        }
    }
}
