package com.hly.keyboard.connection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by hly on 2017/12/18.
 */

public class KInputView extends AppCompatEditText {
    private static final String TAG = "KInputView";

    private StringBuilder content = new StringBuilder();
    private KInputConnection inputConnection;
    private TextPaint paint;
    private InputMethodManager inputMethodManager;
    public KInputView(Context context) {
        this(context, null);
    }
    public KInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 设置可以接受到焦点
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        // 获取 InputMethodManager
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        paint.setTextSize(textSize);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出、关闭输入法
//                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }



    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        Log.i(TAG, "onCreateInputConnection: ");
        return getInputConnection(editorInfo);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
        // 使用 StaticLayout 来 draw
//        new StaticLayout(content, paint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.5f, 0, false).draw(canvas);
//        super.onDraw(canvas);
//    }

    private InputConnection getInputConnection(EditorInfo editorInfo) {
        if (inputConnection != null) {
            return inputConnection;
        }
        inputConnection = new KInputConnection(this, false);
        inputConnection.setOnCommitTextListener(new KInputConnection.OnCommitTextListener() {
            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                content.append(text);
                invalidate();
                return false;
            }
            @Override
            public void onDeleteText() {
                if (content.length() <= 0) {
                    return;
                }
                content.deleteCharAt(content.length() - 1);
                invalidate();
            }
        });
        return inputConnection;
    }
}
