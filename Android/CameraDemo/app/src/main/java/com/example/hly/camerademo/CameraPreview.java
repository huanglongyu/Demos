package com.example.hly.camerademo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hly.camerademo.camera.IPreviewCallback;

/**
 * Created by hly on 10/11/16.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private IPreviewCallback mIPreviewCallback;

    public CameraPreview(Context context) {
        this(context, null);
    }

    public void setPreViewCallback(IPreviewCallback i) {
        mIPreviewCallback = i;
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public Surface getSurface() {
        return mHolder.getSurface();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mIPreviewCallback != null) {
            mIPreviewCallback.surfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mIPreviewCallback != null) {
            mIPreviewCallback.surfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mIPreviewCallback != null) {
            mIPreviewCallback.surfaceDestroyed(holder);
        }
    }
}
