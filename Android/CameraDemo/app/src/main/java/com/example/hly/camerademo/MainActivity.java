package com.example.hly.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hly.camerademo.camera.Capture;

public class MainActivity extends Activity implements View.OnClickListener {
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
                boolean hasCameraPermisson = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean hasStoragePermisson = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (hasCameraPermisson && hasStoragePermisson) {
                    goToCameraActivity();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                break;
        }
    }

    private void goToCameraActivity() {
        Intent in = new Intent(this, CameraActivity.class);
        in.putExtra(ICaptureCallback.CAPTURE_TIPS, "ID Card");
        startActivityForResult(in, CAPUTRE_ID_CARD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToCameraActivity();
            } else {
                Toast.makeText(MainActivity.this, "have no permission", Toast.LENGTH_SHORT).show();
            }
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
