package com.example.hly.camerademo.camera;

import android.view.SurfaceHolder;

/**
 * Created by hly on 10/12/16.
 */

public interface IPreviewCallback {
    void surfaceCreated(SurfaceHolder holder);
    void surfaceChanged(SurfaceHolder holder, int format, int width, int height);
    void surfaceDestroyed(SurfaceHolder holder);
}
