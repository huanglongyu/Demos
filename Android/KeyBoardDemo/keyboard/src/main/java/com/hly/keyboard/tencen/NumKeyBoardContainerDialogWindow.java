package com.hly.keyboard.tencen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.hly.keyboard.KeyBoardWindowIMPL;
import com.hly.keyboard.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hly on 2017/12/18.
 */

public class NumKeyBoardContainerDialogWindow extends Dialog implements KeyBoardWindowIMPL {
    private static final String TAG = "NumKeyBoardContainerWin";
    private boolean mTakesFocus = false;
    private final Rect mBounds = new Rect();
    private int keyboardHeight, screenHeight, screenWidth;
    private int offsetY = 0;

    private ViewGroup contentView;
    private Rect contentRect = new Rect();

    public NumKeyBoardContainerDialogWindow(Context context, EditText editText) {
        super(context, R.style.Theme_Light_NoTitle_Dialog);
        setNoSystemInputOnEditText(editText);
        KeyboardView keyboardView = new KeyboardView(context);
//        keyboardView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        keyboardView.setInputEditText(editText);

        keyboardHeight = context.getResources().getDimensionPixelSize(R.dimen.keyboard_height);
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        initDockWindow();
        setContentView(keyboardView);

        FrameLayout decorView = (FrameLayout) getDecorView(context);
        contentView = decorView.findViewById(android.R.id.content);
        contentView.getWindowVisibleDisplayFrame(contentRect);
        Log.i(TAG, "onAttachedToWindow: " + contentView);
    }

    private void initDockWindow() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();


        lp.gravity = Gravity.BOTTOM;
        updateWidthHeight(lp);

        getWindow().setAttributes(lp);

        int windowSetFlags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int windowModFlags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        if (!mTakesFocus) {
            windowSetFlags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            windowSetFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            windowModFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }

        getWindow().setFlags(windowSetFlags, windowModFlags);
    }

    private void updateWidthHeight(WindowManager.LayoutParams lp) {
        if (lp.gravity == Gravity.TOP || lp.gravity == Gravity.BOTTOM) {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getWindow().getDecorView().getHitRect(mBounds);

        if (isWithinBoundsNoHistory(ev, mBounds)) {
            return super.dispatchTouchEvent(ev);
        } else {
            MotionEvent motionEvent = clampNoHistory(ev, mBounds);
            boolean handled = super.dispatchTouchEvent(motionEvent);
            motionEvent.recycle();
            return handled;
        }
    }

    private MotionEvent clampNoHistory(MotionEvent event, Rect rect) {
        try {
            Method method = event.getClass().getDeclaredMethod("clampNoHistory", float.class, float.class, float.class, float.class);
            method.setAccessible(true);
            return (MotionEvent) method.invoke(event, rect.left, rect.top, rect.right - 1, rect.bottom - 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isWithinBoundsNoHistory(MotionEvent event, Rect rect) {
        try {
            Method method = event.getClass().getDeclaredMethod("isWithinBoundsNoHistory", float.class, float.class, float.class, float.class);
            method.setAccessible(true);
            return (boolean) method.invoke(event, rect.left, rect.top, rect.right - 1, rect.bottom - 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void setWindow(Window window) {
        contentView = (ViewGroup) window.getDecorView();
        contentView.getWindowVisibleDisplayFrame(contentRect);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (offsetY != 0 && contentView != null) {
            contentView.scrollBy(0, -offsetY);
        }
        Log.i(TAG, "onDetachedFromWindow");
    }

    @Override
    public void show(View anchro) {
        if (isShowing()) {
            Log.i(TAG, "is showing, return");
            return;
        }

        int[] location = new int[2];
        anchro.getLocationOnScreen(location);
//        Log.i(TAG, "contentRect: " + contentRect.height() + " " + contentRect.top + " " + contentRect.bottom);
//        Log.i(TAG, "edit text Y: " + location[1] + " screenHeight:" + screenHeight + " keyboardHeight:" + keyboardHeight + " contentView:" + contentView);
        if (screenHeight - location[1] < keyboardHeight && contentView != null) {
            Log.i(TAG, "anchro view is hidded, scroll the content");
            offsetY = keyboardHeight - screenHeight + location[1] + anchro.getHeight() + 50;
            contentView.scrollBy(0, offsetY);

        }
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = screenWidth;
        getWindow().setAttributes(lp);
    }

    private ViewGroup getDecorView(Context context) {
        return (ViewGroup) scanActivity(context).getWindow().getDecorView();
    }

    @Override
    public void hide() {
        super.dismiss();
    }

    private void setNoSystemInputOnEditText(EditText editText) {
        if (Build.VERSION.SDK_INT < 11) {
            editText.setInputType(0);
            return;
        }
        Class cls = EditText.class;
        try {
            Method method = cls.getMethod("setShowSoftInputOnFocus", new Class[]{Boolean.TYPE});
            method.setAccessible(false);
            method.invoke(editText, new Object[]{Boolean.valueOf(false)});
        } catch (NoSuchMethodException e) {
            try {
                Method method2 = cls.getMethod("setSoftInputShownOnFocus", new Class[]{Boolean.TYPE});
                method2.setAccessible(false);
                method2.invoke(editText, new Object[]{Boolean.valueOf(false)});
            } catch (Throwable e2) {
                editText.setInputType(0);
            }
        } catch (Throwable e22) {
        }
    }


    private Activity scanActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else {
            return scanActivity(((ContextThemeWrapper) context).getBaseContext());
        }
    }
}
