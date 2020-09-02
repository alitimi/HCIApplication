package com.noweaj.android.pupildetection.main;

import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding3.view.RxView;
import com.noweaj.android.pupildetection.core.opencv.OpencvApi;
import com.noweaj.android.pupildetection.core.opencv.OpencvNative;
import com.noweaj.android.pupildetection.data.CascadeData;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainPresenter implements MainContract.Presenter, CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View mView;

    public MainPresenter(MainContract.View mView){
        this.mView = mView;
    }

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void subscribe() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void observeButton(Button button, int flag) {
        Disposable disposable = RxView.clicks(button)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(data -> {
                    switch (flag){
                        case 0: // register
                            break;
                        case 1: // verify
                            break;
                        case 2: // delete
                            break;
                        case 3: // cancel
                            break;
                        case 4: // save
                            break;
                        case 5: // settings
                            mView.startSettingsActivity();
                            break;
                        default:
                            break;
                    }
                }, error -> {
                    Log.d(TAG, error.getLocalizedMessage());
                });
        mCompositeDisposable.add(disposable);
    }

    // CameraBridgeViewBase.CvCameraViewListener2
    @Override
    public CameraBridgeViewBase.CvCameraViewListener2 getCvCameraViewListener(){
        return this;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat matInput = inputFrame.rgba();
        Mat matModified = Imgproc.getRotationMatrix2D(new Point(matInput.cols()/2, matInput.rows()/2), 90, 1);
        Imgproc.warpAffine(matInput, matInput, matModified, matInput.size());

        int[][] detectedFace = OpencvApi.detectFrontalFace(matInput);
        if(detectedFace.length < 1) {
            // detected no face
            mView.updateCurrentStatus(-1, "Face not detected");
            return matInput;
        }

        int[][] detectedEyes = OpencvApi.detectEyes(matInput, detectedFace);





//        nativeMethod.ConvertRGBtoGray(matInput.getNativeObjAddr(), matInput.getNativeObjAddr());
//        Core.flip(matInput, matInput, 1);
//        Core.rotate(matInput, matInput, Core.ROTATE_90_CLOCKWISE);
//        OpencvNative.DetectFrontalFace(CascadeData.cascade_frontalface, matInput.getNativeObjAddr(), matInput.getNativeObjAddr());
//        OpencvNative.DetectPupil(CascadeData.cascade_frontalface, CascadeData.cascade_eyes, matInput.getNativeObjAddr(), matInput.getNativeObjAddr());
//        OpencvNative.DetectEyes(CascadeData.cascade_frontalface, CascadeData.cascade_eyes, matInput.getNativeObjAddr(), matInput.getNativeObjAddr());
        return matInput;
    }
}
