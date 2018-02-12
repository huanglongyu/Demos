package com.hly.keyboard;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hly.keyboard.tencen.KeyboardView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    KeyboardView window;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.showdialog).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_content, null);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(layout).create();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                dialog.setCanceledOnTouchOutside(false);
                lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setDimAmount(0f);
                dialog.show();
                Log.i(TAG, "onClick: " + dialog.getWindow().getAttributes().type);
            }
        });
//        mSwitchingDialog = mDialogBuilder.create();
//        mSwitchingDialog.setCanceledOnTouchOutside(true);
//        mSwitchingDialog.getWindow().setType(
//                WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG);
//        mSwitchingDialog.getWindow().getAttributes().privateFlags |=
//                WindowManager.LayoutParams.PRIVATE_FLAG_SHOW_FOR_ALL_USERS;
//        mSwitchingDialog.getWindow().getAttributes().setTitle("Select input method");
//        updateImeWindowStatusLocked();
//        mSwitchingDialog.show();
//        final EditText editText = (EditText) findViewById(R.id.edit);

//        Button button = (Button) findViewById(R.id.inputbutton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager mIMM = (InputMethodManager)getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, 16));
//                editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, 16));
//
//
//                Log.i(TAG, "intput onClick");
//            }
//        });
//
//        final KInputView kView = (KInputView) findViewById(R.id.kinput);
//        Button button2 = (Button) findViewById(R.id.inputbutton2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager mIMM = (InputMethodManager)getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                kView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, 16));
//                kView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, 16));
//                Log.i(TAG, "kview intput onClick");
//            }
//        });

//        final MyKeyBoradView keyBoradView = (MyKeyBoradView) findViewById(R.id.keyboardview);
//        keyBoradView.setStrReceiver(editText);
//
//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                keyBoradView.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });

//        window = (MyKeyboardWindow) findViewById(R.id.tencen);
//        window.setXMode(2);
//        window.setInputEditText(editText);
//        setNoSystemInputOnEditText(editText);
//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick");
//                InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                methodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                v.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i(TAG, "show");
//                        KeyBoardPopWindow popWindow = new KeyBoardPopWindow(MainActivity.this, editText);
//                        popWindow.show(MainActivity.this.getWindow().getDecorView());
//                    }
//                }, 200);
//            }
//        });
    }

    public void showChangeLangDialog(View v) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                View layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_content, null);
//        View layout = inflater.inflate(R.layout.dialog_content, null);
//                AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setView(layout).setTitle("13123").create();
//        EditText editText = new EditText(this);
//        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).setTitle("231").create();
//        dialog.show();
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        dialog.getWindow().setContentView(layout);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(layout);
//        dialog.show();

//        EditText editText1 = layout.findViewById(R.id.dialogEdit);
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_content);
//        dialog.show();
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.dialog_content, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText edt = (EditText) dialogView.findViewById(R.id.dialogEdit);
//
//        dialogBuilder.setTitle("Custom dialog");
//        dialogBuilder.setMessage("Enter text below");
//        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        Log.i(TAG, "onConfigurationChanged: " + newConfig.keyboard);
//        super.onConfigurationChanged(newConfig);
//    }
//    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
//        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && window.getVisibility() == View.VISIBLE) {
//            window.setVisibility(View.GONE);
//            return true;
//        }
//        boolean dispatchKeyEvent = super.dispatchKeyEvent(keyEvent);
//        return dispatchKeyEvent;
//    }
}
