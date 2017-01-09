package com.example.hly.luckymoneydemo;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "longyu";
    private AccessibilityManager accessibilityManager;
    private boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        Button bt = (Button) findViewById(R.id.switchButton);
        bt.setOnClickListener(this);

        isEnabled = isServiceEnabled();
        if (isEnabled) {
            bt.setText("opened");
        } else {
            bt.setText("click to open");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchButton:
                if (!isEnabled) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            Log.i(TAG, "isServiceEnabled: :" + info.getId() + " " + getPackageName() + ".HongbaoService");
            if (info.getId().equals(getPackageName() + "/.HongbaoService")) {
                return true;
            }
        }
        return false;
    }
}
