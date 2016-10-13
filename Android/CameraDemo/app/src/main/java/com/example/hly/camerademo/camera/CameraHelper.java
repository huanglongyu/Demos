package com.example.hly.camerademo.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.hly.camerademo.CameraBase;
import com.example.hly.camerademo.ICaptureCallback;

import java.io.IOException;
import java.util.List;

/**
 * Created by hly on 10/12/16.
 */

public class CameraHelper extends CameraBase implements IPreviewCallback {
    private static final String TAG = "CameraHelper";
    private Context mContext;
    private Capture mCapture;
    private Camera mCamera;
    private CameraPreview mPreview;
    private SurfaceHolder mSurfaceHolder;

    public CameraHelper(Context c) {
        mContext = c;
        mCamera = getCameraInstance();

        mCapture = new Capture();

        mPreview = new CameraPreview(c);
        mPreview.setPreViewCallback(this);
    }

    @Override
    protected void setCaputreInfo(int type, String name) {
        mCapture.setCaputreInfo(type, name);
    }

    @Override
    protected void setActionCallback(ICaptureCallback c) {
        mCapture.setCaptureCallback(c);
    }

    @Override
    protected void doCapture() {
        if (mCamera == null) {
            Log.d(TAG, "doCapture return");
            return;
        }
        mCamera.takePicture(null, null, mCapture);
    }

    @Override
    protected void doPreView() {
        try {
            if (mCamera == null) {
                Log.d(TAG, "doPreView return");
                return;
            }
            Log.d(TAG, "doPreView");
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "doPreView error: " + e.getMessage());
        }
    }

    @Override
    protected void doPaused() {
        try {
            if (mCamera == null) {
                Log.d(TAG, "doPaused return");
                return;
            }
            Log.d(TAG, "doPaused");
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.e(TAG, "doPaused error: " + e.getMessage());
        }
    }

    @Override
    protected void doReleaseCamera() {
        releaseCamera();
    }

    @Override
    protected View getPreView() {
        Log.d(TAG, "getPreView :" + mPreview);
        return mPreview;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated :" + holder);
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged " + holder);
        if (mSurfaceHolder.getSurface() == null) {
            Log.i(TAG, "surfaceChanged return");
            return;
        }
        mSurfaceHolder = holder;
        doPaused();
        doPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed :" + holder + " do nothing");
    }

    private void releaseCamera() {
        if (mCamera != null) {
            Log.d(TAG, "doReleaseCamera");
            mCamera.release();
            mCamera = null;
        } else {
            Log.i(TAG, "releaseCamera return");
        }
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            releaseCamera();
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance

            Camera.Parameters params = c.getParameters();
            List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
            int picWidth = -1, picHeight = -1;
            int screen[] = getScreen(mContext);
            if (screen == null) {
                c.release();
                c = null;
                return null;
            }
            Log.i(TAG, "getScreen:" + screen[0] + " " + screen[1]);
            for (int i = 0; i < previewSizes.size(); i++) {
                Camera.Size size = previewSizes.get(i);
                Log.i(TAG, "SupportedPreviewSizes:" + size.width + " " + size.height);
                if (screen[0] == size.height && screen[1] == size.width) {
                    picWidth = size.width;
                    picHeight = size.height;
                }
            }
            if (picWidth == -1 || picHeight == -1) {
                int halfIndex = previewSizes.size() / 2;
                picWidth = previewSizes.get(halfIndex).width;
                picHeight = previewSizes.get(halfIndex).height;
            }
            Log.i(TAG, "picture size : " + picWidth + ", " + picHeight);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setPictureSize(picWidth, picHeight);
            c.setParameters(params);
        } catch (Exception e) {
            Log.i(TAG, "getCameraInstance error", e);
            c = null;
        }
        return c;
    }

}
