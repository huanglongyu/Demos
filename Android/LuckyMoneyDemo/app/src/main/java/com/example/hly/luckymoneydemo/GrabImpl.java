package com.example.hly.luckymoneydemo;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by hly on 1/8/17.
 */

public interface GrabImpl {
    void doGrab(AccessibilityEvent events, String activityName);

    interface Notification {
        void doGrab(android.app.Notification notification);
    }
}
