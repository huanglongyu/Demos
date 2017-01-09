package com.example.hly.luckymoneydemo;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.hly.QQ.QQImple;
import com.example.hly.WX.WXImple;
import com.example.hly.common.Utils;

import java.util.List;

/**
 * Created by hly on 12/22/16.
 */

public class HongbaoService extends AccessibilityService {
    private static final String TAG = "HongbaoService";

    private static final String QQ_PACKAGE= "com.tencent.mobileqq";
    private static final String WX_PACKAGE= "com.tencent.mm";

    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";

    private static final String WECHAT_OTHERS_HB = "领取红包";
    private static final String WECHAT_SELF_HB = "查看红包";

    private static final String WECHAT_DETAILS_EN = "Details";
    private static final String WECHAT_DETAILS_CH = "红包详情";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_BETTER_LUCK_CH = "手慢了";
    private static final String WECHAT_EXPIRES_CH = "已超过24小时";

    private AccessibilityNodeInfo rootNodeInfo;
    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_DETAIL_ACTIVITY = "LuckyMoneyDetailUI";
    private static final String WECHAT_LUCKMONEY_RECEIVE_ACTIVITY = "LuckyMoneyReceiveUI";

    private Rect preLastHBRect = new Rect();
    private Rect lastHBRect;

    private GrabImpl IMPL;
    private WXImple mWXImple;
    private QQImple mQQImple;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected: ");
        mWXImple = new WXImple(HongbaoService.this);
        mQQImple = new QQImple();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    private String cover(int value){
        String a;
        switch (value) {
            case 2048:
                a = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            case 32:
                a = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case 64:
                a = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            default:
                a = value + "";
        }
        return a;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        String name = Utils.getCurrentActivityName(event);
        if (packageName.equals(QQ_PACKAGE)) {
            mQQImple.doGrab(event, name);
        } else if (packageName.equals(WX_PACKAGE)) {
            mWXImple.doGrab(event, name);
        }
//        Log.e(TAG, "onAccessibilityEvent " + cover(event.getEventType()));
//
//        setCurrentActivityName(event);
//
//        if(goToChatScreen(event)) {
//            return;
//        }
//
//        clickLuckMoneyItem(event);
////        clickToGetMoney(event);
//        judgeResult(event);
    }

    private void clickToGetMoney(AccessibilityEvent event) {
        AccessibilityNodeInfo node = findOpenButton(this.rootNodeInfo);
        if (node != null && "android.widget.Button".equals(node.getClassName())) {
            Log.i(TAG, "clickToGetMoney: " + node);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    private void judgeResult(AccessibilityEvent event) {

        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            return;
        }

        rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        AccessibilityNodeInfo node = findOpenButton(this.rootNodeInfo);
        if (node != null && "android.widget.Button".equals(node.getClassName())) {
            Log.i(TAG, "judgeResult------------sendVoice-----------------1");
            return;
        }

        if (currentActivityName.contains(WECHAT_LUCKMONEY_DETAIL_ACTIVITY)) {
            Log.i(TAG, "judgeResult: have picked!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        boolean unLuck = this.hasOneOfThoseNodes(
                WECHAT_BETTER_LUCK_CH, WECHAT_DETAILS_CH,
                WECHAT_BETTER_LUCK_EN, WECHAT_DETAILS_EN, WECHAT_EXPIRES_CH);
        if (unLuck &&
                currentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            Log.i(TAG, "judgeResult------------sendVoice-----------------2");
        }
    }

    private void clickLuckMoneyItem(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
//            Log.e(TAG, "clickLuckMoneyItem return " + cover(event.getEventType()));
            return;
        }
        this.rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
//            Log.e(TAG, "clickLuckMoneyItem return, rootNodeInfo is null ,return");
            return;
        }
        AccessibilityNodeInfo lastHB = getTheLastNode(WECHAT_OTHERS_HB, WECHAT_SELF_HB);

        if (lastHB != null
                && currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)) {

            //do not use lastHBRect.contains(preLastHBRect))
            //cause chat screen is fragement the x coordinate may be different
            if (lastHBRect.bottom == preLastHBRect.bottom) {
                Log.i(TAG, "clickLuckMoneyItem: return, same location hb" + lastHBRect + " " + preLastHBRect);
                return;
            }

            if (lastHBRect.bottom >= preLastHBRect.bottom && preLastHBRect.bottom != 0) {
                Log.i(TAG, "clickLuckMoneyItem: return, pre location hb " + lastHBRect + " " + preLastHBRect);
                return;
            }

            AccessibilityNodeInfo parent = lastHB.getParent();
            if (parent != null) {
//                if (this.signature.generateSignature(lastHB, "")) {
                    Log.i(TAG, "clickLuckMoneyItem ACTION_CLICK=========");
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    preLastHBRect = lastHBRect;
//                }

            } else {
//                Log.i(TAG, "parent is null, " + currentActivityName);
            }
        } else {
//            Log.i(TAG, "can not find hb!!!!!!!!!!!!, " + currentActivityName + " "
//                    + (lastHB == null ? "null" : "not null"));
        }
    }

    private boolean goToChatScreen(AccessibilityEvent event) {
        boolean isReturn = false;
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                isReturn = true;
                List<CharSequence> list = event.getText();
                if (list == null) {
                    break;
                }
                String tx = list.toString();
                if (!TextUtils.isEmpty(tx)) {
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
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                List<CharSequence> list2 = event.getText();
//                if (list2 == null) {
//                    break;
//                }
//                String tip = list2.toString();
//                if (!tip.contains(WECHAT_NOTIFICATION_TIP)) {
//                    Log.i(TAG, "watchNotifications Not a hongbao: " + tip);
//                    return true;
//                }
//                break;
        }
        return isReturn;
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
                    lastHBRect = bounds;
                }
            }
        }
        return lastNode;
    }

    private boolean hasOneOfThoseNodes(String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) continue;

//            if (rootNodeInfo == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);
            if (nodes != null && !nodes.isEmpty()) return true;
        }
        return false;
    }


    private void setCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            getPackageManager().getActivityInfo(componentName, 0);
            currentActivityName = componentName.flattenToShortString();
        } catch (PackageManager.NameNotFoundException e) {
            currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
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
