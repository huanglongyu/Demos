package com.example.hly.camerademo.camera2;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.hly.camerademo.CameraBase;
import com.example.hly.camerademo.CameraPreview;
import com.example.hly.camerademo.ICaptureCallback;
import com.example.hly.camerademo.camera.IPreviewCallback;

import java.util.Arrays;

/**
 * Created by hly on 10/13/16.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Helper extends CameraBase implements IPreviewCallback{
    private static final String TAG = "Camera2Helper";
    private CameraManager mCameraManager;
    private CameraPreview mCameraPreview;
    private Handler mHandler;
    private String mCameraId;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;


    public Camera2Helper(Context c) {
        mCameraManager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);

        mCameraPreview = new CameraPreview(c);
        mCameraPreview.setPreViewCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated " + mCameraPreview.getWidth() + " ," + mCameraPreview.getHeight() + " holder:");
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        try {
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
            mImageReader = ImageReader.newInstance(mCameraPreview.getWidth(), mCameraPreview.getHeight(),
                    ImageFormat.JPEG,/*maxImages*/7);
//            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mHandler);

            mCameraManager.openCamera(mCameraId, DeviceStateCallback, mHandler);
        } catch (CameraAccessException e) {
            Log.e("linc", "open camera failed." + e.getMessage());
        }
    }

    private CameraDevice.StateCallback DeviceStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            Log.d(TAG, "DeviceStateCallback:camera was opend.");
            mCameraDevice = camera;
            try {
                createCameraCaptureSession();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    private void createCameraCaptureSession() throws CameraAccessException {
        Log.d(TAG, "createCameraCaptureSession");

        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewBuilder.addTarget(mCameraPreview.getSurface());

        mCameraDevice.createCaptureSession(
                Arrays.asList(mCameraPreview.getSurface(), mImageReader.getSurface()),
                mSessionPreviewStateCallback, mHandler);
    }

    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new
            CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {
                    Log.d(TAG, "mSessionPreviewStateCallback onConfigured");
                    mSession = session;
                    try {
                        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        session.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("linc","set preview builder failed."+e.getMessage());
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
    };

//    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback =
//            new CameraCaptureSession.CaptureCallback() {
//
//                @Override
//                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
//                                               TotalCaptureResult result) {
//                    Log.d(TAG,"mSessionCaptureCallback, onCaptureCompleted");
//                    mSession = session;
//                    checkState(result);
//                }
//
//                @Override
//                public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
//                                                CaptureResult partialResult) {
//                    Log.d(TAG,"mSessionCaptureCallback,  onCaptureProgressed");
//                    mSession = session;
//                    checkState(partialResult);
//                }
//
//                private void checkState(CaptureResult result) {
//                }
//
//            };



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void doCapture() {

    }

    @Override
    protected void doPreView() {
    }

    @Override
    protected void doPaused() {

    }

    @Override
    protected View getPreView() {
        return mCameraPreview;
    }

    @Override
    protected void doReleaseCamera() {

    }

    @Override
    protected void setActionCallback(ICaptureCallback c) {

    }

    @Override
    protected void setCaputreInfo(int type, String name) {

    }

    @Override
    protected void toggleFlashLight() {

    }
}
