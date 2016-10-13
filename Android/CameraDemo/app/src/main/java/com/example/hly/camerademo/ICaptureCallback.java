package com.example.hly.camerademo;

/**
 * Created by hly on 10/12/16.
 */

public interface ICaptureCallback {
    String CAPTURE_TIPS= "tips";

    void captureSucceed(String thumbnailPath);
    void captureFailed(int reason);
    void captureProcess();
}
