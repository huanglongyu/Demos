package com.hly.keyboard.tencen;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.hly.keyboard.KeyBoardWindowIMPL;
import com.hly.keyboard.R;

import java.lang.reflect.Method;

/**
 * Created by hly on 2017/12/18.
 */

public class NumKeyBoardContainerPopWindow extends PopupWindow implements KeyBoardWindowIMPL {
    private static final String TAG = "NumKeyBoardContainerWin";

    public NumKeyBoardContainerPopWindow(Context context, EditText editText) {
        super(context);
        setNoSystemInputOnEditText(editText);
        KeyboardView keyboardView = new KeyboardView(context);
        keyboardView.setInputEditText(editText);

//        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        layoutParams.softInputMode += WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
//        layoutParams.flags += WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        int height = context.getResources().getDimensionPixelSize(R.dimen.keyboard_height);
//        layoutParams.height = height * 2;
//        layoutParams.gravity = Gravity.BOTTOM;

//        getWindow().getDecorView().setBackgroundResource(R.color.colorAccent);
//        getWindow().setAttributes(layoutParams);
//        getWindow().setDimAmount(0.0f);
//        setCanceledOnTouchOutside(false);

        setSoftInputMode(INPUT_METHOD_NEEDED);
        setContentView(keyboardView);
    }

//    public void show (View parent) {
//        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//        showAsDropDown(parent);
//        show();
//    }


    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void setWindow(Window window) {

    }

    @Override
    public void show(View anchro) {
//        Log.i(TAG, "show NumKeyBoardContainerWindow: " + getWindow().getDecorView());
//        super.show();
        showAtLocation(anchro, Gravity.BOTTOM, 0, 0);
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

}
