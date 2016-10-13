package com.example.hly.camerademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hly on 10/13/16.
 */

public class Util {
    private static final String TAG = "Util";

    public static void saveThumbnail(byte[] data, File thumbanil) throws IOException {
        //compress first
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 50;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Log.i(TAG, "saveThumbnail first compress : " + baos.toByteArray().length / 1024 + "k");
        //300k
        while (baos.toByteArray().length / 1024 > 300) {
            baos.reset();
            quality -= 10;
            if (quality < 0) {
                quality = 0;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            Log.i(TAG, "saveThumbnail continue compress: " + baos.toByteArray().length / 1024 + "k");
        }
        int targetWidth , targetHeight;
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        if (width > 800) {
            targetWidth = 800;
            float scale = width / 800;
            Log.i(TAG, "saveThumbnail scale:" + scale);
            targetHeight = Math.round(height / scale);
        } else {
            targetWidth = (int)width;
            targetHeight = (int)height;
        }
        Log.i(TAG, "saveThumbnail " + width + " " + height + " targetWidth:" + targetWidth + " targetHeight:" + targetHeight);

        //save thumbnail
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, targetWidth, targetHeight);
        FileOutputStream outputStream = new FileOutputStream(thumbanil);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
    }

    public static void saveOriginal(byte[] data, File pictureFile) throws IOException{
        FileOutputStream fos = new FileOutputStream(pictureFile);
        fos.write(data);
        fos.flush();
        fos.close();
    }
}
