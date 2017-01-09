package com.example.hly.QQ;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.hly.common.Utils;
import com.example.hly.luckymoneydemo.GrabImpl;

/**
 * Created by hly on 1/8/17.
 */

public class QQImple implements GrabImpl {
    private static final String TAG = "QQImple";
    private static final String QQ_NOTIFICATION_TIP = "[QQ红包]";

    @Override
    public void doGrab(AccessibilityEvent events, String activityName) {
        if (Utils.goToChatScreen(events, QQ_NOTIFICATION_TIP)) {
            Log.i(TAG, "doGrab: sendVoice");
        }
    }
}
