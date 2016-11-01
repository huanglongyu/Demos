package com.example.hly.camerademo.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.hly.camerademo.CameraBase;
import com.example.hly.camerademo.CameraPreview;
import com.example.hly.camerademo.ICaptureCallback;

import java.io.IOException;
import java.util.List;

/**
 * Created by hly on 10/12/16.
 */

public class CameraHelper extends CameraBase implements IPreviewCallback, Handler.Callback {
    private static final String TAG = "CameraHelper";
    private Context mContext;
    private Capture mCapture;
    private Camera mCamera;
    private CameraPreview mPreview;
    private SurfaceHolder mSurfaceHolder;
    private Handler mHandler;
    private SensorManager sm;
    private static final int RE_TRY = 1;
    private static final int CHECK = 2;
    private static final int INTERVAL_TIME = 1500;
    private float mLastX, mLastY, mLastZ;
    private boolean mFoucsed = false, sensorOnce = false, registed = false;


    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                Log.i(TAG, "focus success");
                mFoucsed = true;
                sensorOnce = false;
                mCamera.setOneShotPreviewCallback(null);
            } else {
                Log.i(TAG, "focus failed");
                mFoucsed = false;
                sensorOnce = true;
                mHandler.sendEmptyMessageDelayed(RE_TRY,INTERVAL_TIME);
            }

        }
    };

    public CameraHelper(Context c) {
        mContext = c;
        mCamera = getCameraInstance();

        //to adpter some ROM
        //if not get the permission, action will be null
        if (mCamera == null) {
            return;
        }

        sm = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);

        mCapture = new Capture();

        mPreview = new CameraPreview(c);
        mPreview.setPreViewCallback(this);
        mHandler = new Handler(this);
    }

    SensorEventListener myAccelerometerListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float deltaX  = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);

            if (!mFoucsed && !sensorOnce) {
                sensorOnce = true;
                Log.i(TAG, "start to focus");
                mCamera.autoFocus(mAutoFocusCallback);
                Log.i(TAG, "start to focus end");
            }

            if ((deltaX > .5 || deltaY > .5 || deltaZ > .5) && mFoucsed) {
                Log.i(TAG, "moved the camere");
                mFoucsed = false;
                sensorOnce = false;
            }
            mLastX = x;
            mLastY = y;
            mLastZ = z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case RE_TRY:
                mCamera.autoFocus(mAutoFocusCallback);
                break;
        }
        return false;
    }

    @Override
    protected void setCaputreInfo(int type, String name) {
        mCapture.setCaputreInfo(type, name);
    }

    @Override
    protected void setActionCallback(ICaptureCallback c) {
        if (mCapture != null) {
            mCapture.setCaptureCallback(c);
        } else {
            Log.i(TAG, "have no permisson to access Camera");
        }

    }

    @Override
    protected void doCapture() {
        if (mCamera == null) {
            Log.i(TAG, "doCapture return");
            return;
        }
        //in case of call autoFocus repeatly
        if (registed) {
            sm.unregisterListener(myAccelerometerListener);
            registed = false;
        }
        mHandler.removeMessages(RE_TRY);


        mCamera.cancelAutoFocus();
        mCamera.takePicture(null, null, mCapture);
    }

    @Override
    protected void doPreView() {
        try {
            if (mCamera == null) {
                Log.i(TAG, "doPreView return");
                return;
            }
            Log.d(TAG, "doPreView");
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
            if (!registed) {
                registed = true;
                sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            }
//            mCamera.autoFocus(mAutoFocusCallback);
        } catch (IOException e) {
            Log.e(TAG, "doPreView error: " + e.getMessage());
        }
    }

    @Override
    protected void doPaused() {
        try {
            if (mCamera == null) {
                Log.i(TAG, "doPaused return");
                return;
            }
            Log.i(TAG, "doPaused");
            mCamera.cancelAutoFocus();
            mCamera.stopPreview();
            if (registed) {
                sm.unregisterListener(myAccelerometerListener);
                registed = false;
            }
        } catch (Exception e) {
            Log.e(TAG, "doPaused error: " + e.getMessage());
        }
    }

    @Override
    protected void doReleaseCamera() {
        mHandler.removeMessages(CHECK);
        mHandler.removeMessages(RE_TRY);
        releaseCamera();
    }

    @Override
    protected View getPreView() {
        Log.i(TAG, "getPreView :" + mPreview);
        return mPreview;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated :" + holder);
        mSurfaceHolder = holder;
        Camera.Parameters p = mCamera.getParameters();
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(p);
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
            List<Camera.Size> previewSizes = params.getSupportedPictureSizes();
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
//                Log.i(TAG, "getSupportedPictureSizes:" + size.width + " " + size.height);
                if (screen[0] == size.height && screen[1] == size.width) {
                    picWidth = size.width;
                    picHeight = size.height;
                }
            }

//            //AUTO FOCUS
//            List<String> focusModes = params.getSupportedFocusModes();
////            for (int i = 0; i < focusModes.size(); i++) {
////                Log.d(TAG, "getSupportedFocusModes=" + focusModes.get(i));
////            }
//            if (focusModes.contains(params.FOCUS_MODE_AUTO)) {
//                Log.i(TAG, "set FOCUS_MODE_AUTO");
//                params.setFocusMode(params.FOCUS_MODE_AUTO);
//            }
//
//            //WhiteBalance
//            List<?> whiteMode = params.getSupportedWhiteBalance();
//            if (whiteMode != null
//                    && whiteMode
//                    .contains(android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO)) {
//                Log.i(TAG, "set WHITE_BALANCE_AUTO");
//                params.setWhiteBalance(params.WHITE_BALANCE_AUTO);
//            }
//
            //Flash
//            List<String> flashModes = params.getSupportedFlashModes();
//
//            if (flashModes
//                    .contains(Camera.Parameters.FLASH_MODE_AUTO)) {
//                Log.i(TAG, "set FLASH_MODE_AUTO");
//                params.setFlashMode(params.FLASH_MODE_AUTO);
//            }


            if (picWidth == -1 || picHeight == -1) {
                int halfIndex = previewSizes.size() / 2;
                picWidth = previewSizes.get(halfIndex).width;
                picHeight = previewSizes.get(halfIndex).height;
            }
            Log.i(TAG, "picture size : " + picWidth + ", " + picHeight);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setPictureSize(picWidth, picHeight);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(params);

        } catch (Exception e) {
            Log.i(TAG, "getCameraInstance error", e);
            c = null;
        }
        return c;
    }

}
