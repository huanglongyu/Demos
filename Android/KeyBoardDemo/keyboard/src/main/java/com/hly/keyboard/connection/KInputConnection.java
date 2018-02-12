package com.hly.keyboard.connection;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

/**
 * Created by hly on 2017/12/18.
 */

public class KInputConnection extends BaseInputConnection {
    private static final String TAG = "KInputConnection";

    public KInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        Log.i(TAG, "sendKeyEvent: " + event);
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DEL:
                if (event.getAction()== KeyEvent.ACTION_UP
                        && onCommitTextListener != null) {
                    onCommitTextListener.onDeleteText();
                }
                break;
        }
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        Log.i(TAG, "commitText: " + text + " " + newCursorPosition);
        if (onCommitTextListener != null) {
            onCommitTextListener.commitText(text, newCursorPosition);
        }
        return true;
    }


    private OnCommitTextListener  onCommitTextListener;
    public void setOnCommitTextListener(OnCommitTextListener onCommitTextListener) {
        this.onCommitTextListener = onCommitTextListener;
    }
    public interface OnCommitTextListener {
        boolean commitText(CharSequence text, int newCursorPosition);
        void onDeleteText();
    }

}
