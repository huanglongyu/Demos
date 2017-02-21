package com.example.hly.luckymoneydemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.hly.common.Utils;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "longyu";
    private boolean isAccessibilityService = false;
    private boolean isNotifyEnable = false;

    private Button bt, bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.switchButton);
        bt.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.notify_switchButton);
        bt2.setOnClickListener(this);
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
