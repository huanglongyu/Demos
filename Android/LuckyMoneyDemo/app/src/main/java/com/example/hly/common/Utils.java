package com.example.hly.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hly on 1/8/17.
 */

public class Utils {
    private static final String TAG = "Utils";
    public static final String KEY_LAST_NOTE = "last_note";
    public static final String KEY_LAST_NOTE_RECT = "last_note_rect";

    public static String getCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e(TAG, "getCurrentActivityName: return");
            return null;
        }
        ComponentName componentName = new ComponentName(
                event.getPackageName().toString(),
                event.getClassName().toString()
        );

        String currentActivityName = componentName.flattenToShortString();
        return currentActivityName;
    }

    public static boolean goToChatScreen(AccessibilityEvent event, String tips) {
        boolean shouldReturn = false;
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                shouldReturn = true;
                List<CharSequence> list = event.getText();
                if (list == null) {
                    break;
                }
                String tx = list.toString();
                if (!TextUtils.isEmpty(tx)) {
                    if (tx.contains(tips)) {
                        Parcelable parcelable = event.getParcelableData();
                        if (parcelable instanceof Notification) {
                            Notification notification = (Notification) parcelable;
                            try {
                                notification.contentIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
        return shouldReturn;
    }


    public static boolean hasOneOfThoseNodes(AccessibilityNodeInfo rootNodeInfo, String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) continue;

            if (rootNodeInfo == null) continue;

            nodes = rootNodeInfo.findAccessibilityNodeInfosByText(text);
            if (nodes != null && !nodes.isEmpty()) return true;
        }
        return false;
    }

    public static HashMap getTheLastNodeAndRect(AccessibilityNodeInfo rootNodeInfo, String... texts) {
        int bottom = 0;
        HashMap map = new HashMap();
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;
        Rect lastHBRect = null;

        for (String text : texts) {
            if (text == null) continue;

            nodes = rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) return null;
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                    lastHBRect = bounds;
                }
            }
        }
        map.put(KEY_LAST_NOTE, lastNode);
        map.put(KEY_LAST_NOTE_RECT, lastHBRect);
        return map;
    }
}
