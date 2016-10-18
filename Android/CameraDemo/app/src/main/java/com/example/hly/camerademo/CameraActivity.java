package com.example.hly.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hly.camerademo.camera.CameraHelper;
import com.example.hly.camerademo.camera.Capture;
import com.example.hly.camerademo.camera2.Camera2Helper;

public class CameraActivity extends Activity implements View.OnClickListener, ICaptureCallback {
    private static final String TAG = MainActivity.TAG;
    private CameraBase action;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        action.doPreView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        action.doPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        action.doReleaseCamera();
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.capture);
        mTextView.setOnClickListener(this);

        String tips = getIntent().getStringExtra(ICaptureCallback.CAPTURE_TIPS);
        TextView tv = (TextView) findViewById(R.id.capture_tips);
        tv.setText(tips);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            action = new CameraHelper(this);
        } else {
            action = new CameraHelper(this);
        }

        action.setActionCallback(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.preview_layout);
        frameLayout.addView(action.getPreView());
    }

    private void capture(int type, String name) {
        action.setCaputreInfo(type, name);
        action.doCapture();
    }

    @Override
    public void captureSucceed(String path) {
        Intent in = new Intent();
        in.putExtra(Capture.THUMBNAIL_PATH, path);
        setResult(RESULT_OK, in);
        finish();
    }

    @Override
    public void captureFailed(int reason) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void captureProcess() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture:
                capture(Capture.ID_TYPE, "ID_Card");
                mTextView.setClickable(false);
                break;
        }
    }
}
