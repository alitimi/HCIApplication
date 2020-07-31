package com.noweaj.android.pupildetection.core.opencv;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

public class CustomCameraView extends JavaCameraView implements Camera.PictureCallback {
    public CustomCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public CustomCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takePicture(String filename){

        mCamera.takePicture();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
}
