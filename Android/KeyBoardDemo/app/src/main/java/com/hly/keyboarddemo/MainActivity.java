package com.hly.keyboarddemo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hly.keyboard.KeyBoardEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fullScreen).setOnClickListener(this);
        findViewById(R.id.dialog).setOnClickListener(this);
        findViewById(R.id.dialogActivity).setOnClickListener(this);
        findViewById(R.id.dialogFragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent();
        switch (v.getId()) {
            case R.id.fullScreen:
                in.setClass(this, FullScreen.class);
                break;
            case R.id.dialog:
                showEditDialog();
                return;
            case R.id.dialogActivity:
                in.setClass(this, DialogActivity.class);
                break;
            case R.id.dialogFragment:
                showDialogFragment();
                return;
        }
        startActivity(in);
    }

    private void showDialogFragment() {
        EditTextDialogFragment fragment = new EditTextDialogFragment();
        fragment.show(getSupportFragmentManager(), "id");
    }

    private void showEditDialog() {
        new EditDialog(this).show();
    }

    private class EditDialog extends Dialog {
        Window window;

        public EditDialog(@NonNull Context context) {
            super(context);
            this.window = getWindow();
            setContentView(R.layout.dialog_edit);
            KeyBoardEditText editText = findViewById(R.id.dialogEdit);
            editText.setWindow(window);
        }
    }

}
