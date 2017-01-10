package com.example.hly.WX;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.hly.common.Utils;
import com.example.hly.luckymoneydemo.GrabImpl;

import java.util.HashMap;

/**
 * Created by hly on 1/8/17.
 */

public class WXImple implements GrabImpl {

    private static final String TAG = "WXImple";

    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_OTHERS_HB = "领取红包";
    private static final String WECHAT_SELF_HB = "查看红包";

    private static final String WECHAT_DETAILS_EN = "Details";
    private static final String WECHAT_DETAILS_CH = "红包详情";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_BETTER_LUCK_CH = "手慢了";
    private static final String WECHAT_EXPIRES_CH = "已超过24小时";

    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_DETAIL_ACTIVITY = "LuckyMoneyDetailUI";
    private static final String WECHAT_LUCKMONEY_RECEIVE_ACTIVITY = "LuckyMoneyReceiveUI";


    private String mCurrentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    private AccessibilityNodeInfo rootNodeInfo;

    private AccessibilityService mAccessibilityService;

    private boolean isNotifyComing = false;
    private boolean isHBItemClicked = false;

    public WXImple(AccessibilityService service) {
        mAccessibilityService = service;
    }

    @Override
    public void doGrab(final AccessibilityEvent events, String activityName) {
        if (!TextUtils.isEmpty(activityName)) {
            mCurrentActivityName = activityName;
        }

        Log.i(TAG, "doGrab getContentDescription: " + events.getContentDescription());
        if (Utils.goToChatScreen(events, WECHAT_NOTIFICATION_TIP)) {
            Log.e(TAG, "goToChatScreen:");
            isNotifyComing = true;
            isHBItemClicked = false;
            return;
        }

        clickLuckMoneyItem(events);
        judgeResult(events);
    }

    private void judgeResult(AccessibilityEvent event) {

        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Log.e(TAG, "judgeResult return");
            return;
        }

        if (!isNotifyComing) {
            return;
        }

        rootNodeInfo = mAccessibilityService.getRootInActiveWindow();
        if (rootNodeInfo == null) {
            Log.e(TAG, "judgeResult rootNodeInfo is null, return");
            return;
        }

        AccessibilityNodeInfo node = findOpenButton(rootNodeInfo);
        if (node != null
                && "android.widget.Button".equals(node.getClassName())
                && mCurrentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            Log.i(TAG, "judgeResult------------sendVoice-----------------1");
            isNotifyComing = false;
            return;
        }

        if (mCurrentActivityName.contains(WECHAT_LUCKMONEY_DETAIL_ACTIVITY)) {
            Log.i(TAG, "judgeResult: have picked!!!!!!!!!!!!!!!!!!!!!");
            isNotifyComing = false;
            return;
        }

        boolean unLuck = Utils.hasOneOfThoseNodes(rootNodeInfo,
                WECHAT_BETTER_LUCK_CH, WECHAT_DETAILS_CH,
                WECHAT_BETTER_LUCK_EN, WECHAT_DETAILS_EN, WECHAT_EXPIRES_CH);
        if (unLuck &&
                mCurrentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            isNotifyComing = false;
            Log.i(TAG, "judgeResult------------sendVoice-----------------2");
        }
    }

    private void clickLuckMoneyItem(AccessibilityEvent event) {
        this.rootNodeInfo = mAccessibilityService.getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        HashMap map = Utils.getTheLastNodeAndRect(rootNodeInfo, WECHAT_OTHERS_HB, WECHAT_SELF_HB);

        AccessibilityNodeInfo lastHB;
        Object o = map.get(Utils.KEY_LAST_NOTE);
        if (o != null) {
            lastHB = (AccessibilityNodeInfo)o;
        } else {
            return;
        }

        Rect lastHBRect;
        Object o1 = map.get(Utils.KEY_LAST_NOTE_RECT);
        if (o1 != null) {
            lastHBRect = (Rect)o1;
        } else {
            return;
        }

        if (lastHB != null
                && mCurrentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)
                && lastHBRect != null
                && !isHBItemClicked) {

            //do not use lastHBRect.contains(preLastHBRect))
            //cause chat screen is fragement the x coordinate may be different
//            if (lastHBRect.bottom == preLastHBRect.bottom) {
//                Log.i(TAG, "same location hb return, lastHB:" + lastHBRect + " preHB:" + preLastHBRect);
//                return;
//            }
//
//            if (lastHBRect.bottom > preLastHBRect.bottom && preLastHBRect.bottom != 0) {
//                Log.i(TAG, "pre location hb return, lastHB:" + lastHBRect + " preHB:" + preLastHBRect);
//                return;
//            }

            AccessibilityNodeInfo parent = lastHB.getParent();
            if (parent != null) {
                Log.i(TAG, "clickLuckMoneyItem ACTION_CLICK=========lastHB:" + lastHBRect);
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                isHBItemClicked = true;
            }
        } else {
            Log.i(TAG, "can not find hb: " + lastHBRect + " " + mCurrentActivityName);
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
}
