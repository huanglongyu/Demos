package com.example.hly.luckymoneydemo;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by hly on 12/22/16.
 */

public class HongbaoService extends AccessibilityService {
    private static final String TAG = "longyu";

    private boolean luckMoneyItemClicked = false;
    private boolean luckMoneyGetClicked = false;

    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";//"微信红包";
    private static final String WECHAT_VIEW_OTHERS_CH = "领取红包";


    private AccessibilityNodeInfo rootNodeInfo;
    private MyHandler mMyHandler = new MyHandler();

    private static class MyHandler extends Handler{}

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        goToChatScreen(event);
        clickLuckMoneyItem(event);
        clickToGetMoney(event);
    }

    private void clickToGetMoney(AccessibilityEvent event) {
        AccessibilityNodeInfo node = findOpenButton(this.rootNodeInfo);
        if (node != null && "android.widget.Button".equals(node.getClassName()) && !luckMoneyGetClicked) {
            Log.i(TAG, "clickToGetMoney: " + node);
            luckMoneyGetClicked = true;
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    private void clickLuckMoneyItem(AccessibilityEvent event) {
        this.rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            Log.e(TAG, "openLuckMoney , rootNodeInfo is null ,return");
            return;
        }
        AccessibilityNodeInfo last = getTheLastNode(WECHAT_VIEW_OTHERS_CH);
        if (last != null && !luckMoneyItemClicked) {
            luckMoneyItemClicked = true;
            Log.i(TAG, "clickLuckMoneyItem: " + last);
            last.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

    }

    private void goToChatScreen(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                if (!TextUtils.isEmpty(event.getText().toString())) {
                    String tx = event.getText().toString();
                    if (tx.contains(WECHAT_NOTIFICATION_TIP)) {
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
    }

    private AccessibilityNodeInfo getTheLastNode(String... texts) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            if (text == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) return null;
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                }
            }
        }
        return lastNode;
    }

    private AccessibilityNodeInfo findOpenButton(AccessibilityNodeInfo node) {
        if (node == null)
            return null;

        //非layout元素
        if (node.getChildCount() == 0) {
            if ("android.widget.Button".equals(node.getClassName())) {
                return node;
            } else {
                return null;
            }
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo button;
        for (int i = 0; i < node.getChildCount(); i++) {
            button = findOpenButton(node.getChild(i));
            if (button != null)
                return button;
        }
        return null;
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt: ");
    }
}
