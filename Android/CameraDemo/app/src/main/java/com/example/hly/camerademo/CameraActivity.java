package com.example.hly.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hly.camerademo.camera.CameraHelper;
import com.example.hly.camerademo.camera.Capture;

public class CameraActivity extends Activity implements View.OnClickListener, ICaptureCallback {
    private static final String TAG = MainActivity.TAG;
    private CameraBase action;
    private View mCaptureView;
    private View mBack;
    private FrameLayout previewLayout;
    private boolean hasCameraPermisson = false;

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
        //to adpter some ROM has permisson request
        if (hasCameraPermisson) {
            action.doPreView();
        } else {
            previewLayout.removeAllViews();
            previewLayout.setBackgroundDrawable(new ColorDrawable(0xFFFF));
            ((TextView) findViewById(R.id.capture_tips)).setText("Have no permisson");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasCameraPermisson) {
            action.doPaused();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasCameraPermisson) {
            action.doReleaseCamera();
        }
    }

    private void initViews() {
        mCaptureView = findViewById(R.id.capture);
        mCaptureView.setOnClickListener(this);

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(this);

        String tips = getIntent().getStringExtra(ICaptureCallback.CAPTURE_TIPS);
        TextView tv = (TextView) findViewById(R.id.capture_tips);
//        tv.setText(tips);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            action = new CameraHelper(this);
        } else {
            action = new CameraHelper(this);
        }

        action.setActionCallback(this);
        previewLayout = (FrameLayout) findViewById(R.id.preview_layout);
        View v = action.getPreView();
        if (v != null) {
            hasCameraPermisson = true;
            previewLayout.addView(v);
        } else {
            hasCameraPermisson = false;
        }
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
                mCaptureView.setClickable(false);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
