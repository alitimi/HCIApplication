package com.noweaj.android.pupildetection.opencv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class OpenCVCamera implements CameraBridgeViewBase.CvCameraViewListener2 {

    public interface OpenCVCameraListener{
        public void onCameraViewStarted(int width, int height);
        public void onCameraViewStopped();
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame);
    }

    private OpenCVCameraListener mListener;

    public OpenCVCamera(OpenCVCameraListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mListener.onCameraViewStarted(width, height);
    }

    @Override
    public void onCameraViewStopped() {
        mListener.onCameraViewStopped();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mListener.onCameraFrame(inputFrame);
        return null;
    }
}
