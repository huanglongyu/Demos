package com.example.hly.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hly.camerademo.camera.Capture;

public class MainActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "MainActivity";
    private static final int CAPUTRE_ID_CARD = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Button bt = (Button) findViewById(R.id.go_to_capture);
        bt.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.preview_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_to_capture:
                Intent in = new Intent(this, CameraActivity.class);
                in.putExtra(ICaptureCallback.CAPTURE_TIPS, "ID Card");
                startActivityForResult(in, CAPUTRE_ID_CARD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPUTRE_ID_CARD:
                showIDCard(resultCode, data);
                break;
        }
    }

    private void showIDCard(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(Capture.THUMBNAIL_PATH);
            //force update
            imageView.setImageURI(null);
            imageView.setImageURI(Uri.parse(filePath));
            //TODO: upload pic to internet
        } else {
            Log.e(TAG, "showIDCard faild");
        }
    }
}
