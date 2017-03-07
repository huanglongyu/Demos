package com.example.hly.common;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

//import com.android.internal.statusbar.IStatusBarService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hly on 1/8/17.
 */

public class Utils {
    private static final String TAG = "tools";
    private static final int MSG_TYPE_WECHAT_LUCKY_MONEY = 0x1A000031; //436207665
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

    public static final void collapseStatusBar() {

//        IStatusBarService a;
//        IStatusBarService mService = IStatusBarService.Stub.asInterface(
//                ServiceManager.getService("statusbar"));
//
//        try {
//            mService.
//        } catch (RemoteException e) {
//            Log.e(TAG, "collapseStatusBar Exception", e);
//            e.printStackTrace();
//        }

        Class serviceManagerClass = null;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService", String.class);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());

            Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[] { retbinder });
            Method collapse;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Log.i(TAG, "collapsePanels");
                collapse = statusBarClass.getMethod("collapsePanels");
            } else {
                Log.i(TAG, "collapse");
                collapse = statusBarClass.getMethod("collapse");
            }
            collapse.setAccessible(true);
            collapse.invoke(statusBarObject);
        } catch (Exception e) {
            Log.e(TAG, "collapseStatusBar Exception", e);
            e.printStackTrace();
        }



//        Log.i(TAG, "collapseStatusBar: ");
//        try {
//            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
//            Method collapse;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                collapse = statusBarManager.getMethod("collapsePanels");
//            } else {
//                collapse = statusBarManager.getMethod("collapse");
//            }
//            collapse.invoke(mService);
//        } catch (Exception e) {
//            Log.e(TAG, "collapseStatusBar Exception", e);
//            e.printStackTrace();
//        }
    }

    public static boolean goToChatScreen(AccessibilityEvent event, String tips) {
        boolean shouldReturn = false;
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
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
                                shouldReturn = true;
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

    public static void unlock() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class global = Class.forName("android.view.WindowManagerGlobal");
        Method method = global.getDeclaredMethod("getWindowManagerService");
        method.setAccessible(true);
        IWindowManager IWindow = (IWindowManager) method.invoke(null);

        Class wmClass = Class.forName("android.view.IWindowManager");
        Method dismiss = wmClass.getDeclaredMethod("dismissKeyguard");
        dismiss.setAccessible(true);
        dismiss.invoke(IWindow);
    }

    public static int test() {
        try {
            String permission = "android.permission.GET_INTENT_SENDER_INTENT";
            Class activity = ActivityManager.class;
            Method method = activity.getMethod("checkComponentPermission", String.class, int.class, int.class, boolean.class);
            int i = (int)method.invoke(null, permission, Process.myUid(), -1, true);
            Log.i(TAG, "test: i:" + i);
            return i;
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "onClick NoSuchMethodException: ", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.i(TAG, "onClick InvocationTargetException: ", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.i(TAG, "onClick IllegalAccessException: ", e);
            e.printStackTrace();
        }
        return -1000;
    }

    public static boolean isLuckMoney(Notification notification) {
        try {
            Notification n = notification.clone();
//            printExtrasLog(n.contentIntent);
            Class pendingIntentClass = PendingIntent.class;
            Method getIntentMethod = pendingIntentClass.getDeclaredMethod("getIntent");
            getIntentMethod.setAccessible(true);
            Intent in = (Intent) getIntentMethod.invoke(n.contentIntent);
            int i = in.getIntExtra("MainUI_User_Last_Msg_Type", -100);
            if (i == MSG_TYPE_WECHAT_LUCKY_MONEY) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "NoSuchMethodException: ", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.i(TAG, "InvocationTargetException: ", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.i(TAG, "IllegalAccessException: ", e);
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNotifyServiceEnable(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
//        Log.i(TAG, "flat: " + flat);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            Log.i(TAG, "isAccessibilityServiceEnabled: :" + info.getId() + " " + context.getPackageName() + ".HongbaoService");
            if (info.getId().equals(context.getPackageName() + "/.HongbaoService")) {
                return true;
            }
        }
        return false;
    }

    private static void printExtrasLog(PendingIntent pi) {
        if (pi == null) {
            return;
        }
        Bundle extras = null;
        if (pi != null) {
            try {
                Method getIntent = pi.getClass().getMethod("getIntent");
                Intent intent = (Intent) getIntent.invoke(pi);
                extras = intent.getExtras();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (extras == null) {
            return;
        }
        Log.i(TAG, " printExtrasLog begin =============================");

        Iterator<String> it = extras.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Log.i(TAG, " extras :key = " + key + " value = " + extras.get(key));
        }
        Log.i(TAG, " printExtrasLog end =============================");
    }
}
