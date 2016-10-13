package com.example.hly.camerademo.camera;

import android.hardware.Camera;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.example.hly.camerademo.ICaptureCallback;
import com.example.hly.camerademo.MainActivity;
import com.example.hly.camerademo.Util;

import java.io.File;
import java.io.IOException;

/**
 * Created by hly on 10/11/16.
 */

public class Capture implements Camera.PictureCallback {
    public static final String THUMBNAIL_PATH = "thumbnailPath";
    public static final int FILE_ERROR = 1;
    public static final int STORAGE_ERROR = 2;
    public static final int ID_TYPE = 1;

    private ICaptureCallback mICallback;
    private int type = -1;
    private String name;

    public void setCaptureCallback(ICaptureCallback i) {
        mICallback = i;
    }

    public void setCaputreInfo(int i, String n) {
        type = i;
        name = n;
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {
        final File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            mICallback.captureFailed(FILE_ERROR);
            return;
        }
        Log.i(MainActivity.TAG, "picture File data=" + data.length);
        try {
            Util.saveThumbnail(data, pictureFile);
            Log.i(MainActivity.TAG, "saveThumbnail success");
            mICallback.captureSucceed(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "IOException: " + e.getMessage());
            mICallback.captureFailed(STORAGE_ERROR);
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Transferbox");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(MainActivity.TAG, "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + name + ".jpg");
        if (mediaFile.exists()) {
            boolean deleted = mediaFile.delete();
            Log.d(MainActivity.TAG, "deleted old pic :" + deleted);
        }
        return mediaFile;
    }
}
