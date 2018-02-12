package com.hly.keyboard;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hly.keyboard.tencen.NumKeyBoardContainerDialogWindow;

import java.lang.reflect.Method;

/**
 * Created by hly on 2017/12/28.
 */

public class KeyBoardEditText extends AppCompatEditText {
    private static final String TAG = "KeyBoardEditText";

    private InputMethodManager inputMethodManager;
    private KeyBoardWindowIMPL impl;

    public KeyBoardEditText(Context context) {
        this(context, null);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setOnFocusChangeListener(new FocusChangeListener(this));
        setNoSystemInputOnEditText(this);
        setOnTouchListener(new OnTouchListener(this));
        impl = new NumKeyBoardContainerDialogWindow(context, this);
//        impl = new NumKeyBoardContainerPopWindow(context, this);

        Log.i(TAG, "KeyBoardEditText: " + this.getRootView().getRootView());
    }

    public void setWindow(Window window) {
        impl.setWindow(window);
    }

    public void setCustomKeyBoardWindowImpl(KeyBoardWindowIMPL impl) {
        this.impl = impl;
    }

    private class OnTouchListener implements View.OnTouchListener {
        private KeyBoardEditText editText;

        public OnTouchListener(KeyBoardEditText editText) {
            this.editText = editText;
        }
        @Override
        public boolean onTouch(View v, final MotionEvent event) {
            boolean result = hideSystemInputKeyBorad();
            Log.i(TAG, "onTouch: " + result);
            if (result) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        impl.show(editText);
                    }
                }, 500);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        impl.show(editText);
                    }
                });
            }
            requestFocus();
            return false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.i(TAG, "onWindowFocusChanged: " + hasWindowFocus + " " + this.hashCode());
        if (!hasWindowFocus) {
            hideSystemInputKeyBorad();
            if (impl.isShowing()) {
                impl.hide();
            }
        }
    }

    private class FocusChangeListener implements OnFocusChangeListener {
        final KeyBoardEditText editText;

        private FocusChangeListener(KeyBoardEditText sipEditText) {
            this.editText = sipEditText;
        }

        public void onFocusChange(View view, boolean hasFocus) {
            Log.i(TAG, "onFocusChange: " + hasFocus + " " + hashCode());
            if (hasFocus) {
                boolean result = hideSystemInputKeyBorad();
                if (result) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            impl.show(editText);
                        }
                    }, 500);
                } else {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            impl.show(editText);
                        }
                    });
                }

            } else {
                impl.hide();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + impl.isShowing());
        if (keyCode == KeyEvent.KEYCODE_BACK && impl.isShowing()) {
            impl.hide();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean hideSystemInputKeyBorad() {
        return inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
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

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }

}
